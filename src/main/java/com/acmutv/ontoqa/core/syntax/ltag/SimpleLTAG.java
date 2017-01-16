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
@RequiredArgsConstructor
public class SimpleLTAG extends DelegateTree<LTAGNode, LTAGProduction> implements LTAG {

  private Map<LTAGNode, List<LTAGNode>> productionsOrder = new HashMap<>();

  /**
   * Constructs a new LTAG with the specified axiom.
   * @param axiom the LTAG axiom.
   */
  public SimpleLTAG(LTAGNode axiom) {
    super();
    super.setRoot(axiom);
  }

  /**
   * Checks if the specified node is the LTAG axiom.
   * @param node the node to check.
   * @return true if the node is the axiom; false, otherwise.
   */
  @Override
  public boolean isAxiom(LTAGNode node) {
    return super.getRoot().equals(node);
  }

  /**
   * Checks if the specified node is a LTAG leaf.   *
   * @param node the node to check.
   * @return true if the node is a leaf; false, otherwise.
   */
  @Override
  public boolean isLeaf(LTAGNode node) {
    return super.getChildCount(node) == 0;
  }

  /**
   * Checks if the specified node is a LTAG anchor.
   * @param node the node to check.
   * @return true if the node is an anchor; false, otherwise.
   */
  @Override
  public boolean isAnchor(LTAGNode node) {
    return super.isLeaf(node) && node.getType().equals(LTAGNode.Type.LEX);
  }

  /**
   * Returns the LTAG axiom.
   * @return the LTAG axiom.
   */
  @Override
  public LTAGNode getAxiom() {
    return super.getRoot();
  }

  /**
   * Adds the specified production to the LTAG.
   * @param lhs the left-hand-side of the production.
   * @param rhs the right-hand-side of the production.
   * @return true if the production has been added to the LTAG; false, otherwise.
   */
  @Override
  public boolean addProduction(LTAGNode lhs, LTAGNode rhs) {
    LTAGProduction production = new LTAGProduction(lhs, rhs);
    final boolean added = super.addChild(production, lhs, rhs);
    if (added) {
      this.productionsOrder.putIfAbsent(lhs, new ArrayList<>());
      this.productionsOrder.get(lhs).add(rhs);
    }
    return added;
  }

  /**
   * Adds the specified production to the LTAG.
   *
   * @param lhs the left-hand-side of the production.
   * @param rhs the right-hand-side of the production.
   * @param pos the production position (starts from 0).
   * @param replace wheter or not to replace.
   * @return true if the production has been added to the LTAG; false, otherwise.
   */
  @Override
  public boolean addProduction(LTAGNode lhs, LTAGNode rhs, int pos, boolean replace) {
    LTAGProduction production = new LTAGProduction(lhs, rhs);
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
        this.productionsOrder.put(lhs, new ArrayList<LTAGNode>(){{add(rhs);}});
      }
    }
    return added;
  }

  /**
   * Removes the specified production to the LTAG.
   * @param lhs the left-hand-side of the production.
   * @param rhs the right-hand-side of the production.
   * @return true if the production has been removed from the LTAG; false, otherwise.
   */
  @Override
  public boolean removeProduction(LTAGNode lhs, LTAGNode rhs) {
    boolean removed = super.removeChild(rhs);
    if (removed) {
      this.productionsOrder.get(lhs).remove(rhs);
    }
    return removed;
  }

  /**
   * Checks if the production lhs->rhs is contained by the LTAG.
   * @param lhs the left-hand-side of the production.
   * @param rhs the right-hand-side of the production.
   * @return true if the LTAG contains the production; false, othrwise.
   */
  @Override
  public boolean containsProduction(LTAGNode lhs, LTAGNode rhs) {
    return super.containsEdge(new LTAGProduction(lhs, rhs));
  }

  /**
   * Returns the list of all nodes.
   * @return the list of all nodes.
   */
  @Override
  public List<LTAGNode> getNodes() {
    return super.getVertices().stream().collect(Collectors.toList());
  }

  /**
   * Returns the list of all productions.
   * @return the list of all productions.
   */
  @Override
  public List<LTAGProduction> getProductions() {
    return super.getEdges().stream().collect(Collectors.toList());
  }

  /**
   * Returns the list of all nodes marked as substitution nodes.
   * @return the list of all nodes marked as substitution nodes.
   */
  @Override
  public List<LTAGNode> getSubstitutionNodes() {
    return super.getVertices().stream()
        .filter(e-> e.getMarker().equals(LTAGNode.Marker.SUB))
        .collect(Collectors.toList());
  }

  /**
   * Returns the list of all nodes marked as adjunction nodes.
   * @return the list of all nodes marked as adjunction nodes.
   */
  @Override
  public List<LTAGNode> getAdjunctionNodes() {
    return super.getVertices().stream()
        .filter(e-> e.getMarker().equals(LTAGNode.Marker.ADJ))
        .collect(Collectors.toList());
  }

  /**
   * Returns the parent node of the specified node.
   *
   * @param node the child node.
   * @return the parent if exists; null otherwise.
   */
  @Override
  public LTAGNode getLhs(LTAGNode node) {
    return super.getParent(node);
  }

  /**
   * Returns the children of the specified node.
   *
   * @param node the parent node.
   * @return the children if parent node exists.
   */
  @Override
  public List<LTAGNode> getRhs(LTAGNode node) {
    return this.productionsOrder.get(node);
  }

  /**
   * Checks if the LTAG contains the node.
   * @param node the node to check.
   * @return true if the node belongs to the LTAG; false, otherwise.
   */
  @Override
  public boolean contains(LTAGNode node) {
    return super.containsVertex(node);
  }

  /**
   * Checks if the LTAG contains the production.
   * @param prod the prod to check.
   * @return true if the prod belongs to the LTAG; false, otherwise.
   */
  @Override
  public boolean contains(LTAGProduction prod) {
    return super.containsEdge(prod);
  }

  /**
   * Checks if the LTAG contains the production.
   * @param lhs the left-hand-side to check.
   * @param rhs the right-hand-side to check.
   * @return true if the production belongs to the LTAG; false, otherwise.
   */
  @Override
  public boolean contains(LTAGNode lhs, LTAGNode rhs) {
    return super.containsEdge(new LTAGProduction(lhs, rhs));
  }

  /**
   * Returns the pretty string representation.
   * @return the pretty string representation.
   */
  @Override
  public String toPrettyString() {
    StringJoiner sj = new StringJoiner(" ; ");
    Queue<LTAGNode> frontier = new ConcurrentLinkedQueue<>();
    frontier.add(this.getAxiom());
    while (!frontier.isEmpty()) {
      LTAGNode curr = frontier.poll();
      List<LTAGNode> children = this.getRhs(curr);
      if (children == null) continue;
      children.forEach(child -> {
            sj.add(String.format("%s->%s", curr.toPrettyString(), child.toPrettyString()));
            frontier.add(child);
          });
    }
    return sj.toString();
  }

  /**
   * Copies the current LTAG.
   * @return the copied LTAG.
   */
  @Override
  public LTAG copy() {
    LTAG copied = null;
    try {
      copied = this.copy(this.getAxiom());
    } catch (LTAGException ignored) {/*ignored because never thrown.*/}
    return copied;
  }

  /**
   * Returns the LTAG rooted in the specified root.
   * @param root the root node.
   * @return the LTAG if it exists; null, otherwise.
   * @throws LTAGException when LTAG cannot be copied with the specified root.
   */
  @Override
  public LTAG copy(LTAGNode root) throws LTAGException {
    if (!this.contains(root)) {
      throw new LTAGException("Cannot copy. Root does not belong to LTAG.");
    }
    if (!root.getType().equals(LTAGNode.Type.POS)) {
      throw new LTAGException("Cannot copy. Root is not a POS.");
    }

    LTAG copied = new SimpleLTAG(root);
    this.getRhs(root).forEach((LTAGNode child) ->
        copied.appendSubtreeFrom(this, child, copied.getAxiom()));

    return copied;
  }

  /**
   * Appends subtree rooted in {@code otherRoot} from {@code otherLtag} into local LTAG as children
   * of {@code localRoot}.
   * @param otherLtag the LTAG to add from.
   * @param otherRoot the starting node.
   * @param localRoot the local root.
   */
  @Override
  public void appendSubtreeFrom(LTAG otherLtag, LTAGNode otherRoot, LTAGNode localRoot) {
    this.addProduction(localRoot, otherRoot);
    this.addTraversing(otherLtag, otherRoot);
  }

  private void addTraversing(LTAG otherLtag, LTAGNode otherRoot) {
    Queue<LTAGNode> frontier = new ConcurrentLinkedQueue<>();
    frontier.add(otherRoot);
    while (!frontier.isEmpty()) {
      LTAGNode curr = frontier.poll();
      List<LTAGNode> children = otherLtag.getRhs(curr);
      if (children == null) continue;
      children.forEach(child -> {
        if (this.addProduction(curr, child)) {
          frontier.add(child);
        }
      });
    }
  }

  /**
   * Appends subtree rooted in {@code otherRoot} from {@code otherLtag} into local LTAG as a
   * replacement of {@code localRoot}.   *
   * @param otherLtag the LTAG to addSubtree from.
   * @param otherRoot the starting node.
   * @param replaceNode the local node to replace
   */
  @Override
  public void replaceWithSubtreeFrom(LTAG otherLtag, LTAGNode otherRoot, LTAGNode replaceNode) {
    if (this.isAxiom(replaceNode)) return;
    LTAGNode localParent = super.getParent(replaceNode);
    int position = this.productionsOrder.get(localParent).indexOf(replaceNode);

    List<LTAGNode> toremove = new ArrayList<>();
    Queue<LTAGNode> frontier = new ConcurrentLinkedQueue<>();
    toremove.add(replaceNode);
    frontier.add(replaceNode);
    while (!frontier.isEmpty()) {
      LTAGNode curr = frontier.poll();
      List<LTAGNode> children = this.getRhs(curr);
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
   * Executes the substitution on the LTAG.
   * @param target the substitution anchor.
   * @param ltag the LTAG to substitute.
   * @return the resulting LTAG.
   * @throws LTAGException when substitution cannot be executed.
   */
  @Override
  public LTAG substitution(LTAGNode target, LTAG ltag) throws LTAGException {
    if (!this.contains(target)) {
      throw new LTAGException("Cannot execute substitution. The target does not belong to the LTAG.");
    }
    if (!target.getMarker().equals(LTAGNode.Marker.SUB)) {
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

    LTAG res = this.copy();
    res.replaceWithSubtreeFrom(ltag, ltag.getAxiom(), target);

    return res;
  }

  /**
   * Executes the adjunction on the LTAG.
   * @param target1 the adjunction anchor.
   * @param ltag the LTAG to adjunct.
   * @param target2 the node to adjunct.
   * @return the resulting LTAG.
   * @throws LTAGException when adjunction cannot be executed.
   */
  @Override
  public LTAG adjunction(LTAGNode target1, LTAG ltag, LTAGNode target2) throws LTAGException {
    if (!this.contains(target1) || !ltag.contains(target2)) {
      throw new LTAGException("Cannot execute adjunction. The targets does not belong to the LTAGs.");
    }
    if (!target1.getType().equals(LTAGNode.Type.POS)) {
      throw new LTAGException("Cannot execute adjunction. The 1st targets is not POS.");
    }
    if (!target2.getType().equals(LTAGNode.Type.POS)) {
      throw new LTAGException("Cannot execute adjunction. The 2nd targets is not POS.");
    }
    if (!target1.getLabel().equals(target2.getLabel())) {
      throw new LTAGException("Cannot execute adjunction. The two targets does not have the same label.");
    }
    if (!target1.getMarker().equals(LTAGNode.Marker.NONE)) {
      throw new LTAGException("Cannot execute adjunction. The 1st target is not marked as NONE.");
    }
    if (!target2.getMarker().equals(LTAGNode.Marker.ADJ)) {
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

    LTAG sub1 = this.copy(target1);
    LTAG aux = ltag.copy();
    aux.replaceWithSubtreeFrom(sub1, sub1.getAxiom(), target2);

    LTAG res = this.copy();
    res.replaceWithSubtreeFrom(aux, aux.getAxiom(), target1);

    return res;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof SimpleLTAG)) return false;
    SimpleLTAG other = (SimpleLTAG) obj;
    if (!super.getVertices().containsAll(other.getVertices()))
      return false;
    if (!other.getVertices().containsAll(super.getVertices()))
      return false;
    if (!super.getEdges().containsAll(other.getEdges()))
      return false;
    if (!other.getEdges().containsAll(super.getEdges()))
      return false;
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
    this.getProductions().stream().map(LTAGProduction::toPrettyString).forEach(sb::append);
    sb.append(this.productionsOrder);
    return sb.toString();
  }
}
