/*
  The MIT License (MIT)

  Copyright (c) 2017 Antonella Botte, Giacomo Marciani and Debora Partigianoni

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:


  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.


  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
 */

package com.acmutv.ontoqa.core.parser;

import com.acmutv.ontoqa.core.exception.LTAGException;
import com.acmutv.ontoqa.core.exception.OntoqaParsingException;
import com.acmutv.ontoqa.core.grammar.Grammar;
import com.acmutv.ontoqa.core.parser.state.ConflictElement;
import com.acmutv.ontoqa.core.semantics.base.statement.Statement;
import com.acmutv.ontoqa.core.semantics.base.term.Variable;
import com.acmutv.ontoqa.core.semantics.sltag.ElementarySltag;
import com.acmutv.ontoqa.core.semantics.sltag.Sltag;
import com.acmutv.ontoqa.core.syntax.SyntaxCategory;
import com.acmutv.ontoqa.core.syntax.ltag.LtagNode;
import com.acmutv.ontoqa.core.syntax.ltag.LtagNodeMarker;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.acmutv.ontoqa.core.parser.EnglishConstructs.isAskSentence;

/**
 * An advanced SLTAG parser.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
@Deprecated
public class SimpleSltagParser implements SltagParser {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSltagParser.class);

  /**
   * Parses {@code sentence} with {@code grammar}.
   * @param sentence the sentence to parse.
   * @param grammar the grammar to parse with.
   * @return the parsed SLTAG.
   * @throws OntoqaParsingException when parsing fails.
   */
  @Override
  public Sltag parse(String sentence, Grammar grammar) throws Exception {
    ParserState state = new ParserState(sentence);

    SltagTokenizer tokenizer = new SimpleSltagTokenizer(grammar, sentence);

    if (isAskSentence(sentence)) {
      LOGGER.info("Set ASK SPARQL interpretation");
      state.setAsk(true);
    } else {
      LOGGER.info("Set SELECT SPARQL interpretation");
      state.setAsk(false);
    }

    while (tokenizer.hasNext()) {
      Token token = tokenizer.next();

      String lexicalPattern = token.getLexicalPattern();
      state.setIdxPrev(token.getPrev());
      List<ElementarySltag> candidates = token.getCandidates();

      if (candidates.isEmpty()) {
        throw new OntoqaParsingException("Cannot find SLTAG for lexical pattern: %s",
            lexicalPattern);
      }

      /* MULTIPLE CANDIDATES PROCESSING */
      if (candidates.size() > 1) {
        LOGGER.debug("Colliding candidates found (idxPrev: {})", state.getIdxPrev());
        handleMultipleCandidates(candidates, state);
      }

      /* QUEUE INSERTIONS */
      if (candidates.size() == 1) {
        Sltag candidate = candidates.get(0);
        if (candidate.isAdjunctable()) {
          LOGGER.debug("Candidate (adjunction) with idxPrev {} :\n{}", state.getIdxPrev(), candidate.toPrettyString());
          state.addAdjunction(candidate, state.getIdxPrev());
        } else if (candidate.isSentence()) {
          LOGGER.debug("Candidate (sentence):\n{}", candidate.toPrettyString());
          if (state.getCurr() != null) {
            throw new Exception("Cannot decide sentence root: multiple root found.");
          }
          state.setCurr(candidate);
        } else {
          LOGGER.debug("Candidate (substitution) with idxPrev {} :\n{}", state.getIdxPrev(), candidate.toPrettyString());
          state.addSubstitution(candidate, state.getIdxPrev());
        }
      }

      /* QUEUE PROCESSING */
      if (state.getCurr() != null) {
        processSubstitutions(state);
        processAdjunctions(state);
      }
      LOGGER.debug("Current SLTAG\n{}", (state.getCurr() != null) ? state.getCurr().toPrettyString() : "NONE");
    }

    if (state.getCurr() == null) {
      throw new Exception("Cannot build SLTAG");
    }

    /* CONFLICTS SOLVING */
    if (!state.getWaitingList().isEmpty()) {
      solveConflicts(state);
    }

    /* ASK/SELECT INTERPRETATION */
    if (state.isAsk()) {
      state.getCurr().getSemantics().setSelect(false);
    } else {
      state.getCurr().getSemantics().setSelect(true);
    }

    LOGGER.debug("Current SLTAG\n{}", state.getCurr().toPrettyString());

    return state.getCurr();
  }

  private static void handleMultipleCandidates(List<ElementarySltag> candidates, ParserState dashboard) {
    Integer idxPrev = dashboard.getIdxPrev();
    WaitingList wlist = dashboard.getWaitingList();
    Iterator<ElementarySltag> iterCandidates = candidates.iterator();

    while (iterCandidates.hasNext()) {
      Sltag candidate = iterCandidates.next();
      if (candidate.isSentence()) {
        if (candidate.isSentence() && idxPrev == null && candidate.isLeftSub()) { // excludes is (affermative) when we are at the first word.
          LOGGER.debug("Excluded colliding sentence-root candidate (found left-sub at the beginning of the sentence):\n{}", candidate.toPrettyString());
          iterCandidates.remove();
        } else if (candidate.isSentence() && idxPrev != null && !candidate.isLeftSub()) { // excludes is (interrogative) when we are in the middle of the sentence.
          LOGGER.debug("Excluded colliding sentence-root candidate (found not left-sub within the sentence):\n{}", candidate.toPrettyString());
          iterCandidates.remove();
        }
      } else if (candidate.isAdjunctable()) {
        LOGGER.debug("Colliding candidate (adjunction):\n{}", candidate.toPrettyString());
        if (wlist.isEmpty()) wlist.add(new ConflictElement());
        wlist.get(wlist.size() - 1).addAdjunction(candidate, idxPrev);
        iterCandidates.remove();
      } else {
        LOGGER.debug("Colliding candidate (substitution):\n{}", candidate.toPrettyString());
        if (wlist.isEmpty()) wlist.add(new ConflictElement());
        wlist.get(wlist.size() - 1).addSubstitution(candidate, idxPrev);
        iterCandidates.remove();
      }
    }
  }

  private static void processSubstitutions(ParserState dashboard) throws LTAGException {
    Integer idxPrev = dashboard.getIdxPrev();
    Sltag curr = dashboard.getCurr();

    Iterator<LtagNode> localSubstitutions = curr.getNodesDFS(LtagNodeMarker.SUB).iterator();
    while (localSubstitutions.hasNext()) {
      LtagNode localTarget = localSubstitutions.next();
      Iterator<Pair<Sltag,Integer>> waitingSubstitutions = dashboard.getSubstitutions().iterator();
      while (waitingSubstitutions.hasNext()) {
        Pair<Sltag,Integer> entry = waitingSubstitutions.next();
        Sltag waitingSubstitution = entry.getKey();
        if (localTarget.getCategory().equals(waitingSubstitution.getRoot().getCategory())) { /* CAN MAKE SUBSTITUTION */
          if (curr.getSemantics().getMainVariable() == null
              && waitingSubstitution.getSemantics().getMainVariable() != null) { /* RECORD A MAIN VARIABLE MISS */
            int pos = (idxPrev != null) ? idxPrev + 1 : 0;
            Variable mainVar = waitingSubstitution.getSemantics().getMainVariable();
            Set<Statement> statements = waitingSubstitution.getSemantics().getStatements(mainVar);
            curr.substitution(waitingSubstitution, localTarget);
            Variable renamedVar = curr.getSemantics().findRenaming(mainVar, statements);
            if (renamedVar != null) {
              Triple<Variable,Variable,Set<Statement>> missedRecord = new MutableTriple<>(mainVar, renamedVar, statements);
              dashboard.getMissedMainVariables().put(pos, missedRecord);
              LOGGER.info("Recorded main variable: pos: {} | mainVar: {} renamed to {} | statements: {} ", pos, mainVar, renamedVar, statements);
            }
          } else {
            curr.substitution(waitingSubstitution, localTarget);
          }
          LOGGER.debug("Substituted {} with:\n{}", localTarget, waitingSubstitution.toPrettyString());
          waitingSubstitutions.remove();
          localSubstitutions = curr.getNodesDFS(LtagNodeMarker.SUB).iterator();
          break;
        }
      }
    }
  }

  private static void processAdjunctions(ParserState dashboard) throws LTAGException {
    List<String> words = dashboard.getWords();
    Sltag curr = dashboard.getCurr();
    Map<Integer,Triple<Variable,Variable,Set<Statement>>> missedMainVariables = dashboard.getMissedMainVariables();

    Iterator<Pair<Sltag,Integer>> waitingAdjunctions = dashboard.getAdjunctions().iterator();
    while (waitingAdjunctions.hasNext()) {
      Pair<Sltag,Integer> entry = waitingAdjunctions.next();
      Sltag toAdjunct = entry.getLeft();
      Integer start = entry.getRight();
      String startLexicalEntry = (start != null) ? words.get(start) : null;
      LtagNode localTarget = curr.firstMatch(toAdjunct.getRoot().getCategory(), startLexicalEntry, null);
      if (localTarget != null) { /* CAN MAKE ADJUNCTION */
        if (curr.getSemantics().getMainVariable() == null &&
            toAdjunct.isLeftAdj() &&
            missedMainVariables.containsKey(start)) { /* INSPECT MAIN VARIABLE MISS */
          int lookup = (start != null) ? start : 0;
          Variable missedMainVar = missedMainVariables.get(lookup).getMiddle();
          LOGGER.warn("Found possible main variable miss at pos {}: {}", lookup, missedMainVar);
          curr.getSemantics().setMainVariable(missedMainVar);
          LOGGER.warn("Main variable temporarily set to: {}", missedMainVar);
          curr.adjunction(toAdjunct, localTarget);
          curr.getSemantics().setMainVariable(null);
          LOGGER.warn("Resetting main variable to NULL");
        } else if (curr.getSemantics().getMainVariable() == null  &&
            toAdjunct.isRightAdj() &&
            missedMainVariables.containsKey((start != null) ? start + 2 : 1)) {
          int lookup = (start != null) ? start + 2 : 1;
          Variable missedMainVar = missedMainVariables.get(lookup).getMiddle();
          LOGGER.warn("Found possible main variable miss at pos {}: {}", lookup, missedMainVar);
          curr.getSemantics().setMainVariable(missedMainVar);
          LOGGER.warn("Main variable temporarily set to: {}", missedMainVar);
          curr.adjunction(toAdjunct, localTarget);
          curr.getSemantics().setMainVariable(null);
          LOGGER.warn("Resetting main variable to NULL");
        } else {
          curr.adjunction(toAdjunct, localTarget);
        }
        LOGGER.debug("Adjuncted {} on {}", toAdjunct.toPrettyString(), localTarget);
        waitingAdjunctions.remove();
      }
    }
  }

  private static void solveConflicts(ParserState dashboard) throws LTAGException {
    List<String> words = dashboard.getWords();
    WaitingList wlist = dashboard.getWaitingList();
    Map<Integer,Triple<Variable,Variable,Set<Statement>>> missedMainVariables = dashboard.getMissedMainVariables();
    Sltag curr = dashboard.getCurr();

    Iterator<ConflictElement> iter = wlist.iterator();
    while (iter.hasNext()) {
      ConflictElement elements = iter.next();
      boolean used = false;

      LOGGER.debug("Examining collissions: substitutions");
      for (Pair<Sltag, Integer> elem : elements.getSubstitutions()) {
        Sltag other = elem.getLeft();
        Integer start = elem.getRight();
        String startLexicalEntry = (start != null) ? words.get(start) : null;
        LOGGER.debug("Collision examination : substitution starting at {} ({}):\n{}", start, startLexicalEntry, other.toPrettyString());
        LtagNode target = curr.firstMatch(other.getRoot().getCategory(), startLexicalEntry, LtagNodeMarker.SUB);
        if (target != null && LtagNodeMarker.SUB.equals(target.getMarker())) {
          LOGGER.debug("Collision examination : substitution : eligible target found {}", target);
          try {
            curr.substitution(other, target);
            LOGGER.debug("Substituted (colliding element) {} with:\n{}", target, other.toPrettyString());
            used = true;
            break;
          } catch (LTAGException exc) {
            LOGGER.warn(exc.getMessage());
          }
        }
      }

      if (used) {
        iter.remove();
        continue;
      } else {
        LOGGER.debug("Cannot find nodes eligible for substitution");
      }

      LOGGER.debug("Examining collissions: adjunctions");
      for (Pair<Sltag, Integer> elem : elements.getAdjunctions()) {
        Sltag toAdjunct = elem.getLeft();
        Integer start = elem.getRight();
        String startLexicalEntry = (start != null) ? words.get(start) : null;
        SyntaxCategory category = toAdjunct.getRoot().getCategory();
        LOGGER.debug("Collision examination : adjunction starting at {} ({}):\n{}", start, startLexicalEntry, toAdjunct.toPrettyString());
        LtagNode localTarget = curr.firstMatch(category, startLexicalEntry, null);
        if (localTarget != null) { /* CAN MAKE ADJUNCTION */
          LOGGER.debug("isLeftAdj: {} | isRightAdj: {}", toAdjunct.isLeftAdj(), toAdjunct.isRightAdj());
          LOGGER.debug("missedMainVariables: {}", missedMainVariables);
          if (curr.getSemantics().getMainVariable() == null &&
              toAdjunct.isLeftAdj() &&
              missedMainVariables.containsKey(start)) { /* INSPECT MAIN VARIABLE MISS */
            int lookup = (start != null) ? start : 0;
            Variable missedMainVar = missedMainVariables.get(lookup).getMiddle();
            LOGGER.warn("Found possible main variable miss at pos {}: {}", lookup, missedMainVar);
            curr.getSemantics().setMainVariable(missedMainVar);
            LOGGER.warn("Main variable temporarily set to: {}", missedMainVar);
            curr.adjunction(toAdjunct, localTarget);
            curr.getSemantics().setMainVariable(null);
            LOGGER.warn("Resetting main variable to NULL");
          } else if (curr.getSemantics().getMainVariable() == null &&
              toAdjunct.isRightAdj() &&
              missedMainVariables.containsKey((start != null) ? start + 2 : 1)) {
            int lookup = (start != null) ? start + 2 : 1;
            Variable missedMainVar = missedMainVariables.get(lookup).getMiddle();
            LOGGER.warn("Found possible main variable miss at pos {}: {}", lookup, missedMainVar);
            curr.getSemantics().setMainVariable(missedMainVar);
            LOGGER.warn("Main variable temporarily set to: {}", missedMainVar);
            curr.adjunction(toAdjunct, localTarget);
            curr.getSemantics().setMainVariable(null);
            LOGGER.warn("Resetting main variable to NULL");
          } else {
            curr.adjunction(toAdjunct, localTarget);
          }
        }
      }
    }
  }

}
