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

package com.acmutv.ontoqa;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * This class realizes miscellanea JUnit tests (for personal use only)
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @since 1.0
 */
public class MiscTest {

  private static final Logger LOGGER = LogManager.getLogger(MiscTest.class);

  @Before
  public void before() {
    final String configPath = MiscTest.class.getResource("/log/log4j2.sample.xml").getPath();
    LoggerContext context = (LoggerContext) LogManager.getContext(false);
    File file = new File(configPath);
    context.setConfigLocation(file.toURI());
  }

  @Test
  public void test_showcase() {
    LOGGER.fatal("Fatal message");
    LOGGER.error("Error message");
    LOGGER.warn("Warning message");
    LOGGER.info("Info message");
    LOGGER.debug("Debug message");
    LOGGER.trace("Trace message");
  }

  @Test
  public void test_string() {
    final String STR =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut " +
            "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco " +
            "laboris nisi ut aliquip ex ea commodo consequat.";

    System.out.println(STR.replaceAll("(.{100})", "$1\n"));


  }
}
