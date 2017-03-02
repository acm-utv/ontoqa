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
import com.acmutv.ontoqa.core.exception.LTAGException;
import com.acmutv.ontoqa.core.exception.OntoqaFatalException;
import com.acmutv.ontoqa.core.exception.QueryException;
import com.acmutv.ontoqa.core.exception.QuestionException;
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
 * JUnit tests for questions of class [CLASS BASIC-5].
 * `What is the name of the CEO of Apple?`
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class QuestionB05Test {

  private static final Logger LOGGER = LogManager.getLogger(QuestionB05Test.class);

  private static final String QUESTION = "What is the name of the CEO of Apple?";

  private static final Answer ANSWER = new SimpleAnswer(TIM_COOK_IRI);

  /**
   * Tests the question-answering with parsing.
   * @throws QuestionException when the question is malformed.
   * @throws OntoqaFatalException when the question cannot be processed due to some fatal errors.
   */
  @Test
  public void test_nlp() throws OntoqaFatalException, QuestionException, QueryException {
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
  public void test_manual() throws OntoqaFatalException, QuestionException, QueryException, IOException, LTAGException {
    /* what */
    Sltag what = new SimpleSltag(LtagTemplates.wh("what"), DudesTemplates.what());
    LOGGER.info("what:\n{}", what.toPrettyString());

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

    /* name of */
    Sltag nameOf = new SimpleSltag(
        LtagTemplates.relationalPrepositionalNoun("name", "of", "subj", false),
        DudesTemplates.empty()
    );
    LOGGER.info("name of:\n{}", nameOf.toPrettyString());

    /* CEO of */
    Sltag ceoOf = new SimpleSltag(
        LtagTemplates.relationalPrepositionalNoun("CEO", "of", "obj", false),
        DudesTemplates.relationalNounInverse(IS_CEO_OF_IRI, "obj",false)
    );
    LOGGER.info("CEO of:\n{}", ceoOf.toPrettyString());

    /* Apple */
    Sltag apple = new SimpleSltag(
        LtagTemplates.properNoun("Apple"),
        DudesTemplates.properNoun(APPLE_IRI));
    LOGGER.info("Apple:\n{}", apple.toPrettyString());

    /* what is */
    LOGGER.info("what is: processing...");
    Sltag whatIs = new SltagBuilder(is)
        .substitution(what, "1")
        .build();
    LOGGER.info("what is:\n{}", whatIs.toPrettyString());

    /* the name of */
    LOGGER.info("the name of: processing...");
    Sltag theNameOf = new SltagBuilder(the, true)
        .substitution(nameOf, "np")
        .build();
    LOGGER.info("the name of:\n{}", theNameOf.toPrettyString());

    /* the CEO of */
    LOGGER.info("the CEO of: processing...");
    Sltag theCEOOf = new SltagBuilder(the, true)
        .substitution(ceoOf, "np")
        .build();
    LOGGER.info("the CEO of:\n{}", theCEOOf.toPrettyString());

    /* the CEO of Apple */
    LOGGER.info("the CEO of Apple:");
    Sltag theCEOOfApple = new SltagBuilder(theCEOOf)
        .substitution(apple, "obj")
        .build();
    LOGGER.info("the CEO of Apple:\n{}", theCEOOfApple.toPrettyString());

    /* the name of the CEO of Apple */
    LOGGER.info("the name of the CEO of Apple: processing...");
    Sltag theNameOfTheCEOOfApple = new SltagBuilder(theNameOf)
        .substitution(theCEOOfApple, "subj")
        .build();
    LOGGER.info("the name of the CEO of Apple:\n{}", theNameOfTheCEOOfApple.toPrettyString());

    /* what is the name of the CEO of Apple */
    LOGGER.info("what is the name of the CEO of Apple: processing...");
    Sltag whatIsTheNameOfTheCEOOfApple = new SltagBuilder(whatIs)
        .substitution(theCEOOfApple, "2")
        .build();
    LOGGER.info("what is the name of the CEO of Apple:\n{}", whatIsTheNameOfTheCEOOfApple.toPrettyString());

    /* SPARQL */
    LOGGER.info("SPARQL query: processing...");
    Query query = whatIsTheNameOfTheCEOOfApple.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", query);

    Common.test_query(query, ANSWER);
  }

  /**
   * Tests the ontology answering on raw SPARQL query submission.
   */
  @Test
  public void test_ontology() throws OntoqaFatalException, IOException, QueryException {
    String sparql = String.format("SELECT ?x WHERE { ?x <%s> <%s> }", IS_CEO_OF_IRI, APPLE_IRI);
    Query query = QueryFactory.create(sparql);
    LOGGER.debug("SPARQL query:\n{}", query);
    Common.test_query(query, ANSWER);
  }
}
