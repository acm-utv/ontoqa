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
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.Rio;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

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
   * Reads a lexicon from a resource.
   * @param resource the resource to read.
   * @param prefix the default prefix for the lexicon.
   * @param format the lexicon format.
   * @return the lexicon.
   * @throws IOException when lexicon cannot be read.
   */
  public static Lexicon read(String resource, String prefix, LexiconFormat format) throws IOException {
    LOGGER.traceEntry("resource={} prefix={} format={}", resource, prefix, format);
    Lexicon lexicon = new SimpleLexicon();
    Path path = FileSystems.getDefault().getPath(resource).toAbsolutePath();
    try (InputStream in = Files.newInputStream(path)) {
      Model model = Rio.parse(in, prefix, format.getFormat());
      lexicon.merge(model);
    }
    return LOGGER.traceExit(lexicon);
  }

  /**
   * Reads a lexicon from a reader.
   * @param reader the reader to read.
   * @param prefix the default prefix for the lexicon.
   * @param format the lexicon format.
   * @return the lexicon.
   * @throws IOException when lexicon cannot be read.
   */
  public static Lexicon read(Reader reader, String prefix, LexiconFormat format) throws IOException {
    LOGGER.traceEntry("reader={} prefix={} format={}", reader, prefix, format);
    Lexicon lexicon = new SimpleLexicon();
    Model model = Rio.parse(reader, prefix, format.getFormat());
    lexicon.merge(model);
    return LOGGER.traceExit(lexicon);
  }

  /**
   * Writes a lexicon on a resource.
   * @param resource the resource to write on.
   * @param lexicon the lexicon to write.
   * @param format the lexicon format.
   * @throws IOException when lexicon cannot be written.
   */
  public static void write(String resource, Lexicon lexicon, LexiconFormat format) throws IOException {
    LOGGER.traceEntry("resource={} lexicon={} format={}", resource, lexicon, format);
    Path path = FileSystems.getDefault().getPath(resource).toAbsolutePath();
    try (OutputStream out = Files.newOutputStream(path)) {
      Rio.write(lexicon, out, format.getFormat());
    }
  }

  /**
   * Writes a lexicon on a writer.
   * @param writer the writer to write on.
   * @param lexicon the lexicon to write.
   * @param format the lexicon format.
   * @throws IOException when lexicon cannot be written.
   */
  public static void write(Writer writer, Lexicon lexicon, LexiconFormat format) throws IOException {
    LOGGER.traceEntry("writer={} lexicon={} format={}", writer, lexicon, format);
    Rio.write(lexicon, writer, format.getFormat());
  }

}
