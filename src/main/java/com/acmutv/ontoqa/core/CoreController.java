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

import com.acmutv.ontoqa.config.AppConfiguration;
import com.acmutv.ontoqa.config.AppConfigurationService;
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
import com.acmutv.ontoqa.core.syntax.SyntaxTree;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.rio.RDFFormat;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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

  public static String process(final String question) throws IOException {
    LOGGER.traceEntry("question={}", question);
    String ontologyPath = AppConfigurationService.getConfigurations().getOntologyPath();
    String lexiconPath = AppConfigurationService.getConfigurations().getLexiconPath();
    OntologyFormat ontologyFormat = AppConfigurationService.getConfigurations().getOntologyFormat();
    LexiconFormat lexiconFormat = AppConfigurationService.getConfigurations().getLexiconFormat();

    Ontology ontology = KnowledgeManager.readOntology(
            new FileInputStream(ontologyPath), "http://example.org/", ontologyFormat);
    Lexicon lexicon = LexiconManager.readLexicon(
        new FileInputStream(lexiconPath), "http://example.org/", lexiconFormat);
    SyntaxTree syntaxTree = SyntaxManager.getSyntaxTree(question, ontology, lexicon);
    Dudes dudes = SemanticsManager.getDudes(syntaxTree, ontology, lexicon);
    Query query = SemanticsManager.getQuery(dudes);
    QueryResult qQueryResult = KnowledgeManager.submit(query, ontology);
    String answer = qQueryResult.toString();
    return LOGGER.traceExit(answer);
  }
}
