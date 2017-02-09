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

import com.acmutv.ontoqa.core.syntax.POS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Part-Of-Speech (POS) node.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see LtagNode
 * @see Ltag
 */
public class PosNode extends LtagNode {

  public static final String REGEXP = "^\\((\\w+),(\\w+)\\)(\\^|\\*){0,1}$";

  private static final Pattern PATTERN = Pattern.compile(REGEXP);

  /**
   * Constructs a new POS node with no marker.
   * @param id the node unique id.
   * @param pos the POS class.
   */
  public PosNode(String id, POS pos) {
    this(id, pos, null);
  }

  /**
   * Constructs a new POS node.
   * @param id the node unique id.
   * @param pos the node POS class.
   * @param marker the node marker.
   */
  public PosNode(String id, POS pos, Marker marker) {
    super(id, Type.POS, pos.name(), marker);
  }

  @Override
  public String toString() {
    return String.format("(%s,%s)%s",
        super.id, this.label, (this.marker != null) ? this.marker.getSymbol() : "");
  }

  /**
   * Parses {@link PosNode} from string.
   * @param string the string to parse.
   * @return the parsed {@link PosNode}.
   * @throws IllegalArgumentException when {@code string} cannot be parsed.
   */
  public static PosNode valueOf(String string) throws IllegalArgumentException {
    if (string == null) throw new IllegalArgumentException();
    Matcher matcher = PATTERN.matcher(string);
    if (!matcher.matches()) throw new IllegalArgumentException();
    String id = matcher.group(1);
    POS pos = POS.valueOf(matcher.group(2));
    Marker marker = (matcher.group(3) != null) ?
        Marker.fromSymbol(matcher.group(3)) : null;
    return new PosNode(id, pos, marker);
  }

}
