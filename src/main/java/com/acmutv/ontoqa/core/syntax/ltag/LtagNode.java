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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

/**
 * This class realizes a Ltag node.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see LtagNode
 * @see Ltag
 */
@Data
@AllArgsConstructor
public class LtagNode {

  /**
   * The typology of a Ltag node.
   * A Ltag node can be a Part-Of-Speech (POS) node or a Lexical (LEX) node.
   */
  @Getter
  public enum Type {
    POS ("POS", "Part of Speech"),
    LEX ("LEX", "Lexical Entry");

    private String shortName;
    private String longName;

    Type(final String shortName, final String longName) {
      this.shortName = shortName;
      this.longName = longName;
    }
  }

  /**
   * The marker of a Ltag node.
   * A LEX node cannot be marked for Ltag operations, thus it is always marked as NONE.
   * A POS node can be marked for specific Ltag operations.
   * A POS node marked as NONE is a standard POS node.
   * A POS node marked as SUB is a substitution node.
   * A POS node marked as ADJ is an adjunction node.
   */
  @Getter
  public enum Marker {
    NONE ("NONE", "None", ""),
    SUB ("SUB", "Substitution", "^"),
    ADJ ("ADJ", "Adjunction", "*");

    private String shortName;
    private String longName;
    private String symbol;

    Marker(final String shortName, final String longName, final String symbol) {
      this.shortName = shortName;
      this.longName = longName;
      this.symbol = symbol;
    }
  }

  /**
   * The unique node id.
   * The id must be non-null when adding the node inside a {@link Ltag}.
   * Typically, the id follows the following schema: anchor(,anchor):{POS-class|LEX}:Number
   */
  private String id = null;

  /**
   * The typology of the Ltag node.
   * A node can be a Part-Of-Speech (POS) node or a Lexical (LEX) node.
   */
  @NonNull
  private Type type;

  /**
   * The label of the Ltag node.
   * A POS node is labeled with a POS class.
   * A LEX node is labeled with a lexical entry.
   */
  @NonNull
  private String label;

  /**
   * The marker of the Ltag node.
   * A LEX node cannot be marked for Ltag operations, thus it is always marked as NONE.
   * A POS node can be marked for specific Ltag operations.
   * A POS node marked as NONE is a standard POS node.
   * A POS node marked as SUB is a substitution node.
   * A POS node marked as ADJ is an adjunction node.
   */
  @NonNull
  private Marker marker;

  /**
   * Returns the pretty string representation.
   * @return the pretty string representation.
   */
  public String toPrettyString() {
    return String.format("%s%s", this.getLabel(), this.getMarker().getSymbol());
  }

}
