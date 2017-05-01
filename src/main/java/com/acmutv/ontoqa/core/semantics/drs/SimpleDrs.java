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

package com.acmutv.ontoqa.core.semantics.drs;

import com.acmutv.ontoqa.core.semantics.base.statement.Replace;
import com.acmutv.ontoqa.core.semantics.base.statement.Statement;
import com.acmutv.ontoqa.core.semantics.base.term.Term;
import com.acmutv.ontoqa.core.semantics.base.term.Variable;
import lombok.Data;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class SimpleDrs implements Drs {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleDrs.class);

  /**
   * The identification number.
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
   * Creates a new DRS with identified by {@code label}.
   * @param label the identification number.
   */
  public SimpleDrs(int label) {
    this.label = label;
  }

  /**
   * Creates a new DRS as a clone of {@code other}.
   * @param other the DRS to clone.
   */
  public SimpleDrs(Drs other) {
    this.label = other.getLabel();

    for (Variable v : other.getVariables()) {
      this.getVariables().add(v.clone());
    }

    for (Statement s : other.getStatements()) {
      this.getStatements().add(s.clone());
    }
  }

  /**
   * Creates a new DRS, as a clone of the current DRS.
   * @return the cloned DRS.
   */
  @Override
  public Drs clone() {
    Drs clone = new SimpleDrs(this.label);

    for (Variable v : this.getVariables()) {
      clone.getVariables().add(v.clone());
    }

    for (Statement s : this.getStatements()) {
      clone.getStatements().add(s.clone());
    }

    return clone;
  }

  /**
   * Collects the set of all replacements.
   * @return the set of all replacements.
   */
  @Override
  public Set<Replace> collectReplacements() {
    Set<Replace> replacements = new HashSet<>();
    for (Statement s : this.statements) {
      replacements.addAll(s.collectReplacements());
    }
    return replacements;
  }

  /**
   * Collects the set of all variables.
   * @return the set of all variables.
   */
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

  /**
   * Generates the RDF elements, according to the top-level query {@code top}.
   * @param top the top-level query.
   * @return the RDF elements.
   */
  @Override
  public Element convertToRDF(Query top) {
    ElementGroup group = new ElementGroup();
    //TODO bugfix by Giacomo Marciani
    /* bugfix (Giacomo Marciani): start
    for (Statement s : this.statements) {
      group.addElement(s.convertToRDF(top));
    }
    */
    Element emptyElement = new ElementGroup();
    for (Statement s : this.statements) {
      Element element = s.convertToRDF(top);
      if (!element.equals(emptyElement)) {
        group.addElement(element);
      }
    }
    /* bugfix (Giacomo Marciani): end */

    if (group.getElements().size() == 1) {
      return group.getElements().get(0);
    } else {
      return group;
    }
  }

  /**
   * Minimizes the DRS.
   */
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

  /**
   * Renames all the occurrences of the variable {@code oldval} with the variable {@code newval}.
   * @param oldval the old variable.
   * @param newval the new variable.
   */
  @Override
  public void rename(int oldval, int newval) {
    if (this.label == oldval) this.label = newval;
    for (Variable v : this.variables)  {
      v.rename(oldval, newval);
    }
    for (Statement s : this.statements) {
      s.rename(oldval, newval);
    }
  }

  /**
   * Renames all the occurrences of the variable {@code oldval} with the variable {@code newval}.
   * @param oldval the old variable.
   * @param newval the new variable.
   */
  @Override
  public void rename(String oldval, String newval) {
    for (Statement s : this.statements) {
      s.rename(oldval, newval);
    }
  }

  /**
   * Replaces all the occurrences of the term {@code oldval} with the term {@code newval}.
   * @param oldval the old term.
   * @param newval the new term.
   */
  @Override
  public void replace(Term oldval, Term newval) {
    Set<Variable> new_variables = new HashSet<>();
    for (Variable var : this.variables) {
      if (var.equals(oldval)) {
        if (newval.isVariable())  {
          new_variables.add((Variable) newval);
        }
      } else  new_variables.add(var);
    }
    this.variables = new_variables;

    for (Statement s : statements) s.replace(oldval, newval);
  }

  @Override
  public void union(Drs other, int label) {
    if (this.label == label) {
      this.variables.addAll(other.getVariables());
      this.statements.addAll(other.getStatements());
    } else {
      for (Statement s : this.statements) {
         s.union(other, label);
      }
    }
  }

  @Override
  public String toString() {
    return String.format("%d:[%s | %s]",
        this.label,
        this.variables.stream().map(String::valueOf).collect(Collectors.joining(",")),
        this.statements.stream().map(String::valueOf).collect(Collectors.joining(",")));
  }
    
}
