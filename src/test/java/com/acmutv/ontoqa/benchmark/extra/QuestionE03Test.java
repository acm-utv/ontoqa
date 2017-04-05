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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static com.acmutv.ontoqa.benchmark.Common.*;

/**
 * JUnit tests for questions of class [CLASS EXTRA-03].
 * `Did Microsoft acquire an Italian company?`
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class QuestionE03Test {

  private static final Logger LOGGER = LogManager.getLogger(QuestionE03Test.class);

  private static final String QUESTION = "Did Microsoft acquire an italian company?";

  private static final Answer ANSWER = new SimpleAnswer("true");

  /**
   * Tests the question-answering with parsing.
   * @throws QuestionException when the question is malformed.
   * @throws OntoqaFatalException when the question cannot be processed due to some fatal errors.
   */
  @Test
  public void test_nlp() throws Exception {
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
        LtagTemplates.questioningDo("did"),
        DudesTemplates.empty());
    LOGGER.info("did:\n{}", did.toPrettyString());

    /* Microsoft */
    Sltag microsoft = new SimpleSltag(
        LtagTemplates.properNoun("Microsoft"),
        DudesTemplates.properNoun(MICROSOFT_IRI));
    LOGGER.info("Microsoft:\n{}", microsoft.toPrettyString());

    /* acquire */
    Sltag acquire = new SimpleSltag(
        LtagTemplates.transitiveVerbActiveIndicative("acquire", "subj", "obj"),
        DudesTemplates.property(ACQUIRE_COMPANY_IRI, "subj", "obj")
    );
    LOGGER.info("acquire:\n{}", acquire.toPrettyString());

    /* an */
    Sltag an = new SimpleSltag(
        LtagTemplates.determiner("an", "np"),
        DudesTemplates.determiner("np"));
    LOGGER.info("an:\n{}", an.toPrettyString());

    /* italian */
    Sltag italian = new SimpleSltag(
        LtagTemplates.adjectiveAttributive("italian"),
        DudesTemplates.propertyObjectValued(IS_HEADQUARTERED_IRI, ITALY_IRI)
    );
    LOGGER.info("italian:\n{}", italian.toPrettyString());

    /* company */
    Sltag company = new SimpleSltag(
        LtagTemplates.classNoun("company", false),
        DudesTemplates.classNoun(COMPANY_IRI, false)
    );
    LOGGER.info("company:\n{}", company.toPrettyString());

    /* an italian company */
    LOGGER.info("an italian company: processing...");
    Sltag anItalianCompany = new SltagBuilder(an)
        .substitution(company, "np")
        .adjunction(italian)
        .build();
    LOGGER.info("an italian company:\n{}", anItalianCompany.toPrettyString());

    /* Microsoft acquire an italian company */
    LOGGER.info("Microsoft acquire an italian company: processing...");
    Sltag microsoftAcquireAnItalianCompany = new SltagBuilder(acquire)
        .substitution(microsoft, "subj")
        .substitution(anItalianCompany, "obj")
        .build();
    LOGGER.info("Microsoft acquire an italian company:\n{}", microsoftAcquireAnItalianCompany.toPrettyString());

    /* did Microsoft acquire an italian company */
    LOGGER.info("did Microsoft acquire an italian company:");
    Sltag didMicrosoftAcquireAnItalianCompany = new SltagBuilder(microsoftAcquireAnItalianCompany)
        .adjunction(did)
        .build();
    LOGGER.info("did Microsoft acquire an italian company:\n{}", didMicrosoftAcquireAnItalianCompany.toPrettyString());

    //TODO the parsing algorithm should determine that did makes the semantic to be an ASK QUERY
    didMicrosoftAcquireAnItalianCompany.getSemantics().setSelect(false);

    /* SPARQL */
    LOGGER.info("SPARQL query: processing...");
    Query query = didMicrosoftAcquireAnItalianCompany.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", query);

    Common.test_query(query, ANSWER);
  }

  /**
   * Tests the ontology answering on raw SPARQL query submission.
   */
  @Test
  public void test_ontology() throws OntoqaFatalException, IOException, QueryException {
    String sparql = String.format("ASK WHERE { <%s> <%s> ?company . ?company <%s> <%s> }",
        MICROSOFT_IRI, ACQUIRE_COMPANY_IRI, IS_HEADQUARTERED_IRI, ITALY_IRI);
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

    /* did */
    Sltag did = new SimpleSltag(
        LtagTemplates.questioningDo("did"),
        DudesTemplates.empty());
    LOGGER.info("did:\n{}", did.toPrettyString());

    /* Microsoft */
    Sltag microsoft = new SimpleSltag(
        LtagTemplates.properNoun("Microsoft"),
        DudesTemplates.properNoun(MICROSOFT_IRI));
    LOGGER.info("Microsoft:\n{}", microsoft.toPrettyString());

    /* acquire */
    Sltag acquire = new SimpleSltag(
        LtagTemplates.transitiveVerbActiveIndicative("acquire", "subj", "obj"),
        DudesTemplates.property(ACQUIRE_COMPANY_IRI, "subj", "obj")
    );
    LOGGER.info("acquire:\n{}", acquire.toPrettyString());

    /* an */
    Sltag an = new SimpleSltag(
        LtagTemplates.determiner("an", "np"),
        DudesTemplates.determiner("np"));
    LOGGER.info("an:\n{}", an.toPrettyString());

    /* italian */
    Sltag italian = new SimpleSltag(
        LtagTemplates.adjectiveAttributive("italian"),
        DudesTemplates.propertyObjectValued(HAS_NATIONALITY_IRI, ITALY_IRI)
    );
    LOGGER.info("italian:\n{}", italian.toPrettyString());

    /* company */
    Sltag company = new SimpleSltag(
        LtagTemplates.classNoun("company", false),
        DudesTemplates.classNoun(COMPANY_IRI, false)
    );
    LOGGER.info("company:\n{}", company.toPrettyString());

    grammar.addElementarySLTAG(
        new SimpleElementarySltag("did", did)
    );

    grammar.addElementarySLTAG(
        new SimpleElementarySltag("Microsoft", microsoft)
    );

    grammar.addElementarySLTAG(
        new SimpleElementarySltag("acquire", acquire)
    );

    grammar.addElementarySLTAG(
        new SimpleElementarySltag("an", an)
    );

    grammar.addElementarySLTAG(
        new SimpleElementarySltag("italian", italian)
    );

    grammar.addElementarySLTAG(
        new SimpleElementarySltag("company", company)
    );

    return grammar;
  }

}
