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

import com.acmutv.ontoqa.core.semantics.base.slot.Slot;
import com.acmutv.ontoqa.core.semantics.base.term.Term;
import com.acmutv.ontoqa.core.semantics.base.term.Variable;
import com.acmutv.ontoqa.core.semantics.drs.Drs;
import org.apache.jena.query.Query;

import java.util.Set;

/**
 * The Dependency-based Underspecified Discourse Representation Structure (DUDES).
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Drs
 */
public interface Dudes {

  /**
   * Collects the set of all variables.
   * @return the set of all variables.
   */
  Set<Integer> collectVariables();

  /**
   * Converts the DUDES into an equivalent SPARQL query.
   * @return the equivalent SPARQL query.
   */
  Query convertToSPARQL();

  /**
   * Returns the DRS.
   * @return the DRS.
   */
  Drs getDrs();

  /**
   * Returns the label of the main DRS.
   * @return the label of the main DRS.
   */
  int getMainDrs();

  /**
   * Returns the main variable.
   * @return the main variable.
   */
  Variable getMainVariable();

  /**
   * Returns the set of projections.
   * @return the set of projections.
   */
  Set<Term> getProjection();

  /**
   * Returns the set of slots.
   * @return the set of slots.
   */
  Set<Slot> getSlots();

  /**
   * Checks if there is a slot for {@code anchor}.
   * @param anchor the anchor to check.
   * @return true if there is a slot for {@code anchor}; false, otherwise.
   */
  boolean hasSlot(String anchor);

  /**
   * Checks if a {@code SELECT} SPARQL query should be generated.
   * @return true if a {@code SELECT} SPARQL query should be generated; false, otherwise.
   */
  boolean isSelect();

  void merge(Dudes other);

  void merge(Dudes other, String anchor);

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

  /**
   * Sets the DUDES DRS.
   * @param drs the DRS.
   */
  void setDrs(Drs drs);

  /**
   * Sets the main DRS, giving its label {@code i}.
   * @param i the main DRS label.
   */
  void setMainDrs(int i);

  /**
   * Sets the main DRS.
   * @param drs the main DRS.
   */
  void setMainDrs(Drs drs);

  /**
   * Sets the main variable.
   * @param var the main variable.
   */
  void setMainVariable(Variable var);

  /**
   * Sets if a {@code SELECT} SPARQL query should be generated.
   * @param select whether or not to generate a {@code SELECT} SPARQL query.
   */
  void setSelect(boolean select);

  /**
   * Returns the pretty string representation.
   * @return the pretty string representation.
   */
  String toPrettyString();

}
