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

import java.io.IOException;

/**
 * JUnit tests for {@link LtagNode} serialization.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see LtagNode
 */
public class LtagNodeSerializationTest {

  /**
   * Tests {@link LtagNode} serialization/deserialization.
   * @throws IOException when LTAG cannot be serialized/deserialized.
   */
  @Test
  public void test_terminal() throws IOException {
    LtagNode expected = new TerminalNode("word");
    String string = expected.toString();
    LtagNode actual = LtagNodes.valueOf(string);
    Assert.assertTrue(actual.identical(expected));
  }

  /**
   * Tests {@link LtagNode} serialization/deserialization.
   * @throws IOException when LTAG cannot be serialized/deserialized.
   */
  @Test
  public void test_terminal_pretty() throws IOException {
    LtagNode expected = new TerminalNode(1, "word");
    LtagNode actual = LtagNodes.valueOf("'word'");
    Assert.assertTrue(actual.identical(expected));
  }

  /**
   * Tests {@link LtagNode} serialization/deserialization.
   * The node is neither marked nor labeled.
   * @throws IOException when LTAG cannot be serialized/deserialized.
   */
  @Test
  public void test_nonterminal_unmarkedUnlabeled() throws IOException {
    for (SyntaxCategory syntaxCategory : SyntaxCategory.values()) {
      LtagNode expected = new NonTerminalNode(syntaxCategory);
      String string = expected.toString();
      LtagNode actual = LtagNodes.valueOf(string);
      Assert.assertTrue(actual.identical(expected));
    }
  }

  /**
   * Tests {@link LtagNode} serialization/deserialization.
   * The node is neither marked nor labeled (pretty).
   * @throws IOException when LTAG cannot be serialized/deserialized.
   */
  @Test
  public void test_nonterminal_unmarkedUnlabeled_pretty() throws IOException {
    for (SyntaxCategory syntaxCategory : SyntaxCategory.values()) {
      LtagNode expected = new NonTerminalNode(syntaxCategory);
      LtagNode actual = LtagNodes.valueOf(syntaxCategory.name());
      Assert.assertTrue(actual.identical(expected));
    }
  }

  /**
   * Tests {@link LtagNode} serialization/deserialization.
   * The node is marked and labeled.
   * @throws IOException when LTAG cannot be serialized/deserialized.
   */
  @Test
  public void test_nonterminal_markedLabeled() throws IOException {
    for (SyntaxCategory syntaxCategory : SyntaxCategory.values()) {
      for (LtagNodeMarker marker : LtagNodeMarker.values()) {
        LtagNode expected = new NonTerminalNode(syntaxCategory, marker, "myAnchor");
        String string = expected.toString();
        LtagNode actual = LtagNodes.valueOf(string);
        Assert.assertTrue(actual.identical(expected));
      }
    }
  }

  /**
   * Tests {@link LtagNode} serialization/deserialization.
   * The node is marked and labeled (pretty).
   * @throws IOException when LTAG cannot be serialized/deserialized.
   */
  @Test
  public void test_nonterminal_markedLabeled_pretty() throws IOException {
    for (SyntaxCategory syntaxCategory : SyntaxCategory.values()) {
      String label = "my" + syntaxCategory.name();
      for (LtagNodeMarker marker : LtagNodeMarker.values()) {
        LtagNode expected = new NonTerminalNode(1, syntaxCategory, marker, label);
        LtagNode actual = LtagNodes.valueOf(String.format("%s%s(%s)",
            syntaxCategory.name(), marker.getSymbol(), label)
        );
        Assert.assertTrue(actual.identical(expected));
      }
    }
  }

}
