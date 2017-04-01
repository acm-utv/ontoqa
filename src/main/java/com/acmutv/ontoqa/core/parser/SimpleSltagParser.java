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

    while (i < words.length) {
      currLexicalEntry = "";
      List<ElementarySltag> candidates;
      List<ElementarySltag> temp = new ArrayList<>();

      currLexicalEntry = words[i++];
      candidates = grammar.getAllElementarySLTAG(currLexicalEntry);

      do {
        if (i >= words.length) {
          throw new OntoqaParsingException("Cannot find lexical entry: " + currLexicalEntry);
        }
        candidates = new ArrayList<>(temp);
        currLexicalEntry = currLexicalEntry.concat(((currLexicalEntry.isEmpty())? "" : " ") + words[i++]);
        temp = grammar.getAllElementarySLTAG(currLexicalEntry);
        LOGGER.debug("LexicalEntry: {}", currLexicalEntry);
        LOGGER.debug("Temp: {}", temp);
      } while (temp.isEmpty() && grammar.matchStart(currLexicalEntry));

      LOGGER.debug("Lexical Entry: {}", currLexicalEntry);
      LOGGER.debug("ElementarySLTAGs: {}", candidates);

      if (candidates.size() > 1) {
        Iterator<ElementarySltag> iter = candidates.iterator();
        while (iter.hasNext()) {
          ElementarySltag candidate = iter.next();
          if (i == 0 && candidate.isLeftSub()) {
            iter.remove();
          } else if (candidate.isAdjunctable()) {
            wlist.get(wlist.size() - 1).addAdjunction(candidate, prevLexicalEntry);
          } else {
            wlist.get(wlist.size() - 1).addSubstitution(candidate, prevLexicalEntry);
          }
        }
      } else {
        ElementarySltag candidate = candidates.get(0);
        if (candidate.isAdjunctable()) {
          LOGGER.debug("Candidate (adjunctable): {}", candidate);
          dashboard.addAdjunction(candidate, prevLexicalEntry);
        } else if (candidate.isSentence()) {
          LOGGER.debug("Candidate (sentence): {}", candidate);
          if (curr != null) {
            throw new Exception("Cannot decide sentence root: multiple root found.");
          }
          curr = candidate;
        } else {
          LOGGER.debug("Candidate (substitution): {}", candidate);
          dashboard.addSubstitution(candidate);
        }
      }

      if (curr != null) {
        Iterator<LtagNode> localSubstitutions = curr.getNodes(LtagNodeMarker.SUB).iterator();
        while (localSubstitutions.hasNext()) {
          boolean substituted = false;
          LtagNode localSubstitution = localSubstitutions.next();
          LOGGER.debug("Local Substitution: {}", localSubstitution);
          Iterator<Sltag> waitingSubstitutions = dashboard.getSubstitutions().iterator();
          while (waitingSubstitutions.hasNext()) {
            Sltag waitingSubstitution = waitingSubstitutions.next();
            LOGGER.debug("Waiting Substitution: {}", waitingSubstitution);
            if (localSubstitution.getCategory().equals(waitingSubstitution.getRoot().getCategory())) {
              curr.substitution(waitingSubstitution, localSubstitution);
              LOGGER.debug("Substituted: {} with {}", waitingSubstitution, localSubstitution);
              waitingSubstitutions.remove();
              substituted = true;
              break;
            }
          }
          if (substituted) {
            localSubstitutions.remove();
          }
        }
      }

      prevLexicalEntry = currLexicalEntry;
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
              continue;
            }
          }
        }
      }
    }

    return curr;
  }
}
