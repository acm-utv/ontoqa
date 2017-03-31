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
import com.acmutv.ontoqa.core.syntax.SyntaxCategory;
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
   * Executes an adjunction with the SLTAG {@code other} matching {@code target1} and {@code target2}.
   * @param other the SLTAG to adjunct.
   * @param target1 the local node to adjunct to.
   * @throws LTAGException when adjunction cannot be performed.
   */
  boolean adjunction(Sltag other, LtagNode target1) throws LTAGException;

  /**
   * Executes an adjunction with the SLTAG {@code other} matching {@code target1} and {@code target2}.
   * @param other the SLTAG to adjunct.
   * @param target1 the local node to adjunct to.
   * @param target2 the node of {@code other} to adjunct.
   * @throws LTAGException when adjunction cannot be performed.
   */
  void adjunction(Sltag other, LtagNode target1, LtagNode target2) throws LTAGException;

  /**
   * Executes the adjunction on the SLTAG.
   * @param other the SLTAG to adjunct.
   * @param anchor the adjunction anchor.
   * @throws LTAGException when adjunction cannot be executed.
   */
  void adjunction(Sltag other, String anchor) throws LTAGException;

  /**
   * Executes the adjunction on the SLTAG.
   * @param other the SLTAG to adjunct.
   * @param anchor1 the adjunction anchor.
   * @param anchor2 the node to adjunct.
   * @throws LTAGException when adjunction cannot be executed.
   */
  void adjunction(Sltag other, String anchor1, String anchor2) throws LTAGException;

  /**
   * Converts the SLTAG into an equivalent SPARQL query.
   * @return the equivalent SPARQL query.
   */
  Query convertToSPARQL();

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
   * @param target the local node to adjunct to.
   * @throws LTAGException when substitution cannot be performed.
   */
  boolean substitution(Sltag other, LtagNode target) throws LTAGException;

  LtagNode firstMatch(SyntaxCategory category, String start);

  boolean substitution(Sltag other, String anchor) throws LTAGException;

  boolean isLeftSub();

  boolean isAdjunctable();

  boolean isSentence();
}
