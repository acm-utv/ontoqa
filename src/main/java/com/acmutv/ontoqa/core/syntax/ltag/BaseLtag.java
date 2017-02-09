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
import edu.uci.ics.jung.graph.DelegateTree;
import lombok.*;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * A simple syntax tree.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
@Getter
@Setter
public class BaseLtag extends DelegateTree<LtagNode, LtagProduction> implements Ltag {

  protected IdSupplier ids = new IdSupplier(0);

  protected Map<LtagNode, List<LtagNode>> productionsOrder = new HashMap<>();

  protected BaseLtag() { /* hidden */ }

  /**
   * Constructs a new Ltag with the specified axiom.
   * @param axiom the Ltag axiom.
   */
  public BaseLtag(LtagNode axiom) {
    super();
    super.setRoot(axiom);
  }

  /**
   * Constructs a new Ltag as a clone of {@code ltag}.
   * @param other the Ltag to clone
   */
  public BaseLtag(Ltag other) {
    super();
    LtagNode otherAxiom = other.getAxiom();
    super.setRoot(new LtagNode(otherAxiom));

    List<LtagNode> rootChildren = other.getRhs(otherAxiom);
    assert rootChildren != null;

    rootChildren.forEach((LtagNode child) ->
        this.appendSubtreeFrom(other, child, otherAxiom));
  }

  /**
   * Checks if the specified node is the Ltag axiom.
   * @param node the node to check.
   * @return true if the node is the axiom; false, otherwise.
   */
  @Override
  public boolean isAxiom(LtagNode node) {
    return super.getRoot().equals(node);
  }

  /**
   * Checks if the specified node is a Ltag leaf.   *
   * @param node the node to check.
   * @return true if the node is a leaf; false, otherwise.
   */
  @Override
  public boolean isLeaf(LtagNode node) {
    return super.getChildCount(node) == 0;
  }

  /**
   * Checks if the specified node is a Ltag anchor.
   * @param node the node to check.
   * @return true if the node is an anchor; false, otherwise.
   */
  @Override
  public boolean isAnchor(LtagNode node) {
    return super.isLeaf(node) && node.getType().equals(LtagNode.Type.LEX);
  }

  /**
   * Returns the Ltag axiom.
   * @return the Ltag axiom.
   */
  @Override
  public LtagNode getAxiom() {
    return super.getRoot();
  }

  /**
   * Adds the specified production to the Ltag.
   * @param lhs the left-hand-side of the production.
   * @param rhs the right-hand-side of the production.
   * @return true if the production has been added to the Ltag; false, otherwise.
   */
  @Override
  public boolean addProduction(LtagNode lhs, LtagNode rhs) {
    LtagProduction production = new LtagProduction(lhs, rhs);
    final boolean added = super.addChild(production, lhs, rhs);
    if (added) {
      this.productionsOrder.putIfAbsent(lhs, new ArrayList<>());
      this.productionsOrder.get(lhs).add(rhs);
    }
    return added;
  }

  /**
   * Adds the specified production to the Ltag.
   *
   * @param lhs the left-hand-side of the production.
   * @param rhs the right-hand-side of the production.
   * @param pos the production position (starts from 0).
   * @param replace wheter or not to replace.
   * @return true if the production has been added to the Ltag; false, otherwise.
   */
  @Override
  public boolean addProduction(LtagNode lhs, LtagNode rhs, int pos, boolean replace) {
    LtagProduction production = new LtagProduction(lhs, rhs);
    final boolean added = super.addChild(production, lhs, rhs);
    if (added) {
      if (this.productionsOrder.containsKey(lhs)) {
        try {
          if (replace) {
            this.productionsOrder.get(lhs).set(pos, rhs);
          } else {
            this.productionsOrder.get(lhs).add(pos, rhs);
          }
        } catch (IndexOutOfBoundsException exc) {
          this.productionsOrder.get(lhs).add(rhs);
        }
      } else {
        this.productionsOrder.put(lhs, new ArrayList<LtagNode>(){{add(rhs);}});
      }
    }
    return added;
  }

  /**
   * Removes the specified production to the Ltag.
   * @param lhs the left-hand-side of the production.
   * @param rhs the right-hand-side of the production.
   * @return true if the production has been removed from the Ltag; false, otherwise.
   */
  @Override
  public boolean removeProduction(LtagNode lhs, LtagNode rhs) {
    boolean removed = super.removeChild(rhs);
    if (removed) {
      this.productionsOrder.get(lhs).remove(rhs);
    }
    return removed;
  }

  /**
   * Checks if the production lhs->rhs is contained by the Ltag.
   * @param lhs the left-hand-side of the production.
   * @param rhs the right-hand-side of the production.
   * @return true if the Ltag contains the production; false, othrwise.
   */
  @Override
  public boolean containsProduction(LtagNode lhs, LtagNode rhs) {
    return super.containsEdge(new LtagProduction(lhs, rhs));
  }

  /**
   * Returns the list of all nodes.
   * @return the list of all nodes.
   */
  @Override
  public List<LtagNode> getNodes() {
    return super.getVertices().stream().collect(Collectors.toList());
  }

  /**
   * Returns the list of all productions.
   * @return the list of all productions.
   */
  @Override
  public List<LtagProduction> getProductions() {
    return super.getEdges().stream().collect(Collectors.toList());
  }

  /**
   * Returns the list of all nodes marked as substitution nodes.
   * @return the list of all nodes marked as substitution nodes.
   */
  @Override
  public List<LtagNode> getSubstitutionNodes() {
    return super.getVertices().stream()
        .filter(e-> e.getMarker().equals(LtagNode.Marker.SUB))
        .collect(Collectors.toList());
  }

  /**
   * Returns the list of all nodes marked as adjunction nodes.
   * @return the list of all nodes marked as adjunction nodes.
   */
  @Override
  public List<LtagNode> getAdjunctionNodes() {
    return super.getVertices().stream()
        .filter(e-> e.getMarker().equals(LtagNode.Marker.ADJ))
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
    return this.productionsOrder.get(node);
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
  public boolean contains(LtagProduction prod) {
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
    return super.containsEdge(new LtagProduction(lhs, rhs));
  }

  /**
   * Returns the pretty string representation.
   * @return the pretty string representation.
   */
  @Override
  public String toPrettyString() {
    StringJoiner sj = new StringJoiner(" ; ");
    Queue<LtagNode> frontier = new ConcurrentLinkedQueue<>();
    frontier.add(this.getAxiom());
    while (!frontier.isEmpty()) {
      LtagNode curr = frontier.poll();
      List<LtagNode> children = this.getRhs(curr);
      if (children == null) continue;
      children.forEach(child -> {
            sj.add(String.format("%s->%s", curr, child));
            frontier.add(child);
          });
    }
    return sj.toString();
  }

  /**
   * Copies the current Ltag.
   * @return the copied Ltag.
   */
  @Override
  public Ltag copy() {
    Ltag copied = null;
    try {
      copied = this.copy(this.getAxiom());
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
      throw new LTAGException("Cannot copy. Root does not belong to Ltag.");
    }
    if (!root.getType().equals(LtagNode.Type.POS)) {
      throw new LTAGException("Cannot copy. Root is not a POS.");
    }

    Ltag copied = new BaseLtag(root);
    this.getRhs(root).forEach((LtagNode child) ->
        copied.appendSubtreeFrom(this, child, copied.getAxiom()));

    return copied;
  }

  /**
   * Appends subtree rooted in {@code otherRoot} from {@code otherLtag} into local Ltag as children
   * of {@code localRoot}.
   * @param otherLtag the Ltag to add from.
   * @param otherRoot the starting node.
   * @param localRoot the local root.
   */
  @Override
  public void appendSubtreeFrom(Ltag otherLtag, LtagNode otherRoot, LtagNode localRoot) {
    this.addProduction(localRoot, otherRoot);
    this.addTraversing(otherLtag, otherRoot);
  }

  private void addTraversing(Ltag otherLtag, LtagNode otherRoot) {
    Queue<LtagNode> frontier = new ConcurrentLinkedQueue<>();
    frontier.add(otherRoot);
    while (!frontier.isEmpty()) {
      LtagNode curr = frontier.poll();
      List<LtagNode> children = otherLtag.getRhs(curr);
      if (children == null) continue;
      children.forEach(child -> {
        if (this.addProduction(curr, child)) {
          frontier.add(child);
        }
      });
    }
  }

  /**
   * Appends subtree rooted in {@code otherRoot} from {@code otherLtag} into local Ltag as a
   * replacement of {@code localRoot}.   *
   * @param otherLtag the Ltag to addSubtree from.
   * @param otherRoot the starting node.
   * @param replaceNode the local node to replace
   */
  @Override
  public void replaceWithSubtreeFrom(Ltag otherLtag, LtagNode otherRoot, LtagNode replaceNode) {
    if (this.isAxiom(replaceNode)) return;
    LtagNode localParent = super.getParent(replaceNode);
    int position = this.productionsOrder.get(localParent).indexOf(replaceNode);

    List<LtagNode> toremove = new ArrayList<>();
    Queue<LtagNode> frontier = new ConcurrentLinkedQueue<>();
    toremove.add(replaceNode);
    frontier.add(replaceNode);
    while (!frontier.isEmpty()) {
      LtagNode curr = frontier.poll();
      List<LtagNode> children = this.getRhs(curr);
      if (children == null) continue;
      children.forEach(child -> {
        if (toremove.add(child)) {
          frontier.add(child);
        }
      });
    }
    toremove.forEach(node -> {
      super.removeChild(replaceNode);
      this.productionsOrder.remove(node);
      this.productionsOrder.values().forEach(l -> l.remove(node));
    });

    this.addProduction(localParent, otherRoot, position, false);
    this.addTraversing(otherLtag, otherRoot);
  }

  /**
   * Executes the substitution on the Ltag.
   * @param target the substitution anchor.
   * @param ltag the Ltag to substitute.
   * @throws LTAGException when substitution cannot be executed.
   */
  @Override
  public void substitution(LtagNode target, Ltag ltag) throws LTAGException {
    if (!this.contains(target)) {
      throw new LTAGException("Cannot execute substitution. The target does not belong to the Ltag.");
    }
    if (!target.getMarker().equals(LtagNode.Marker.SUB)) {
      throw new LTAGException("Cannot execute substitution. The target is not marked as SUB.");
    }
    if (!target.getType().equals(ltag.getAxiom().getType())) {
      throw new LTAGException("Cannot execute substitution. The target and the root do not have the same POS class.");
    }
    if (this.isAxiom(target)) {
      throw new LTAGException("Cannot execute substitution. The target is the axiom.");
    }
    if (this.isAnchor(target)) {
      throw new LTAGException("Cannot execute substitution. The target is an anchor.");
    }

    this.replaceWithSubtreeFrom(ltag, ltag.getAxiom(), target);
  }

  /**
   * Executes the adjunction on the Ltag.
   * @param target1 the adjunction anchor.
   * @param ltag the Ltag to adjunct.
   * @param target2 the node to adjunct.
   * @throws LTAGException when adjunction cannot be executed.
   */
  @Override
  public void adjunction(LtagNode target1, Ltag ltag, LtagNode target2) throws LTAGException {
    if (!this.contains(target1) || !ltag.contains(target2)) {
      throw new LTAGException("Cannot execute adjunction. The targets does not belong to the LTAGs.");
    }
    if (!target1.getType().equals(LtagNode.Type.POS)) {
      throw new LTAGException("Cannot execute adjunction. The 1st targets is not POS.");
    }
    if (!target2.getType().equals(LtagNode.Type.POS)) {
      throw new LTAGException("Cannot execute adjunction. The 2nd targets is not POS.");
    }
    if (!target1.getLabel().equals(target2.getLabel())) {
      throw new LTAGException("Cannot execute adjunction. The two targets does not have the same label.");
    }
    if (target1.getMarker() != null) {
      throw new LTAGException("Cannot execute adjunction. The 1st target is marked.");
    }
    if (!target2.getMarker().equals(LtagNode.Marker.ADJ)) {
      throw new LTAGException("Cannot execute adjunction. The 2nd target is not marked as ADJ.");
    }
    if (this.isAxiom(target1)) {
      throw new LTAGException("Cannot execute adjunction. The 1st target is the axiom.");
    }
    if (ltag.isAxiom(target2)) {
      throw new LTAGException("Cannot execute adjunction. The 2nd target is the axiom.");
    }
    if (!ltag.isLeaf(target2)) {
      throw new LTAGException("Cannot execute substitution. The 2nd target is not a leaf.");
    }

    Ltag sub1 = this.copy(target1);
    Ltag aux = ltag.copy();
    aux.replaceWithSubtreeFrom(sub1, sub1.getAxiom(), target2);

    this.replaceWithSubtreeFrom(aux, aux.getAxiom(), target1);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof BaseLtag)) return false;
    BaseLtag other = (BaseLtag) obj;
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

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    this.getProductions().stream().map(LtagProduction::toPrettyString).forEach(sb::append);
    sb.append(this.productionsOrder);
    return sb.toString();
  }
}
