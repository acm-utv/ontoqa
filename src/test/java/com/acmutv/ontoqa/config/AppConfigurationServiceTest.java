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

import com.acmutv.ontoqa.core.knowledge.ontology.OntologyFormat;
import com.acmutv.ontoqa.core.lexicon.LexiconFormat;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class realizes JUnit tests for {@link AppConfigurationService}.
 * and {@link AppConfiguration}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see AppConfigurationService
 */
public class AppConfigurationServiceTest {

  /**
   * Tests the restoring of default settings in app configuration.
   */
  @Test
  public void test_fromDefault() {
    AppConfiguration actual = AppConfigurationService.fromDefault();
    AppConfiguration expected = new AppConfiguration();
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the app configuration parsing from an external JSON/YAML file.
   * In this test the configuration file provides with complete custom settings.
   */
  @Test
  public void test_fromJsonYaml_custom() throws IOException {
    InputStream injson = AppConfigurationServiceTest.class.getResourceAsStream("/config/custom.json");
    InputStream inyaml = AppConfigurationServiceTest.class.getResourceAsStream("/config/custom.yaml");
    AppConfiguration actualjson = AppConfigurationService.fromJson(injson);
    AppConfiguration actualyaml = AppConfigurationService.fromYaml(inyaml);
    AppConfiguration expected = new AppConfiguration();
    expected.setOntologyFormat(OntologyFormat.TURTLE);
    expected.setLexiconFormat(LexiconFormat.RDFXML);
    expected.setOntologyPath(AppConfigurationServiceTest.class.getResource("/knowledge/sample2.ttl").getPath());
    expected.setLexiconPath(AppConfigurationServiceTest.class.getResource("/lexicon/sample2.lexicon.rdf").getPath());
    Assert.assertEquals(expected, actualjson);
    Assert.assertEquals(expected, actualyaml);
  }

  /**
   * Tests the app configuration parsing from an external JSON/YAML file.
   * In this test the configuration file provides with complete default settings.
   */
  @Test
  public void test_fromJsonYaml_default() throws IOException {
    InputStream injson = AppConfigurationServiceTest.class.getResourceAsStream("/config/default.json");
    InputStream inyaml = AppConfigurationServiceTest.class.getResourceAsStream("/config/default.yaml");
    AppConfiguration actualjson = AppConfigurationService.fromJson(injson);
    AppConfiguration actualyaml = AppConfigurationService.fromYaml(inyaml);
    AppConfiguration expected = new AppConfiguration();
    Assert.assertEquals(expected, actualjson);
    Assert.assertEquals(expected, actualyaml);
  }

  /**
   * Tests the configuration parsing from an external JSON/YAML file.
   * In this test the configuration file provides with empty settings.
   */
  @Test
  public void test_fromJsonYaml_empty() {
    InputStream injson = AppConfigurationServiceTest.class.getResourceAsStream("/config/empty.json");
    InputStream inyaml = AppConfigurationServiceTest.class.getResourceAsStream("/config/empty.yaml");
    try {
      AppConfiguration actualjson = AppConfigurationService.fromJson(injson);
      AppConfiguration actualyaml = AppConfigurationService.fromYaml(inyaml);
    } catch (IOException exc) { return; }
    Assert.fail();
  }

  /**
   * Tests the configuration parsing from an external JSON/YAML configuration file.
   * In this test the configuration file provides with partial custom settings.
   */
  @Test
  public void test_fromJsonYaml_partialCustom() throws IOException {
    InputStream injson = AppConfigurationServiceTest.class.getResourceAsStream("/config/partial.json");
    InputStream inyaml = AppConfigurationServiceTest.class.getResourceAsStream("/config/partial.yaml");
    AppConfiguration actualjson = AppConfigurationService.fromJson(injson);
    AppConfiguration actualyaml = AppConfigurationService.fromYaml(inyaml);
    AppConfiguration expected = new AppConfiguration();
    expected.setOntologyFormat(OntologyFormat.RDFXML);
    Assert.assertEquals(expected, actualjson);
    Assert.assertEquals(expected, actualyaml);
  }
}