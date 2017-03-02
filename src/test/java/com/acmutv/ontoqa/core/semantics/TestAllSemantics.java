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

import com.acmutv.ontoqa.core.exception.QueryException;
import com.acmutv.ontoqa.core.knowledge.KnowledgeManager;
import com.acmutv.ontoqa.core.knowledge.ontology.Ontology;
import com.acmutv.ontoqa.core.knowledge.ontology.OntologyFormat;
import com.acmutv.ontoqa.core.knowledge.query.QueryResult;
import com.acmutv.ontoqa.core.semantics.base.TestAllSemanticsBase;
import com.acmutv.ontoqa.core.semantics.drs.TestAllSemanticsDrs;
import com.acmutv.ontoqa.core.semantics.dudes.TestAllSemanticsDudes;
import com.acmutv.ontoqa.core.semantics.sltag.TestAllSemanticsSltag;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JUnit test suite for semantics services.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see TestAllSemanticsBase
 * @see TestAllSemanticsDrs
 * @see TestAllSemanticsDudes
 * @see TestAllSemanticsSltag
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    TestAllSemanticsBase.class,
    TestAllSemanticsDrs.class,
    TestAllSemanticsDudes.class,
    TestAllSemanticsSltag.class
})
public class TestAllSemantics {

  private static final Logger LOGGER = LogManager.getLogger(TestAllSemantics.class);

  public static Ontology ONTOLOGY;

  public static final String ONTOLOGY_PATH = TestAllSemantics.class.getResource("/semantics/sample.owl").getPath();

  public static final String ALBERT_EINSTEIN_IRI = "http://example.com/sample#Albert_Einstein";

  public static final String ELSA_EINSTEIN_IRI = "http://example.com/sample#Elsa_Einstein";

  public static final String HAS_SPOUSE_IRI = "http://example.com/sample#spouse";

  public static final String WOMAN_IRI = "http://example.com/sample#Woman";

  public static final String HEIGHT_IRI = "http://example.com/sample#height";

  public static final String PERSON_IRI = "http://example.com/sample#Person";

  public static final String RDF_TYPE_IRI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";

  /**
   * Loads the test ontology.
   * @throws IOException when ontology cannot be loaded.
   */
  public static void loadOntology() throws IOException {
    String prefix = "http://example.org/sample#";
    OntologyFormat format = OntologyFormat.TURTLE;
    ONTOLOGY = KnowledgeManager.read(ONTOLOGY_PATH, prefix, format);
  }

  /**
   * Tests the loaded ontology.
   */
  public static void test() throws QueryException {
    List<String> queries = new ArrayList<>();
    queries.add(String.format("ASK WHERE { <%s> <%s> <%s> }", ALBERT_EINSTEIN_IRI, HAS_SPOUSE_IRI, ELSA_EINSTEIN_IRI));
    queries.add(String.format("SELECT DISTINCT ?x WHERE { ?x <%s> <%s> }", HAS_SPOUSE_IRI, ELSA_EINSTEIN_IRI));
    queries.add(String.format("SELECT DISTINCT ?x WHERE { <%s> <%s> ?x }", ALBERT_EINSTEIN_IRI, HAS_SPOUSE_IRI));
    queries.add(String.format("SELECT DISTINCT ?x WHERE { ?x a <%s> . ?x <%s> ?h } ORDER BY DESC(?h) OFFSET 0 LIMIT 1", PERSON_IRI, HEIGHT_IRI));
    queries.add(String.format("SELECT (COUNT(DISTINCT ?wife) AS ?fout0) WHERE { ?wife a <%s> . <%s> <%s> ?wife }", WOMAN_IRI, ALBERT_EINSTEIN_IRI, HAS_SPOUSE_IRI));

    List<String> answers = new ArrayList<>();
    answers.add("true");
    answers.add(ALBERT_EINSTEIN_IRI);
    answers.add(ELSA_EINSTEIN_IRI);
    answers.add(ALBERT_EINSTEIN_IRI);
    answers.add("1");

    for (int i = 0; i < queries.size(); i++) {
      String sparql = queries.get(i);
      Query query = QueryFactory.create(sparql);
      LOGGER.debug("SPARQL query:\n{}", query);
      String expected = answers.get(i);
      QueryResult result = KnowledgeManager.submit(ONTOLOGY, query);
      String actual = (result.get(0) != null) ? result.get(0).stringValue() : "No result";
      Assert.assertEquals(expected, actual);
    }
  }

  /**
   * Checks assertions on generated SPARQL query.
   * @param query the actual query.
   * @param expected the expected result.
   */
  public static void testQuery(Query query, String expected) throws QueryException {
    QueryResult result = KnowledgeManager.submit(ONTOLOGY, query);
    String actual = (!result.isEmpty()) ? result.get(0).stringValue() : "No aswer";
    Assert.assertEquals(expected, actual);
  }

}
