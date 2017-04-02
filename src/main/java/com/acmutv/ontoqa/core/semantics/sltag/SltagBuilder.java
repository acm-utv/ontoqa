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
import com.acmutv.ontoqa.core.syntax.ltag.LtagNode;
import com.sun.media.jfxmedia.logging.Logger;
import lombok.EqualsAndHashCode;

/**
 * A builder for {@link Sltag}.
 * It executes substitution and adjunction operations with the builder pattern.
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
   * Executes an adjunction on the current SLTAG {@code anchor} with {@code other}.
   * @param other the SLTAG to adjunction.
   * @param localAnchor the local SLTAG node anchor.
   * @return the SLTAG resulting from the current adjunction.
   */
  public SltagBuilder adjunction(Sltag other, String localAnchor) throws LTAGException {
    this.current.adjunction(other, this.current.getNode(localAnchor));
    return this;
  }

  /**
   * Executes an adjunction on the current SLTAG {@code anchor} with {@code other}.
   * @param other the SLTAG to adjunction.
   * @return the SLTAG resulting from the current adjunction.
   */
  public SltagBuilder adjunction(Sltag other) throws LTAGException {
    LtagNode targetNode = this.current.firstMatch(other.getRoot().getCategory(), null);
    this.current.adjunction(other, targetNode);
    return this;
  }

  /**
   * Executes an adjunction on the current SLTAG {@code anchor} with {@code other}.
   * @param other the SLTAG to adjunction.
   * @param start the starting local lexical entry.
   * @return the SLTAG resulting from the current adjunction.
   */
  public SltagBuilder adjunctionAfter(Sltag other, String start) throws LTAGException {
    LtagNode node = this.current.firstMatch(other.getRoot().getCategory(), start);
    this.current.adjunction(other, node);
    return this;
  }

  /**
   * Executes a substitution on the current SLTAG with {@code other} against {@code anchor}.
   * @param other the SLTAG to substitute.
   * @param localAnchor the anchor to substitute.
   * @return the SLTAG resulting from the current substitution.
   */
  public SltagBuilder substitution(Sltag other, String localAnchor) throws LTAGException {
    this.current.substitution(other, localAnchor);
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
