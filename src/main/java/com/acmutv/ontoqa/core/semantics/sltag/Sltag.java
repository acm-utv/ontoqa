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
import org.apache.jena.query.Query;

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
   * Executes an adjunction with the SLTAG {@code other} matching {@code target1}.
   * @param other the SLTAG to adjunct.
   * @param localAnchor the local node to adjunct to.
   * @throws LTAGException when adjunction cannot be performed.
   */
  void adjunction(Sltag other, LtagNode localAnchor) throws LTAGException;

  /**
   * Converts the SLTAG into an equivalent SPARQL query.
   * @return the equivalent SPARQL query.
   */
  Query convertToSPARQL();

  /**
   * Return a copy of the SLTAG.
   * @return the copied SLTAG.
   */
  Sltag copy();

  /**
   * Returns the semantics.
   * @return the semantics.
   */
  Dudes getSemantics();

  /**
   * Sets if a {@code SELECT} SPARQL query should be generated.
   * @param select whether or not to generate a {@code SELECT} SPARQL query.
   */
  void setSelect(boolean select);

  /**
   * Executes a substitution with the SLTAG {@code other} matching its root with {@code target}.
   * @param other the SLTAG to adjunct.
   * @param localAnchor the local node to adjunct to.
   * @throws LTAGException when substitution cannot be performed.
   */
  void substitution(Sltag other, LtagNode localAnchor) throws LTAGException;

  /**
   * Executes the substitution on the Ltag.
   * @param other the Ltag to substitute.
   * @param localAnchor the substitution anchor.
   * @throws LTAGException when substitution cannot be executed.
   */
  void substitution(Sltag other, String localAnchor) throws LTAGException;
}
