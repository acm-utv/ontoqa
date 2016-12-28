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

import com.acmutv.ontoqa.config.AppConfigurationService;
import com.acmutv.ontoqa.core.exception.SyntaxProcessingException;
import com.acmutv.ontoqa.core.knowledge.Answer;
import com.acmutv.ontoqa.core.knowledge.ontology.OntologyFormat;
import com.acmutv.ontoqa.core.knowledge.query.Query;
import com.acmutv.ontoqa.core.knowledge.query.QueryResult;
import com.acmutv.ontoqa.core.lexicon.Lexicon;
import com.acmutv.ontoqa.core.lexicon.LexiconFormat;
import com.acmutv.ontoqa.core.lexicon.LexiconManager;
import com.acmutv.ontoqa.core.semantics.Dudes;
import com.acmutv.ontoqa.core.knowledge.ontology.Ontology;
import com.acmutv.ontoqa.core.knowledge.KnowledgeManager;
import com.acmutv.ontoqa.core.semantics.SemanticsManager;
import com.acmutv.ontoqa.core.syntax.SyntaxManager;
import com.acmutv.ontoqa.core.syntax.ltag.LTAG;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * This class realizes the core business logic.
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
   * @throws IOException when the ontolgy and/or lexicon file(s) cannot be processed.
   */
  public static Answer process(final String question) throws IOException, SyntaxProcessingException {
    LOGGER.traceEntry("question={}", question);
    Ontology ontology = readOntology();
    Lexicon lexicon = readLexicon();
    LTAG syntaxTree = SyntaxManager.getSyntaxTree(question, ontology, lexicon);
    Dudes dudes = SemanticsManager.getDudes(syntaxTree, ontology, lexicon);
    Query query = SemanticsManager.getQuery(dudes);
    QueryResult qQueryResult = KnowledgeManager.submit(query, ontology);
    Answer answer = qQueryResult.toAnswer();
    return LOGGER.traceExit(answer);
  }

  private static Ontology readOntology() throws IOException {
    String ontologyPath = AppConfigurationService.getConfigurations().getOntologyPath();
    OntologyFormat ontologyFormat = AppConfigurationService.getConfigurations().getOntologyFormat();
    Ontology ontology = KnowledgeManager.read(ontologyPath, "http://example.org/", ontologyFormat);
    return ontology;
  }

  private static Lexicon readLexicon() throws IOException {
    String lexiconPath = AppConfigurationService.getConfigurations().getLexiconPath();
    LexiconFormat lexiconFormat = AppConfigurationService.getConfigurations().getLexiconFormat();
    Lexicon lexicon = LexiconManager.read(lexiconPath, "http://example.org/", lexiconFormat);
    return lexicon;
  }
}
