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

package com.acmutv.ontoqa.core;

import com.acmutv.ontoqa.core.exception.QueryException;
import com.acmutv.ontoqa.core.exception.OntoqaFatalException;
import com.acmutv.ontoqa.core.exception.QuestionException;
import com.acmutv.ontoqa.core.knowledge.answer.Answer;
import com.acmutv.ontoqa.core.knowledge.query.QueryResult;
import com.acmutv.ontoqa.core.semantics.dudes.Dudes;
import com.acmutv.ontoqa.core.knowledge.KnowledgeManager;
import com.acmutv.ontoqa.core.semantics.sltag.Sltag;
import com.acmutv.ontoqa.session.SessionManager;
import org.apache.jena.query.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The core business logic.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Dudes
 */
public class CoreController {

  private static final Logger LOGGER = LogManager.getLogger(CoreController.class);

  /**
   * The core main method.
   * It realizes the question-answering process, retrieving an answer for the given question.
   * The underlying ontology and lexicon are specified in the app configuration.
   * @param question the question.
   * @return the answer.
   * @throws QuestionException when question is malformed.
   * @throws QueryException when the SPARQL query cannot be submitted.
   * @throws OntoqaFatalException when question cannot be processed.
   */
  public static Answer process(String question)
      throws QuestionException, QueryException, OntoqaFatalException {
    LOGGER.traceEntry("question={}", question);
    QueryResult qQueryResult = getQueryResultIfNotYetImplemented(question); /* TO BE REMOVED (ONLY FOR DEVELOPMENT) */
    if (qQueryResult == null) { /* the query has been implemented */
      question = normalizeQuestion(question);
      LOGGER.info("Normalized question: {}", question);
      Sltag sltag = parse(question);
      Dudes dudes = sltag.getSemantics();
      Query query = dudes.convertToSPARQL();
      qQueryResult = KnowledgeManager.submit(query.toString(), SessionManager.getOntology());
    }
    Answer answer = qQueryResult.toAnswer();
    return LOGGER.traceExit(answer);
  }

  /**
   * Returns the normalized version of {@code question}.
   * @param question the question to normalize.
   * @return the normalized version of {@code question}.
   * @throws QuestionException when question cannot be processed.
   */
  public static String normalizeQuestion(final String question) throws QuestionException {
    if (question == null || question.isEmpty())
      throw new QuestionException("Question is empty");
    return question.replaceAll("((?:\\s)+)", " ").replaceAll("((?:\\s)*\\?)", "");
  }


  /**
   * The parsing algorithm.
   * @param question the question to parse.
   * @return the parsed Sltag.
   * @throws OntoqaFatalException when parsing cannot be executed.
   */
  private static Sltag parse(String question) throws OntoqaFatalException {
    throw new OntoqaFatalException("Parsing is not yet implemented.");
  }

  /* TO BE REMOVED (ONLY FOR DEVELOPMENT) */
  /**
   * Returns the query result for {@code question} against {@code ONTOLOGY}.
   * @param question the natural language question.
   * @return the submitted query result.
   * @throws QuestionException when question cannot be processed.
   * @throws QueryException when the SPARQL query cannot be submitted.
   */
  private static QueryResult getQueryResultIfNotYetImplemented(final String question)
      throws QuestionException, QueryException {
    LOGGER.traceEntry(question);
    if (question == null) throw new QuestionException("The question cannot be null");
    String prefix = "http://www.semanticweb.org/debby/ontologies/2016/11/organization-ontology#";
    String sparql;
    if (question.equalsIgnoreCase("WHO FOUNDED MICROSOFT?")) {
      sparql = String.format("SELECT ?x WHERE { ?x <%sisFounderOf> <%sMicrosoft> }", prefix, prefix);
    } else if (question.equalsIgnoreCase("WHO ARE THE FOUNDERS OF MICROSOFT?")) {
      sparql = String.format("SELECT ?x WHERE { ?x <%sisFounderOf> <%sMicrosoft> }", prefix, prefix);
    } else if (question.equalsIgnoreCase("HOW MANY PEOPLE FOUNDED MICROSOFT?")) {
      sparql = String.format("SELECT (COUNT(DISTINCT ?people) AS ?x) WHERE { ?people <%sisFounderOf> <%sMicrosoft> }", prefix, prefix);
    } else if (question.equalsIgnoreCase("WHO IS THE CHIEF EXECUTIVE OFFICER OF APPLE?")) {
      sparql = String.format("SELECT ?x WHERE { ?x <%sisCEOOf> <%sApple> }", prefix, prefix);
    } else if (question.equalsIgnoreCase("WHO IS THE CEO OF APPLE?")) {
      sparql = String.format("SELECT ?x WHERE { ?x <%sisCEOOf> <%sApple> }", prefix, prefix);
    } else if (question.equalsIgnoreCase("WHAT IS THE NAME OF THE CEO OF APPLE?")) {
      sparql = String.format("SELECT ?x WHERE { ?x <%sisCEOOf> <%sApple> }", prefix, prefix);
    } else if (question.equalsIgnoreCase("WHAT IS THE CHIEF EXECUTIVE OFFICER OF APPLE?")) {
      sparql = String.format("SELECT ?x WHERE { ?x <%sisCEOOf> <%sApple> }", prefix, prefix);
    } else if (question.equalsIgnoreCase("WHO IS THE CHIEF FINANCIAL OFFICER OF APPLE?")) {
      sparql = String.format("SELECT ?x WHERE { ?x <%sisCFOOf> <%sApple> }", prefix, prefix);
    } else if (question.equalsIgnoreCase("WHO ARE THE CORPORATE OFFICERS OF APPLE?")) {
      sparql = String.format("SELECT ?x WHERE { ?x <%sisCorporateOfficerOf> <%sApple> }", prefix, prefix);
    } else if (question.equalsIgnoreCase("WHO IS THE CHAIRMAN OF APPLE?")) {
      sparql = String.format("SELECT ?x WHERE { ?x <%sisChairmanOf> <%sApple> }", prefix, prefix);
    } else if (question.equalsIgnoreCase("WHO IS THE PRESIDENT OF GOOGLE?")) {
      sparql = String.format("SELECT ?x WHERE { ?x <%sisChairmanOf> <%sGoogle> }", prefix, prefix);
    } else if (question.equalsIgnoreCase("WHAT IS THE NET INCOME OF MICROSOFT?")) {
      sparql = String.format("SELECT ?x WHERE { <%sMicrosoft> <%snetIncome> ?x }", prefix, prefix);
    } else if (question.equalsIgnoreCase("IS SATYA NADELLA THE CEO OF MICROSOFT?")) {
      sparql = String.format("ASK WHERE { <%sSatya_Nadella> <%sisCEOOf> <%sMicrosoft> }", prefix, prefix, prefix);
    } else if (question.equalsIgnoreCase("IS SATYA NADELLA ITALIAN?")) {
      sparql = String.format("ASK WHERE { <%sSatya_Nadella> <%snationality> <%sItaly> }", prefix, prefix, prefix);
    } else if (question.equalsIgnoreCase("DID MICROSOFT ACQUIRE A COMPANY HEADQUARTERED IN ITALY?")) {
      sparql = String.format("ASK { <%sMicrosoft> <%sacquireCompany> ?company . ?company <%sisHeadquartered> <%sItaly> }",
          prefix, prefix, prefix, prefix);
    } else if (question.equalsIgnoreCase("DID MICROSOFT ACQUIRE AN ITALIAN COMPANY?")) {
      sparql = String.format("ASK { <%sMicrosoft> <%sacquireCompany> ?company . ?company <%sisHeadquartered> <%sItaly> }",
          prefix, prefix, prefix, prefix);
    } else if (question.equalsIgnoreCase("WHERE IS MICROSOFT HEADQUARTERED?")) {
      sparql = String.format("SELECT ?x WHERE{ <%sMicrosoft> <%sisHeadquartered> ?x }", prefix, prefix);
    } else if (question.equalsIgnoreCase("WHAT IS THE MOST VALUABLE COMPANY?")) {
      sparql = String.format("SELECT ?x ?value WHERE { ?x a <%sCompany> . ?x <%scompanyValue> ?value } ORDER BY DESC(?value) LIMIT 1",
          prefix, prefix);
    } else {
      return null;
    }

    QueryResult result = KnowledgeManager.submit(sparql, SessionManager.getOntology(), "x");

    return LOGGER.traceExit(result);
  }

}
