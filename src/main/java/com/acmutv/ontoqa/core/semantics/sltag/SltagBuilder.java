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
import lombok.EqualsAndHashCode;

/**
 * A builder for {@link Sltag}.
 * It executes substitution and adjoin operations with the builder pattern.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Sltag
 * @see SimpleSltag
 */
@EqualsAndHashCode
public class SltagBuilder {

  /**
   * The current working SLTAG.
   */
  protected Sltag current;

  /**
   * Creates a new builder with {@code start} as initial DUDES.
   * @param start the initial SLTAG.
   */
  public SltagBuilder(Sltag start) {
    this(start, false);
  }

  /**
   * Creates a new builder with {@code start} as initial DUDES.
   * @param start the initial SLTAG.
   * @param copy whether or not to operate on a copy of {@code start}.
   */
  public SltagBuilder(Sltag start, boolean copy) {
    if (copy) {
      this.current = new SimpleSltag(start);
    } else {
      this.current = start;
    }
  }

  /**
   * Executes a substitution on the current SLTAG with {@code other} against {@code anchor}.
   * @param other the SLTAG to substitute.
   * @param anchor the anchor to substitute.
   * @return the SLTAG resulting from the current substitution.
   */
  public SltagBuilder substitution(Sltag other, String anchor) throws LTAGException {
    this.current.substitution(other, anchor);
    return this;
  }

  /**
   * Executes an adjoin on the current SLTAG {@code anchor} with {@code other}.
   * @param other the SLTAG to adjoin.
   * @param anchor the local SLTAG node anchor.
   * @return the SLTAG resulting from the current adjoin.
   */
  public SltagBuilder adjoin(Sltag other, String anchor) throws LTAGException {
    this.current.adjunction(other, anchor);
    return this;
  }

  /**
   * Executes an adjoin on the current SLTAG with {@code anchor2} from {@code other} against
   * {@code anchor1}.
   * @param other the SLTAG to adjoin.
   * @param anchor1 the local SLTAG node anchor.
   * @param anchor2 the node anchor of {@code other}.
   * @return the SLTAG resulting from the current adjoin.
   */
  public SltagBuilder adjoin(Sltag other, String anchor1, String anchor2) throws LTAGException {
    this.current.adjunction(other, anchor1, anchor2);
    return this;
  }

  /**
   * Returns the built SLTAG.
   * @return the built SLTAG.
   */
  public Sltag build() {
    return this.current;
  }
}
