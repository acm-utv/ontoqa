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

import java.io.IOException;

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
   * Tests the string representation of a lexical node.
   */
  @Test
  public void test_toString_lex() {
    LtagNode node = new LexicalNode("1", "wins");
    String expected = "(1,'wins')";
    String actual = node.toString();
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the string representation of a POS node, unmarked.
   */
  @Test
  public void test_toString_posUnmarked() {
    LtagNode node = new PosNode("2", POS.NP);
  }

  /**
   * Tests the string representation of a POS node, marked for adjunction.
   */
  @Test
  public void test_toString_posAdjunction() {
    LtagNode node = new PosNode("1", POS.NP, LtagNode.Marker.ADJ);
    String expected = "(1,NP)*";
    String actual = node.toString();
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the string representation of a POS node, marked for substitution.
   */
  @Test
  public void test_toString_posSubstitution() {
    LtagNode node = new PosNode("1", POS.NP, LtagNode.Marker.SUB);
    String expected = "(1,NP)^";
    String actual = node.toString();
    Assert.assertEquals(expected, actual);
  }
}
