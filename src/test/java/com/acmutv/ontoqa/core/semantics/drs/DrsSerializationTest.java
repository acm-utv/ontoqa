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

package com.acmutv.ontoqa.core.semantics.drs;

import com.acmutv.ontoqa.core.semantics.base.statement.Replace;
import com.acmutv.ontoqa.core.semantics.base.term.Variable;
import com.acmutv.ontoqa.core.semantics.drs.serial.DrsJsonMapper;
import com.acmutv.ontoqa.core.semantics.dudes.serial.DudesJsonMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * JUnit tests for {@link Drs} serialization.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Drs
 * @see DudesJsonMapper
 */
public class DrsSerializationTest {

  private static final Logger LOGGER = LogManager.getLogger(DrsSerializationTest.class);

  /**
   * Tests {@link Drs} serialization/deserialization.
   * @throws IOException when Drs cannot be serialized/deserialized.
   */
  @Test
  public void test_empty() throws IOException {
    Drs expected = new SimpleDrs(1);
    String json = new DrsJsonMapper().writeValueAsString(expected);
    Drs actual = new DrsJsonMapper().readValue(json, Drs.class);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests {@link Drs} serialization/deserialization.
   * @throws IOException when Drs cannot be serialized/deserialized.
   */
  @Test
  public void test_simple() throws IOException {
    Drs expected = new SimpleDrs(1);
    Variable v1 = new Variable(1);
    Variable v2 = new Variable(2);
    expected.getVariables().add(v1);
    expected.getVariables().add(v2);
    expected.getStatements().add(new Replace(v1, v2));
    expected.getStatements().add(new Replace(v1, v2));

    DrsJsonMapper mapper = new DrsJsonMapper();
    String json = mapper.writeValueAsString(expected);
    Drs actual = mapper.readValue(json, Drs.class);

    Assert.assertEquals(expected, actual);
  }

}
