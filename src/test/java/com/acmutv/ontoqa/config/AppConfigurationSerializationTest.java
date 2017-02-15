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

import com.acmutv.ontoqa.config.serial.AppConfigurationJsonMapper;
import com.acmutv.ontoqa.config.serial.AppConfigurationYamlMapper;
import com.acmutv.ontoqa.core.grammar.GrammarFormat;
import com.acmutv.ontoqa.core.knowledge.ontology.OntologyFormat;
import com.acmutv.ontoqa.core.lexicon.LexiconFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * JUnit tests for {@link AppConfiguration} serialization.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see AppConfiguration
 * @see AppConfigurationJsonMapper
 * @see AppConfigurationYamlMapper
 */
public class AppConfigurationSerializationTest {

  /**
   * Tests {@link AppConfiguration} serialization/deserialization.
   * LtagNodeType: default
   * @throws IOException when configuration cannot be serialized/deserialized.
   */
  @Test
  public void test_default() throws IOException {
    AppConfiguration configExpected = new AppConfiguration();
    ObjectMapper mapperJson = new AppConfigurationJsonMapper();
    ObjectMapper mapperYaml = new AppConfigurationYamlMapper();
    String jsonActual = mapperJson.writeValueAsString(configExpected);
    String yamlActual = mapperYaml.writeValueAsString(configExpected);
    AppConfiguration configJsonActual = mapperJson.readValue(jsonActual, AppConfiguration.class);
    AppConfiguration configYamlActual = mapperYaml.readValue(yamlActual, AppConfiguration.class);
    Assert.assertEquals(configExpected, configJsonActual);
    Assert.assertEquals(configExpected, configYamlActual);
  }

  /**
   * Tests {@link AppConfiguration} serialization/deserialization.
   * LtagNodeType: custom
   * @throws IOException when configuration cannot be serialized/deserialized.
   */
  @Test
  public void test_custom() throws IOException {
    AppConfiguration configExpected = new AppConfiguration();
    configExpected.setOntologyPath("data/ontology/sample.ontology.ttl");
    configExpected.setOntologyFormat(OntologyFormat.TURTLE);
    configExpected.setGrammarPath("data/grammar/sample.grammar.json");
    configExpected.setGrammarFormat(GrammarFormat.YAML);
    ObjectMapper mapperJson = new AppConfigurationJsonMapper();
    ObjectMapper mapperYaml = new AppConfigurationYamlMapper();
    String jsonActual = mapperJson.writeValueAsString(configExpected);
    String yamlActual = mapperYaml.writeValueAsString(configExpected);
    AppConfiguration configJsonActual = mapperJson.readValue(jsonActual, AppConfiguration.class);
    AppConfiguration configYamlActual = mapperYaml.readValue(yamlActual, AppConfiguration.class);
    Assert.assertEquals(configExpected, configJsonActual);
    Assert.assertEquals(configExpected, configYamlActual);
  }

}
