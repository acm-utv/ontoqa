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

import com.acmutv.ontoqa.core.exception.LTAGException;

import java.util.Collection;
import java.util.List;

/**
 * This interface defines the Lexicalized Tree Adjoining Grammar (LTAG) data structure.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public interface LTAG {

  /**
   * Checks if the specified node is the LTAG axiom.
   * @param node the node to check.
   * @return true if the node is the axiom; false, otherwise.
   */
  boolean isAxiom(LTAGNode node);

  /**
   * Checks if the specified node is a LTAG leaf.
   * @param node the node to check.
   * @return true if the node is a leaf; false, otherwise.
   */
  boolean isLeaf(LTAGNode node);

  /**
   * Checks if the specified node is a LTAG anchor.
   * @param node the node to check.
   * @return true if the node is an anchor; false, otherwise.
   */
  boolean isAnchor(LTAGNode node);

  /**
   * Returns the LTAG axiom.
   * @return the LTAG axiom.
   */
  LTAGNode getAxiom();

  /**
   * Adds the specified production to the LTAG.
   * @param lhs the left-hand-side of the production.
   * @param rhs the right-hand-side of the production.
   * @return true if the production has been added to the LTAG; false, otherwise.
   */
  boolean addProduction(LTAGNode lhs, LTAGNode rhs);

  /**
   * Removes the specified production from the LTAG.
   * @param lhs the left-hand-side of the production.
   * @param rhs the right-hand-side of the production.
   * @return true if the production has been removed from the LTAG; false, otherwise.
   */
  boolean removeProduction(LTAGNode lhs, LTAGNode rhs);

  /**
   * Removes the specified node from the LTAG.
   * @param node the node to remove.
   * @return true if the production has been removed from the LTAG; false, otherwise.
   */
  boolean removeNode(LTAGNode node);

  /**
   * Checks if the production lhs->rhs is contained by the LTAG.
   * @param lhs the left-hand-side of the production.
   * @param rhs the right-hand-side of the production.
   * @return true if the LTAG contains the production; false, othrwise.
   */
  boolean containsProduction(LTAGNode lhs, LTAGNode rhs);

  /**
   * Returns the list of all nodes.
   * @return the list of all nodes.
   */
  List<LTAGNode> getNodes();

  /**
   * Returns the list of all productions.
   * @return the list of all productions.
   */
  List<LTAGProduction> getProductions();

  /**
   * Returns the list of all nodes marked as substitution nodes.
   * @return the list of all nodes marked as substitution nodes.
   */
  List<LTAGNode> getSubstitutionNodes();

  /**
   * Returns the list of all nodes marked as adjunction nodes.
   * @return the list of all nodes marked as adjunction nodes.
   */
  List<LTAGNode> getAdjunctionNodes();

  /**
   * Returns the parent node of the specified node.
   * @param node the child node.
   * @return the parent if exists; null otherwise.
   */
  LTAGNode getParent(LTAGNode node);

  /**
   * Returns the children of the specified node.
   * @param node the parent node.
   * @return the children if parent node exists.
   */
  Collection<LTAGNode> getChildren(LTAGNode node);

  /**
   * Checks if the LTAG contains the node.
   * @param node the node to check.
   * @return true if the node belongs to the LTAG; false, otherwise.
   */
  boolean contains(LTAGNode node);

  /**
   * Checks if the LTAG contains the production.
   * @param prod the prod to check.
   * @return true if the production belongs to the LTAG; false, otherwise.
   */
  boolean contains(LTAGProduction prod);

  /**
   * Checks if the LTAG contains the production.
   * @param lhs the left-hand-side to check.
   * @param rhs the right-hand-side to check.
   * @return true if the production belongs to the LTAG; false, otherwise.
   */
  boolean contains(LTAGNode lhs, LTAGNode rhs);

  /**
   * Returns the pretty string representation.
   * @return the pretty string representation.
   */
  String toPrettyString();

  /**
   * Copies the current LTAG.
   * @return the copied LTAG.
   */
  LTAG copy();

  /**
   * Returns the LTAG rooted in the specified root.
   * @param root the root node.
   * @return the LTAG if it exists; null, otherwise.
   * @throws LTAGException when LTAG cannot be copied with the specified root.
   */
  LTAG copy(LTAGNode root) throws LTAGException;

  /**
   * Executes the substitution on the LTAG.
   * @param target the substitution anchor.
   * @param ltag the LTAG to substitute.
   * @return the resulting LTAG.
   * @throws LTAGException when substitution cannot be executed.
   */
  LTAG substitution(LTAGNode target, LTAG ltag) throws LTAGException;

  /**
   * Executes the adjunction on the LTAG.
   * @param target1 the adjunction anchor.
   * @param ltag the LTAG to adjunct.
   * @param target2 the node to adjunct.
   * @return the resulting LTAG.
   * @throws LTAGException when adjunction cannot be executed.
   */
  LTAG adjunction(LTAGNode target1, LTAG ltag, LTAGNode target2) throws LTAGException;

}
