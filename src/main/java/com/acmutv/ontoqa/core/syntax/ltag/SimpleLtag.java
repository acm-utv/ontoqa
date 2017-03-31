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
import com.google.common.collect.Lists;
import edu.uci.ics.jung.graph.DelegateTree;
import lombok.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * A simple LTAG.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
@Getter
@Setter
public class SimpleLtag extends DelegateTree<LtagNode, LtagEdge> implements Ltag {

  private static final Logger LOGGER = LogManager.getLogger(SimpleLtag.class);

  protected Map<String,LtagNode> labelMap = new HashMap<>();

  protected Map<LtagNode, List<LtagNode>> productionsOrder = new HashMap<>();

  protected SimpleLtag() { /* hidden */ }

  /**
   * Constructs a new LTAG with the specified root.
   * @param root the LTAG root.
   */
  public SimpleLtag(LtagNode root) {
    super();
    super.setRoot(root);
  }

  /**
   * Constructs a new LTAG as a clone of {@code ltag}.
   * @param other the LTAG to clone.
   */
  public SimpleLtag(Ltag other) {
    super();
    LtagNode otherRoot = other.getRoot();
    super.setRoot(new LtagNode(otherRoot));

    List<LtagNode> rootChildren = other.getRhs(otherRoot);

    rootChildren.forEach((LtagNode child) ->
        this.append(this.getRoot(), other, child, null));
  }

  /**
   * Adds the specified production to the Ltag.
   * @param lhs the left-hand-side of the production.
   * @param rhs the right-hand-side of the production.
   * @return true if the production has been added to the Ltag; false, otherwise.
   */
  @Override
  public boolean addEdge(LtagNode lhs, LtagNode rhs) {
    boolean added = super.addChild(new LtagEdge(lhs, rhs), lhs, rhs);
    if (added) {
      this.productionsOrder.putIfAbsent(lhs, new ArrayList<>());
      this.productionsOrder.get(lhs).add(rhs);
      if (rhs.getLabel() != null) {
        this.labelMap.put(rhs.getLabel(), rhs);
      }
    }
    return added;
  }

  /**
   * Adds the specified edge to the Ltag.
   * @param lhs    the left-hand-side of the edge.
   * @param rhs    the right-hand-side of the edge.
   * @param rename whether or not to rename in case of id collision.
   * @return true if the edge has been added to the Ltag; false, otherwise.
   */
  @Override
  public LtagNode addEdge(LtagNode lhs, LtagNode rhs, Integer pos, boolean rename) {
    boolean added = false;
    LtagNode _rhs = new LtagNode(rhs);
    LtagEdge edge = new LtagEdge(lhs, _rhs);
    while (!added) {
      try {
        added = super.addChild(edge, lhs, _rhs);
      } catch (IllegalArgumentException exc) {
        if (rename && super.containsVertex(_rhs)) { // rhs duplication due to id collision
          _rhs.setId(rhs.getId()+1);
          edge = new LtagEdge(lhs, _rhs);
        } else {
          throw new IllegalArgumentException(exc.getMessage());
        }
      }
    }

    this.productionsOrder.putIfAbsent(lhs, new ArrayList<>());

    if (pos != null && pos < this.productionsOrder.size()) {
      this.productionsOrder.get(lhs).add(pos, _rhs);
    } else {
      this.productionsOrder.get(lhs).add(_rhs);
    }

    if (_rhs.getLabel() != null) {
      this.labelMap.put(_rhs.getLabel(), _rhs);
    }

    return _rhs;
  }

  /**
   * Executes the adjunction on the Ltag.
   * @param other  the Ltag to adjunct.
   * @param anchor the node to adjunct to.
   * @throws LTAGException when adjunction cannot be executed.
   */
  @Override
  public void adjunction(Ltag other, String anchor) throws LTAGException {
    LtagNode target1 = this.getNode(anchor);
    List<LtagNode> adjunctionNodes = other.getNodes(LtagNodeMarker.ADJ);
    LtagNode target2 = (!adjunctionNodes.isEmpty()) ?
        other.getNodes(LtagNodeMarker.ADJ).get(0) : null;
    if (target1 == null) {
      throw new LTAGException("The LTAG (base) does not contain a node labeled with " + anchor);
    }
    if (target2 == null) {
      throw new LTAGException("The LTAG (adjoining) does not contain a root");
    }
    this.adjunction(target1, other, target2);
  }

  /**
   * Executes the adjunction on the Ltag.
   *
   * @param other  the Ltag to adjunct.
   * @param anchor the node to adjunct to.
   * @throws LTAGException when adjunction cannot be executed.
   */
  @Override
  public void adjunction(Ltag other, LtagNode anchor) throws LTAGException {

  }

  /**
   * Executes the adjunction on the Ltag.
   * @param anchor1 the adjunction anchor.
   * @param other    the Ltag to adjunct.
   * @param anchor2 the node to adjunct.
   * @throws LTAGException when adjunction cannot be executed.
   */
  @Override
  public void adjunction(String anchor1, Ltag other, String anchor2) throws LTAGException {
    LtagNode target1 = this.getNode(anchor1);
    LtagNode target2 = other.getNode(anchor2);
    if (target1 == null) {
      throw new LTAGException("The LTAG (base) does not contain a node labeled with " + anchor1);
    }
    if (target2 == null) {
      throw new LTAGException("The LTAG (adjoining) does not contain a node labeled with " + anchor2);
    }
    this.adjunction(target1, other, target2);
  }

  /**
   * Executes the adjunction on the Ltag.
   * @param target1 the adjunction anchor.
   * @param other the Ltag to adjunct.
   * @param target2 the node to adjunct.
   * @throws LTAGException when adjunction cannot be executed.
   */
  @Override
  public void adjunction(LtagNode target1, Ltag other, LtagNode target2) throws LTAGException {
    if (!this.contains(target1) || !other.contains(target2)) {
      throw new LTAGException("The targets does not belong to the LTAGs.");
    }
    if (!target1.getType().equals(LtagNodeType.NON_TERMINAL)) {
      throw new LTAGException("The 1st targets is not non-terminal.");
    }
    if (!target2.getType().equals(LtagNodeType.NON_TERMINAL)) {
      throw new LTAGException("The 2nd targets is not non-terminal.");
    }
    if (target1.getMarker() != null) {
      throw new LTAGException("The 1st target is marked.");
    }
    if (!target2.getMarker().equals(LtagNodeMarker.ADJ)) {
      throw new LTAGException("The 2nd target is not marked as ADJ.");
    }
    if (this.isRoot(target1)) {
      throw new LTAGException("The 1st target is the axiom.");
    }
    if (other.isRoot(target2)) {
      throw new LTAGException("The 2nd target is the axiom.");
    }
    if (!other.isLeaf(target2)) {
      throw new LTAGException("The 2nd target is not a leaf.");
    }

    Ltag sub1 = this.copy(target1);
    Ltag aux = other.copy();
    aux.replace(target2, sub1, sub1.getRoot());

    LOGGER.debug("partial: {}", aux.toPrettyString());
    LOGGER.debug("target1: {}", target1);

    this.replace(target1, aux, aux.getRoot());
  }

  /**
   * Appends to {@code localNode} the subtree of {@code otherLtag} rooted in {@code otherNode}.
   * @param localNode the local node to append to.
   * @param otherLtag the LTAG to take the subtree from.
   * @param otherNode the subtree root.
   * @param pos       the node position.
   */
  @Override
  public void append(LtagNode localNode, Ltag otherLtag, LtagNode otherNode, Integer pos) {
    Map<LtagNode, LtagNode> renaming = new HashMap<>();
    LtagNode otherNode_renamed = this.addEdge(localNode, otherNode, pos, true);
    if (!otherNode_renamed.equals(otherNode)) {
      LOGGER.debug("Node {} renamed to {}", otherNode, otherNode_renamed);
      renaming.put(otherNode, otherNode_renamed);
    }

    Queue<LtagNode> frontier = new ConcurrentLinkedQueue<>();
    frontier.add(otherNode);
    while (!frontier.isEmpty()) {
      LtagNode curr = frontier.poll();
      List<LtagNode> children = otherLtag.getRhs(curr);
      if (children == null) continue;
      children.forEach(child -> {
        LtagNode curr_renamed = renaming.getOrDefault(curr, curr);
        LtagNode child_renamed = this.addEdge(curr_renamed, child, null, true);
        if (!child_renamed.equals(child)) {
          LOGGER.debug("Node {} renamed to {}", child, child_renamed);
          renaming.put(child, child_renamed);
        }
        frontier.add(child);
      });
    }
  }

  /**
   * Returns the structural analysis of the LTAG.
   *
   * @return the structural analysis of the LTAG.
   */
  @Override
  public Properties analyze() {
    Properties props = new Properties();
    int lex = 0;
    List<Integer> words = new ArrayList<>();
    List<List<String>> subvec = new ArrayList<List<String>>() {{add(new ArrayList<>());}};
    List<List<String>> adjvec = new ArrayList<List<String>>() {{add(new ArrayList<>());}};

    Stack<LtagNode> stack = new Stack<>();
    stack.push(this.getRoot());
    while (!stack.isEmpty()) {
      LtagNode curr = stack.pop();
      if (curr.getType().equals(LtagNodeType.TERMINAL)) {
        words.add(curr.getLabel().split(" ").length);
        subvec.add(new ArrayList<>());
        adjvec.add(new ArrayList<>());
        lex ++;
      } else if (curr.getMarker() != null) {
        switch (curr.getMarker()) {
          case SUB:
            subvec.get(lex).add(curr.getLabel());
            break;
          case ADJ:
            adjvec.get(lex).add(curr.getLabel());
            break;
          default:
            break;
        }
      }



      for (LtagNode child : Lists.reverse(this.getRhs(curr))) {
        stack.push(child);
      }
    }

    props.put("lex", lex);
    props.put("words", words);
    props.put("subvec", subvec);
    props.put("adjvec", adjvec);

    return props;
  }

  /**
   * Returns the list of nodes visited according to BFS starting at {@code node}.
   * @param node the starting node.
   * @return the list of nodes visited according to BFS starting at {@code node}.
   */
  @Override
  public List<LtagNode> bfs(LtagNode node) {
    List<LtagNode> nodes = new ArrayList<>();
    Queue<LtagNode> frontier = new ConcurrentLinkedQueue<>();
    nodes.add(node);
    frontier.add(node);
    while (!frontier.isEmpty()) {
      LtagNode curr = frontier.poll();
      List<LtagNode> children = this.getRhs(curr);
      if (children == null) continue;
      children.forEach(child -> {
        if (nodes.add(child)) {
          frontier.add(child);
        }
      });
    }
    return nodes;
  }

  /**
   * Checks if the Ltag contains the node.
   * @param node the node to check.
   * @return true if the node belongs to the Ltag; false, otherwise.
   */
  @Override
  public boolean contains(LtagNode node) {
    return super.containsVertex(node);
  }

  /**
   * Checks if the Ltag contains the production.
   * @param prod the prod to check.
   * @return true if the prod belongs to the Ltag; false, otherwise.
   */
  @Override
  public boolean contains(LtagEdge prod) {
    return super.containsEdge(prod);
  }

  /**
   * Checks if the Ltag contains the production.
   * @param lhs the left-hand-side to check.
   * @param rhs the right-hand-side to check.
   * @return true if the production belongs to the Ltag; false, otherwise.
   */
  @Override
  public boolean contains(LtagNode lhs, LtagNode rhs) {
    return super.containsEdge(new LtagEdge(lhs, rhs));
  }

  /**
   * Copies the current Ltag.
   * @return the copied Ltag.
   */
  @Override
  public Ltag copy() {
    Ltag copied = null;
    try {
      copied = this.copy(this.getRoot());
    } catch (LTAGException ignored) {/*ignored because never thrown.*/}
    return copied;
  }

  /**
   * Returns the Ltag rooted in the specified root.
   * @param root the root node.
   * @return the Ltag if it exists; null, otherwise.
   * @throws LTAGException when Ltag cannot be copied with the specified root.
   */
  @Override
  public Ltag copy(LtagNode root) throws LTAGException {
    if (!this.contains(root)) {
      throw new LTAGException("Root does not belong to LTAG.");
    }
    Ltag copied = new SimpleLtag(root);
    this.getRhs(root).forEach((LtagNode child) ->
        copied.append(copied.getRoot(), this, child, null));
    return copied;
  }

  @Override
  public LtagNode firstMatch(SyntaxCategory category, String start) {
    //TODO
    return null;
  }

  /**
   * Returns the node labeled with {@code label}.
   * @param label the node label.
   * @return the node labele with {@code label}, if exists; null otherwise.
   */
  @Override
  public LtagNode getNode(String label) {
    return this.labelMap.get(label);
  }

  /**
   * Returns the list of all nodes.
   * @return the list of all nodes.
   */
  @Override
  public List<LtagNode> getNodes() {
    return new ArrayList<>(super.getVertices());
  }

  /**
   * Returns the list of all productions.
   * @return the list of all productions.
   */
  @Override
  public List<LtagEdge> getEdges() {
    return new ArrayList<>(super.getEdges());
  }

  /**
   * Returns the list of all nodes marked as adjunction nodes.
   * @return the list of all nodes marked as adjunction nodes.
   */
  @Override
  public List<LtagNode> getNodes(LtagNodeMarker marker) {
    return super.getVertices().stream()
        .filter(e -> e.getMarker() != null && e.getMarker().equals(marker))
        .collect(Collectors.toList());
  }

  /**
   * Returns the parent node of the specified node.
   *
   * @param node the child node.
   * @return the parent if exists; null otherwise.
   */
  @Override
  public LtagNode getLhs(LtagNode node) {
    return super.getParent(node);
  }

  /**
   * Returns the children of the specified node.
   *
   * @param node the parent node.
   * @return the children if parent node exists.
   */
  @Override
  public List<LtagNode> getRhs(LtagNode node) {
    return this.productionsOrder.getOrDefault(node, new ArrayList<>( ));
  }

  /**
   * Returns the Ltag axiom.
   * @return the Ltag axiom.
   */
  @Override
  public LtagNode getRoot() {
    return super.getRoot();
  }

  /**
   * Checks if the LTAG is an adjunctable LTAG.   *
   * @return true if the LTAG is an adjunctable LTAG.
   */
  @Override
  public boolean isAdjunctable() {
    SyntaxCategory rootCategory = super.getRoot().getCategory();
    for (LtagNode node : super.getVertices()) {
      LtagNodeMarker marker = node.getMarker();
      if (marker != null && marker.equals(LtagNodeMarker.ADJ) && node.getCategory().equals(rootCategory)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if the specified node is a Ltag leaf.
   * @param node the node to check.
   * @return true if the node is a leaf; false, otherwise.
   */
  @Override
  public boolean isLeaf(LtagNode node) {
    return super.getChildCount(node) == 0;
  }

  /**
   * Checks if the LTAG has a substitution node left to the first lexical entry node.   *
   * @return true if the LTAG has a substitution node left to the first lexical entry node; false, otherwise.
   */
  @Override
  public boolean isLeftSub() {
    boolean foundSub = false;
    boolean foundLexical = false;

    Queue<LtagNode> frontier = new ConcurrentLinkedQueue<>();
    frontier.add(super.getRoot());
    while (!frontier.isEmpty()) {
      LtagNode curr = frontier.poll();
      List<LtagNode> children = this.getRhs(curr);
      if (children == null) continue;
      for (LtagNode child : children) {
        LtagNodeMarker marker = child.getMarker();
        if (marker != null && marker.equals(LtagNodeMarker.SUB)) {
          return true;
        } else if (child.getType().equals(LtagNodeType.TERMINAL)) {
          return false;
        }
      }
    }

    return false;
  }

  /**
   * Checks if the LTAG is a root sentence LTAG.
   * @return true if the LTAG is a root sentence LTAG; false, otherwise.
   */
  @Override
  public boolean isSentence() {
    boolean rootSentence = super.getRoot().getCategory().equals(SyntaxCategory.S);
    return rootSentence && !this.isAdjunctable();
  }

  /**
   * Checks if the specified node is the Ltag root.
   * @param node the node to check.
   * @return true if the node is the root; false, otherwise.
   */
  @Override
  public boolean isRoot(LtagNode node) {
    return super.getRoot().equals(node);
  }

  /**
   * Checks if the specified node is a LTAG terminal node.
   * @param node the node to check.
   * @return true if the node is a terminal node; false, otherwise.
   */
  @Override
  public boolean isTerminal(LtagNode node) {
    return super.isLeaf(node) && node.getType().equals(LtagNodeType.TERMINAL);
  }

  /**
   * Removes {@code node} from LTAG.
   * @param node the node to remove.
   */
  @Override
  public boolean remove(LtagNode node) {
    List<LtagNode> toremove = this.bfs(node);
    boolean removed = super.removeChild(node);
    if (removed) {
      toremove.add(node);
      toremove.forEach(n -> {
        this.productionsOrder.remove(n);
        this.productionsOrder.values().forEach(l -> l.remove(n));
        this.labelMap.remove(n.getLabel());
      });
    }
    return removed;
  }

  /**
   * Appends subtree rooted in {@code otherRoot} from {@code otherLtag} into local Ltag as a
   * replacement of {@code localRoot}.
   * @param replaceNode the local node to replace.
   * @param otherLtag the Ltag to addSubtree from.
   * @param otherRoot the starting node.
   */
  @Override
  public void replace(LtagNode replaceNode, Ltag otherLtag, LtagNode otherRoot) throws LTAGException {
    if (this.isRoot(replaceNode)) {
      throw new LTAGException("Cannot replace root.");
    }
    LtagNode localParent = this.getParent(replaceNode);
    int pos = this.productionsOrder.get(localParent).indexOf(replaceNode);

    this.remove(replaceNode);
    this.append(localParent, otherLtag, otherRoot, pos);
  }

  /**
   * Executes the substitution on the Ltag.
   * @param anchor the substitution anchor.
   * @param other   the Ltag to substitute.
   * @throws LTAGException when substitution cannot be executed.
   */
  @Override
  public void substitution(String anchor, Ltag other) throws LTAGException {
    LtagNode target = this.labelMap.get(anchor);
    if (target == null) {
      throw new LTAGException("The LTAG (base) does not contain a node labeled with " + anchor);
    }
    this.substitution(target, other);
  }

  /**
   * Executes the substitution on the Ltag.
   * @param target the substitution anchor.
   * @param other the Ltag to substitute.
   * @throws LTAGException when substitution cannot be executed.
   */
  @Override
  public void substitution(LtagNode target, Ltag other) throws LTAGException {
    LOGGER.debug("target={}", target);
    if (!this.contains(target)) {
      throw new LTAGException("LTAG (base) does not contain the target.");
    }
    if (!target.getMarker().equals(LtagNodeMarker.SUB)) {
      throw new LTAGException("The target is not marked for substitution.");
    }
    if (!target.getType().equals(other.getRoot().getType())) {
      throw new LTAGException("The target and the root do not match.");
    }
    if (this.isRoot(target)) {
      throw new LTAGException("The target is the LTAG (base) root.");
    }
    if (this.isTerminal(target)) {
      throw new LTAGException("The target is an anchor.");
    }

    this.replace(target, other, other.getRoot());
  }

  /**
   * Returns the pretty string representation.
   * @return the pretty string representation.
   */
  @Override
  public String toPrettyString() {
    StringJoiner sj = new StringJoiner(" ; ");
    Queue<LtagNode> frontier = new ConcurrentLinkedQueue<>();
    frontier.add(this.getRoot());
    while (!frontier.isEmpty()) {
      LtagNode curr = frontier.poll();
      List<LtagNode> children = this.getRhs(curr);
      if (children == null) continue;
      children.forEach(child -> {
        sj.add(String.format("%s->%s", curr.toString(), child.toString()));
        frontier.add(child);
      });
    }
    return sj.toString();
  }

  /**
   * Returns the string representation.
   * @return the string representation.
   */
  @Override
  public String toString() {
    return String.format("%s %s",
        this.getEdges().stream().map(LtagEdge::toString).collect(Collectors.joining(" ; ")),
        this.productionsOrder);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof SimpleLtag)) return false;
    SimpleLtag other = (SimpleLtag) obj;
    if (!super.getVertices().containsAll(other.getVertices()))
      return false;
    if (!other.getVertices().containsAll(super.getVertices()))
      return false;
    if (!super.getEdges().containsAll(other.getEdges()))
      return false;
    if (!other.getEdges().containsAll(super.getEdges()))
      return false;
    //noinspection RedundantIfStatement
    if (this.productionsOrder == null ?
        other.productionsOrder != null
        :
        !this.productionsOrder.equals(other.productionsOrder)
        )
      return false;
    return true;
  }

  @SuppressWarnings("EmptyMethod")
  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
