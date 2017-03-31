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
public class GrammarTest {

  private static final Logger LOGGER = LogManager.getLogger(GrammarTest.class);

  private Grammar build() {
    Grammar grammar = new SimpleGrammar();

    grammar.addElementarySLTAG(
        new SimpleElementarySltag(
            "Uruguay FC",
            LtagTemplates.properNoun("Uruguay FC"),
            DudesTemplates.properNoun("http://dbpedia.org/resource/Uruguay")
        )
    );

    grammar.addElementarySLTAG(
        new SimpleElementarySltag(
            "wins",
            LtagTemplates.intransitiveVerb("wins", "DP1"),
            DudesTemplates.intransitiveVerb("http://dbpedia.org/resource/winner", "DP1")
        )
    );

    grammar.addElementarySLTAG(
        new SimpleElementarySltag(
            "a",
            LtagTemplates.determiner("a", "NP1"),
            DudesTemplates.articleUndeterminative("NP1")
        )
    );

    grammar.addElementarySLTAG(
        new SimpleElementarySltag(
            "game",
            LtagTemplates.classNoun("game", false),
            DudesTemplates.classNoun("http://dbpedia.org/resource/Game", false)
        )
    );

    return grammar;
  }

  /**
   * Tests match start
   */
  @Test
  public void test_matchStart() {
    Grammar grammar = build();

    Assert.assertTrue(grammar.matchStart("Uruguay"));
    Assert.assertTrue(grammar.matchStart("Uruguay FC"));
    Assert.assertTrue(grammar.matchStart("wins"));
    Assert.assertFalse(grammar.matchStart("wins a"));
  }

}
