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
import com.acmutv.ontoqa.core.knowledge.answer.Answer;
import com.acmutv.ontoqa.core.knowledge.answer.SimpleAnswer;
import com.acmutv.ontoqa.core.knowledge.ontology.Ontology;
import com.acmutv.ontoqa.core.semantics.dudes.DudesTemplates;
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
 * JUnit tests for questions of class [CLASS EXTRA-02].
 * `Did Microsoft acquire a company headquartered in Italy?`
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class QuestionE02Test {

  private static final Logger LOGGER = LoggerFactory.getLogger(QuestionE02Test.class);

  private static final String QUESTION = "Did Microsoft acquire a company headquartered in Italy?";

  private static final Answer ANSWER = new SimpleAnswer("false");

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
    /* did */
    Sltag did = new SimpleSltag(
        LtagTemplates.questioningDo_bis("did"),
        DudesTemplates.empty());
    LOGGER.info("did:\n{}", did.toPrettyString());

    /* Microsoft */
    Sltag microsoft = new SimpleSltag(
        LtagTemplates.properNoun("Microsoft"),
        DudesTemplates.properNoun(MICROSOFT_IRI));
    LOGGER.info("Microsoft:\n{}", microsoft.toPrettyString());

    /* acquire */
    Sltag acquire = new SimpleSltag(
        LtagTemplates.transitiveVerbActiveIndicative("acquire", "company2", "company1"),
        DudesTemplates.property(IS_ACQUIRED_BY_IRI, "company", "company2")
    );
    LOGGER.info("acquire:\n{}", acquire.toPrettyString());

    /* a */
    Sltag a = new SimpleSltag(
        LtagTemplates.determiner("a", "np"),
        DudesTemplates.determiner("np"));
    LOGGER.info("a:\n{}", a.toPrettyString());

    /* company */
    Sltag company = new SimpleSltag(
        LtagTemplates.classNoun("company", false),
        DudesTemplates.classNoun(COMPANY_IRI, false)
    );
    LOGGER.info("company:\n{}", company.toPrettyString());

    /* headquartered in */
    Sltag headquarteredIn = new SimpleSltag(
        LtagTemplates.adjectivePrepositional("headquartered", "in", "nation"),
        DudesTemplates.property(HAS_HEADQUARTER_IRI, null, "nation")
    );
    LOGGER.info("headquartered in:\n{}", headquarteredIn.toPrettyString());

    /* Italy */
    Sltag italy = new SimpleSltag(
        LtagTemplates.properNoun("Italy"),
        DudesTemplates.properNoun(ITALY_IRI));
    LOGGER.info("Italy:\n{}", italy.toPrettyString());

    /* a company */
    LOGGER.info("a company: processing...");
    Sltag aCompany = new SltagBuilder(a)
        .substitution(company, "np")
        .build();
    LOGGER.info("a company:\n{}", aCompany.toPrettyString());

    /* a company headquartered in */
    LOGGER.info("a company headquartered in: processing...");
    Sltag aCompanyHeadquarteredIn = new SltagBuilder(aCompany)
        .adjunctionAfter(headquarteredIn, "company")
        .build();
    LOGGER.info("a company headquartered in:\n{}", aCompanyHeadquarteredIn.toPrettyString());

    /* a company headquartered in Italy */
    LOGGER.info("a company headquartered in Italy: processing...");
    Sltag aCompanyHeadquarteredInItaly = new SltagBuilder(aCompanyHeadquarteredIn)
        .substitution(italy, "nation")
        .build();
    LOGGER.info("a company headquartered in Italy:\n{}", aCompanyHeadquarteredInItaly.toPrettyString());

    /* Microsoft acquire a company headquartered in Italy */
    LOGGER.info("Microsoft acquire a company headquartered in Italy: processing...");
    Sltag microsoftAcquireACompanyHeadquarteredInItaly = new SltagBuilder(acquire)
        .substitution(microsoft, "subj")
        .substitution(aCompanyHeadquarteredInItaly, "obj")
        .build();
    LOGGER.info("Microsoft acquire a company headquartered in Italy:\n{}", microsoftAcquireACompanyHeadquarteredInItaly.toPrettyString());

    /* did Microsoft acquire a company headquartered in Italy */
    LOGGER.info("did Microsoft acquire a company headquartered in Italy:");
    Sltag didMicrosoftAcquireACompanyHeadquarteredInItaly = new SltagBuilder(microsoftAcquireACompanyHeadquarteredInItaly)
        .adjunction(did)
        .build();
    LOGGER.info("did Microsoft acquire a company headquartered in Italy:\n{}", didMicrosoftAcquireACompanyHeadquarteredInItaly.toPrettyString());

    //TODO the parsing algorithm should determine that did makes the semantic to be an ASK QUERY
    didMicrosoftAcquireACompanyHeadquarteredInItaly.getSemantics().setSelect(false);

    /* SPARQL */
    LOGGER.info("SPARQL query: processing...");
    Query query = didMicrosoftAcquireACompanyHeadquarteredInItaly.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", query);

    Common.test_query(query, ANSWER);
  }

  /**
   * Tests the ontology answering on raw SPARQL query submission.
   */
  @Test
  public void test_ontology() throws OntoqaFatalException, IOException, QueryException {
    String sparql = String.format("ASK WHERE { ?company <%s> <%s> . ?company <%s> <%s> }",
        IS_ACQUIRED_BY_IRI, MICROSOFT_IRI, HAS_HEADQUARTER_IRI, ITALY_IRI);
    Query query = QueryFactory.create(sparql);
    LOGGER.debug("SPARQL query:\n{}", query);
    Common.test_query(query, ANSWER);
  }
}
