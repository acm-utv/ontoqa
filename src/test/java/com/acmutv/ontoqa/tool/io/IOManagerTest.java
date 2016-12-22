/*
  The MIT License (MIT)

  Copyright (c) 2016 Giacomo Marciani

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

package com.acmutv.ontoqa.tool.io;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * This class realizes JUnit tests for {@link IOManager}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see IOManager
 */
public class IOManagerTest {

  /**
   * Tests the {@link InputStream} creation from a local resource.
   * @throws IOException when {@link InputStream} cannot be opened.
   */
  @Test
  public void test_getInputStream_localResource() throws IOException {
    String resource = IOManagerTest.class.getResource("/tool/sample.txt").getPath();
    String actual;
    try (final InputStream in = IOManager.getInputStream(resource)) {
      actual = IOUtils.toString(in, Charset.defaultCharset());
    }
    String expected = "Hello World!";
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the {@link InputStream} creation from a local file.
   * @throws IOException when {@link InputStream} cannot be opened.
   */
  @Test
  public void test_getInputStream_localFile() throws IOException {
    String resource = "README.md";
    String actual;
    try (final InputStream in = IOManager.getInputStream(resource)) {
      actual = IOUtils.toString(in, Charset.defaultCharset());
    }
    Assert.assertNotNull(actual);
  }

  /**
   * Tests the {@link InputStream} creation from a remote resource.
   * @throws IOException when {@link InputStream} cannot be opened.
   */
  @Test
  public void test_getInputStream_remote() throws IOException {
    String resource = "http://www.google.com/robots.txt";
    String actual;
    try (final InputStream in = IOManager.getInputStream(resource)) {
       actual = IOUtils.toString(in, Charset.defaultCharset());
    }
    Assert.assertNotNull(actual);
  }

  /**
   * Tests the {@link OutputStream} creation from a local file.
   * @throws IOException when {@link OutputStream} cannot be opened.
   */
  @Test
  public void test_getOutputStream_localFile() throws IOException {
    String resource = "data/test/sample.txt";
    try (final OutputStream out = IOManager.getOutputStream(resource)) {
      IOUtils.write("Hello World!", out, Charset.defaultCharset());
    }
  }

  /**
   * Tests the {@link OutputStream} creation from a remote resource.
   */
  @Test
  public void test_getOutputStream_remote() {
    String resource = "http://www.google.com/robots.txt";
    try (final OutputStream out = IOManager.getOutputStream(resource)) {
      IOUtils.write("Hello World!", out, Charset.defaultCharset());
    } catch (IOException exc) { return; }
    Assert.fail();
  }

}
