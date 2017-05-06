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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

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
   * The unique node id.
   * The id must be non-null when adding the node inside a {@link Ltag}.
   * Typically, the id follows the following schema: anchor(,anchor):{SyntaxCategory-class|TERMINAL}:Number
   */
  protected int id;

  /**
   * The typology of the Ltag node.
   * A node can be terminal or non-terminal.
   */
  @NonNull
  protected LtagNodeType type;

  /**
   * The syntax category for a non terminal LTAG node.
   * A terminal LTAG node has always {@code category} set to {@code null}.
   */
  protected SyntaxCategory category;

  /**
   * The marker of the Ltag node.
   * A terminal node cannot be marked for LTAG operations, thus it is always marked as NONE.
   * A non-terminal node can be marked for specific LTAG operations.
   * A non-terminal node marked as {@code SUB} is a substitution node.
   * A non-terminal node marked as {@code ADJ} is an adjunction node.
   */
  protected LtagNodeMarker marker;

  /**
   * The label of the LTAG node.
   * The label is used to tag the node for operations.
   */
  protected String label;

  /**
   * Constructs a new node as a copy of {@code other}.
   * @param other the node to copy.
   */
  public LtagNode(LtagNode other) {
    this.copy(other);
  }

  /**
   * Copies the {@code other} node.
   * @param other the node to copy.
   */
  public void copy(LtagNode other) {
    this.id = other.getId();
    this.type = other.getType();
    this.category = other.getCategory();
    this.marker = other.getMarker();
    this.label = other.getLabel();
  }

  public boolean identical(Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof LtagNode)) return false;
    LtagNode other = (LtagNode) obj;
    if (this.getType() == null ? other.getType() != null : !this.getType().equals(other.getType())) return false;
    if (this.getCategory() == null ? other.getCategory() != null : !this.getCategory().equals(other.getCategory())) return false;
    if (this.getId() != other.getId()) return false;
    if (this.getMarker() == null ? other.getMarker() != null : !this.getMarker().equals(other.getMarker())) return false;
    if (this.getLabel() == null ? other.getLabel() != null : !this.getLabel().equals(other.getLabel())) return false;
    return true;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof LtagNode)) return false;
    LtagNode other = (LtagNode) obj;
    if (this.getType() == null ? other.getType() != null : !this.getType().equals(other.getType())) return false;
    if (this.getType().equals(LtagNodeType.NON_TERMINAL)) {
      if (this.getCategory() == null ? other.getCategory() != null : !this.getCategory().equals(other.getCategory())) return false;
      if (this.getId() != other.getId()) return false;
    } else {
      if (this.getLabel() == null ? other.getLabel() != null : !this.getLabel().equals(other.getLabel())) return false;
      if (this.getId() != other.getId()) return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final long temp1 = Integer.toUnsignedLong(this.getId());
    result = (result*PRIME) + (this.getType() == null ? 43 : this.getType().hashCode());
    result = (result*PRIME) + (this.getCategory() == null ? 43 : this.getCategory().hashCode());
    result = (result*PRIME) + (int)(temp1 ^ (temp1 >>> 32));
    return result;
  }

  /**
   * Returns the string representation.
   * @return the string representation.
   */
  @Override
  public String toString() {
    if (this.type == LtagNodeType.NON_TERMINAL) {
      return String.format("%s%s%s%s",
          this.category,
          (this.id != 1) ? this.id : "",
          (this.marker != null) ? this.marker.getSymbol() : "",
          (this.label != null) ? "(" + this.label + ")" : "");
    } else {
      return String.format("'%s'%s",
          this.label,
          (this.id != 1) ? this.id : "");
    }
  }

}
