/*
  The MIT License (MIT)

  Copyright (c) 2016 Antonella Botte, Giacomo Marciani and Debora Partigianoni

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

package com.acmutv.ontoqa.core.syntax.ltag;

import com.acmutv.ontoqa.core.syntax.SyntaxCategory;

import java.util.HashMap;
import java.util.Map;

/**
 * A supplier for LTAG node ids.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class IdSupplier {

  /**
   * The general ID.
   */
  private int next = 0;

  /**
   * The specific IDs mapping for syntax categories.
   */
  private Map<String,Integer> map = new HashMap<>();

  /**
   * Constructs a new IdSupplier with general ID starting at {@code start}.
   * @param start the starting general ID.
   */
  public IdSupplier(int start) {
    this.next = start;
  }

  /**
   * Constructs a new IdSupplier with syntax category ID mapping starting at {@code start}.
   * @param start the starting syntax category ID mapping.
   */
  public IdSupplier(Map<String,Integer> start) {
    this.map = start;
  }

  /**
   * Returns the next general ID.
   * @return the next general ID.
   */
  public int getNext() {
    return this.next++;
  }

  /**
   * Returns the next ID for {@code category}.
   * @param category the syntax category.
   * @return the next ID for {@code category}.
   */
  public int getNext(SyntaxCategory category) {
    return this.map.compute(category.name(), (k, v) -> (v == null) ? 0 : ++v);
  }

  /**
   * Returns the next ID for {@code entry}.
   * @param word the lexical entry.
   * @return the next ID for {@code entry}.
   */
  public int getNext(String word) {
    return this.map.compute("TERMINAL:" + word, (k, v) -> (v == null) ? 0 : ++v);
  }

  /**
   * Resets the general ID to {@code start}.
   * @param start the starting general ID.
   */
  public void reset(int start) {
    this.next = start;
  }

  /**
   * Resets the ID for {@code category} to {@code start}.
   * @param category the syntax category.
   * @param start the starting ID.
   */
  public void reset(SyntaxCategory category, int start) {
    this.map.put(category.name(), start);
  }

  /**
   * Resets the ID for {@code word} to {@code start}.
   * @param word the lexical entry.
   * @param start the starting ID.
   */
  public void reset(String word, int start) {
    this.map.put("TERMINAL:" + word, start);
  }
    
}
