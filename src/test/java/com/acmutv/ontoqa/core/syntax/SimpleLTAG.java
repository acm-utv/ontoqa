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
 * JUnit tests for {@link com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG
 */
public class SimpleLTAG {

  private static final Logger LOGGER = LogManager.getLogger(SimpleLTAG.class);

  /**
   * Tests the LTAG creation.
   */
  @Test
  public void test_creation() {
    LTAGNode nodeS = new PosNode("anchor:S:1", POS.S);
    LTAGNode nodeDP1 = new PosNode("anchor:DP:1", POS.DP, LTAGNode.Marker.SUB);
    LTAGNode nodeVP = new PosNode("anchor:VP:1", POS.VP);
    LTAGNode nodeV = new PosNode("anchor:V:1", POS.V);
    LTAGNode nodeDP2 = new PosNode("anchor:DP:2", POS.DP, LTAGNode.Marker.SUB);
    LTAGNode nodeWins = new LexicalNode("anchor:LEX:wins", "wins");

    LTAG tree = new com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG(nodeS);
    tree.addProduction(nodeS, nodeDP1);
    tree.addProduction(nodeS, nodeVP);
    tree.addProduction(nodeVP, nodeV);
    tree.addProduction(nodeVP, nodeDP2);
    tree.addProduction(nodeV, nodeWins);

    Assert.assertTrue(tree.isAxiom(nodeS));
    Assert.assertTrue(tree.containsProduction(nodeS, nodeDP1));
    Assert.assertTrue(tree.containsProduction(nodeS, nodeVP));
    Assert.assertTrue(tree.containsProduction(nodeVP, nodeV));
    Assert.assertTrue(tree.containsProduction(nodeVP, nodeDP2));
    Assert.assertTrue(tree.containsProduction(nodeV, nodeWins));

    Assert.assertEquals(new ArrayList<LTAGNode>(){{
      add(nodeDP1);
      add(nodeVP);
    }}, tree.getRhs(nodeS));

    LTAGNode nodeADV = new PosNode("anchor:S:2", POS.ADV, LTAGNode.Marker.SUB);
  }

  @Test
  public void test_getRhs() {
    LTAGNode root = new PosNode("root", POS.S);
    LTAG tree = new com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG(root);
    List<LTAGNode> expectedRhs = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      LTAGNode child = new PosNode("child" + i, POS.NP);
      tree.addProduction(root, child);
      expectedRhs.add(child);
    }

    List<LTAGNode> actualRhs = tree.getRhs(root);

    for (int i = 0; i < expectedRhs.size(); i++) {
      Assert.assertEquals("iteration " + i, expectedRhs.get(i), actualRhs.get(i));
    }
  }

  /**
   * Tests the LTAG copy (whole tree).
   */
  @Test
  public void test_copy() {
    LTAGNode nodeS = new PosNode("anchor:S:1", POS.S);
    LTAGNode nodeDP1 = new PosNode("anchor:DP:1", POS.DP, LTAGNode.Marker.SUB);
    LTAGNode nodeVP = new PosNode("anchor:VP:1", POS.VP);
    LTAGNode nodeV = new PosNode("anchor:V:1", POS.V);
    LTAGNode nodeDP2 = new PosNode("anchor:DP:2", POS.DP, LTAGNode.Marker.SUB);
    LTAGNode nodeWins = new LexicalNode("anchor:LEX:wins", "wins");

    LTAG expected = new com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG(nodeS);
    expected.addProduction(nodeS, nodeDP1);
    expected.addProduction(nodeS, nodeVP);
    expected.addProduction(nodeVP, nodeV);
    expected.addProduction(nodeVP, nodeDP2);
    expected.addProduction(nodeV, nodeWins);

    LTAG actual = expected.copy();

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the LTAG copy (subtree).
   */
  @Test
  public void test_copy_subtree() throws LTAGException {
    LTAGNode nodeS = new PosNode("anchor:S:1", POS.S);
    LTAGNode nodeDP1 = new PosNode("anchor:DP:1", POS.DP, LTAGNode.Marker.SUB);
    LTAGNode nodeVP = new PosNode("anchor:VP:1", POS.VP);
    LTAGNode nodeV = new PosNode("anchor:V:1", POS.V);
    LTAGNode nodeDP2 = new PosNode("anchor:DP:2", POS.DP, LTAGNode.Marker.SUB);
    LTAGNode nodeWins = new LexicalNode("anchor:LEX:wins", "wins");

    LTAG tree = new com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG(nodeS);
    tree.addProduction(nodeS, nodeDP1);
    tree.addProduction(nodeS, nodeVP);
    tree.addProduction(nodeVP, nodeV);
    tree.addProduction(nodeVP, nodeDP2);
    tree.addProduction(nodeV, nodeWins);

    LTAG actual = tree.copy(nodeVP);

    LTAG expected = new com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG(nodeVP);
    expected.addProduction(nodeVP, nodeV);
    expected.addProduction(nodeVP, nodeDP2);
    expected.addProduction(nodeV, nodeWins);

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the LTAG subtree append.
   */
  @Test
  public void test_appendSubtreeFrom() throws LTAGException {
    LTAGNode wins_nodeS = new PosNode("wins:S:1", POS.S);
    LTAGNode wins_nodeDP1 = new PosNode("wins:DP:1", POS.DP, LTAGNode.Marker.SUB);
    LTAGNode wins_nodeVP = new PosNode("wins:VP:1", POS.VP);
    LTAGNode wins_nodeV = new PosNode("wins:V:1", POS.V);
    //LTAGNode wins_nodeDP2 = new PosNode("wins:DP:2", POS.DP, LTAGNode.Marker.SUB);
    LTAGNode wins_nodeWins = new LexicalNode("wins:LEX:wins", "wins");

    LTAG treeWins = new com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG(wins_nodeS);
    treeWins.addProduction(wins_nodeS, wins_nodeDP1);
    treeWins.addProduction(wins_nodeS, wins_nodeVP);
    treeWins.addProduction(wins_nodeVP, wins_nodeV);
    //treeWins.addProduction(wins_nodeVP, wins_nodeDP2);
    treeWins.addProduction(wins_nodeV, wins_nodeWins);

    LTAGNode uruguay_nodeDP = new PosNode("uruguay:DP:1", POS.DP);
    LTAGNode uruguay_nodeUruguay = new LexicalNode("uruguay:LEX:uruguay", "Uruguay");

    LTAG treeUruguay = new com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG(uruguay_nodeDP);
    treeUruguay.addProduction(uruguay_nodeDP, uruguay_nodeUruguay);

    LTAG actual = treeWins.copy();
    actual.appendSubtreeFrom(treeUruguay, uruguay_nodeDP, wins_nodeVP);

    LTAG expected = new com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG(wins_nodeS);
    expected.addProduction(wins_nodeS, wins_nodeDP1);
    expected.addProduction(wins_nodeS, wins_nodeVP);
    expected.addProduction(wins_nodeVP, wins_nodeV);
    expected.addProduction(wins_nodeVP, uruguay_nodeDP);
    expected.addProduction(wins_nodeV, wins_nodeWins);
    expected.addProduction(uruguay_nodeDP, uruguay_nodeUruguay);

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the LTAG subtree replacement.
   */
  @Test
  public void test_replaceWithSubtreeFrom() throws LTAGException {
    LTAGNode wins_nodeS = new PosNode("wins:S:1", POS.S);
    LTAGNode wins_nodeDP1 = new PosNode("wins:DP:1", POS.DP, LTAGNode.Marker.SUB);
    LTAGNode wins_nodeVP = new PosNode("wins:VP:1", POS.VP);
    LTAGNode wins_nodeV = new PosNode("wins:V:1", POS.V);
    LTAGNode wins_nodeDP2 = new PosNode("wins:DP:2", POS.DP, LTAGNode.Marker.SUB);
    LTAGNode wins_nodeWins = new LexicalNode("wins:LEX:wins", "wins");

    LTAG treeWins = new com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG(wins_nodeS);
    treeWins.addProduction(wins_nodeS, wins_nodeDP1);
    treeWins.addProduction(wins_nodeS, wins_nodeVP);
    treeWins.addProduction(wins_nodeVP, wins_nodeV);
    treeWins.addProduction(wins_nodeVP, wins_nodeDP2);
    treeWins.addProduction(wins_nodeV, wins_nodeWins);

    LTAGNode uruguay_nodeDP = new PosNode("uruguay:DP:1", POS.DP);
    LTAGNode uruguay_nodeUruguay = new LexicalNode("uruguay:LEX:uruguay", "Uruguay");

    LTAG treeUruguay = new com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG(uruguay_nodeDP);
    treeUruguay.addProduction(uruguay_nodeDP, uruguay_nodeUruguay);

    LTAG actual = treeWins.copy();
    actual.replaceWithSubtreeFrom(treeUruguay, uruguay_nodeDP, wins_nodeDP1);

    LTAG expected = new com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG(wins_nodeS);
    expected.addProduction(wins_nodeS, uruguay_nodeDP);
    expected.addProduction(wins_nodeS, wins_nodeVP);
    expected.addProduction(uruguay_nodeDP, uruguay_nodeUruguay);
    expected.addProduction(wins_nodeVP, wins_nodeV);
    expected.addProduction(wins_nodeVP, wins_nodeDP2);
    expected.addProduction(wins_nodeV, wins_nodeWins);

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the retrieval of all the substitution nodes.
   */
  @Test
  public void test_getSubstitutionNodes() {
    LTAGNode nodeS = new PosNode("anchor:S:1", POS.S);
    LTAGNode nodeDP1 = new PosNode("anchor:DP:1", POS.DP, LTAGNode.Marker.SUB);
    LTAGNode nodeVP = new PosNode("anchor:VP:1", POS.VP);
    LTAGNode nodeV = new PosNode("anchor:V:1", POS.V);
    LTAGNode nodeDP2 = new PosNode("anchor:DP:2", POS.DP, LTAGNode.Marker.SUB);
    LTAGNode nodeWins = new LexicalNode("anchor:LEX:wins", "wins");

    LTAG tree = new com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG(nodeS);
    tree.addProduction(nodeS, nodeDP1);
    tree.addProduction(nodeS, nodeVP);
    tree.addProduction(nodeVP, nodeV);
    tree.addProduction(nodeVP, nodeDP2);
    tree.addProduction(nodeV, nodeWins);

    List<LTAGNode> actual = tree.getSubstitutionNodes();

    List<LTAGNode> expected = new ArrayList<>();
    expected.add(nodeDP1);
    expected.add(nodeDP2);

    Assert.assertTrue(expected.containsAll(actual) && actual.containsAll(expected));
  }

  /**
   * Tests the retrieval of all the adjunction nodes.
   */
  @Test
  public void test_getAdjunctionNodes() {
    LTAGNode nodeVP1 = new PosNode("anchor:VP:1", POS.VP);
    LTAGNode nodeVP2 = new PosNode("anchor:VP:2", POS.VP, LTAGNode.Marker.ADJ);
    LTAGNode nodeADV = new PosNode("anchor:ADV:1", POS.ADV);
    LTAGNode nodeEasily = new LexicalNode("anchor:LEX:easily", "easily");

    LTAG tree = new com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG(nodeVP1);
    tree.addProduction(nodeVP1, nodeVP2);
    tree.addProduction(nodeVP1, nodeADV);
    tree.addProduction(nodeADV, nodeEasily);

    List<LTAGNode> actual = tree.getAdjunctionNodes();

    List<LTAGNode> expected = new ArrayList<>();
    expected.add(nodeVP2);

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests LTAG pretty string representation.
   */
  @Test
  public void test_toPrettyString() {
    LTAGNode nodeS = new PosNode("anchor:S:1", POS.S);
    LTAGNode nodeDP1 = new PosNode("anchor:DP:1", POS.DP, LTAGNode.Marker.SUB);
    LTAGNode nodeVP = new PosNode("anchor:VP:1", POS.VP);
    LTAGNode nodeV = new PosNode("anchor:V:1", POS.V);
    LTAGNode nodeDP2 = new PosNode("anchor:DP:2", POS.DP, LTAGNode.Marker.SUB);
    LTAGNode nodeWins = new LexicalNode("anchor:LEX:wins", "wins");

    LTAG tree = new com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG(nodeS);
    tree.addProduction(nodeS, nodeDP1);
    tree.addProduction(nodeS, nodeVP);
    tree.addProduction(nodeVP, nodeV);
    tree.addProduction(nodeVP, nodeDP2);
    tree.addProduction(nodeV, nodeWins);

    String actual = tree.toPrettyString();
    String expected = "S->DP^ ; S->VP ; VP->V ; VP->DP^ ; V->wins";

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests LTAG substitution.
   */
  @Test
  public void test_substitution() throws LTAGException {
    /* wins */
    LTAGNode wins_nodeS = new PosNode("wins:S:1", POS.S);
    LTAGNode wins_nodeDP1 = new PosNode("wins:DP:1", POS.DP, LTAGNode.Marker.SUB);
    LTAGNode wins_nodeVP = new PosNode("wins:VP:1", POS.VP);
    LTAGNode wins_nodeV = new PosNode("wins:V:1", POS.V);
    LTAGNode wins_nodeDP2 = new PosNode("wins:DP:2", POS.DP, LTAGNode.Marker.SUB);
    LTAGNode wins_nodeWins = new LexicalNode("wins:LEX:wins", "wins");

    LTAG treeWins = new com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG(wins_nodeS);
    treeWins.addProduction(wins_nodeS, wins_nodeDP1);
    treeWins.addProduction(wins_nodeS, wins_nodeVP);
    treeWins.addProduction(wins_nodeVP, wins_nodeV);
    treeWins.addProduction(wins_nodeVP, wins_nodeDP2);
    treeWins.addProduction(wins_nodeV, wins_nodeWins);

    /* Uruguay */
    LTAGNode uruguay_nodeDP = new PosNode("uruguay:DP:1", POS.DP);
    LTAGNode uruguay_nodeUruguay = new LexicalNode("uruguay:LEX:Uruguay", "Uruguay");

    LTAG treeUruguay = new com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG(uruguay_nodeDP);
    treeUruguay.addProduction(uruguay_nodeDP, uruguay_nodeUruguay);

    /* the */
    LTAGNode the_nodeDP = new PosNode("the:DP:1", POS.DP);
    LTAGNode the_nodeDET = new PosNode("the:DET:1", POS.DET);
    LTAGNode the_nodeNP = new PosNode("the:NP:1", POS.NP, LTAGNode.Marker.SUB);
    LTAGNode the_nodeThe = new LexicalNode("the:LEX:the", "the");

    LTAG treeThe = new com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG(the_nodeDP);
    treeThe.addProduction(the_nodeDP, the_nodeDET);
    treeThe.addProduction(the_nodeDP, the_nodeNP);
    treeThe.addProduction(the_nodeDET, the_nodeThe);

    /* game */
    LTAGNode game_nodeNP = new PosNode("game:NP:1", POS.NP);
    LTAGNode game_nodeGame = new LexicalNode("game:LEX:game", "game");

    LTAG treeGame = new com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG(game_nodeNP);
    treeGame.addProduction(game_nodeNP, game_nodeGame);

    /* Uruguay wins */
    LTAG treeUruguayWins = treeWins.substitution(wins_nodeDP1, treeUruguay);
    LTAG expected_treeUruguayWins = new com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG(wins_nodeS);
    expected_treeUruguayWins.addProduction(wins_nodeS, uruguay_nodeDP);
    expected_treeUruguayWins.addProduction(wins_nodeS, wins_nodeVP);
    expected_treeUruguayWins.addProduction(wins_nodeVP, wins_nodeV);
    expected_treeUruguayWins.addProduction(wins_nodeVP, wins_nodeDP2);
    expected_treeUruguayWins.addProduction(wins_nodeV, wins_nodeWins);
    expected_treeUruguayWins.addProduction(uruguay_nodeDP, uruguay_nodeUruguay);

    LOGGER.info("URUGUAY WINS");
    LOGGER.info(expected_treeUruguayWins.toPrettyString());
    LOGGER.info("---");
    LOGGER.info(treeUruguayWins.toPrettyString());
    LOGGER.info("");
    Assert.assertEquals(expected_treeUruguayWins.toPrettyString(), treeUruguayWins.toPrettyString());

    /* the game */
    LTAG treeTheGame = treeThe.substitution(the_nodeNP, treeGame);
    LTAG expected_treeTheGame = new com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG(the_nodeDP);
    expected_treeTheGame.addProduction(the_nodeDP, the_nodeDET);
    expected_treeTheGame.addProduction(the_nodeDP, game_nodeNP);
    expected_treeTheGame.addProduction(the_nodeDET, the_nodeThe);
    expected_treeTheGame.addProduction(game_nodeNP, game_nodeGame);

    LOGGER.info("THE GAME");
    LOGGER.info(expected_treeTheGame.toPrettyString());
    LOGGER.info("---");
    LOGGER.info(treeTheGame.toPrettyString());
    LOGGER.info("");
    Assert.assertEquals(expected_treeTheGame, treeTheGame);

    /* Uruguay wins the game */
    LTAG treeUruguayWinsTheGame = treeUruguayWins.substitution(wins_nodeDP2, treeTheGame);
    LTAG expected_treeUruguayWinsTheGame = new com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG(wins_nodeS);
    expected_treeUruguayWinsTheGame.addProduction(wins_nodeS, uruguay_nodeDP);
    expected_treeUruguayWinsTheGame.addProduction(wins_nodeS, wins_nodeVP);
    expected_treeUruguayWinsTheGame.addProduction(uruguay_nodeDP, uruguay_nodeUruguay);
    expected_treeUruguayWinsTheGame.addProduction(wins_nodeVP, wins_nodeV);
    expected_treeUruguayWinsTheGame.addProduction(wins_nodeVP, the_nodeDP);
    expected_treeUruguayWinsTheGame.addProduction(wins_nodeV, wins_nodeWins);
    expected_treeUruguayWinsTheGame.addProduction(the_nodeDP, the_nodeDET);
    expected_treeUruguayWinsTheGame.addProduction(the_nodeDP, game_nodeNP);
    expected_treeUruguayWinsTheGame.addProduction(the_nodeDET, the_nodeThe);
    expected_treeUruguayWinsTheGame.addProduction(game_nodeNP, game_nodeGame);

    LOGGER.info("URUGUAY WINS THE GAME");
    LOGGER.info(expected_treeUruguayWinsTheGame.toPrettyString());
    LOGGER.info("---");
    LOGGER.info(treeUruguayWinsTheGame.toPrettyString());
    LOGGER.info("");
    Assert.assertEquals(expected_treeUruguayWinsTheGame, treeUruguayWinsTheGame);
  }

  /**
   * Tests LTAG adjunction.
   */
  @Test
  public void test_adjunction() throws LTAGException {
    /* wins */
    LTAGNode wins_nodeS = new PosNode("wins:S:1", POS.S);
    LTAGNode wins_nodeDP1 = new PosNode("wins:DP:1", POS.DP, LTAGNode.Marker.SUB);
    LTAGNode wins_nodeVP = new PosNode("wins:VP:1", POS.VP);
    LTAGNode wins_nodeV = new PosNode("wins:V:1", POS.V);
    LTAGNode wins_nodeDP2 = new PosNode("wins:DP:2", POS.DP, LTAGNode.Marker.SUB);
    LTAGNode wins_nodeWins = new LexicalNode("wins:LEX:wins", "wins");

    LTAG treeWins = new com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG(wins_nodeS);
    treeWins.addProduction(wins_nodeS, wins_nodeDP1);
    treeWins.addProduction(wins_nodeS, wins_nodeVP);
    treeWins.addProduction(wins_nodeVP, wins_nodeV);
    treeWins.addProduction(wins_nodeVP, wins_nodeDP2);
    treeWins.addProduction(wins_nodeV, wins_nodeWins);

    /* easily */
    LTAGNode easily_nodeVP1 = new PosNode("easily:VP:1", POS.VP);
    LTAGNode easily_nodeVP2 = new PosNode("easily:VP:2", POS.VP, LTAGNode.Marker.ADJ);
    LTAGNode easily_nodeADV = new PosNode("easily:ADV:1", POS.ADV);
    LTAGNode easily_nodeEasily = new LexicalNode("easily:LEX:easily", "easily");

    LTAG treeEasily = new com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG(easily_nodeVP1);
    treeEasily.addProduction(easily_nodeVP1, easily_nodeVP2);
    treeEasily.addProduction(easily_nodeVP1, easily_nodeADV);
    treeEasily.addProduction(easily_nodeADV, easily_nodeEasily);

    LOGGER.info("wins");
    LOGGER.info(treeWins.toPrettyString());
    LOGGER.info("---");
    LOGGER.info("easily");
    LOGGER.info(treeEasily.toPrettyString());
    LOGGER.info("");

    /* wins easily */
    LTAG treeWinsEasily = treeWins.adjunction(wins_nodeVP, treeEasily, easily_nodeVP2);
    LTAG expected_treeWinsEasily = new com.acmutv.ontoqa.core.syntax.ltag.SimpleLTAG(wins_nodeS);
    expected_treeWinsEasily.addProduction(wins_nodeS, wins_nodeDP1);
    expected_treeWinsEasily.addProduction(wins_nodeS, easily_nodeVP1);
    expected_treeWinsEasily.addProduction(easily_nodeVP1, wins_nodeVP);
    expected_treeWinsEasily.addProduction(wins_nodeVP, wins_nodeV);
    expected_treeWinsEasily.addProduction(wins_nodeVP, wins_nodeDP2);
    expected_treeWinsEasily.addProduction(wins_nodeV, wins_nodeWins);
    expected_treeWinsEasily.addProduction(easily_nodeVP1, easily_nodeADV);
    expected_treeWinsEasily.addProduction(easily_nodeADV, easily_nodeEasily);

    LOGGER.info("wins easily");
    LOGGER.info(expected_treeWinsEasily.toPrettyString());
    LOGGER.info("---");
    LOGGER.info(treeWinsEasily.toPrettyString());
    LOGGER.info("");

    Assert.assertEquals(expected_treeWinsEasily, treeWinsEasily);
  }
}
