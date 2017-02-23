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

package com.acmutv.ontoqa.core.semantics.dudes;
import lombok.EqualsAndHashCode;

/**
 * A builder for {@link Dudes}.
 * It executes substitution and adjoin operations with the builder pattern.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Dudes
 * @see SimpleDudes
 */
@EqualsAndHashCode
public class DudesBuilder {

  /**
   * The current working DUDES.
   */
  protected Dudes current;

  /**
   * Creates a new builder with {@code start} as initial DUDES.
   * @param start the initial DUDES.
   */
  public DudesBuilder(Dudes start) {
    this.current = start;
  }

  /**
   * Executes a substitution on the current DUDES with {@code other} against {@code anchor}.
   * @param other the DUDES to substitute.
   * @param anchor the anchor to substitute.
   * @return the DUDES resulting from the current substitution.
   */
  public DudesBuilder substitution(Dudes other, String anchor) {
    this.current.merge(other, anchor);
    return this;
  }

  /**
   * Executes an adjoin on the current DUDES with {@code other} against {@code anchor}.
   * @param other the DUDES to adjoin.
   * @param anchor the anchor to adjoin.
   * @return the DUDES resulting from the current adjoin.
   */
  public DudesBuilder adjoin(Dudes other, String anchor) {
    this.current.merge(other, anchor);
    return this;
  }

  /**
   * Returns the built DUDES.
   * @return the built DUDES.
   */
  public Dudes build() {
    Dudes built = new SimpleDudes(this.current);
    this.current = null;
    return built;
  }
}
