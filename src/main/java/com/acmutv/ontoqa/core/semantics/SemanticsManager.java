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

package com.acmutv.ontoqa.core.semantics;

import com.acmutv.ontoqa.core.knowledge.ontology.Ontology;
import com.acmutv.ontoqa.core.lexicon.Lexicon;
import com.acmutv.ontoqa.core.semantics.dudes.Dudes;
import com.acmutv.ontoqa.core.semantics.dudes.BaseDudes;
import com.acmutv.ontoqa.core.syntax.ltag.Ltag;
import org.apache.jena.query.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class realizes the semantics management services.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class SemanticsManager {

  private static final Logger LOGGER = LogManager.getLogger(SemanticsManager.class);

  /**
   * Builds the BaseDudes from a syntax tree related to an ontology and lexicon.
   * @param tree the syntax tree.
   * @param ontology the ontology to address.
   * @param lexicon the lexicon to address.
   * @return the BaseDudes.
   */
  public static Dudes getDudes(Ltag tree, Ontology ontology, Lexicon lexicon) {
    LOGGER.traceEntry("tree={} ontology={} lexicon={}", tree, ontology, lexicon);

    Dudes dudes = new BaseDudes();

    //TODO

    return LOGGER.traceExit(dudes);
  }

  /**
   * Returns the query representation of the BaseDudes.
   * @param dudes the BaseDudes.
   * @return the query representation.
   */
  public static Query getQuery(Dudes dudes) {
    LOGGER.traceEntry("dudes={}", dudes);

    final Query query = dudes.convertToSPARQL();

    return LOGGER.traceExit(query);
  }
}
