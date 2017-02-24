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


import lombok.Data;
import lombok.NonNull;
import org.apache.jena.graph.Node;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprAggregator;
import org.apache.jena.sparql.expr.aggregate.AggCountVarDistinct;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A DUDES/DRS function.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
@Data
public class Function implements Term {

  public static final String REGEXP = "^(COUNT)\\((.+)\\)$";

  private static final Pattern PATTERN = Pattern.compile(REGEXP);

  @NonNull
  private FunctionType funcType;

  @NonNull
  private Term term;

  @Override
  public boolean isVariable() {
        return false;
    }

  @Override
  public boolean isFunction() {
        return true;
    }

  @Override
  public void rename(int i_old, int i_new) {
      this.getTerm().rename(i_old,i_new);
  }

  @Override
  public void rename(String s_old, String s_new) {
      this.getTerm().rename(s_old,s_new);
  }

  @Override
  public Term replace(Term t_old, Term t_new) {
      if (this.equals(t_old)) {
        return t_new;
      }
      if (this.getTerm().equals(t_old)) {
        this.term = t_new;
      }

      return this;
  }

  @Override
  public Node convertToNode(Query top) {

      return this.getTerm().convertToNode(top);
  }

  @Override
  public Expr convertToExpr(Query top) {
    if (this.getTerm().isVariable()) {
      return new ExprAggregator(null,
          new AggCountVarDistinct(this.getTerm().convertToExpr(top)));
    }
    return null;
  }

  @Override
  public String toString() {
    return String.format("%s(%s)", this.getFuncType(), this.getTerm().toString());
  }

  /**
   * Parses {@link Function} from string.
   * @param string the string to parse.
   * @return the parsed {@link Function}.
   * @throws IllegalArgumentException when {@code string} cannot be parsed.
   */
  public static Function valueOf(String string) throws IllegalArgumentException {
    if (string == null) throw new IllegalArgumentException();
    Matcher matcher = PATTERN.matcher(string);
    if (!matcher.matches()) throw new IllegalArgumentException();
    String strType = matcher.group(1);
    String strTerm = matcher.group(2);
    FunctionType type = FunctionType.valueOf(strType);
    Term term = Terms.valueOf(strTerm);
    return new Function(type, term);
  }

  @Override
  public Function clone() {
      return new Function(this.getFuncType(), this.getTerm().clone());
  }
    
}
