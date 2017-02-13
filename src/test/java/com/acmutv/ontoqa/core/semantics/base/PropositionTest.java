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

import com.acmutv.ontoqa.core.semantics.base.statement.Proposition;
import com.acmutv.ontoqa.core.semantics.base.term.Constant;
import com.acmutv.ontoqa.core.semantics.base.term.Function;
import com.acmutv.ontoqa.core.semantics.base.term.Variable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * JUnit tests for {@link Proposition}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Proposition
 */
public class PropositionTest {

  private static final Logger LOGGER = LogManager.getLogger(PropositionTest.class);

  /**
   * Tests string matching for {@link Proposition}.
   */
  @Test
  public void test_match() {
    String correct[] = {"v1(v2)", "v1(v2,v3)"};
    String wrong[] = {"", "REPLACE", "REPLACE()", "REPLACE(,)"};

    for (String s : correct) {
      Assert.assertTrue(s.matches(Proposition.REGEXP));
    }

    for (String s : wrong) {
      Assert.assertFalse(s.matches(Proposition.REGEXP));
    }
  }

  /**
   * Tests {@link Proposition} serialization/deserialization.
   * @throws IOException when Drs cannot be serialized/deserialized.
   */
  @Test
  public void test_serialization_1() throws IOException {
    Proposition expected = new Proposition(new Variable(1), new Variable(2));
    String string = expected.toString();
    LOGGER.debug(string);
    Proposition actual = Proposition.valueOf(string);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests {@link Proposition} serialization/deserialization.
   * @throws IOException when Drs cannot be serialized/deserialized.
   */
  @Test
  public void test_serialization_2() throws IOException {
    Proposition expected = new Proposition(
        new Constant("http://dbpedia.org/resource/Albert_Einstein"), new Variable(1));
    String string = expected.toString();
    LOGGER.debug(string);
    Proposition actual = Proposition.valueOf(string);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests {@link Proposition} serialization/deserialization.
   * @throws IOException when Drs cannot be serialized/deserialized.
   */
  @Test
  public void test_serialization_3() throws IOException {
    Proposition expected = new Proposition(
        new Constant("http://dbpedia.org/resource/capacity"), new Variable(1), new Variable(2));
    String string = expected.toString();
    LOGGER.debug(string);
    Proposition actual = Proposition.valueOf(string);
    Assert.assertEquals(expected, actual);
  }

}
