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

package com.acmutv.ontoqa.core.semantics.dudes;

import com.acmutv.ontoqa.core.semantics.base.*;
import com.acmutv.ontoqa.core.semantics.drs.Drs;
import com.acmutv.ontoqa.core.semantics.drs.SimpleDrs;

import java.util.ArrayList;
import java.util.List;

/**
 * Collection of common expressions.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class DudesExpressions {

  public static Dudes what() {
    return wh(null, null);
  }

  public static Dudes who() {
    return wh(null, null);
  }

  private static Dudes wh(String p, String c) {
    VariableSupply vars = new VariableSupply();
    Variable var = new Variable(vars.getFresh());
    Dudes wh = new BaseDudes();
    wh.getProjection().add(var);
    wh.setMainVariable(var);
    wh.setMainDrs(0);
    Drs wh_drs = new SimpleDrs(0);
    wh_drs.getVariables().add(var);
    if(p != null && c != null) {
      List<Term> args = new ArrayList<>();
      args.add(var);
      args.add(new Constant(c));
      wh_drs.getStatements().add(new Proposition(new Constant(p), args));
    }

    wh.setDrs(wh_drs);
    return wh;
  }

  public static Dudes which(String anchor) {
    VariableSupply vars = new VariableSupply();
    Variable var = new Variable(vars.getFresh()); // noun

    Dudes which = new BaseDudes();
    which.getProjection().add(var);
    which.setMainVariable(var);
    which.setMainDrs(0);

    Drs which_drs = new SimpleDrs(0);
    which_drs.getVariables().add(var);
    which.setDrs(which_drs);
    which.getSlots().add(new Slot(var,anchor));

    return which;
  }

  public static Dudes copula(String anchor1, String anchor2) {
    VariableSupply vars = new VariableSupply();
    Variable var1 = new Variable(vars.getFresh());
    Variable var2 = new Variable(vars.getFresh());
    Dudes cop = new BaseDudes();
    cop.setMainDrs(0);
    Drs cop_drs = new SimpleDrs(0);
    cop_drs.getStatements().add(new Replace(var2, var1));
    cop.setDrs(cop_drs);
    cop.getSlots().add(new Slot(var1, anchor1));
    cop.getSlots().add(new Slot(var2, anchor2));
    return cop;
  }

  public static Dudes howmany(String anchor) {
    VariableSupply vars = new VariableSupply();
    Variable var = new Variable(vars.getFresh());
    Dudes howmany = new BaseDudes();
    howmany.getProjection().add(new Function(Function.Func.COUNT, var));
    howmany.setMainVariable(var);
    howmany.setMainDrs(0);
    Drs howmany_drs = new SimpleDrs(0);
    howmany_drs.getVariables().add(var);
    howmany.setDrs(howmany_drs);
    howmany.getSlots().add(new Slot(var, anchor));
    return howmany;
  }

  public static Dudes num(int n) {
    VariableSupply vars = new VariableSupply();
    Variable var = new Variable(vars.getFresh());

    Dudes num = new BaseDudes();

    num.setMainDrs(0);
    num.setMainVariable(var);

    SimpleDrs num_drs = new SimpleDrs(0);
    num_drs.getVariables().add(var);
    num_drs.getStatements().add(new Replace(var,new Constant(""+n,Constant.Datatype.INT)));
    num.setDrs(num_drs);

    return num;
  }

  public static Dudes did() {
    VariableSupply vars = new VariableSupply();
    Dudes did = new BaseDudes();
    did.setMainDrs(0);
    new SimpleDrs(0); //TODO can wereplace thi line with SimpleDrs did.setDrs(new SimpleDrs(0));
    return did;
  }

  @Deprecated
  public static Dudes superlative(String anchor, OperatorStatement.Operator op, boolean withProperty) {
    VariableSupply vars = new VariableSupply();
    Variable var1 = new Variable(vars.getFresh());
    Variable var2 = new Variable(vars.getFresh());
    Dudes superlative = new BaseDudes();
    if (withProperty) {
      superlative.setMainVariable(var1);
    } else {
      superlative.setMainVariable(var2);
    }

    superlative.setMainDrs(0);
    Drs superlative_drs = new SimpleDrs(0);
    if (withProperty) {
      List<Term> args = new ArrayList<>();
      args.add(var1);
      args.add(var2);
      superlative_drs.getStatements().add(new Proposition(new Variable(vars.getFresh()), args));
      superlative.getSlots().add(new Slot(var1, anchor));
    }

    superlative_drs.getStatements().add(new OperatorStatement(op, var1, var2));
    superlative.setDrs(superlative_drs);
    return superlative;
  }
}
