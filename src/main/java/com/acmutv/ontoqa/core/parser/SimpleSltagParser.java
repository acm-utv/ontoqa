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
import com.acmutv.ontoqa.core.semantics.base.statement.Proposition;
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

import static com.acmutv.ontoqa.core.parser.EnglishConstructs.ASK_TRIGGERS;

/**
 * An advanced SLTAG parser.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class SimpleSltagParser implements SltagParser {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSltagParser.class);

  /**
   * Checks if the sentence is an ASK type sentence.
   * @param sentence the sentence.
   * @return true if {@code sentence} is an ASK type sentence.
   */
  private static boolean isAskSentence(String sentence) {
    String firstWord = sentence.split(" ")[0];
    return (ASK_TRIGGERS.contains(firstWord));
  }

  /**
   * Parses {@code sentence} with {@code grammar}.
   * @param sentence the sentence to parse.
   * @param grammar the grammar to parse with.
   * @return the parsed SLTAG.
   * @throws OntoqaParsingException when parsing fails.
   */
  @Override
  public Sltag parse(String sentence, Grammar grammar) throws Exception {
    ParserDashboard dashboard = new ParserDashboard();
    WaitingList wlist = dashboard.getWaitingList();
    SltagTokenizer tokenizer = new SimpleSltagTokenizer(grammar, sentence);

    final String[] words = sentence.split(" ");
    Sltag curr = null;

    boolean isAskType = isAskSentence(sentence);

    Map<Integer, Triple<Variable,Variable,Set<Statement>>> missedMainVariables = new HashMap<>();

    while (tokenizer.hasNext()) {
      Token token = tokenizer.next();

      String lexicalPattern = token.getLexicalPattern();
      Integer idxPrevLexicalEntry = token.getPrev();
      List<ElementarySltag> candidates = token.getCandidates();

      //String prevLexicalEntry = (idxPrevLexicalEntry == null) ? null : words[idxPrevLexicalEntry];

      if (candidates.isEmpty()) {
        throw new OntoqaParsingException("Cannot find SLTAG for lexical pattern: %s",
            lexicalPattern);
      }

      /* CANDIDATES PROCESSING */

      if (candidates.size() > 1) {
        LOGGER.debug("Colliding candidates found (idxPrevLexicalEntry: {})", idxPrevLexicalEntry);
        Iterator<ElementarySltag> iterCandidates = candidates.iterator();

        while (iterCandidates.hasNext()) {
          Sltag candidate = iterCandidates.next();
          if (candidate.isSentence()) {
            if (candidate.isSentence() && idxPrevLexicalEntry == null && candidate.isLeftSub()) { // excludes is (affermative) when we are at the first word.
              LOGGER.debug("Excluded colliding sentence-root candidate (found left-sub at the beginning of the sentence):\n{}", candidate.toPrettyString());
              iterCandidates.remove();
            } else if (candidate.isSentence() && idxPrevLexicalEntry != null && !candidate.isLeftSub()) { // excludes is (interrogative) when we are in the middle of the sentence.
              LOGGER.debug("Excluded colliding sentence-root candidate (found not left-sub within the sentence):\n{}", candidate.toPrettyString());
              iterCandidates.remove();
            }
          } else if (candidate.isAdjunctable()) {
            LOGGER.debug("Colliding candidate (adjunction):\n{}", candidate.toPrettyString());
            if (wlist.isEmpty()) {
              wlist.add(new ConflictElement());
            }
            wlist.get(wlist.size() - 1).addAdjunction(candidate, idxPrevLexicalEntry);
            iterCandidates.remove();
          } else {
            LOGGER.debug("Colliding candidate (substitution):\n{}", candidate.toPrettyString());
            if (wlist.isEmpty()) {
              wlist.add(new ConflictElement());
            }
            wlist.get(wlist.size() - 1).addSubstitution(candidate, idxPrevLexicalEntry);
            iterCandidates.remove();
          }

        }
      }

      /* QUEUE INSERTIONS */

      if (candidates.size() == 1) {
        Sltag candidate = candidates.get(0);
        if (candidate.isAdjunctable()) {
          LOGGER.debug("Candidate (adjunction) with idxPrev {} :\n{}", idxPrevLexicalEntry, candidate.toPrettyString());
          dashboard.addAdjunction(candidate, idxPrevLexicalEntry);
        } else if (candidate.isSentence()) {
          LOGGER.debug("Candidate (sentence):\n{}", candidate.toPrettyString());
          if (curr != null) {
            throw new Exception("Cannot decide sentence root: multiple root found.");
          }
          curr = candidate;
        } else {
          LOGGER.debug("Candidate (substitution) with idxPrev {} :\n{}", idxPrevLexicalEntry, candidate.toPrettyString());
          dashboard.addSubstitution(candidate, idxPrevLexicalEntry);
        }
      }

      /* QUEUE PROCESSING*/

      if (curr != null) {
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
                int pos = (idxPrevLexicalEntry != null) ? idxPrevLexicalEntry + 1 : 0;
                Variable mainVar = waitingSubstitution.getSemantics().getMainVariable();
                Set<Statement> statements = waitingSubstitution.getSemantics().getStatements(mainVar);
                curr.substitution(waitingSubstitution, localTarget);
                Variable renamedVar = curr.getSemantics().findRenaming(mainVar, statements);
                if (renamedVar != null) {
                  Triple<Variable,Variable,Set<Statement>> missedRecord = new MutableTriple<>(mainVar, renamedVar, statements);
                  missedMainVariables.put(pos, missedRecord);
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

        Iterator<Pair<Sltag,Integer>> waitingAdjunctions = dashboard.getAdjunctions().iterator();
        while (waitingAdjunctions.hasNext()) {
          Pair<Sltag,Integer> entry = waitingAdjunctions.next();
          Sltag toAdjunct = entry.getLeft();
          Integer start = entry.getRight();
          String startLexicalEntry = (start != null) ? words[start] : null;
          LtagNode localTarget = curr.firstMatch(toAdjunct.getRoot().getCategory(), startLexicalEntry, null);
          if (localTarget != null) { /* CAN MAKE ADJUNCTION */
            if (curr.getSemantics().getMainVariable() == null &&
                missedMainVariables.containsKey(start)) { /* INSPECT MAIN VARIABLE MISS */
              Variable missedMainVar = missedMainVariables.get(start).getMiddle();
              LOGGER.warn("Found possible main variable miss at pos {}: {}", start, missedMainVar);
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
      LOGGER.debug("Current SLTAG\n{}", (curr != null) ? curr.toPrettyString() : "NONE");
    }

    if (curr == null) {
      throw new Exception("Cannot build SLTAG");
    }

    /* WAITING LIST: NOT SOLVED CONFLICTS */

    if (!wlist.isEmpty()) {
      Iterator<ConflictElement> iter = wlist.iterator();
      while (iter.hasNext()) {
        ConflictElement elements = iter.next();
        boolean used = false;

        LOGGER.debug("Examining collissions: substitutions");
        for (Pair<Sltag, Integer> elem : elements.getSubstitutions()) {
          Sltag other = elem.getLeft();
          Integer start = elem.getRight();
          String startLexicalEntry = (start != null) ? words[start] : null;
          LOGGER.debug("Collision examination : substitution starting at {} ({}):\n{}", start, startLexicalEntry, other.toPrettyString());
          LtagNode target = curr.firstMatch(other.getRoot().getCategory(), startLexicalEntry, LtagNodeMarker.SUB);
          if (target != null && LtagNodeMarker.SUB.equals(target.getMarker())) {
            LOGGER.debug("Collision examination : adjunction : eligible target found {}", target);
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
          Sltag other = elem.getLeft();
          Integer start = elem.getRight();
          String startLexicalEntry = (start != null) ? words[start] : null;
          SyntaxCategory category = other.getRoot().getCategory();
          LOGGER.debug("Collision examination : adjunction starting at {} ({}):\n{}", start, startLexicalEntry, other.toPrettyString());
          LtagNode target = curr.firstMatch(category, startLexicalEntry, null);
          if (target != null) {
            LOGGER.debug("Collision examination : adjunction : eligible target found {}", target);
            try {
              curr.adjunction(other, target);
              LOGGER.debug("Adjuncted (colliding element) {} with:\n{}", target, other.toPrettyString());
              break;
            } catch (LTAGException exc) {
              LOGGER.warn(exc.getMessage());
            }
          }
        }
      }
      LOGGER.debug("Current SLTAG\n{}", curr.toPrettyString());
    }

    if (isAskType) {
      curr.getSemantics().setSelect(false);
    }

    return curr;
  }
}
