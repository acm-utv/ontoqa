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

import java.util.List;

/**
 * This interface defines the Lexicalized Tree Adjoining SimpleGrammar (Ltag) data structure.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public interface Ltag {

  /**
   * Checks if the specified node is the Ltag axiom.
   * @param node the node to check.
   * @return true if the node is the axiom; false, otherwise.
   */
  boolean isAxiom(LtagNode node);

  /**
   * Checks if the specified node is a Ltag leaf.
   * @param node the node to check.
   * @return true if the node is a leaf; false, otherwise.
   */
  boolean isLeaf(LtagNode node);

  /**
   * Checks if the specified node is a Ltag anchor.
   * @param node the node to check.
   * @return true if the node is an anchor; false, otherwise.
   */
  boolean isAnchor(LtagNode node);

  /**
   * Returns the Ltag root.
   * @return the Ltag root.
   */
  LtagNode getRoot();

  /**
   * Adds the specified edge to the Ltag.
   * @param lhs the left-hand-side of the edge.
   * @param rhs the right-hand-side of the edge.
   * @return true if the edge has been added to the Ltag; false, otherwise.
   */
  boolean addEdge(LtagNode lhs, LtagNode rhs);

  /**
   * Adds the specified edge to the Ltag.
   * @param lhs the left-hand-side of the edge.
   * @param rhs the right-hand-side of the edge.
   * @param pos the production position (starts from 0).
   * @param replace wheter or not to replace.
   * @return true if the production has been added to the Ltag; false, otherwise.
   */
  boolean addEdge(LtagNode lhs, LtagNode rhs, int pos, boolean replace);

  /**
   * Removes the specified production from the Ltag.
   * @param lhs the left-hand-side of the production.
   * @param rhs the right-hand-side of the production.
   * @return true if the production has been removed from the Ltag; false, otherwise.
   */
  boolean removeProduction(LtagNode lhs, LtagNode rhs);

  /**
   * Checks if the production lhs->rhs is contained by the Ltag.
   * @param lhs the left-hand-side of the production.
   * @param rhs the right-hand-side of the production.
   * @return true if the Ltag contains the production; false, othrwise.
   */
  boolean containsEdge(LtagNode lhs, LtagNode rhs);

  /**
   * Returns the list of all nodes.
   * @return the list of all nodes.
   */
  List<LtagNode> getNodes();

  /**
   * Returns the list of all productions.
   * @return the list of all productions.
   */
  List<LtagEdge> getEdges();

  /**
   * Returns the list of all nodes marked as substitution nodes.
   * @return the list of all nodes marked as substitution nodes.
   */
  List<LtagNode> getSubstitutionNodes();

  /**
   * Returns the list of all nodes marked as adjunction nodes.
   * @return the list of all nodes marked as adjunction nodes.
   */
  List<LtagNode> getAdjunctionNodes();

  /**
   * Returns the parent node of the specified node.
   * @param node the child node.
   * @return the parent if exists; null otherwise.
   */
  LtagNode getLhs(LtagNode node);

  /**
   * Returns the children of the specified node.
   * @param node the parent node.
   * @return the children if parent node exists.
   */
  List<LtagNode> getRhs(LtagNode node);

  /**
   * Checks if the Ltag contains the node.
   * @param node the node to check.
   * @return true if the node belongs to the Ltag; false, otherwise.
   */
  boolean contains(LtagNode node);

  /**
   * Checks if the Ltag contains the production.
   * @param prod the prod to check.
   * @return true if the production belongs to the Ltag; false, otherwise.
   */
  boolean contains(LtagEdge prod);

  /**
   * Checks if the Ltag contains the production.
   * @param lhs the left-hand-side to check.
   * @param rhs the right-hand-side to check.
   * @return true if the production belongs to the Ltag; false, otherwise.
   */
  boolean contains(LtagNode lhs, LtagNode rhs);

  /**
   * Returns the pretty string representation.
   * @return the pretty string representation.
   */
  String toPrettyString();

  /**
   * Copies the current Ltag.
   * @return the copied Ltag.
   */
  Ltag copy();

  /**
   * Returns the Ltag rooted in the specified root.
   * @param root the root node.
   * @return the Ltag if it exists; null, otherwise.
   * @throws LTAGException when Ltag cannot be copied with the specified root.
   */
  Ltag copy(LtagNode root) throws LTAGException;

  /**
   * Appends subtree rooted in {@code otherRoot} from {@code otherLtag} into local Ltag as children
   * of {@code localRoot}.
   * @param otherLtag the Ltag to addSubtree from.
   * @param otherRoot the starting node.
   * @param localRoot the local root.
   */
  void appendSubtreeFrom(Ltag otherLtag, LtagNode otherRoot, LtagNode localRoot);

  /**
   * Appends subtree rooted in {@code otherRoot} from {@code otherLtag} into local Ltag as a
   * replacement of {@code localRoot}.
   * @param otherLtag the Ltag to addSubtree from.
   * @param otherRoot the starting node.
   * @param replaceNode the local node to replace.
   */
  void replaceWithSubtreeFrom(Ltag otherLtag, LtagNode otherRoot, LtagNode replaceNode);

  /**
   * Executes the substitution on the Ltag.
   * @param target the substitution anchor.
   * @param ltag the Ltag to substitute.
   * @throws LTAGException when substitution cannot be executed.
   */
  void substitution(LtagNode target, Ltag ltag) throws LTAGException;

  /**
   * Executes the adjunction on the Ltag.
   * @param target1 the adjunction anchor.
   * @param ltag the Ltag to adjunct.
   * @param target2 the node to adjunct.
   * @throws LTAGException when adjunction cannot be executed.
   */
  void adjunction(LtagNode target1, Ltag ltag, LtagNode target2) throws LTAGException;

}
