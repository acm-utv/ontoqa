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

package com.acmutv.ontoqa.core.syntax;

import com.acmutv.ontoqa.core.exception.SyntaxProcessingException;
import com.acmutv.ontoqa.core.syntax.ltag.LTAG;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class realizes a simple syntax repository, that is a collection of syntax elementary tree.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
public class SimpleSyntaxRepo extends HashMap<String, LTAG> implements SyntaxRepo {

  public SimpleSyntaxRepo() {
    super();
  }

  public List<LTAG> getAll(String ...lexicalEntries) throws SyntaxProcessingException {
    List<LTAG> trees = new ArrayList<>();
    for (String lexicalEntry : lexicalEntries) {
      LTAG elementaryTree = super.get(lexicalEntry);
      if (elementaryTree == null) {
        throw new SyntaxProcessingException("Cannot find elementary tree for lexical entry [%s]", lexicalEntry);
      }
      trees.add(elementaryTree);
    }
    return trees;
  }
}
