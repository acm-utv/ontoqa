/*
  The MIT License (MIT)

  Copyright (c) 2016 Antonella Botte, Giacomo Marciani and Debora Partigianoni

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

package com.acmutv.ontoqa.core.lexicon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

/**
 * This class realizes JUnit tests for {@link LexiconManager}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see LexiconManager
 */
public class LexiconManagerTest {

  private static final Logger LOGGER = LogManager.getLogger(LexiconManagerTest.class);

  /**
   * Tests lexicon reading.
   * @throws IOException
   */
  @Test
  public void test_readLexicon() throws IOException {
    final InputStream input = LexiconManagerTest.class.getResourceAsStream("/lexicon/sample.lexicon.ttl");

    final Lexicon actual = LexiconManager.readLexicon(
        input, "example", LexiconFormat.TURTLE);
    final Lexicon expected = Commons.buildLexicon(1);

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests lexicon writing.
   * @throws IOException
   */
  @Test
  public void test_writeLexicon() throws IOException {
    final Lexicon expected = Commons.buildLexicon(1);
    Writer output = new StringWriter();
    LexiconManager.writeLexicon(output, expected, LexiconFormat.TURTLE);
    final Lexicon actual = LexiconManager.readLexicon(
        new StringReader(output.toString()), "example", LexiconFormat.TURTLE);

    Assert.assertEquals(expected, actual);
  }
}
