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

package com.acmutv.ontoqa.core.semantics.base;

import com.acmutv.ontoqa.core.semantics.base.statement.Replace;
import com.acmutv.ontoqa.core.semantics.base.term.Variable;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * JUnit tests for {@link Replace}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Replace
 */
public class ReplaceTest {

  /**
   * Tests string matching for {@link Replace}.
   */
  @Test
  public void test_match() {
    String correct[] = {"REPLACE(v1,v2)"};
    String wrong[] = {"", "REPLACE", "REPLCAE()", "REPLCAE(,)", "REPLACE(a)", "REPLACE(,b)"};

    for (String s : correct) {
      Assert.assertTrue(s.matches(Replace.REGEXP));
    }

    for (String s : wrong) {
      Assert.assertFalse(s.matches(Replace.REGEXP));
    }
  }

  /**
   * Tests {@link Replace} serialization/deserialization.
   * @throws IOException when Drs cannot be serialized/deserialized.
   */
  @Test
  public void test_serialization() throws IOException {
    Replace expected = new Replace(new Variable(1), new Variable(2));
    String string = expected.toString();
    Replace actual = Replace.valueOf(string);
    Assert.assertEquals(expected, actual);
  }

}
