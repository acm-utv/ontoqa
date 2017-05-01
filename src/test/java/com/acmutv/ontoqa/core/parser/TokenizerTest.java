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
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

  private static final Logger LOGGER = LoggerFactory.getLogger(TokenizerTest.class);

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
   * Tests tokenizer, with sentence `how many people founded Microsoft`.
   */
  @Test
  public void test_howManyPeopleFoundedMicrosoft() {
    Grammar grammar = CommonGrammar.build_completeGrammar();
    String sentence = "how many people founded Microsoft";

    SltagTokenizer tokenizer = new SimpleSltagTokenizer(grammar, sentence);

    List<Token> actual = new ArrayList<>();
    while (tokenizer.hasNext()) {
      Token token = tokenizer.next();
      actual.add(token);
      LOGGER.debug("\n#=====#\nTOKEN: {}\n#=====#", token);
    }

    List<Token> expected = new ArrayList<>();
    expected.add(new Token(
            "how many", new ArrayList<ElementarySltag>(){{
          add(HOW_MANY);
        }},
            null)
    );
    expected.add(new Token(
            "people", new ArrayList<ElementarySltag>(){{
          add(PEOPLE);
        }},
            1)
    );
    expected.add(new Token(
            "founded", new ArrayList<ElementarySltag>(){{
          add(FOUNDED);
        }},
            2)
    );
    expected.add(new Token(
            "Microsoft", new ArrayList<ElementarySltag>(){{
          add(MICROSOFT);
        }},
            3)
    );

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests tokenizer, with sentence `who is the CEO of Apple`.
   */
  @Test
  public void test_whoIsTheCEOOfApple() {
    Grammar grammar = CommonGrammar.build_completeGrammar();
    String sentence = "who is the CEO of Apple";

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
            "is", new ArrayList<ElementarySltag>(){{
          add(IS);
          add(IS_INTERROGATIVE);
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
            "CEO of", new ArrayList<ElementarySltag>(){{
          add(CEO_OF);
        }},
            2)
    );
    expected.add(new Token(
            "Apple", new ArrayList<ElementarySltag>(){{
          add(APPLE);
        }},
            4)
    );

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests tokenizer, with sentence `what is the name of the CEO of Apple`.
   */
  @Test
  public void test_whatIsTheNameOfTheCEOOfApple() {
    Grammar grammar = CommonGrammar.build_completeGrammar();
    String sentence = "what is the name of the CEO of Apple";

    SltagTokenizer tokenizer = new SimpleSltagTokenizer(grammar, sentence);

    List<Token> actual = new ArrayList<>();
    while (tokenizer.hasNext()) {
      Token token = tokenizer.next();
      actual.add(token);
      LOGGER.debug("\n#=====#\nTOKEN: {}\n#=====#", token);
    }

    List<Token> expected = new ArrayList<>();
    expected.add(new Token(
            "what", new ArrayList<ElementarySltag>(){{
          add(WHAT);
        }},
            null)
    );
    expected.add(new Token(
            "is", new ArrayList<ElementarySltag>(){{
          add(IS);
          add(IS_INTERROGATIVE);
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
            "name of", new ArrayList<ElementarySltag>(){{
          add(NAME_OF);
        }},
            2)
    );
    expected.add(new Token(
            "the", new ArrayList<ElementarySltag>(){{
          add(THE);
        }},
            4)
    );
    expected.add(new Token(
            "CEO of", new ArrayList<ElementarySltag>(){{
          add(CEO_OF);
        }},
            5)
    );
    expected.add(new Token(
            "Apple", new ArrayList<ElementarySltag>(){{
          add(APPLE);
        }},
            7)
    );

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests tokenizer, with sentence `who is the chief executive officer of Apple`.
   */
  @Test
  public void test_whoIsTheChiefExecutiveOfficerOfApple() {
    Grammar grammar = CommonGrammar.build_completeGrammar();
    String sentence = "who is the chief executive officer of Apple";

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
            "is", new ArrayList<ElementarySltag>(){{
          add(IS);
          add(IS_INTERROGATIVE);
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
            "chief executive officer of", new ArrayList<ElementarySltag>(){{
          add(CHIEF_EXECUTIVE_OFFICER_OF);
        }},
            2)
    );
    expected.add(new Token(
            "Apple", new ArrayList<ElementarySltag>(){{
          add(APPLE);
        }},
            6)
    );

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests tokenizer, with sentence `who is the chief financial officer of Apple`.
   */
  @Test
  public void test_whoIsTheChiefFinancialOfficerOfApple() {
    Grammar grammar = CommonGrammar.build_completeGrammar();
    String sentence = "who is the chief financial officer of Apple";

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
            "is", new ArrayList<ElementarySltag>(){{
          add(IS);
          add(IS_INTERROGATIVE);
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
            "chief financial officer of", new ArrayList<ElementarySltag>(){{
          add(CHIEF_FINANCIAL_OFFICER_OF);
        }},
            2)
    );
    expected.add(new Token(
            "Apple", new ArrayList<ElementarySltag>(){{
          add(APPLE);
        }},
            6)
    );

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests tokenizer, with sentence `who are the corporate officers of Apple`.
   */
  @Test
  public void test_whoAreTheCorporateOfficersOfApple() {
    Grammar grammar = CommonGrammar.build_completeGrammar();
    String sentence = "who are the corporate officers of Apple";

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
            "corporate officers of", new ArrayList<ElementarySltag>(){{
          add(CORPORATE_OFFICERS_OF);
        }},
            2)
    );
    expected.add(new Token(
            "Apple", new ArrayList<ElementarySltag>(){{
          add(APPLE);
        }},
            5)
    );

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests tokenizer, with sentence `who is the chairman of Apple`.
   */
  @Test
  public void test_whoIsTheChairmanOfApple() {
    Grammar grammar = CommonGrammar.build_completeGrammar();
    String sentence = "who is the chairman of Apple";

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
            "is", new ArrayList<ElementarySltag>(){{
          add(IS);
          add(IS_INTERROGATIVE);
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
            "chairman of", new ArrayList<ElementarySltag>(){{
          add(CHAIRMAN_OF);
        }},
            2)
    );
    expected.add(new Token(
            "Apple", new ArrayList<ElementarySltag>(){{
          add(APPLE);
        }},
            4)
    );

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests tokenizer, with sentence `who is the president of Google`.
   */
  @Test
  public void test_whoIsThePresidentOfGoogle() {
    Grammar grammar = CommonGrammar.build_completeGrammar();
    String sentence = "who is the president of Google";

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
            "is", new ArrayList<ElementarySltag>(){{
          add(IS);
          add(IS_INTERROGATIVE);
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
            "president of", new ArrayList<ElementarySltag>(){{
          add(PRESIDENT_OF);
        }},
            2)
    );
    expected.add(new Token(
            "Google", new ArrayList<ElementarySltag>(){{
          add(GOOGLE);
        }},
            4)
    );

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests tokenizer, with sentence `what is the net income of Microsoft`.
   */
  @Test
  public void test_whatIsTheNetIncomeOfMicrosoft() {
    Grammar grammar = CommonGrammar.build_completeGrammar();
    String sentence = "what is the net income of Microsoft";

    SltagTokenizer tokenizer = new SimpleSltagTokenizer(grammar, sentence);

    List<Token> actual = new ArrayList<>();
    while (tokenizer.hasNext()) {
      Token token = tokenizer.next();
      actual.add(token);
      LOGGER.debug("\n#=====#\nTOKEN: {}\n#=====#", token);
    }

    List<Token> expected = new ArrayList<>();
    expected.add(new Token(
            "what", new ArrayList<ElementarySltag>(){{
          add(WHAT);
        }},
            null)
    );
    expected.add(new Token(
            "is", new ArrayList<ElementarySltag>(){{
          add(IS);
          add(IS_INTERROGATIVE);
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
            "net income of", new ArrayList<ElementarySltag>(){{
          add(NET_INCOME_OF);
        }},
            2)
    );
    expected.add(new Token(
            "Microsoft", new ArrayList<ElementarySltag>(){{
          add(MICROSOFT);
        }},
            5)
    );

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests tokenizer, with sentence `is Satya Nadella the CEO of Microsoft`.
   */
  @Test
  public void test_isSatyaNadellaTheCEOOfMicrosoft() {
    Grammar grammar = CommonGrammar.build_completeGrammar();
    String sentence = "is Satya Nadella the CEO of Microsoft";

    SltagTokenizer tokenizer = new SimpleSltagTokenizer(grammar, sentence);

    List<Token> actual = new ArrayList<>();
    while (tokenizer.hasNext()) {
      Token token = tokenizer.next();
      actual.add(token);
      LOGGER.debug("\n#=====#\nTOKEN: {}\n#=====#", token);
    }

    List<Token> expected = new ArrayList<>();
    expected.add(new Token(
            "is", new ArrayList<ElementarySltag>(){{
            add(IS);
            add(IS_INTERROGATIVE);
        }},
            null)
    );
    expected.add(new Token(
            "Satya Nadella", new ArrayList<ElementarySltag>(){{
          add(SATYA_NADELLA);
        }},
            0)
    );
    expected.add(new Token(
            "the", new ArrayList<ElementarySltag>(){{
          add(THE);
        }},
            2)
    );
    expected.add(new Token(
            "CEO of", new ArrayList<ElementarySltag>(){{
          add(CEO_OF);
        }},
            3)
    );
    expected.add(new Token(
            "Microsoft", new ArrayList<ElementarySltag>(){{
          add(MICROSOFT);
        }},
            5)
    );

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests tokenizer, with sentence `did Microsoft acquire a company headquartered in Italy`.
   */
  @Test
  public void test_didMicrosoftAcquireACompanyHeadquarteredInItaly() {
    Grammar grammar = CommonGrammar.build_completeGrammar();
    String sentence = "did Microsoft acquire a company headquartered in Italy";

    SltagTokenizer tokenizer = new SimpleSltagTokenizer(grammar, sentence);

    List<Token> actual = new ArrayList<>();
    while (tokenizer.hasNext()) {
      Token token = tokenizer.next();
      actual.add(token);
      LOGGER.debug("\n#=====#\nTOKEN: {}\n#=====#", token);
    }

    List<Token> expected = new ArrayList<>();
    expected.add(new Token(
            "did", new ArrayList<ElementarySltag>(){{
          add(DID);
        }},
            null)
    );
    expected.add(new Token(
            "Microsoft", new ArrayList<ElementarySltag>(){{
          add(MICROSOFT);
        }},
            0)
    );
    expected.add(new Token(
            "acquire", new ArrayList<ElementarySltag>(){{
          add(ACQUIRE);
        }},
            1)
    );
    expected.add(new Token(
            "a", new ArrayList<ElementarySltag>(){{
          add(A);
        }},
            2)
    );
    expected.add(new Token(
            "company", new ArrayList<ElementarySltag>(){{
          add(COMPANY);
        }},
            3)
    );
    expected.add(new Token(
            "headquartered in", new ArrayList<ElementarySltag>(){{
          add(HEADQUARTERED_IN);
        }},
            4)
    );
    expected.add(new Token(
            "Italy", new ArrayList<ElementarySltag>(){{
          add(ITALY);
        }},
            6)
    );

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests tokenizer, with sentence `did Microsoft acquire an italian company`.
   */
  @Test
  public void test_didMicrosoftAcquireAnItalianCompany() {
    Grammar grammar = CommonGrammar.build_completeGrammar();
    String sentence = "did Microsoft acquire an italian company";

    SltagTokenizer tokenizer = new SimpleSltagTokenizer(grammar, sentence);

    List<Token> actual = new ArrayList<>();
    while (tokenizer.hasNext()) {
      Token token = tokenizer.next();
      actual.add(token);
      LOGGER.debug("\n#=====#\nTOKEN: {}\n#=====#", token);
    }

    List<Token> expected = new ArrayList<>();
    expected.add(new Token(
            "did", new ArrayList<ElementarySltag>(){{
          add(DID);
        }},
            null)
    );
    expected.add(new Token(
            "Microsoft", new ArrayList<ElementarySltag>(){{
          add(MICROSOFT);
        }},
            0)
    );
    expected.add(new Token(
            "acquire", new ArrayList<ElementarySltag>(){{
          add(ACQUIRE);
        }},
            1)
    );
    expected.add(new Token(
            "an", new ArrayList<ElementarySltag>(){{
          add(AN);
        }},
            2)
    );
    expected.add(new Token(
            "italian", new ArrayList<ElementarySltag>(){{
          add(ITALIAN_ATTRIBUTIVE);
          add(ITALIAN_NOMINATIVE);
        }},
            3)
    );
    expected.add(new Token(
            "company", new ArrayList<ElementarySltag>(){{
          add(COMPANY);
        }},
            4)
    );

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests tokenizer, with sentence `is Satya Nadella italian`.
   */
  @Test
  public void test_isSatyaNadellaItalian() {
    Grammar grammar = CommonGrammar.build_completeGrammar();
    String sentence = "is Satya Nadella italian";

    SltagTokenizer tokenizer = new SimpleSltagTokenizer(grammar, sentence);

    List<Token> actual = new ArrayList<>();
    while (tokenizer.hasNext()) {
      Token token = tokenizer.next();
      actual.add(token);
      LOGGER.debug("\n#=====#\nTOKEN: {}\n#=====#", token);
    }

    List<Token> expected = new ArrayList<>();
    expected.add(new Token(
            "is", new ArrayList<ElementarySltag>(){{
              add(IS);
              add(IS_INTERROGATIVE);
        }},
            null)
    );
    expected.add(new Token(
            "Satya Nadella", new ArrayList<ElementarySltag>(){{
          add(SATYA_NADELLA);
        }},
            0)
    );
    expected.add(new Token(
            "italian", new ArrayList<ElementarySltag>(){{
          add(ITALIAN_ATTRIBUTIVE);
          add(ITALIAN_NOMINATIVE);
        }},
            2)
    );

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests tokenizer, with sentence `where is Microsoft headquartered`.
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

  /**
   * Tests tokenizer, with sentence `what is the most valuable company`.
   */
  @Test
  public void test_whatIsTheMostValuableCompany() {
    Grammar grammar = CommonGrammar.build_completeGrammar();
    String sentence = "what is the most valuable company";

    SltagTokenizer tokenizer = new SimpleSltagTokenizer(grammar, sentence);

    List<Token> actual = new ArrayList<>();
    while (tokenizer.hasNext()) {
      Token token = tokenizer.next();
      actual.add(token);
      LOGGER.debug("\n#=====#\nTOKEN: {}\n#=====#", token);
    }

    List<Token> expected = new ArrayList<>();
    expected.add(new Token(
            "what", new ArrayList<ElementarySltag>(){{
          add(WHAT);
        }},
            null)
    );
    expected.add(new Token(
            "is", new ArrayList<ElementarySltag>(){{
          add(IS);
          add(IS_INTERROGATIVE);
        }},
            0)
    );
    expected.add(new Token(
            "the most valuable", new ArrayList<ElementarySltag>(){{
          add(THE_MOST_VALUABLE);
        }},
            1)
    );
    expected.add(new Token(
            "company", new ArrayList<ElementarySltag>(){{
          add(COMPANY);
        }},
            4)
    );

    Assert.assertEquals(expected, actual);
  }

}
