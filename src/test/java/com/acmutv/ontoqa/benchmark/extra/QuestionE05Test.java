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
import com.acmutv.ontoqa.core.knowledge.answer.Answer;
import com.acmutv.ontoqa.core.knowledge.answer.SimpleAnswer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static com.acmutv.ontoqa.benchmark.Common.PREFIX;

/**
 * JUnit tests for questions of class [CLASS EXTRA-05].
 * `Where is Microsoft headquartered?`
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class QuestionE05Test {

  private static final String QUESTION = "Where is Microsoft headquartered?";

  private static final Answer ANSWER = new SimpleAnswer(
      String.format("%sUnited_States", PREFIX)
  );

  /**
   * Tests the question-answering with parsing.
   * @throws QuestionException when the question is malformed.
   * @throws OntoqaFatalException when the question cannot be processed due to some fatal errors.
   */
  @Test
  public void test_nlp() throws OntoqaFatalException, QuestionException, QueryException {
    final Answer actual = CoreController.process(QUESTION);
    Assert.assertEquals(ANSWER, actual);
  }

  /**
   * Tests the question-answering with manual compilation of SLTAG.
   * @throws QuestionException when question is malformed.
   * @throws OntoqaFatalException when question cannot be processed due to some fatal errors.
   */
  @Test
  @Ignore
  public void test_manual() throws OntoqaFatalException, QuestionException, QueryException {
    final Answer actual = CoreController.process(QUESTION);
    //TODO
    Assert.assertEquals(ANSWER, actual);
  }

  /**
   * Tests the ontology answering on raw SPARQL query submission.
   */
  @Test
  @Before
  public void test_ontology() throws OntoqaFatalException {
    String sparql = String.format("SELECT ?x WHERE{ <%sMicrosoft> <%sisHeadquartered> ?x }", PREFIX, PREFIX);
    String expected = String.format("%sUnited_States", PREFIX);
    Common.test_ontology(sparql, expected);
    Common.loadSession();
  }
}
