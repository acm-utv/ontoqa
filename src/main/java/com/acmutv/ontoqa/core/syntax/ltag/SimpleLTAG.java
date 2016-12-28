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
import java.util.stream.Collectors;

/**
 * This class realizes a simple syntax tree.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
@Getter
@Setter
@RequiredArgsConstructor
public class SimpleLTAG extends DelegateTree<LTAGNode, LTAGProduction> implements LTAG {

  /**
   * Constructs a new LTAG with the specified axiom.
   * @param axiom the LTAG axiom.
   */
  public SimpleLTAG(LTAGNode axiom) {
    super();
    super.setRoot(axiom);
    super.equals(null);
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
    return super.addChild(new LTAGProduction(lhs, rhs), lhs, rhs);
  }

  /**
   * Removes the specified production to the LTAG.   *
   * @param lhs the left-hand-side of the production.
   * @param rhs the right-hand-side of the production.
   * @return true if the production has been removed from the LTAG; false, otherwise.
   */
  @Override
  public boolean removeProduction(LTAGNode lhs, LTAGNode rhs) {
    return super.removeChild(rhs);
  }

  /**
   * Removes the specified node from the LTAG.
   * @param node the node to remove.
   * @return true if the production has been removed from the LTAG; false, otherwise.
   */
  @Override
  public boolean removeNode(LTAGNode node) {
    return super.removeChild(node);
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
    return this.getProductions().stream()
        .map(LTAGProduction::toPrettyString)
        .collect(Collectors.joining(";"));
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

    Stack<LTAGNode> barrier = new Stack<>();
    barrier.push(root);

    while (!barrier.isEmpty()) {
      LTAGNode curr = barrier.pop();
      Collection<LTAGNode> children = super.getChildren(curr);
      if (children == null) continue;
      children.forEach(child -> {
        if (copied.addProduction(curr, child)) {
          barrier.push(child);
        }
      });
    }

    return copied;
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
    LTAGNode parent = res.getParent(target);
    res.removeProduction(parent, target);
    res.addProduction(parent, ltag.getAxiom());

    Stack<LTAGNode> barrier = new Stack<>();
    barrier.push(ltag.getAxiom());

    while (!barrier.isEmpty()) {
      LTAGNode curr = barrier.pop();
      Collection<LTAGNode> children = ltag.getChildren(curr);
      if (children == null) continue;
      children.forEach(child -> {
        if (res.addProduction(curr, child)) {
          barrier.push(child);
        }
      });
    }

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

    LTAG res = this.copy();
    LTAG sub1 = res.copy(target1);
    LTAGNode resParent = res.getParent(target1);
    res.removeNode(target1);
    LTAG aux = ltag.copy();
    LTAGNode auxParent = aux.getParent(target2);
    aux.removeNode(target2);

    /* aux.insect(auxParent, sub1)*/
    aux.addProduction(auxParent, sub1.getAxiom());
    Stack<LTAGNode> barrier1 = new Stack<>();
    barrier1.push(sub1.getAxiom());

    while (!barrier1.isEmpty()) {
      LTAGNode curr = barrier1.pop();
      Collection<LTAGNode> children = sub1.getChildren(curr);
      if (children == null) continue;
      children.forEach(child -> {
        if (aux.addProduction(curr, child)) {
          barrier1.push(child);
        }
      });
    }

    /* res.insect(resParent, aux)*/
    res.addProduction(resParent, aux.getAxiom());
    Stack<LTAGNode> barrier2 = new Stack<>();
    barrier2.push(aux.getAxiom());

    while (!barrier2.isEmpty()) {
      LTAGNode curr = barrier2.pop();
      Collection<LTAGNode> children = aux.getChildren(curr);
      if (children == null) continue;
      children.forEach(child -> {
        if (res.addProduction(curr, child)) {
          barrier2.push(child);
        }
      });
    }

    return res;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof SimpleLTAG)) return false;
    SimpleLTAG other = (SimpleLTAG) obj;
    if (this.getProductions() == null ?
        other.getProductions() != null
        :
        !this.getProductions().equals(other.getProductions()))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    this.getProductions().forEach(sb::append);
    return sb.toString();
  }
}
