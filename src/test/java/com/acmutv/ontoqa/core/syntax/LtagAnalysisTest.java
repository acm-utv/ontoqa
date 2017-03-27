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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * JUnit tests for {@link Ltag} analysis.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Ltag
 * @see LtagJsonMapper
 */
public class LtagAnalysisTest {

  private static final Logger LOGGER = LogManager.getLogger(LtagAnalysisTest.class);

  /**
   * Tests {@link Ltag} analysis.
   */
  @Test
  public void test_transitiveVerbActiveIndicative() {
    Ltag ltag = LtagTemplates.transitiveVerbActiveIndicative("wins", "subj", "obj");

    Properties expected = new Properties();
    expected.put("lex", 1);
    expected.put("words", new ArrayList<Integer>(){{add(1);}});
    expected.put("subvec", new ArrayList<List<String>>(){{
      add(new ArrayList<String>(){{add("subj");}});
      add(new ArrayList<String>(){{add("obj");}});
    }});
    expected.put("adjvec", new ArrayList<List<String>>(){{
      add(new ArrayList<>());
      add(new ArrayList<>());
    }});

    Properties actual = ltag.analyze();

    Assert.assertEquals(expected, actual);
  }

}
