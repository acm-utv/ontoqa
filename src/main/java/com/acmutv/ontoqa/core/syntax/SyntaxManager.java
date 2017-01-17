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

package com.acmutv.ontoqa.core.syntax;

import com.acmutv.ontoqa.core.exception.SyntaxProcessingException;
import com.acmutv.ontoqa.core.knowledge.ontology.Ontology;
import com.acmutv.ontoqa.core.lexicon.Lexicon;
import com.acmutv.ontoqa.core.syntax.ltag.Ltag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * This class realizes the syntax management services.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class SyntaxManager {

  private static final Logger LOGGER = LogManager.getLogger(SyntaxManager.class);

  /**
   * Builds the syntax tree for a natural language sentence, addressing an ontology and lexicon.
   * @param nlString the natural language sentence.
   * @param ontology the ontology to address.
   * @param lexicon the lexicon to address.
   * @return the syntax tree.
   */
  public static Ltag getSyntaxTree(String nlString, Ontology ontology, Lexicon lexicon)
      throws SyntaxProcessingException {
    LOGGER.traceEntry("nlString={} ontology={} lexicon={}", nlString, ontology, lexicon);
    final String[] words = nlString.split(" ");
    List<Ltag> trees = buildSyntaxTrees(lexicon).getAll(words);
    Ltag tree = SyntaxManager.reduce(trees);
    return LOGGER.traceExit(tree);
  }

  public static SyntaxRepo buildSyntaxTrees(Lexicon lexicon) {

    //TODO process Lexicon to get SyntaxRepo of elementary trees

    return new SimpleSyntaxRepo();
  }

  public static Ltag reduce(List<Ltag> trees) {
    //TODO implement the syntax tree reduction
    return null;
  }
}
