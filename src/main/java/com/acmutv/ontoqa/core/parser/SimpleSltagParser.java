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
import com.acmutv.ontoqa.core.semantics.sltag.ElementarySltag;
import com.acmutv.ontoqa.core.semantics.sltag.SimpleSltag;
import com.acmutv.ontoqa.core.semantics.sltag.Sltag;
import com.acmutv.ontoqa.core.syntax.SyntaxCategory;
import com.acmutv.ontoqa.core.syntax.ltag.LtagNode;
import com.acmutv.ontoqa.core.syntax.ltag.LtagNodeMarker;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.acmutv.ontoqa.core.syntax.ltag.LtagNodeMarker.ADJ;
import static com.acmutv.ontoqa.core.syntax.ltag.LtagNodeMarker.SUB;

/**
 * A simple SLTAG parser.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class SimpleSltagParser implements SltagParser {

  private static final Logger LOGGER = LogManager.getLogger(SimpleSltagParser.class);

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

    final String[] words = sentence.split(" ");
    String currLexicalEntry;
    String prevLexicalEntry = null;
    Sltag curr = null;
    int i = 0;
    int numwords = words.length;

    LOGGER.debug("Sentence {} splitted in {} words", sentence, numwords);

    while (i < numwords) {
      currLexicalEntry = "";
      List<Sltag> candidates = new ArrayList<>();

      int itemp = i;
      String tempLexicalEntry = "";
      List<ElementarySltag> temp = new ArrayList<>();

      while (itemp < numwords) {
        tempLexicalEntry = tempLexicalEntry.concat(((tempLexicalEntry.isEmpty()) ? "" : " ") + words[itemp++]);
        temp = grammar.getAllElementarySLTAG(tempLexicalEntry);
        if (!temp.isEmpty()) {
          i = itemp;
          currLexicalEntry = tempLexicalEntry;
          temp.forEach(tc -> candidates.add(tc.copy()));
        }

        if (!grammar.matchStart(tempLexicalEntry)) {
          break;
        }
      }

      if (candidates.isEmpty()) {
        throw new OntoqaParsingException("Cannot find SLTAG for lexical entry: %s (temp: %s)",
            currLexicalEntry, tempLexicalEntry);
      }

      LOGGER.debug("Lexical Entry: {}", currLexicalEntry);
      LOGGER.debug("SLTAG Candidates:\n{}",
          candidates.stream().map(Sltag::toPrettyString).collect(Collectors.joining("\n")));

      if (candidates.size() > 1) {
        LOGGER.debug("Colliding candidates found");
        Iterator<Sltag> iter = candidates.iterator();
        while (iter.hasNext()) {
          Sltag candidate = iter.next();
          if (i == 0 && candidate.isLeftSub()) {
            LOGGER.debug("Excluded colliding candidate:\n{}", candidate.toPrettyString());
            iter.remove();
          } else if (candidate.isAdjunctable()) {
            LOGGER.debug("Colliding candidate (adjunction):\n{}", candidate.toPrettyString());
            wlist.get(wlist.size() - 1).addAdjunction(candidate, prevLexicalEntry);
          } else {
            LOGGER.debug("Colliding candidate (substitution):\n{}", candidate.toPrettyString());
            wlist.get(wlist.size() - 1).addSubstitution(candidate, prevLexicalEntry);
          }
        }
      } else {
        Sltag candidate = candidates.get(0);
        LOGGER.debug("Candidate:\n{}", candidate.toPrettyString());
        if (candidate.isAdjunctable()) {
          LOGGER.debug("Candidate (adjunction):\n{}", candidate.toPrettyString());
          dashboard.addAdjunction(candidate, prevLexicalEntry);
        } else if (candidate.isSentence()) {
          LOGGER.debug("Candidate (sentence):\n{}", candidate.toPrettyString());
          if (curr != null) {
            throw new Exception("Cannot decide sentence root: multiple root found.");
          }
          curr = candidate;
        } else {
          LOGGER.debug("Candidate (substitution):\n{}", candidate.toPrettyString());
          dashboard.addSubstitution(candidate);
        }
      }

      prevLexicalEntry = currLexicalEntry;

      if (curr != null) {
        Iterator<LtagNode> localSubstitutions = curr.getNodesDFS(LtagNodeMarker.SUB).iterator();
        while (localSubstitutions.hasNext()) {
          boolean substituted = false;
          LtagNode localSubstitution = localSubstitutions.next();
          LOGGER.debug("Local Substitution: {}", localSubstitution);
          Iterator<Sltag> waitingSubstitutions = dashboard.getSubstitutions().iterator();
          while (waitingSubstitutions.hasNext()) {
            Sltag waitingSubstitution = waitingSubstitutions.next();
            if (localSubstitution.getCategory().equals(waitingSubstitution.getRoot().getCategory())) {
              LOGGER.debug("Substituting {} with:\n{}", localSubstitution, waitingSubstitution.toPrettyString());
              curr.substitution(waitingSubstitution, localSubstitution);
              LOGGER.debug("Substituted {} with:\n{}", localSubstitution, waitingSubstitution.toPrettyString());
              waitingSubstitutions.remove();
              substituted = true;
              break;
            }
          }
          if (substituted) {
            localSubstitutions.remove();
          }
        }

        Iterator<Pair<Sltag,String>> waitingAdjunctions = dashboard.getAdjunctions().iterator();
        while (waitingAdjunctions.hasNext()) {
          Pair<Sltag,String> waitingAdjunction = waitingAdjunctions.next();
          Sltag toAdjunct = waitingAdjunction.getLeft();
          String start = waitingAdjunction.getRight();
          LtagNode anchor = curr.firstMatch(toAdjunct.getRoot().getCategory(), start);
          if (anchor != null) {
            LOGGER.debug("Adjuncting {} on {}", toAdjunct.toPrettyString(), anchor);
            curr.adjunction(toAdjunct, anchor);
            LOGGER.debug("Adjuncted {} on {}", toAdjunct.toPrettyString(), anchor);
            waitingAdjunctions.remove();
          }
        }
      }
      LOGGER.debug("Current SLTAG\n{}", (curr != null) ? curr.toPrettyString() : "NONE");
    }

    if (curr == null) {
      throw new Exception("Cannot build SLTAG");
    }

    if (wlist.isEmpty()) {
      return curr;
    } else {
      Iterator<ConflictElement> iter = wlist.iterator();
      while (iter.hasNext()) {
        ConflictElement elements = iter.next();
        boolean used = false;

        for (Pair<Sltag, String> elem : elements.getSubstitutions()) {
          Sltag other = elem.getLeft();
          String start = elem.getRight();
          LtagNode target = curr.firstMatch(other.getRoot().getCategory(), start);
          if (target != null) {
            try {
              curr.substitution(other, target);
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
        }

        for (Pair<Sltag, String> elem : elements.getAdjunctions()) {
          Sltag other = elem.getLeft();
          String start = elem.getRight();
          SyntaxCategory category = other.getRoot().getCategory();
          LtagNode target = curr.firstMatch(category, start);
          if (target != null) {
            try {
              curr.adjunction(other, target);
              break;
            } catch (LTAGException exc) {
              LOGGER.warn(exc.getMessage());
              //continue;
            }
          }
        }
      }
      LOGGER.debug("Current SLTAG\n{}", curr.toPrettyString());
    }

    return curr;
  }
}
