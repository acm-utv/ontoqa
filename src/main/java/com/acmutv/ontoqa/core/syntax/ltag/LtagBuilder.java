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

import com.acmutv.ontoqa.core.exception.LTAGException;
import lombok.EqualsAndHashCode;

/**
 * A builder for {@link Ltag}.
 * It executes substitution and adjunction operations with the builder pattern.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Ltag
 * @see SimpleLtag
 */
@EqualsAndHashCode
public class LtagBuilder {

  /**
   * The current working LTAG.
   */
  protected Ltag current;

  /**
   * Creates a new builder with {@code start} as initial LTAG.
   * @param start the initial LTAG.
   */
  public LtagBuilder(Ltag start) {
    this(start, false);
  }

  /**
   * Creates a new builder with {@code start} as initial LTAG.
   * @param start the initial LTAG.
   * @param copy whether or not to operate on a copy of {@code start}.
   */
  public LtagBuilder(Ltag start, boolean copy) {
    if (copy) {
      this.current = new SimpleLtag(start);
    } else {
      this.current = start;
    }
  }

  /**
   * Executes an adjunction on the current LTAG with {@code anchor2} from {@code other} against
   * {@code anchor1}.
   * @param other the LTAG to adjunction.
   * @param localAnchor the local LTAG node anchor.
   * @return the LTAG resulting from the current adjunction.
   */
  public LtagBuilder adjunction(Ltag other, String localAnchor) throws LTAGException {
    this.current.adjunction(other, this.current.getNode(localAnchor));
    return this;
  }

  /**
   * Executes a substitution on the current LTAG with {@code other} against {@code anchor}.
   * @param other the LTAG to substitute.
   * @param localAnchor the anchor to substitute.
   * @return the LTAG resulting from the current substitution.
   */
  public LtagBuilder substitution(Ltag other, String localAnchor) throws LTAGException {
    this.current.substitution(other, localAnchor);
    return this;
  }

  /**
   * Returns the built LTAG.
   * @return the built LTAG.
   */
  public Ltag build() {
    return this.current;
  }
}
