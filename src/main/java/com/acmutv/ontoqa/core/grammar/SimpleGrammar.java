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
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleGrammar.class);

  /**
   * Returns <tt>true</tt> if this map contains a mapping for the
   * specified key.
   *
   * @param key The key whose presence in this map is to be tested
   * @return <tt>true</tt> if this map contains a mapping for the specified
   * key.
   */
  @Override
  public boolean containsKey(Object key) {
    final String strKey = String.valueOf(key).toLowerCase();
    return super.containsKey(strKey);
  }

  @Override
  public List<ElementarySltag> get(Object key) {
    final String strKey = String.valueOf(key).toLowerCase();
    return super.get(strKey);
  }

  @Override
  public List<ElementarySltag> getOrDefault(Object key, List<ElementarySltag> defaultValue) {
    final String strKey = String.valueOf(key).toLowerCase();
    return super.getOrDefault(strKey, defaultValue);
  }

  /**
   * Associates the specified value with the specified key in this map.
   * If the map previously contained a mapping for the key, the old
   * value is replaced.
   *
   * @param key   key with which the specified value is to be associated
   * @param value value to be associated with the specified key
   * @return the previous value associated with <tt>key</tt>, or
   * <tt>null</tt> if there was no mapping for <tt>key</tt>.
   * (A <tt>null</tt> return can also indicate that the map
   * previously associated <tt>null</tt> with <tt>key</tt>.)
   */
  @Override
  public List<ElementarySltag> put(String key, List<ElementarySltag> value) {
    final String strKey = String.valueOf(key).toLowerCase();
    return super.put(strKey, value);
  }

  @Override
  public List<ElementarySltag> putIfAbsent(String key, List<ElementarySltag> value) {
    final String strKey = String.valueOf(key).toLowerCase();
    return super.putIfAbsent(strKey, value);
  }

  /**
   * Returns the set of all elementary SLTAG.
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
   * @param sltag the Sltag to add.
   * @return true if {@code sltag} has been added; false otherwise.
   */
  @Override
  public boolean addElementarySLTAG(ElementarySltag sltag) {
    String word = sltag.getEntry();
    this.putIfAbsent(word, new ArrayList<>());
    return this.get(word).add(sltag);
  }

  /**
   * Returns the set of elementary Sltag for {@code word}.
   * @param word the word.
   * @return the set of elementary Sltag for {@code word}.
   */
  @Override
  public List<ElementarySltag> getAllElementarySLTAG(String word) {
    return this.getOrDefault(word, new ArrayList<>());
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
      if (word.toLowerCase().matches(key)) {
        trees.addAll(this.get(key));
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
        if (key.startsWith(lexicalEntry.toLowerCase())) {
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
        Matcher matcher = Pattern.compile(key).matcher(lexicalEntry.toLowerCase());
        if (matcher.matches() || matcher.hitEnd()) {
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
    boolean matched = false;

    if (lexicalPattern != null) {
      for (String key : super.keySet()) {
        Matcher matcher = Pattern.compile(key).matcher(lexicalPattern.toLowerCase());
        if (matcher.matches()) {
          LOGGER.debug("MATCH FULL with key: {}", key);
          matched = true;
          break;
        }
      }

      if (matched) return GrammarMatchType.FULL;

      for (String key : super.keySet()) {
        if (key.startsWith(lexicalPattern.toLowerCase())) {
          LOGGER.debug("MATCH PART with key: {}", key);
          matched = true;
          break;
        }
      }

      if (matched) return GrammarMatchType.PART;

      for (String key : super.keySet()) {
        Matcher matcher = Pattern.compile(key).matcher(lexicalPattern.toLowerCase());
        //noinspection ResultOfMethodCallIgnored
        matcher.matches();
        if (matcher.hitEnd()) {
          LOGGER.debug("MATCH PART_STAR with key: {}", key);
          matched = true;
          break;
        }
      }

      if (matched) return GrammarMatchType.PART_STAR;
    }

    return GrammarMatchType.NONE;
  }
}
