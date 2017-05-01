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

package com.acmutv.ontoqa.core.grammar;

import com.acmutv.ontoqa.core.grammar.serial.GrammarJsonMapper;
import com.acmutv.ontoqa.core.grammar.serial.GrammarYamlMapper;
import com.acmutv.ontoqa.core.semantics.dudes.DudesTemplates;
import com.acmutv.ontoqa.core.semantics.sltag.SimpleElementarySltag;
import com.acmutv.ontoqa.core.syntax.ltag.LtagTemplates;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * JUnit tests for {@link Grammar} serialization.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Grammar
 * @see GrammarJsonMapper
 */
public class GrammarSerializationTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(GrammarSerializationTest.class);

  /**
   * Asserts the serialization correctness.
   * @param expected the expected value.
   * @throws IOException when value cannot be serialized or deserialized.
   */
  private void testSerialization(Grammar expected) throws IOException {
    GrammarJsonMapper jsonMapper = new GrammarJsonMapper();
    String json = jsonMapper.writeValueAsString(expected);
    LOGGER.debug("Grammar JSON serialization: \n{}", json);
    Grammar actualJson = jsonMapper.readValue(json, Grammar.class);
    Assert.assertEquals(expected, actualJson);

    GrammarYamlMapper yamlMapper = new GrammarYamlMapper();
    String yaml = yamlMapper.writeValueAsString(expected);
    LOGGER.debug("Grammar YAML serialization: \n{}", yaml);
    Grammar actualYaml = yamlMapper.readValue(yaml, Grammar.class);
    Assert.assertEquals(expected, actualYaml);
  }

  /**
   * Tests {@link Grammar} serialization/deserialization.
   * Empty.
   * @throws IOException when LTAG cannot be serialized/deserialized.
   */
  @Test
  public void test_empty() throws IOException {
    Grammar expected = new SimpleGrammar();
    testSerialization(expected);
  }

  /**
   * Tests {@link Grammar} serialization/deserialization.
   * Simple.
   * @throws IOException when LTAG cannot be serialized/deserialized.
   */
  @Test
  public void test_simple() throws IOException {
    Grammar expected = new SimpleGrammar();

    expected.addElementarySLTAG(
        new SimpleElementarySltag(
            "Uruguay",
            LtagTemplates.properNoun("Uruguay"),
            DudesTemplates.properNoun("http://dbpedia.org/resource/Uruguay")
        )
    );

    expected.addElementarySLTAG(
        new SimpleElementarySltag(
            "wins",
            LtagTemplates.intransitiveVerb("wins", "DP1"),
            DudesTemplates.intransitiveVerb("http://dbpedia.org/resource/winner", "DP1")
        )
    );

    expected.addElementarySLTAG(
        new SimpleElementarySltag(
            "a",
            LtagTemplates.determiner("a", "NP1"),
            DudesTemplates.articleUndeterminative("NP1")
        )
    );

    expected.addElementarySLTAG(
        new SimpleElementarySltag(
            "game",
            LtagTemplates.classNoun("game", false),
            DudesTemplates.classNoun("http://dbpedia.org/resource/Game", false)
        )
    );

    testSerialization(expected);
  }

}
