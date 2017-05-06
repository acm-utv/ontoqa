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
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit tests for {@link LtagNode}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see LtagNode
 */
public class LtagNodeTest {

  /**
   * Tests the string representation of a terminal node (custom id).
   */
  @Test
  public void test_toString_terminal_customId() {
    LtagNode node = new TerminalNode(2, "wins");
    String expected = "'wins'2";
    String actual = node.toString();
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the string representation of a terminal node (default id).
   */
  @Test
  public void test_toString_terminal_defaultId() {
    LtagNode node = new TerminalNode(1, "wins");
    String expected = "'wins'";
    String actual = node.toString();
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the string representation of a non-terminal node (custom id).
   * The node is neither marked nor labeled.
   */
  @Test
  public void test_toString_nonterminal_unmarkedUnlabeled_customId() {
    LtagNode node = new NonTerminalNode(2, SyntaxCategory.NP);
    String expected = "NP2";
    String actual = node.toString();
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the string representation of a non-terminal node (default id).
   * The node is neither marked nor labeled.
   */
  @Test
  public void test_toString_nonterminal_unmarkedUnlabeled_defaultId() {
    LtagNode node = new NonTerminalNode(1, SyntaxCategory.NP);
    String expected = "NP";
    String actual = node.toString();
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the string representation of a non-terminal node (custom id).
   * The node is marked and labeled for adjunction.
   */
  @Test
  public void test_toString_nonterminal_markedLabeledAdjunction_customId() {
    LtagNode node = new NonTerminalNode(2, SyntaxCategory.NP, LtagNodeMarker.ADJ, "myAnchor");
    String expected = "NP2*(myAnchor)";
    String actual = node.toString();
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the string representation of a non-terminal node (default id).
   * The node is marked and labeled for adjunction.
   */
  @Test
  public void test_toString_nonterminal_markedLabeledAdjunction_defaultId() {
    LtagNode node = new NonTerminalNode(1, SyntaxCategory.NP, LtagNodeMarker.ADJ, "myAnchor");
    String expected = "NP*(myAnchor)";
    String actual = node.toString();
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the string representation of a non-terminal node (custom id).
   * The node is marked and labeled for substitution.
   */
  @Test
  public void test_toString_nonterminal_markedLabeledSubstitution_customId() {
    LtagNode node = new NonTerminalNode(2, SyntaxCategory.NP, LtagNodeMarker.SUB, "myAnchor");
    String expected = "NP2^(myAnchor)";
    String actual = node.toString();
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the string representation of a non-terminal node (default id).
   * The node is marked and labeled for substitution.
   */
  @Test
  public void test_toString_nonterminal_markedLabeledSubstitution_defaultId() {
    LtagNode node = new NonTerminalNode(1, SyntaxCategory.NP, LtagNodeMarker.SUB, "myAnchor");
    String expected = "NP^(myAnchor)";
    String actual = node.toString();
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the copy of nodes.
   */
  @Test
  public void test_copy() {
    LtagNode node1 = new NonTerminalNode(1, SyntaxCategory.NP, LtagNodeMarker.SUB, "myAnchor");
    LtagNode node2 = new NonTerminalNode(SyntaxCategory.S);
    node2.copy(node1);
    Assert.assertEquals(node1, node2);
  }
}
