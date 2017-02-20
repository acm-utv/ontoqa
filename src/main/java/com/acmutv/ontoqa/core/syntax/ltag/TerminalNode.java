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

package com.acmutv.ontoqa.core.syntax.ltag;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A terminal LTAG node.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see LtagNode
 * @see Ltag
 */
public class TerminalNode extends LtagNode {

  /**
   * The regular expression
   */
  public static final String REGEXP = "^'(.+)'([0-9]+){0,1}$";

  /**
   * The pattern matcher used to match strings on {@code REGEXP}.
   */
  private static final Pattern PATTERN = Pattern.compile(REGEXP);

  /**
   * Constructs a new terminal node with default id 1.
   * @param lexicalEntry the lexical entry.
   */
  public TerminalNode(String lexicalEntry) {
    super(1, LtagNodeType.TERMINAL, null, null, lexicalEntry);
  }

  /**
   * Constructs a new terminal node.
   * @param id the node unique id.
   * @param lexicalEntry the lexical entry.
   */
  public TerminalNode(int id, String lexicalEntry) {
    super(id, LtagNodeType.TERMINAL, null, null, lexicalEntry);
  }

  /**
   * Parses {@link TerminalNode} from string.
   * @param string the string to parse.
   * @return the parsed {@link TerminalNode}.
   * @throws IllegalArgumentException when {@code string} cannot be parsed.
   */
  public static TerminalNode valueOf(String string) throws IllegalArgumentException {
    if (string == null) throw new IllegalArgumentException();
    Matcher matcher = PATTERN.matcher(string);
    if (!matcher.matches()) throw new IllegalArgumentException();
    String lexicalEntry = matcher.group(1);
    int id = (matcher.group(2) != null) ?
        Integer.valueOf(matcher.group(2)) : 1;
    return new TerminalNode(id, lexicalEntry);
  }

}
