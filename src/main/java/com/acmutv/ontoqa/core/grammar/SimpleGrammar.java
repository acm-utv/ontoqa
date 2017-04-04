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

package com.acmutv.ontoqa.core.grammar;

import com.acmutv.ontoqa.core.semantics.sltag.ElementarySltag;
import com.acmutv.ontoqa.core.semantics.sltag.Sltag;
import lombok.EqualsAndHashCode;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple SLTAG.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
public class SimpleGrammar extends HashMap<String,List<ElementarySltag>> implements Grammar {

  /**
   * Returns the set of all elementary SLTAG.
   *
   * @return the set of all elementary SLTAG.
   */
  @Override
  public List<ElementarySltag> getAllElementarySLTAG() {
    List<ElementarySltag> all = new ArrayList<>();
    super.values().forEach(all::addAll);
    return all;
  }

  /**
   * Adds {@code sltag} to the grammar, as a new elementary Sltag for {@code word}.
   *
   * @param sltag the Sltag to add.
   * @return true if {@code sltag} has been added; false otherwise.
   */
  @Override
  public boolean addElementarySLTAG(ElementarySltag sltag) {
    String word = sltag.getEntry();
    super.putIfAbsent(word, new ArrayList<>());
    return super.get(word).add(sltag);
  }

  /**
   * Returns the set of elementary Sltag for {@code word}.
   * @param word the word.
   * @return the set of elementary Sltag for {@code word}.
   */
  @Override
  public List<ElementarySltag> getAllElementarySLTAG(String word) {
    return super.getOrDefault(word, new ArrayList<>());
  }

  /**
   * Returns the set of elementary Sltag matching {@code word}.
   *
   * @param word the word.
   * @return the set of elementary Sltag matching {@code word}.
   */
  @Override
  public List<ElementarySltag> getAllMatchingElementarySLTAG(String word) {
    List<ElementarySltag> trees = new ArrayList<>();
    for (String key : super.keySet()) {
      if (word.matches(key)) {
        trees.addAll(super.get(key));
      }
    }
    return trees;
  }

  /**
   * Merges the current grammar with {@code other}.
   *
   * @param other the grammar to substitution.
   */
  @Override
  public void merge(Grammar other) {
    other.getAllElementarySLTAG().forEach(this::addElementarySLTAG);
  }

  /**
   * Checks if grammar contains SLTAG with lexical entry starting with {@code lexicalEntry}.
   * @param lexicalEntry the lexical entry.
   * @return true, if the grammar contains SLTAG with lexical entry starting with {@code lexicalEntry}; false. otherwise.
   */
  @Override
  public boolean matchStart(String lexicalEntry) {
    if (lexicalEntry == null) {
      return false;
    } else if (lexicalEntry.isEmpty()) {
      return true;
    } else {
      for (String key : super.keySet()) {
        if (key.startsWith(lexicalEntry)) {
          return true;
        }
      }
      return false;
    }
  }

  /**
   * Checks if grammar contains SLTAG with lexical entry matching with {@code lexicalEntry}.
   * @param lexicalEntry the lexical entry.
   * @return true, if the grammar contains SLTAG with lexical entry matching with {@code lexicalEntry}; false. otherwise.
   */
  @Override
  public boolean match(String lexicalEntry) {
    if (lexicalEntry != null) {
      for (String key : super.keySet()) {
        Matcher matcher = Pattern.compile(key).matcher(lexicalEntry);
        if (matcher.matches() || matcher.hitEnd()) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Checks if grammar contains SLTAG with lexical entry equal to {@code lexicalPattern}.
   * @param lexicalPattern the lexical pattern to match.
   * @return true, if the grammar contains SLTAG with lexical entry equal to {@code lexicalPattern}; false. otherwise.
   */
  @Override
  public boolean matchFull(String lexicalPattern) {
    if (lexicalPattern != null) {
      for (String key : super.keySet()) {
        if (lexicalPattern.matches(key)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Returns the grammar matching type for {@code lexicalPattern}.
   * @param lexicalPattern the lexical pattern to match.
   * @return the grammar matching type for {@code lexicalPattern}.
   */
  @Override
  public GrammarMatchType matchType(String lexicalPattern) {
    if (lexicalPattern != null) {
      for (String key : super.keySet()) {
        Matcher matcher = Pattern.compile(key).matcher(lexicalPattern);
        if (matcher.matches()) {
          return GrammarMatchType.FULL;
        } else if (matcher.hitEnd()) {
          return GrammarMatchType.PART;
        }
      }
    }
    return GrammarMatchType.NONE;
  }
}
