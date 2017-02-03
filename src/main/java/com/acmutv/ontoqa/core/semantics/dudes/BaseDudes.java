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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class BaseDudes implements Dudes {

  private Variable mainVariable = null;

  private int mainDrs = 0;

  @NonNull
  private Set<Term> projection = new HashSet<>();

  @NonNull
  private Drs drs = new SimpleDrs(0);

  @NonNull
  private Set<Slot> slots = new HashSet<>();

  private boolean select = true;

  /**
   * The cloning constructor.
   * @param other the DUDES to clone.
   */
  public BaseDudes(Dudes other) {
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

  public void rename(int oldValue, int newValue) {
    if (this.mainVariable != null) {
      this.mainVariable.rename(oldValue,newValue);
    }

    if (this.mainDrs == oldValue) {
      this.mainDrs = newValue;
    }

    for (Term t : this.projection) {
         t.rename(oldValue,newValue);
    }

    for (Slot s : this.slots) {
         s.replace(oldValue,newValue);
    }

    this.drs.rename(oldValue,newValue);
  }

  public void rename(String oldValue, String newValue) {
    this.drs.rename(oldValue,newValue);
  }

  public void replace(Term oldValue, Term newValue) {
    if (oldValue.isVariable() && newValue.isVariable()) {
      rename(((Variable) oldValue).getI(),((Variable) newValue).getI());
    } else if (!oldValue.isVariable() && !newValue.isVariable()) {
      rename(((Constant) oldValue).getValue(),((Constant) newValue).getValue());
    } else if (!oldValue.isVariable() && newValue.isVariable()) {
    // TODO This is only to avoid that alreay replaced constants are again replaced by a variable.
    } else {
      boolean canBeReplaced = true;
      // t_old can only be replaced by t_new if it is not contained in any slot
      if (oldValue.isVariable()) {
        for (Slot slot : this.slots) {
        if  (slot.getVariable().equals(oldValue))
          canBeReplaced = false;
        }
      }
      if (canBeReplaced) {
        this.drs.replace(oldValue,newValue);
        if (oldValue.isVariable()) {
        // if t_old now doesn't occur anywhere in the main body
        if (!this.drs.collectVariables().contains(((Variable) oldValue).getI())) {
          // but t_old is the mainVariable, then delete the main variable
          if (this.mainVariable != null && this.mainVariable.equals(oldValue)) {
            this.mainVariable = null;
          }
          // but t_old is a projection variable, then delete that as well
          if (this.projection.contains(oldValue)) {
            this.projection.remove(oldValue);
          }
        }}
      }
    }
  }

  @Override
  public void merge(Dudes other) {
    this.merge(other, "");
  }

  @Override
  public void merge(Dudes other, String anchor) {
    if (other == null) return;

    BaseDudes d2 = new BaseDudes(other);

    Set<Integer> allVariables = this.collectVariables();
    allVariables.addAll(d2.collectVariables());
    VariableSupply vars = new VariableSupply();
    vars.reset(Collections.max(allVariables));
    for (int i : d2.collectVariables()) {
      d2.rename(i, vars.getFresh());
    }

    if (!this.hasSlot(anchor) && !d2.hasSlot(anchor)) {
      this.union(d2, true);
    } else {
      if (this.hasSlot(anchor)) {
        this.applyTo(d2, anchor);
      }
      if (d2.hasSlot(anchor)) {
        d2.applyTo(this, anchor);
      }
    }
  }

  @Override
  public void setMainDrs(int i) {
    this.mainDrs = i;
  }

  @Override
  public void setMainDrs(Drs drs) {
    this.setDrs(drs);
    this.setMainDrs(drs.getLabel());
  }

  private void applyTo(BaseDudes other, String anchor) {
    if (other.getMainVariable() != null) {
      for (Slot s : this.slots) {
         if (s.getAnchor().equals(anchor)) {
           this.slots.remove(s);
           this.rename(s.getVariable().getI(), other.getMainVariable().getI());
           this.projection.addAll(other.getProjection());
           this.drs.union(other.getDrs(), s.getLabel());
           this.slots.addAll(other.getSlots());
         }
      }
    }
  }

  private void union(BaseDudes other, boolean unify) {
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

  public void postprocess() {
      this.drs.postprocess();
  }

  @Override
  public Query convertToSPARQL() {
    this.postprocess();

    Query query = QueryFactory.make();

    for (Term t : this.projection) {
        query.addResultVar(t.convertToExpr(query));
    }

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
