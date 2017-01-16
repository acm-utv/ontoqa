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

import com.acmutv.ontoqa.core.semantics.base.Replace;
import com.acmutv.ontoqa.core.semantics.base.Statement;
import com.acmutv.ontoqa.core.semantics.base.Term;
import com.acmutv.ontoqa.core.semantics.base.Variable;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.syntax.Element;

import java.util.Set;

/**
 * The Discourse Representation Structure (SimpleDrs) data structure.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public interface Drs {

  int getLabel();

  Set<Variable> getVariables();

  Set<Statement> getStatements();

  void postprocess();

  Set<Integer> collectVariables();

  void rename(int oldValue, int newValue);

  void rename(String oldValue, String newValue);

  void replace(Term oldValue, Term newValue);

  void union(Drs other, int label);

  Element convertToRDF(Query top);

  Set<Replace> collectReplacements();

  Drs clone();
}
