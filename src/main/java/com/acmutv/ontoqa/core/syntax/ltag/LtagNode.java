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

import com.acmutv.ontoqa.core.semantics.base.slot.Slot;
import com.acmutv.ontoqa.core.semantics.base.term.Variable;
import com.acmutv.ontoqa.core.syntax.POS;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A basic Ltag node.
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
    POS ("Part-of-Speech"),
    LEX ("Lexical-Entry");

    private String longName;

    Type(final String longName) {
      this.longName = longName;
    }
  }

  /**
   * The marker of a Ltag node.
   * A LEX node cannot be marked for Ltag operations, thus it is always marked as NONE.
   * A POS node can be marked for specific Ltag operations.
   * A POS node marked as SUB is a substitution node.
   * A POS node marked as ADJ is an adjunction node.
   */
  @Getter
  public enum Marker {
    ADJ  ("ADJ", "Adjunction", "*"),
    SUB  ("SUB", "Substitution", "^");

    private String shortName;
    private String longName;
    private String symbol;

    Marker(final String shortName, final String longName, final String symbol) {
      this.shortName = shortName;
      this.longName = longName;
      this.symbol = symbol;
    }

    /**
     * Returns the {@link Marker} corresponding to the given {@code symbol}.
     * @param symbol the marker symbol.
     * @return the {@link Marker} corresponding to the given {@code symbol}.
     * @throws IllegalArgumentException when {@code symbol} does not correspond to any {@link Marker}.
     */
    public static Marker fromSymbol(String symbol) {
      for (Marker marker : Marker.values()) {
        if (marker.getSymbol().equals(symbol)) {
          return marker;
        }
      }
      throw new IllegalArgumentException();
    }
  }

  /**
   * The unique node id.
   * The id must be non-null when adding the node inside a {@link Ltag}.
   * Typically, the id follows the following schema: anchor(,anchor):{POS-class|LEX}:Number
   */
  protected String id = null;

  /**
   * The typology of the Ltag node.
   * A node can be a Part-Of-Speech (POS) node or a Lexical (LEX) node.
   */
  @NonNull
  protected Type type;

  /**
   * The label of the Ltag node.
   * A POS node is labeled with a POS class.
   * A LEX node is labeled with a lexical entry.
   */
  @NonNull
  protected String label;

  /**
   * The marker of the Ltag node.
   * A LEX node cannot be marked for Ltag operations, thus it is always marked as NONE.
   * A POS node can be marked for specific Ltag operations.
   * A POS node marked as SUB is a substitution node.
   * A POS node marked as ADJ is an adjunction node.
   */
  protected Marker marker = null;

  public LtagNode(LtagNode other) {
    this.id = other.getId();
    this.type = other.getType();
    this.label = other.getLabel();
    this.marker = other.getMarker();
  }

  @Override
  public String toString() {
    if (this.type == Type.POS) {
      return String.format("(%s,%s)%s",
          this.id, this.label, (this.marker != null) ? this.marker.getSymbol() : "");
    } else {
      return String.format("(%s,'%s')",
          this.id, this.label);
    }
  }


  /**
   * Returns the pretty tring representattion.
   * @return the pretty string representation.
   */
  public String toPrettyString() {
    return String.format("%s%s",
        this.label, (this.marker != null) ? this.marker.getSymbol() : "");
  }

}
