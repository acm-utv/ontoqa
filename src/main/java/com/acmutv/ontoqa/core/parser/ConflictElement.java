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

package com.acmutv.ontoqa.core.parser;

import com.acmutv.ontoqa.core.semantics.sltag.Sltag;
import lombok.Data;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of colliding elements.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
@Data
public class ConflictElement {

  private List<Pair<Sltag, String>> substitutions = new ArrayList<>();

  private List<Pair<Sltag, String>> adjunctions = new ArrayList<>();

  /**
   * Adds conflict element for substitution.
   * @param candidate the SLTAG candidate.
   * @param prevLexicalEntry the previous lexical entry.
   */
  public void addAdjunction(Sltag candidate, String prevLexicalEntry) {
    this.getAdjunctions().add(new ImmutablePair<>(candidate, prevLexicalEntry));
  }

  /**
   * Adds conflict element for adjunction.
   * @param candidate the SLTAG candidate.
   * @param prevLexicalEntry the previous lexical entry.
   */
  public void addSubstitution(Sltag candidate, String prevLexicalEntry) {
    this.getSubstitutions().add(new ImmutablePair<>(candidate, prevLexicalEntry));
  }


}
