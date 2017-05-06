package com.acmutv.ontoqa.core.semantics.base.statement;

import com.acmutv.ontoqa.core.semantics.base.term.Term;
import com.acmutv.ontoqa.core.semantics.drs.Drs;
import lombok.Data;
import lombok.NonNull;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.sparql.syntax.ElementUnion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

@Data
public class Disjunction implements Statement {

  private static final Logger LOGGER = LoggerFactory.getLogger(Disjunction.class);

  @NonNull
  private Drs left;

  @NonNull
  private Drs right;

  @Override
  public Set<Integer> collectVariables() {
    Set<Integer> vars = new HashSet<>();
    vars.addAll(this.getLeft().collectVariables());
    vars.addAll(this.getRight().collectVariables());
    return vars;
  }

  @Override
  public void union(Drs drs, int label) {
    LOGGER.debug("Union (label: {})", label);
    this.getLeft().union(drs, label);
    this.getRight().union(drs, label);
  }

  @Override
  public void rename(int i_old, int i_new) {
    this.getLeft().rename(i_old, i_new);
    this.getRight().rename(i_old, i_new);
  }

  @Override
  public void rename(String s_old, String s_new) {
    this.getLeft().rename(s_old,s_new);
    this.getRight().rename(s_old,s_new);
  }

  @Override
  public void replace(Term t_old, Term t_new) {
    this.getLeft().replace(t_old,t_new);
    this.getRight().replace(t_old,t_new);
  }

  @Override
  public Set<Replace> collectReplacements() {
    Set<Replace> replacements = new HashSet<>();
    replacements.addAll(this.getLeft().collectReplacements());
    replacements.addAll(this.getRight().collectReplacements());
    return replacements;
  }

  @Override
  public Element convertToRDF(Query top) {
    // TODO add { left.convertToRDF() } UNION { right.convertToRDF() } to top

    ElementGroup union_left  = new ElementGroup();
    ElementGroup union_right = new ElementGroup();
    union_left.addElement(left.convertToRDF(top));
    union_right.addElement(right.convertToRDF(top));

    ElementUnion union = new ElementUnion();
    union.addElement(union_left);
    union.addElement(union_right);

    return union;
  }

  @Override
  public String toString() {
      return this.getLeft().toString() + "OR" + this.getRight().toString();
  }

  @Override
  public Disjunction clone() {
      return new Disjunction(this.getLeft().clone(), this.getRight().clone());
  }
    
}
