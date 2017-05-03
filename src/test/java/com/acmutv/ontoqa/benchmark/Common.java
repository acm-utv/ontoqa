/*
  The MIT License (MIT)

  Copyright (c) 2017 Antonella Botte, Giacomo Marciani and Debora Partigianoni

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

import com.acmutv.ontoqa.core.exception.*;
import com.acmutv.ontoqa.core.exception.QueryException;
import com.acmutv.ontoqa.core.grammar.Grammar;
import com.acmutv.ontoqa.core.grammar.GrammarFormat;
import com.acmutv.ontoqa.core.knowledge.KnowledgeManager;
import com.acmutv.ontoqa.core.knowledge.answer.Answer;
import com.acmutv.ontoqa.core.knowledge.ontology.Ontology;
import com.acmutv.ontoqa.core.knowledge.ontology.OntologyFormat;
import com.acmutv.ontoqa.session.SessionManager;
import org.apache.jena.query.*;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Common utilities for Benchamrk tests.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class Common {

  private static final Logger LOGGER = LoggerFactory.getLogger(Common.class);

  public static final String PREFIX = "http://www.semanticweb.org/organization#";

  public static final String ONTOLOGY_PATH = Common.class.getResource("/knowledge/organization.ttl").getPath();

  public static final OntologyFormat ONTOLOGY_FORMAT = OntologyFormat.TURTLE;

  public static final String GRAMMAR_PATH = Common.class.getResource("/grammar/sltag.json").getPath();

  public static final GrammarFormat GRAMMAR_FORMAT = GrammarFormat.YAML;

  public static final String FORMAT = "TURTLE";

  /* classes */
  public static final String COMPANY_IRI = String.format("%sCompany", PREFIX);

  public static final String NATION_IRI = String.format("%sNation", PREFIX);

  public static final String PERSON_IRI = String.format("%sPerson", PREFIX);

  /* companies */

  public static final String APPLE_IRI = String.format("%sApple", PREFIX);

  public static final String GOOGLE_IRI = String.format("%sGoogle", PREFIX);

  public static final String LINKEDIN_IRI = String.format("%sLinkedIn", PREFIX);

  public static final String MICROSOFT_IRI = String.format("%sMicrosoft", PREFIX);

  /* states */

  public static final String ITALY_IRI = String.format("%sItaly", PREFIX);

  public static final String UNITED_STATES_IRI = String.format("%sUnited_States", PREFIX);

  /* people */

  public static final String ARTHUR_LEVINSON_IRI = String.format("%sArthur_Levinson", PREFIX);

  public static final String BILL_GATES_IRI = String.format("%sBill_Gates", PREFIX);

  public static final String ERIC_SCHMIDT_IRI = String.format("%sEric_Schmidt", PREFIX);

  public static final String LUCA_MAESTRI_IRI = String.format("%sLuca_Maestri", PREFIX);

  public static final String PAUL_ALLEN_IRI = String.format("%sPaul_Allen", PREFIX);

  public static final String SATYA_NADELLA_IRI = String.format("%sSatya_Nadella", PREFIX);

  public static final String TIM_COOK_IRI = String.format("%sTim_Cook", PREFIX);

  /* predicates */

  public static final String ACQUIRE_IRI = String.format("%sacquire", PREFIX);

  public static final String HAS_NATIONALITY_IRI = String.format("%shasNationality", PREFIX);

  public static final String HAS_NETINCOME_IRI = String.format("%snetIncome", PREFIX);

  public static final String HAS_COMPANY_VALUE_IRI = String.format("%scompanyValue", PREFIX);

  public static final String IS_CEO_OF_IRI = String.format("%sisCEOOf", PREFIX);

  public static final String IS_CFO_OF_IRI = String.format("%sisCFOOf", PREFIX);

  public static final String IS_CHAIRMAN_OF_IRI = String.format("%sisChairmanOf", PREFIX);

  public static final String IS_CORPORATE_OFFICER_OF_IRI = String.format("%sisCorporateOfficerOf", PREFIX);

  public static final String IS_FOUNDER_OF_IRI = String.format("%sisFounderOf", PREFIX);

  public static final String HAS_HEADQUARTER_IRI = String.format("%shasHeadquarter", PREFIX);

  public static final String RDF_TYPE_IRI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
  
  public static final String HAS_NATION_IRI = String.format("%shasNation", PREFIX);

  /**
   * Test the assertion on ontology answers.
   * @param query the SPARQL query.
   * @param expected the expected answer.
   */
  public static void test_query(Query query, Answer expected) throws IOException, QueryException {
    Ontology ontology = KnowledgeManager.read(ONTOLOGY_PATH, PREFIX, ONTOLOGY_FORMAT);
    Answer actual = KnowledgeManager.submit(ontology, query).toAnswer();
    Assert.assertEquals(expected, actual);
  }

  /**
   * Loads session for all the benchmark JUnit tests.
   * @throws OntoqaFatalException when grammar cannot be loaded.
   */
  public static synchronized void loadGrammar() throws OntoqaFatalException {
    if (SessionManager.getGrammar() == null) {
      try {
        SessionManager.loadGrammar(GRAMMAR_PATH, GRAMMAR_FORMAT);
      } catch (IOException exc) {
        throw new OntoqaFatalException("Cannot load grammar in %s format from %s",
            GRAMMAR_PATH, GRAMMAR_FORMAT);
      }
    }
  }

  /**
   * Returns the loaded grammar.
   * @return the loaded grammar.
   */
  public static Grammar getGrammar() {
    Grammar grammar = SessionManager.getGrammar();
    if (grammar == null) {
      try {
        loadGrammar();
        grammar = SessionManager.getGrammar();
      } catch (OntoqaFatalException exc) {
        LOGGER.error(exc.getMessage());
        return null;
      }
    }
    return grammar;
  }

  /**
   * Loads session for all the benchmark JUnit tests.
   * @throws OntoqaFatalException when ontology or grammar cannot be loaded.
   */
  public static synchronized void loadOntology() throws OntoqaFatalException {
    if (SessionManager.getOntology() == null) {
      try {
        SessionManager.loadOntology(ONTOLOGY_PATH, ONTOLOGY_FORMAT);
      } catch (IOException exc) {
        throw new OntoqaFatalException("Cannot load ontology in %s format from %s",
            ONTOLOGY_FORMAT, ONTOLOGY_PATH);
      }
    }
  }

  /**
   * Returns the loaded ontology.
   * @return the loaded ontology.
   */
  public static Ontology getOntology() {
    Ontology ontology = SessionManager.getOntology();
    if (ontology == null) {
      try {
        loadOntology();
        ontology = SessionManager.getOntology();
      } catch (OntoqaFatalException exc) {
        LOGGER.error(exc.getMessage());
        return null;
      }
    }
    return ontology;
  }

}
