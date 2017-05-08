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
import com.acmutv.ontoqa.core.parser.conflict.Candidate;
import com.acmutv.ontoqa.core.parser.conflict.Conflict;
import com.acmutv.ontoqa.core.parser.conflict.ConflictList;
import com.acmutv.ontoqa.core.parser.state.ConflictElement;
import com.acmutv.ontoqa.core.semantics.base.statement.Statement;
import com.acmutv.ontoqa.core.semantics.base.term.Variable;
import com.acmutv.ontoqa.core.semantics.sltag.ElementarySltag;
import com.acmutv.ontoqa.core.semantics.sltag.Sltag;
import com.acmutv.ontoqa.core.syntax.SyntaxCategory;
import com.acmutv.ontoqa.core.syntax.ltag.LtagNode;
import com.acmutv.ontoqa.core.syntax.ltag.LtagNodeMarker;
import org.apache.commons.lang3.tuple.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.acmutv.ontoqa.core.parser.EnglishConstructs.isAskSentence;

/**
 * An advanced SLTAG parser.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class SimpleSltagParserNew implements SltagParser {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSltagParserNew.class);

  /**
   * Parses {@code sentence} with {@code grammar}.
   * @param sentence the sentence to parse.
   * @param grammar the grammar to parse with.
   * @return the parsed SLTAG.
   * @throws OntoqaParsingException when parsing fails.
   */
  @Override
  public Sltag parse(String sentence, Grammar grammar) throws Exception {
    ParserStateNew state = new ParserStateNew(sentence);

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

      /* QUEUE INSERTIONS (NO COLLIDING CANDIDATES) */
      if (candidates.size() == 1) {
        Sltag candidate = candidates.get(0);
        if (candidate.isAdjunctable()) {
          LOGGER.debug("Candidate (adjunction) with idxPrev {} :\n{}", state.getIdxPrev(), candidate.toPrettyString());
          state.addWaitingAdjunction(candidate, state.getIdxPrev());
        } else if (candidate.isSentence()) {
          LOGGER.debug("Candidate (sentence):\n{}", candidate.toPrettyString());
          if (state.getCurr() != null) {
            throw new Exception("Cannot decide sentence root: multiple root found.");
          }
          state.setCurr(candidate);
        } else {
          LOGGER.debug("Candidate (substitution) with idxPrev {} :\n{}", state.getIdxPrev(), candidate.toPrettyString());
          state.addWaitingSubstitution(candidate, state.getIdxPrev());
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
    if (!state.getConflictList().isEmpty()) {
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

  private static void handleMultipleCandidates(List<ElementarySltag> candidates, ParserStateNew state) {
    Integer idxPrev = state.getIdxPrev();
    ConflictList conflicts = state.getConflictList();
    Iterator<ElementarySltag> iterCandidates = candidates.iterator();

    while (iterCandidates.hasNext()) {
      Sltag candidate = iterCandidates.next();
      if (candidate.isSentence()) { /* SYNTACTICALLY SOLVABLE CONFLICTS */
        if (idxPrev == null && candidate.isLeftSub()) { // excludes is (affermative) when we are at the first word.
          LOGGER.debug("Excluded colliding sentence-root candidate (found left-sub at the beginning of the sentence):\n{}", candidate.toPrettyString());
          iterCandidates.remove();
        } else if (idxPrev != null && !candidate.isLeftSub()) { // excludes is (interrogative) when we are in the middle of the sentence.
          LOGGER.debug("Excluded colliding sentence-root candidate (found not left-sub within the sentence):\n{}", candidate.toPrettyString());
          iterCandidates.remove();
        }
      } else {
        LOGGER.debug("Colliding candidate:\n{}", candidate.toPrettyString());
        conflicts.add(candidate, idxPrev);
        iterCandidates.remove();
      }
    }
  }

  private static void processSubstitutions(ParserStateNew state) throws LTAGException {
    Integer idxPrev = state.getIdxPrev();
    Sltag curr = state.getCurr();

    Iterator<LtagNode> substitutionTargets = curr.getNodesDFS(LtagNodeMarker.SUB).iterator();
    while (substitutionTargets.hasNext()) {
      LtagNode substitutionTarget = substitutionTargets.next();
      Iterator<Candidate> waitingSubstitutionCandidates = state.getWaitSubstitutions().iterator();
      while (waitingSubstitutionCandidates.hasNext()) {
        Candidate waitingSubstitutionCandidate = waitingSubstitutionCandidates.next();
        Sltag substitutionCandidate = waitingSubstitutionCandidate.getSltag();
        if (substitutionTarget.getCategory().equals(substitutionCandidate.getRoot().getCategory())) { /* CAN MAKE SUBSTITUTION */
          if (curr.getSemantics().getMainVariable() == null
              && substitutionCandidate.getSemantics().getMainVariable() != null) { /* RECORD A MAIN VARIABLE MISS */
            int pos = (idxPrev != null) ? idxPrev + 1 : 0;
            Variable mainVar = substitutionCandidate.getSemantics().getMainVariable();
            Set<Statement> statements = substitutionCandidate.getSemantics().getStatements(mainVar);
            curr.substitution(substitutionCandidate, substitutionTarget);
            Variable renamedVar = curr.getSemantics().findRenaming(mainVar, statements);
            if (renamedVar != null) {
              Triple<Variable,Variable,Set<Statement>> missedRecord = new MutableTriple<>(mainVar, renamedVar, statements);
              state.getMissedMainVariables().put(pos, missedRecord);
              LOGGER.info("Recorded main variable: pos: {} | mainVar: {} renamed to {} | statements: {} ", pos, mainVar, renamedVar, statements);
            }
          } else {
            curr.substitution(substitutionCandidate, substitutionTarget);
          }
          LOGGER.debug("Substituted {} with:\n{}", substitutionTarget, substitutionCandidate.toPrettyString());
          waitingSubstitutionCandidates.remove();
          substitutionTargets = curr.getNodesDFS(LtagNodeMarker.SUB).iterator();
          break;
        }
      }
    }
  }

  private static void processAdjunctions(ParserStateNew state) throws LTAGException {
    List<String> words = state.getWords();
    Sltag curr = state.getCurr();
    Map<Integer,Triple<Variable,Variable,Set<Statement>>> missedMainVariables = state.getMissedMainVariables();

    Iterator<Candidate> waitingAdjunctionCandidates = state.getWaitAdjunction().iterator();
    while (waitingAdjunctionCandidates.hasNext()) {
      Candidate waitingAdjunctionCandidate = waitingAdjunctionCandidates.next();
      Sltag adjunctionCandidate = waitingAdjunctionCandidate.getSltag();
      Integer start = waitingAdjunctionCandidate.getPosition();
      String startLexicalEntry = (start != null) ? words.get(start) : null;
      LtagNode localTarget = curr.firstMatch(adjunctionCandidate.getRoot().getCategory(), startLexicalEntry, null);
      if (localTarget != null) { /* CAN MAKE ADJUNCTION */
        if (curr.getSemantics().getMainVariable() == null &&
            adjunctionCandidate.isLeftAdj() &&
            missedMainVariables.containsKey(start)) { /* INSPECT MAIN VARIABLE MISS */
          int lookup = (start != null) ? start : 0;
          Variable missedMainVar = missedMainVariables.get(lookup).getMiddle();
          LOGGER.warn("Found possible main variable miss at pos {}: {}", lookup, missedMainVar);
          curr.getSemantics().setMainVariable(missedMainVar);
          LOGGER.warn("Main variable temporarily set to: {}", missedMainVar);
          curr.adjunction(adjunctionCandidate, localTarget);
          curr.getSemantics().setMainVariable(null);
          LOGGER.warn("Resetting main variable to NULL");
        } else if (curr.getSemantics().getMainVariable() == null  &&
            adjunctionCandidate.isRightAdj() &&
            missedMainVariables.containsKey((start != null) ? start + 2 : 1)) {
          int lookup = (start != null) ? start + 2 : 1;
          Variable missedMainVar = missedMainVariables.get(lookup).getMiddle();
          LOGGER.warn("Found possible main variable miss at pos {}: {}", lookup, missedMainVar);
          curr.getSemantics().setMainVariable(missedMainVar);
          LOGGER.warn("Main variable temporarily set to: {}", missedMainVar);
          curr.adjunction(adjunctionCandidate, localTarget);
          curr.getSemantics().setMainVariable(null);
          LOGGER.warn("Resetting main variable to NULL");
        } else {
          curr.adjunction(adjunctionCandidate, localTarget);
        }
        LOGGER.debug("Adjuncted {} on {}", adjunctionCandidate.toPrettyString(), localTarget);
        waitingAdjunctionCandidates.remove();
      }
    }
  }

  private static void solveConflicts(ParserStateNew state) throws LTAGException {
    List<String> words = state.getWords();
    ConflictList conflictsList = state.getConflictList();
    Map<Integer,Triple<Variable,Variable,Set<Statement>>> missedMainVariables = state.getMissedMainVariables();
    Sltag curr = state.getCurr();

    LOGGER.debug("Conflicts inspection: substitutions");
    Iterator<Integer> conflictPositions = conflictsList.keySet().iterator();
    while (conflictPositions.hasNext()) {
      Integer conflictPosition = conflictPositions.next();
      Conflict conflict = conflictsList.get(conflictPosition);
      Iterator<Candidate> conflictingCandidates = conflict.iterator();

      while (conflictingCandidates.hasNext()) {
        Candidate conflictingCandidate = conflictingCandidates.next();
        Sltag candidate = conflictingCandidate.getSltag();
        Integer position = conflictingCandidate.getPosition();
        if (isFeasibleSubstitution(candidate, position)) {
          String startLexicalEntry = (position != null) ? words.get(position) : null;
          LOGGER.debug("Collision inspection : substitution starting at {} ({}):\n{}", position, startLexicalEntry, candidate.toPrettyString());
          LtagNode target = curr.firstMatch(candidate.getRoot().getCategory(), startLexicalEntry, LtagNodeMarker.SUB);
          try {
            curr.substitution(candidate, target);
            LOGGER.debug("Substituted (colliding candidate) in {} with:\n{}", target, candidate.toPrettyString());
            conflictPositions.remove();
            conflictsList.remove(position);
            break;
          } catch (LTAGException exc) {
            LOGGER.warn(exc.getMessage());
          }
        }
      }
    }

    LOGGER.debug("Conflicts inspection: adjunctions");
    conflictPositions = conflictsList.keySet().iterator();
    while (conflictPositions.hasNext()) {
      Integer conflictPosition = conflictPositions.next();
      Conflict conflict = conflictsList.get(conflictPosition);
      Iterator<Candidate> conflictingCandidates = conflict.iterator();

      while (conflictingCandidates.hasNext()) {
        Candidate conflictingCandidate = conflictingCandidates.next();
        Sltag candidate = conflictingCandidate.getSltag();
        Integer position = conflictingCandidate.getPosition();
        if (isFeasibleAdjunction(candidate, position)) {
          String startLexicalEntry = (position != null) ? words.get(position) : null;
          SyntaxCategory category = candidate.getRoot().getCategory();
          LOGGER.debug("Collision examination : adjunction starting at {} ({}):\n{}", position, startLexicalEntry, candidate.toPrettyString());
          LtagNode localTarget = curr.firstMatch(category, startLexicalEntry, null);
          if (localTarget != null) { /* CAN MAKE ADJUNCTION */
            LOGGER.debug("isLeftAdj: {} | isRightAdj: {}", candidate.isLeftAdj(), candidate.isRightAdj());
            LOGGER.debug("missedMainVariables: {}", missedMainVariables);
            if (curr.getSemantics().getMainVariable() == null &&
                candidate.isLeftAdj() &&
                missedMainVariables.containsKey(position)) { /* INSPECT MAIN VARIABLE MISS */
              int lookup = (position != null) ? position : 0;
              Variable missedMainVar = missedMainVariables.get(lookup).getMiddle();
              LOGGER.warn("Found possible main variable miss at pos {}: {}", lookup, missedMainVar);
              curr.getSemantics().setMainVariable(missedMainVar);
              LOGGER.warn("Main variable temporarily set to: {}", missedMainVar);
              curr.adjunction(candidate, localTarget);
              curr.getSemantics().setMainVariable(null);
              LOGGER.warn("Resetting main variable to NULL");
            } else if (curr.getSemantics().getMainVariable() == null &&
                candidate.isRightAdj() &&
                missedMainVariables.containsKey((position != null) ? position + 2 : 1)) {
              int lookup = (position != null) ? position + 2 : 1;
              Variable missedMainVar = missedMainVariables.get(lookup).getMiddle();
              LOGGER.warn("Found possible main variable miss at pos {}: {}", lookup, missedMainVar);
              curr.getSemantics().setMainVariable(missedMainVar);
              LOGGER.warn("Main variable temporarily set to: {}", missedMainVar);
              curr.adjunction(candidate, localTarget);
              curr.getSemantics().setMainVariable(null);
              LOGGER.warn("Resetting main variable to NULL");
            } else {
              curr.adjunction(candidate, localTarget);
            }
          }
        }
      }
    }
  }

  private static boolean isFeasibleSubstitution(Sltag candidate, Integer position) {
    return !candidate.isAdjunctable();
  }

  private static boolean isFeasibleAdjunction(Sltag candidate, Integer position) {
    return candidate.isAdjunctable();
  }

}
