/*
  The MIT License (MIT)

  Copyright (c) 2017 Giacomo Marciani

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
package com.acmutv.ontoqa.core.parser;

import com.acmutv.ontoqa.core.grammar.Grammar;
import com.acmutv.ontoqa.core.grammar.GrammarMatchType;
import com.acmutv.ontoqa.core.semantics.sltag.ElementarySltag;
import com.acmutv.ontoqa.core.semantics.sltag.SimpleElementarySltag;
import com.acmutv.ontoqa.core.semantics.sltag.Sltag;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * A simple SLTAG tokenizer.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @since 1.0
 */
public class SimpleSltagTokenizer implements SltagTokenizer {

  private static final Logger LOGGER = LogManager.getLogger(SimpleSltagTokenizer.class);

  @NonNull
  private Grammar grammar;

  @NonNull
  private String sentence;

  private List<TokenizerBufferElement> buffer = new ArrayList<>();

  /**
   * Creates a new tokenizer.
   * @param grammar the grammar to tokenize with.
   * @param sentence the sentence to tokenize.
   */
  public SimpleSltagTokenizer(Grammar grammar, String sentence) {
    this.grammar = grammar;
    this.sentence = sentence;
    if (!sentence.isEmpty()) {
      Stream.of(sentence.split(" "))
          .forEach(word -> this.buffer.add(new TokenizerBufferElement(word)));
    }
  }

  /**
   * Checks if there could be another token.
   * @return true, if there is another token; false, otherwise.
   */
  @Override
  public boolean hasNext() {
    return this.nextTokenizable() != -1;
  }

  /**
   * Returns the next token.
   * @return the next token; null, otherwise.
   */
  @Override
  public Token next() {
    int start = this.nextTokenizable();
    LOGGER.debug("Tokenizer Buffer: {}", this.buffer);
    LOGGER.debug("Next tokenizable: {}", start);
    if (start == -1) {
      return null;
    }
    int end = start;
    String lexicalPattern = "";
    String tempLexicalPattern = "";
    List<ElementarySltag> candidates = new ArrayList<>();

    while (end < this.buffer.size()) {
      LOGGER.debug("end: {}", end);
      TokenizerBufferElement elem = this.buffer.get(end);

      if (elem.isTokenized()) {
        LOGGER.debug("Already tokenized: {}", elem.getWord());
      } else {
        tempLexicalPattern = tempLexicalPattern.concat((tempLexicalPattern.isEmpty())?"":" ") + elem.getWord();

        GrammarMatchType matchType = grammar.matchType(tempLexicalPattern);

        LOGGER.debug("tempLexicalPattern: {}", tempLexicalPattern);
        LOGGER.debug("Match: {}", matchType);

        if (GrammarMatchType.FULL.equals(matchType)) {
          candidates.clear();
          List<ElementarySltag> elemCandidates = grammar.getAllMatchingElementarySLTAG(tempLexicalPattern);
          LOGGER.debug("Candidates: {}", elemCandidates);
          for (ElementarySltag esltag : elemCandidates) {
            ElementarySltag copy = new SimpleElementarySltag(esltag);
            candidates.add(copy);
          }
          lexicalPattern = tempLexicalPattern;
          this.buffer.get(end).setTokenized(true);
        } else if (GrammarMatchType.NONE.equals(matchType)) {
          break;
        } else if (GrammarMatchType.PART.equals(matchType)) {
          this.buffer.get(end).setTokenized(true);
        }
      }

      end++;
    }

    Integer idxPrevLexicalEntry = (start > 0) ? start - 1 : null;

    LOGGER.debug("Tokenizer Buffer: {}", this.buffer);

    return new Token(lexicalPattern, candidates, idxPrevLexicalEntry);
  }

  /**
   * Returns the index of the next tokenizable element.
   * @return the index of the next tokenizable element; -1, if there is not any.
   */
  private int nextTokenizable() {
    for (int i = 0; i < this.buffer.size(); i++) {
      TokenizerBufferElement elem = this.buffer.get(i);
      if (!elem.isTokenized() && elem.isTokenizable()) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Resets the tokenizer.
   */
  @Override
  public void reset() {
    this.buffer.clear();
    Stream.of(sentence.split(" "))
        .forEach(word -> this.buffer.add(new TokenizerBufferElement(word)));
  }
}
