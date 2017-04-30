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
import com.acmutv.ontoqa.core.syntax.ltag.serial.LtagYamlMapper;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * JUnit tests for {@link Ltag} serialization.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Ltag
 * @see LtagJsonMapper
 */
public class LtagSerializationTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(LtagSerializationTest.class);

  /**
   * Asserts the serialization correctness.
   * @param expected the expected value.
   * @throws IOException when value cannot be serialized or deserialized.
   */
  private void testSerialization(Ltag expected) throws IOException {
    LtagJsonMapper jsonMapper = new LtagJsonMapper();
    String json = jsonMapper.writeValueAsString(expected);
    LOGGER.debug("LTAG JSON serialization: \n{}", json);
    Ltag actualJson = jsonMapper.readValue(json, Ltag.class);
    Assert.assertEquals(expected, actualJson);

    LtagYamlMapper yamlMapper = new LtagYamlMapper();
    String yaml = yamlMapper.writeValueAsString(expected);
    LOGGER.debug("LTAG YAML serialization: \n{}", yaml);
    Ltag actualYaml = yamlMapper.readValue(yaml, Ltag.class);
    Assert.assertEquals(expected, actualYaml);
  }

  /**
   * Tests {@link Ltag} serialization/deserialization.
   * @throws IOException when LTAG cannot be serialized/deserialized.
   */
  @Test
  public void test_simple() throws IOException {
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

    LtagJsonMapper mapper = new LtagJsonMapper();
    String json = mapper.writeValueAsString(expected);
    Ltag actual = mapper.readValue(json, Ltag.class);

    Assert.assertEquals(expected, actual);
  }

}
