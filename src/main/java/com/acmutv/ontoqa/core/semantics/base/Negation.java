package com.acmutv.ontoqa.core.semantics.base;

import com.acmutv.ontoqa.core.semantics.drs.Drs;
import lombok.Data;
import lombok.NonNull;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.expr.E_Exists;
import org.apache.jena.sparql.expr.E_LogicalNot;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementFilter;

import java.util.Set;

@Data
public class Negation implements Statement {

  @NonNull
  private Drs drs;

  @Override
  public Set<Integer> collectVariables() {
    return drs.collectVariables();
  }

  @Override
  public void union(Drs drs, int label) {
    drs.union(drs, label);
  }

  @Override
  public void rename(int i_old, int i_new) {
    this.drs.rename(i_old, i_new);
  }

  @Override
  public void rename(String s_old, String s_new) {
      this.drs.rename(s_old,s_new);
  }

  @Override
  public void replace(Term t_old, Term t_new) {
      this.drs.replace(t_old,t_new);
  }

  @Override
  public Set<Replace> collectReplacements() {
      return this.drs.collectReplacements();
  }

  @Override
  public Element convertToRDF(Query top) {
      return new ElementFilter(new E_LogicalNot(new E_Exists(this.drs.convertToRDF(top))));
  }

  @Override
  public String toString() {
      return "NOT" + this.drs.toString();
  }

  @Override
  public Negation clone() {
      return new Negation(this.drs.clone());
  }
    
}
