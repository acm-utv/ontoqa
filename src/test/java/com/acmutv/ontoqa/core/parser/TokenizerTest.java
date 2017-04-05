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

package com.acmutv.ontoqa.core.parser;

import com.acmutv.ontoqa.core.grammar.CommonGrammar;
import com.acmutv.ontoqa.core.grammar.Grammar;
import com.acmutv.ontoqa.core.semantics.sltag.ElementarySltag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.acmutv.ontoqa.core.grammar.CommonGrammar.*;

/**
 * JUnit tests for {@link SltagTokenizer}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see SltagTokenizer
 */
public class TokenizerTest {

  private static final Logger LOGGER = LogManager.getLogger(TokenizerTest.class);

  /**
   * Tests tokenizer, with empty sentence.
   */
  @Test
  public void test_empty() {
    Grammar grammar = CommonGrammar.build_completeGrammar();
    String sentence = "";

    SltagTokenizer tokenizer = new SimpleSltagTokenizer(grammar, sentence);

    Token actual = tokenizer.next();

    Assert.assertNull(actual);
  }

  /**
   * Tests tokenizer, with sentence `who founded Microsoft`.
   */
  @Test
  public void test_whoFoundedMicrosoft() {
    Grammar grammar = CommonGrammar.build_completeGrammar();
    String sentence = "who founded Microsoft";

    SltagTokenizer tokenizer = new SimpleSltagTokenizer(grammar, sentence);

    List<Token> actual = new ArrayList<>();
    while (tokenizer.hasNext()) {
      Token token = tokenizer.next();
      actual.add(token);
      LOGGER.debug("\n#=====#\nTOKEN: {}\n#=====#", token);
    }

    List<Token> expected = new ArrayList<>();
    expected.add(new Token(
            "who", new ArrayList<ElementarySltag>(){{
          add(WHO);
        }},
            null)
    );
    expected.add(new Token(
            "founded", new ArrayList<ElementarySltag>(){{
          add(FOUNDED);
        }},
            0)
    );
    expected.add(new Token(
            "Microsoft", new ArrayList<ElementarySltag>(){{
          add(MICROSOFT);
        }},
            1)
    );

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests tokenizer, with sentence `who are the founders of Microsoft`.
   */
  @Test
  public void test_whoAreTheFoundersOfMicrosoft() {
    Grammar grammar = CommonGrammar.build_completeGrammar();
    String sentence = "who are the founders of Microsoft";

    SltagTokenizer tokenizer = new SimpleSltagTokenizer(grammar, sentence);

    List<Token> actual = new ArrayList<>();
    while (tokenizer.hasNext()) {
      Token token = tokenizer.next();
      actual.add(token);
      LOGGER.debug("\n#=====#\nTOKEN: {}\n#=====#", token);
    }

    List<Token> expected = new ArrayList<>();
    expected.add(new Token(
            "who", new ArrayList<ElementarySltag>(){{
          add(WHO);
        }},
            null)
    );
    expected.add(new Token(
            "are", new ArrayList<ElementarySltag>(){{
          add(ARE);
        }},
            0)
    );
    expected.add(new Token(
            "the", new ArrayList<ElementarySltag>(){{
          add(THE);
        }},
            1)
    );
    expected.add(new Token(
            "founders of", new ArrayList<ElementarySltag>(){{
          add(FOUNDERS_OF);
        }},
            2)
    );
    expected.add(new Token(
            "Microsoft", new ArrayList<ElementarySltag>(){{
          add(MICROSOFT);
        }},
            4)
    );

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests tokenizer, with sentence `where IS Microsoft headquartered`.
   */
  @Test
  public void test_whereIsMicrosoftHeadquartered() {
    Grammar grammar = CommonGrammar.build_completeGrammar();
    String sentence = "where is Microsoft headquartered";

    SltagTokenizer tokenizer = new SimpleSltagTokenizer(grammar, sentence);

    List<Token> actual = new ArrayList<>();
    while (tokenizer.hasNext()) {
      Token token = tokenizer.next();
      actual.add(token);
      LOGGER.debug("\n#=====#\nTOKEN: {}\n#=====#", token);
    }

    List<Token> expected = new ArrayList<>();
    expected.add(new Token(
        "where", new ArrayList<ElementarySltag>(){{
          add(WHERE);
        }},
        null)
    );
    expected.add(new Token(
            "is Microsoft headquartered", new ArrayList<ElementarySltag>(){{
          add(IS_HEADQUARTERED);
        }},
            0)
    );
    expected.add(new Token(
            "Microsoft", new ArrayList<ElementarySltag>(){{
          add(MICROSOFT);
        }},
            1)
    );

    Assert.assertEquals(expected, actual);
  }

}
