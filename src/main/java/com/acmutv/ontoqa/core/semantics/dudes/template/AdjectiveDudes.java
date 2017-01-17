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

package com.acmutv.ontoqa.core.semantics.dudes.template;

import com.acmutv.ontoqa.core.semantics.base.Constant;
import com.acmutv.ontoqa.core.semantics.base.Proposition;
import com.acmutv.ontoqa.core.semantics.base.Variable;
import com.acmutv.ontoqa.core.semantics.drs.Drs;
import com.acmutv.ontoqa.core.semantics.drs.SimpleDrs;
import com.acmutv.ontoqa.core.semantics.dudes.BaseDudes;
import com.acmutv.ontoqa.core.semantics.dudes.Dudes;

/**
 * A DUDES representing an adjective (both for attributive and predicative use).
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class AdjectiveDudes extends BaseDudes implements Dudes {

  public AdjectiveDudes(String predicateIRI) {
    super();

    Variable varX = new Variable(1); // x

    Constant predicate = new Constant(predicateIRI); // P
    Constant trueLiteral = new Constant("true", Constant.Datatype.STRING);

    Drs drs = new SimpleDrs(0);
    drs.getStatements().add(new Proposition(predicate, varX, trueLiteral)); // P(x, true)

    super.setMainDrs(drs);
    super.setMainVariable(varX);
  }
}
