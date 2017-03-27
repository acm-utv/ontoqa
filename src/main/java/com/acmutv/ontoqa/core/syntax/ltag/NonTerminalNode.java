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

import com.acmutv.ontoqa.core.syntax.SyntaxCategory;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A non-terminal LTAG node.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see LtagNode
 * @see Ltag
 */
public class NonTerminalNode extends LtagNode {

  /**
   * The regular expression
   */
  public static final String REGEXP =
      String.format("^(%s)([0-9]+){0,1}(\\^|\\*){0,1}(?:\\((\\w+)\\)){0,1}$",
          Arrays.stream(SyntaxCategory.values()).map(SyntaxCategory::name).collect(Collectors.joining("|")));

  /**
   * The pattern matcher used to match strings on {@code REGEXP}.
   */
  private static final Pattern PATTERN = Pattern.compile(REGEXP);

  /**
   * Constructs a new non-terminal node with default id 1.
   * @param category the syntax category.
   */
  public NonTerminalNode(SyntaxCategory category) {
    super(1, LtagNodeType.NON_TERMINAL, category, null, null);
  }

  /**
   * Constructs a new non-terminal node with no marker.
   * @param id the node id.
   * @param category the syntax category.
   */
  public NonTerminalNode(int id, SyntaxCategory category) {
    super(id, LtagNodeType.NON_TERMINAL, category, null, null);
  }

  /**
   * Constructs a new non-terminal node with default id 1.
   * @param category the syntax category.
   * @param marker the node marker.
   */
  public NonTerminalNode(SyntaxCategory category, LtagNodeMarker marker) {
    super(1, LtagNodeType.NON_TERMINAL, category, marker, null);
  }

  /**
   * Constructs a new non-terminal node with default id 1.
   * @param category the syntax category.
   * @param marker the node marker.
   * @param label the node label.
   */
  public NonTerminalNode(SyntaxCategory category, LtagNodeMarker marker, String label) {
    super(1, LtagNodeType.NON_TERMINAL, category, marker, label);
  }

  /**
   * Constructs a new non-terminal node.
   * @param category the syntax category.
   * @param label the node label.
   */
  public NonTerminalNode(SyntaxCategory category, String label) {
    super(1, LtagNodeType.NON_TERMINAL, category, null, label);
  }

  /**
   * Constructs a new non-terminal node.
   * @param id the node unique id.
   * @param category the syntax category.
   * @param label the node label.
   */
  public NonTerminalNode(int id, SyntaxCategory category, String label) {
    super(id, LtagNodeType.NON_TERMINAL, category, null, label);
  }

  /**
   * Constructs a new non-terminal node.
   * @param id the node unique id.
   * @param category the syntax category.
   * @param marker the node marker.
   */
  public NonTerminalNode(int id, SyntaxCategory category, LtagNodeMarker marker) {
    super(id, LtagNodeType.NON_TERMINAL, category, marker, null);
  }

  /**
   * Constructs a new non-terminal node.
   * @param id the node unique id.
   * @param category the syntax category.
   * @param marker the node marker.
   * @param label the node label.
   */
  public NonTerminalNode(int id, SyntaxCategory category, LtagNodeMarker marker, String label) {
    super(id, LtagNodeType.NON_TERMINAL, category, marker, label);
  }

  /**
   * Parses {@link NonTerminalNode} from string.
   * @param string the string to parse.
   * @return the parsed {@link NonTerminalNode}.
   * @throws IllegalArgumentException when {@code string} cannot be parsed.
   */
  public static NonTerminalNode valueOf(String string) throws IllegalArgumentException {
    if (string == null) throw new IllegalArgumentException();
    Matcher matcher = PATTERN.matcher(string);
    if (!matcher.matches()) throw new IllegalArgumentException();
    SyntaxCategory category = SyntaxCategory.valueOf(matcher.group(1));
    int id = (matcher.group(2) != null) ?
        Integer.valueOf( matcher.group(2)) : 1;
    LtagNodeMarker marker = (matcher.group(3) != null) ?
        LtagNodeMarker.fromSymbol(matcher.group(3)) : null;
    String label = matcher.group(4);
    return new NonTerminalNode(id, category, marker, label);
  }

}
