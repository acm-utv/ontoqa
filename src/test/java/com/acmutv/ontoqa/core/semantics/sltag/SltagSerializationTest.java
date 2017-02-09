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

package com.acmutv.ontoqa.core.semantics.sltag;

import com.acmutv.ontoqa.core.semantics.dudes.Dudes;
import com.acmutv.ontoqa.core.semantics.dudes.DudesTemplates;
import com.acmutv.ontoqa.core.semantics.sltag.SemanticLtag;
import com.acmutv.ontoqa.core.semantics.sltag.serial.SemanticLtagJsonMapper;
import com.acmutv.ontoqa.core.syntax.POS;
import com.acmutv.ontoqa.core.syntax.ltag.*;
import com.acmutv.ontoqa.core.syntax.ltag.serial.LtagJsonMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * JUnit tests for {@link SemanticLtag} serialization.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see SemanticLtag
 * @see SemanticLtagJsonMapper
 */
public class SltagSerializationTest {

  /**
   * Tests {@link SemanticLtag} serialization/deserialization.
   * @throws IOException when SLTAG cannot be serialized/deserialized.
   */
  @Test
  public void test_simple() throws IOException {
    /* LTAG */
    LtagNode nodeDP = new PosNode("DP1", POS.DP);
    LtagNode nodeAlbertEinstein = new LexicalNode("LEX:Albert_Einstein", "Albert Einstein");

    Ltag ltag = new BaseLtag(nodeDP);
    ltag.addProduction(nodeDP, nodeAlbertEinstein);

    /* DUDES */
    Dudes dudes = DudesTemplates.properNoun("http://dbpedia.org/resource/Albert_Einstein");

    /* SLTAG */
    SemanticLtag expected = new BaseSemanticLtag(ltag, dudes);

    SemanticLtagJsonMapper mapper = new SemanticLtagJsonMapper();
    String json = mapper.writeValueAsString(expected);
    System.out.println(json);
    SemanticLtag actual = mapper.readValue(json, SemanticLtag.class);

    Assert.assertEquals(expected, actual);
  }

}
