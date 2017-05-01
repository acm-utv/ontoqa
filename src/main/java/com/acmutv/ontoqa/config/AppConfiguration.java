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

package com.acmutv.ontoqa.config;

import com.acmutv.ontoqa.core.grammar.GrammarFormat;
import com.acmutv.ontoqa.core.knowledge.ontology.OntologyFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

/**
 * The app configuration model.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Yaml
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(AppConfiguration.class);

  private static final String ONTOLOGY_PATH = "data/knowledge/sample.ontology.ttl";
  private static final OntologyFormat ONTOLOGY_FORMAT = OntologyFormat.TURTLE;

  private static final String GRAMMAR_PATH = "data/grammar/sample.grammar.json";
  private static final GrammarFormat GRAMMAR_FORMAT = GrammarFormat.JSON;

  private String ontologyPath = ONTOLOGY_PATH;
  private OntologyFormat ontologyFormat = ONTOLOGY_FORMAT;
  private String grammarPath = GRAMMAR_PATH;
  private GrammarFormat grammarFormat = GRAMMAR_FORMAT;

  /**
   * Constructs a configuration as a copy of the one specified.
   * @param other the configuration to copy.
   */
  public AppConfiguration(AppConfiguration other) {
    this.copy(other);
  }

  /**
   * Copies the settings of the configuration specified.
   * @param other the configuration to copy.
   */
  public void copy(AppConfiguration other) {
    this.ontologyPath = other.ontologyPath;
    this.ontologyFormat = other.ontologyFormat;
    this.grammarPath = other.grammarPath;
    this.grammarFormat = other.grammarFormat;
  }

  /**
   * Restores the default configuration settings.
   */
  public void toDefault() {
    this.ontologyPath = ONTOLOGY_PATH;
    this.ontologyFormat = ONTOLOGY_FORMAT;
    this.grammarPath = GRAMMAR_PATH;
    this.grammarFormat = GRAMMAR_FORMAT;
  }

}
