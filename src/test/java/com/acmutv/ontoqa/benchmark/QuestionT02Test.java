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

package com.acmutv.ontoqa.benchmark;

import com.acmutv.ontoqa.core.CoreController;
import com.acmutv.ontoqa.core.exception.LTAGException;
import com.acmutv.ontoqa.core.exception.OntoqaFatalException;
import com.acmutv.ontoqa.core.exception.QueryException;
import com.acmutv.ontoqa.core.exception.QuestionException;
import com.acmutv.ontoqa.core.grammar.CommonGrammar;
import com.acmutv.ontoqa.core.grammar.Grammar;
import com.acmutv.ontoqa.core.knowledge.answer.Answer;
import com.acmutv.ontoqa.core.knowledge.answer.SimpleAnswer;
import com.acmutv.ontoqa.core.knowledge.ontology.Ontology;
import com.acmutv.ontoqa.core.semantics.dudes.DudesTemplates;
import com.acmutv.ontoqa.core.semantics.sltag.*;
import com.acmutv.ontoqa.core.syntax.ltag.LtagTemplates;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.acmutv.ontoqa.benchmark.Common.HAS_CHAIRMAN_IRI;
import static com.acmutv.ontoqa.benchmark.Common.HAS_NETINCOME_IRI;
import static com.acmutv.ontoqa.benchmark.Common.MICROSOFT_IRI;

/**
 * JUnit tests for questions of class [CLASS BASIC-11].
 * `What is the net income of Microsoft?`
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class QuestionT02Test {

  private static final Logger LOGGER = LoggerFactory.getLogger(QuestionT02Test.class);

  private static final String QUESTION = "What is the net income of Microsoft?";

  private static final Answer ANSWER = new SimpleAnswer("16.8");

  /**
   * Tests the question-answering with parsing.
   * @throws QuestionException when the question is malformed.
   * @throws OntoqaFatalException when the question cannot be processed due to some fatal errors.
   */
  @Test
  public void test_nlp() throws Exception {
    Grammar grammar = Common.getGrammar();
    grammar.remove("net income of");
    /* net income of */
    ElementarySltag NET_INCOME_OF = new SimpleElementarySltag("net income of",
        LtagTemplates.relationalPrepositionalNoun("net income", "of", "subj", false),
        DudesTemplates.relationalNoun_bis(HAS_NETINCOME_IRI, "subj",false)
    );
    grammar.addElementarySLTAG(NET_INCOME_OF);
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
    grammar.remove("net income of");
    /* net income of */
    ElementarySltag NET_INCOME_OF = new SimpleElementarySltag("net income of",
        LtagTemplates.relationalPrepositionalNoun("net income", "of", "subj", false),
        DudesTemplates.relationalNoun_bis(HAS_NETINCOME_IRI, "subj",false)
    );
    grammar.addElementarySLTAG(NET_INCOME_OF);
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

    /* net income of */
    Sltag netIncomeOf = new SimpleSltag(
        LtagTemplates.relationalPrepositionalNoun("net income", "of", "subj", false),
        DudesTemplates.relationalNoun_bis(HAS_NETINCOME_IRI, "subj",false)
    );
    LOGGER.info("net income of:\n{}", netIncomeOf.toPrettyString());

    /* Microsoft */
    Sltag microsoft = new SimpleSltag(
        LtagTemplates.properNoun("Microsoft"),
        DudesTemplates.properNoun(MICROSOFT_IRI));
    LOGGER.info("Microsoft:\n{}", microsoft.toPrettyString());

    /* what is */
    LOGGER.info("what is: processing...");
    Sltag whatIs = new SltagBuilder(is)
        .substitution(what, "1")
        .build();
    LOGGER.info("what is:\n{}", whatIs.toPrettyString());

    /* the net income of */
    LOGGER.info("the net income of: processing...");
    Sltag theNetIncomeOf = new SltagBuilder(the, true)
        .substitution(netIncomeOf, "np")
        .build();
    LOGGER.info("the net income of:\n{}", theNetIncomeOf.toPrettyString());

    /* the net income of Microsoft */
    LOGGER.info("the net income of Microsoft:");
    Sltag theNetIncomeOfMicrosoft = new SltagBuilder(theNetIncomeOf)
        .substitution(microsoft, "subj")
        .build();
    LOGGER.info("the net income of Microsoft:\n{}", theNetIncomeOfMicrosoft.toPrettyString());

    /* what is the net income of Microsoft */
    LOGGER.info("what is the net income of Microsoft: processing...");
    Sltag whatIsTheNetIncomeOfApple = new SltagBuilder(whatIs)
        .substitution(theNetIncomeOfMicrosoft, "2")
        .build();
    LOGGER.info("what is the net income of Microsoft:\n{}", whatIsTheNetIncomeOfApple.toPrettyString());

    /* SPARQL */
    LOGGER.info("SPARQL query: processing...");
    Query query = whatIsTheNetIncomeOfApple.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", query);

    Common.test_query(query, ANSWER);
  }

  /**
   * Tests the ontology answering on raw SPARQL query submission.
   */
  @Test
  public void test_ontology() throws OntoqaFatalException, IOException, QueryException {
    String sparql = String.format("SELECT ?x WHERE { <%s> <%s> ?x }", MICROSOFT_IRI, HAS_NETINCOME_IRI);
    Query query = QueryFactory.create(sparql);
    LOGGER.debug("SPARQL query:\n{}", query);
    Common.test_query(query, ANSWER);
  }
}
