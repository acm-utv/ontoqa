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

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.io.output.WriterOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.Rio;

import java.io.*;
import java.nio.charset.Charset;

/**
 * This class realizes the lexicon management services.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class LexiconManager {

  private static final Logger LOGGER = LogManager.getLogger(LexiconManager.class);

  /**
   * Reads a lexicon from an input.
   * @param input the input to read.
   * @param prefix the default prefix for the lexicon.
   * @param format the lexicon format.
   * @return the lexicon.
   * @throws IOException when lexicon cannot be read.
   */
  public static Lexicon readLexicon(Reader input, String prefix, LexiconFormat format) throws IOException {
    LOGGER.traceEntry("input={} prefix={} format={}", input, prefix, format);

    InputStream stream = new ReaderInputStream(input, Charset.defaultCharset());
    Lexicon lexicon = readLexicon(stream, prefix, format);

    return LOGGER.traceExit(lexicon);
  }

  /**
   * Reads a lexicon from an input.
   * @param input the input to read.
   * @param prefix the default prefix for the lexicon.
   * @param format the lexicon format.
   * @return the lexicon.
   * @throws IOException when lexicon cannot be read.
   */
  public static Lexicon readLexicon(InputStream input, String prefix, LexiconFormat format) throws IOException {
    LOGGER.traceEntry("input={} prefix={} format={}", input, prefix, format);

    Lexicon lexicon = new SimpleLexicon();
    Model model = Rio.parse(input, prefix, format.getFormat());
    lexicon.merge(model);

    return LOGGER.traceExit(lexicon);
  }

  /**
   * Writes a lexicon to an output.
   * @param output the output to write.
   * @param lexicon the lexicon to write.
   * @param format the lexicon format.
   */
  public static void writeLexicon(Writer output, Lexicon lexicon, LexiconFormat format) {
    LOGGER.traceEntry("output={} lexicon={} format={}", output, lexicon, format);

    OutputStream stream = new WriterOutputStream(output, Charset.defaultCharset());
    writeLexicon(stream, lexicon, format);
  }

  /**
   * Writes a lexicon to an output.
   * @param output the output to write.
   * @param lexicon the lexicon to write.
   * @param format the lexicon format.
   */
  public static void writeLexicon(OutputStream output, Lexicon lexicon, LexiconFormat format) {
    LOGGER.traceEntry("output={} lexicon={} format={}", output, lexicon, format);

    Rio.write(lexicon, output, format.getFormat());
  }

}
