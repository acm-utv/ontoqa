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

package com.acmutv.ontoqa.core.semantics.dudes;

import com.acmutv.ontoqa.core.exception.QueryException;
import com.acmutv.ontoqa.core.knowledge.KnowledgeManager;
import com.acmutv.ontoqa.core.knowledge.ontology.Ontology;
import com.acmutv.ontoqa.core.knowledge.ontology.OntologyFormat;
import com.acmutv.ontoqa.core.knowledge.query.QueryResult;
import com.acmutv.ontoqa.core.semantics.base.statement.OperatorType;
import org.apache.jena.query.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class realizes JUnit tests for {@link Dudes}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Dudes
 */
public class DudesTest {

  private static final Logger LOGGER = LogManager.getLogger(DudesTest.class);

  private static Ontology ONTOLOGY;

  /**
   * Loads the test ontology.
   * @throws IOException when ontology cannot be loaded.
   */
  @Before
  public void loadOntology() throws IOException {
    String path = DudesTest.class.getResource("/semantics/sample.owl").getPath();
    String prefix = "http://example.org/sample#";
    OntologyFormat format = OntologyFormat.TURTLE;
    LOGGER.info("Loading test ontology {}", path);
    ONTOLOGY = KnowledgeManager.read(path, prefix, format);
  }

  /**
   * Tests the loaded ontology.
   */
  @Test
  public void test() throws QueryException {
    String albertEinsteinIRI = "<http://example.com/sample#Albert_Einstein>";
    String elsaEinsteinIRI = "<http://example.com/sample#Elsa_Einstein>";
    String spouseIRI = "<http://example.com/sample#spouse>";
    String womanIRI = "<http://example.com/sample#Woman>";

    List<String> queries = new ArrayList<>();
    queries.add(String.format("ASK WHERE { %s %s %s }", albertEinsteinIRI, spouseIRI, elsaEinsteinIRI));
    queries.add(String.format("SELECT DISTINCT ?x WHERE { ?x %s %s }", spouseIRI, elsaEinsteinIRI));
    queries.add(String.format("SELECT DISTINCT ?x WHERE { %s %s ?x }", albertEinsteinIRI, spouseIRI));
    queries.add(String.format("SELECT (COUNT(DISTINCT ?wife) AS ?x) WHERE { ?wife a %s . %s %s ?wife }", womanIRI, albertEinsteinIRI, spouseIRI));
    //queries.add(String.format("SELECT (COUNT(DISTINCT ?x)) WHERE { ?x a %s . %s %s ?x }", womanIRI, albertEinsteinIRI, spouseIRI));

    for (String query : queries) {
      QueryResult result = KnowledgeManager.submit(query, ONTOLOGY);
      String actual = (result.get(0) != null) ? result.get(0).stringValue() : "No result";
      LOGGER.info("Result: {}", actual);
    }
  }

  /**
   * Checks assertions on generated SPARQL query.
   * @param query the actual query.
   * @param expected the expected result.
   */
  private void testQuery(Query query, String expected) throws QueryException {
    QueryResult result;
    if (query.isAskType()) {
      result = KnowledgeManager.submit(query.toString(), ONTOLOGY);
    } else if (query.isSelectType()){
      String var = query.getProject().getExpr(query.getProjectVars().get(0)).getVarName();
      if (var != null) {
        result = KnowledgeManager.submit(query.toString(), ONTOLOGY, var);
      } else {
        result = KnowledgeManager.submit(query.toString(), ONTOLOGY);
      }

    } else {
      throw new QueryException("Unknown query type: " + query.getQueryType());
    }
    String actual = (!result.isEmpty()) ? result.get(0).stringValue() : "No aswer";
    LOGGER.info("Result: {}", actual);
    //Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the DUDES pretty string representation.
   */
  @Test
  public void test_prettyString() {
    /* DUDES */
    Dudes dudes = DudesTemplates.properNoun("http://example.com/Uruguay");

    String pretty = dudes.toPrettyString();

    LOGGER.info("DUDES pretty representation:\n{}", pretty);
  }

  /**
   * Example implementing the question: "Albert Einstein married Elsa Einstein?"
   */
  @Test
  public void test_ask() throws QueryException {
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

    /* SPARQL */
    Query actualSparql = albertEinsteinMarriedElsaEinsteinDUDES.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", actualSparql);

    testQuery(actualSparql, "");
  }

  /**
   * Example implementing the question: "Who married Elsa Einstein?"
   */
  @Test
  public void test_whoMarried() throws QueryException {
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

    /* SPARQL */
    Query actualSparql = whoMarriedElsaEinsteinDUDES.convertToSPARQL();
    LOGGER.info("SPARQL query: {}", actualSparql);

    testQuery(actualSparql, "");
  }

  /**
   * Example implementing the question: "Who is the spouse of Albert Einstein?"
   */
  @Test
  public void test_whoIsThe() throws QueryException {
    String albertEinsteinIRI = "http://example.com/sample#Albert_Einstein";
    String spouseIRI = "http://example.com/sample#spouse";

    /* who */
    Dudes whoDUDES = DudesTemplates.what();
    LOGGER.info("who: {}", whoDUDES);

    /* is (copula) */
    Dudes isDUDES = DudesTemplates.copula("1", "2");
    LOGGER.info("is: {}", isDUDES);

    /* the */
    Dudes theDUDES = DudesTemplates.determiner("np");
    LOGGER.info("the: {}", theDUDES);

    /* spouse of */
    Dudes spouseDUDES =
        DudesTemplates.relationalNoun(spouseIRI, "dp", false);
    LOGGER.info("spouse of: {}", spouseDUDES);

    /* Albert Einstein */
    Dudes albertEinsteinDUDES = DudesTemplates.properNoun(albertEinsteinIRI);
    LOGGER.info("Albert Einstein: {}", albertEinsteinDUDES);

    /* spouse of Albert Einstein */
    Dudes sposeOfAlbertEinsteinDUDES = new DudesBuilder(spouseDUDES)
        .substitution(albertEinsteinDUDES, "dp")
        .build();
    LOGGER.info("spouse of Albert Einstein: {}", sposeOfAlbertEinsteinDUDES);

    /* the spouse of Albert Einstein */
    Dudes theSposeOfAlbertEinsteinDUDES = new DudesBuilder(theDUDES)
        .substitution(sposeOfAlbertEinsteinDUDES, "np")
        .build();
    LOGGER.info("the spouse of Albert Einstein: {}", theSposeOfAlbertEinsteinDUDES);

    /* who is */
    Dudes whoIsDUDES = new DudesBuilder(isDUDES)
        .substitution(whoDUDES, "1")
        .build();
    LOGGER.info("who is: {}", whoIsDUDES);

    /* who is the spouse of Albert Einstein */
    Dudes whoIsTheSposeOfAlbertEinsteinDUDES = new DudesBuilder(whoIsDUDES)
        .substitution(theSposeOfAlbertEinsteinDUDES, "2")
        .build();
    LOGGER.info("who is the spouse of Albert Einstein: {}", whoIsTheSposeOfAlbertEinsteinDUDES);

    /* SPARQL */
    Query actualSparql = whoIsTheSposeOfAlbertEinsteinDUDES.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", actualSparql);

    testQuery(actualSparql, "");
  }

  /**
   * Example implementing the question: "How many women Albert Einstein married?"
   */
  @Test
  @Ignore
  public void test_howMany() throws QueryException {
    String albertEinsteinIRI = "http://example.com/sample#Albert_Einstein";
    String spouseIRI = "http://example.com/sample#spouse";
    String rdfTypeIRI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    String womenIRI = "http://example.com/sample#Woman";

    /* how many */
    Dudes howmanyDUDES = DudesTemplates.howmany("np");
    LOGGER.info("how many: {}", howmanyDUDES);

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
    Dudes didDUDES = DudesTemplates.did();
    LOGGER.info("did: {}", didDUDES);

    /* how many women */
    Dudes howmanyWomenDUDES = new DudesBuilder(howmanyDUDES)
        .substitution(womenDUDES, "np")
        .build();
    LOGGER.info("how many women: {}", howmanyWomenDUDES);

    /* Albert Einstein marry */
    Dudes albertEinsteinMarryDUDES = new DudesBuilder(marryDUDES)
        .substitution(albertEinsteinDUDES, "subj")
        .build();
    LOGGER.info("Albert Einstein marry: {}", albertEinsteinMarryDUDES);

    /* how many Albert Einstein marry */
    Dudes howManyAlbertEinsteinMarryDUDES = new DudesBuilder(albertEinsteinMarryDUDES)
        .substitution(howmanyWomenDUDES, "obj")
        .build();
    LOGGER.info("how many women Albert Einstein marry: {}", howManyAlbertEinsteinMarryDUDES);

    /* how many women did Albert Einstein marry */
    Dudes howManyWomenDidAlbertEinstenMarryDUDES = new DudesBuilder(didDUDES)
        .substitution(howManyAlbertEinsteinMarryDUDES, "")
        .build();

    LOGGER.info("how many women did Albert Einstein marry: {}", howManyWomenDidAlbertEinstenMarryDUDES);

    /* SPARQL */
    Query actualSparql = howManyWomenDidAlbertEinstenMarryDUDES.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", actualSparql);

    testQuery(actualSparql, "");
  }

  /**
   * Example implementing the question: "What is the highest person?"
   */
  @Test
  public void test_whatSuperlative() throws QueryException {
    String heightIRI = "http://example.com/sample#height";
    String rdfTypeIRI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    String personIRI = "http://example.com/sample#Person";

    /* what */
    Dudes whatDUDES = DudesTemplates.what();
    LOGGER.info("what: {}", whatDUDES);

    /* is */
    Dudes isDUDES = DudesTemplates.copula("1", "2");
    LOGGER.info("is: {}", isDUDES);

    /* the highest */
    Dudes theHighestDUDES = DudesTemplates.adjectiveSuperlative(
        OperatorType.MAX, heightIRI, "np");
    LOGGER.info("the highest: {}", theHighestDUDES);

    /* person */
    Dudes personDUDES =
        DudesTemplates.type(
            rdfTypeIRI,
            personIRI);
    LOGGER.info("person: {}", personDUDES);

    /* the highest person */
    Dudes theHighestPersonDUDES = new DudesBuilder(theHighestDUDES)
        .substitution(personDUDES, "np")
        .build();
    LOGGER.info("the highest person: {}", theHighestPersonDUDES);

    /* what is */
    Dudes whatIsDUDES = new DudesBuilder(isDUDES)
        .substitution(whatDUDES, "1")
        .build();
    LOGGER.info("what is: {}", whatIsDUDES);

    /* what is the highest person */
    Dudes whatIsTheHighestPersonDUDES = new DudesBuilder(whatIsDUDES)
        .substitution(theHighestPersonDUDES, "2")
        .build();
    LOGGER.info("what is the highest person: {}", whatIsTheHighestPersonDUDES);

    /* SPARQL */
    Query actualSparql = whatIsTheHighestPersonDUDES.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", actualSparql);

    testQuery(actualSparql, "");
  }
}
