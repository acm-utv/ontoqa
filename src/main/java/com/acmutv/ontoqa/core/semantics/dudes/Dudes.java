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
 * This interface defines the SimpleDudes data structure.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public interface Dudes {

  Set<Integer> collectVariables();

  int getMainDrs();

  Drs getDrs();

  Variable getMainVariable();

  Set<Term> getProjection();

  Set<Slot> getSlots();

  boolean hasSlot(String anchor);

  boolean isSelect();

  void setSelect(boolean select);

  void rename(int oldValue, int newValue);

  void rename(String oldValue, String newValue);

  void replace(Term oldValue, Term newValue);

  void merge(Dudes other);

  void merge(Dudes other, String anchor);

  /**
   * Sets the DUDES DRS.
   * @param drs the DRS.
   */
  void setDrs(Drs drs);

  /**
   * Sets the main DRS.
   * @param i the DRS label.
   */
  void setMainDrs(int i);

  /**
   * Sets the main DRS.
   * @param drs the DRS.
   */
  void setMainDrs(Drs drs);

  /**
   * Sets the SimpleDudes main variable.
   * @param var the main variable.
   */
  void setMainVariable(Variable var);

  Query convertToSPARQL();

  /**
   * Returns the pretty string representation.
   * @return the pretty string representation.
   */
  String toPrettyString();

}
