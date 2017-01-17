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

package com.acmutv.ontoqa.core.syntax.ltag.template;

import com.acmutv.ontoqa.core.syntax.POS;
import com.acmutv.ontoqa.core.syntax.ltag.*;

/**
 * A LTAG representing a relational prepositional noun.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class RelationalPrepNounLtag extends BaseLtag implements Ltag {

  public RelationalPrepNounLtag(String noun) {
    this(noun, false);
  }

  public RelationalPrepNounLtag(String noun, boolean generic) {
    super();

    LtagNode np = new PosNode("NP:1", POS.NP);
    LtagNode n = new PosNode("N:1", POS.N);
    LtagNode pp = new PosNode("PP:1", POS.PP);
    LtagNode p = new PosNode("P:1", POS.P);
    LtagNode dp2 = new PosNode("DP:2", POS.DP, LtagNode.Marker.SUB);
    LtagNode lex = new LexicalNode("LEX:noun", noun);
    LtagNode lexOf = new LexicalNode("LEX:of", "of");

    if (generic) {
      LtagNode dp1 = new PosNode("DP:1", POS.DP);
      super.setRoot(dp1);
      super.addProduction(dp1, np);
    } else {
      super.setRoot(np);
    }

    super.addProduction(np, n);
    super.addProduction(np, pp);
    super.addProduction(n, lex);
    super.addProduction(pp, p);
    super.addProduction(pp, dp2);
    super.addProduction(p, lexOf);
  }
}
