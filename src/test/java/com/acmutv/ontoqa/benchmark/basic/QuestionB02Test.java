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
import com.acmutv.ontoqa.core.knowledge.answer.Answer;
import com.acmutv.ontoqa.core.knowledge.answer.SimpleAnswer;
import com.acmutv.ontoqa.core.semantics.dudes.DudesTemplates;
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
 * JUnit tests for questions of class [CLASS BASIC-2].
 * `Who are the founders of Microsoft?`
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class QuestionB02Test {

  private static final Logger LOGGER = LogManager.getLogger(QuestionB02Test.class);

  private static final String QUESTION = "Who are the founders of Microsoft?";

  private static final Answer ANSWER = new SimpleAnswer(BILL_GATES_IRI, PAUL_ALLEN_IRI);

  /**
   * Tests the question-answering with parsing.
   * @throws QuestionException when the question is malformed.
   * @throws OntoqaFatalException when the question cannot be processed due to some fatal errors.
   */
  @Test
  public void test_nlp() throws Exception {
    Common.loadSession();
    final Answer actual = CoreController.process(QUESTION);
    Assert.assertEquals(ANSWER, actual);
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

    /* are */
    Sltag are = new SimpleSltag(
        LtagTemplates.copula("are", "1", "2"),
        DudesTemplates.copula("1", "2"));
    LOGGER.info("are:\n{}", are.toPrettyString());

    /* the */
    Sltag the = new SimpleSltag(
        LtagTemplates.determiner("the", "np"),
        DudesTemplates.determiner("np"));
    LOGGER.info("the:\n{}", the.toPrettyString());

    /* founders of */
    Sltag foundersOf = new SimpleSltag(
        LtagTemplates.relationalPrepositionalNoun("founders", "of", "obj", false),
        DudesTemplates.relationalNounInverse(IS_FOUNDER_OF_IRI, "obj",false)
    );
    LOGGER.info("foundersOf:\n{}", foundersOf.toPrettyString());

    /* Microsoft */
    Sltag microsoft = new SimpleSltag(
        LtagTemplates.properNoun("Microsoft"),
        DudesTemplates.properNoun(MICROSOFT_IRI));
    LOGGER.info("Microsoft:\n{}", microsoft.toPrettyString());

    /* who are */
    LOGGER.info("who are: processing...");
    Sltag whoAre = new SltagBuilder(are)
        .substitution(who, "1")
        .build();
    LOGGER.info("who are:\n{}", whoAre.toPrettyString());

    /* the founders of */
    LOGGER.info("the founders of: processing...");
    Sltag theFoundersOf = new SltagBuilder(the)
        .substitution(foundersOf, "np")
        .build();
    LOGGER.info("the founders of:\n{}", theFoundersOf.toPrettyString());

    /* the founders of Microsoft */
    LOGGER.info("the founders of Microsoft:");
    Sltag theFoundersOfMicrosoft = new SltagBuilder(theFoundersOf)
        .substitution(microsoft, "obj")
        .build();
    LOGGER.info("the founders of Microsoft:\n{}", theFoundersOfMicrosoft.toPrettyString());

    /* who are the founders of Microsoft */
    LOGGER.info("who are the founders of Microsoft: processing...");
    Sltag whoAreTheFoundersOfMicrosoft = new SltagBuilder(whoAre)
        .substitution(theFoundersOfMicrosoft, "2")
        .build();
    LOGGER.info("who are the founders of Microsoft:\n{}", whoAreTheFoundersOfMicrosoft.toPrettyString());

    /* SPARQL */
    LOGGER.info("SPARQL query: processing...");
    Query query = whoAreTheFoundersOfMicrosoft.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", query);

    Common.test_query(query, ANSWER);
  }

  /**
   * Tests the ontology answering on raw SPARQL query submission.
   */
  @Test
  public void test_ontology() throws OntoqaFatalException, IOException, QueryException {
    String sparql = String.format("SELECT ?x WHERE { ?x <%s> <%s> }", IS_FOUNDER_OF_IRI, MICROSOFT_IRI);
    Query query = QueryFactory.create(sparql);
    LOGGER.debug("SPARQL query:\n{}", query);
    Common.test_query(query, ANSWER);
  }
}
