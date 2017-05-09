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

package com.acmutv.ontoqa.benchmark.advanced;

import com.acmutv.ontoqa.benchmark.Common;
import com.acmutv.ontoqa.core.CoreController;
import com.acmutv.ontoqa.core.exception.OntoqaFatalException;
import com.acmutv.ontoqa.core.exception.QueryException;
import com.acmutv.ontoqa.core.exception.QuestionException;
import com.acmutv.ontoqa.core.grammar.Grammar;
import com.acmutv.ontoqa.core.knowledge.answer.Answer;
import com.acmutv.ontoqa.core.knowledge.answer.SimpleAnswer;
import com.acmutv.ontoqa.core.knowledge.ontology.Ontology;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.acmutv.ontoqa.benchmark.Common.*;

/**
 * JUnit tests for questions of class [CLASS ADVANCED-01].
 * `Who is the CEO of the most valuable company?`
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class QuestionA01Test {

  private static final Logger LOGGER = LoggerFactory.getLogger(QuestionA01Test.class);

  private static final String QUESTION = "Who are the corporate officers of the most valuable company?";

  private static final Answer ANSWER = new SimpleAnswer(LUCA_MAESTRI_IRI);

  private static final String QUERY = String.format("SELECT DISTINCT  ?v4\n" +
      "WHERE\n" +
      "  { ?v14  <%s>  ?v4 . \n" +
      "    ?v14  <%s>  ?v12\n" +
      "  }\n" +
      "ORDER BY DESC(?v12)\n" +
      "OFFSET  0\n" +
      "LIMIT   1\n", HAS_CORPORATE_OFFICER_IRI, HAS_COMPANY_VALUE_IRI);

  private static final String QUERY_bis = String.format("SELECT DISTINCT  ?v4\n" +
      "WHERE\n" +
      "  { ?v14  <%s>  ?v12 . \n" +
      "    ?v14  <%s>  ?v4\n" +
      "  }\n" +
      "ORDER BY DESC(?v12)\n" +
      "OFFSET  0\n" +
      "LIMIT   1\n", HAS_COMPANY_VALUE_IRI, HAS_CORPORATE_OFFICER_IRI);

  /**
   * Tests the question-answering with parsing.
   * @throws QuestionException when the question is malformed.
   * @throws OntoqaFatalException when the question cannot be processed due to some fatal errors.
   */
  @Test
  public void test_nlp() throws Exception {
    final Grammar grammar = Common.getGrammar();
    final Ontology ontology = Common.getOntology();
    final Pair<Query,Answer> result = CoreController.process(QUESTION, grammar, ontology);
    final Query query = result.getKey();
    final Answer answer = result.getValue();
    LOGGER.info("Query: {}", query);
    LOGGER.info("Answer: {}", answer);
    Assert.assertTrue(QUERY.equals(query.toString()) || QUERY_bis.equals(query.toString()));
    Assert.assertEquals(ANSWER, answer);
  }

  /**
   * Tests the ontology answering on raw SPARQL query submission.
   */
  @Test
  public void test_ontology() throws OntoqaFatalException, IOException, QueryException {
    String sparql = String.format("SELECT DISTINCT  ?v4 WHERE { ?v14  <%s>  ?v4 . ?v14  <%s>  ?v12 } " +
        "ORDER BY DESC(?v12) OFFSET 0 LIMIT 1", HAS_CORPORATE_OFFICER_IRI, HAS_COMPANY_VALUE_IRI);
    Query query = QueryFactory.create(sparql);
    LOGGER.debug("SPARQL query:\n{}", query);
    Common.test_query(query, ANSWER);
  }

}
