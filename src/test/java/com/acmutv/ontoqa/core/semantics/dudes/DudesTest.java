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

import com.acmutv.ontoqa.core.semantics.base.statement.OperatorType;
import org.apache.jena.query.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

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

  /**
   * Tests the DUDES pretty string representation.
   */
  @Test
  public void test_prettyString() {
    /* DUDES */
    Dudes dudes = DudesTemplates.properNoun("http://dbpedia.org/resource/Uruguay");

    String pretty = dudes.toPrettyString();

    LOGGER.debug("DUDES pretty representation:\n{}", pretty);
  }

  /**
   * Example implementing the question: "Albert Einstein married Elsa Einstein?"
   */
  @Test
  public void test_ask() {
    String albertEinsteinIRI = "http://dbpedia.org/resource/Albert_Einstein";
    String elsaEinsteinIRI = "http://dbpedia.org/resource/Elsa_Einstein";
    String spouseIRI = "http://dbpedia.org/ontology/spouse";

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
    LOGGER.info("SPARQL query: {}", actualSparql);
    /*
    String expectedSparql = String.format("ASK\nWHERE\n  { <%s>\n              <%s>  <%s>}\n",
        albertEinsteinURI, spouseURI, elsaEinsteinURI);

    Assert.assertEquals(expectedSparql, actualSparql.toString());
    */
  }

  /**
   * Example implementing the question: "Who married Elsa Einstein?"
   */
  @Test
  public void test_whoMarried() {
    String elsaEinsteinIRI = "http://dbpedia.org/resource/Elsa_Einstein";
    String spouseIRI = "http://dbpedia.org/ontology/spouse";

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

    /*
    String expectedSparql = String.format("SELECT DISTINCT  ?v5\nWHERE\n  { ?v5  <%s>  <%s>}\n",
        spouseURI, elsaEinsteinURI);

    Assert.assertEquals(expectedSparql, actualSparql.toString());
    */
  }

  /**
   * Example implementing the question: "Who is the spouse of Elsa Einstein?"
   */
  @Test
  public void test_whoIsThe() {
    String elsaEinsteinIRI = "http://dbpedia.org/resource/Elsa_Einstein";
    String spouseIRI = "http://dbpedia.org/ontology/spouse";

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

    /* Elsa Einstein */
    Dudes elsaEinsteinDUDES = DudesTemplates.properNoun(elsaEinsteinIRI);
    LOGGER.info("Elsa Einstein: {}", elsaEinsteinDUDES);

    /* spouse of Elsa Einstein */
    Dudes sposeOfElsaEinsteinDUDES = new DudesBuilder(spouseDUDES)
        .substitution(elsaEinsteinDUDES, "dp")
        .build();
    LOGGER.info("spouse of Elsa Einstein: {}", sposeOfElsaEinsteinDUDES);

    /* the spouse of Elsa Einstein */
    Dudes theSposeOfElsaEinsteinDUDES = new DudesBuilder(theDUDES)
        .substitution(sposeOfElsaEinsteinDUDES, "np")
        .build();
    LOGGER.info("the spouse of Elsa Einstein: {}", theSposeOfElsaEinsteinDUDES);

    /* who is */
    Dudes whoIsDUDES = new DudesBuilder(isDUDES)
        .substitution(whoDUDES, "1")
        .build();
    LOGGER.info("who is: {}", whoIsDUDES);

    /* who is the spouse of Elsa Einstein */
    Dudes whoIsTheSposeOfElsaEinsteinDUDES = new DudesBuilder(whoIsDUDES)
        .substitution(theSposeOfElsaEinsteinDUDES, "2")
        .build();
    LOGGER.info("who is the spouse of Elsa Einstein: {}", whoIsTheSposeOfElsaEinsteinDUDES);

    /* SPARQL */
    Query actualSparql = whoIsTheSposeOfElsaEinsteinDUDES.convertToSPARQL();
    LOGGER.info("SPARQL query: {}", actualSparql);

    /*
    String expectedSparql = String.format("SELECT DISTINCT  ?v4\n" +
            "WHERE\n" +
            "  { ?v4  <%s>  ?v4 . \n" +
            "    <%s>\n" +
            "              <%s>  ?v4\n" +
            "  }\n",
        "http://www.w3.org/2002/07/owl#sameAs", elsaEinsteinURI, spouseURI);

    Assert.assertEquals(expectedSparql, actualSparql.toString());
    */
  }

  /**
   * Example implementing the question: "How many women Albert Einstein married?"
   */
  @Test
  public void test_howMany() {
    String albertEinsteinIRI = "http://dbpedia.org/resource/Albert_Einstein";
    String spouseIRI = "http://dbpedia.org/ontology/spouse";
    String rdfTypeIRI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    String womenIRI = "http://dbpedia.org/class/yago/Woman110787470";

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
    LOGGER.info("SPARQL query: {}", actualSparql);

    /*
    String expectedSparql = String.format("SELECT DISTINCT  (COUNT(DISTINCT ?v10))\n" +
            "WHERE\n" +
            "  { ?v10  a                     <%s> . \n" +
            "    <%s>\n" +
            "              <%s>  ?v10\n" +
            "  }\n",
        womenURI, albertEinsteinURI, spouseURI);

    Assert.assertEquals(expectedSparql, actualSparql.toString());
    */
  }

  /**
   * Example implementing the question: "What is the highest mountain?"
   */
  @Test
  public void test_whatSuperlative() {
    String prominenceIRI = "http://dbpedia.org/ontology/prominence";
    String rdfTypeIRI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    String mountainIRI = "http://dbpedia.org/ontology/Mountain";

    /* what */
    Dudes whatDUDES = DudesTemplates.what();
    LOGGER.info("what: {}", whatDUDES);

    /* is */
    Dudes isDUDES = DudesTemplates.copula("1", "2");
    LOGGER.info("is: {}", isDUDES);

    /* the highest */
    Dudes theHighestDUDES = DudesTemplates.adjectiveSuperlative(
        OperatorType.MAX, prominenceIRI, "np");
    LOGGER.info("the highest: {}", theHighestDUDES);

    /* mountain */
    Dudes mountainDUDES =
        DudesTemplates.type(
            rdfTypeIRI,
            mountainIRI);
    LOGGER.info("mountain: {}", mountainDUDES);

    /* the highest mountain */
    Dudes theHighestMountainDUDES = new DudesBuilder(theHighestDUDES)
        .substitution(mountainDUDES, "np")
        .build();
    LOGGER.info("the highest mountain: {}", theHighestMountainDUDES);

    /* what is */
    Dudes whatIsDUDES = new DudesBuilder(isDUDES)
        .substitution(whatDUDES, "1")
        .build();
    LOGGER.info("what is: {}", whatIsDUDES);

    /* what is the highest mountain */
    Dudes whatIsTheHighestMountainDUDES = new DudesBuilder(whatIsDUDES)
        .substitution(theHighestMountainDUDES, "2")
        .build();
    LOGGER.info("what is the highest mountain: {}", whatIsTheHighestMountainDUDES);

    /* SPARQL */
    Query actualSparql = whatIsTheHighestMountainDUDES.convertToSPARQL();
    LOGGER.info("SPARQL query: {}", actualSparql);

    /*
    String expectedSparql = String.format("SELECT DISTINCT  ?v4\n" +
            "WHERE\n" +
            "  { {  }\n" +
            "    ?v4  <%s>  ?v4 . \n" +
            "    ?v4  a                     <%s> . \n" +
            "    ?v4  <%s>  ?v6\n" +
            "  }\n" +
            "ORDER BY DESC(?v6)\n" +
            "OFFSET  0\n" +
            "LIMIT   1\n",
        "http://www.w3.org/2002/07/owl#sameAs", mountainIRI, prominenceURI);

    Assert.assertEquals(expectedSparql, actualSparql.toString());
    */
  }
}
