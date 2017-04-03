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
 * JUnit tests for {@link SimpleLtag} substitution operation.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Ltag
 * @see SimpleLtag
 */
public class LtagSubstitutionTest {

  private static final Logger LOGGER = LogManager.getLogger(LtagSubstitutionTest.class);

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

}
