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

  @Test
  public void test_fromDefault() {
    final AppConfiguration actual = AppConfigurationService.fromDefault();
    final AppConfiguration expected = new AppConfiguration();
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the configuration parsing from an external YAML file.
   * In this test the configuration file provides with empty settings.
   */
  @Test
  public void test_fromYaml_empty() {
    InputStream in = AppConfigurationServiceTest.class.getResourceAsStream("/config/empty.yml");
    AppConfiguration actual = AppConfigurationService.fromYaml(in);
    AppConfiguration expected = new AppConfiguration();
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the app configuration parsing from an external YAML file.
   * In this test the configuration file provides with complete default settings.
   */
  @Test
  public void test_fromYaml_default() {
    InputStream in = AppConfigurationServiceTest.class.getResourceAsStream("/config/default.yml");
    AppConfiguration actual = AppConfigurationService.fromYaml(in);
    AppConfiguration expected = new AppConfiguration();
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the app configuration parsing from an external YAML file.
   * In this test the configuration file provides with complete custom settings.
   * The configuration file has non-null values and template string (${RES}).
   */
  @Test
  public void test_fromYaml_custom() {
    InputStream in = AppConfigurationServiceTest.class.getResourceAsStream("/config/custom.yml");
    AppConfiguration actual = AppConfigurationService.fromYaml(in);
    AppConfiguration expected = new AppConfiguration();
    expected.setOntologyFormat(OntologyFormat.TURTLE);
    expected.setLexiconFormat(LexiconFormat.RDFXML);
    expected.setOntologyPath(AppConfigurationServiceTest.class.getResource("/knowledge/sample2.ttl").getPath());
    expected.setLexiconPath(AppConfigurationServiceTest.class.getResource("/lexicon/sample2.lexicon.rdf").getPath());
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the configuration parsing from an external YAML configuration file.
   * In this test the configuration file provides with partial custom settings.
   */
  @Test
  public void test_fromYaml_partialCustom() {
    InputStream in = AppConfigurationServiceTest.class.getResourceAsStream("/config/partial.yml");
    AppConfiguration actual = AppConfigurationService.fromYaml(in);
    AppConfiguration expected = new AppConfiguration();
    expected.setOntologyFormat(OntologyFormat.RDFXML);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the configuration parsing from an external JSON file.
   * In this test the configuration file provides with empty settings.
   */
  @Test
  public void test_fromJson_empty() {
    InputStream in = AppConfigurationServiceTest.class.getResourceAsStream("/config/empty.json");
    AppConfiguration actual = AppConfigurationService.fromJson(in);
    AppConfiguration expected = new AppConfiguration();
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the app configuration parsing from an external JSON file.
   * In this test the configuration file provides with complete default settings.
   */
  @Test
  public void test_fromJson_default() {
    InputStream in = AppConfigurationServiceTest.class.getResourceAsStream("/config/default.json");
    AppConfiguration actual = AppConfigurationService.fromJson(in);
    AppConfiguration expected = new AppConfiguration();
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the app configuration parsing from an external JSON file.
   * In this test the configuration file provides with complete custom settings.
   */
  @Test
  public void test_fromJson_custom() {
    InputStream in = AppConfigurationServiceTest.class.getResourceAsStream("/config/custom.json");
    AppConfiguration actual = AppConfigurationService.fromJson(in);
    AppConfiguration expected = new AppConfiguration();
    expected.setOntologyFormat(OntologyFormat.TURTLE);
    expected.setLexiconFormat(LexiconFormat.RDFXML);
    expected.setOntologyPath(AppConfigurationServiceTest.class.getResource("/knowledge/sample2.ttl").getPath());
    expected.setLexiconPath(AppConfigurationServiceTest.class.getResource("/lexicon/sample2.lexicon.rdf").getPath());
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the configuration parsing from an external JSON configuration file.
   * In this test the configuration file provides with partial custom settings.
   */
  @Test
  public void test_fromJson_partialCustom() {
    InputStream in = AppConfigurationServiceTest.class.getResourceAsStream("/config/partial.json");
    AppConfiguration actual = AppConfigurationService.fromJson(in);
    AppConfiguration expected = new AppConfiguration();
    expected.setOntologyFormat(OntologyFormat.RDFXML);
    Assert.assertEquals(expected, actual);
  }
}