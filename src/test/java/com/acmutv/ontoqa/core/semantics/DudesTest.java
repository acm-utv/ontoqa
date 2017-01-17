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

import com.acmutv.ontoqa.core.semantics.base.OperatorStatement;
import com.acmutv.ontoqa.core.semantics.base.Slot;
import com.acmutv.ontoqa.core.semantics.base.Variable;
import com.acmutv.ontoqa.core.semantics.drs.Drs;
import com.acmutv.ontoqa.core.semantics.drs.SimpleDrs;
import com.acmutv.ontoqa.core.semantics.dudes.*;
import org.apache.jena.query.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
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
   * Example implementing the question: "Albert Einstein married Elsa Einstein?"
   */
  @Test
  public void test_ask() {
    String albertEinsteinURI = "http://dbpedia.org/resource/Albert_Einstein";
    String elsaEinsteinURI = "http://dbpedia.org/resource/Elsa_Einstein";
    String spouseURI = "http://dbpedia.org/ontology/spouse";

    /* Albert Einstein */
    Dudes albertEinsteinDUDES = new ProperNounDudes(albertEinsteinURI);
    LOGGER.info("Albert Einstein: {}", albertEinsteinDUDES);

    /* Elsa Einstein */
    Dudes elsaEinsteinDUDES = new ProperNounDudes(elsaEinsteinURI);
    LOGGER.info("Elsa Einstein: {}", elsaEinsteinDUDES);

    /* marry */
    Dudes marryDUDES = new PropertyDudes(spouseURI);
    LOGGER.info("marry: {}", marryDUDES);

    /* Albert Einstein married */
    Dudes albertEinsteinMarriedDUDES = new DudesBuilder(marryDUDES)
        .merge(albertEinsteinDUDES, PropertyDudes.SUBJECT)
        .build();
    LOGGER.info("Albert Einstein married: {}", albertEinsteinMarriedDUDES);

    /* Albert Einstein married Elsa Einstein */
    Dudes albertEinsteinMarriedElsaEinsteinDUDES = new DudesBuilder(albertEinsteinMarriedDUDES)
        .merge(elsaEinsteinDUDES, PropertyDudes.OBJECT)
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
    String elsaEinsteinURI = "http://dbpedia.org/resource/Elsa_Einstein";
    String spouseURI = "http://dbpedia.org/ontology/spouse";

    /* who */
    Dudes whoDUDES = DudesExpressions.what();
    LOGGER.info("who: {}", whoDUDES);

    /* Elsa Einstein */
    Dudes elsaEinsteinDUDES = new ProperNounDudes(elsaEinsteinURI);
    LOGGER.info("Elsa Einstein: {}", elsaEinsteinDUDES);

    /* marry */
    Dudes marryDUDES = new PropertyDudes(spouseURI);
    LOGGER.info("marry: {}", marryDUDES);

    /* who married Elsa Einstein */
    Dudes whoMarriedElsaEinsteinDUDES = new DudesBuilder(marryDUDES)
        .merge(whoDUDES, PropertyDudes.SUBJECT)
        .merge(elsaEinsteinDUDES, PropertyDudes.OBJECT)
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
    String elsaEinsteinURI = "http://dbpedia.org/resource/Elsa_Einstein";
    String spouseURI = "http://dbpedia.org/ontology/spouse";

    /* who */
    Dudes whoDUDES = DudesExpressions.what();
    LOGGER.info("who: {}", whoDUDES);

    /* is (copula) */
    Dudes isDUDES = DudesExpressions.copula("1", "2");
    LOGGER.info("is: {}", isDUDES);

    /* the */
    Drs theDRS = new SimpleDrs(0);
    Variable theVar1 = new Variable(1);
    theDRS.getVariables().add(theVar1);
    Dudes theDUDES = new BaseDudes();
    theDUDES.setDrs(theDRS);
    theDUDES.setMainDrs(0);
    theDUDES.setMainVariable(theVar1);
    theDUDES.getSlots().add(new Slot(theVar1, "np"));
    LOGGER.info("the: {}", theDUDES);

    /* spouse of */
    Dudes spouseDUDES =
        new RelationalNounDudes(spouseURI, "dp");
    LOGGER.info("spouse of: {}", spouseDUDES);

    /* Elsa Einstein */
    Dudes elsaEinsteinDUDES = new ProperNounDudes(elsaEinsteinURI);
    LOGGER.info("Elsa Einstein: {}", elsaEinsteinDUDES);

    /* spouse of Elsa Einstein */
    Dudes sposeOfElsaEinsteinDUDES = new DudesBuilder(spouseDUDES)
        .merge(elsaEinsteinDUDES, "dp")
        .build();
    LOGGER.info("spouse of Elsa Einstein: {}", sposeOfElsaEinsteinDUDES);

    /* the spouse of Elsa Einstein */
    Dudes theSposeOfElsaEinsteinDUDES = new DudesBuilder(theDUDES)
        .merge(sposeOfElsaEinsteinDUDES, "np")
        .build();
    LOGGER.info("the spouse of Elsa Einstein: {}", theSposeOfElsaEinsteinDUDES);

    /* who is */
    Dudes whoIsDUDES = new DudesBuilder(isDUDES)
        .merge(whoDUDES, "1")
        .build();
    LOGGER.info("who is: {}", whoIsDUDES);

    /* who is the spouse of Elsa Einstein */
    Dudes whoIsTheSposeOfElsaEinsteinDUDES = new DudesBuilder(whoIsDUDES)
        .merge(theSposeOfElsaEinsteinDUDES, "2")
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
    String albertEinsteinURI = "http://dbpedia.org/resource/Albert_Einstein";
    String elsaEinsteinURI = "http://dbpedia.org/resource/Elsa_Einstein";
    String spouseURI = "http://dbpedia.org/ontology/spouse";
    String rdfTypeURI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    String womenURI = "http://dbpedia.org/class/yago/Woman110787470";

    /* how many */
    Dudes howmanyDUDES = DudesExpressions.howmany("np");
    LOGGER.info("how many: {}", howmanyDUDES);

    /* women */
    Dudes womenDUDES = new ClassDudes(rdfTypeURI, womenURI);
    LOGGER.info("women: {}", womenDUDES);

    /* Albert Einstein */
    Dudes albertEinsteinDUDES = new ProperNounDudes(albertEinsteinURI);
    LOGGER.info("Albert Einstein: {}", albertEinsteinDUDES);

    /* marry */
    Dudes marryDUDES = new PropertyDudes(spouseURI);
    LOGGER.info("marry: {}", marryDUDES);

    /* did */
    Dudes didDUDES = DudesExpressions.did();
    LOGGER.info("did: {}", didDUDES);

    /* how many women */
    Dudes howmanyWomenDUDES = new DudesBuilder(howmanyDUDES)
        .merge(womenDUDES, "np")
        .build();
    LOGGER.info("how many women: {}", howmanyWomenDUDES);

    /* Albert Einstein marry */
    Dudes albertEinsteinMarryDUDES = new DudesBuilder(marryDUDES)
        .merge(albertEinsteinDUDES, PropertyDudes.SUBJECT)
        .build();
    LOGGER.info("Albert Einstein marry: {}", albertEinsteinMarryDUDES);

    /* how many Albert Einstein marry */
    Dudes howManyAlbertEinsteinMarryDUDES = new DudesBuilder(albertEinsteinMarryDUDES)
        .merge(howmanyWomenDUDES, PropertyDudes.OBJECT)
        .build();
    LOGGER.info("how many women Albert Einstein marry: {}", howManyAlbertEinsteinMarryDUDES);

    /* how many women did Albert Einstein marry */
    Dudes howManyWomenDidAlbertEinstenMarryDUDES = new DudesBuilder(didDUDES)
        .merge(howManyAlbertEinsteinMarryDUDES, "")
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
    String prominenceURI = "http://dbpedia.org/ontology/prominence";
    String rdfTypeIRI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    String mountainIRI = "http://dbpedia.org/ontology/Mountain";

    /* what */
    Dudes whatDUDES = DudesExpressions.what();
    LOGGER.info("what: {}", whatDUDES);

    /* is */
    Dudes isDUDES = DudesExpressions.copula("1", "2");
    LOGGER.info("is: {}", isDUDES);

    /* the highest */
    Dudes theHighestDUDES = new SuperlativeDudes(
        OperatorStatement.Operator.MAX, prominenceURI,
        "np");
    LOGGER.info("the highest: {}", theHighestDUDES);

    /* mountain */
    Dudes mountainDUDES =
        new ClassDudes(
            rdfTypeIRI,
            mountainIRI);
    LOGGER.info("mountain: {}", mountainDUDES);

    /* the highest mountain */
    Dudes theHighestMountainDUDES = new DudesBuilder(theHighestDUDES)
        .merge(mountainDUDES, "np")
        .build();
    LOGGER.info("the highest mountain: {}", theHighestMountainDUDES);

    /* what is */
    Dudes whatIsDUDES = new DudesBuilder(isDUDES)
        .merge(whatDUDES, "1")
        .build();
    LOGGER.info("what is: {}", whatIsDUDES);

    /* what is the highest mountain */
    Dudes whatIsTheHighestMountainDUDES = new DudesBuilder(whatIsDUDES)
        .merge(theHighestMountainDUDES, "2")
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
