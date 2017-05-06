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
    Dudes template = new SimpleDudes();

    Variable varX = new Variable(1); // x
    Variable varY = new Variable(2); // y

    Drs drs = new SimpleDrs(0);
    drs.getStatements().add(new Replace(varY, varX)); // y = x

    template.setMainDrs(drs);
    template.getSlots().add(new Slot(varX, anchor1, 0)); // (x,anchor1)
    template.getSlots().add(new Slot(varY, anchor2, 0)); // (y,anchor2)

    return template;
  }

  public static Dudes copulaInterrogative(String anchor1, String anchor2) {
    Dudes template = new SimpleDudes();

    Variable varX = new Variable(1); // x
    Variable varY = new Variable(2); // y

    Drs drs = new SimpleDrs(0);
    drs.getStatements().add(new Replace(varY, varX)); // y = x

    template.setMainDrs(drs);
    template.getSlots().add(new Slot(varX, anchor1, 0)); // (x,anchor1)
    template.getSlots().add(new Slot(varY, anchor2, 0)); // (y,anchor2)

    template.setSelect(false);

    return template;
  }

  public static Dudes determiner(String anchor) {
    Dudes template = new SimpleDudes();

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
  
  public static Dudes where()
  {
	  return wh(null, null);
  }
  
  public static Dudes wh()
  {
	  return wh(null, null);
  }

  /**
   * 
   * @param p
   * @param c
   * @return
   */
  private static Dudes wh(String p, String c) {
    Dudes template = new SimpleDudes();

    Variable varX = new Variable(1); // x

    Drs drs = new SimpleDrs(0);
    drs.getVariables().add(varX);

    if(p != null && c != null) {
      drs.getStatements().add(new Proposition(new Constant(p), varX, new Constant(c)));
    }

    template.setMainDrs(drs);
    template.getProjection().add(varX);
    template.setMainVariable(varX);

    return template;
  }

  /**
   * 
   * @param anchor
   * @return
   */
  public static Dudes which(String anchor) {
    Dudes template = new SimpleDudes();

    Variable var = new Variable(1); // x

    Drs drs = new SimpleDrs(0);
    drs.getVariables().add(var);

    template.setMainDrs(drs);
    template.setMainVariable(var);
    template.getProjection().add(var);
    template.getSlots().add(new Slot(var,anchor));

    return template;
  }

/**
 * 
 * @param anchor
 * @return
 */
  public static Dudes howmany(String anchor) {
    Dudes template = new SimpleDudes();

    Variable varX = new Variable(1); // x

    Drs drs = new SimpleDrs(0);
    drs.getVariables().add(varX);

    template.setMainDrs(drs);
    template.getProjection().add(new Function(FunctionType.COUNT, varX));
    template.setMainVariable(varX);
    template.getSlots().add(new Slot(varX, anchor));

    return template;
  }

  /**
   * 
   * @param n
   * @return
   */
  public static Dudes num(int n) {
    Dudes template = new SimpleDudes();

    Variable var = new Variable(1); // x

    SimpleDrs drs = new SimpleDrs(0);
    drs.getVariables().add(var);
    drs.getStatements().add(new Replace(var,new Constant(""+n, ConstantType.INT)));

    template.setMainDrs(drs);
    template.setMainVariable(var);

    return template;
  }

  /**
   * Generates a DUDES representing a proper noun.
   * @param entityIRI the IRI for the proper noun.
   * @return the DUDES representing the specified proper noun.
   */
  public static Dudes properNoun(String entityIRI) {
    Dudes template = new SimpleDudes();

    Variable varX = new Variable(1); // x

    Constant entity = new Constant(entityIRI); // E

    Drs drs = new SimpleDrs(0);
    drs.getVariables().add(varX);
    drs.getStatements().add(new Replace(varX, entity)); // x = E

    template.setMainDrs(drs);
    template.setMainVariable(varX);

    return template;
  }

  /**
   * Generates a DUDES representing a class noun.
   * @param predicateIRI the IRI for the class predicate.
   * @param generic whether or not the class noun must be generic.
   * @return the DUDES representing the specified class noun.
   */
  public static Dudes classNoun(String predicateIRI, boolean generic) {
    Dudes template = new SimpleDudes();

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


  /**
   * Generates a DUDES representing a relational noun.
   * @param propertyIRI the IRI for the property predicate.
   * @param subjectAnchor the anchor for the prepositional subject.
   * @param generic whether or not the class noun must be generic.
   * @return the DUDES representing the specified relational noun.
   */
  @Deprecated
  public static Dudes relationalNoun(String propertyIRI, String subjectAnchor, boolean generic) {
    Dudes template = new SimpleDudes();

    Variable varX = new Variable(1); // x
    Variable varY = new Variable(2); // y

    Constant predicate = new Constant(propertyIRI); // P

    Drs drs = new SimpleDrs(0);
    if (generic) {
      drs.getVariables().add(varY);
    }
    drs.getStatements().add(new Proposition(predicate, varX, varY)); // P(x,y)

    template.setMainDrs(drs);
    template.setMainVariable(varX);
    template.getSlots().add(new Slot(varY, subjectAnchor, 0)); // (x,subjectAnchor)

    return template;
  }

  /**
   * Generates a DUDES representing a relational noun.
   * @param propertyIRI the IRI for the property predicate.
   * @param subjectAnchor the anchor for the prepositional subject.
   * @param generic whether or not the class noun must be generic.
   * @return the DUDES representing the specified relational noun.
   */
  public static Dudes relationalNoun_bis(String propertyIRI, String subjectAnchor, boolean generic) {
    Dudes template = new SimpleDudes();

    Variable varX = new Variable(1); // x
    Variable varY = new Variable(2); // y

    Constant predicate = new Constant(propertyIRI); // P

    Drs drs = new SimpleDrs(0);
    if (generic) {
      drs.getVariables().add(varY);
    }
    drs.getStatements().add(new Proposition(predicate, varX, varY)); // P(x,y)

    template.setMainDrs(drs);
    template.setMainVariable(varY);
    template.getSlots().add(new Slot(varX, subjectAnchor, 0)); // (x,subjectAnchor)

    return template;
  }

  /**
   * Generates a DUDES representing a relational noun.
   * @param propertyIRI the IRI for the property predicate.
   * @param objectAnchor the anchor for the prepositional object.
   * @param generic whether or not the class noun must be generic.
   * @return the DUDES representing the specified relational noun.
   */
  @Deprecated
  public static Dudes relationalNounInverse(String propertyIRI, String objectAnchor, boolean generic) {
    Dudes template = new SimpleDudes();

    Variable varX = new Variable(1); // x
    Variable varY = new Variable(2); // y

    Constant predicate = new Constant(propertyIRI); // P

    Drs drs = new SimpleDrs(0);
    if (generic) {
      drs.getVariables().add(varY);
    }
    drs.getStatements().add(new Proposition(predicate, varX, varY)); // P(x,y)

    template.setMainDrs(drs);
    template.setMainVariable(varX);
    template.getSlots().add(new Slot(varY, objectAnchor, 0)); // (y,objectAnchor)

    return template;
  }

  /**
   * Generates a DUDES representing an intransitive verb.
   * @param predicateIRI the IRI for the predicate.
   * @param objectAnchor the anchor for the predicate object.
   * @return the DUDES representing the specified intransitive verb.
   */
  public static Dudes intransitiveVerb(String predicateIRI, String objectAnchor) {
    Dudes template = new SimpleDudes();

    Variable varX = new Variable(1); // x
    Variable varY = new Variable(2); // y

    Constant predicate = new Constant(predicateIRI); // P

    Drs drs = new SimpleDrs(0);
    drs.getVariables().add(varX);
    drs.getStatements().add(new Proposition(predicate, varX, varY)); // P(x,y)

    template.setMainDrs(drs);
    template.getSlots().add(new Slot(varY, objectAnchor, 0)); // (y,objectAnchor)

    return template;
  }

  /**
   * Generates a DUDES representing a classing intransitive verb.
   * @param predicateIRI the IRI for the predicate.
   * @param subjectAnchor the anchor for the predicate subject.
   * @return the DUDES representing the specified classing intransitive verb.
   */
  public static Dudes intransitiveVerbClassing(String predicateIRI, String subjectAnchor) {
    Dudes template = new SimpleDudes();

    Variable varX = new Variable(1); // x

    Constant predicate = new Constant(predicateIRI); // P

    Drs drs = new SimpleDrs(0);
    drs.getStatements().add(new Proposition(predicate, varX)); // P(x)

    template.setMainDrs(drs);
    template.getSlots().add(new Slot(varX, subjectAnchor, 0)); // (x,subjectAnchor)

    return template;
  }

  /**
   * Generates a DUDES representing a transitive verb.
   * @param predicateIRI the IRI for the predicate.
   * @param subjectAnchor the anchor for the predicate subject.
   * @param objectAnchor the anchor for the predicate object.
   * @return the DUDES representing the specified transitive verb.
   */
  public static Dudes transitiveVerb(String predicateIRI, String subjectAnchor, String objectAnchor) {
    Dudes template = new SimpleDudes();

    Variable varX = new Variable(1); // x
    Variable varY = new Variable(2); // y

    Constant predicate = new Constant(predicateIRI); // P

    Drs drs = new SimpleDrs(0);
    drs.getStatements().add(new Proposition(predicate, varX, varY)); // P(x,y)

    template.setMainDrs(drs);
    if (subjectAnchor == null) {
      template.setMainVariable(varX); // main: X
      template.getSlots().add(new Slot(varY, objectAnchor, 0)); // (y,objectAnchor)
    } else if (objectAnchor == null) {
      template.setMainVariable(varY); // main: Y
      template.getSlots().add(new Slot(varX, subjectAnchor, 0)); // (x,subjectAnchor)
    } else {
      template.getSlots().add(new Slot(varX, subjectAnchor, 0)); // (x,subjectAnchor)
      template.getSlots().add(new Slot(varY, objectAnchor, 0)); // (y,objectAnchor)
    }

    return template;
  }

  /**
   * Generates a DUDES representing a transitive verb (prepositional).
   * @param positivePredicateIRI the IRI for the positive predicate.
   * @param negativePredicateIRI the IRI for the negative predicate.
   * @param subjectAnchor the anchor for the predicate subject.
   * @param positiveObjectAnchor the anchor for the positive predicate object.
   * @param positiveObjectAnchor the anchor for the negative predicate object.
   * @return the DUDES representing the specified transitive verb (prepositional).
   */
  public static Dudes transitivePrepositionalVerb(String positivePredicateIRI, String negativePredicateIRI,
                                                  String subjectAnchor,
                                                  String positiveObjectAnchor,
                                                  String negativeObjectAnchor) {
    Dudes template = new SimpleDudes();

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

  /**
   * Generates a DUDES representing an adjective.
   * @param predicateIRI the IRI for the predicate.
   * @return the DUDES representing the specified adjective.
   */
  public static Dudes adjectivePP(String predicateIRI, String subjectAnchor, String objectAnchor){
    Dudes template = new SimpleDudes();

    Variable varX = new Variable(1); // x
    Variable varY = new Variable(2); // y
   
    Constant predicate = new Constant(predicateIRI); // P

    Drs drs = new SimpleDrs(0);
    drs.getStatements().add(new Proposition(predicate, varX, varY)); // P1(z,y)
    
    if (subjectAnchor == null) {
      template.getSlots().add(new Slot(varY, objectAnchor, 0)); // (y,objectAnchor)
    } else if (objectAnchor == null) {
      template.getSlots().add(new Slot(varX, subjectAnchor, 0)); // (x,subjectAnchor)
    } else {
      template.getSlots().add(new Slot(varX, subjectAnchor, 0)); // (x,subjectAnchor)
      template.getSlots().add(new Slot(varY, objectAnchor, 0)); // (y,objectAnchor)
    }
    template.setMainDrs(drs);
    return template;
    
  }
  
  
  /**
   * Generates a DUDES representing an adjective.
   * @param predicateIRI the IRI for the predicate.
   * @return the DUDES representing the specified adjective.
   */
  public static Dudes adjective(String predicateIRI) {
    Dudes template = new SimpleDudes();

    Variable varX = new Variable(1); // x
   

    Constant predicate = new Constant(predicateIRI); // P
    Constant trueLiteral = new Constant("true", ConstantType.BOOLEAN);

    Drs drs = new SimpleDrs(0);
    drs.getVariables().add(varX);
    
    drs.getStatements().add(new Proposition(predicate, varX, trueLiteral)); // P(x,true)

    template.setMainDrs(drs);
    template.setMainVariable(varX);
    return template;
    
  }
  
  /**
   * Generates a DUDES representing an adjective with restriction in the reference.
   * @param propertyIRI the IRI of the property.
   * @param entityIRI  the IRI of the hasValue
   * @return the DUDES representing the specified adjective.
   */
  public static Dudes adjectiveWithRestriction(String propertyIRI, String entityIRI) {
    Dudes template = new SimpleDudes();

    Variable varX = new Variable(1); // x
   

    Constant property= new Constant(propertyIRI); // P
    Constant entity = new Constant(entityIRI);

    Drs drs = new SimpleDrs(0);
    drs.getVariables().add(varX);
	 Variable varY = new Variable(2); // y
    
    drs.getStatements().add(new Proposition(property, varX, varY)); // P(x,true)
    drs.getStatements().add(new Replace(varY, entity)); // y = E

   drs.getVariables().add(varY);

    template.setMainDrs(drs);
    template.setMainVariable(varX);
    
    return template;
    
  }

  /**
   * Generates a DUDES representing a comparative adjective.
   * @param op the comparison operator.
   * @param predicateIRI the IRI for the predicate.
   * @param subjectAnchor the subject anchor.
   * @param comparisonAnchor the comparative object anchor.
   * @return the DUDES representing the specified comparative adjective.
   */
  public static Dudes adjectiveComparative(OperatorType op, String predicateIRI,
                                           String subjectAnchor, String comparisonAnchor) {
    Dudes template = new SimpleDudes();

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
    template.getSlots().add(new Slot(varX, subjectAnchor, 0));    // (x,subjectAnchor)
    template.getSlots().add(new Slot(varY, comparisonAnchor, 0)); // (y,comparisonAnchor)

    return template;
  }

  /**
   * Generates a DUDES representing a superlative adjective.
   * @param op the comparison operator.
   * @param predicateIRI the IRI for the predicate.
   * @param subjectAnchor the subject anchor.
   * @return the DUDES representing the specified superlative adjective.
   */
  public static Dudes adjectiveSuperlative(OperatorType op,
                                           String predicateIRI,
                                           String subjectAnchor) {
    Dudes template = new SimpleDudes();

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

  /**
   * Generates a DUDES representing an undeterminative determiner.
   * @param subjectAnchor the subject anchor.
   * @return the DUDES representing the specified undeterminative determiner.
   */
  public static Dudes articleUndeterminative(String subjectAnchor) {
    Dudes template = new SimpleDudes();

    Variable varX = new Variable(1); // x

    Drs drs = new SimpleDrs(0);
    drs.getVariables().add(varX);

    template.setMainDrs(drs);
    template.setMainVariable(varX);
    template.getSlots().add(new Slot(varX, subjectAnchor, 0)); // (x,subjectAnchor)

    return template;
  }

  /**
   * Generates a DUDES representing the existence of an entity {@code X} for which holds the
   * {@code predicateIRI} with object {@code objectIRI}.
   * @param typeOfIRI the IRI for the RDF type predicate.
   * @param classIRI the IRI for the class predicate.
   * @return the DUDES representing the specified undeterminative determiner.
   */
  public static Dudes type(String typeOfIRI, String classIRI) {
    Dudes template = new SimpleDudes();

    Drs drs = new SimpleDrs(0);
    Variable varP = new Variable(1); // p
    Variable varX = new Variable(2); // x
    Variable varY = new Variable(3); // y

    Constant property = new Constant(typeOfIRI); // P
    Constant object = new Constant(classIRI); // E

    drs.getStatements().add(new Proposition(varP, varX, varY)); // P(x,y)

    template.setMainDrs(drs);
    template.setMainVariable(varX);

    template.replace(varP, property);
    template.replace(varY, object);

    return template;
  }

  /**
   * Generates a DUDES representing a {@code predicateIRI} that holds for {@code subjectAnchor} and
   * {@code objectAnchor}.
   * @param predicateIRI the IRI for the predicate.
   * @param subjectAnchor the anchor for the predicate subject.
   * @param objectAnchor the anchor for the predicate object.
   * @return the DUDES representing the specified undeterminative determiner.
   */
  public static Dudes property(String predicateIRI, String subjectAnchor, String objectAnchor) {
    Dudes template = new SimpleDudes();

    Variable varP = new Variable(1); // P
    Variable varX = new Variable(2); // x
    Variable varY = new Variable(3); // y

    Constant predicate = new Constant(predicateIRI); // P

    Drs drs = new SimpleDrs(0);
    drs.getStatements().add(new Proposition(varP, varX, varY)); // P(x,y)

    template.setMainDrs(drs);

    if (subjectAnchor == null) {
      template.setMainVariable(varX);
    } else {
      template.getSlots().add(new Slot(varX, subjectAnchor, 0)); // (x,subjectAnchor)
    }

    if (objectAnchor == null) {
      template.setMainVariable(varY);
    } else {
      template.getSlots().add(new Slot(varY, objectAnchor, 0)); // (y,ObjectAnchor)
    }

    template.replace(varP, predicate);

    return template;
  }

  /**
   * Generates a DUDES representing a {@code predicateIRI} that holds for {@code subjectAnchor} and
   * {@code objectAnchor}.
   * @param predicateIRI the IRI for the predicate.
   * @return the DUDES representing the specified undeterminative determiner.
   */
  public static Dudes propertyEmpty(String predicateIRI) {
    Dudes template = new SimpleDudes();

    Variable varP = new Variable(1); // P
    Variable varX = new Variable(2); // x
    Variable varY = new Variable(3); // y

    Constant predicate = new Constant(predicateIRI); // P

    Drs drs = new SimpleDrs(0);
    drs.getStatements().add(new Proposition(varP, varX, varY)); // P(x,y)

    template.setMainDrs(drs);

    template.setMainVariable(varX);

    template.replace(varP, predicate);

    return template;
  }

  /**
   * Generates a DUDES representing a {@code predicateIRI} that holds for {@code subjectAnchor} and
   * {@code objectAnchor}.
   * @param predicateIRI the IRI for the predicate.
   * @param objectIRI the IRI for the predicate object.
   * @return the DUDES representing the specified undeterminative determiner.
   */
  public static Dudes propertyObjectValued(String predicateIRI, String objectIRI) {
    Dudes template = new SimpleDudes();

    Variable varP = new Variable(1); // P
    Variable varX = new Variable(2); // x
    Variable varY = new Variable(3); // y

    Constant predicate = new Constant(predicateIRI); // P
    Constant object = new Constant(objectIRI); // y (IRI)

    Drs drs = new SimpleDrs(0);
    /* BUGFIX by gmarciani: START */
    //drs.getVariables().add(varX);
    /* BUGFIX by gmarciani: END */
    drs.getStatements().add(new Proposition(varP, varX, varY)); // P(x,y)

    template.setMainDrs(drs);
    template.setMainVariable(varX);

    template.replace(varP, predicate);
    template.replace(varY, object);

    return template;
  }

  /**
   * Generates a DUDES representing a {@code predicateIRI} that holds for {@code subjectAnchor} and
   * {@code objectAnchor}.
   * @param predicateIRI the IRI for the predicate.
   * @param objectIRI the IRI for the predicate object.
   * @return the DUDES representing the specified undeterminative determiner.
   */
  public static Dudes propertyObjectValued_bis(String predicateIRI, String objectIRI) {
    Dudes template = new SimpleDudes();

    Variable varP = new Variable(1); // P
    Variable varX = new Variable(2); // x
    Variable varY = new Variable(3); // y

    Constant predicate = new Constant(predicateIRI); // P
    Constant object = new Constant(objectIRI); // y (IRI)

    Drs drs = new SimpleDrs(0);
    drs.getStatements().add(new Proposition(varP, varX, varY)); // P(x,y)

    template.setMainDrs(drs);
    template.setMainVariable(varX);

    template.replace(varP, predicate);
    template.replace(varY, object);

    return template;
  }

  /**
   * Generates a DUDES representing an empty meaning.
   * @return the DUDES representing an emoty meaning.
   */
  public static Dudes empty() {
    Dudes template = new SimpleDudes();

    Drs drs = new SimpleDrs(0);

    template.setMainDrs(drs);

    return template;
  }
}
