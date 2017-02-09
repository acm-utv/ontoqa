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
 * JUnit tests for {@link Ltag} serialization.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Ltag
 * @see LtagJsonMapper
 */
public class LtagSerializationTest {

  /**
   * Tests {@link Ltag} serialization/deserialization.
   * @throws IOException when LTAG cannot be serialized/deserialized.
   */
  @Test
  public void test_simple() throws IOException {
    LtagNode nodeS = new PosNode("anchor:S:1", POS.S);
    LtagNode nodeDP1 = new PosNode("anchor:DP:1", POS.DP, LtagNode.Marker.SUB);
    LtagNode nodeVP = new PosNode("anchor:VP:1", POS.VP);
    LtagNode nodeV = new PosNode("anchor:V:1", POS.V);
    LtagNode nodeDP2 = new PosNode("anchor:DP:2", POS.DP, LtagNode.Marker.SUB);
    LtagNode nodeWins = new LexicalNode("anchor:LEX:wins", "wins");

    Ltag expected = new BaseLtag(nodeS);
    expected.addProduction(nodeS, nodeDP1);
    expected.addProduction(nodeS, nodeVP);
    expected.addProduction(nodeVP, nodeV);
    expected.addProduction(nodeVP, nodeDP2);
    expected.addProduction(nodeV, nodeWins);

    LtagJsonMapper mapper = new LtagJsonMapper();
    String json = mapper.writeValueAsString(expected);
    Ltag actual = mapper.readValue(json, Ltag.class);

    Assert.assertEquals(expected, actual);
  }

}
