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
import com.acmutv.ontoqa.core.semantics.dudes.Dudes;
import com.acmutv.ontoqa.core.syntax.ltag.Ltag;
import com.acmutv.ontoqa.core.syntax.ltag.LtagNode;

/**
 * The Semantic Ltag is an Ltag with a semantic semantics.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Ltag
 * @see Dudes
 */
public interface Sltag extends Ltag {

  /**
   * Returns the semantics.
   * @return the semantics.
   */
  Dudes getSemantics();

  /**
   * Executes an adjunction with the SLTAG {@code other} matching {@code target1} and {@code target2}.
   * @param other the SLTAG to adjunct.
   * @param target1 the local node to adjunct to.
   * @param target2 the node of {@code other} to adjunct.
   * @throws LTAGException when adjunction cannot be performed.
   */
  void adjunction(Sltag other, LtagNode target1, LtagNode target2) throws LTAGException;

  /**
   * Executes a substitution with the SLTAG {@code other} matching its root with {@code target}.
   * @param other the SLTAG to adjunct.
   * @param target the local node to adjunct to.
   * @throws LTAGException when substitution cannot be performed.
   */
  void substitution(Sltag other, LtagNode target) throws LTAGException;

}
