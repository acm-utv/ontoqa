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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * JUnit tests for {@link SimpleLtag}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Ltag
 * @see SimpleLtag
 */
public class LtagTest {

  private static final Logger LOGGER = LogManager.getLogger(LtagTest.class);

  /**
   * Tests the Ltag creation.
   */
  @Test
  public void test_creation() {
    LtagNode nodeS = new NonTerminalNode(SyntaxCategory.S);
    LtagNode nodeDP1 = new NonTerminalNode(1, SyntaxCategory.DP, LtagNodeMarker.SUB, "myDP1");
    LtagNode nodeVP = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode nodeV = new NonTerminalNode(SyntaxCategory.V);
    LtagNode nodeDP2 = new NonTerminalNode(2, SyntaxCategory.DP, LtagNodeMarker.SUB, "myDP2");
    LtagNode nodeWins = new TerminalNode("wins");

    Ltag tree = new SimpleLtag(nodeS);
    tree.addEdge(nodeS, nodeDP1);
    tree.addEdge(nodeS, nodeVP);
    tree.addEdge(nodeVP, nodeV);
    tree.addEdge(nodeVP, nodeDP2);
    tree.addEdge(nodeV, nodeWins);

    Assert.assertTrue(tree.isRoot(nodeS));
    Assert.assertTrue(tree.contains(nodeS, nodeDP1));
    Assert.assertTrue(tree.contains(nodeS, nodeVP));
    Assert.assertTrue(tree.contains(nodeVP, nodeV));
    Assert.assertTrue(tree.contains(nodeVP, nodeDP2));
    Assert.assertTrue(tree.contains(nodeV, nodeWins));

    Assert.assertEquals(new ArrayList<LtagNode>(){{
      add(nodeDP1);
      add(nodeVP);
    }}, tree.getRhs(nodeS));
  }

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

    LOGGER.info("wins");
    LOGGER.info(treeWins.toPrettyString());

    /* easily */
    LtagNode easily_nodeVP1 = new NonTerminalNode(1, SyntaxCategory.VP);
    LtagNode easily_nodeVP2 = new NonTerminalNode(2, SyntaxCategory.VP, LtagNodeMarker.ADJ);
    LtagNode easily_nodeADV = new NonTerminalNode(SyntaxCategory.ADV);
    LtagNode easily_nodeEasily = new TerminalNode("easily");

    Ltag treeEasily = new SimpleLtag(easily_nodeVP1);
    treeEasily.addEdge(easily_nodeVP1, easily_nodeVP2);
    treeEasily.addEdge(easily_nodeVP1, easily_nodeADV);
    treeEasily.addEdge(easily_nodeADV, easily_nodeEasily);

    LOGGER.info("easily");
    LOGGER.info(treeEasily.toPrettyString());

    /* wins easily */
    Ltag treeWinsEasily = treeWins.copy();
    treeWinsEasily.adjunction(treeEasily, wins_nodeVP);

    LtagNode wins_nodeVP_renamed = new LtagNode(wins_nodeVP);
    wins_nodeVP_renamed.setId(2);

    Ltag expected_treeWinsEasily = new SimpleLtag(wins_nodeS);
    expected_treeWinsEasily.addEdge(wins_nodeS, wins_nodeDP1);
    expected_treeWinsEasily.addEdge(wins_nodeS, easily_nodeVP1);
    expected_treeWinsEasily.addEdge(easily_nodeVP1, wins_nodeVP_renamed);
    expected_treeWinsEasily.addEdge(wins_nodeVP_renamed, wins_nodeV);
    expected_treeWinsEasily.addEdge(wins_nodeVP_renamed, wins_nodeDP2);
    expected_treeWinsEasily.addEdge(wins_nodeV, wins_nodeWins);
    expected_treeWinsEasily.addEdge(easily_nodeVP1, easily_nodeADV);
    expected_treeWinsEasily.addEdge(easily_nodeADV, easily_nodeEasily);

    LOGGER.info("wins easily");
    LOGGER.info(treeWinsEasily.toPrettyString());

    Assert.assertEquals(expected_treeWinsEasily, treeWinsEasily);
  }

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
   * Tests the LTAG clone.
   */
  @Test
  public void test_clone() {
    LtagNode nodeS = new NonTerminalNode(SyntaxCategory.S);
    LtagNode nodeDP1 = new NonTerminalNode(1, SyntaxCategory.DP, LtagNodeMarker.SUB, "myDP1");
    LtagNode nodeVP = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode nodeV = new NonTerminalNode(SyntaxCategory.V);
    LtagNode nodeDP2 = new NonTerminalNode(2, SyntaxCategory.DP, LtagNodeMarker.SUB, "myDP2");
    LtagNode nodeWins = new TerminalNode("wins");

    Ltag expected = new SimpleLtag(nodeS);
    expected.addEdge(nodeS, nodeDP1);
    expected.addEdge(nodeS, nodeVP);
    expected.addEdge(nodeVP, nodeV);
    expected.addEdge(nodeVP, nodeDP2);
    expected.addEdge(nodeV, nodeWins);

    Ltag actual = new SimpleLtag(expected);

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the LTAG copy (while tree, equivalent to clone).
   */
  @Test
  public void test_copy_whole() {
    LtagNode nodeS = new NonTerminalNode(SyntaxCategory.S);
    LtagNode nodeDP1 = new NonTerminalNode(1, SyntaxCategory.DP, LtagNodeMarker.SUB, "myDP1");
    LtagNode nodeVP = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode nodeV = new NonTerminalNode(SyntaxCategory.V);
    LtagNode nodeDP2 = new NonTerminalNode(2, SyntaxCategory.DP, LtagNodeMarker.SUB, "myDP2");
    LtagNode nodeWins = new TerminalNode("wins");

    Ltag expected = new SimpleLtag(nodeS);
    expected.addEdge(nodeS, nodeDP1);
    expected.addEdge(nodeS, nodeVP);
    expected.addEdge(nodeVP, nodeV);
    expected.addEdge(nodeVP, nodeDP2);
    expected.addEdge(nodeV, nodeWins);

    Ltag actual = expected.copy();

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the Ltag copy (subtree).
   */
  @Test
  public void test_copy_subtree() throws LTAGException {
    LtagNode nodeS = new NonTerminalNode(SyntaxCategory.S);
    LtagNode nodeDP1 = new NonTerminalNode(1, SyntaxCategory.DP, LtagNodeMarker.SUB, "myDP1");
    LtagNode nodeVP = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode nodeV = new NonTerminalNode(SyntaxCategory.V);
    LtagNode nodeDP2 = new NonTerminalNode(2, SyntaxCategory.DP, LtagNodeMarker.SUB, "myDP2");
    LtagNode nodeWins = new TerminalNode("wins");

    Ltag tree = new SimpleLtag(nodeS);
    tree.addEdge(nodeS, nodeDP1);
    tree.addEdge(nodeS, nodeVP);
    tree.addEdge(nodeVP, nodeV);
    tree.addEdge(nodeVP, nodeDP2);
    tree.addEdge(nodeV, nodeWins);

    Ltag actual = tree.copy(nodeVP);

    Ltag expected = new SimpleLtag(nodeVP);
    expected.addEdge(nodeVP, nodeV);
    expected.addEdge(nodeVP, nodeDP2);
    expected.addEdge(nodeV, nodeWins);

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the node matching starting at some given lexical point.
   */
  @Test
  public void test_firstMatch() {
    LtagNode nodeS = new NonTerminalNode(SyntaxCategory.S);
    LtagNode nodeDP1 = new NonTerminalNode(1, SyntaxCategory.DP, LtagNodeMarker.SUB, "myDP1");
    LtagNode nodeVP = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode nodeV = new NonTerminalNode(SyntaxCategory.V);
    LtagNode nodeDP2 = new NonTerminalNode(2, SyntaxCategory.DP, LtagNodeMarker.SUB, "myDP2");
    LtagNode nodeWins = new TerminalNode("wins");

    Ltag tree = new SimpleLtag(nodeS);
    tree.addEdge(nodeS, nodeDP1);
    tree.addEdge(nodeS, nodeVP);
    tree.addEdge(nodeVP, nodeV);
    tree.addEdge(nodeVP, nodeDP2);
    tree.addEdge(nodeV, nodeWins);

    LtagNode actual1 = tree.firstMatch(SyntaxCategory.DP, "wins");

    Assert.assertEquals(nodeDP2, actual1);

    LtagNode actual2 = tree.firstMatch(SyntaxCategory.VP, "wins");

    Assert.assertEquals(nodeVP, actual2);

    LtagNode actual3 = tree.firstMatch(SyntaxCategory.ADJ, "wins");

    Assert.assertNull(actual3);
  }

  /**
   * Tests the labeled node retrieval.
   */
  @Test
  public void test_getNodeByLabel() {
    LtagNode nodeS = new NonTerminalNode(SyntaxCategory.S);
    LtagNode nodeDP1 = new NonTerminalNode(1, SyntaxCategory.DP, LtagNodeMarker.SUB, "myDP1");
    LtagNode nodeVP = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode nodeV = new NonTerminalNode(SyntaxCategory.V);
    LtagNode nodeDP2 = new NonTerminalNode(2, SyntaxCategory.DP, LtagNodeMarker.SUB, "myDP2");
    LtagNode nodeWins = new TerminalNode("wins");

    Ltag tree = new SimpleLtag(nodeS);
    tree.addEdge(nodeS, nodeDP1);
    tree.addEdge(nodeS, nodeVP);
    tree.addEdge(nodeVP, nodeV);
    tree.addEdge(nodeVP, nodeDP2);
    tree.addEdge(nodeV, nodeWins);

    Assert.assertEquals(nodeDP1, tree.getNode("myDP1"));
    Assert.assertEquals(nodeDP2, tree.getNode("myDP2"));
    Assert.assertEquals(null, tree.getNode("myDP3"));
  }

  /**
   * Tests the RHS retrieval.
   */
  @Test
  public void test_getRhs() {
    LtagNode root = new NonTerminalNode(SyntaxCategory.S);
    Ltag tree = new SimpleLtag(root);
    List<LtagNode> expectedRhs = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      LtagNode child = new NonTerminalNode(i, SyntaxCategory.NP);
      tree.addEdge(root, child);
      expectedRhs.add(child);
    }

    List<LtagNode> actualRhs = tree.getRhs(root);

    for (int i = 0; i < expectedRhs.size(); i++) {
      Assert.assertEquals("iteration " + i, expectedRhs.get(i), actualRhs.get(i));
    }
  }

  /**
   * Tests the retrieval of all the adjunction nodes.
   */
  @Test
  public void test_getAdjunctionNodes() {
    LtagNode nodeVP1 = new NonTerminalNode(1, SyntaxCategory.VP);
    LtagNode nodeVP2 = new NonTerminalNode(2, SyntaxCategory.VP, LtagNodeMarker.ADJ, "myVP2");
    LtagNode nodeADV = new NonTerminalNode(SyntaxCategory.ADV);
    LtagNode nodeEasily = new TerminalNode("easily");

    Ltag tree = new SimpleLtag(nodeVP1);
    tree.addEdge(nodeVP1, nodeVP2);
    tree.addEdge(nodeVP1, nodeADV);
    tree.addEdge(nodeADV, nodeEasily);

    List<LtagNode> actual = tree.getNodes(LtagNodeMarker.ADJ);

    List<LtagNode> expected = new ArrayList<>();
    expected.add(nodeVP2);

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the retrieval of all the substitution nodes.
   */
  @Test
  public void test_getSubstitutionNodes() {
    LtagNode nodeS = new NonTerminalNode(SyntaxCategory.S);
    LtagNode nodeDP1 = new NonTerminalNode(1, SyntaxCategory.DP, LtagNodeMarker.SUB, "myDP1");
    LtagNode nodeVP = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode nodeV = new NonTerminalNode(SyntaxCategory.V);
    LtagNode nodeDP2 = new NonTerminalNode(2, SyntaxCategory.DP, LtagNodeMarker.SUB, "myDP2");
    LtagNode nodeWins = new TerminalNode("wins");

    Ltag tree = new SimpleLtag(nodeS);
    tree.addEdge(nodeS, nodeDP1);
    tree.addEdge(nodeS, nodeVP);
    tree.addEdge(nodeVP, nodeV);
    tree.addEdge(nodeVP, nodeDP2);
    tree.addEdge(nodeV, nodeWins);

    List<LtagNode> actual = tree.getNodes(LtagNodeMarker.SUB);

    List<LtagNode> expected = new ArrayList<>();
    expected.add(nodeDP1);
    expected.add(nodeDP2);

    Assert.assertTrue(expected.containsAll(actual) && actual.containsAll(expected));
  }

  /**
   * Tests the adjunction check (true).
   */
  @Test
  public void test_isAdjunctable_true() {
    LtagNode nodeVP1 = new NonTerminalNode(1, SyntaxCategory.VP);
    LtagNode nodeV = new NonTerminalNode(SyntaxCategory.V);
    LtagNode nodeVP2 = new NonTerminalNode(2, SyntaxCategory.VP, LtagNodeMarker.ADJ);
    LtagNode nodeDid = new TerminalNode("did");

    Ltag tree = new SimpleLtag(nodeVP1);
    tree.addEdge(nodeVP1, nodeV);
    tree.addEdge(nodeVP1, nodeVP2);
    tree.addEdge(nodeV, nodeDid);

    Assert.assertTrue(tree.isAdjunctable());
  }

  /**
   * Tests the adjunction check (false).
   */
  @Test
  public void test_isAdjunctable_false() {
    LtagNode nodeS = new NonTerminalNode(SyntaxCategory.S);
    LtagNode nodeDP1 = new NonTerminalNode(1, SyntaxCategory.DP, LtagNodeMarker.SUB, "myDP1");
    LtagNode nodeVP = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode nodeV = new NonTerminalNode(SyntaxCategory.V);
    LtagNode nodeDP2 = new NonTerminalNode(2, SyntaxCategory.DP, LtagNodeMarker.SUB, "myDP2");
    LtagNode nodeWins = new TerminalNode("wins");

    Ltag tree = new SimpleLtag(nodeS);
    tree.addEdge(nodeS, nodeDP1);
    tree.addEdge(nodeS, nodeVP);
    tree.addEdge(nodeVP, nodeV);
    tree.addEdge(nodeVP, nodeDP2);
    tree.addEdge(nodeV, nodeWins);

    Assert.assertFalse(tree.isAdjunctable());
  }

  /**
   * Tests the left substitution check (true).
   */
  @Test
  public void test_isLeftSub_true() {
    LtagNode nodeS = new NonTerminalNode(SyntaxCategory.S);
    LtagNode nodeDP1 = new NonTerminalNode(1, SyntaxCategory.DP, LtagNodeMarker.SUB, "myDP1");
    LtagNode nodeVP = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode nodeV = new NonTerminalNode(SyntaxCategory.V);
    LtagNode nodeDP2 = new NonTerminalNode(2, SyntaxCategory.DP, LtagNodeMarker.SUB, "myDP2");
    LtagNode nodeWins = new TerminalNode("wins");

    Ltag tree = new SimpleLtag(nodeS);
    tree.addEdge(nodeS, nodeDP1);
    tree.addEdge(nodeS, nodeVP);
    tree.addEdge(nodeVP, nodeV);
    tree.addEdge(nodeVP, nodeDP2);
    tree.addEdge(nodeV, nodeWins);

    Assert.assertTrue(tree.isLeftSub());
  }

  /**
   * Tests the left substitution check (false).
   */
  @Test
  public void test_isLeftSub_false() {
    LtagNode nodeDP = new NonTerminalNode(SyntaxCategory.DP);
    LtagNode nodeDET = new NonTerminalNode(SyntaxCategory.DET);
    LtagNode nodeNP = new NonTerminalNode(SyntaxCategory.NP, LtagNodeMarker.SUB, "noun");
    LtagNode nodeThe = new TerminalNode("the");

    Ltag tree = new SimpleLtag(nodeDP);
    tree.addEdge(nodeDP, nodeDET);
    tree.addEdge(nodeDP, nodeNP);
    tree.addEdge(nodeDET, nodeThe);

    Assert.assertFalse(tree.isLeftSub());
  }

  /**
   * Tests the sentence check (true).
   */
  @Test
  public void test_isSentence_true() {
    LtagNode nodeS = new NonTerminalNode(SyntaxCategory.S);
    LtagNode nodeDP1 = new NonTerminalNode(1, SyntaxCategory.DP, LtagNodeMarker.SUB, "myDP1");
    LtagNode nodeVP = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode nodeV = new NonTerminalNode(SyntaxCategory.V);
    LtagNode nodeDP2 = new NonTerminalNode(2, SyntaxCategory.DP, LtagNodeMarker.SUB, "myDP2");
    LtagNode nodeWins = new TerminalNode("wins");

    Ltag tree = new SimpleLtag(nodeS);
    tree.addEdge(nodeS, nodeDP1);
    tree.addEdge(nodeS, nodeVP);
    tree.addEdge(nodeVP, nodeV);
    tree.addEdge(nodeVP, nodeDP2);
    tree.addEdge(nodeV, nodeWins);

    Assert.assertTrue(tree.isSentence());
  }

  /**
   * Tests the sentence check (false).
   */
  @Test
  public void test_isSentence_false() {
    LtagNode nodeVP1 = new NonTerminalNode(1, SyntaxCategory.VP);
    LtagNode nodeV = new NonTerminalNode(SyntaxCategory.V);
    LtagNode nodeVP2 = new NonTerminalNode(2, SyntaxCategory.VP, LtagNodeMarker.ADJ);
    LtagNode nodeDid = new TerminalNode("did");

    Ltag tree = new SimpleLtag(nodeVP1);
    tree.addEdge(nodeVP1, nodeV);
    tree.addEdge(nodeVP1, nodeVP2);
    tree.addEdge(nodeV, nodeDid);

    Assert.assertFalse(tree.isSentence());
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
   * Tests LTAG substitution.
   */
  @Test
  public void test_substitution() throws LTAGException {
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

    /* Uruguay */
    LtagNode uruguay_nodeDP = new NonTerminalNode(SyntaxCategory.DP);
    LtagNode uruguay_nodeUruguay = new TerminalNode("Uruguay");

    Ltag treeUruguay = new SimpleLtag(uruguay_nodeDP);
    treeUruguay.addEdge(uruguay_nodeDP, uruguay_nodeUruguay);

    /* the */
    LtagNode the_nodeDP = new NonTerminalNode(SyntaxCategory.DP);
    LtagNode the_nodeDET = new NonTerminalNode(SyntaxCategory.DET);
    LtagNode the_nodeNP = new NonTerminalNode(SyntaxCategory.NP, LtagNodeMarker.SUB, "the:subject");
    LtagNode the_nodeThe = new TerminalNode("the");

    Ltag treeThe = new SimpleLtag(the_nodeDP);
    treeThe.addEdge(the_nodeDP, the_nodeDET);
    treeThe.addEdge(the_nodeDP, the_nodeNP);
    treeThe.addEdge(the_nodeDET, the_nodeThe);

    /* game */
    LtagNode game_nodeNP = new NonTerminalNode(SyntaxCategory.NP);
    LtagNode game_nodeGame = new TerminalNode("game");

    Ltag treeGame = new SimpleLtag(game_nodeNP);
    treeGame.addEdge(game_nodeNP, game_nodeGame);

    /* Uruguay wins */
    Ltag treeUruguayWins = treeWins.copy();
    treeUruguayWins.substitution(treeUruguay, "wins:subject");
    Ltag expected_treeUruguayWins = new SimpleLtag(wins_nodeS);
    expected_treeUruguayWins.addEdge(wins_nodeS, uruguay_nodeDP);
    expected_treeUruguayWins.addEdge(wins_nodeS, wins_nodeVP);
    expected_treeUruguayWins.addEdge(wins_nodeVP, wins_nodeV);
    expected_treeUruguayWins.addEdge(wins_nodeVP, wins_nodeDP2);
    expected_treeUruguayWins.addEdge(wins_nodeV, wins_nodeWins);
    expected_treeUruguayWins.addEdge(uruguay_nodeDP, uruguay_nodeUruguay);

    LOGGER.info("URUGUAY WINS");
    LOGGER.info(treeUruguayWins.toPrettyString());
    Assert.assertEquals(expected_treeUruguayWins.toPrettyString(), treeUruguayWins.toPrettyString());

    /* the game */
    Ltag treeTheGame = treeThe.copy();
    treeTheGame.substitution(treeGame, "the:subject");
    Ltag expected_treeTheGame = new SimpleLtag(the_nodeDP);
    expected_treeTheGame.addEdge(the_nodeDP, the_nodeDET);
    expected_treeTheGame.addEdge(the_nodeDP, game_nodeNP);
    expected_treeTheGame.addEdge(the_nodeDET, the_nodeThe);
    expected_treeTheGame.addEdge(game_nodeNP, game_nodeGame);

    LOGGER.info("THE GAME");
    LOGGER.info(treeTheGame.toPrettyString());
    Assert.assertEquals(expected_treeTheGame, treeTheGame);

    /* Uruguay wins the game */
    Ltag treeUruguayWinsTheGame = treeUruguayWins.copy();
    treeUruguayWinsTheGame.substitution(treeTheGame, "wins:object");

    LtagNode the_nodeDP_renamed = new LtagNode(the_nodeDP);
    the_nodeDP_renamed.setId(2);

    Ltag expected_treeUruguayWinsTheGame = new SimpleLtag(wins_nodeS);
    expected_treeUruguayWinsTheGame.addEdge(wins_nodeS, uruguay_nodeDP);
    expected_treeUruguayWinsTheGame.addEdge(wins_nodeS, wins_nodeVP);
    expected_treeUruguayWinsTheGame.addEdge(uruguay_nodeDP, uruguay_nodeUruguay);
    expected_treeUruguayWinsTheGame.addEdge(wins_nodeVP, wins_nodeV);
    expected_treeUruguayWinsTheGame.addEdge(wins_nodeVP, the_nodeDP_renamed);
    expected_treeUruguayWinsTheGame.addEdge(wins_nodeV, wins_nodeWins);
    expected_treeUruguayWinsTheGame.addEdge(the_nodeDP_renamed, the_nodeDET);
    expected_treeUruguayWinsTheGame.addEdge(the_nodeDP_renamed, game_nodeNP);
    expected_treeUruguayWinsTheGame.addEdge(the_nodeDET, the_nodeThe);
    expected_treeUruguayWinsTheGame.addEdge(game_nodeNP, game_nodeGame);

    LOGGER.info("URUGUAY WINS THE GAME");
    LOGGER.info(treeUruguayWinsTheGame.toPrettyString());
    Assert.assertEquals(expected_treeUruguayWinsTheGame, treeUruguayWinsTheGame);
  }

  /**
   * Tests Ltag pretty string representation.
   */
  @Test
  public void test_toPrettyString() {
    LtagNode nodeS = new NonTerminalNode(SyntaxCategory.S);
    LtagNode nodeDP1 = new NonTerminalNode(1, SyntaxCategory.DP, LtagNodeMarker.SUB, "myDP1");
    LtagNode nodeVP = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode nodeV = new NonTerminalNode(SyntaxCategory.V);
    LtagNode nodeDP2 = new NonTerminalNode(2, SyntaxCategory.DP, LtagNodeMarker.SUB, "myDP2");
    LtagNode nodeWins = new TerminalNode("wins");

    Ltag tree = new SimpleLtag(nodeS);
    tree.addEdge(nodeS, nodeDP1);
    tree.addEdge(nodeS, nodeVP);
    tree.addEdge(nodeVP, nodeV);
    tree.addEdge(nodeVP, nodeDP2);
    tree.addEdge(nodeV, nodeWins);

    String actual = tree.toPrettyString();
    String expected = "S->DP^(myDP1) ; S->VP ; VP->V ; VP->DP2^(myDP2) ; V->'wins'";

    Assert.assertEquals(expected, actual);
  }

}
