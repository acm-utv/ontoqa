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

package com.acmutv.ontoqa.benchmark;

import com.acmutv.ontoqa.JenaTest;
import com.acmutv.ontoqa.core.exception.OntoqaFatalException;
import com.acmutv.ontoqa.core.grammar.GrammarFormat;
import com.acmutv.ontoqa.core.knowledge.ontology.OntologyFormat;
import com.acmutv.ontoqa.session.SessionManager;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.core.Var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Common utilities for Benchamrk tests.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class Common {

  private static final Logger LOGGER = LogManager.getLogger(Common.class);

  public static final String prefix = "http://www.semanticweb.org/debby/ontologies/2016/11/organization-ontology#";

  public static final String ONTOLOGY_PATH = JenaTest.class.getResource("/knowledge/organization.ttl").getPath();

  public static final OntologyFormat ONTOLOGY_FORMAT = OntologyFormat.TURTLE;

  public static final String GRAMMAR_PATH = JenaTest.class.getResource("/grammar/organization/").getPath();

  public static final GrammarFormat GRAMMAR_FORMAT = GrammarFormat.YAML;

  /**
   * The format of the ontology used for benchmarks.
   */
  private static final String FORMAT = "TURTLE";

  /**
   * Test the assertion on ontology answers.
   * @param sparql the SPARQL query.
   * @param expected the expected answer.
   */
  public static void test_ontology(String sparql, String expected) {
    Model model = ModelFactory.createDefaultModel();
    model.read(ONTOLOGY_PATH, FORMAT);
    Query query = QueryFactory.create(sparql);
    LOGGER.info("Query SPARQL:\n{}", query.toString());

    String answer = null;
    if (query.isAskType()) {
      answer = submitAskQuery(model, query);
    } else if (query.isSelectType()) {
      answer = submitSelectQuery(model, query);
    } else {
      Assert.fail("Unrecognized query type.");
    }

    Assert.assertEquals(expected, answer);
  }

  /**
   * Submits an {@code ASK} SPARQL query.
   * @param model the ontology.
   * @param query the query.
   * @return the answer.
   */
  private static String submitAskQuery(Model model, Query query) {
    String answer;
    try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
      boolean solution = qexec.execAsk();
      answer = String.valueOf(solution);
      LOGGER.info("Answer: {}", answer);
    }
    return answer;
  }

  /**
   * Submits an {@code ASK} SPARQL query.
   * @param model the ontology.
   * @param query the query.
   * @return the answer.
   */
  private static String submitSelectQuery(Model model, Query query) {
    String var = getVariableName(query);
    LOGGER.info("Variable: {}", var);

    List<String> sols = new ArrayList<>();
    try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
      ResultSet results = qexec.execSelect();
      while (results.hasNext()) {
        QuerySolution sol = results.next();
        sols.add(sol.get(var).toString());
      }
    }

    String answer = sols.stream().collect(Collectors.joining(","));
    LOGGER.info("Answer: {}", answer);

    return answer;
  }

  /**
   * Retrieves the result variable name.
   * @param query the query
   * @return the result variable name.
   */
  private static String getVariableName(Query query) {
    String var = query.getProjectVars().get(0).getVarName();
    if (var == null) {
      for (Var v : query.getProject().getVars()) {
        if (v.getVarName().startsWith("fout")) {
          var = v.getVarName();
          break;
        }
      }
    }
    return var;
  }

  /**
   * Loads session for all the benchmark JUnit tests.
   * @throws OntoqaFatalException when ontology or grammar cannot be loaded.
   */
  public static void loadSession() throws OntoqaFatalException {
    if (SessionManager.getOntology() == null) {
      try {
        SessionManager.loadOntology(ONTOLOGY_PATH, ONTOLOGY_FORMAT);
      } catch (IOException exc) {
        throw new OntoqaFatalException("Cannot load ontology in %s format from %s",
            ONTOLOGY_FORMAT, ONTOLOGY_PATH);
      }
    }

    if (SessionManager.getGrammar() == null) {
      try {
        SessionManager.loadGrammar(GRAMMAR_PATH, GRAMMAR_FORMAT);
      } catch (IOException exc) {
        throw new OntoqaFatalException("Cannot load grammar in %s format from %s",
            GRAMMAR_FORMAT, GRAMMAR_PATH);
      }
    }
  }
}
