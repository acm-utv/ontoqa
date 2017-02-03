package com.acmutv.ontoqa.core.semantics.drs;


import com.acmutv.ontoqa.core.semantics.base.statement.Replace;
import com.acmutv.ontoqa.core.semantics.base.statement.Statement;
import com.acmutv.ontoqa.core.semantics.base.term.Term;
import com.acmutv.ontoqa.core.semantics.base.term.Variable;
import lombok.Data;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementGroup;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class SimpleDrs implements Drs {

  /**
   * The DRS identification number.
   */
  private int label = 0;

  /**
   * The set of variables.
   */
  private Set<Variable>  variables = new HashSet<>();

  /**
   * The set of statements.
   */
  private Set<Statement> statements = new HashSet<>();

  /**
   * The constructor.
   * @param label the identification number.
   */
  public SimpleDrs(int label) {
    this.label = label;
  }

  @Override
  public Set<Integer> collectVariables() {
    HashSet<Integer> vars = new HashSet<>();

    vars.add(this.label);

    for (Variable v : this.variables) {
         vars.add(v.getI());
    }
    for (Statement s : this.statements) {
         vars.addAll(s.collectVariables());
    }

    return vars;
  }

  @Override
  public void rename(int oldValue, int newValue) {
    if (this.label == oldValue) this.label = newValue;
    for (Variable v : this.variables)  {
      v.rename(oldValue, newValue);
    }
    for (Statement s : this.statements) {
      s.rename(oldValue, newValue);
    }
  }

  @Override
  public void rename(String oldValue, String newValue) {
    for (Statement s : this.statements) {
      s.rename(oldValue, newValue);
    }
  }

  @Override
  public void replace(Term oldValue, Term newValue) {
    Set<Variable> new_variables = new HashSet<>();
    for (Variable var : this.variables) {
      if (var.equals(oldValue)) {
        if (newValue.isVariable())  {
          new_variables.add((Variable) newValue);
        }
      } else  new_variables.add(var);
    }
    this.variables = new_variables;

    for (Statement s : statements) s.replace(oldValue,newValue);
  }

  @Override
  public void union(Drs other, int label) {
    if (this.label == label) {
      this.variables.addAll(other.getVariables());
      this.statements.addAll(other.getStatements());
    }
    else {
      for (Statement s : this.statements) {
         s.union(other, label);
      }
    }
  }

  @Override
  public Element convertToRDF(Query top) {
    ElementGroup group = new ElementGroup();
    for (Statement s : this.statements) {
      group.addElement(s.convertToRDF(top));
    }

    if (group.getElements().size() == 1) {
      return group.getElements().get(0);
    } else {
      return group;
    }
  }

  @Override
  public Set<Replace> collectReplacements() {
    Set<Replace> replacements = new HashSet<>();
    for (Statement s : this.statements) {
      replacements.addAll(s.collectReplacements());
    }
    return replacements;
  }

  @Override
  public void postprocess() {
    Set<Replace> replaces = collectReplacements();

    Set<Replace> var2var = new HashSet<>();
    Set<Replace> var2con = new HashSet<>();
    Set<Replace> delete  = new HashSet<>();

    for (Replace r : replaces) {
      if (r.getSource().equals(r.getTarget())) {
        delete.add(r);
        continue;
      }
      if (r.getSource().isVariable()) {
        if (r.getTarget().isVariable()) {
          var2var.add(r);
        } else {
          var2con.add(r);
        }
      }
    }

    // remove all those where source=target
    for (Replace r : delete) {
         this.getStatements().remove(r);
    }
    // first replace those of form REPLACE(var1,var2)
    for (Replace r : var2var) {
      this.getStatements().remove(r);
      replace(r.getSource(),r.getTarget());
    }
    // then replace those of form REPLACE(var,cons)
    for (Replace r : var2con) {
      this.getStatements().remove(r);
      replace(r.getSource(),r.getTarget());
    }
  }

  @Override
  public String toString() {
    return String.format("%d:[%s | %s]",
        this.label,
        this.variables.stream().map(String::valueOf).collect(Collectors.joining(",")),
        this.statements.stream().map(String::valueOf).collect(Collectors.joining(",")));
  }

  @Override
  public SimpleDrs clone() {
    SimpleDrs clone = new SimpleDrs(this.label);

    for (Variable v : this.variables) {
      clone.getVariables().add(v.clone());
    }

    for (Statement s : this.statements) {
      clone.getStatements().add(s.clone());
    }

    return clone;
  }
    
}
