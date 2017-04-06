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
import com.acmutv.ontoqa.core.syntax.SyntaxCategory;

import java.util.List;
import java.util.Properties;

/**
 * This interface defines the Lexicalized Tree Adjoining SimpleGrammar (Ltag) data structure.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public interface Ltag {

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
   * @param pos  the node position ad child of {@code lhs}.
   * @param rename whether or not to rename in case of id collision.
   * @return true if the edge has been added to the Ltag; false, otherwise.
   */
  LtagNode addEdge(LtagNode lhs, LtagNode rhs, Integer pos, boolean rename);

  /**
   * Executes the adjunction on the Ltag.
   * @param other the Ltag to adjunct.
   * @param localAnchor the local node to adjunct to.
   * @throws LTAGException when adjunction cannot be executed.
   */
  void adjunction(Ltag other, LtagNode localAnchor) throws LTAGException;

  /**
   * Appends to {@code localNode} the subtree of {@code otherLtag} rooted in {@code otherNode}.
   * @param localNode the local node to append to.
   * @param otherLtag the LTAG to take the subtree from.
   * @param otherNode the subtree root.
   * @param pos the node position.
   */
  void append(LtagNode localNode, Ltag otherLtag, LtagNode otherNode, Integer pos);

  /**
   * Returns the structural analysis of the LTAG.
   * @return the structural analysis of the LTAG.
   */
  Properties analyze();

  /**
   * Returns the list of nodes visited according to BFS starting at {@code node}.
   * @param node the starting node.
   * @return the list of nodes visited according to BFS starting at {@code node}.
   */
  List<LtagNode> bfs(LtagNode node);

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
   * Returns the first node matching {@code category} after the lexical node with entry {@code start}.
   * @param category the syntax category.
   * @param start the lexical entry.
   * @param marker the node marker.
   * @return the first node matching {@code category} after the lexical node with entry {@code start}.
   */
  LtagNode firstMatch(SyntaxCategory category, String start, LtagNodeMarker marker);

  /**
   * Returns the list of all productions.
   * @return the list of all productions.
   */
  List<LtagEdge> getEdges();

  /**
   * Returns the parent node of the specified node.
   * @param node the child node.
   * @return the parent if exists; null otherwise.
   */
  LtagNode getLhs(LtagNode node);

  /**
   * Returns the node labeled with {@code label}.
   * @param label the node label.
   * @return the node labele with {@code label}, if exists; null otherwise.
   */
  LtagNode getNode(String label);

  /**
   * Returns the list of all nodes.
   * @return the list of all nodes.
   */
  List<LtagNode> getNodes();

  /**
   * Returns the list of all nodes marked with {@code marker}.
   * @param marker the node marker.
   * @return the list of all nodes.
   */
  List<LtagNode> getNodes(LtagNodeMarker marker);

  /**
   * Returns the list of all nodes marked with {@code marker} exploring tree with DFS.
   * @param marker the node marker.
   * @return the list of all nodes.
   */
  List<LtagNode> getNodesDFS(LtagNodeMarker marker);

  /**
   * Returns the children of the specified node.
   * @param node the parent node.
   * @return the children if parent node exists.
   */
  List<LtagNode> getRhs(LtagNode node);

  /**
   * Returns the Ltag root.
   * @return the Ltag root.
   */
  LtagNode getRoot();

  /**
   * Checks if the LTAG is an adjunctable LTAG.
   * @return true if the LTAG is an adjunctable LTAG.
   */
  boolean isAdjunctable();

  /**
   * Checks if the specified node is a Ltag leaf.
   * @param node the node to check.
   * @return true if the node is a leaf; false, otherwise.
   */
  boolean isLeaf(LtagNode node);

  /**
   * Checks if the LTAG has a substitution node left to the first lexical entry node.
   * @return true if the LTAG has a substitution node left to the first lexical entry node; false, otherwise.
   */
  boolean isLeftSub();

  /**
   * Checks if the LTAG is a root sentence LTAG.
   * @return true if the LTAG is a root sentence LTAG; false, otherwise.
   */
  boolean isSentence();

  /**
   * Checks if the specified node is the Ltag axiom.
   * @param node the node to check.
   * @return true if the node is the axiom; false, otherwise.
   */
  boolean isRoot(LtagNode node);

  /**
   * Checks if the specified node is a LTAG terminal node.
   * @param node the node to check.
   * @return true if the node is a terminal node; false, otherwise.
   */
  boolean isTerminal(LtagNode node);

  /**
   * Removes {@code node} from LTAG.
   * @param node the node to remove.
   */
  boolean remove(LtagNode node);

  /**
   * Appends subtree rooted in {@code otherRoot} from {@code otherLtag} into local Ltag as a
   * replacement of {@code localRoot}.
   * @param replaceNode the local node to replace.
   * @param otherLtag the Ltag to addSubtree from.
   * @param otherRoot the starting node.
   */
  void replace(LtagNode replaceNode, Ltag otherLtag, LtagNode otherRoot) throws LTAGException;

  /**
   * Appends to {@code localNode} the subtree of {@code otherLtag} rooted in {@code otherNode}.
   * @param localNode the local node to append to.
   * @param otherLtag the LTAG to take the subtree from.
   * @param otherNode the subtree root.
   */
  void replaceNodeWithSubtreeRootedIn(LtagNode localNode, Ltag otherLtag, LtagNode otherNode) throws LTAGException;

  /**
   * Executes the substitution on the Ltag.
   * @param other the Ltag to substitute.
   * @param localAnchor the substitution anchor.
   * @throws LTAGException when substitution cannot be executed.
   */
  void substitution(Ltag other, String localAnchor) throws LTAGException;

  /**
   * Executes the substitution on the Ltag.
   * @param other the Ltag to substitute.
   * @param localAnchor the substitution anchor.
   * @throws LTAGException when substitution cannot be executed.
   */
  void substitution(Ltag other, LtagNode localAnchor) throws LTAGException;

  /**
   * Returns the pretty string representation.
   * @return the pretty string representation.
   */
  String toPrettyString();
}
