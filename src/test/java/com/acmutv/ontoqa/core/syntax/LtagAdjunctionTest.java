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

package com.acmutv.ontoqa.core.syntax;

import com.acmutv.ontoqa.core.exception.LTAGException;
import com.acmutv.ontoqa.core.syntax.ltag.*;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JUnit tests for {@link SimpleLtag} adjunction operation.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Ltag
 * @see SimpleLtag
 */
public class LtagAdjunctionTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(LtagAdjunctionTest.class);

  /**
   * Tests Ltag adjunction.
   */
  @Test
  public void test_adjunction() throws LTAGException {
    /* wins */
    LtagNode wins_nodeS = new NonTerminalNode(SyntaxCategory.S);
    LtagNode wins_nodeDP1 = new NonTerminalNode(1, SyntaxCategory.DP, LtagNodeMarker.SUB, "wins:subject");
    LtagNode wins_nodeVP = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode wins_nodeV = new NonTerminalNode(SyntaxCategory.V);
    LtagNode wins_nodeDP2 = new NonTerminalNode(2, SyntaxCategory.DP, LtagNodeMarker.SUB, "wins:object");
    LtagNode wins_nodeWins = new TerminalNode("wins");

    Ltag treeWins = new SimpleLtag(wins_nodeS);
    treeWins.addEdge(wins_nodeS, wins_nodeDP1);
    treeWins.addEdge(wins_nodeS, wins_nodeVP);
    treeWins.addEdge(wins_nodeVP, wins_nodeV);
    treeWins.addEdge(wins_nodeVP, wins_nodeDP2);
    treeWins.addEdge(wins_nodeV, wins_nodeWins);

    LOGGER.info("LTAG 'wins':\n{}", treeWins.toPrettyString());

    /* easily */
    LtagNode easily_nodeVP1 = new NonTerminalNode(1, SyntaxCategory.VP);
    LtagNode easily_nodeVP2 = new NonTerminalNode(2, SyntaxCategory.VP, LtagNodeMarker.ADJ);
    LtagNode easily_nodeADV = new NonTerminalNode(SyntaxCategory.ADV);
    LtagNode easily_nodeEasily = new TerminalNode("easily");

    Ltag treeEasily = new SimpleLtag(easily_nodeVP1);
    treeEasily.addEdge(easily_nodeVP1, easily_nodeVP2);
    treeEasily.addEdge(easily_nodeVP1, easily_nodeADV);
    treeEasily.addEdge(easily_nodeADV, easily_nodeEasily);

    LOGGER.info("LTAG 'easily':\n{}", treeEasily.toPrettyString());

    /* wins easily */
    Ltag treeWinsEasily = treeWins.copy();
    treeWinsEasily.adjunction(treeEasily, wins_nodeVP);

    LtagNode wins_nodeVP_renamed = new LtagNode(wins_nodeVP);
    wins_nodeVP_renamed.setId(2);

    Ltag expected = new SimpleLtag(wins_nodeS);
    expected.addEdge(wins_nodeS, wins_nodeDP1);
    expected.addEdge(wins_nodeS, easily_nodeVP1);
    expected.addEdge(easily_nodeVP1, wins_nodeVP_renamed);
    expected.addEdge(wins_nodeVP_renamed, wins_nodeV);
    expected.addEdge(wins_nodeVP_renamed, wins_nodeDP2);
    expected.addEdge(wins_nodeV, wins_nodeWins);
    expected.addEdge(easily_nodeVP1, easily_nodeADV);
    expected.addEdge(easily_nodeADV, easily_nodeEasily);

    Assert.assertEquals(expected, treeWinsEasily);
  }

  /**
   * Tests Ltag adjunction.
   */
  @Test
  public void test_adjunction2() throws LTAGException {
    /* a company */
    LtagNode acompany_nodeDP = new NonTerminalNode(SyntaxCategory.DP);
    LtagNode acompany_nodeDET = new NonTerminalNode(SyntaxCategory.DET);
    LtagNode acompany_nodeNP = new NonTerminalNode(SyntaxCategory.NP);
    LtagNode acompany_nodeA = new TerminalNode("a");
    LtagNode acompany_nodeCompany = new TerminalNode("company");

    Ltag treeACompany = new SimpleLtag(acompany_nodeDP);
    treeACompany.addEdge(acompany_nodeDP, acompany_nodeDET);
    treeACompany.addEdge(acompany_nodeDP, acompany_nodeNP);
    treeACompany.addEdge(acompany_nodeDET, acompany_nodeA);
    treeACompany.addEdge(acompany_nodeNP, acompany_nodeCompany);

    LOGGER.info("LTAG 'a company':\n{}", treeACompany.toPrettyString());

    /* headquartered in */
    LtagNode headquarteredIn_nodeNP1 = new NonTerminalNode(1, SyntaxCategory.NP);
    LtagNode headquarteredIn_nodeNP2 = new NonTerminalNode(2, SyntaxCategory.NP, LtagNodeMarker.ADJ);
    LtagNode headquarteredIn_nodeADJPP = new NonTerminalNode(SyntaxCategory.ADJPP);
    LtagNode headquarteredIn_nodeADJ = new NonTerminalNode(SyntaxCategory.ADJ);
    LtagNode headquarteredIn_nodePP = new NonTerminalNode(SyntaxCategory.PP);
    LtagNode headquarteredIn_nodeP = new NonTerminalNode(SyntaxCategory.P);
    LtagNode headquarteredIn_nodeDP = new NonTerminalNode(SyntaxCategory.DP, LtagNodeMarker.SUB, "dp");
    LtagNode headquarteredIn_nodeHeadquartered = new TerminalNode("headquartered");
    LtagNode headquarteredIn_nodeIn = new TerminalNode("in");

    Ltag treeHeadquarteredIn = new SimpleLtag(headquarteredIn_nodeNP1);
    treeHeadquarteredIn.addEdge(headquarteredIn_nodeNP1, headquarteredIn_nodeNP2);
    treeHeadquarteredIn.addEdge(headquarteredIn_nodeNP1, headquarteredIn_nodeADJPP);
    treeHeadquarteredIn.addEdge(headquarteredIn_nodeADJPP, headquarteredIn_nodeADJ);
    treeHeadquarteredIn.addEdge(headquarteredIn_nodeADJPP, headquarteredIn_nodePP);
    treeHeadquarteredIn.addEdge(headquarteredIn_nodeADJ, headquarteredIn_nodeHeadquartered);
    treeHeadquarteredIn.addEdge(headquarteredIn_nodePP, headquarteredIn_nodeP);
    treeHeadquarteredIn.addEdge(headquarteredIn_nodePP, headquarteredIn_nodeDP);
    treeHeadquarteredIn.addEdge(headquarteredIn_nodeP, headquarteredIn_nodeIn);

    LOGGER.info("LTAG 'headquartered in':\n{}", treeHeadquarteredIn.toPrettyString());

    /* a company headquartered in */
    Ltag treeACompanyHeadquarteredIn = treeACompany.copy();
    treeACompanyHeadquarteredIn.adjunction(treeHeadquarteredIn, acompany_nodeNP);

    LtagNode acompany_nodeNP_renamed = new LtagNode(acompany_nodeNP);
    acompany_nodeNP_renamed.setId(2);

    LtagNode headquarteredIn_nodeDP_renamed = new LtagNode(headquarteredIn_nodeDP);
    headquarteredIn_nodeDP_renamed.setId(2);

    Ltag expected = new SimpleLtag(acompany_nodeDP);
    expected.addEdge(acompany_nodeDP, acompany_nodeDET);
    expected.addEdge(acompany_nodeDP, headquarteredIn_nodeNP1);
    expected.addEdge(acompany_nodeDET, acompany_nodeA);
    expected.addEdge(headquarteredIn_nodeNP1, acompany_nodeNP_renamed);
    expected.addEdge(headquarteredIn_nodeNP1, headquarteredIn_nodeADJPP);
    expected.addEdge(acompany_nodeNP_renamed, acompany_nodeCompany);
    expected.addEdge(headquarteredIn_nodeADJPP, headquarteredIn_nodeADJ);
    expected.addEdge(headquarteredIn_nodeADJPP, headquarteredIn_nodePP);
    expected.addEdge(headquarteredIn_nodeADJ, headquarteredIn_nodeHeadquartered);
    expected.addEdge(headquarteredIn_nodePP, headquarteredIn_nodeP);
    expected.addEdge(headquarteredIn_nodePP, headquarteredIn_nodeDP_renamed);
    expected.addEdge(headquarteredIn_nodeP, headquarteredIn_nodeIn);

    Assert.assertEquals(expected, treeACompanyHeadquarteredIn);
  }

}
