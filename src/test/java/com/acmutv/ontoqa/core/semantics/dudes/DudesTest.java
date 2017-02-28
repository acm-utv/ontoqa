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
import org.apache.jena.sparql.core.Var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
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

  public static final String ONTOLOGY_PATH = DudesTest.class.getResource("/semantics/sample.owl").getPath();

  private static Ontology ONTOLOGY;

  private static final String ALBERT_EINSTEIN_IRI = "http://example.com/sample#Albert_Einstein";

  private static final String ELSA_EINSTEIN_IRI = "http://example.com/sample#Elsa_Einstein";

  private static final String SPOUSE_IRI = "http://example.com/sample#spouse";

  private static final String RDF_TYPE_IRI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";

  private static final String WOMAN_IRI = "http://example.com/sample#Woman";

  private static final String HEIGHT_IRI = "http://example.com/sample#height";

  private static final String PERSON_IRI = "http://example.com/sample#Person";

  /**
   * Loads the test ontology.
   * @throws IOException when ontology cannot be loaded.
   */
  @Before
  public void loadOntology() throws IOException {
    String prefix = "http://example.org/sample#";
    OntologyFormat format = OntologyFormat.TURTLE;
    LOGGER.info("Loading test ontology {}", ONTOLOGY_PATH);
    ONTOLOGY = KnowledgeManager.read(ONTOLOGY_PATH, prefix, format);
  }

  /**
   * Tests the loaded ontology.
   */
  @Test
  public void test() throws QueryException {
    List<String> queries = new ArrayList<>();
    queries.add(String.format("ASK WHERE { <%s> <%s> <%s> }", ALBERT_EINSTEIN_IRI, SPOUSE_IRI, ELSA_EINSTEIN_IRI));
    queries.add(String.format("SELECT DISTINCT ?x WHERE { ?x <%s> <%s> }", SPOUSE_IRI, ELSA_EINSTEIN_IRI));
    queries.add(String.format("SELECT DISTINCT ?x WHERE { <%s> <%s> ?x }", ALBERT_EINSTEIN_IRI, SPOUSE_IRI));
    queries.add(String.format("SELECT (COUNT(DISTINCT ?wife) AS ?x) WHERE { ?wife a <%s> . <%s> <%s> ?wife }", WOMAN_IRI, ALBERT_EINSTEIN_IRI, SPOUSE_IRI));
    queries.add(String.format("SELECT DISTINCT ?x WHERE { ?x a <%s> . ?x <%s> ?h } ORDER BY DESC(?h) OFFSET 0 LIMIT 1", PERSON_IRI, HEIGHT_IRI));

    List<String> answers = new ArrayList<>();
    answers.add("true");
    answers.add("http://example.com/sample#Albert_Einstein");
    answers.add("http://example.com/sample#Elsa_Einstein");
    answers.add("1");
    answers.add("http://example.com/sample#Albert_Einstein");

    for (int i = 0; i < queries.size(); i++) {
      String query = queries.get(i);
      LOGGER.info(query);
      String expected = answers.get(i);
      QueryResult result = KnowledgeManager.submit(query, ONTOLOGY);
      String actual = (result.get(0) != null) ? result.get(0).stringValue() : "No result";
      LOGGER.info("Result: {}", actual);
      Assert.assertEquals(expected, actual);
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
      if (var == null) {
        for (Var v : query.getProject().getVars()) {
          if (v.getVarName().startsWith("fout")) {
            var = v.getVarName();
            break;
          }
        }
      }
      LOGGER.info("Result variable: {}", var);
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
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the DUDES pretty string representation.
   */
  @Test
  public void test_prettyString() {
    /* DUDES */
    Dudes dudes = DudesTemplates.properNoun(ALBERT_EINSTEIN_IRI);

    String pretty = dudes.toPrettyString();

    LOGGER.info("DUDES pretty representation:\n{}", pretty);
  }

  /**
   * Example implementing the question: "Albert Einstein married Elsa Einstein?"
   */
  @Test
  public void test_ask() throws QueryException {
    /* Albert Einstein */
    Dudes albert = DudesTemplates.properNoun(ALBERT_EINSTEIN_IRI);
    LOGGER.info("Albert Einstein: {}", albert);

    /* Elsa Einstein */
    Dudes elsa = DudesTemplates.properNoun(ELSA_EINSTEIN_IRI);
    LOGGER.info("Elsa Einstein: {}", elsa);

    /* married */
    Dudes married = DudesTemplates.property(SPOUSE_IRI, "subj", "obj");
    LOGGER.info("married: {}", married);

    /* Albert Einstein married */
    Dudes albertMarried = new DudesBuilder(married)
        .substitution(albert, "subj")
        .build();
    LOGGER.info("Albert Einstein married: {}", albertMarried);

    /* Albert Einstein married Elsa Einstein */
    Dudes albertMarriedElsa = new DudesBuilder(albertMarried)
        .substitution(elsa, "obj")
        .build();
    LOGGER.info("Albert Einstein married Elsa Einstein: {}", albertMarriedElsa);

    albertMarriedElsa.setSelect(false);

    /* SPARQL */
    Query actualSparql = albertMarriedElsa.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", actualSparql);

    testQuery(actualSparql, "true");
  }

  /**
   * Example implementing the question: "Who married Elsa Einstein?"
   */
  @Test
  public void test_whoMarried() throws QueryException {
    /* who */
    Dudes who = DudesTemplates.what();
    LOGGER.info("who: {}", who);

    /* Elsa Einstein */
    Dudes elsa = DudesTemplates.properNoun(ELSA_EINSTEIN_IRI);
    LOGGER.info("Elsa Einstein: {}", elsa);

    /* married */
    Dudes married = DudesTemplates.property(SPOUSE_IRI, "subj", "obj");
    LOGGER.info("married: {}", married);

    /* who married Elsa Einstein */
    Dudes whoMarriedElsa = new DudesBuilder(married)
        .substitution(who, "subj")
        .substitution(elsa, "obj")
        .build();
    LOGGER.info("who married Elsa Einstein: {}", whoMarriedElsa);

    /* SPARQL */
    Query actualSparql = whoMarriedElsa.convertToSPARQL();
    LOGGER.info("SPARQL query: {}", actualSparql);

    testQuery(actualSparql, ALBERT_EINSTEIN_IRI);
  }

  /**
   * Example implementing the question: "Who is the spouse of Albert Einstein?"
   */
  @Test
  public void test_whoIsThe() throws QueryException {
    /* who */
    Dudes who = DudesTemplates.what();
    LOGGER.info("who: {}", who);

    /* is (copula) */
    Dudes is = DudesTemplates.copula("1", "2");
    LOGGER.info("is: {}", is);

    /* the */
    Dudes the = DudesTemplates.determiner("np");
    LOGGER.info("the: {}", the);

    /* spouse of */
    Dudes spouseOf = DudesTemplates.relationalNoun(SPOUSE_IRI, "dp", false);
    LOGGER.info("spouse of: {}", spouseOf);

    /* Albert Einstein */
    Dudes albert = DudesTemplates.properNoun(ALBERT_EINSTEIN_IRI);
    LOGGER.info("Albert Einstein: {}", albert);

    /* spouse of Albert Einstein */
    Dudes spouseOfAlbert = new DudesBuilder(spouseOf)
        .substitution(albert, "dp")
        .build();
    LOGGER.info("spouse of Albert Einstein: {}", spouseOfAlbert);

    /* the spouse of Albert Einstein */
    Dudes theSpouseOfAlbert = new DudesBuilder(the)
        .substitution(spouseOfAlbert, "np")
        .build();
    LOGGER.info("the spouse of Albert Einstein: {}", theSpouseOfAlbert);

    /* who is */
    Dudes whoIs = new DudesBuilder(is)
        .substitution(who, "1")
        .build();
    LOGGER.info("who is: {}", whoIs);

    /* who is the spouse of Albert Einstein */
    Dudes whoIsTheSpouseOfAlbert = new DudesBuilder(whoIs)
        .substitution(theSpouseOfAlbert, "2")
        .build();
    LOGGER.info("who is the spouse of Albert Einstein: {}", whoIsTheSpouseOfAlbert);

    /* SPARQL */
    Query actualSparql = whoIsTheSpouseOfAlbert.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", actualSparql);

    testQuery(actualSparql, ELSA_EINSTEIN_IRI);
  }

  /**
   * Example implementing the question: "How many women Albert Einstein married?"
   */
  @Test
  public void test_howMany() throws QueryException {
    /* count many */
    Dudes howMany = DudesTemplates.howmany("np");
    LOGGER.info("count many: {}", howMany);

    /* women */
    Dudes women = DudesTemplates.type(RDF_TYPE_IRI, WOMAN_IRI);
    LOGGER.info("women: {}", women);

    /* Albert Einstein */
    Dudes albert = DudesTemplates.properNoun(ALBERT_EINSTEIN_IRI);
    LOGGER.info("Albert Einstein: {}", albert);

    /* marry */
    Dudes marry = DudesTemplates.property(SPOUSE_IRI, "subj", "obj");
    LOGGER.info("marry: {}", marry);

    /* did */
    Dudes did = DudesTemplates.did();
    LOGGER.info("did: {}", did);

    /* count many women */
    Dudes howManyWomen = new DudesBuilder(howMany)
        .substitution(women, "np")
        .build();
    LOGGER.info("count many women: {}", howManyWomen);

    /* Albert Einstein marry */
    Dudes albertMarry = new DudesBuilder(marry)
        .substitution(albert, "subj")
        .build();
    LOGGER.info("Albert Einstein marry: {}", albertMarry);

    /* count many women Albert Einstein marry */
    Dudes howManyWomenAlbertMarry = new DudesBuilder(albertMarry)
        .substitution(howManyWomen, "obj")
        .build();
    LOGGER.info("count many women Albert Einstein marry: {}", howManyWomenAlbertMarry);

    /* count many women did Albert Einstein marry */
    Dudes howManyWomenDidAlbertMarry = new DudesBuilder(did)
        .substitution(howManyWomenAlbertMarry, "")
        .build();
    LOGGER.info("count many women did Albert Einstein marry: {}", howManyWomenDidAlbertMarry);

    /* SPARQL */
    Query actualSparql = howManyWomenDidAlbertMarry.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", actualSparql);

    testQuery(actualSparql, "1");
  }

  /**
   * Example implementing the question: "Who is the highest person?"
   */
  @Test
  public void test_whoSuperlative() throws QueryException {
    /* who */
    Dudes who = DudesTemplates.what();
    LOGGER.info("who: {}", who);

    /* is */
    Dudes is = DudesTemplates.copula("1", "2");
    LOGGER.info("is: {}", is);

    /* the highest */
    Dudes theHighest = DudesTemplates.adjectiveSuperlative(
        OperatorType.MAX, HEIGHT_IRI, "np");
    LOGGER.info("the highest: {}", theHighest);

    /* person */
    Dudes person = DudesTemplates.type(RDF_TYPE_IRI, PERSON_IRI);
    LOGGER.info("person: {}", person);

    /* the highest person */
    Dudes theHighestPerson = new DudesBuilder(theHighest)
        .substitution(person, "np")
        .build();
    LOGGER.info("the highest person: {}", theHighestPerson);

    /* who is */
    Dudes whoIs = new DudesBuilder(is)
        .substitution(who, "1")
        .build();
    LOGGER.info("who is: {}", whoIs);

    /* who is the highest person */
    Dudes whatIsTheHighestPerson = new DudesBuilder(whoIs)
        .substitution(theHighestPerson, "2")
        .build();
    LOGGER.info("who is the highest person: {}", whatIsTheHighestPerson);

    /* SPARQL */
    Query actualSparql = whatIsTheHighestPerson.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", actualSparql);

    testQuery(actualSparql, ALBERT_EINSTEIN_IRI);
  }
}
