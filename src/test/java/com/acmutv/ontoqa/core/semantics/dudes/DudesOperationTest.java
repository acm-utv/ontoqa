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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

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

  private static final Logger LOGGER = LogManager.getLogger(DudesOperationTest.class);

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
    Dudes married = DudesTemplates.property(SPOUSE_IRI, "subj", "obj");
    LOGGER.info("married:\n{}", married.toPrettyString());

    /* Albert Einstein married */
    Dudes albertMarried = new DudesBuilder(married)
        .substitution(albert, "subj")
        .build();
    LOGGER.info("Albert Einstein married:\n{}", albertMarried.toPrettyString());

    /* Albert Einstein married Elsa Einstein */
    Dudes albertMarriedElsa = new DudesBuilder(albertMarried)
        .substitution(elsa, "obj")
        .build();
    LOGGER.info("Albert Einstein married Elsa Einstein:\n{}", albertMarriedElsa.toPrettyString());

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
    Dudes who = DudesTemplates.who();
    LOGGER.info("who:\n{}", who);

    /* Elsa Einstein */
    Dudes elsa = DudesTemplates.properNoun(ELSA_EINSTEIN_IRI);
    LOGGER.info("Elsa Einstein:\n{}", elsa);

    /* married */
    Dudes married = DudesTemplates.property(SPOUSE_IRI, "subj", "obj");
    LOGGER.info("married:\n{}", married);

    /* who married Elsa Einstein */
    Dudes whoMarriedElsa = new DudesBuilder(married)
        .substitution(who, "subj")
        .substitution(elsa, "obj")
        .build();
    LOGGER.info("who married Elsa Einstein:\n{}", whoMarriedElsa);

    /* SPARQL */
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
    LOGGER.info("who:\n{}", who);

    /* is (copula) */
    Dudes is = DudesTemplates.copula("1", "2");
    LOGGER.info("is:\n{}", is);

    /* the */
    Dudes the = DudesTemplates.determiner("np");
    LOGGER.info("the:\n{}", the);

    /* spouse of */
    Dudes spouseOf = DudesTemplates.relationalNoun(SPOUSE_IRI, "dp", false);
    LOGGER.info("spouse of:\n{}", spouseOf);

    /* Albert Einstein */
    Dudes albert = DudesTemplates.properNoun(ALBERT_EINSTEIN_IRI);
    LOGGER.info("Albert Einstein:\n{}", albert);

    /* spouse of Albert Einstein */
    Dudes spouseOfAlbert = new DudesBuilder(spouseOf)
        .substitution(albert, "dp")
        .build();
    LOGGER.info("spouse of Albert Einstein:\n{}", spouseOfAlbert);

    /* the spouse of Albert Einstein */
    Dudes theSpouseOfAlbert = new DudesBuilder(the)
        .substitution(spouseOfAlbert, "np")
        .build();
    LOGGER.info("the spouse of Albert Einstein:\n{}", theSpouseOfAlbert);

    /* who is */
    Dudes whoIs = new DudesBuilder(is)
        .substitution(who, "1")
        .build();
    LOGGER.info("who is:\n{}", whoIs);

    /* who is the spouse of Albert Einstein */
    Dudes whoIsTheSpouseOfAlbert = new DudesBuilder(whoIs)
        .substitution(theSpouseOfAlbert, "2")
        .build();
    LOGGER.info("who is the spouse of Albert Einstein:\n{}", whoIsTheSpouseOfAlbert);

    /* SPARQL */
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
    LOGGER.info("who:\n{}", who);

    /* is */
    Dudes is = DudesTemplates.copula("1", "2");
    LOGGER.info("is:\n{}", is);

    /* the highest */
    Dudes theHighest = DudesTemplates.adjectiveSuperlative(
        OperatorType.MAX, HEIGHT_IRI, "np");
    LOGGER.info("the highest:\n{}", theHighest);

    /* person */
    Dudes person = DudesTemplates.type(RDF_TYPE_IRI, PERSON_IRI);
    LOGGER.info("person:\n{}", person);

    /* the highest person */
    Dudes theHighestPerson = new DudesBuilder(theHighest)
        .substitution(person, "np")
        .build();
    LOGGER.info("the highest person:\n{}", theHighestPerson);

    /* who is */
    Dudes whoIs = new DudesBuilder(is)
        .substitution(who, "1")
        .build();
    LOGGER.info("who is:\n{}", whoIs);

    /* who is the highest person */
    Dudes whoIsTheHighestPerson = new DudesBuilder(whoIs)
        .substitution(theHighestPerson, "2")
        .build();
    LOGGER.info("who is the highest person:\n{}", whoIsTheHighestPerson);

    /* SPARQL */
    Query actualSparql = whoIsTheHighestPerson.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", actualSparql);

    testQuery(actualSparql, ALBERT_EINSTEIN_IRI);
  }

  /**
   * Example implementing the question: "How many women Albert Einstein married?"
   */
  @Test
  @Ignore
  public void test_howMany() throws QueryException {
    /* how many */
    Dudes howMany = DudesTemplates.howmany("np");
    LOGGER.info("how many:\n{}", howMany);

    /* women */
    Dudes women = DudesTemplates.type(RDF_TYPE_IRI, WOMAN_IRI);
    LOGGER.info("women:\n{}", women);

    /* Albert Einstein */
    Dudes albert = DudesTemplates.properNoun(ALBERT_EINSTEIN_IRI);
    LOGGER.info("Albert Einstein:\n{}", albert);

    /* marry */
    Dudes marry = DudesTemplates.property(SPOUSE_IRI, "subj", "obj");
    LOGGER.info("marry:\n{}", marry);

    /* did */
    Dudes did = DudesTemplates.did();
    LOGGER.info("did:\n{}", did);

    /* how many women */
    Dudes howManyWomen = new DudesBuilder(howMany)
        .substitution(women, "np")
        .build();
    LOGGER.info("how many women:\n{}", howManyWomen);

    /* Albert Einstein marry */
    Dudes albertMarry = new DudesBuilder(marry)
        .substitution(albert, "subj")
        .build();
    LOGGER.info("Albert Einstein marry:\n{}", albertMarry);

    /* how many women Albert Einstein marry */
    Dudes howManyWomenAlbertMarry = new DudesBuilder(albertMarry)
        .substitution(howManyWomen, "obj")
        .build();
    LOGGER.info("how many women Albert Einstein marry:\n{}", howManyWomenAlbertMarry);

    /* how many women did Albert Einstein marry */
    Dudes howManyWomenDidAlbertMarry = new DudesBuilder(did)
        .substitution(howManyWomenAlbertMarry, "")
        .build();
    LOGGER.info("how many women did Albert Einstein marry:\n{}", howManyWomenDidAlbertMarry);

    /* SPARQL */
    Query actualSparql = howManyWomenDidAlbertMarry.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", actualSparql);

    testQuery(actualSparql, "1");
  }
}
