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

package com.acmutv.ontoqa.core.semantics.sltag;

import com.acmutv.ontoqa.core.exception.LTAGException;
import com.acmutv.ontoqa.core.semantics.dudes.SimpleDudes;
import com.acmutv.ontoqa.core.semantics.dudes.Dudes;
import com.acmutv.ontoqa.core.syntax.ltag.Ltag;
import com.acmutv.ontoqa.core.syntax.ltag.LtagNode;
import com.acmutv.ontoqa.core.syntax.ltag.SimpleLtag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.apache.jena.query.Query;

/**
 * A simple Semantic Ltag.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SimpleSltag extends SimpleLtag implements Sltag {

  /**
   * The interpretation.
   */
  @NonNull
  protected Dudes semantics = new SimpleDudes();

  /**
   * Creates a new SLTAG with {@code syntax} as LTAG and {@code semantics} as DUDES.
   * @param syntax the LTAG.
   * @param semantics the DUDES.
   */
  public SimpleSltag(Ltag syntax, Dudes semantics) {
    super(syntax);
    this.semantics = semantics;
  }

  /**
   * Constructs a new SLTAG as a clone of {@code ltag}.
   * @param other the SLTAG to clone
   */
  public SimpleSltag(Sltag other) {
    this(new SimpleLtag(other), new SimpleDudes(other.getSemantics()));
  }

  /**
   * Executes an adjunction with the SLTAG {@code other} matching {@code target1} and {@code target2}.
   * @param other the SLTAG to adjunct.
   * @param target1 the local node to adjunct to.
   * @param target2 the node of {@code other} to adjunct.
   * @throws LTAGException when adjunction cannot be performed.
   */
  @Override
  public void adjunction(Sltag other, LtagNode target1, LtagNode target2) throws LTAGException {
    super.adjunction(target1, other, target2);
    this.semantics.merge(other.getSemantics(), "");
  }

  /**
   * Executes the adjunction on the SLTAG.
   * @param other   the SLTAG to adjunct.
   * @param anchor1 the adjunction anchor.
   * @param anchor2 the node to adjunct.
   * @throws LTAGException when adjunction cannot be executed.
   */
  @Override
  public void adjunction(Sltag other, String anchor1, String anchor2) throws LTAGException {
    super.adjunction(anchor1, other, anchor2);
    this.semantics.merge(other.getSemantics(), "");
  }

  /**
   * Converts the SLTAG into an equivalent SPARQL query.
   * @return the equivalent SPARQL query.
   */
  @Override
  public Query convertToSPARQL() {
    return this.semantics.convertToSPARQL();
  }



  /**
   * Sets if a {@code SELECT} SPARQL query should be generated.
   * @param select whether or not to generate a {@code SELECT} SPARQL query.
   */
  @Override
  public void setSelect(boolean select) {
    this.semantics.setSelect(select);
  }

  /**
   * Executes a substitution with the SLTAG {@code other} matching its root with {@code target}.
   * @param other the SLTAG to adjunct.
   * @param target the local node to adjunct to.
   * @throws LTAGException when substitution cannot be performed.
   */
  @Override
  public void substitution(Sltag other, LtagNode target) throws LTAGException {
    super.substitution(target, other);
    this.semantics.merge(other.getSemantics(), target.getLabel()+target.getId());
  }

  /**
   * Executes the substitution on the SLTAG.
   * @param other  the SLTAG to substitute.
   * @param anchor the substitution anchor.
   * @throws LTAGException when substitution cannot be executed.
   */
  @Override
  public void substitution(Sltag other, String anchor) throws LTAGException {
    super.substitution(anchor, other);
    this.semantics.merge(other.getSemantics(), anchor);
  }

  /**
   * Returns the pretty string representation.
   * @return the pretty string representation.
   */
  @Override
  public String toPrettyString() {
    return String.format("%s\n\n%s", super.toPrettyString(), this.semantics.toPrettyString());
  }
}
