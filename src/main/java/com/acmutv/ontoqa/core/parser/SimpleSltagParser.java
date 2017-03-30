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
import com.acmutv.ontoqa.core.semantics.sltag.Sltag;
import com.acmutv.ontoqa.core.syntax.SyntaxCategory;
import com.acmutv.ontoqa.core.syntax.ltag.LtagNode;
import com.acmutv.ontoqa.core.syntax.ltag.LtagNodeMarker;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Iterator;
import java.util.List;

/**
 * A simple SLTAG parser.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class SimpleSltagParser implements SltagParser {

  /**
   * Parses {@code sentence} with {@code grammar}.
   * @param sentence the sentence to parse.
   * @param grammar the grammar to parse with.
   * @return the parsed SLTAG.
   * @throws OntoqaParsingException when parsing fails.
   */
  @Override
  public Sltag parse(String sentence, Grammar grammar) throws OntoqaParsingException, LTAGException {
    ParserDashboard dashboard = new ParserDashboard();

    final String[] words = sentence.split(" ");
    String prevLexicalEntry = null;
    Sltag curr = null;
    int i = 0;

    /*
    while (i < words.length) {
        String lexicalEntry = "";
        List<Sltag> candidates = null;
        List<Sltag> temp = null;
        do {
            if (i >= words.lenght) {
                throw new Exception("lexicalEntry not found");
            }
            candidates = temp;
            lexicalEntry = lexicalEntry.concat(((lexicalEntry.isEmpty()) ? "": " ") + words[i++];
        } while (!(temp=grammar.get(lexicalEntry)).isEmpty() || grammar.isThere(lexicalEntry));

        if (candidates.size() > 1) {
            for (Sltag candidate : candidates) {
                if (i == 0 && candidate.hasLeftSub()) {
                    candidates.remove(candidate);
                } else if (candidate.isAdjunctable()) {
                    waitingList.add(ADJ, candidate, previousLexicalEntry);
                } else {
                    waitingList.add(SUB; candidate);
                }
            }
        } else {
            Sltag candidate = candidates.get(0);
            if (candidate.isAdjunctable()) {
                adjunctions.add(candidate, previousLexicalEntry);
            } else if (candidate.isSentence()) {
                if (curr != null) throw new Exception("Cannot decide sentence root: multiple root found.");
                curr = candidate;
            } else {
                substitutions.add(candidate);
            }
        }
        previousLexicalEntry = lexicalEntry;
    }
    */

    WaitingList wlist = dashboard.getWaitingList();
    if (wlist.isEmpty()) {
      return curr;
    } else {
      Iterator<List<Triple<LtagNodeMarker, Sltag, String>>> iter = wlist.iterator();
      while (iter.hasNext()) {
        List<Triple<LtagNodeMarker, Sltag, String>> list = iter.next();

        Iterator<Triple<LtagNodeMarker, Sltag, String>> subiter = list.iterator();
        while (subiter.hasNext()) {
          Triple<LtagNodeMarker, Sltag, String> wnode = subiter.next();
          if (wnode.getLeft().equals(LtagNodeMarker.ADJ)) continue;
          Sltag other = wnode.getMiddle();
          String anchor = wnode.getRight();
          boolean executed = curr.substitution(other, anchor);
          if (executed) {
            iter.remove();
            break;
          }
        }

        Iterator<Triple<LtagNodeMarker, Sltag, String>> adjiter = list.iterator();
        while (adjiter.hasNext()) {
          Triple<LtagNodeMarker, Sltag, String> wnode = subiter.next();
          if (wnode.getLeft().equals(LtagNodeMarker.SUB)) continue;
          Sltag other = wnode.getMiddle();
          SyntaxCategory category = other.getRoot().getCategory();
          String start = wnode.getRight();
          LtagNode target = curr.firstMatch(category, start);
          if (target != null) {
            boolean executed = curr.adjunction(other, target);
            if (executed) {
              iter.remove();
              break;
            }
          }
        }
      }
    }

    return curr;
  }
}
