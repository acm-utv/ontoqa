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

import com.acmutv.ontoqa.core.semantics.dudes.DudesTemplates;
import com.acmutv.ontoqa.core.semantics.sltag.SimpleElementarySltag;
import com.acmutv.ontoqa.core.syntax.ltag.LtagTemplates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * JUnit tests for {@link GrammarManager}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Grammar
 * @see GrammarManager
 */
public class GrammarManagerTest {

  private static final Logger LOGGER = LogManager.getLogger(GrammarManagerTest.class);

  /**
   * Asserts the deserialization correctness.
   * @param actual the actual value.
   */
  private void testReading(Grammar actual) {
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

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests {@link Grammar} reading from a single file (YAML).
   */
  @Test
  public void test_read_yaml() throws IOException {
    String in = GrammarManagerTest.class.getResource("/grammar/yaml/uruguay.wins.a.game.sltag").getPath();
    Grammar actual = GrammarManager.read(in, GrammarFormat.YAML);
    testReading(actual);
  }

  /**
   * Tests {@link Grammar} reading from a single file (JSON).
   */
  @Test
  public void test_read_json() throws IOException {
    String in = GrammarManagerTest.class.getResource("/grammar/json/uruguay.wins.a.game.sltag").getPath();
    Grammar actual = GrammarManager.read(in, GrammarFormat.JSON);
    testReading(actual);
  }

  /**
   * Tests {@link Grammar} reading from multiple files (YAML).
   */
  @Test
  public void test_readAll_yaml() throws IOException {
    String in = GrammarManagerTest.class.getResource("/grammar/yaml/multiple/").getPath();
    Grammar actual = GrammarManager.readAll(in, GrammarFormat.YAML);
    testReading(actual);
  }

  /**
   * Tests {@link Grammar} reading from multiple files (JSON).
   */
  @Test
  public void test_readAll_json() throws IOException {
    String in = GrammarManagerTest.class.getResource("/grammar/json/multiple/").getPath();
    Grammar actual = GrammarManager.readAll(in, GrammarFormat.JSON);
    testReading(actual);
  }

}
