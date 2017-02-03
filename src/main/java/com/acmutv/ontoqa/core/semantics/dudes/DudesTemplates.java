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
package com.acmutv.ontoqa.core.semantics.dudes;

import com.acmutv.ontoqa.core.semantics.base.slot.Slot;
import com.acmutv.ontoqa.core.semantics.base.statement.OperatorStatement;
import com.acmutv.ontoqa.core.semantics.base.statement.OperatorType;
import com.acmutv.ontoqa.core.semantics.base.statement.Proposition;
import com.acmutv.ontoqa.core.semantics.base.statement.Replace;
import com.acmutv.ontoqa.core.semantics.base.term.*;
import com.acmutv.ontoqa.core.semantics.drs.Drs;
import com.acmutv.ontoqa.core.semantics.drs.SimpleDrs;

/**
 * A collection of common DUDES templates generators.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class DudesTemplates {

  public static Dudes copula(String anchor1, String anchor2) {
    Dudes template = new BaseDudes();

    Variable varX = new Variable(1); // x
    Variable varY = new Variable(2); // y

    Drs drs = new SimpleDrs(0);
    drs.getStatements().add(new Replace(varY, varX)); // y = x

    template.setMainDrs(drs);
    template.getSlots().add(new Slot(varX, anchor1, 0)); // (x,anchor1)
    template.getSlots().add(new Slot(varY, anchor2, 0)); // (y,anchor2)

    return template;
  }

  public static Dudes determiner(String anchor) {
    Dudes template = new BaseDudes();

    Variable varX = new Variable(1); // x

    Drs drs = new SimpleDrs(0);
    drs.getVariables().add(varX);

    template.setMainDrs(drs);
    template.setMainVariable(varX);
    template.getSlots().add(new Slot(varX, anchor, 0)); // (x,anchor)

    return template;
  }

  public static Dudes what() {
    return wh(null, null);
  }

  public static Dudes who() {
    return wh(null, null);
  }

  private static Dudes wh(String p, String c) {
    Dudes template = new BaseDudes();

    Variable var = new Variable(1); // x

    Drs drs = new SimpleDrs(0);
    drs.getVariables().add(var);

    if(p != null && c != null) {
      drs.getStatements().add(new Proposition(new Constant(p), var, new Constant(c)));
    }

    template.setMainDrs(drs);
    template.getProjection().add(var);
    template.setMainVariable(var);

    return template;
  }

  public static Dudes which(String anchor) {
    Dudes template = new BaseDudes();

    Variable var = new Variable(1); // x

    Drs drs = new SimpleDrs(0);
    drs.getVariables().add(var);

    template.setMainDrs(drs);
    template.setMainVariable(var);
    template.getProjection().add(var);
    template.getSlots().add(new Slot(var,anchor));

    return template;
  }

  public static Dudes howmany(String anchor) {
    Dudes template = new BaseDudes();

    Variable var = new Variable(1); // x

    Drs drs = new SimpleDrs(0);
    drs.getVariables().add(var);

    template.setMainDrs(drs);
    template.getProjection().add(new Function(FunctionType.COUNT, var));
    template.setMainVariable(var);
    template.getSlots().add(new Slot(var, anchor));

    return template;
  }

  public static Dudes num(int n) {
    Dudes template = new BaseDudes();

    Variable var = new Variable(1); // x

    SimpleDrs drs = new SimpleDrs(0);
    drs.getVariables().add(var);
    drs.getStatements().add(new Replace(var,new Constant(""+n, ConstantType.INT)));

    template.setMainDrs(drs);
    template.setMainVariable(var);

    return template;
  }

  public static Dudes did() {
    Dudes template = new BaseDudes();

    Drs drs = new SimpleDrs(0);

    template.setMainDrs(drs);

    return template;
  }

  public static Dudes properNoun(String entityUri) {
    Dudes template = new BaseDudes();

    Variable varX = new Variable(1); // x

    Constant entity = new Constant(entityUri); // E

    Drs drs = new SimpleDrs(0);
    drs.getVariables().add(varX);
    drs.getStatements().add(new Replace(varX, entity)); // x = E

    template.setMainDrs(drs);
    template.setMainVariable(varX);

    return template;
  }

  public static Dudes classNoun(String predicateIRI, boolean generic) {
    Dudes template = new BaseDudes();

    Variable varX = new Variable(1); // x

    Constant predicate = new Constant(predicateIRI); // P

    Drs drs = new SimpleDrs(0);
    if (generic) {
      drs.getVariables().add(varX);
    }
    drs.getStatements().add(new Proposition(predicate, varX)); // P(x)

    template.setMainDrs(drs);
    template.setMainVariable(varX);

    return template;
  }

  public static Dudes classNounPrepositional(String noun,
                                            String preposition, String prepositionAnchor,
                                            boolean generic) {
    //TODO
    return null;
  }

  public static Dudes cause(String cause, String causeAnchor) {
    //TODO
    return null;
  }

  public static Dudes relationalNoun(String propertyIRI, String subjectAnchor) {
    Dudes template = new BaseDudes();

    Variable varX = new Variable(1); // x
    Variable varY = new Variable(2); // y

    Constant predicate = new Constant(propertyIRI); // P

    Drs drs = new SimpleDrs(0);
    drs.getStatements().add(new Proposition(predicate, varX, varY)); // P(x,y)

    template.setMainDrs(drs);
    template.setMainVariable(varY);
    template.getSlots().add(new Slot(varX, subjectAnchor, 0)); // (x,subjectAnchor)

    return template;
  }

  public static Dudes intransitiveVerb(String predicateIRI, String subjectAnchor) {
    Dudes template = new BaseDudes();

    Variable varX = new Variable(1); // x
    Variable varY = new Variable(2); // y

    Constant predicate = new Constant(predicateIRI); // P

    Drs drs = new SimpleDrs(0);
    drs.getVariables().add(varX);
    drs.getStatements().add(new Proposition(predicate, varX, varY)); // P(x,y)

    template.setMainDrs(drs);
    template.getSlots().add(new Slot(varY, subjectAnchor, 0)); // (y,subjectAnchor)

    return template;
  }

  public static Dudes intransitiveVerbClassing(String predicateIRI, String objectAnchor) {
    Dudes template = new BaseDudes();

    Variable varX = new Variable(1); // x

    Constant predicate = new Constant(predicateIRI); // P

    Drs drs = new SimpleDrs(0);
    drs.getStatements().add(new Proposition(predicate, varX)); // P(x)

    template.setMainDrs(drs);
    template.getSlots().add(new Slot(varX, objectAnchor, 0)); // (x,objectAnchor)

    return template;
  }

  public static Dudes transitiveVerb(String predicateIRI, String subjectAnchor, String objectAnchor) {
    Dudes template = new BaseDudes();

    Variable varX = new Variable(1); // x
    Variable varY = new Variable(2); // y

    Constant predicate = new Constant(predicateIRI); // P

    Drs drs = new SimpleDrs(0);
    drs.getStatements().add(new Proposition(predicate, varX, varY)); // P(x,y)

    template.setMainDrs(drs);
    if (subjectAnchor == null) {
      template.setMainVariable(varX);
      template.getSlots().add(new Slot(varY, objectAnchor, 0)); // (y,objectAnchor)
    } else if (objectAnchor == null) {
      template.setMainVariable(varY);
      template.getSlots().add(new Slot(varX, subjectAnchor, 0)); // (x,subjectAnchor)
    } else {
      template.getSlots().add(new Slot(varX, subjectAnchor, 0)); // (x,subjectAnchor)
      template.getSlots().add(new Slot(varY, objectAnchor, 0)); // (y,objectAnchor)
    }

    return template;
  }

  public static Dudes transitivePrepositionalVerb(String positivePredicateIRI, String negativePredicateIRI,
                                                  String subjectAnchor,
                                                  String positiveObjectAnchor,
                                                  String negativeObjectAnchor) {
    Dudes template = new BaseDudes();

    Variable varX = new Variable(1); // x
    Variable varY = new Variable(2); // y
    Variable varZ = new Variable(3); // z

    Constant positivePredicate = new Constant(positivePredicateIRI); // P1
    Constant negativePredicate = new Constant(negativePredicateIRI); // P2

    Drs drs = new SimpleDrs(0);
    drs.getStatements().add(new Proposition(positivePredicate, varZ, varX)); // P1(z,x)
    drs.getStatements().add(new Proposition(negativePredicate, varZ, varY)); // P1(z,y)

    template.setMainDrs(drs);
    template.getSlots().add(new Slot(varZ, subjectAnchor, 0));         // (z,subjectAnchor)
    template.getSlots().add(new Slot(varX, positiveObjectAnchor, 0));  // (x,positiveObjectAnchor)
    template.getSlots().add(new Slot(varY, negativeObjectAnchor, 0));  // (y,negativeObjectAnchor)

    return template;
  }

  public static Dudes adjective(String predicateIRI) {
    Dudes template = new BaseDudes();

    Variable varX = new Variable(1); // x

    Constant predicate = new Constant(predicateIRI); // P
    Constant trueLiteral = new Constant("true", ConstantType.BOOLEAN);

    Drs drs = new SimpleDrs(0);
    drs.getStatements().add(new Proposition(predicate, varX, trueLiteral)); // P(x,true)

    template.setMainDrs(drs);
    template.setMainVariable(varX);

    return template;
  }

  public static Dudes adjectiveComparative(OperatorType op,
                                           String predicateIRI,
                                           String wrtAnchor) {
    Dudes template = new BaseDudes();

    Variable varX = new Variable(1); // x
    Variable varY = new Variable(2); // y
    Variable varN1 = new Variable(3); // n1
    Variable varN2 = new Variable(4); // n2

    Constant predicate = new Constant(predicateIRI); // P

    Drs drs = new SimpleDrs(0);
    drs.getStatements().add(new Proposition(predicate, varX, varN1)); // P(x,n1)
    drs.getStatements().add(new Proposition(predicate, varY, varN2)); // P(y,n2)
    drs.getStatements().add(new OperatorStatement(op, varN1, varN2)); // n1 OP n2

    template.setMainDrs(drs);
    template.getSlots().add(new Slot(varY, wrtAnchor, 0)); // (y,wrtAnchor)

    return template;
  }

  public static Dudes adjectiveSuperlative(OperatorType op,
                                           String predicateIRI,
                                           String subjectAnchor) {
    Dudes template = new BaseDudes();

    Variable varX = new Variable(1); // x
    Variable varN1 = new Variable(2); // n

    Constant predicate = new Constant(predicateIRI); // P

    Drs drs = new SimpleDrs(0);
    drs.getStatements().add(new Proposition(predicate, varX, varN1)); // P(x,n)
    drs.getStatements().add(new OperatorStatement(op, varX, varN1));

    template.setMainDrs(drs);
    template.setMainVariable(varX);
    template.getSlots().add(new Slot(varX, subjectAnchor, 0)); // (x,subjectAnchor)

    return template;
  }

  public static Dudes classedProperty(String propertyIRI, String objectIRI) {
    Dudes template = new BaseDudes();

    Drs drs = new SimpleDrs(0);
    Variable var1 = new Variable(1); // p
    Variable var2 = new Variable(2); // x
    Variable var3 = new Variable(3); // y

    Constant property = new Constant(propertyIRI); // P
    Constant object = new Constant(objectIRI); // E

    drs.getStatements().add(new Proposition(var1, var2, var3)); // P(x,y)

    template.setMainDrs(drs);
    template.setMainVariable(var2);

    template.replace(var1, property);
    template.replace(var3, object);

    return template;
  }

  public static Dudes property(String propertyUri, String subjectAnchor, String objectAnchor) {
    Dudes template = new BaseDudes();

    Variable var1 = new Variable(1); // P
    Variable var2 = new Variable(2); // x
    Variable var3 = new Variable(3); // y

    Constant predicate = new Constant(propertyUri); // P

    Drs drs = new SimpleDrs(0);
    drs.getStatements().add(new Proposition(var1, var2, var3)); // P(x,y)

    template.setMainDrs(drs);
    template.getSlots().add(new Slot(var2, subjectAnchor, 0)); // (x,subjectAnchor)
    template.getSlots().add(new Slot(var3, objectAnchor, 0)); // (y,ObjectAnchor)

    template.replace(var1, predicate);

    return template;
  }
}
