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

package com.acmutv.ontoqa.session;

import com.acmutv.ontoqa.core.grammar.Grammar;
import com.acmutv.ontoqa.core.grammar.GrammarFormat;
import com.acmutv.ontoqa.core.grammar.GrammarManager;
import com.acmutv.ontoqa.core.knowledge.KnowledgeManager;
import com.acmutv.ontoqa.core.knowledge.ontology.Ontology;
import com.acmutv.ontoqa.core.knowledge.ontology.OntologyFormat;
import com.acmutv.ontoqa.tool.io.IOManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The session management services.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class SessionManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(SessionManager.class);

  private static Session session = new Session();

  /**
   * Returns the current session ontology.
   * @return the current session ontology.
   */
  public static Ontology getOntology() {
    return session.getOntology();
  }

  /**
   * Returns the current session grammar.
   * @return the current session grammar.
   */
  public static Grammar getGrammar() {
    return session.getGrammar();
  }

  /**
   * Loads the ontology in {@code path} serialized as {@code format}.
   * @param path the ontology path.
   * @param format the ontology format.
   * @throws IOException when ontology cannot be loaded.
   */
  public static void loadOntology(String path, OntologyFormat format) throws IOException {
    session.setOntology(KnowledgeManager.read(path, "http://example.org/", format));
  }

  /**
   * Loads the grammar in {@code path} serialized as {@code format}.
   * @param path the grammar path.
   * @param format the grammar format.
   * @throws IOException when grammar cannot be loaded.
   */
  public static void loadGrammar(String path, GrammarFormat format) throws IOException {
    if (IOManager.isDirectory(path)) {
      session.setGrammar(GrammarManager.readAll(path, format));
    } else if (IOManager.isFile(path)) {
      session.setGrammar(GrammarManager.read(path, format));
    } else {
      throw new IOException("Cannot load grammar from path " + path);
    }
  }
}
