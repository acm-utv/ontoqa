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

package com.acmutv.ontoqa.core.semantics.sltag;

import com.acmutv.ontoqa.core.exception.LTAGException;
import com.acmutv.ontoqa.core.exception.QueryException;
import com.acmutv.ontoqa.core.semantics.TestAllSemantics;
import com.acmutv.ontoqa.core.semantics.base.statement.OperatorType;
import com.acmutv.ontoqa.core.semantics.dudes.DudesTemplates;
import com.acmutv.ontoqa.core.syntax.ltag.LtagTemplates;
import org.apache.jena.query.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static com.acmutv.ontoqa.core.semantics.TestAllSemantics.*;

/**
 * JUnit tests for operations with {@link Sltag}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Sltag
 */
@Ignore
public class SLTAGOperationTest {

  private static final Logger LOGGER = LogManager.getLogger(SLTAGOperationTest.class);

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
  public void test_ask() throws QueryException, LTAGException {
    /* Albert Einstein */
    Sltag albert = new SimpleSltag(
        LtagTemplates.properNoun("Albert Einstein"),
        DudesTemplates.properNoun(ALBERT_EINSTEIN_IRI)
    );
    LOGGER.info("Albert Einstein:\n{}", albert.toPrettyString());

    /* Elsa Einstein */
    Sltag elsa = new SimpleSltag(
        LtagTemplates.properNoun("Elsa Einstein"),
        DudesTemplates.properNoun(ELSA_EINSTEIN_IRI)
    );
    LOGGER.info("Elsa Einstein:\n{}", elsa.toPrettyString());

    /* married */
    Sltag married = new SimpleSltag(
        LtagTemplates.transitiveVerbActiveIndicative("merried", "subj", "obj"),
        DudesTemplates.property(SPOUSE_IRI, "subj", "obj")
    );
    LOGGER.info("married:\n{}", married.toPrettyString());

    /* Albert Einstein married */
    Sltag albertMarried = new SltagBuilder(married)
        .substitution(albert, "subj")
        .build();
    LOGGER.info("Albert Einstein married:\n{}", albertMarried.toPrettyString());

    /* Albert Einstein married Elsa Einstein */
    Sltag albertMarriedElsa = new SltagBuilder(albertMarried)
        .substitution(elsa, "obj")
        .build();
    LOGGER.info("Albert Einstein married Elsa Einstein:\n{}", albertMarriedElsa.toPrettyString());

    albertMarriedElsa.setSelect(false);

    /* SPARQL */
    Query actualSparql = albertMarriedElsa.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", actualSparql);

    TestAllSemantics.testQuery(actualSparql, "true");
  }

  /**
   * Example implementing the question: "Who married Elsa Einstein?"
   */
  @Test
  public void test_whoMarried() throws QueryException, LTAGException {
    /* who */
    Sltag who = new SimpleSltag(LtagTemplates.wh("who"), DudesTemplates.who());
    LOGGER.info("who:\n{}", who);

    /* Elsa Einstein */
    Sltag elsa = new SimpleSltag(
        LtagTemplates.properNoun("Elsa Einstein"),
        DudesTemplates.properNoun(ELSA_EINSTEIN_IRI)
    );
    LOGGER.info("Elsa Einstein:\n{}", elsa);

    /* married */
    Sltag married = new SimpleSltag(
        LtagTemplates.transitiveVerbActiveIndicative("married", "subj", "obj"),
        DudesTemplates.property(SPOUSE_IRI, "subj", "obj")
    );
    LOGGER.info("married:\n{}", married);

    /* who married Elsa Einstein */
    Sltag whoMarriedElsa = new SltagBuilder(married)
        .substitution(who, "subj")
        .substitution(elsa, "obj")
        .build();
    LOGGER.info("who married Elsa Einstein:\n{}", whoMarriedElsa);

    /* SPARQL */
    Query actualSparql = whoMarriedElsa.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", actualSparql);

    TestAllSemantics.testQuery(actualSparql, ALBERT_EINSTEIN_IRI);
  }

  /**
   * Example implementing the question: "Who is the spouse of Albert Einstein?"
   */
  @Test
  public void test_whoIsThe() throws QueryException, LTAGException {
    /* who */
    Sltag who = new SimpleSltag(LtagTemplates.wh("who"), DudesTemplates.who());
    LOGGER.info("who:\n{}", who);

    /* is (copula) */
    Sltag is = new SimpleSltag(
        LtagTemplates.copula("is", "1", "2"),
        DudesTemplates.copula("1", "2")
    );
    LOGGER.info("is:\n{}", is);

    /* the */
    Sltag the = new SimpleSltag(
        LtagTemplates.determiner("the", "np"),
        DudesTemplates.determiner("np")
    );
    LOGGER.info("the:\n{}", the);

    /* spouse of */
    Sltag spouseOf = new SimpleSltag(
        LtagTemplates.relationalPrepositionalNoun("spouse", "of", "dp", false),
        DudesTemplates.relationalNoun(SPOUSE_IRI, "dp", false));
    LOGGER.info("spouse of:\n{}", spouseOf);

    /* Albert Einstein */
    Sltag albert = new SimpleSltag(
        LtagTemplates.properNoun("Albert Einstein"),
        DudesTemplates.properNoun(ALBERT_EINSTEIN_IRI)
    );
    LOGGER.info("Albert Einstein:\n{}", albert);

    /* spouse of Albert Einstein */
    Sltag spouseOfAlbert = new SltagBuilder(spouseOf)
        .substitution(albert, "dp")
        .build();
    LOGGER.info("spouse of Albert Einstein:\n{}", spouseOfAlbert);

    /* the spouse of Albert Einstein */
    Sltag theSpouseOfAlbert = new SltagBuilder(the)
        .substitution(spouseOfAlbert, "np")
        .build();
    LOGGER.info("the spouse of Albert Einstein:\n{}", theSpouseOfAlbert);

    /* who is */
    Sltag whoIs = new SltagBuilder(is)
        .substitution(who, "1")
        .build();
    LOGGER.info("who is:\n{}", whoIs);

    /* who is the spouse of Albert Einstein */
    Sltag whoIsTheSpouseOfAlbert = new SltagBuilder(whoIs)
        .substitution(theSpouseOfAlbert, "2")
        .build();
    LOGGER.info("who is the spouse of Albert Einstein:\n{}", whoIsTheSpouseOfAlbert);

    /* SPARQL */
    Query actualSparql = whoIsTheSpouseOfAlbert.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", actualSparql);

    TestAllSemantics.testQuery(actualSparql, ELSA_EINSTEIN_IRI);
  }

  /**
   * Example implementing the question: "Who is the highest person?"
   */
  @Test
  public void test_whoSuperlative() throws QueryException, LTAGException {
    /* who */
    Sltag who = new SimpleSltag(LtagTemplates.wh("who"), DudesTemplates.who());
    LOGGER.info("who:\n{}", who);

    /* is (copula) */
    Sltag is = new SimpleSltag(
        LtagTemplates.copula("is", "1", "2"),
        DudesTemplates.copula("1", "2")
    );
    LOGGER.info("is:\n{}", is);

    /* the highest */
    Sltag theHighest = new SimpleSltag(
        LtagTemplates.adjectiveSuperlative("highest", "the", "np"),
        DudesTemplates.adjectiveSuperlative(OperatorType.MAX, HEIGHT_IRI, "np"));
    LOGGER.info("the highest:\n{}", theHighest);

    /* person */
    Sltag person = new SimpleSltag(
        LtagTemplates.classNoun("person", false),
        DudesTemplates.type(RDF_TYPE_IRI, PERSON_IRI)
    );
    LOGGER.info("person:\n{}", person);

    /* the highest person */
    Sltag theHighestPerson = new SltagBuilder(theHighest)
        .substitution(person, "np")
        .build();
    LOGGER.info("the highest person:\n{}", theHighestPerson);

    /* who is */
    Sltag whoIs = new SltagBuilder(is)
        .substitution(who, "1")
        .build();
    LOGGER.info("who is:\n{}", whoIs);

    /* who is the highest person */
    Sltag whoIsTheHighestPerson = new SltagBuilder(whoIs)
        .substitution(theHighestPerson, "2")
        .build();
    LOGGER.info("who is the highest person:\n{}", whoIsTheHighestPerson);

    /* SPARQL */
    Query actualSparql = whoIsTheHighestPerson.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", actualSparql);

    TestAllSemantics.testQuery(actualSparql, ALBERT_EINSTEIN_IRI);
  }

  /**
   * Example implementing the question: "How many women Albert Einstein married?"
   */
  @Test
  @Ignore
  public void test_howMany() throws QueryException, LTAGException {
    /* how many */
    Sltag howMany = new SimpleSltag(
        LtagTemplates.how("how", "many", "np"),
        DudesTemplates.howmany("np")
    );
    LOGGER.info("how many:\n{}", howMany);

    /* women */
    Sltag women = new SimpleSltag(
        LtagTemplates.classNoun("women", false),
        DudesTemplates.type(RDF_TYPE_IRI, WOMAN_IRI)
    );
    LOGGER.info("women:\n{}", women);

    /* Albert Einstein */
    Sltag albert = new SimpleSltag(
        LtagTemplates.properNoun(ALBERT_EINSTEIN_IRI),
        DudesTemplates.properNoun(ALBERT_EINSTEIN_IRI)
    );
    LOGGER.info("Albert Einstein:\n{}", albert);

    /* marry */
    Sltag marry = new SimpleSltag(
        LtagTemplates.transitiveVerbActiveIndicative("marry", "subj", "obj"),
        DudesTemplates.property(SPOUSE_IRI, "subj", "obj")
    );
    LOGGER.info("marry:\n{}", marry);

    /* did */
    Sltag did = new SimpleSltag(LtagTemplates.did(), DudesTemplates.did());
    LOGGER.info("did:\n{}", did);

    /* how many women */
    Sltag howManyWomen = new SltagBuilder(howMany)
        .substitution(women, "np")
        .build();
    LOGGER.info("how many women:\n{}", howManyWomen);

    /* Albert Einstein marry */
    Sltag albertMarry = new SltagBuilder(marry)
        .substitution(albert, "subj")
        .build();
    LOGGER.info("Albert Einstein marry:\n{}", albertMarry);

    /* how many women Albert Einstein marry */
    Sltag howManyWomenAlbertMarry = new SltagBuilder(albertMarry)
        .substitution(howManyWomen, "obj")
        .build();
    LOGGER.info("how many women Albert Einstein marry:\n{}", howManyWomenAlbertMarry);

    /* how many women did Albert Einstein marry */
    Sltag howManyWomenDidAlbertMarry = new SltagBuilder(did)
        .substitution(howManyWomenAlbertMarry, "")
        .build();
    LOGGER.info("how many women did Albert Einstein marry:\n{}", howManyWomenDidAlbertMarry);

    /* SPARQL */
    Query actualSparql = howManyWomenDidAlbertMarry.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", actualSparql);

    TestAllSemantics.testQuery(actualSparql, "1");
  }
}
