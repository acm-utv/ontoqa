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

import java.util.ArrayList;

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

  private static final Logger LOGGER = LoggerFactory.getLogger(LtagTest.class);

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
