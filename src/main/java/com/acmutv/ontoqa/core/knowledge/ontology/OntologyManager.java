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

package com.acmutv.ontoqa.core.knowledge.ontology;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.*;

/**
 * This class realizes the ontology management services.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class OntologyManager {

  private static final Logger LOGGER = LogManager.getLogger(OntologyManager.class);

  /**
   * @param input
   * @param prefix
   * @param format
   * @return
   * @throws IOException
   */
  public static Ontology readOntology(InputStream input, String prefix, RDFFormat format) throws IOException {
    LOGGER.traceEntry("input={} prefix={} format={}", input, prefix, format);

    Ontology ontology = new SimpleOntology();
    Model model = Rio.parse(input, prefix, format);
    ontology.merge(model);

    return LOGGER.traceExit(ontology);
  }

  /**
   * @param input
   * @param prefix
   * @param format
   * @return
   * @throws IOException
   */
  public static Ontology readOntology(Reader input, String prefix, RDFFormat format) throws IOException {
    LOGGER.traceEntry("input={} prefix={} format={}", input, prefix, format);

    Ontology ontology = new SimpleOntology();
    Model model = Rio.parse(input, prefix, format);
    ontology.merge(model);

    return LOGGER.traceExit(ontology);
  }

  /**
   * @param output
   * @param ontology
   * @param format
   */
  public static void writeOntology(OutputStream output, Ontology ontology, RDFFormat format) {
    LOGGER.traceEntry("output={} ontology={} format={}", output, ontology, format);

    Rio.write(ontology, output, format);
  }

  /**
   * @param output
   * @param ontology
   * @param format
   */
  public static void writeOntology(Writer output, Ontology ontology, RDFFormat format) {
    LOGGER.traceEntry("output={} ontology={} format={}", output, ontology, format);

    Rio.write(ontology, output, format);
  }

}
