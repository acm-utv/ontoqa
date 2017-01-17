package com.acmutv.ontoqa.core.semantics.base;

import com.acmutv.ontoqa.core.semantics.drs.Drs;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.syntax.Element;

import java.util.Set;

public interface Statement {

  Set<Integer> collectVariables();

  void union(Drs drs, int label);

  void rename(int i_old, int i_new);

  void rename(String s_old, String s_new);

  void replace(Term t_old, Term t_new);

  Set<Replace> collectReplacements();

  Statement clone();

  Element convertToRDF(Query top);
}
