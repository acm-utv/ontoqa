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

package com.acmutv.ontoqa.core.semantics.dudes;

import com.acmutv.ontoqa.core.semantics.base.*;
import com.acmutv.ontoqa.core.semantics.base.slot.Slot;
import com.acmutv.ontoqa.core.semantics.base.term.Constant;
import com.acmutv.ontoqa.core.semantics.base.term.Term;
import com.acmutv.ontoqa.core.semantics.base.term.Variable;
import com.acmutv.ontoqa.core.semantics.drs.Drs;
import com.acmutv.ontoqa.core.semantics.drs.SimpleDrs;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.sparql.syntax.Element;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class SimpleDudes implements Dudes {

  private static final Logger LOGGER = LogManager.getLogger(SimpleDudes.class);

  /**
   * The main variable.
   * Default is {@code null}.
   */
  private Variable mainVariable = null;

  /**
   * The label of the main DRS.
   * Default is {@code 0}.
   */
  private int mainDrs = 0;

  /**
   * The set of projections.
   */
  @NonNull
  private Set<Term> projection = new HashSet<>();

  /**
   * The DRS.
   */
  @NonNull
  private Drs drs = new SimpleDrs(0);

  /**
   * The set of slots.
   */
  @NonNull
  private Set<Slot> slots = new HashSet<>();

  /**
   * Whether or not to generate a {@code SELECT} SPARQL query.
   */
  private boolean select = true;

  /**
   * Creates a new DUDES as a clone of {@code other}.
   * @param other the DUDES to clone.
   */
  public SimpleDudes(Dudes other) {
    this.setMainDrs(other.getMainDrs());

    if (other.getMainVariable() != null) {
      this.setMainVariable(other.getMainVariable().clone());
    }

    for (Term t : other.getProjection()) {
      this.getProjection().add(t.clone());
    }

    for (Slot s : other.getSlots()) {
      this.getSlots().add(s.clone());
    }

    this.setDrs(other.getDrs().clone());
  }

  /**
   * Collects the set of all variables.
   * @return the set of all variables.
   */
  @Override
  public Set<Integer> collectVariables() {

    HashSet<Integer> vars = new HashSet<>();

    if (this.mainVariable != null) {
      vars.add(this.mainVariable.getI());
    }

    for (Term t : this.projection) {
      if (t.isVariable()) {
        vars.add(((Variable) t).getI());
      }
    }

    for (Slot s : this.slots) {
      vars.add(s.getVariable().getI());
    }

    vars.add(this.mainDrs);
    vars.addAll(this.drs.collectVariables());

    return vars;
  }

  /**
   * Converts the DUDES into an equivalent SPARQL query.
   * @return the equivalent SPARQL query.
   */
  @Override
  public Query convertToSPARQL() {
    this.drs.postprocess();

    Query query = QueryFactory.make();

    //TODO bugfix by Giacomo Marciani
    /* bugfix (Giacomo Marciani): start
    for (Term t : this.projection) {
      query.addResultVar(t.convertToExpr(query));
    }
    */
    int functionIdx = 0;
    for (Term t : this.projection) {
      if (t.isFunction()) {
        query.addResultVar("fout" + functionIdx, t.convertToExpr(query));
      } else {
        query.addResultVar(t.convertToExpr(query));
      }
    }
    /* bugfix (Giacomo Marciani): end */

    Element queryBody = this.drs.convertToRDF(query);
    query.setQueryPattern(queryBody);

    if (this.isSelect()) {
      query.setQuerySelectType();
      if (query.getProjectVars().isEmpty()) {
        query.setQueryResultStar(true);
      } else {
        query.setDistinct(true);
      }
    } else {
      query.setQueryAskType();
    }

    return query;
  }

  /**
   * Returns a copy of the DUDES.
   * @return a copy of the DUDES.
   */
  @Override
  public Dudes copy() {
    return new SimpleDudes(this);
  }

  /**
   * Checks if there is a slot for {@code anchor}.
   * @param anchor the anchor to check.
   * @return true if there is a slot for {@code anchor}; false, otherwise.
   */
  @Override
  public boolean hasSlot(String anchor) {
    for (Slot s : this.slots) {
      if (s.getAnchor().equals(anchor)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void merge(Dudes other) {
    this.merge(other, "");
  }

  @Override
  public void merge(Dudes other, String anchor) {
    if (other == null) return;

    SimpleDudes other_clone = new SimpleDudes(other);

    Set<Integer> allVariables = this.collectVariables();
    allVariables.addAll(other_clone.collectVariables());
    VariableSupply vars = new VariableSupply();
    vars.reset(Collections.max(allVariables));
    LOGGER.debug("Slots (other): {}", other_clone.getSlots());
    for (int i : other_clone.collectVariables()) {
      other_clone.rename(i, vars.getFresh());
    }
    LOGGER.debug("Slots (other): {}", other_clone.getSlots());

    if (!this.hasSlot(anchor) && !other_clone.hasSlot(anchor)) { /* adjunction */
      LOGGER.debug("Anchor ({}) not found in DUDES", anchor);
      this.union(other_clone, true);
    } else {
      if (this.hasSlot(anchor)) { /* substitution: other inside this */
        LOGGER.debug("Anchor ({}) found in DUDES (this)", anchor);
        this.applyTo(other_clone, anchor);
      }
      if (other_clone.hasSlot(anchor)) { /* substitution: this inside other */
        LOGGER.debug("Anchor ({}) found in DUDES (other)", anchor);
        other_clone.applyTo(this, anchor);
      }
    }
  }

  private void applyTo(SimpleDudes other, String anchor) {
    if (other.getMainVariable() != null) {
      for (Slot s : this.slots) {
        if (s.getAnchor().equals(anchor)) {
          LOGGER.debug("Slot matched: found anchor {} in slot {}", anchor, s);
          this.slots.remove(s);
          this.rename(s.getVariable().getI(), other.getMainVariable().getI());
          this.projection.addAll(other.getProjection());
          this.drs.union(other.getDrs(), s.getLabel());
          this.slots.addAll(other.getSlots());
        }
      }
    }
  }

  private void union(SimpleDudes other, boolean unify) {
    if (unify) {
      other.rename(other.getMainDrs(), this.mainDrs);
      if (this.mainVariable != null && other.getMainVariable() != null) {
        other.rename(other.getMainVariable().getI(),this.mainVariable.getI());
      }
    }

    this.projection.addAll(other.getProjection());
    this.drs.union(other.getDrs(), this.drs.getLabel());
    for (Slot s : this.slots) {
      if (!other.getSlots().contains(s)) {
        other.getSlots().add(s);
      }
    }
  }

  /**
   * Renames all the occurrences of the variable {@code oldval} with the variable {@code newval}.
   * @param oldval the old variable.
   * @param newval the new variable.
   */
  public void rename(int oldval, int newval) {
    if (this.mainVariable != null) {
      this.mainVariable.rename(oldval, newval);
    }

    if (this.mainDrs == oldval) {
      this.mainDrs = newval;
    }

    for (Term t : this.projection) {
         t.rename(oldval, newval);
    }

    for (Slot s : this.slots) {
         s.replace(oldval, newval);
    }

    this.drs.rename(oldval, newval);
  }

  /**
   * Renames all the occurrences of the variable {@code oldval} with the variable {@code newval}.
   * @param oldval the old variable.
   * @param newval the new variable.
   */
  public void rename(String oldval, String newval) {
    this.drs.rename(oldval, newval);
  }

  /**
   * Replaces all the occurrences of the term {@code oldval} with the term {@code newval}.
   * @param oldval the old term.
   * @param newval the new term.
   */
  public void replace(Term oldval, Term newval) {
    if (oldval.isVariable() && newval.isVariable()) {
      rename(((Variable) oldval).getI(),((Variable) newval).getI());
    } else if (!oldval.isVariable() && !newval.isVariable()) {
      rename(((Constant) oldval).getValue(),((Constant) newval).getValue());
    } else //noinspection StatementWithEmptyBody
      if (!oldval.isVariable() && newval.isVariable()) {
    // TODO This is only to avoid that alreay replaced constants are again replaced by a variable.
    } else {
      boolean canBeReplaced = true;
      // t_old can only be replaced by t_new if it is not contained in any slot
      if (oldval.isVariable()) {
        for (Slot slot : this.slots) {
        if  (slot.getVariable().equals(oldval))
          canBeReplaced = false;
        }
      }
      if (canBeReplaced) {
        this.drs.replace(oldval, newval);
        if (oldval.isVariable()) {
        // if t_old now doesn't occur anywhere in the main body
        if (!this.drs.collectVariables().contains(((Variable) oldval).getI())) {
          // but t_old is the mainVariable, then delete the main variable
          if (this.mainVariable != null && this.mainVariable.equals(oldval)) {
            this.mainVariable = null;
          }
          // but t_old is a projection variable, then delete that as well
          if (this.projection.contains(oldval)) {
            this.projection.remove(oldval);
          }
        }}
      }
    }
  }

  /**
   * Sets the main DRS, giving its label {@code i}.
   * @param i the main DRS label.
   */
  @Override
  public void setMainDrs(int i) {
    this.mainDrs = i;
  }

  /**
   * Sets the main DRS.
   * @param drs the main DRS.
   */
  @Override
  public void setMainDrs(Drs drs) {
    this.setDrs(drs);
    this.setMainDrs(drs.getLabel());
  }

  /**
   * Returns the pretty string representation.
   * @return the pretty string representation.
   */
  @Override
  public String toPrettyString() {
    String variablesLine = String.format("%-2s | %-2s",
        (this.mainVariable != null) ? this.mainVariable : "",
        this.drs.getVariables().stream().map(String::valueOf).collect(Collectors.joining(" ")));
    List<String> statementsLines = this.drs.getStatements().stream().map(String::valueOf).collect(Collectors.toList());
    String slotsLine = this.slots.stream().map(Slot::toPrettyString).collect(Collectors.joining(" "));
    int varlen = variablesLine.length();
    @SuppressWarnings("OptionalGetWithoutIsPresent") int stalen = (!statementsLines.isEmpty()) ?
        statementsLines.stream().map(String::length).max(Comparator.naturalOrder()).get() : 0;
    int slolen = slotsLine.length();
    @SuppressWarnings("OptionalGetWithoutIsPresent") int max = Stream.of(varlen, stalen, slolen).max(Comparator.naturalOrder()).get();
    String delimiter = "+" + new String(new char[max+2]).replace("\0", "-") + "+";
    return String.format(
        "%s\n| %s\n%s\n| %s \n%s\n| %s \n%s",
        delimiter,
        variablesLine,
        delimiter,
        statementsLines.stream().collect(Collectors.joining("\n| ")),
        delimiter,
        slotsLine,
        delimiter);
  }

  /**
   * Returns the string representation.
   * @return the string representation.
   */
  @Override
  public String toString() {
    return String.format("{return: %s | main: (%s,%d) | drs: %s | slots: %s}",
        this.projection.stream().map(String::valueOf).collect(Collectors.joining(" ")),
        this.mainVariable,
        this.mainDrs,
        this.drs,
        this.slots.stream().map(String::valueOf).collect(Collectors.joining(" ")));
  }
    
}
