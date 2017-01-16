package com.acmutv.ontoqa.core.semantics.base;

import com.acmutv.ontoqa.core.semantics.drs.Drs;
import lombok.Data;
import lombok.NonNull;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.expr.*;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementGroup;

import java.util.HashSet;
import java.util.Set;

@Data
public class OperatorStatement implements Statement {

  public enum Operator { EQUALS, LESS, LESSEQUALS, GREATER, GREATEREQUALS, MAX, MIN }

  @NonNull
  private Operator operator;

  @NonNull
  private Term left;

  @NonNull
  private Term right;

  @Override
  public Set<Integer> collectVariables() {
      HashSet<Integer> vars = new HashSet<>();
      if (this.left.isVariable()) {
        vars.add(((Variable) this.left).getI());
      }
      if (this.right.isVariable()) {
        vars.add(((Variable) this.right).getI());
      }
      return vars;
  }

  @Override
  public void union(Drs drs, int label) {
    //TODO
  }

  @Override
  public void rename(int i_old, int i_new) {
    left.rename(i_old, i_new);
    right.rename(i_old, i_new);
  }

  @Override
  public void rename(String s_old, String s_new) {
    left.rename(s_old, s_new);
    right.rename(s_old, s_new);
  }

  @Override
  public void replace(Term t_old, Term t_new) {
    this.left = this.left.replace(t_old,t_new);
    this.right = this.right.replace(t_old,t_new);
  }

  @Override
  public Set<Replace> collectReplacements() {
    return new HashSet<>();
  }

  @Override
  public Element convertToRDF(Query top) {

      if (left.isVariable()) {
          // top.addGroupBy(left.toString());
      }
      else if (left.isFunction()) {
          Term t = ((Function) left).getTerm();
          if (t.isVariable()) top.addGroupBy(t.toString());
      }
      else if (right.isVariable()) {
          // top.addGroupBy(right.toString());
      }

      switch (operator) {
          case EQUALS:        top.addHavingCondition(new E_Equals(left.convertToExpr(top),right.convertToExpr(top))); break;
          case LESS:          top.addHavingCondition(new E_LessThan(left.convertToExpr(top),right.convertToExpr(top))); break;
          case LESSEQUALS:    top.addHavingCondition(new E_LessThanOrEqual(left.convertToExpr(top),right.convertToExpr(top))); break;
          case GREATER:       top.addHavingCondition(new E_GreaterThan(left.convertToExpr(top),right.convertToExpr(top))); break;
          case GREATEREQUALS: top.addHavingCondition(new E_GreaterThanOrEqual(left.convertToExpr(top),right.convertToExpr(top))); break;
          case MAX:           // top.addGroupBy(left.convertToExpr(top));
                              top.addOrderBy(right.convertToExpr(top),Query.ORDER_DESCENDING); top.setOffset(0); top.setLimit(1); break;
          case MIN:           // top.addGroupBy(left.convertToExpr(top));
                              top.addOrderBy(right.convertToExpr(top),Query.ORDER_ASCENDING);  top.setOffset(0); top.setLimit(1); break;
      }

      return new ElementGroup();
  }

  @Override
  public String toString() {

      if (operator == Operator.MAX) return "max_" + left.toString() + "(" + right.toString() + ")";
      if (operator == Operator.MIN) return "min_" + left.toString() + "(" + right.toString() + ")";

      String op = "";
      switch (operator) {
          case EQUALS:        op = "=" ; break;
          case LESS:          op = "<" ; break;
          case LESSEQUALS:    op = "<="; break;
          case GREATER:       op = ">" ; break;
          case GREATEREQUALS: op = ">="; break;
      }
      return left.toString() + op + right.toString();
  }

  @Override
  public OperatorStatement clone() {
    return new OperatorStatement(this.operator, this.left.clone(), this.right.clone());
  }
}
