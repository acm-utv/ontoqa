/*
  The MIT License (MIT)

  Copyright (c) 2017 Antonella Botte, Giacomo Marciani and Debora Partigianoni

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
import org.apache.jena.query.Query;
import org.apache.jena.sparql.syntax.Element;

import java.util.Set;

/**
 * The Discourse Representation Structure (DRS).
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public interface Drs {

  /**
   * Creates a new DRS, as a clone of the current DRS.
   * @return the cloned DRS.
   */
  Drs clone();

  /**
   * Collects the set of all replacements.
   * @return the set of all replacements.
   */
  Set<Replace> collectReplacements();

  /**
   * Collects the set of all variables.
   * @return the set of all variables.
   */
  Set<Integer> collectVariables();

  /**
   * Generates the RDF elements, according to the top-level query {@code top}.
   * @param top the top-level query.
   * @return the RDF elements.
   */
  Element convertToRDF(Query top);

  /**
   * Returns the DRS label.
   * @return the DRS label.
   */
  int getLabel();

  /**
   * Returns the set of variables.
   * @return the set of variables.
   */
  Set<Variable> getVariables();

  /**
   * Returns the set of statements.
   * @return the set of statements.
   */
  Set<Statement> getStatements();

  /**
   * Minimizes the DRS.
   */
  void postprocess();

  /**
   * Renames all the occurrences of the variable {@code oldval} with the variable {@code newval}.
   * @param oldval the old variable.
   * @param newval the new variable.
   */
  void rename(int oldval, int newval);

  /**
   * Renames all the occurrences of the variable {@code oldval} with the variable {@code newval}.
   * @param oldval the old variable.
   * @param newval the new variable.
   */
  void rename(String oldval, String newval);

  /**
   * Replaces all the occurrences of the term {@code oldval} with the term {@code newval}.
   * @param oldval the old term.
   * @param newval the new term.
   */
  void replace(Term oldval, Term newval);

  void union(Drs other, int label);
}
