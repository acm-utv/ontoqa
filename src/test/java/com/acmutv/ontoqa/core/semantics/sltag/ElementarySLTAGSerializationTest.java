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

package com.acmutv.ontoqa.core.semantics.sltag;

import com.acmutv.ontoqa.core.semantics.base.statement.OperatorType;
import com.acmutv.ontoqa.core.semantics.dudes.Dudes;
import com.acmutv.ontoqa.core.semantics.dudes.DudesTemplates;
import com.acmutv.ontoqa.core.semantics.sltag.serial.ElementarySltagJsonMapper;
import com.acmutv.ontoqa.core.semantics.sltag.serial.ElementarySltagYamlMapper;
import com.acmutv.ontoqa.core.syntax.ltag.Ltag;
import com.acmutv.ontoqa.core.syntax.ltag.LtagTemplates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * JUnit tests for {@link ElementarySltag} serialization.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see ElementarySltag
 * @see ElementarySltagJsonMapper
 */
public class ElementarySLTAGSerializationTest {

  private static final Logger LOGGER = LogManager.getLogger(ElementarySLTAGSerializationTest.class);

  /**
   * Asserts the serialization correctness.
   * @param expected the expected value.
   * @throws IOException when value cannot be serialized or deserialized.
   */
  private void testSerialization(ElementarySltag expected) throws IOException {
    ElementarySltagJsonMapper jsonMapper = new ElementarySltagJsonMapper();
    String json = jsonMapper.writeValueAsString(expected);
    LOGGER.debug("Elementary SLTAG JSON serialization: \n{}", json);
    ElementarySltag actualJson = jsonMapper.readValue(json, ElementarySltag.class);
    Assert.assertEquals(expected, actualJson);

    ElementarySltagYamlMapper yamlMapper = new ElementarySltagYamlMapper();
    String yaml = yamlMapper.writeValueAsString(expected);
    LOGGER.debug("Elementary SLTAG YAML serialization: \n{}", yaml);
    ElementarySltag actualYaml = yamlMapper.readValue(yaml, ElementarySltag.class);
    Assert.assertEquals(expected, actualYaml);
  }

  /**
   * Tests {@link ElementarySltag} serialization/deserialization.
   * Proper noun.
   * @throws IOException when the ElementarySltag cannot be serialized/deserialized.
   */
  @Test
  public void test_properNoun() throws IOException {
    /* Word */
    String word = "Uruguay";

    /* LTAG */
    Ltag ltag = LtagTemplates.properNoun("Uruguay");

    /* DUDES */
    Dudes dudes = DudesTemplates.properNoun("http://dbpedia.org/resource/Uruguay");

    /* ElementarySltag */
    ElementarySltag expected = new SimpleElementarySltag(word, ltag, dudes);

    testSerialization(expected);
  }

  /**
   * Tests {@link ElementarySltag} serialization/deserialization.
   * Class noun (specific).
   * @throws IOException when ElementarySltag cannot be serialized/deserialized.
   */
  @Test
  public void test_classNoun_specific() throws IOException {
    /* Word */
    String word = "team";

    /* LTAG */
    Ltag ltag = LtagTemplates.classNoun("team", false);

    /* DUDES */
    Dudes dudes = DudesTemplates.classNoun("http://dbpedia.org/resource/Team", false);

    /* ElementarySltag */
    ElementarySltag expected = new SimpleElementarySltag(word, ltag, dudes);

    testSerialization(expected);
  }

  /**
   * Tests {@link ElementarySltag} serialization/deserialization.
   * Class noun (generic).
   * @throws IOException when ElementarySltag cannot be serialized/deserialized.
   */
  @Test
  public void test_classNoun_generic() throws IOException {
    /* Word */
    String word = "teams";

    /* LTAG */
    Ltag ltag = LtagTemplates.classNoun("teams", true);

    /* DUDES */
    Dudes dudes = DudesTemplates.classNoun("http://dbpedia.org/resource/Team", false);

    /* ElementarySltag */
    ElementarySltag expected = new SimpleElementarySltag(word, ltag, dudes);

    testSerialization(expected);
  }

  /**
   * Tests {@link ElementarySltag} serialization/deserialization.
   * Relational prepositional noun (specific).
   * @throws IOException when ElementarySltag cannot be serialized/deserialized.
   */
  @Test
  public void test_relationalPrepositionalNoun_specific() throws IOException {
    /* Word */
    String word = "capacity";

    /* LTAG */
    Ltag ltag = LtagTemplates.relationalPrepositionalNoun("capacity", "of", "DP2", false);

    /* DUDES */
    Dudes dudes = DudesTemplates.relationalNoun("http://dbpedia.org/resource/capacity", "DP2", false);

    /* ElementarySltag */
    ElementarySltag expected = new SimpleElementarySltag(word, ltag, dudes);

    testSerialization(expected);
  }

  /**
   * Tests {@link ElementarySltag} serialization/deserialization.
   * Relational prepositional noun (specific).
   * @throws IOException when ElementarySltag cannot be serialized/deserialized.
   */
  @Test
  public void test_relationalPrepositionalNoun_generic() throws IOException {
    /* Word */
    String word = "capacity";

    /* LTAG */
    Ltag ltag = LtagTemplates.relationalPrepositionalNoun("capacity", "of", "DP2", true);

    /* DUDES */
    Dudes dudes = DudesTemplates.relationalNoun("http://dbpedia.org/resource/capacity", "DP2", true);

    /* ElementarySltag */
    ElementarySltag expected = new SimpleElementarySltag(word, ltag, dudes);

    testSerialization(expected);
  }

  /**
   * Tests {@link ElementarySltag} serialization/deserialization.
   * Relational possessive noun (specific).
   * @throws IOException when ElementarySltag cannot be serialized/deserialized.
   */
  @Test
  public void test_relationalPossessiveNoun_specific() throws IOException {
    /* Word */
    String word = "capacity";

    /* LTAG */
    Ltag ltag = LtagTemplates.relationalPossessiveNoun("capacity", "'s", "DP2", false);

    /* DUDES */
    Dudes dudes = DudesTemplates.relationalNoun("http://dbpedia.org/resource/capacity", "DP2", false);

    /* ElementarySltag */
    ElementarySltag expected = new SimpleElementarySltag(word, ltag, dudes);

    testSerialization(expected);
  }

  /**
   * Tests {@link ElementarySltag} serialization/deserialization.
   * Relational possessive noun (specific).
   * @throws IOException when ElementarySltag cannot be serialized/deserialized.
   */
  @Test
  public void test_relationalPossessiveNoun_generic() throws IOException {
    /* Word */
    String word = "capacity";

    /* LTAG */
    Ltag ltag = LtagTemplates.relationalPossessiveNoun("capacity", "'s", "DP2", true);

    /* DUDES */
    Dudes dudes = DudesTemplates.relationalNoun("http://dbpedia.org/resource/capacity", "DP2", true);

    /* ElementarySltag */
    ElementarySltag expected = new SimpleElementarySltag(word, ltag, dudes);

    testSerialization(expected);
  }

  /**
   * Tests {@link ElementarySltag} serialization/deserialization.
   * Intransitive verb.
   * @throws IOException when ElementarySltag cannot be serialized/deserialized.
   */
  @Test
  public void test_intransitiveVerb() throws IOException {
    /* Word */
    String word = "win";

    /* LTAG */
    Ltag ltag = LtagTemplates.intransitiveVerb("win", "DP1");

    /* DUDES */
    Dudes dudes = DudesTemplates.intransitiveVerb("http://dbpedia.org/resource/winner", "DP1");

    /* ElementarySltag */
    ElementarySltag expected = new SimpleElementarySltag(word, ltag, dudes);

    testSerialization(expected);
  }

  /**
   * Tests {@link ElementarySltag} serialization/deserialization.
   * Intransitive verb (classing).
   * @throws IOException when ElementarySltag cannot be serialized/deserialized.
   */
  @Test
  public void test_intransitiveVerb_classing() throws IOException {
    /* Word */
    String word = "win";

    /* LTAG */
    Ltag ltag = LtagTemplates.intransitiveVerb("win", "DP1");

    /* DUDES */
    Dudes dudes = DudesTemplates.intransitiveVerbClassing("http://dbpedia.org/resource/winner", "DP1");

    /* ElementarySltag */
    ElementarySltag expected = new SimpleElementarySltag(word, ltag, dudes);

    testSerialization(expected);
  }

  /**
   * Tests {@link ElementarySltag} serialization/deserialization.
   * Transitive verb (active indicative).
   * @throws IOException when ElementarySltag cannot be serialized/deserialized.
   */
  @Test
  public void test_transitiveVerb_activeIndicative() throws IOException {
    /* Word */
    String word = "respects";

    /* LTAG */
    Ltag ltag = LtagTemplates.transitiveVerbActiveIndicative("respects", "DP1", "DP2");

    /* DUDES */
    Dudes dudes = DudesTemplates.transitiveVerb("http://dbpedia.org/resource/respect", "DP1", "DP2");

    /* ElementarySltag */
    ElementarySltag expected = new SimpleElementarySltag(word, ltag, dudes);

    testSerialization(expected);
  }

  /**
   * Tests {@link ElementarySltag} serialization/deserialization.
   * Transitive verb (passive indicative).
   * @throws IOException when ElementarySltag cannot be serialized/deserialized.
   */
  @Test
  public void test_transitiveVerb_passiveIndicative() throws IOException {
    /* Word */
    String word = "respected";

    /* LTAG */
    Ltag ltag = LtagTemplates.transitiveVerbPassiveIndicative("respected", "is", "by", "DP2", "DP1");

    /* DUDES */
    Dudes dudes = DudesTemplates.transitiveVerb("http://dbpedia.org/resource/respect", "DP1", "DP2");

    /* ElementarySltag */
    ElementarySltag expected = new SimpleElementarySltag(word, ltag, dudes);

    testSerialization(expected);
  }

  /**
   * Tests {@link ElementarySltag} serialization/deserialization.
   * Transitive verb (active gerundive).
   * @throws IOException when ElementarySltag cannot be serialized/deserialized.
   */
  @Test
  public void test_transitiveVerb_activeGerundive() throws IOException {
    /* Word */
    String word = "respecting";

    /* LTAG */
    Ltag ltag = LtagTemplates.transitiveVerbActiveGerundive("respecting", "NP2", "DP2");

    /* DUDES */
    Dudes dudes = DudesTemplates.transitiveVerb("http://dbpedia.org/resource/respect", null, "DP2");

    /* ElementarySltag */
    ElementarySltag expected = new SimpleElementarySltag(word, ltag, dudes);

    testSerialization(expected);
  }

  /**
   * Tests {@link ElementarySltag} serialization/deserialization.
   * Transitive verb (passive gerundive).
   * @throws IOException when ElementarySltag cannot be serialized/deserialized.
   */
  @Test
  public void test_transitiveVerb_passiveGerundive() throws IOException {
    /* Word */
    String word = "respected";

    /* LTAG */
    Ltag ltag = LtagTemplates.transitiveVerbPassiveGerundive("respected", "by", "NP2", "DP1");

    /* DUDES */
    Dudes dudes = DudesTemplates.transitiveVerb("http://dbpedia.org/resource/respect", "DP1", null);

    /* ElementarySltag */
    ElementarySltag expected = new SimpleElementarySltag(word, ltag, dudes);

    testSerialization(expected);
  }

  /**
   * Tests {@link ElementarySltag} serialization/deserialization.
   * Transitive verb (active relative).
   * @throws IOException when ElementarySltag cannot be serialized/deserialized.
   */
  @Test
  public void test_transitiveVerb_activeRelative() throws IOException {
    /* Word */
    String word = "respects";

    /* LTAG */
    Ltag ltag = LtagTemplates.transitiveVerbActiveRelative("respects", "who", "NP2", "DP2");

    /* DUDES */
    Dudes dudes = DudesTemplates.transitiveVerb("http://dbpedia.org/resource/respect", null, "DP2");

    /* ElementarySltag */
    ElementarySltag expected = new SimpleElementarySltag(word, ltag, dudes);

    testSerialization(expected);
  }

  /**
   * Tests {@link ElementarySltag} serialization/deserialization.
   * Transitive verb (passive relative).
   * @throws IOException when ElementarySltag cannot be serialized/deserialized.
   */
  @Test
  public void test_transitiveVerb_passiveRelative() throws IOException {
    /* Word */
    String word = "respects";

    /* LTAG */
    Ltag ltag = LtagTemplates.transitiveVerbPassiveRelative("respects", "is", "who", "by","NP2", "DP2");

    /* DUDES */
    Dudes dudes = DudesTemplates.transitiveVerb("http://dbpedia.org/resource/respect", "DP1", null);

    /* ElementarySltag */
    ElementarySltag expected = new SimpleElementarySltag(word, ltag, dudes);

    testSerialization(expected);
  }

  /**
   * Tests {@link ElementarySltag} serialization/deserialization.
   * Transitive verb (prepositional).
   * @throws IOException when ElementarySltag cannot be serialized/deserialized.
   */
  @Test
  public void test_transitiveVerb_prepositional() throws IOException {
    /* Word */
    String word = "wins";

    /* LTAG */
    Ltag ltag = LtagTemplates.transitiveVerbPrepositional(
        "wins", "against", "DP1", "DP2", "DP3");

    /* DUDES */
    Dudes dudes = DudesTemplates.transitivePrepositionalVerb(
        "http://dbpedia.org/resource/winner",
        "http://dbpedia.org/resource/loser",
        "DP2", "DP1", "DP3");

    /* ElementarySltag */
    ElementarySltag expected = new SimpleElementarySltag(word, ltag, dudes);

    testSerialization(expected);
  }

  /**
   * Tests {@link ElementarySltag} serialization/deserialization.
   * Adjective (attributive).
   * @throws IOException when ElementarySltag cannot be serialized/deserialized.
   */
  @Test
  public void test_adjective_attributive() throws IOException {
    /* Word */
    String word = "suspended";

    /* LTAG */
    Ltag ltag = LtagTemplates.adjectiveAttributive("suspended", "N2");

    /* DUDES */
    Dudes dudes = DudesTemplates.adjective("http://dbpedia.org/resource/suspended");

    /* ElementarySltag */
    ElementarySltag expected = new SimpleElementarySltag(word, ltag, dudes);

    testSerialization(expected);
  }

  /**
   * Tests {@link ElementarySltag} serialization/deserialization.
   * Adjective (predicative).
   * @throws IOException when ElementarySltag cannot be serialized/deserialized.
   */
  @Test
  public void test_adjective_predicative() throws IOException {
    /* Word */
    String word = "suspended";

    /* LTAG */
    Ltag ltag = LtagTemplates.adjectivePredicative("suspended", "is", "DP1");

    /* DUDES */
    Dudes dudes = DudesTemplates.adjective("http://dbpedia.org/resource/suspended");

    /* ElementarySltag */
    ElementarySltag expected = new SimpleElementarySltag(word, ltag, dudes);

    testSerialization(expected);
  }

  /**
   * Tests {@link ElementarySltag} serialization/deserialization.
   * Adjective (comparative).
   * @throws IOException when ElementarySltag cannot be serialized/deserialized.
   */
  @Test
  public void test_adjective_comparative() throws IOException {
    /* Word */
    String word = "taller";

    /* LTAG */
    Ltag ltag = LtagTemplates.adjectiveComparative(
        "taller", "is", "than", "DP1", "DP2");

    /* DUDES */
    Dudes dudes = DudesTemplates.adjectiveComparative(
        OperatorType.GREATER, "http://dbpedia.org/resource/height",
        "DP1", "DP2");

    /* ElementarySltag */
    ElementarySltag expected = new SimpleElementarySltag(word, ltag, dudes);

    testSerialization(expected);
  }

  /**
   * Tests {@link ElementarySltag} serialization/deserialization.
   * Adjective (superlative).
   * @throws IOException when ElementarySltag cannot be serialized/deserialized.
   */
  @Test
  public void test_adjective_superlative() throws IOException {
    /* Word */
    String word = "tallest";

    /* LTAG */
    Ltag ltag = LtagTemplates.adjectiveSuperlative(
        "tallest", "the", "NP1");

    /* DUDES */
    Dudes dudes = DudesTemplates.adjectiveSuperlative(
        OperatorType.MAX, "http://dbpedia.org/resource/height", "NP1");

    /* ElementarySltag */
    ElementarySltag expected = new SimpleElementarySltag(word, ltag, dudes);

    testSerialization(expected);
  }

  /**
   * Tests {@link ElementarySltag} serialization/deserialization.
   * Article (undeterminative)
   * @throws IOException when ElementarySltag cannot be serialized/deserialized.
   */
  @Test
  public void test_article_undeterminative() throws IOException {
    /* Word */
    String word = "a";

    /* LTAG */
    Ltag ltag = LtagTemplates.articleUndeterminative("a", "NP1");

    /* DUDES */
    Dudes dudes = DudesTemplates.articleUndeterminative("NP1");

    /* ElementarySltag */
    ElementarySltag expected = new SimpleElementarySltag(word, ltag, dudes);

    testSerialization(expected);
  }


}
