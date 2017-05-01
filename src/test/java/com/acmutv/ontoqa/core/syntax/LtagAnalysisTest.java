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

package com.acmutv.ontoqa.core.syntax;

import com.acmutv.ontoqa.core.syntax.ltag.*;
import com.acmutv.ontoqa.core.syntax.ltag.serial.LtagJsonMapper;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * JUnit tests for {@link Ltag} analysis.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Ltag
 * @see LtagJsonMapper
 */
public class LtagAnalysisTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(LtagAnalysisTest.class);

  /**
   * Tests {@link Ltag} analysis.
   */
  @Test
  public void test_transitiveVerbActiveIndicative() {
    Ltag ltag = LtagTemplates.transitiveVerbActiveIndicative("wins", "subj", "obj");

    Properties expected = new Properties();
    expected.put("lex", 1);
    expected.put("words", new ArrayList<Integer>(){{add(1);}});
    expected.put("subvec", new ArrayList<List<String>>(){{
      add(new ArrayList<String>(){{add("subj");}});
      add(new ArrayList<String>(){{add("obj");}});
    }});
    expected.put("adjvec", new ArrayList<List<String>>(){{
      add(new ArrayList<>());
      add(new ArrayList<>());
    }});

    Properties actual = ltag.analyze();

    Assert.assertEquals(expected, actual);
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

}
