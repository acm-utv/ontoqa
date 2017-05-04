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
import com.acmutv.ontoqa.core.semantics.base.statement.OperatorType;
import com.acmutv.ontoqa.core.semantics.dudes.DudesTemplates;
import com.acmutv.ontoqa.core.semantics.sltag.ElementarySltag;
import com.acmutv.ontoqa.core.semantics.sltag.SimpleElementarySltag;
import com.acmutv.ontoqa.core.syntax.ltag.LtagTemplates;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.acmutv.ontoqa.benchmark.Common.*;

/**
 * Collection of Elementary SLTAG for tests.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class CommonGrammar {

  private static final Logger LOGGER = LoggerFactory.getLogger(CommonGrammar.class);

  /* what */
  public static final ElementarySltag WHAT = new SimpleElementarySltag("what",
      LtagTemplates.wh("what"),
      DudesTemplates.what()
  );

  /* where */
  public static final ElementarySltag WHERE = new SimpleElementarySltag("where",
      LtagTemplates.wh("where"),
      DudesTemplates.where()
  );

  /* who */
  public static final ElementarySltag WHO = new SimpleElementarySltag("who",
      LtagTemplates.wh("who"),
      DudesTemplates.who()
  );

  /* how many */
  public static final ElementarySltag HOW_MANY = new SimpleElementarySltag("how many",
      LtagTemplates.how("how", "many", "np"),
      DudesTemplates.howmany("np")
  );

  /* a */
  public static final ElementarySltag A = new SimpleElementarySltag("a",
      LtagTemplates.determiner("a", "np"),
      DudesTemplates.determiner("np")
  );

  /* an */
  public static final ElementarySltag AN = new SimpleElementarySltag("an",
      LtagTemplates.determiner("an", "np"),
      DudesTemplates.determiner("np")
  );

  /* the */
  public static final ElementarySltag THE = new SimpleElementarySltag("the",
      LtagTemplates.determiner("the", "np"),
      DudesTemplates.determiner("np")
  );

  /* CEO of */
  public static final ElementarySltag CEO_OF = new SimpleElementarySltag("CEO of",
      LtagTemplates.relationalPrepositionalNoun("CEO", "of", "obj", false),
      DudesTemplates.relationalNounInverse(HAS_CEO_IRI, "obj",false)
  );

  /* chairman of */
  public static final ElementarySltag CHAIRMAN_OF = new SimpleElementarySltag("chairman of",
      LtagTemplates.relationalPrepositionalNoun("chairman", "of", "obj", false),
      DudesTemplates.relationalNounInverse(HAS_CHAIRMAN_IRI, "obj",false)
  );

  /* chief executive officer of */
  public static final ElementarySltag CHIEF_EXECUTIVE_OFFICER_OF = new SimpleElementarySltag("chief executive officer of",
      LtagTemplates.relationalPrepositionalNoun("chief executive officer", "of", "obj", false),
      DudesTemplates.relationalNounInverse(HAS_CEO_IRI, "obj",false)
  );

  /* chief financial officer of */
  public static final ElementarySltag CHIEF_FINANCIAL_OFFICER_OF = new SimpleElementarySltag("chief financial officer of",
      LtagTemplates.relationalPrepositionalNoun("chief financial officer", "of", "obj", false),
      DudesTemplates.relationalNounInverse(HAS_CFO_IRI, "obj",false)
  );

  /* company */
  public static final ElementarySltag COMPANY = new SimpleElementarySltag("company",
      LtagTemplates.classNoun("company", false),
      DudesTemplates.classNoun(COMPANY_IRI, false)
  );

  /* corporate officers of */
  public static final ElementarySltag CORPORATE_OFFICERS_OF = new SimpleElementarySltag("corporate officers of",
      LtagTemplates.relationalPrepositionalNoun("corporate officers", "of", "obj", false),
      DudesTemplates.relationalNounInverse(HAS_CORPORATE_OFFICER_IRI, "obj",false)
  );

  /* founders of */
  public static final ElementarySltag FOUNDERS_OF = new SimpleElementarySltag("founders of",
      LtagTemplates.relationalPrepositionalNoun("founders", "of", "obj", false),
      DudesTemplates.relationalNounInverse(HAS_FOUNDER_IRI, "obj",false)
  );

  /* name of */
  public static final ElementarySltag NAME_OF = new SimpleElementarySltag("name of",
      LtagTemplates.relationalPrepositionalNoun("name", "of", "subj", false),
      DudesTemplates.empty()
  );

  /* people */
  public static final ElementarySltag PEOPLE = new SimpleElementarySltag("people",
      LtagTemplates.classNoun("people", false),
      DudesTemplates.type(RDF_TYPE_IRI, PERSON_IRI)
  );

  /* net income of */
  public static final ElementarySltag NET_INCOME_OF = new SimpleElementarySltag("net income of",
      LtagTemplates.relationalPrepositionalNoun("net income", "of", "subj", false),
      DudesTemplates.relationalNoun(HAS_NETINCOME_IRI, "subj",false)
  );

  /* president of */
  public static final ElementarySltag PRESIDENT_OF = new SimpleElementarySltag("president of",
      LtagTemplates.relationalPrepositionalNoun("president", "of", "obj", false),
      DudesTemplates.relationalNounInverse(HAS_CHAIRMAN_IRI, "obj",false)
  );

  /* founded */
  public static final ElementarySltag FOUNDED = new SimpleElementarySltag("founded",
      LtagTemplates.transitiveVerbActiveIndicative("founded", "subj", "obj"),
      DudesTemplates.property(HAS_FOUNDER_IRI, "subj", "obj")
  );

  /* acquire */
  public static final ElementarySltag ACQUIRE = new SimpleElementarySltag("acquire",
      LtagTemplates.transitiveVerbActiveIndicative("acquire", "subj", "obj"),
      DudesTemplates.property(ACQUIRE_IRI, "subj", "obj")
  );

  /* are */
  public static final ElementarySltag ARE = new SimpleElementarySltag("are",
      LtagTemplates.copula("are", "1", "2"),
      DudesTemplates.copula("1", "2")
  );

  /* did */
  public static final ElementarySltag DID = new SimpleElementarySltag("did",
      LtagTemplates.questioningDo("did"),
      DudesTemplates.empty()
  );

  /* headquartered in */
  public static final ElementarySltag HEADQUARTERED_IN = new SimpleElementarySltag("headquartered in",
      LtagTemplates.adjectivePrepositional("headquartered", "in", "dp"),
      DudesTemplates.property(HAS_HEADQUARTER_IRI, null, "dp")
  );

  /* italian (attributive) */
  public static final ElementarySltag ITALIAN_ATTRIBUTIVE = new SimpleElementarySltag("italian",
      LtagTemplates.adjectiveAttributive("italian"),
      DudesTemplates.propertyObjectValued(HAS_NATION_IRI, ITALY_IRI)
  );

  /* italian (nominative) */
  public static final ElementarySltag ITALIAN_NOMINATIVE = new SimpleElementarySltag("italian",
      LtagTemplates.adjectiveNominative("italian"),
      DudesTemplates.propertyObjectValued(HAS_NATION_IRI, ITALY_IRI)
  );

  /* the most valuable */
  public static final ElementarySltag THE_MOST_VALUABLE = new SimpleElementarySltag("the most valuable",
      LtagTemplates.adjectiveSuperlative("most valuable", "the", "np"),
      DudesTemplates.adjectiveSuperlative(OperatorType.MAX, HAS_COMPANY_VALUE_IRI, "np")
  );

  /* is */
  public static final ElementarySltag IS = new SimpleElementarySltag("is",
      LtagTemplates.copula("is", "1","2"),
      DudesTemplates.copula("1", "2")
  );

  /* is (interrogative) */
  public static final ElementarySltag IS_INTERROGATIVE = new SimpleElementarySltag("is",
      LtagTemplates.copulaInterrogative("is", "1", "2"),
      DudesTemplates.copulaInterrogative("1", "2")
  );

  /* is * headquartered */
  public static final ElementarySltag IS_HEADQUARTERED = new SimpleElementarySltag("is \\w* headquartered",
      LtagTemplates.transitiveVerbPassiveIndicativeInterrogative("headquartered", "is","subj", "obj"),
      DudesTemplates.property(HAS_HEADQUARTER_IRI,"subj", "obj")
  );

  /* Apple */
  public static final ElementarySltag APPLE = new SimpleElementarySltag("Apple",
      LtagTemplates.properNoun("Apple"),
      DudesTemplates.properNoun(APPLE_IRI)
  );

  /* Google */
  public static final ElementarySltag GOOGLE = new SimpleElementarySltag("Google",
      LtagTemplates.properNoun("Google"),
      DudesTemplates.properNoun(GOOGLE_IRI)
  );

  /* Italy */
  public static final ElementarySltag ITALY = new SimpleElementarySltag("Italy",
      LtagTemplates.properNoun("Italy"),
      DudesTemplates.properNoun(ITALY_IRI)
  );

  /* Microsoft */
  public static final ElementarySltag MICROSOFT = new SimpleElementarySltag("Microsoft",
      LtagTemplates.properNoun("Microsoft"),
      DudesTemplates.properNoun(MICROSOFT_IRI)
  );

  /* Satya Nadella */
  public static final ElementarySltag SATYA_NADELLA = new SimpleElementarySltag("Satya Nadella",
      LtagTemplates.properNoun("Satya Nadella"),
      DudesTemplates.properNoun(SATYA_NADELLA_IRI)
  );

  public static Grammar build_completeGrammar() {
    Grammar grammar = new SimpleGrammar();

    grammar.addElementarySLTAG(WHAT);
    grammar.addElementarySLTAG(WHERE);
    grammar.addElementarySLTAG(WHO);
    grammar.addElementarySLTAG(HOW_MANY);

    grammar.addElementarySLTAG(A);
    grammar.addElementarySLTAG(AN);
    grammar.addElementarySLTAG(THE);

    grammar.addElementarySLTAG(APPLE);
    grammar.addElementarySLTAG(GOOGLE);
    grammar.addElementarySLTAG(ITALY);
    grammar.addElementarySLTAG(MICROSOFT);
    grammar.addElementarySLTAG(SATYA_NADELLA);

    grammar.addElementarySLTAG(CEO_OF);
    grammar.addElementarySLTAG(CHAIRMAN_OF);
    grammar.addElementarySLTAG(CHIEF_EXECUTIVE_OFFICER_OF);
    grammar.addElementarySLTAG(CHIEF_FINANCIAL_OFFICER_OF);
    grammar.addElementarySLTAG(COMPANY);
    grammar.addElementarySLTAG(CORPORATE_OFFICERS_OF);
    grammar.addElementarySLTAG(FOUNDERS_OF);
    grammar.addElementarySLTAG(NAME_OF);
    grammar.addElementarySLTAG(NET_INCOME_OF);
    grammar.addElementarySLTAG(PEOPLE);
    grammar.addElementarySLTAG(PRESIDENT_OF);

    grammar.addElementarySLTAG(ACQUIRE);
    grammar.addElementarySLTAG(ARE);
    grammar.addElementarySLTAG(DID);
    grammar.addElementarySLTAG(IS);
    grammar.addElementarySLTAG(IS_HEADQUARTERED);
    grammar.addElementarySLTAG(IS_INTERROGATIVE);
    grammar.addElementarySLTAG(FOUNDED);

    grammar.addElementarySLTAG(HEADQUARTERED_IN);
    grammar.addElementarySLTAG(ITALIAN_ATTRIBUTIVE);
    grammar.addElementarySLTAG(ITALIAN_NOMINATIVE);
    grammar.addElementarySLTAG(THE_MOST_VALUABLE);

    try {
      LOGGER.info(new GrammarJsonMapper().writeValueAsString(grammar));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    return grammar;
  }

}
