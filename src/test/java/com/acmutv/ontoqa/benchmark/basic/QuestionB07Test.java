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

package com.acmutv.ontoqa.benchmark.basic;

import com.acmutv.ontoqa.benchmark.Common;
import com.acmutv.ontoqa.core.CoreController;
import com.acmutv.ontoqa.core.exception.*;
import com.acmutv.ontoqa.core.grammar.Grammar;
import com.acmutv.ontoqa.core.grammar.SimpleGrammar;
import com.acmutv.ontoqa.core.knowledge.answer.Answer;
import com.acmutv.ontoqa.core.knowledge.answer.SimpleAnswer;
import com.acmutv.ontoqa.core.knowledge.ontology.Ontology;
import com.acmutv.ontoqa.core.semantics.dudes.DudesTemplates;
import com.acmutv.ontoqa.core.semantics.sltag.SimpleElementarySltag;
import com.acmutv.ontoqa.core.semantics.sltag.SimpleSltag;
import com.acmutv.ontoqa.core.semantics.sltag.Sltag;
import com.acmutv.ontoqa.core.semantics.sltag.SltagBuilder;
import com.acmutv.ontoqa.core.syntax.ltag.LtagTemplates;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;

import java.io.IOException;

import static com.acmutv.ontoqa.benchmark.Common.*;

/**
 * JUnit tests for questions of class [CLASS BASIC-7].
 * `Who is the chief financial officer of Apple?`
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class QuestionB07Test {

  private static final Logger LOGGER = LogManager.getLogger(QuestionB07Test.class);

  private static final String QUESTION = "Who is the chief financial officer of Apple?";

  private static final Answer ANSWER = new SimpleAnswer(LUCA_MAESTRI_IRI);

  /**
   * Tests the question-answering with parsing.
   * @throws QuestionException when the question is malformed.
   * @throws OntoqaFatalException when the question cannot be processed due to some fatal errors.
   */
  @Test
  public void test_nlp() throws Exception {
    Grammar grammar = generateGrammar();
    Ontology ontology = Common.getOntology();
    final Answer answer = CoreController.process(QUESTION, grammar, ontology);
    LOGGER.info("Answer: {}", answer);
    Assert.assertEquals(ANSWER, answer);
  }

  /**
   * Tests the question-answering with manual compilation of SLTAG.
   * @throws QuestionException when question is malformed.
   * @throws OntoqaFatalException when question cannot be processed due to some fatal errors.
   */
  @Test
  public void test_manual() throws OntoqaFatalException, QuestionException, QueryException, LTAGException, IOException {
    /* who */
    Sltag who = new SimpleSltag(LtagTemplates.wh("who"), DudesTemplates.who());
    LOGGER.info("who:\n{}", who.toPrettyString());

    /* is */
    Sltag is = new SimpleSltag(
        LtagTemplates.copula("is", "1", "2"),
        DudesTemplates.copula("1", "2"));
    LOGGER.info("is:\n{}", is.toPrettyString());

    /* the */
    Sltag the = new SimpleSltag(
        LtagTemplates.determiner("the", "np"),
        DudesTemplates.determiner("np"));
    LOGGER.info("the:\n{}", the.toPrettyString());

    /* chief financial officer of */
    Sltag cfoOf = new SimpleSltag(
        LtagTemplates.relationalPrepositionalNoun("chief financial officer", "of", "obj", false),
        DudesTemplates.relationalNounInverse(IS_CFO_OF_IRI, "obj",false)
    );
    LOGGER.info("chief financial officer of:\n{}", cfoOf.toPrettyString());

    /* Apple */
    Sltag apple = new SimpleSltag(
        LtagTemplates.properNoun("Apple"),
        DudesTemplates.properNoun(APPLE_IRI));
    LOGGER.info("Apple:\n{}", apple.toPrettyString());

    /* who is */
    LOGGER.info("who is: processing...");
    Sltag whoIs = new SltagBuilder(is)
        .substitution(who, "1")
        .build();
    LOGGER.info("who is:\n{}", whoIs.toPrettyString());

    /* the chief financial officer of */
    LOGGER.info("the chief financial officer of: processing...");
    Sltag theCFOOf = new SltagBuilder(the)
        .substitution(cfoOf, "np")
        .build();
    LOGGER.info("the chief financial officer of:\n{}", theCFOOf.toPrettyString());

    /* the chief financial officer of Apple */
    LOGGER.info("the chief financial officer of Apple:");
    Sltag theCFOOfApple = new SltagBuilder(theCFOOf)
        .substitution(apple, "obj")
        .build();
    LOGGER.info("the chief financial officer of Apple:\n{}", theCFOOfApple.toPrettyString());

    /* who is the chief financial officer of Apple */
    LOGGER.info("who is the chief financial officer of Apple: processing...");
    Sltag whoIsTheCFOOfApple = new SltagBuilder(whoIs)
        .substitution(theCFOOfApple, "2")
        .build();
    LOGGER.info("who is the chief financial officer of Apple:\n{}", whoIsTheCFOOfApple.toPrettyString());

    /* SPARQL */
    LOGGER.info("SPARQL query: processing...");
    Query query = whoIsTheCFOOfApple.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", query);

    Common.test_query(query, ANSWER);
  }

  /**
   * Tests the ontology answering on raw SPARQL query submission.
   */
  @Test
  public void test_ontology() throws OntoqaFatalException, IOException, QueryException {
    String sparql = String.format("SELECT ?x WHERE { ?x <%s> <%s>}", IS_CFO_OF_IRI, APPLE_IRI);
    Query query = QueryFactory.create(sparql);
    LOGGER.debug("SPARQL query:\n{}", query);
    Common.test_query(query, ANSWER);
  }

  /**
   * Generates the grammar to parse the question.
   * @return the grammar to parse the question.
   */
  private static Grammar generateGrammar() {
    Grammar grammar = new SimpleGrammar();

    /* who */
    Sltag who = new SimpleSltag(LtagTemplates.wh("who"), DudesTemplates.who());
    LOGGER.info("who:\n{}", who.toPrettyString());

    /* is */
    Sltag is = new SimpleSltag(
        LtagTemplates.copula("is", "1", "2"),
        DudesTemplates.copula("1", "2"));
    LOGGER.info("is:\n{}", is.toPrettyString());

    /* the */
    Sltag the = new SimpleSltag(
        LtagTemplates.determiner("the", "np"),
        DudesTemplates.determiner("np"));
    LOGGER.info("the:\n{}", the.toPrettyString());

    /* chief financial officer of */
    Sltag cfoOf = new SimpleSltag(
        LtagTemplates.relationalPrepositionalNoun("chief financial officer", "of", "obj", false),
        DudesTemplates.relationalNounInverse(IS_CFO_OF_IRI, "obj",false)
    );
    LOGGER.info("chief financial officer of:\n{}", cfoOf.toPrettyString());

    /* Apple */
    Sltag apple = new SimpleSltag(
        LtagTemplates.properNoun("Apple"),
        DudesTemplates.properNoun(APPLE_IRI));
    LOGGER.info("Apple:\n{}", apple.toPrettyString());

    grammar.addElementarySLTAG(
        new SimpleElementarySltag("who", who)
    );

    grammar.addElementarySLTAG(
        new SimpleElementarySltag("is", is)
    );

    grammar.addElementarySLTAG(
        new SimpleElementarySltag("the", the)
    );

    grammar.addElementarySLTAG(
        new SimpleElementarySltag("chief financial officer of", cfoOf)
    );

    grammar.addElementarySLTAG(
        new SimpleElementarySltag("Apple", apple)
    );

    return grammar;
  }
}
