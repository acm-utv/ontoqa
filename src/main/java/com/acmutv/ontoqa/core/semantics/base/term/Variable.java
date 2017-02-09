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

package com.acmutv.ontoqa.core.semantics.base.term;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprVar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A DUDES/DRS variable.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
@Data
@AllArgsConstructor
public class Variable implements Term {

  public static final String REGEXP = "^v([0-9]+)$";

  private static final Pattern PATTERN = Pattern.compile(REGEXP);

  private int i;
    
  @Override
  public boolean isVariable() {
    return true;
  }

  @Override
  public boolean isFunction() {
    return false;
  }

  @Override
  public void rename(int i_old, int i_new) {
    if (this.i == i_old) {
      this.i = i_new;
    }
  }

  @Override
  public void rename(String s_old, String s_new) { }

  @Override
  public Term replace(Term t_old, Term t_new) {
    if (this.equals(t_old))  {
      return t_new;
    } else {
      return this;
    }
  }

  @Override
  public Node convertToNode(Query top) {
    return NodeFactory.createVariable(this.toString());
  }

  @Override
  public Expr convertToExpr(Query top) {
    return new ExprVar(this.toString());
  }

  @Override
  public String toString() {
    return String.format("v%d", this.i);
  }

  /**
   * Parses {@link Variable} from string.
   * @param string the string to parse.
   * @return the parsed {@link Variable}.
   * @throws IllegalArgumentException when {@code string} cannot be parsed.
   */
  public static Variable valueOf(String string) throws IllegalArgumentException {
    if (string == null) throw new IllegalArgumentException();
    Matcher matcher = PATTERN.matcher(string);
    if (!matcher.matches()) throw new IllegalArgumentException();
    int value = Integer.valueOf(matcher.group(1));
    return new Variable(value);
  }

  @Override
  public Variable clone() {
      return new Variable(this.getI());
  }
    
}
