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
import com.acmutv.ontoqa.core.semantics.TestAllSemantics;
import com.acmutv.ontoqa.core.semantics.base.statement.OperatorType;
import org.apache.jena.query.Query;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.acmutv.ontoqa.core.semantics.TestAllSemantics.*;

/**
 * This class realizes JUnit tests for {@link Dudes}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Dudes
 */
public class DudesOperationTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(DudesOperationTest.class);

  /**
   * Loads the test ontology.
   * @throws IOException when ontology cannot be loaded.
   */
  @BeforeClass
  public static void loadOntology() throws IOException {
    TestAllSemantics.loadOntology();
  }

  /**
   * Tests the loaded ontology.
   */
  @Test
  public void test() throws QueryException {
    TestAllSemantics.test();
  }

  /**
   * Example implementing the question: "Albert Einstein married Elsa Einstein?"
   */
  @Test
  public void test_ask() throws QueryException {
    /* Albert Einstein */
    Dudes albert = DudesTemplates.properNoun(ALBERT_EINSTEIN_IRI);
    LOGGER.info("Albert Einstein:\n{}", albert.toPrettyString());

    /* Elsa Einstein */
    Dudes elsa = DudesTemplates.properNoun(ELSA_EINSTEIN_IRI);
    LOGGER.info("Elsa Einstein:\n{}", elsa.toPrettyString());

    /* married */
    Dudes married = DudesTemplates.property(HAS_SPOUSE_IRI, "subj", "obj");
    LOGGER.info("married:\n{}", married.toPrettyString());

    /* Albert Einstein married */
    LOGGER.info("Albert Einstein married: processing...");
    Dudes albertMarried = new DudesBuilder(married)
        .substitution(albert, "subj")
        .build();
    LOGGER.info("Albert Einstein married:\n{}", albertMarried.toPrettyString());

    /* Albert Einstein married Elsa Einstein */
    LOGGER.info("Albert Einstein married Elsa Einstein: processing...");
    Dudes albertMarriedElsa = new DudesBuilder(albertMarried)
        .substitution(elsa, "obj")
        .build();
    LOGGER.info("Albert Einstein married Elsa Einstein:\n{}", albertMarriedElsa.toPrettyString());

    albertMarriedElsa.setSelect(false);

    /* SPARQL */
    LOGGER.info("SPARQL query: processing...");
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
    Dudes who = DudesTemplates.who();
    LOGGER.info("who:\n{}", who.toPrettyString());

    /* Elsa Einstein */
    Dudes elsa = DudesTemplates.properNoun(ELSA_EINSTEIN_IRI);
    LOGGER.info("Elsa Einstein:\n{}", elsa.toPrettyString());

    /* married */
    Dudes married = DudesTemplates.property(HAS_SPOUSE_IRI, "subj", "obj");
    LOGGER.info("married:\n{}", married.toPrettyString());

    /* who married Elsa Einstein */
    LOGGER.info("who married Elsa Einstein: processing...");
    Dudes whoMarriedElsa = new DudesBuilder(married)
        .substitution(who, "subj")
        .substitution(elsa, "obj")
        .build();
    LOGGER.info("who married Elsa Einstein:\n{}", whoMarriedElsa.toPrettyString());

    /* SPARQL */
    LOGGER.info("SPARQL query: processing...");
    Query actualSparql = whoMarriedElsa.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", actualSparql);

    testQuery(actualSparql, ALBERT_EINSTEIN_IRI);
  }

  /**
   * Example implementing the question: "Who is the spouse of Albert Einstein?"
   */
  @Test
  public void test_whoIsThe() throws QueryException {
    /* who */
    Dudes who = DudesTemplates.who();
    LOGGER.info("who:\n{}", who.toPrettyString());

    /* is (copula) */
    Dudes is = DudesTemplates.copula("1", "2");
    LOGGER.info("is:\n{}", is.toPrettyString());

    /* the */
    Dudes the = DudesTemplates.determiner("np");
    LOGGER.info("the:\n{}", the.toPrettyString());

    /* spouse of */
    Dudes spouseOf = DudesTemplates.relationalNoun_bis(HAS_SPOUSE_IRI, "dp", false);
    LOGGER.info("spouse of:\n{}", spouseOf.toPrettyString());

    /* Albert Einstein */
    Dudes albert = DudesTemplates.properNoun(ALBERT_EINSTEIN_IRI);
    LOGGER.info("Albert Einstein:\n{}", albert.toPrettyString());

    /* spouse of Albert Einstein */
    LOGGER.info("spouse of Albert Einstein: processing...");
    Dudes spouseOfAlbert = new DudesBuilder(spouseOf)
        .substitution(albert, "dp")
        .build();
    LOGGER.info("spouse of Albert Einstein:\n{}", spouseOfAlbert.toPrettyString());

    /* the spouse of Albert Einstein */
    LOGGER.info("the spouse of Albert Einstein: processing...");
    Dudes theSpouseOfAlbert = new DudesBuilder(the)
        .substitution(spouseOfAlbert, "np")
        .build();
    LOGGER.info("the spouse of Albert Einstein:\n{}", theSpouseOfAlbert.toPrettyString());

    /* who is */
    LOGGER.info("who is: processing...");
    Dudes whoIs = new DudesBuilder(is)
        .substitution(who, "1")
        .build();
    LOGGER.info("who is:\n{}", whoIs.toPrettyString());

    /* who is the spouse of Albert Einstein */
    LOGGER.info("who is the spouse of Albert Einstein: processing...");
    Dudes whoIsTheSpouseOfAlbert = new DudesBuilder(whoIs)
        .substitution(theSpouseOfAlbert, "2")
        .build();
    LOGGER.info("who is the spouse of Albert Einstein:\n{}", whoIsTheSpouseOfAlbert.toPrettyString());

    /* SPARQL */
    LOGGER.info("SPARQL query: processing...");
    Query actualSparql = whoIsTheSpouseOfAlbert.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", actualSparql);

    testQuery(actualSparql, ELSA_EINSTEIN_IRI);
  }

  /**
   * Example implementing the question: "Who is the highest person?"
   */
  @Test
  public void test_whoSuperlative() throws QueryException {
    /* who */
    Dudes who = DudesTemplates.who();
    LOGGER.info("who:\n{}", who.toPrettyString());

    /* is */
    Dudes is = DudesTemplates.copula("1", "2");
    LOGGER.info("is:\n{}", is.toPrettyString());

    /* the highest */
    Dudes theHighest = DudesTemplates.adjectiveSuperlative(
        OperatorType.MAX, HEIGHT_IRI, "np");
    LOGGER.info("the highest:\n{}", theHighest.toPrettyString());

    /* person */
    Dudes person = DudesTemplates.type(RDF_TYPE_IRI, PERSON_IRI);
    LOGGER.info("person:\n{}", person.toPrettyString());

    /* the highest person */
    LOGGER.info("the highest person: processing...");
    Dudes theHighestPerson = new DudesBuilder(theHighest)
        .substitution(person, "np")
        .build();
    LOGGER.info("the highest person:\n{}", theHighestPerson.toPrettyString());

    /* who is */
    LOGGER.info("who is: processing...");
    Dudes whoIs = new DudesBuilder(is)
        .substitution(who, "1")
        .build();
    LOGGER.info("who is:\n{}", whoIs.toPrettyString());

    /* who is the highest person */
    LOGGER.info("who is the highest person: processing...");
    Dudes whoIsTheHighestPerson = new DudesBuilder(whoIs)
        .substitution(theHighestPerson, "2")
        .build();
    LOGGER.info("who is the highest person:\n{}", whoIsTheHighestPerson.toPrettyString());

    /* SPARQL */
    LOGGER.info("SPARQL query: processing...");
    Query actualSparql = whoIsTheHighestPerson.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", actualSparql);

    testQuery(actualSparql, ALBERT_EINSTEIN_IRI);
  }

  /**
   * Example implementing the question: "How many women Albert Einstein married?"
   */
  @Test
  public void test_howMany() throws QueryException {
    /* how many */
    Dudes howMany = DudesTemplates.howmany("np");
    LOGGER.info("how many:\n{}", howMany.toPrettyString());

    /* women */
    Dudes women = DudesTemplates.type(RDF_TYPE_IRI, WOMAN_IRI);
    LOGGER.info("women:\n{}", women.toPrettyString());

    /* did */
    Dudes did = DudesTemplates.empty();
    LOGGER.info("did:\n{}", did.toPrettyString());

    /* Albert Einstein */
    Dudes albert = DudesTemplates.properNoun(ALBERT_EINSTEIN_IRI);
    LOGGER.info("Albert Einstein:\n{}", albert.toPrettyString());

    /* marry */
    Dudes marry = DudesTemplates.property(HAS_SPOUSE_IRI, "subj", "obj");
    LOGGER.info("marry:\n{}", marry.toPrettyString());

    /* how many women */
    LOGGER.info("how many women: processing...");
    Dudes howManyWomen = new DudesBuilder(howMany)
        .substitution(women, "np")
        .build();
    LOGGER.info("how many women:\n{}", howManyWomen.toPrettyString());

    /* Albert Einstein marry */
    LOGGER.info("Albert Einstein marry: processing...");
    Dudes albertMarry = new DudesBuilder(marry)
        .substitution(albert, "subj")
        .build();
    LOGGER.info("Albert Einstein marry:\n{}", albertMarry.toPrettyString());

    /* how many women Albert Einstein marry */
    LOGGER.info("how many women Albert Einstein marry: processing...");
    Dudes howManyWomenAlbertMarry = new DudesBuilder(albertMarry)
        .substitution(howManyWomen, "obj")
        .build();
    LOGGER.info("how many women Albert Einstein marry:\n{}", howManyWomenAlbertMarry.toPrettyString());

    /* how many women did Albert Einstein marry */
    LOGGER.info("how many women did Albert Einstein marry: processing...");
    Dudes howManyWomenDidAlbertMarry = new DudesBuilder(did)
        .substitution(howManyWomenAlbertMarry, "")
        .build();
    LOGGER.info("how many women did Albert Einstein marry:\n{}", howManyWomenDidAlbertMarry.toPrettyString());

    /* SPARQL */
    LOGGER.info("SPARQL query: processing...");
    Query actualSparql = howManyWomenDidAlbertMarry.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", actualSparql);

    testQuery(actualSparql, "1");
  }
}
