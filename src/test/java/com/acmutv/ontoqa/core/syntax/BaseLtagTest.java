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
import com.acmutv.ontoqa.core.semantics.dudes.Dudes;
import com.acmutv.ontoqa.core.semantics.dudes.DudesTemplates;
import com.acmutv.ontoqa.core.semantics.sltag.BaseSemanticLtag;
import com.acmutv.ontoqa.core.semantics.sltag.SemanticLtag;
import com.acmutv.ontoqa.core.syntax.ltag.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * JUnit tests for {@link BaseLtag}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see BaseLtag
 */
public class BaseLtagTest {

  private static final Logger LOGGER = LogManager.getLogger(BaseLtagTest.class);

  /**
   * Tests the LTAG pretty string representation.
   */
  @Test
  public void test_prettyString() {
    /* LTAG */
    Ltag ltag = LtagTemplates.properNoun("Uruguay");

    String pretty = ltag.toPrettyString();

    LOGGER.debug("LTAG pretty representation:\n{}", pretty);
  }

  /**
   * Tests the Ltag creation.
   */
  @Test
  public void test_creation() {
    LtagNode nodeS = new PosNode("S:1", POS.S);
    LtagNode nodeDP1 = new PosNode("DP:1", POS.DP, LtagNode.Marker.SUB);
    LtagNode nodeVP = new PosNode("VP:1", POS.VP);
    LtagNode nodeV = new PosNode("V:1", POS.V);
    LtagNode nodeDP2 = new PosNode("DP:2", POS.DP, LtagNode.Marker.SUB);
    LtagNode nodeWins = new LexicalNode("LEX:wins", "wins");

    Ltag tree = new BaseLtag(nodeS);
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

    Assert.assertEquals(new ArrayList<LtagNode>(){{
      add(nodeDP1);
      add(nodeVP);
    }}, tree.getRhs(nodeS));
  }

  @Test
  public void test_getRhs() {
    LtagNode root = new PosNode("root", POS.S);
    Ltag tree = new BaseLtag(root);
    List<LtagNode> expectedRhs = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      LtagNode child = new PosNode("child" + i, POS.NP);
      tree.addProduction(root, child);
      expectedRhs.add(child);
    }

    List<LtagNode> actualRhs = tree.getRhs(root);

    for (int i = 0; i < expectedRhs.size(); i++) {
      Assert.assertEquals("iteration " + i, expectedRhs.get(i), actualRhs.get(i));
    }
  }

  /**
   * Tests the Ltag clone.
   */
  @Test
  public void test_clone() {
    LtagNode nodeS = new PosNode("anchor:S:1", POS.S);
    LtagNode nodeDP1 = new PosNode("anchor:DP:1", POS.DP, LtagNode.Marker.SUB);
    LtagNode nodeVP = new PosNode("anchor:VP:1", POS.VP);
    LtagNode nodeV = new PosNode("anchor:V:1", POS.V);
    LtagNode nodeDP2 = new PosNode("anchor:DP:2", POS.DP, LtagNode.Marker.SUB);
    LtagNode nodeWins = new LexicalNode("anchor:LEX:wins", "wins");

    Ltag expected = new BaseLtag(nodeS);
    expected.addProduction(nodeS, nodeDP1);
    expected.addProduction(nodeS, nodeVP);
    expected.addProduction(nodeVP, nodeV);
    expected.addProduction(nodeVP, nodeDP2);
    expected.addProduction(nodeV, nodeWins);

    Ltag actual = new BaseLtag(expected);

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the Ltag copy (whole tree).
   */
  @Test
  public void test_copy() {
    LtagNode nodeS = new PosNode("anchor:S:1", POS.S);
    LtagNode nodeDP1 = new PosNode("anchor:DP:1", POS.DP, LtagNode.Marker.SUB);
    LtagNode nodeVP = new PosNode("anchor:VP:1", POS.VP);
    LtagNode nodeV = new PosNode("anchor:V:1", POS.V);
    LtagNode nodeDP2 = new PosNode("anchor:DP:2", POS.DP, LtagNode.Marker.SUB);
    LtagNode nodeWins = new LexicalNode("anchor:LEX:wins", "wins");

    Ltag expected = new BaseLtag(nodeS);
    expected.addProduction(nodeS, nodeDP1);
    expected.addProduction(nodeS, nodeVP);
    expected.addProduction(nodeVP, nodeV);
    expected.addProduction(nodeVP, nodeDP2);
    expected.addProduction(nodeV, nodeWins);

    Ltag actual = expected.copy();

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the Ltag copy (subtree).
   */
  @Test
  public void test_copy_subtree() throws LTAGException {
    LtagNode nodeS = new PosNode("anchor:S:1", POS.S);
    LtagNode nodeDP1 = new PosNode("anchor:DP:1", POS.DP, LtagNode.Marker.SUB);
    LtagNode nodeVP = new PosNode("anchor:VP:1", POS.VP);
    LtagNode nodeV = new PosNode("anchor:V:1", POS.V);
    LtagNode nodeDP2 = new PosNode("anchor:DP:2", POS.DP, LtagNode.Marker.SUB);
    LtagNode nodeWins = new LexicalNode("anchor:LEX:wins", "wins");

    Ltag tree = new BaseLtag(nodeS);
    tree.addProduction(nodeS, nodeDP1);
    tree.addProduction(nodeS, nodeVP);
    tree.addProduction(nodeVP, nodeV);
    tree.addProduction(nodeVP, nodeDP2);
    tree.addProduction(nodeV, nodeWins);

    Ltag actual = tree.copy(nodeVP);

    Ltag expected = new BaseLtag(nodeVP);
    expected.addProduction(nodeVP, nodeV);
    expected.addProduction(nodeVP, nodeDP2);
    expected.addProduction(nodeV, nodeWins);

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the Ltag subtree append.
   */
  @Test
  public void test_appendSubtreeFrom() throws LTAGException {
    LtagNode wins_nodeS = new PosNode("wins:S:1", POS.S);
    LtagNode wins_nodeDP1 = new PosNode("wins:DP:1", POS.DP, LtagNode.Marker.SUB);
    LtagNode wins_nodeVP = new PosNode("wins:VP:1", POS.VP);
    LtagNode wins_nodeV = new PosNode("wins:V:1", POS.V);
    //LtagNode wins_nodeDP2 = new PosNode("wins:DP:2", POS.DP, LtagNode.Marker.SUB);
    LtagNode wins_nodeWins = new LexicalNode("wins:LEX:wins", "wins");

    Ltag treeWins = new BaseLtag(wins_nodeS);
    treeWins.addProduction(wins_nodeS, wins_nodeDP1);
    treeWins.addProduction(wins_nodeS, wins_nodeVP);
    treeWins.addProduction(wins_nodeVP, wins_nodeV);
    //treeWins.addProduction(wins_nodeVP, wins_nodeDP2);
    treeWins.addProduction(wins_nodeV, wins_nodeWins);

    LtagNode uruguay_nodeDP = new PosNode("uruguay:DP:1", POS.DP);
    LtagNode uruguay_nodeUruguay = new LexicalNode("uruguay:LEX:uruguay", "Uruguay");

    Ltag treeUruguay = new BaseLtag(uruguay_nodeDP);
    treeUruguay.addProduction(uruguay_nodeDP, uruguay_nodeUruguay);

    Ltag actual = treeWins.copy();
    actual.appendSubtreeFrom(treeUruguay, uruguay_nodeDP, wins_nodeVP);

    Ltag expected = new BaseLtag(wins_nodeS);
    expected.addProduction(wins_nodeS, wins_nodeDP1);
    expected.addProduction(wins_nodeS, wins_nodeVP);
    expected.addProduction(wins_nodeVP, wins_nodeV);
    expected.addProduction(wins_nodeVP, uruguay_nodeDP);
    expected.addProduction(wins_nodeV, wins_nodeWins);
    expected.addProduction(uruguay_nodeDP, uruguay_nodeUruguay);

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the Ltag subtree replacement.
   */
  @Test
  public void test_replaceWithSubtreeFrom() throws LTAGException {
    LtagNode wins_nodeS = new PosNode("wins:S:1", POS.S);
    LtagNode wins_nodeDP1 = new PosNode("wins:DP:1", POS.DP, LtagNode.Marker.SUB);
    LtagNode wins_nodeVP = new PosNode("wins:VP:1", POS.VP);
    LtagNode wins_nodeV = new PosNode("wins:V:1", POS.V);
    LtagNode wins_nodeDP2 = new PosNode("wins:DP:2", POS.DP, LtagNode.Marker.SUB);
    LtagNode wins_nodeWins = new LexicalNode("wins:LEX:wins", "wins");

    Ltag treeWins = new BaseLtag(wins_nodeS);
    treeWins.addProduction(wins_nodeS, wins_nodeDP1);
    treeWins.addProduction(wins_nodeS, wins_nodeVP);
    treeWins.addProduction(wins_nodeVP, wins_nodeV);
    treeWins.addProduction(wins_nodeVP, wins_nodeDP2);
    treeWins.addProduction(wins_nodeV, wins_nodeWins);

    LtagNode uruguay_nodeDP = new PosNode("uruguay:DP:1", POS.DP);
    LtagNode uruguay_nodeUruguay = new LexicalNode("uruguay:LEX:uruguay", "Uruguay");

    Ltag treeUruguay = new BaseLtag(uruguay_nodeDP);
    treeUruguay.addProduction(uruguay_nodeDP, uruguay_nodeUruguay);

    Ltag actual = treeWins.copy();
    actual.replaceWithSubtreeFrom(treeUruguay, uruguay_nodeDP, wins_nodeDP1);

    Ltag expected = new BaseLtag(wins_nodeS);
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
    LtagNode nodeS = new PosNode("anchor:S:1", POS.S);
    LtagNode nodeDP1 = new PosNode("anchor:DP:1", POS.DP, LtagNode.Marker.SUB);
    LtagNode nodeVP = new PosNode("anchor:VP:1", POS.VP);
    LtagNode nodeV = new PosNode("anchor:V:1", POS.V);
    LtagNode nodeDP2 = new PosNode("anchor:DP:2", POS.DP, LtagNode.Marker.SUB);
    LtagNode nodeWins = new LexicalNode("anchor:LEX:wins", "wins");

    Ltag tree = new BaseLtag(nodeS);
    tree.addProduction(nodeS, nodeDP1);
    tree.addProduction(nodeS, nodeVP);
    tree.addProduction(nodeVP, nodeV);
    tree.addProduction(nodeVP, nodeDP2);
    tree.addProduction(nodeV, nodeWins);

    List<LtagNode> actual = tree.getSubstitutionNodes();

    List<LtagNode> expected = new ArrayList<>();
    expected.add(nodeDP1);
    expected.add(nodeDP2);

    Assert.assertTrue(expected.containsAll(actual) && actual.containsAll(expected));
  }

  /**
   * Tests the retrieval of all the adjunction nodes.
   */
  @Test
  public void test_getAdjunctionNodes() {
    LtagNode nodeVP1 = new PosNode("anchor:VP:1", POS.VP);
    LtagNode nodeVP2 = new PosNode("anchor:VP:2", POS.VP, LtagNode.Marker.ADJ);
    LtagNode nodeADV = new PosNode("anchor:ADV:1", POS.ADV);
    LtagNode nodeEasily = new LexicalNode("anchor:LEX:easily", "easily");

    Ltag tree = new BaseLtag(nodeVP1);
    tree.addProduction(nodeVP1, nodeVP2);
    tree.addProduction(nodeVP1, nodeADV);
    tree.addProduction(nodeADV, nodeEasily);

    List<LtagNode> actual = tree.getAdjunctionNodes();

    List<LtagNode> expected = new ArrayList<>();
    expected.add(nodeVP2);

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests Ltag pretty string representation.
   */
  @Test
  public void test_toPrettyString() {
    LtagNode nodeS = new PosNode("anchor:S:1", POS.S);
    LtagNode nodeDP1 = new PosNode("anchor:DP:1", POS.DP, LtagNode.Marker.SUB);
    LtagNode nodeVP = new PosNode("anchor:VP:1", POS.VP);
    LtagNode nodeV = new PosNode("anchor:V:1", POS.V);
    LtagNode nodeDP2 = new PosNode("anchor:DP:2", POS.DP, LtagNode.Marker.SUB);
    LtagNode nodeWins = new LexicalNode("anchor:LEX:wins", "wins");

    Ltag tree = new BaseLtag(nodeS);
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
   * Tests Ltag substitution.
   */
  @Test
  public void test_substitution() throws LTAGException {
    /* wins */
    LtagNode wins_nodeS = new PosNode("wins:S:1", POS.S);
    LtagNode wins_nodeDP1 = new PosNode("wins:DP:1", POS.DP, LtagNode.Marker.SUB);
    LtagNode wins_nodeVP = new PosNode("wins:VP:1", POS.VP);
    LtagNode wins_nodeV = new PosNode("wins:V:1", POS.V);
    LtagNode wins_nodeDP2 = new PosNode("wins:DP:2", POS.DP, LtagNode.Marker.SUB);
    LtagNode wins_nodeWins = new LexicalNode("wins:LEX:wins", "wins");

    Ltag treeWins = new BaseLtag(wins_nodeS);
    treeWins.addProduction(wins_nodeS, wins_nodeDP1);
    treeWins.addProduction(wins_nodeS, wins_nodeVP);
    treeWins.addProduction(wins_nodeVP, wins_nodeV);
    treeWins.addProduction(wins_nodeVP, wins_nodeDP2);
    treeWins.addProduction(wins_nodeV, wins_nodeWins);

    /* Uruguay */
    LtagNode uruguay_nodeDP = new PosNode("uruguay:DP:1", POS.DP);
    LtagNode uruguay_nodeUruguay = new LexicalNode("uruguay:LEX:Uruguay", "Uruguay");

    Ltag treeUruguay = new BaseLtag(uruguay_nodeDP);
    treeUruguay.addProduction(uruguay_nodeDP, uruguay_nodeUruguay);

    /* the */
    LtagNode the_nodeDP = new PosNode("the:DP:1", POS.DP);
    LtagNode the_nodeDET = new PosNode("the:DET:1", POS.DET);
    LtagNode the_nodeNP = new PosNode("the:NP:1", POS.NP, LtagNode.Marker.SUB);
    LtagNode the_nodeThe = new LexicalNode("the:LEX:the", "the");

    Ltag treeThe = new BaseLtag(the_nodeDP);
    treeThe.addProduction(the_nodeDP, the_nodeDET);
    treeThe.addProduction(the_nodeDP, the_nodeNP);
    treeThe.addProduction(the_nodeDET, the_nodeThe);

    /* game */
    LtagNode game_nodeNP = new PosNode("game:NP:1", POS.NP);
    LtagNode game_nodeGame = new LexicalNode("game:LEX:game", "game");

    Ltag treeGame = new BaseLtag(game_nodeNP);
    treeGame.addProduction(game_nodeNP, game_nodeGame);

    /* Uruguay wins */
    Ltag treeUruguayWins = treeWins.copy();
    treeUruguayWins.substitution(wins_nodeDP1, treeUruguay);
    Ltag expected_treeUruguayWins = new BaseLtag(wins_nodeS);
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
    Ltag treeTheGame = treeThe.copy();
    treeTheGame.substitution(the_nodeNP, treeGame);
    Ltag expected_treeTheGame = new BaseLtag(the_nodeDP);
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
    Ltag treeUruguayWinsTheGame = treeUruguayWins.copy();
    treeUruguayWinsTheGame.substitution(wins_nodeDP2, treeTheGame);
    Ltag expected_treeUruguayWinsTheGame = new BaseLtag(wins_nodeS);
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
   * Tests Ltag adjunction.
   */
  @Test
  public void test_adjunction() throws LTAGException {
    /* wins */
    LtagNode wins_nodeS = new PosNode("wins:S:1", POS.S);
    LtagNode wins_nodeDP1 = new PosNode("wins:DP:1", POS.DP, LtagNode.Marker.SUB);
    LtagNode wins_nodeVP = new PosNode("wins:VP:1", POS.VP);
    LtagNode wins_nodeV = new PosNode("wins:V:1", POS.V);
    LtagNode wins_nodeDP2 = new PosNode("wins:DP:2", POS.DP, LtagNode.Marker.SUB);
    LtagNode wins_nodeWins = new LexicalNode("wins:LEX:wins", "wins");

    Ltag treeWins = new BaseLtag(wins_nodeS);
    treeWins.addProduction(wins_nodeS, wins_nodeDP1);
    treeWins.addProduction(wins_nodeS, wins_nodeVP);
    treeWins.addProduction(wins_nodeVP, wins_nodeV);
    treeWins.addProduction(wins_nodeVP, wins_nodeDP2);
    treeWins.addProduction(wins_nodeV, wins_nodeWins);

    /* easily */
    LtagNode easily_nodeVP1 = new PosNode("easily:VP:1", POS.VP);
    LtagNode easily_nodeVP2 = new PosNode("easily:VP:2", POS.VP, LtagNode.Marker.ADJ);
    LtagNode easily_nodeADV = new PosNode("easily:ADV:1", POS.ADV);
    LtagNode easily_nodeEasily = new LexicalNode("easily:LEX:easily", "easily");

    Ltag treeEasily = new BaseLtag(easily_nodeVP1);
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
    Ltag treeWinsEasily = treeWins.copy();
    treeWinsEasily.adjunction(wins_nodeVP, treeEasily, easily_nodeVP2);
    Ltag expected_treeWinsEasily = new BaseLtag(wins_nodeS);
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
