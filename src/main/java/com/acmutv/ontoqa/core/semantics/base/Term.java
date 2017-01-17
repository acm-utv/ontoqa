package com.acmutv.ontoqa.core.semantics.base;

import org.apache.jena.graph.Node;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.expr.Expr;

public interface Term {

  boolean isVariable();

  boolean isFunction();

  void rename(int i_old, int i_new);

  void rename(String s_old, String s_new);

  Term replace(Term t_old, Term t_new);

  Term clone();

  Node convertToNode(Query top);

  Expr convertToExpr(Query top);
}