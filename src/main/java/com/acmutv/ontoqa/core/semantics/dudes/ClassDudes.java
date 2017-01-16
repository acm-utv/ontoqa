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

package com.acmutv.ontoqa.core.semantics.dudes;

import com.acmutv.ontoqa.core.semantics.base.Constant;
import com.acmutv.ontoqa.core.semantics.base.Proposition;
import com.acmutv.ontoqa.core.semantics.base.Variable;
import com.acmutv.ontoqa.core.semantics.drs.Drs;
import com.acmutv.ontoqa.core.semantics.drs.SimpleDrs;

/**
 * A DUDES representing a class.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class ClassDudes extends BaseDudes implements Dudes {

  public ClassDudes(String propertyIRI, String objectIRI) {
    super();

    Drs drs = new SimpleDrs(0);
    Variable var1 = new Variable(1); // p
    Variable var2 = new Variable(2); // x
    Variable var3 = new Variable(3); // y
    Constant property = new Constant(propertyIRI);
    Constant object = new Constant(objectIRI);

    drs.getStatements().add(new Proposition(var1, var2, var3));

    super.setMainDrs(0);
    super.setMainVariable(var2);
    super.setDrs(drs);

    super.replace(var1, property);
    super.replace(var3, object);
  }
}
