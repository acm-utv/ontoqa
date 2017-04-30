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

package com.acmutv.ontoqa.core.grammar;

import com.acmutv.ontoqa.core.grammar.serial.GrammarJsonMapper;
import com.acmutv.ontoqa.core.grammar.serial.GrammarYamlMapper;
import com.acmutv.ontoqa.tool.io.IOManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

/**
 * This class realizes the grammar management services.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class GrammarManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(GrammarManager.class);

  /**
   * Reads all the SLTAG inside {@code directory} serializaed as {@code format}.
   * @param directory the directory.
   * @param format the grammar format.
   * @return the grammar produced by merging all grammars inside {@code directory}
   * serializaed as {@code format}
   * @throws IOException when grammar cannot be read.
   */
  public static Grammar readAll(String directory, GrammarFormat format) throws IOException {
    List<Path> partials = IOManager.allFiles(directory, "*.sltag");
    Grammar grammar = new SimpleGrammar();
    for (Path file : partials) {
      grammar.merge(GrammarManager.read(file.toString(), format));
    }
    return grammar;
  }

  /**
   * Reads a grammar from {@code resource} as {@code format}.
   * @param resource the resource to read.
   * @param format the grammar format.
   * @return the grammar.
   * @throws IOException when grammar cannot be read.
   */
  public static Grammar read(String resource, GrammarFormat format) throws IOException {
    LOGGER.trace("resource={} format={}", resource, format);
    Grammar grammar;
    switch (format) {
      case JSON: grammar = readJson(resource); break;
      case YAML: grammar = readYaml(resource); break;
      default: throw new IOException("Unrecognized Grammar format");
    }
    return grammar;
  }

  /**
   * Reads a grammar from {@code resource} as JSON.
   * @param resource the resource to read.
   * @return the grammar.
   * @throws IOException when grammar cannot be read.
   */
  private static Grammar readJson(String resource) throws IOException {
    Grammar grammar;
    try (InputStream in = IOManager.getInputStream(resource)) {
      GrammarJsonMapper mapper = new GrammarJsonMapper();
      grammar = mapper.readValue(in, Grammar.class);
    }
    return grammar;
  }

  /**
   * Reads a grammar from {@code resource} as YAML.
   * @param resource the resource to read.
   * @return the grammar.
   * @throws IOException when grammar cannot be read.
   */
  private static Grammar readYaml(String resource) throws IOException {
    Grammar grammar;
    try (InputStream in = IOManager.getInputStream(resource)) {
      GrammarYamlMapper mapper = new GrammarYamlMapper();
      grammar = mapper.readValue(in, Grammar.class);
    }
    return grammar;
  }

}
