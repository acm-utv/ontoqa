/*
  The MIT License (MIT)

  Copyright (c) 2017 Antonella Botte, Giacomo Marciani and Debora Partigianoni

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

package com.acmutv.ontoqa.core.syntax;

import com.acmutv.ontoqa.core.exception.LTAGException;
import com.acmutv.ontoqa.core.exception.QueryException;
import com.acmutv.ontoqa.core.syntax.ltag.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit tests for LTAG operations with {@link SimpleLtag}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Ltag
 * @see SimpleLtag
 */
public class LtagOperationTest {

  private static final Logger LOGGER = LogManager.getLogger(LtagOperationTest.class);

  /**
   * Example implementing the question: "Albert Einstein married Elsa Einstein?"
   */
  @Test
  public void test_ask() throws QueryException, LTAGException {
    /* Albert Einstein */
    Ltag albert = LtagTemplates.properNoun("Albert Einstein");
    LOGGER.info("Albert Einstein:\n{}", albert.toPrettyString());

    /* Elsa Einstein */
    Ltag elsa = LtagTemplates.properNoun("Elsa Einstein");
    LOGGER.info("Elsa Einstein:\n{}", elsa.toPrettyString());

    /* married */
    Ltag married = LtagTemplates.transitiveVerbActiveIndicative("married", "subj", "obj");
    LOGGER.info("married:\n{}", married.toPrettyString());

    /* Albert Einstein married */
    LOGGER.info("Albert Einstein married: processing...");
    Ltag albertMarried = new LtagBuilder(married)
        .substitution(albert, "subj")
        .build();
    LOGGER.info("Albert Einstein married:\n{}", albertMarried.toPrettyString());

    /* Albert Einstein married Elsa Einstein */
    LOGGER.info("Albert Einstein married Elsa Einstein: processing...");
    Ltag albertMarriedElsa = new LtagBuilder(albertMarried)
        .substitution(elsa, "obj")
        .build();
    LOGGER.info("Albert Einstein married Elsa Einstein:\n{}", albertMarriedElsa.toPrettyString());

    /* expected */
    LtagNode s = new NonTerminalNode(SyntaxCategory.S);
    LtagNode dp1 = new NonTerminalNode(1, SyntaxCategory.DP);
    LtagNode vp = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode v = new NonTerminalNode(SyntaxCategory.V);
    LtagNode dp2 = new NonTerminalNode(2, SyntaxCategory.DP);
    LtagNode lexAlbert = new TerminalNode("Albert Einstein");
    LtagNode lexMarried = new TerminalNode("married");
    LtagNode lexElsa = new TerminalNode("Elsa Einstein");

    Ltag expected = new SimpleLtag(s);
    expected.addEdge(s, dp1);
    expected.addEdge(s, vp);
    expected.addEdge(dp1, lexAlbert);
    expected.addEdge(vp, v);
    expected.addEdge(vp, dp2);
    expected.addEdge(v, lexMarried);
    expected.addEdge(dp2, lexElsa);

    Assert.assertEquals(expected, albertMarriedElsa);
  }

  /**
   * Example implementing the question: "Who married Elsa Einstein?"
   */
  @Test
  public void test_whoMarried() throws QueryException, LTAGException {
    /* who */
    Ltag who = LtagTemplates.wh("who");
    LOGGER.info("who:\n{}", who.toPrettyString());

    /* Elsa Einstein */
    Ltag elsa = LtagTemplates.properNoun("Elsa Einstein");
    LOGGER.info("Elsa Einstein:\n{}", elsa.toPrettyString());

    /* married */
    Ltag married = LtagTemplates.transitiveVerbActiveIndicative("married", "subj", "obj");
    LOGGER.info("married:\n{}", married.toPrettyString());

    /* who married Elsa Einstein */
    LOGGER.info("who married Elsa Einstein: processing...");
    Ltag whoMarriedElsa = new LtagBuilder(married)
        .substitution(who, "subj")
        .substitution(elsa, "obj")
        .build();
    LOGGER.info("who married Elsa Einstein:\n{}", whoMarriedElsa.toPrettyString());

    /* expected */
    LtagNode s = new NonTerminalNode(SyntaxCategory.S);
    LtagNode dp1 = new NonTerminalNode(1, SyntaxCategory.DP);
    LtagNode vp = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode prn = new NonTerminalNode(SyntaxCategory.PRN);
    LtagNode v = new NonTerminalNode(SyntaxCategory.V);
    LtagNode dp2 = new NonTerminalNode(2, SyntaxCategory.DP);
    LtagNode lexWho = new TerminalNode("who");
    LtagNode lexMarried = new TerminalNode("married");
    LtagNode lexElsa = new TerminalNode("Elsa Einstein");

    Ltag expected = new SimpleLtag(s);
    expected.addEdge(s, dp1);
    expected.addEdge(s, vp);
    expected.addEdge(dp1, prn);
    expected.addEdge(vp, v);
    expected.addEdge(vp, dp2);
    expected.addEdge(prn, lexWho);
    expected.addEdge(v, lexMarried);
    expected.addEdge(dp2, lexElsa);

    Assert.assertEquals(expected, whoMarriedElsa);
  }

  /**
   * Example implementing the question: "Who is the spouse of Albert Einstein?"
   */
  @Test
  public void test_whoIsThe() throws QueryException, LTAGException {
    /* who */
    Ltag who = LtagTemplates.wh("who");
    LOGGER.info("who:\n{}", who.toPrettyString());

    /* is (copula) */
    Ltag is = LtagTemplates.copula("is", "1", "2");
    LOGGER.info("is:\n{}", is.toPrettyString());

    /* the */
    Ltag the = LtagTemplates.determiner("the", "np");
    LOGGER.info("the:\n{}", the.toPrettyString());

    /* spouse of */
    Ltag spouseOf = LtagTemplates.relationalPrepositionalNoun("spouse", "of", "dp", false);
    LOGGER.info("spouse of:\n{}", spouseOf.toPrettyString());

    /* Albert Einstein */
    Ltag albert = LtagTemplates.properNoun("Albert Einstein");
    LOGGER.info("Albert Einstein:\n{}", albert.toPrettyString());

    /* spouse of Albert Einstein */
    LOGGER.info("spouse of Albert Einstein: processing...");
    Ltag spouseOfAlbert = new LtagBuilder(spouseOf)
        .substitution(albert, "dp")
        .build();
    LOGGER.info("spouse of Albert Einstein:\n{}", spouseOfAlbert.toPrettyString());

    /* the spouse of Albert Einstein */
    LOGGER.info("the spouse of Albert Einstein: processing...");
    Ltag theSpouseOfAlbert = new LtagBuilder(the)
        .substitution(spouseOfAlbert, "np")
        .build();
    LOGGER.info("the spouse of Albert Einstein:\n{}", theSpouseOfAlbert.toPrettyString());

    /* who is */
    LOGGER.info("who is: processing...");
    Ltag whoIs = new LtagBuilder(is)
        .substitution(who, "1")
        .build();
    LOGGER.info("who is:\n{}", whoIs.toPrettyString());

    /* who is the spouse of Albert Einstein */
    LOGGER.info("who is the spouse of Albert Einstein: processing...");
    Ltag whoIsTheSpouseOfAlbert = new LtagBuilder(whoIs)
        .substitution(theSpouseOfAlbert, "2")
        .build();
    LOGGER.info("who is the spouse of Albert Einstein:\n{}", whoIsTheSpouseOfAlbert.toPrettyString());

    /* expected */
    LtagNode s = new NonTerminalNode(SyntaxCategory.S);
    LtagNode dp1 = new NonTerminalNode(1, SyntaxCategory.DP);
    LtagNode vp = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode prn = new NonTerminalNode(SyntaxCategory.PRN);
    LtagNode v = new NonTerminalNode(SyntaxCategory.V);
    LtagNode dp2 = new NonTerminalNode(2, SyntaxCategory.DP);
    LtagNode det = new NonTerminalNode(SyntaxCategory.DET);
    LtagNode np = new NonTerminalNode(SyntaxCategory.NP);
    LtagNode n = new NonTerminalNode(SyntaxCategory.N);
    LtagNode pp = new NonTerminalNode(SyntaxCategory.PP);
    LtagNode p = new NonTerminalNode(SyntaxCategory.P);
    LtagNode dp3 = new NonTerminalNode(3, SyntaxCategory.DP);
    LtagNode lexWho = new TerminalNode("who");
    LtagNode lexIs = new TerminalNode("is");
    LtagNode lexThe = new TerminalNode("the");
    LtagNode lexSpouse = new TerminalNode("spouse");
    LtagNode lexOf = new TerminalNode("of");
    LtagNode lexAlbert = new TerminalNode("Albert Einstein");

    Ltag expected = new SimpleLtag(s);
    expected.addEdge(s, dp1);
    expected.addEdge(s, vp);
    expected.addEdge(dp1, prn);
    expected.addEdge(vp, v);
    expected.addEdge(vp, dp2);
    expected.addEdge(prn, lexWho);
    expected.addEdge(v, lexIs);
    expected.addEdge(dp2, det);
    expected.addEdge(dp2, np);
    expected.addEdge(det, lexThe);
    expected.addEdge(np, n);
    expected.addEdge(np, pp);
    expected.addEdge(n, lexSpouse);
    expected.addEdge(pp, p);
    expected.addEdge(pp, dp3);
    expected.addEdge(p, lexOf);
    expected.addEdge(dp3, lexAlbert);

    Assert.assertEquals(expected, whoIsTheSpouseOfAlbert);
  }

  /**
   * Example implementing the question: "Who is the highest person?"
   */
  @Test
  public void test_whoSuperlative() throws QueryException, LTAGException {
    /* who */
    Ltag who = LtagTemplates.wh("who");
    LOGGER.info("who:\n{}", who.toPrettyString());

    /* is (copula) */
    Ltag is = LtagTemplates.copula("is", "1", "2");
    LOGGER.info("is:\n{}", is.toPrettyString());

    /* the highest */
    Ltag theHighest = LtagTemplates.adjectiveSuperlative("highest", "the", "np");
    LOGGER.info("the highest:\n{}", theHighest.toPrettyString());

    /* person */
    Ltag person = LtagTemplates.classNoun("person", false);
    LOGGER.info("person:\n{}", person.toPrettyString());

    /* the highest person */
    LOGGER.info("the highest person: processing...");
    Ltag theHighestPerson = new LtagBuilder(theHighest)
        .substitution(person, "np")
        .build();
    LOGGER.info("the highest person:\n{}", theHighestPerson.toPrettyString());

    /* who is */
    LOGGER.info("who is: processing...");
    Ltag whoIs = new LtagBuilder(is)
        .substitution(who, "1")
        .build();
    LOGGER.info("who is:\n{}", whoIs.toPrettyString());

    /* who is the highest person */
    LOGGER.info("who is the highest person: processing...");
    Ltag whoIsTheHighestPerson = new LtagBuilder(whoIs)
        .substitution(theHighestPerson, "2")
        .build();
    LOGGER.info("who is the highest person:\n{}", whoIsTheHighestPerson.toPrettyString());

    /* expected */
    LtagNode s = new NonTerminalNode(SyntaxCategory.S);
    LtagNode dp1 = new NonTerminalNode(1, SyntaxCategory.DP);
    LtagNode vp = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode prn = new NonTerminalNode(SyntaxCategory.PRN);
    LtagNode v = new NonTerminalNode(SyntaxCategory.V);
    LtagNode dp2 = new NonTerminalNode(2, SyntaxCategory.DP);
    LtagNode det = new NonTerminalNode(SyntaxCategory.DET);
    LtagNode adj = new NonTerminalNode(SyntaxCategory.ADJ);
    LtagNode np = new NonTerminalNode(SyntaxCategory.NP);
    LtagNode lexWho = new TerminalNode("who");
    LtagNode lexIs = new TerminalNode("is");
    LtagNode lexThe = new TerminalNode("the");
    LtagNode lexHighest = new TerminalNode("highest");
    LtagNode lexPerson = new TerminalNode("person");

    Ltag expected = new SimpleLtag(s);
    expected.addEdge(s, dp1);
    expected.addEdge(s, vp);
    expected.addEdge(dp1, prn);
    expected.addEdge(vp, v);
    expected.addEdge(vp, dp2);
    expected.addEdge(prn, lexWho);
    expected.addEdge(v, lexIs);
    expected.addEdge(dp2, det);
    expected.addEdge(dp2, adj);
    expected.addEdge(dp2, np);
    expected.addEdge(det, lexThe);
    expected.addEdge(adj, lexHighest);
    expected.addEdge(np, lexPerson);

    Assert.assertEquals(expected, whoIsTheHighestPerson);
  }

  /**
   * Example implementing the question: "How many women Albert Einstein married?"
   */
  @Test
  public void test_howMany() throws QueryException, LTAGException {
    /* how many */
    Ltag howMany = LtagTemplates.how("how", "many", "np");
    LOGGER.info("how many:\n{}", howMany.toPrettyString());

    /* women */
    Ltag women = LtagTemplates.classNoun("women", false);
    LOGGER.info("women:\n{}", women.toPrettyString());

    /* did */
    Ltag did = LtagTemplates.questioningDo("did");
    LOGGER.info("did:\n{}", did.toPrettyString());

    /* Albert Einstein */
    Ltag albert = LtagTemplates.properNoun("Albert Einstein");
    LOGGER.info("Albert Einstein:\n{}", albert.toPrettyString());

    /* marry */
    Ltag marry = LtagTemplates.transitiveVerbActiveIndicativeQuestioning("marry", "subj", "obj", "vp");
    LOGGER.info("marry:\n{}", marry.toPrettyString());

    /* how many women */
    LOGGER.info("how many women: processing...");
    Ltag howManyWomen = new LtagBuilder(howMany)
        .substitution(women, "np")
        .build();
    LOGGER.info("how many women:\n{}", howManyWomen.toPrettyString());

    /* Albert Einstein marry */
    LOGGER.info("Albert Einstein marry: processing...");
    Ltag albertMarry = new LtagBuilder(marry)
        .substitution(albert, "subj")
        .build();
    LOGGER.info("Albert Einstein marry:\n{}", albertMarry.toPrettyString());

    /* how many women Albert Einstein marry */
    LOGGER.info("how many women Albert Einstein marry: processing...");
    Ltag howManyWomenAlbertMarry = new LtagBuilder(albertMarry)
        .substitution(howManyWomen, "obj")
        .build();
    LOGGER.info("how many women Albert Einstein marry:\n{}", howManyWomenAlbertMarry.toPrettyString());

    /* how many women did Albert Einstein marry */
    LOGGER.info("how many women did Albert Einstein marry: processing...");
    Ltag howManyWomenDidAlbertMarry = new LtagBuilder(howManyWomenAlbertMarry)
        .adjoin(did, "vp")
        .build();
    LOGGER.info("how many women did Albert Einstein marry:\n{}", howManyWomenDidAlbertMarry.toPrettyString());

    /* expected */
    LtagNode s = new NonTerminalNode(SyntaxCategory.S);
    LtagNode dp1 = new NonTerminalNode(1, SyntaxCategory.DP);
    LtagNode vp1 = new NonTerminalNode(1, SyntaxCategory.VP);
    LtagNode prnp = new NonTerminalNode(SyntaxCategory.PRNP);
    LtagNode np = new NonTerminalNode(SyntaxCategory.NP);
    LtagNode v1 = new NonTerminalNode(1, SyntaxCategory.V);
    LtagNode vp2 = new NonTerminalNode(2, SyntaxCategory.VP, "vp");
    LtagNode adv = new NonTerminalNode(SyntaxCategory.ADV);
    LtagNode prn = new NonTerminalNode(SyntaxCategory.PRN);
    LtagNode dp2 = new NonTerminalNode(2, SyntaxCategory.DP);
    LtagNode v2 = new NonTerminalNode(2, SyntaxCategory.V);
    LtagNode lexHow = new TerminalNode("how");
    LtagNode lexMany = new TerminalNode("many");
    LtagNode lexWomen = new TerminalNode("women");
    LtagNode lexDid = new TerminalNode("did");
    LtagNode lexAlbert = new TerminalNode("Albert Einstein");
    LtagNode lexMarry = new TerminalNode("marry");

    Ltag expected = new SimpleLtag(s);
    expected.addEdge(s, dp1);
    expected.addEdge(s, vp1);
    expected.addEdge(dp1, prnp);
    expected.addEdge(dp1, np);
    expected.addEdge(vp1, v1);
    expected.addEdge(vp1, vp2);
    expected.addEdge(prnp, adv);
    expected.addEdge(prnp, prn);
    expected.addEdge(np, lexWomen);
    expected.addEdge(v1, lexDid);
    expected.addEdge(vp2, dp2);
    expected.addEdge(vp2, v2);
    expected.addEdge(adv, lexHow);
    expected.addEdge(prn, lexMany);
    expected.addEdge(dp2, lexAlbert);
    expected.addEdge(v2, lexMarry);

    Assert.assertEquals(expected, howManyWomenDidAlbertMarry);
  }
}
