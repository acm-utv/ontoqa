/*
  The MIT License (MIT)

  Copyright (c) 2017 Antonella Botte, Giacomo Marciani and Debora Partigianoni

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

package com.acmutv.ontoqa.core.semantics.base.slot;

import com.acmutv.ontoqa.core.semantics.base.term.Variable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A DUDES slot.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
@Data
@AllArgsConstructor
public class Slot {

  public static final String REGEXP = "^\\((v[0-9]+),(.+),([0-9]+)\\)$";

  private static final Pattern PATTERN = Pattern.compile(REGEXP);

  /**
   * The variable to substitute.
   */
  @NonNull
  private Variable variable;

  /**
   * The anchor to attach to.
   */
  @NonNull
  private String anchor = "*";

  /**
   * The DRS label.
   */
  private int label = 0;

  public Slot(Variable variable, String anchor) {
    this.variable = variable;
    this.anchor = anchor;
  }

  public void replace(int i_old, int i_new) {
    this.variable.rename(i_old,i_new);
    if (this.label == i_old) {
      this.label = i_new;
    }
  }

  @Override
  public String toString() {
    return String.format("(%s,%s,%d)", this.variable, this.anchor, this.label);
  }

  /**
   * Parses {@link Slot} from string.
   * @param string the string to parse.
   * @return the parsed {@link Slot}.
   * @throws IllegalArgumentException when {@code string} cannot be parsed.
   */
  public static Slot valueOf(String string) throws IllegalArgumentException {
    if (string == null) throw new IllegalArgumentException();
    Matcher matcher = PATTERN.matcher(string);
    if (!matcher.matches()) throw new IllegalArgumentException();
    String strVariable = matcher.group(1);
    String anchor = matcher.group(2);
    String strLabel = matcher.group(3);
    Variable variable = Variable.valueOf(strVariable);
    int label = Integer.valueOf(strLabel);
    return new Slot(variable, anchor, label);
  }

  @Override
  public Slot clone() {
    return new Slot(new Variable(this.variable.getI()),this.anchor,this.label);
  }
    
}
