/*
  The MIT License (MIT)

  Copyright (c) 2016 Giacomo Marciani and Michele Porretta

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

package com.acmutv.ontoqa.config.json;

import com.acmutv.ontoqa.config.AppConfiguration;
import com.acmutv.ontoqa.core.knowledge.ontology.OntologyFormat;
import com.acmutv.ontoqa.core.lexicon.LexiconFormat;
import com.acmutv.ontoqa.tool.string.TemplateEngine;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.hp.hpl.jena.sparql.syntax.Template;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * This class realizes the JSON deserializer for {@link AppConfiguration}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see AppConfiguration
 */
public class AppConfigurationDeserializer extends StdDeserializer<AppConfiguration> {

  private static final Logger LOGGER = LogManager.getLogger(AppConfigurationDeserializer.class);

  /**
   * The singleton of {@link AppConfigurationDeserializer}.
   */
  private static AppConfigurationDeserializer instance;

  /**
   * Returns the singleton of {@link AppConfigurationDeserializer}.
   * @return the singleton.
   */
  public static AppConfigurationDeserializer getInstance() {
    if (instance == null) {
      instance = new AppConfigurationDeserializer();
    }
    return instance;
  }

  /**
   * Initializes the singleton of {@link AppConfigurationDeserializer}.
   */
  private AppConfigurationDeserializer() {
    super((Class<?>) null);
  }

  @Override
  public AppConfiguration deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
    LOGGER.traceEntry();
    AppConfiguration config = new AppConfiguration();
    JsonNode node = parser.getCodec().readTree(parser);
    LOGGER.trace("node={}", node);

    if (node.has("ontologyPath")) {
      final String ontologyPath = TemplateEngine.getInstance().replace(node.get("ontologyPath").asText());
      config.setOntologyPath(ontologyPath);
    }

    if (node.has("ontologyFormat")) {
      final OntologyFormat ontologyFormat = OntologyFormat.valueOf(node.get("ontologyFormat").asText());
      config.setOntologyFormat(ontologyFormat);
    }

    if (node.has("lexiconPath")) {
      final String lexiconPath = TemplateEngine.getInstance().replace(node.get("lexiconPath").asText());
      config.setLexiconPath(lexiconPath);
    }

    if (node.has("lexiconFormat")) {
      final LexiconFormat lexiconFormat = LexiconFormat.valueOf(node.get("lexiconFormat").asText());
      config.setLexiconFormat(lexiconFormat);
    }

    return LOGGER.traceExit(config);
  }
}