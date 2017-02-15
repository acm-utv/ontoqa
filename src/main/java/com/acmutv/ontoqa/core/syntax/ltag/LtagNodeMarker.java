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

package com.acmutv.ontoqa.core.syntax.ltag;

import lombok.Getter;

/**
 * The marker of a Ltag node.
 * A LEX node cannot be marked for Ltag operations, thus it is always marked as NONE.
 * A SyntaxCategory node can be marked for specific Ltag operations.
 * A SyntaxCategory node marked as SUB is a substitution node.
 * A SyntaxCategory node marked as ADJ is an adjunction node.
 */
@Getter
public enum LtagNodeMarker {
  ADJ  ("ADJ", "Adjunction", "*"),
  SUB  ("SUB", "Substitution", "^");

  private String shortName;
  private String longName;
  private String symbol;

  LtagNodeMarker(final String shortName, final String longName, final String symbol) {
    this.shortName = shortName;
    this.longName = longName;
    this.symbol = symbol;
  }

  /**
   * Returns the {@link LtagNodeMarker} corresponding to the given {@code symbol}.
   * @param symbol the marker symbol.
   * @return the {@link LtagNodeMarker} corresponding to the given {@code symbol}.
   * @throws IllegalArgumentException when {@code symbol} does not correspond to any {@link LtagNodeMarker}.
   */
  public static LtagNodeMarker fromSymbol(String symbol) {
    for (LtagNodeMarker marker : LtagNodeMarker.values()) {
      if (marker.getSymbol().equals(symbol)) {
        return marker;
      }
    }
    throw new IllegalArgumentException();
  }
}