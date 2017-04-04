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
import com.acmutv.ontoqa.core.semantics.sltag.Sltag;
import lombok.NonNull;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * A simple SLTAG tokenizer.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @since 1.0
 */
public class SimpleSltagTokenizer implements SltagTokenizer {

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
    Stream.of(sentence.split(" "))
        .forEach(word -> this.buffer.add(new TokenizerBufferElement(word)));
  }

  /**
   * Checks if there could be another token.
   * @return true, if there is another token; false, otherwise.
   */
  @Override
  public boolean hasNext() {
    return this.nextUnchecked() != -1;
  }

  /**
   * Returns the next token.
   * @return the next token; null, otherwise.
   */
  @Override
  public Token next() {
    int start = this.nextUnchecked();
    if (start == -1) {
      return null;
    }
    int end = start;
    String lexicalPattern = "";
    String tempLexicalPattern = "";
    List<Sltag> candidates = new ArrayList<>();

    while (end < this.buffer.size()) {
      if (this.buffer.get(end).isTokenized()) {
        end++;
        continue;
      }

      tempLexicalPattern = tempLexicalPattern.concat((tempLexicalPattern.isEmpty())?"":" ") + this.buffer.get(end++);

      GrammarMatchType matchType = grammar.matchType(tempLexicalPattern);

      if (GrammarMatchType.FULL.equals(matchType)) {
        candidates.clear();
        candidates.addAll(grammar.getAllMatchingElementarySLTAG(tempLexicalPattern));
      } else if (GrammarMatchType.NONE.equals(matchType)) {
        break;
      }
    }

    Integer idxPrevLexicalEntry = (start > 0) ? start - 1 : null;

    return new Token(lexicalPattern, candidates, idxPrevLexicalEntry);
  }

  /**
   * Returns the index of the next tokenizable element.
   * @return the index of the next tokenizable element; -1, if there is not any.
   */
  private int nextUnchecked() {
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
