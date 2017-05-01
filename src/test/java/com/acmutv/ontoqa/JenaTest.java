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

package com.acmutv.ontoqa;

import com.acmutv.ontoqa.core.CoreController;
import com.acmutv.ontoqa.core.knowledge.KnowledgeManager;
import com.acmutv.ontoqa.core.semantics.dudes.Dudes;
import com.acmutv.ontoqa.core.semantics.dudes.DudesBuilder;
import com.acmutv.ontoqa.core.semantics.dudes.DudesTemplates;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.sparql.core.Var;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JUnit tests for personal use only.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see CoreController
 */
public class JenaTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(JenaTest.class);

  private Model readOntology() {
    String path = JenaTest.class.getResource("/knowledge/einstein.ttl").getPath();
    Model model = ModelFactory.createDefaultModel();
    model.read(path, "TURTLE");
    return model;
  }

  private Dudes createDudesAsk() {
    String albertEinsteinIRI = "http://example.com/sample#Albert_Einstein";
    String elsaEinsteinIRI = "http://example.com/sample#Elsa_Einstein";
    String spouseIRI = "http://example.com/sample#spouse";

    /* Albert Einstein */
    Dudes albertEinsteinDUDES = DudesTemplates.properNoun(albertEinsteinIRI);
    LOGGER.info("Albert Einstein: {}", albertEinsteinDUDES);

    /* Elsa Einstein */
    Dudes elsaEinsteinDUDES = DudesTemplates.properNoun(elsaEinsteinIRI);
    LOGGER.info("Elsa Einstein: {}", elsaEinsteinDUDES);

    /* marry */
    Dudes marryDUDES = DudesTemplates.property(spouseIRI, "subj", "obj");
    LOGGER.info("marry: {}", marryDUDES);

    /* Albert Einstein married */
    Dudes albertEinsteinMarriedDUDES = new DudesBuilder(marryDUDES)
        .substitution(albertEinsteinDUDES, "subj")
        .build();
    LOGGER.info("Albert Einstein married: {}", albertEinsteinMarriedDUDES);

    /* Albert Einstein married Elsa Einstein */
    Dudes albertEinsteinMarriedElsaEinsteinDUDES = new DudesBuilder(albertEinsteinMarriedDUDES)
        .substitution(elsaEinsteinDUDES, "obj")
        .build();
    LOGGER.info("Albert Einstein married Elsa Einstein: {}", albertEinsteinMarriedElsaEinsteinDUDES);

    albertEinsteinMarriedElsaEinsteinDUDES.setSelect(false);

    return albertEinsteinMarriedElsaEinsteinDUDES;
  }

  private Dudes createDudesCount() {
    String albertEinsteinIRI = "http://example.com/sample#Albert_Einstein";
    String spouseIRI = "http://example.com/sample#spouse";
    String rdfTypeIRI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    String womenIRI = "http://example.com/sample#Woman";

    /* count many */
    Dudes howmanyDUDES = DudesTemplates.howmany("np");
    LOGGER.info("count many: {}", howmanyDUDES);

    /* women */
    Dudes womenDUDES = DudesTemplates.type(rdfTypeIRI, womenIRI);
    LOGGER.info("women: {}", womenDUDES);

    /* Albert Einstein */
    Dudes albertEinsteinDUDES = DudesTemplates.properNoun(albertEinsteinIRI);
    LOGGER.info("Albert Einstein: {}", albertEinsteinDUDES);

    /* marry */
    Dudes marryDUDES = DudesTemplates.property(spouseIRI, "subj", "obj");
    LOGGER.info("marry: {}", marryDUDES);

    /* did */
    Dudes didDUDES = DudesTemplates.empty();
    LOGGER.info("did: {}", didDUDES);

    /* count many women */
    Dudes howmanyWomenDUDES = new DudesBuilder(howmanyDUDES)
        .substitution(womenDUDES, "np")
        .build();
    LOGGER.info("count many women: {}", howmanyWomenDUDES);

    /* Albert Einstein marry */
    Dudes albertEinsteinMarryDUDES = new DudesBuilder(marryDUDES)
        .substitution(albertEinsteinDUDES, "subj")
        .build();
    LOGGER.info("Albert Einstein marry: {}", albertEinsteinMarryDUDES);

    /* count many Albert Einstein marry */
    Dudes howManyAlbertEinsteinMarryDUDES = new DudesBuilder(albertEinsteinMarryDUDES)
        .substitution(howmanyWomenDUDES, "obj")
        .build();
    LOGGER.info("count many women Albert Einstein marry: {}", howManyAlbertEinsteinMarryDUDES);

    /* count many women did Albert Einstein marry */
    Dudes howManyWomenDidAlbertEinstenMarryDUDES = new DudesBuilder(didDUDES)
        .substitution(howManyAlbertEinsteinMarryDUDES, "")
        .build();

    return howManyWomenDidAlbertEinstenMarryDUDES;
  }

  private Dudes createDudesSelect() {
    String elsaEinsteinIRI = "http://example.com/sample#Elsa_Einstein";
    String spouseIRI = "http://example.com/sample#spouse";

    /* who */
    Dudes whoDUDES = DudesTemplates.what();
    LOGGER.info("who: {}", whoDUDES);

    /* Elsa Einstein */
    Dudes elsaEinsteinDUDES = DudesTemplates.properNoun(elsaEinsteinIRI);
    LOGGER.info("Elsa Einstein: {}", elsaEinsteinDUDES);

    /* marry */
    Dudes marryDUDES = DudesTemplates.property(spouseIRI, "subj", "obj");
    LOGGER.info("marry: {}", marryDUDES);

    /* who married Elsa Einstein */
    Dudes whoMarriedElsaEinsteinDUDES = new DudesBuilder(marryDUDES)
        .substitution(whoDUDES, "subj")
        .substitution(elsaEinsteinDUDES, "obj")
        .build();

    return whoMarriedElsaEinsteinDUDES;
  }

  @Test
  public void test_jena_read() {
    Model model = readOntology();
    StmtIterator i = model.listStatements();
    while(i.hasNext()) {
      Statement s = i.next();
    }
  }

  @Test
  public void test_jena_query_ask() {
    Model model = readOntology();
    Dudes dudes = createDudesAsk();
    Query query = QueryFactory.create(dudes.convertToSPARQL());
    LOGGER.info("Query SPARQL:\n{}", query.toString());
    try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
      boolean sol = qexec.execAsk();
      LOGGER.info("Solution: {}", sol);
    }
  }

  @Test
  public void test_jena_query_count() {
    Model model = readOntology();
    Dudes dudes = createDudesCount();
    Query query = QueryFactory.create(dudes.convertToSPARQL());
    LOGGER.info("Query SPARQL:\n{}", query.toString());
    String var = query.getProjectVars().get(0).getVarName();
    if (var == null) {
      for (Var v : query.getProject().getVars()) {
        if (v.getVarName().startsWith("fout")) {
          var = v.getVarName();
          break;
        }
      }
    }
    LOGGER.info("Variable: {}", var);
    try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
      ResultSet results = qexec.execSelect();
      QuerySolution sol = results.next();
      LOGGER.info("Solution: {}", sol.get(var));
    }
  }

  @Test
  public void test_jena_query_select() {
    Model model = readOntology();
    Dudes dudes = createDudesSelect();
    Query query = QueryFactory.create(dudes.convertToSPARQL());
    LOGGER.info("Query SPARQL:\n{}", query.toString());
    String var = query.getProjectVars().get(0).getVarName();
    LOGGER.info("Variable: {}", var);
    try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
      ResultSet results = qexec.execSelect();
      QuerySolution sol = results.next();
      LOGGER.info("Solution: {}", sol.get(var));
    }
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
      LOGGER.debug("Answer: {}", answer);
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
    String var = KnowledgeManager.getVariableName(query);
    LOGGER.debug("Variable: {}", var);

    List<String> sols = new ArrayList<>();
    try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
      ResultSet results = qexec.execSelect();
      while (results.hasNext()) {
        QuerySolution sol = results.next();
        sols.add(sol.get(var).toString());
      }
    }

    String answer = sols.stream().collect(Collectors.joining(","));
    LOGGER.debug("Answer: {}", answer);

    return answer;
  }
}
