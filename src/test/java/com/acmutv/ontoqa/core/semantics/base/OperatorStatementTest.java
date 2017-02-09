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

import com.acmutv.ontoqa.core.semantics.base.statement.OperatorStatement;
import com.acmutv.ontoqa.core.semantics.base.statement.OperatorType;
import com.acmutv.ontoqa.core.semantics.base.term.Function;
import com.acmutv.ontoqa.core.semantics.base.term.Variable;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * JUnit tests for {@link OperatorStatement}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see OperatorStatement
 */
public class OperatorStatementTest {

  /**
   * Tests string matching for {@link OperatorStatement}.
   */
  @Test
  public void test_match() {
    String correct[] = {"MIN(v1,v2)", "MAX(v1,v2)"};
    String wrong[] = {"", "REPLACE", "REPLACE()", "REPLACE(,)"};

    for (String s : correct) {
      Assert.assertTrue(s.matches(OperatorStatement.REGEXP));
    }

    for (String s : wrong) {
      Assert.assertFalse(s.matches(OperatorStatement.REGEXP));
    }
  }

  /**
   * Tests {@link OperatorStatement} serialization/deserialization.
   * @throws IOException when Drs cannot be serialized/deserialized.
   */
  @Test
  public void test_serialization() throws IOException {
    OperatorStatement expected = new OperatorStatement(OperatorType.MAX, new Variable(1), new Variable(2));
    String string = expected.toString();
    OperatorStatement actual = OperatorStatement.valueOf(string);
    Assert.assertEquals(expected.getOperator(), actual.getOperator());
    Assert.assertEquals(expected.getLeft(), actual.getLeft());
    Assert.assertEquals(expected.getRight(), actual.getRight());
  }

}
