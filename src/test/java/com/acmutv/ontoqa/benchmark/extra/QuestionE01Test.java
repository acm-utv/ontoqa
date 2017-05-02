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

package com.acmutv.ontoqa.benchmark.extra;

import com.acmutv.ontoqa.benchmark.Common;
import com.acmutv.ontoqa.core.CoreController;
import com.acmutv.ontoqa.core.exception.OntoqaFatalException;
import com.acmutv.ontoqa.core.exception.QueryException;
import com.acmutv.ontoqa.core.exception.QuestionException;
import com.acmutv.ontoqa.core.grammar.CommonGrammar;
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
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.acmutv.ontoqa.benchmark.Common.*;

/**
 * JUnit tests for questions of class [CLASS EXTRA-01].
 * `Is Satya Nadella the CEO of Microsoft?`
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class QuestionE01Test {

  private static final Logger LOGGER = LoggerFactory.getLogger(QuestionE01Test.class);

  private static final String QUESTION = "Is Satya Nadella the CEO of Microsoft?";

  private static final Answer ANSWER = new SimpleAnswer("true");

  /**
   * Tests the question-answering with parsing.
   * @throws QuestionException when the question is malformed.
   * @throws OntoqaFatalException when the question cannot be processed due to some fatal errors.
   */
  @Test
  public void test_nlp() throws Exception {
    Grammar grammar = Common.getGrammar();
    Ontology ontology = Common.getOntology();
    final Answer answer = CoreController.process(QUESTION, grammar, ontology);
    LOGGER.info("Answer: {}", answer);
    Assert.assertEquals(ANSWER, answer);
  }

  /**
   * Tests the question-answering with parsing.
   * @throws QuestionException when the question is malformed.
   * @throws OntoqaFatalException when the question cannot be processed due to some fatal errors.
   */
  @Test
  public void test_nlp_wired() throws Exception {
    Grammar grammar = CommonGrammar.build_completeGrammar();
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
  public void test_manual() throws Exception {
    /* is */
    Sltag is = new SimpleSltag(
        LtagTemplates.copulaInterrogative("is", "1", "2"),
        DudesTemplates.copulaInterrogative("1", "2"));
    LOGGER.info("is:\n{}", is.toPrettyString());

    /* Satya Nadella */
    Sltag satya = new SimpleSltag(
        LtagTemplates.properNoun("Satya Nadella"),
        DudesTemplates.properNoun(SATYA_NADELLA_IRI));
    LOGGER.info("Satya Nadella:\n{}", satya.toPrettyString());

    /* the */
    Sltag the = new SimpleSltag(
        LtagTemplates.determiner("the", "np"),
        DudesTemplates.determiner("np"));
    LOGGER.info("the:\n{}", the.toPrettyString());

    /* CEO of */
    Sltag ceoOf = new SimpleSltag(
        LtagTemplates.relationalPrepositionalNoun("CEO", "of", "obj", false),
        DudesTemplates.relationalNounInverse(IS_CEO_OF_IRI, "obj",false)
    );
    LOGGER.info("CEO of:\n{}", ceoOf.toPrettyString());

    /* Microsoft */
    Sltag microsoft = new SimpleSltag(
        LtagTemplates.properNoun("Microsoft"),
        DudesTemplates.properNoun(MICROSOFT_IRI));
    LOGGER.info("Microsoft:\n{}", microsoft.toPrettyString());

    /* is Satya Nadella */
    LOGGER.info("is Satya Nadella: processing...");
    Sltag isSatyaNadella = new SltagBuilder(is)
        .substitution(satya, "1")
        .build();
    LOGGER.info("is Satya Nadella:\n{}", isSatyaNadella.toPrettyString());

    /* the CEO of */
    LOGGER.info("the CEO of: processing...");
    Sltag theCEOOf = new SltagBuilder(the)
        .substitution(ceoOf, "np")
        .build();
    LOGGER.info("the CEO of:\n{}", theCEOOf.toPrettyString());

    /* the CEO of Microsoft */
    LOGGER.info("the CEO of Microsoft:");
    Sltag theCEOOfMicrosoft = new SltagBuilder(theCEOOf)
        .substitution(microsoft, "obj")
        .build();
    LOGGER.info("the CEO of Microsoft:\n{}", theCEOOfMicrosoft.toPrettyString());

    /* is Satya Nadella the CEO of Microsoft */
    LOGGER.info("is Satya Nadella the CEO of Microsoft: processing...");
    Sltag isSatyaNadellaTheCEOOfMicrosoft = new SltagBuilder(isSatyaNadella)
        .substitution(theCEOOfMicrosoft, "2")
        .build();
    LOGGER.info("is Satya Nadella the CEO of Microsoft:\n{}", isSatyaNadellaTheCEOOfMicrosoft.toPrettyString());

    /* SPARQL */
    LOGGER.info("SPARQL query: processing...");
    Query query = isSatyaNadellaTheCEOOfMicrosoft.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", query);

    Common.test_query(query, ANSWER);
  }

  /**
   * Tests the ontology answering on raw SPARQL query submission.
   */
  @Test
  public void test_ontology() throws OntoqaFatalException, IOException, QueryException {
    String sparql = String.format("ASK WHERE { <%s> <%s> <%s> }", SATYA_NADELLA_IRI, IS_CEO_OF_IRI, MICROSOFT_IRI);
    Query query = QueryFactory.create(sparql);
    LOGGER.debug("SPARQL query:\n{}", query);
    Common.test_query(query, ANSWER);
  }

}
