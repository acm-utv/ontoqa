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

package com.acmutv.ontoqa.core.session;

import com.acmutv.ontoqa.core.grammar.Grammar;
import com.acmutv.ontoqa.core.grammar.GrammarFormat;
import com.acmutv.ontoqa.core.grammar.GrammarManager;
import com.acmutv.ontoqa.core.knowledge.KnowledgeManager;
import com.acmutv.ontoqa.core.knowledge.ontology.Ontology;
import com.acmutv.ontoqa.core.knowledge.ontology.OntologyFormat;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * The session management services.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class SessionManager {

  private static final Logger LOGGER = LogManager.getLogger(SessionManager.class);

  /**
   * The ontology to submit questions to.
   */
  private static Ontology ONTOLOGY;

  /**
   * The grammar to parse questions with.
   */
  private static Grammar GRAMMAR;

  /**
   * Returns the current session ontology.
   * @return the current session ontology.
   */
  public static Ontology getOntology() {
    return ONTOLOGY;
  }

  /**
   * Returns the current session grammar.
   * @return the current session grammar.
   */
  public static Grammar getGrammar() {
    return GRAMMAR;
  }

  public static void loadOntology(String path, OntologyFormat format) throws IOException {
    ONTOLOGY = KnowledgeManager.read(path, "http://example.org/", format);
  }

  public static void loadGrammar(String path, GrammarFormat format) throws IOException {
    GRAMMAR = GrammarManager.read(path, format);
  }


}
