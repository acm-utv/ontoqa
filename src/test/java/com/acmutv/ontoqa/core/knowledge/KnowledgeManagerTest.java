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

package com.acmutv.ontoqa.core.knowledge;

import com.acmutv.ontoqa.core.knowledge.ontology.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

/**
 * This class realizes JUnit tests for {@link KnowledgeManager}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see KnowledgeManager
 */
public class KnowledgeManagerTest {

  private static final Logger LOGGER = LogManager.getLogger(KnowledgeManagerTest.class);

  /**
   * Tests ontology reading.
   * @throws IOException when the ontology file cannot be read.
   */
  @Test
  public void test_readOntology() throws IOException {
    final String resource = KnowledgeManagerTest.class.getResource("/knowledge/sample.ttl").getPath();

    final Ontology actual = KnowledgeManager.read(resource, "example", OntologyFormat.TURTLE);
    final Ontology expected = Commons.buildOntology(1);

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests ontology writing.
   * @throws IOException when the ontology file cannot be written.
   */
  @Test
  public void test_writeOntology() throws IOException {
    final Ontology expected = Commons.buildOntology(1);
    Writer writer = new StringWriter();
    KnowledgeManager.write(writer, expected, OntologyFormat.TURTLE);
    final Ontology actual = KnowledgeManager.read(new StringReader(writer.toString()), "example", OntologyFormat.TURTLE);

    Assert.assertEquals(expected, actual);
  }
}
