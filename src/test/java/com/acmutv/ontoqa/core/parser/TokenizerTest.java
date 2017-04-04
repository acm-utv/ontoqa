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

import com.acmutv.ontoqa.core.grammar.Grammar;
import com.acmutv.ontoqa.core.grammar.GrammarManager;
import com.acmutv.ontoqa.core.grammar.GrammarMatchType;
import com.acmutv.ontoqa.core.grammar.SimpleGrammar;
import com.acmutv.ontoqa.core.semantics.dudes.DudesTemplates;
import com.acmutv.ontoqa.core.semantics.sltag.ElementarySltag;
import com.acmutv.ontoqa.core.semantics.sltag.SimpleElementarySltag;
import com.acmutv.ontoqa.core.semantics.sltag.SimpleSltag;
import com.acmutv.ontoqa.core.semantics.sltag.Sltag;
import com.acmutv.ontoqa.core.syntax.ltag.LtagTemplates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.acmutv.ontoqa.benchmark.Common.IS_HEADQUARTERED_IRI;
import static com.acmutv.ontoqa.benchmark.Common.MICROSOFT_IRI;

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

  /* where */
  private static final Sltag where = new SimpleSltag(LtagTemplates.wh("where"), DudesTemplates.where());

  /* is */
  private static final  Sltag is = new SimpleSltag(
      LtagTemplates.copula("is", "1","2"),
      DudesTemplates.copula("1", "2"));

  /* is * headquartered (interrogative) */
  private static final Sltag isHeadquartered = new SimpleSltag(
      LtagTemplates.transitiveVerbPassiveIndicativeInterrogative("headquartered", "is","subj", "obj"),
      DudesTemplates.property(IS_HEADQUARTERED_IRI,"subj", "obj"));

  /* Microsoft */
  private static final Sltag microsoft = new SimpleSltag(
      LtagTemplates.properNoun("Microsoft"),
      DudesTemplates.properNoun(MICROSOFT_IRI)
  );

  private Grammar build() {
    Grammar grammar = new SimpleGrammar();

    grammar.addElementarySLTAG(
        new SimpleElementarySltag("where", where)
    );

    grammar.addElementarySLTAG(
        new SimpleElementarySltag("is", is)
    );

    grammar.addElementarySLTAG(
        new SimpleElementarySltag("is \\w* headquartered", isHeadquartered)
    );

    grammar.addElementarySLTAG(
        new SimpleElementarySltag("Microsoft", microsoft)
    );

    return grammar;
  }

  /**
   * Tests tokenizer, with empty sentence.
   */
  @Test
  public void test_empty() {
    Grammar grammar = build();
    String sentence = "";

    SltagTokenizer tokenizer = new SimpleSltagTokenizer(grammar, sentence);

    Token actual = tokenizer.next();

    Assert.assertNull(actual);
  }

  /**
   * Tests tokenizer, with sentence `where is Microsoft headquartered`.
   */
  @Test
  @Ignore
  public void test_whereIsMicrosoftHeadquartered() {
    Grammar grammar = build();
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
        "where", new ArrayList<Sltag>(){{
          add(where);
        }},
        null)
    );
    expected.add(new Token(
            "is \\w* headquartered", new ArrayList<Sltag>(){{
          add(isHeadquartered);
        }},
            0)
    );
    expected.add(new Token(
            "Microsoft", new ArrayList<Sltag>(){{
          add(isHeadquartered);
        }},
            0)
    );

    Assert.assertEquals(expected, actual);
  }

}
