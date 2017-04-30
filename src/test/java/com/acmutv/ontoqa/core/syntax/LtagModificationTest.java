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
 * JUnit tests for {@link SimpleLtag} auxiliary modification operations.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Ltag
 * @see SimpleLtag
 */
public class LtagModificationTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(LtagModificationTest.class);

  /**
   * Tests the LTAG append.
   */
  @Test
  public void test_append() throws LTAGException {
    LtagNode wins_nodeS = new NonTerminalNode(SyntaxCategory.S);
    LtagNode wins_nodeDP1 = new NonTerminalNode(SyntaxCategory.DP, LtagNodeMarker.SUB, "myDP1");
    LtagNode wins_nodeVP = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode wins_nodeV = new NonTerminalNode(SyntaxCategory.V);
    LtagNode wins_nodeWins = new TerminalNode("wins");

    Ltag treeWins = new SimpleLtag(wins_nodeS);
    treeWins.addEdge(wins_nodeS, wins_nodeDP1);
    treeWins.addEdge(wins_nodeS, wins_nodeVP);
    treeWins.addEdge(wins_nodeVP, wins_nodeV);
    treeWins.addEdge(wins_nodeV, wins_nodeWins);

    LtagNode the_nodeDP = new NonTerminalNode(SyntaxCategory.DP);
    LtagNode the_nodeDET = new NonTerminalNode(SyntaxCategory.DET);
    LtagNode the_nodeNP = new NonTerminalNode(SyntaxCategory.NP, LtagNodeMarker.SUB,"the_subject");
    LtagNode the_nodeThe = new TerminalNode("the");

    Ltag treeThe = new SimpleLtag(the_nodeDP);
    treeThe.addEdge(the_nodeDP, the_nodeDET);
    treeThe.addEdge(the_nodeDP, the_nodeNP);
    treeThe.addEdge(the_nodeDET, the_nodeThe);

    Ltag actual = treeWins.copy();
    actual.append(wins_nodeVP, treeThe, the_nodeDP, null);

    LtagNode the_nodeDP_renamed = new LtagNode(the_nodeDP);
    the_nodeDP_renamed.setId(2);

    Ltag expected = new SimpleLtag(wins_nodeS);
    expected.addEdge(wins_nodeS, wins_nodeDP1);
    expected.addEdge(wins_nodeS, wins_nodeVP);
    expected.addEdge(wins_nodeVP, wins_nodeV);
    expected.addEdge(wins_nodeVP, the_nodeDP_renamed);
    expected.addEdge(wins_nodeV, wins_nodeWins);
    expected.addEdge(the_nodeDP_renamed, the_nodeDET);
    expected.addEdge(the_nodeDP_renamed, the_nodeNP);
    expected.addEdge(the_nodeDET, the_nodeThe);

    LOGGER.debug(expected.toPrettyString());
    LOGGER.debug(actual.toPrettyString());

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the LTAG replace.
   */
  @Test
  public void test_replace() throws LTAGException {
    LtagNode wins_nodeS = new NonTerminalNode(SyntaxCategory.S);
    LtagNode wins_nodeDP1 = new NonTerminalNode(1, SyntaxCategory.DP, LtagNodeMarker.SUB, "myDP1");
    LtagNode wins_nodeVP = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode wins_nodeV = new NonTerminalNode(SyntaxCategory.V);
    LtagNode wins_nodeDP2 = new NonTerminalNode(2, SyntaxCategory.DP, LtagNodeMarker.SUB, "myDP2");
    LtagNode wins_nodeWins = new TerminalNode("wins");

    Ltag treeWins = new SimpleLtag(wins_nodeS);
    treeWins.addEdge(wins_nodeS, wins_nodeDP1);
    treeWins.addEdge(wins_nodeS, wins_nodeVP);
    treeWins.addEdge(wins_nodeVP, wins_nodeV);
    treeWins.addEdge(wins_nodeVP, wins_nodeDP2);
    treeWins.addEdge(wins_nodeV, wins_nodeWins);

    LtagNode uruguay_nodeDP = new NonTerminalNode(SyntaxCategory.DP);
    LtagNode uruguay_nodeUruguay = new TerminalNode("Uruguay");

    Ltag treeUruguay = new SimpleLtag(uruguay_nodeDP);
    treeUruguay.addEdge(uruguay_nodeDP, uruguay_nodeUruguay);

    Ltag actual = treeWins.copy();
    actual.replace(wins_nodeDP1, treeUruguay, uruguay_nodeDP);

    Ltag expected = new SimpleLtag(wins_nodeS);
    expected.addEdge(wins_nodeS, uruguay_nodeDP);
    expected.addEdge(wins_nodeS, wins_nodeVP);
    expected.addEdge(uruguay_nodeDP, uruguay_nodeUruguay);
    expected.addEdge(wins_nodeVP, wins_nodeV);
    expected.addEdge(wins_nodeVP, wins_nodeDP2);
    expected.addEdge(wins_nodeV, wins_nodeWins);

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the LTAG replaceNodeWithSubtreeRootedIn (depth 1 left).
   */
  @Test
  public void test_replaceNodeWithSubtreeRootedIn_1_left() throws LTAGException {
    LtagNode successful_nodeNP1 = new NonTerminalNode(1, SyntaxCategory.NP);
    LtagNode successful_nodeNP2 = new NonTerminalNode(2, SyntaxCategory.NP, LtagNodeMarker.ADJ);
    LtagNode successful_nodeADJ = new NonTerminalNode(SyntaxCategory.ADJ);
    LtagNode successful_nodeSuccessful = new TerminalNode("successful");

    Ltag treeSuccessful = new SimpleLtag(successful_nodeNP1);
    treeSuccessful.addEdge(successful_nodeNP1, successful_nodeNP2);
    treeSuccessful.addEdge(successful_nodeNP1, successful_nodeADJ);
    treeSuccessful.addEdge(successful_nodeADJ, successful_nodeSuccessful);

    LOGGER.debug("LTAG 'successful':\n{}", treeSuccessful.toPrettyString());

    LtagNode company_nodeNP = new NonTerminalNode(SyntaxCategory.NP);
    LtagNode company_nodeCompany = new TerminalNode("company");

    Ltag treeCompany = new SimpleLtag(company_nodeNP);
    treeCompany.addEdge(company_nodeNP, company_nodeCompany);

    LOGGER.debug("LTAG 'company':\n{}", treeCompany.toPrettyString());

    Ltag actual = treeSuccessful.copy();
    actual.replaceNodeWithSubtreeRootedIn(successful_nodeNP2, treeCompany, company_nodeNP);

    Ltag expected = new SimpleLtag(successful_nodeNP1);
    expected.addEdge(successful_nodeNP1, company_nodeCompany);
    expected.addEdge(successful_nodeNP1, successful_nodeADJ);
    expected.addEdge(successful_nodeADJ, successful_nodeSuccessful);

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the LTAG replaceNodeWithSubtreeRootedIn (depth 1, right).
   */
  @Test
  public void test_replaceNodeWithSubtreeRootedIn_1_right() throws LTAGException {
    LtagNode successful_nodeNP1 = new NonTerminalNode(1, SyntaxCategory.NP);
    LtagNode successful_nodeADJ = new NonTerminalNode(SyntaxCategory.ADJ);
    LtagNode successful_nodeNP2 = new NonTerminalNode(2, SyntaxCategory.NP, LtagNodeMarker.ADJ);
    LtagNode successful_nodeSuccessful = new TerminalNode("successful");

    Ltag treeSuccessful = new SimpleLtag(successful_nodeNP1);
    treeSuccessful.addEdge(successful_nodeNP1, successful_nodeADJ);
    treeSuccessful.addEdge(successful_nodeNP1, successful_nodeNP2);
    treeSuccessful.addEdge(successful_nodeADJ, successful_nodeSuccessful);

    LOGGER.debug("LTAG 'successful':\n{}", treeSuccessful.toPrettyString());

    LtagNode company_nodeNP = new NonTerminalNode(SyntaxCategory.NP);
    LtagNode company_nodeCompany = new TerminalNode("company");

    Ltag treeCompany = new SimpleLtag(company_nodeNP);
    treeCompany.addEdge(company_nodeNP, company_nodeCompany);

    LOGGER.debug("LTAG 'company':\n{}", treeCompany.toPrettyString());

    Ltag actual = treeSuccessful.copy();
    actual.replaceNodeWithSubtreeRootedIn(successful_nodeNP2, treeCompany, company_nodeNP);

    Ltag expected = new SimpleLtag(successful_nodeNP1);
    expected.addEdge(successful_nodeNP1, successful_nodeADJ);
    expected.addEdge(successful_nodeNP1, company_nodeCompany);
    expected.addEdge(successful_nodeADJ, successful_nodeSuccessful);

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the LTAG replaceNodeWithSubtreeRootedIn (depth 2 left).
   */
  @Test
  public void test_replaceNodeWithSubtreeRootedIn_2_left() throws LTAGException {
    LtagNode successful_nodeDP1 = new NonTerminalNode(1, SyntaxCategory.DP);
    LtagNode successful_nodeDP2 = new NonTerminalNode(2, SyntaxCategory.DP, LtagNodeMarker.ADJ);
    LtagNode successful_nodeADJ = new NonTerminalNode(SyntaxCategory.ADJ);
    LtagNode successful_nodeSuccessful = new TerminalNode("successful");

    Ltag treeSuccessful = new SimpleLtag(successful_nodeDP1);
    treeSuccessful.addEdge(successful_nodeDP1, successful_nodeDP2);
    treeSuccessful.addEdge(successful_nodeDP1, successful_nodeADJ);
    treeSuccessful.addEdge(successful_nodeADJ, successful_nodeSuccessful);

    LOGGER.debug("LTAG 'successful':\n{}", treeSuccessful.toPrettyString());

    LtagNode aCompany_nodeDP = new NonTerminalNode(SyntaxCategory.DP);
    LtagNode aCompany_nodeDET = new NonTerminalNode(SyntaxCategory.DET);
    LtagNode aCompany_nodeNP = new NonTerminalNode(SyntaxCategory.NP);
    LtagNode aCompany_nodeA = new TerminalNode("a");
    LtagNode aCompany_nodeCompany = new TerminalNode("company");

    Ltag treeACompany = new SimpleLtag(aCompany_nodeDP);
    treeACompany.addEdge(aCompany_nodeDP, aCompany_nodeDET);
    treeACompany.addEdge(aCompany_nodeDP, aCompany_nodeNP);
    treeACompany.addEdge(aCompany_nodeDET, aCompany_nodeA);
    treeACompany.addEdge(aCompany_nodeNP, aCompany_nodeCompany);

    LOGGER.debug("LTAG 'a company':\n{}", treeACompany.toPrettyString());

    Ltag actual = treeSuccessful.copy();
    actual.replaceNodeWithSubtreeRootedIn(successful_nodeDP2, treeACompany, aCompany_nodeDP);

    Ltag expected = new SimpleLtag(successful_nodeDP1);
    expected.addEdge(successful_nodeDP1, aCompany_nodeDET);
    expected.addEdge(successful_nodeDP1, aCompany_nodeNP);
    expected.addEdge(successful_nodeDP1, successful_nodeADJ);
    expected.addEdge(aCompany_nodeDET, aCompany_nodeA);
    expected.addEdge(aCompany_nodeNP, aCompany_nodeCompany);
    expected.addEdge(successful_nodeADJ, successful_nodeSuccessful);

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the LTAG replaceNodeWithSubtreeRootedIn (depth 2 right).
   */
  @Test
  public void test_replaceNodeWithSubtreeRootedIn_2_right() throws LTAGException {
    LtagNode successful_nodeDP1 = new NonTerminalNode(1, SyntaxCategory.DP);
    LtagNode successful_nodeADJ = new NonTerminalNode(SyntaxCategory.ADJ);
    LtagNode successful_nodeDP2 = new NonTerminalNode(2, SyntaxCategory.DP, LtagNodeMarker.ADJ);
    LtagNode successful_nodeSuccessful = new TerminalNode("successful");

    Ltag treeSuccessful = new SimpleLtag(successful_nodeDP1);
    treeSuccessful.addEdge(successful_nodeDP1, successful_nodeADJ);
    treeSuccessful.addEdge(successful_nodeDP1, successful_nodeDP2);
    treeSuccessful.addEdge(successful_nodeADJ, successful_nodeSuccessful);

    LOGGER.debug("LTAG 'successful':\n{}", treeSuccessful.toPrettyString());

    LtagNode aCompany_nodeDP = new NonTerminalNode(SyntaxCategory.DP);
    LtagNode aCompany_nodeDET = new NonTerminalNode(SyntaxCategory.DET);
    LtagNode aCompany_nodeNP = new NonTerminalNode(SyntaxCategory.NP);
    LtagNode aCompany_nodeA = new TerminalNode("a");
    LtagNode aCompany_nodeCompany = new TerminalNode("company");

    Ltag treeACompany = new SimpleLtag(aCompany_nodeDP);
    treeACompany.addEdge(aCompany_nodeDP, aCompany_nodeDET);
    treeACompany.addEdge(aCompany_nodeDP, aCompany_nodeNP);
    treeACompany.addEdge(aCompany_nodeDET, aCompany_nodeA);
    treeACompany.addEdge(aCompany_nodeNP, aCompany_nodeCompany);

    LOGGER.debug("LTAG 'a company':\n{}", treeACompany.toPrettyString());

    Ltag actual = treeSuccessful.copy();
    actual.replaceNodeWithSubtreeRootedIn(successful_nodeDP2, treeACompany, aCompany_nodeDP);

    Ltag expected = new SimpleLtag(successful_nodeDP1);
    expected.addEdge(successful_nodeDP1, successful_nodeADJ);
    expected.addEdge(successful_nodeDP1, aCompany_nodeDET);
    expected.addEdge(successful_nodeDP1, aCompany_nodeNP);
    expected.addEdge(aCompany_nodeDET, aCompany_nodeA);
    expected.addEdge(aCompany_nodeNP, aCompany_nodeCompany);
    expected.addEdge(successful_nodeADJ, successful_nodeSuccessful);

    Assert.assertEquals(expected, actual);
  }

}
