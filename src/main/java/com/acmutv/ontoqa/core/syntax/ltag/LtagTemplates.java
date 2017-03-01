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
package com.acmutv.ontoqa.core.syntax.ltag;

import com.acmutv.ontoqa.core.syntax.SyntaxCategory;

/**
 * A collection of common LTAG templates generators
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class LtagTemplates {

  /**
   * Generates a LTAG representing a proper noun.
   * @param noun the proper noun.
   * @return the LTAG representing the specified proper noun.
   */
  public static Ltag properNoun(String noun) {
    LtagNode dp = new NonTerminalNode(SyntaxCategory.DP);
    LtagNode lex = new TerminalNode(noun);

    Ltag template = new SimpleLtag(dp);
    template.addEdge(dp, lex);

    return template;
  }

  /**
   * Generates a LTAG representing a class noun.
   * @param noun the class noun.
   * @param generic whether or not the class noun must be generic.
   * @return the LTAG representing the specified class noun.
   */
  public static Ltag classNoun(String noun, boolean generic) {
    LtagNode np = new NonTerminalNode(SyntaxCategory.NP);
    LtagNode lex = new TerminalNode(noun);

    Ltag template;

    if (generic) {
      LtagNode dp = new NonTerminalNode(SyntaxCategory.DP);
      template = new SimpleLtag(dp);
      template.addEdge(dp, np);
    } else {
      template = new SimpleLtag(np);
    }

    template.addEdge(np, lex);

    return template;
  }

  /**
   * Generates a LTAG representing a relational prepositional noun.
   * @param noun the class noun.
   * @param preposition the relational preposition.
   * @param anchor the relational anchor.
   * @param generic whether or not the class noun must be generic.
   * @return the LTAG representing the specified prepositional noun.
   */
  public static Ltag relationalPrepositionalNoun(String noun, String preposition, String anchor, boolean generic) {
    LtagNode np = new NonTerminalNode(SyntaxCategory.NP);
    LtagNode n = new NonTerminalNode(SyntaxCategory.N);
    LtagNode pp = new NonTerminalNode(SyntaxCategory.PP);
    LtagNode p = new NonTerminalNode(SyntaxCategory.P);
    LtagNode dp2 = new NonTerminalNode(2, SyntaxCategory.DP, LtagNodeMarker.SUB, anchor);
    LtagNode lex = new TerminalNode(1, noun);
    LtagNode lexOf = new TerminalNode(2, preposition);

    Ltag template;

    if (generic) {
      LtagNode dp1 = new NonTerminalNode(1, SyntaxCategory.DP);
      template = new SimpleLtag(dp1);
      template.addEdge(dp1, np);
    } else {
      template = new SimpleLtag(np);
    }

    template.addEdge(np, n);
    template.addEdge(np, pp);
    template.addEdge(n, lex);
    template.addEdge(pp, p);
    template.addEdge(pp, dp2);
    template.addEdge(p, lexOf);

    return template;
  }

  /**
   * Generates a LTAG representing a relational possessive noun.
   * @param noun the class noun.
   * @param possessive the possessive preposition.
   * @param anchor the relational anchor.
   * @param generic whether or not the class noun must be generic.
   * @return the LTAG representing the specified possessive noun.
   */
  public static Ltag relationalPossessiveNoun(String noun, String possessive, String anchor, boolean generic) {
    LtagNode np = new NonTerminalNode(SyntaxCategory.NP);
    LtagNode dp2 = new NonTerminalNode(2, SyntaxCategory.DP, LtagNodeMarker.SUB, anchor);
    LtagNode poss = new NonTerminalNode(SyntaxCategory.POSS);
    LtagNode n = new NonTerminalNode(SyntaxCategory.N);
    LtagNode lex = new TerminalNode(1, noun);
    LtagNode lexGenitive = new TerminalNode(2, possessive);

    Ltag template;

    if (generic) {
      LtagNode dp1 = new NonTerminalNode(1, SyntaxCategory.DP);
      template = new SimpleLtag(dp1);
      template.addEdge(dp1, np);
    } else {
      template = new SimpleLtag(np);
    }

    template.addEdge(np, dp2);
    template.addEdge(np, poss);
    template.addEdge(np, n);
    template.addEdge(poss, lexGenitive);
    template.addEdge(n, lex);

    return template;
  }

  /**
   * Generates a LTAG representing an intransitive verb.
   * @param verb the verb.
   * @param anchor the subject anchor.
   * @return the LTAG representing the specified intransitive verb.
   */
  public static Ltag intransitiveVerb(String verb, String anchor) {
    LtagNode s = new NonTerminalNode(SyntaxCategory.S);
    LtagNode dp = new NonTerminalNode(SyntaxCategory.DP, LtagNodeMarker.SUB, anchor);
    LtagNode vp = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode lex = new TerminalNode(verb);

    Ltag template = new SimpleLtag(s);
    template.addEdge(s, dp);
    template.addEdge(s, vp);
    template.addEdge(vp, lex);

    return template;
  }

  /**
   * Generates a LTAG representing a transitive verb (active indicative).
   * @param verb the verb.
   * @param subjectAnchor the subject anchor.
   * @param objectAnchor the object anchor.
   * @return the LTAG representing the specified transitive verb (active indicative).
   */
  public static Ltag transitiveVerbActiveIndicative(String verb, String subjectAnchor, String objectAnchor) {
    LtagNode s = new NonTerminalNode(SyntaxCategory.S);
    LtagNode dp1 = new NonTerminalNode(1, SyntaxCategory.DP, LtagNodeMarker.SUB, subjectAnchor);
    LtagNode vp = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode v = new NonTerminalNode(SyntaxCategory.V);
    LtagNode dp2 = new NonTerminalNode(2, SyntaxCategory.DP, LtagNodeMarker.SUB, objectAnchor);
    LtagNode lex = new TerminalNode(verb);

    Ltag template = new SimpleLtag(s);
    template.addEdge(s, dp1);
    template.addEdge(s, vp);
    template.addEdge(vp, v);
    template.addEdge(vp, dp2);
    template.addEdge(v, lex);

    return template;
  }

  /**
   * Generates a LTAG representing a transitive verb (passive indicative).
   * @param verb the verb.
   * @param copula the copula.
   * @param subjectAnchor the subject anchor.
   * @param objectAnchor the object anchor.
   * @return the LTAG representing the specified transitive verb (passive indicative).
   */
  public static Ltag transitiveVerbPassiveIndicative(String verb, String copula, String preposition,
                                                     String subjectAnchor, String objectAnchor) {
    LtagNode s = new NonTerminalNode(SyntaxCategory.S);
    LtagNode dp2 = new NonTerminalNode(2, SyntaxCategory.DP, LtagNodeMarker.SUB, subjectAnchor);
    LtagNode vp = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode v = new NonTerminalNode(SyntaxCategory.V);
    LtagNode ap = new NonTerminalNode(SyntaxCategory.AP);
    LtagNode pp = new NonTerminalNode(SyntaxCategory.PP);
    LtagNode p = new NonTerminalNode(SyntaxCategory.P);
    LtagNode dp1 = new NonTerminalNode(1, SyntaxCategory.DP, LtagNodeMarker.SUB, objectAnchor);
    LtagNode lexCopula = new TerminalNode(copula);
    LtagNode lexVerb = new TerminalNode(1, verb);
    LtagNode lexPreposition = new TerminalNode(2, preposition);

    Ltag template = new SimpleLtag(s);
    template.addEdge(s, dp2);
    template.addEdge(s, vp);
    template.addEdge(vp, v);
    template.addEdge(vp, ap);
    template.addEdge(vp, pp);
    template.addEdge(v, lexCopula);
    template.addEdge(ap, lexVerb);
    template.addEdge(pp, p);
    template.addEdge(pp, dp1);
    template.addEdge(p, lexPreposition);

    return template;
  }

  /**
   * Generates a LTAG representing a transitive verb (active gerundive).
   * @param verb the verb.
   * @param subjectAnchor the subject anchor.
   * @param objectAnchor the object anchor.
   * @return the LTAG representing the specified transitive verb (active gerundive).
   */
  public static Ltag transitiveVerbActiveGerundive(String verb,
                                                   String subjectAnchor, String objectAnchor) {
    LtagNode np1 = new NonTerminalNode(1, SyntaxCategory.NP);
    LtagNode np2 = new NonTerminalNode(2, SyntaxCategory.NP, LtagNodeMarker.ADJ, subjectAnchor);
    LtagNode ap = new NonTerminalNode(SyntaxCategory.AP);
    LtagNode a = new NonTerminalNode(SyntaxCategory.A);
    LtagNode dp2 = new NonTerminalNode(2, SyntaxCategory.DP, LtagNodeMarker.SUB, objectAnchor);
    LtagNode lex = new TerminalNode(verb);

    Ltag template = new SimpleLtag(np1);
    template.addEdge(np1, np2);
    template.addEdge(np1, ap);
    template.addEdge(ap, a);
    template.addEdge(ap, dp2);
    template.addEdge(a, lex);

    return template;
  }

  /**
   * Generates a LTAG representing a transitive verb (passive gerundive).
   * @param verb the verb.
   * @param subjectAnchor the subject anchor.
   * @param objectAnchor the object anchor.
   * @return the LTAG representing the specified transitive verb (passive gerundive).
   */
  public static Ltag transitiveVerbPassiveGerundive(String verb, String preposition,
                                                    String subjectAnchor, String objectAnchor) {
    LtagNode np1 = new NonTerminalNode(SyntaxCategory.NP);
    LtagNode np2 = new NonTerminalNode(2, SyntaxCategory.NP, LtagNodeMarker.ADJ, subjectAnchor);
    LtagNode ap = new NonTerminalNode(SyntaxCategory.AP);
    LtagNode a = new NonTerminalNode(SyntaxCategory.A);
    LtagNode pp = new NonTerminalNode(SyntaxCategory.PP);
    LtagNode p = new NonTerminalNode(SyntaxCategory.P);
    LtagNode dp1 = new NonTerminalNode(1, SyntaxCategory.DP, LtagNodeMarker.SUB, objectAnchor);
    LtagNode lexVerb = new TerminalNode(1, verb);
    LtagNode lexPreposition = new TerminalNode(2, preposition);

    Ltag template = new SimpleLtag(np1);
    template.addEdge(np1, np2);
    template.addEdge(np1, ap);
    template.addEdge(ap, a);
    template.addEdge(ap, pp);
    template.addEdge(a, lexVerb);
    template.addEdge(pp, p);
    template.addEdge(pp, dp1);
    template.addEdge(p, lexPreposition);

    return template;
  }

  /**
   * Generates a LTAG representing a transitive verb (active relative).
   * @param verb the verb.
   * @param pronoun the relational pronoun.
   * @param subjectAnchor the subject anchor.
   * @param objectAnchor the object anchor.
   * @return the LTAG representing the specified transitive verb (active relative).
   */
  public static Ltag transitiveVerbActiveRelative(String verb, String pronoun,
                                                  String subjectAnchor, String objectAnchor) {
    LtagNode np1 = new NonTerminalNode(SyntaxCategory.NP);
    LtagNode np2 = new NonTerminalNode(2, SyntaxCategory.NP, LtagNodeMarker.ADJ, subjectAnchor);
    LtagNode s = new NonTerminalNode(SyntaxCategory.S);
    LtagNode rel = new NonTerminalNode(SyntaxCategory.REL);
    LtagNode vp = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode v = new NonTerminalNode(SyntaxCategory.V);
    LtagNode dp2 = new NonTerminalNode(2, SyntaxCategory.DP, LtagNodeMarker.SUB, objectAnchor);
    LtagNode lexRelative = new TerminalNode(1, pronoun);
    LtagNode lexVerb = new TerminalNode(2, verb);

    Ltag template = new SimpleLtag(np1);
    template.addEdge(np1, np2);
    template.addEdge(np1, s);
    template.addEdge(s, rel);
    template.addEdge(s, vp);
    template.addEdge(rel, lexRelative);
    template.addEdge(vp, v);
    template.addEdge(vp, dp2);
    template.addEdge(v, lexVerb);

    return template;
  }

  /**
   * Generates a LTAG representing a transitive verb (passive relative).
   * @param verb the verb.
   * @param copula the copula.
   * @param pronoun the relational pronoun.
   * @param preposition the relative preposition.
   * @param subjectAnchor the subject anchor.
   * @param objectAnchor the object anchor.
   * @return the LTAG representing the specified transitive verb (passive relative).
   */
  public static Ltag transitiveVerbPassiveRelative(String verb, String copula, String pronoun, String preposition,
                                                   String subjectAnchor, String objectAnchor) {
    LtagNode np1 = new NonTerminalNode(1, SyntaxCategory.NP);
    LtagNode np2 = new NonTerminalNode(2, SyntaxCategory.NP, LtagNodeMarker.ADJ, subjectAnchor);
    LtagNode s = new NonTerminalNode(SyntaxCategory.S);
    LtagNode rel = new NonTerminalNode(SyntaxCategory.REL);
    LtagNode vp = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode v = new NonTerminalNode(SyntaxCategory.V);
    LtagNode ap = new NonTerminalNode(SyntaxCategory.AP);
    LtagNode a = new NonTerminalNode(SyntaxCategory.A);
    LtagNode pp = new NonTerminalNode(SyntaxCategory.PP);
    LtagNode p = new NonTerminalNode(SyntaxCategory.P);
    LtagNode dp1 = new NonTerminalNode(1, SyntaxCategory.DP, LtagNodeMarker.SUB, objectAnchor);
    LtagNode lexRelative = new TerminalNode(1, pronoun);
    LtagNode lexCopula = new TerminalNode(2, copula);
    LtagNode lexVerb = new TerminalNode(3, verb);
    LtagNode lexPreposition = new TerminalNode(4, preposition);

    Ltag template = new SimpleLtag(np1);
    template.addEdge(np1, np2);
    template.addEdge(np1, s);
    template.addEdge(s, rel);
    template.addEdge(s, vp);
    template.addEdge(rel, lexRelative);
    template.addEdge(vp, v);
    template.addEdge(vp, ap);
    template.addEdge(v, lexCopula);
    template.addEdge(ap, a);
    template.addEdge(ap, pp);
    template.addEdge(a, lexVerb);
    template.addEdge(pp, p);
    template.addEdge(pp, dp1);
    template.addEdge(p, lexPreposition);

    return template;
  }

  /**
   * Generates a LTAG representing a transitive verb (prepositional).
   * @param verb the verb.
   * @param preposition the preposition.
   * @param subjectAnchor the subject anchor.
   * @param objectAnchor the object anchor.
   * @param prepositionalObjectAnchor the prepositional object anchor.
   * @return the LTAG representing the specified transitive verb (prepositional).
   */
  public static Ltag transitiveVerbPrepositional(String verb, String preposition,
                                                 String subjectAnchor, String objectAnchor,
                                                 String prepositionalObjectAnchor) {
    LtagNode s = new NonTerminalNode(SyntaxCategory.S);
    LtagNode dp1 = new NonTerminalNode(1, SyntaxCategory.DP, LtagNodeMarker.SUB, subjectAnchor);
    LtagNode vp = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode v = new NonTerminalNode(SyntaxCategory.V);
    LtagNode dp2 = new NonTerminalNode(2, SyntaxCategory.DP, LtagNodeMarker.SUB, objectAnchor);
    LtagNode pp = new NonTerminalNode(SyntaxCategory.PP);
    LtagNode p = new NonTerminalNode(SyntaxCategory.P);
    LtagNode dp3 = new NonTerminalNode(3, SyntaxCategory.DP, LtagNodeMarker.SUB, prepositionalObjectAnchor);
    LtagNode lexVerb = new TerminalNode(1, verb);
    LtagNode lexPreposition = new TerminalNode(2, preposition);


    Ltag template = new SimpleLtag(s);
    template.addEdge(s, dp1);
    template.addEdge(s, vp);
    template.addEdge(vp, v);
    template.addEdge(vp, dp2);
    template.addEdge(vp, pp);
    template.addEdge(v, lexVerb);
    template.addEdge(pp, p);
    template.addEdge(pp, dp3);
    template.addEdge(p, lexPreposition);

    return template;
  }

  /**
   * Generates a LTAG representing an attributive adjective.
   * @param adjective the adjective.
   * @param subjectAnchor the subject anchor.
   * @return the LTAG representing the specified attributive adjective.
   */
  public static Ltag adjectiveAttributive(String adjective, String subjectAnchor) {
    LtagNode n1 = new NonTerminalNode(1, SyntaxCategory.N);
    LtagNode adj = new NonTerminalNode(SyntaxCategory.ADJ);
    LtagNode n2 = new NonTerminalNode(2, SyntaxCategory.N, LtagNodeMarker.ADJ, subjectAnchor);
    LtagNode lex = new TerminalNode(adjective);

    Ltag template = new SimpleLtag(n1);
    template.addEdge(n1, adj);
    template.addEdge(n1, n2);
    template.addEdge(adj, lex);

    return template;
  }

  /**
   * Generates a LTAG representing a predicative adjective.
   * @param adjective the adjective.
   * @param copula the copula.
   * @param subjectAnchor the subject anchor.
   * @return the LTAG representing the specified predicative adjective.
   */
  public static Ltag adjectivePredicative(String adjective, String copula, String subjectAnchor) {
    LtagNode s = new NonTerminalNode(SyntaxCategory.S);
    LtagNode dp1 = new NonTerminalNode(SyntaxCategory.DP, LtagNodeMarker.SUB, subjectAnchor);
    LtagNode vp = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode v = new NonTerminalNode(SyntaxCategory.V);
    LtagNode adj = new NonTerminalNode(SyntaxCategory.ADJ);
    LtagNode lexCopula = new TerminalNode(1, copula);
    LtagNode lexAdjective = new TerminalNode(2, adjective);

    Ltag template = new SimpleLtag(s);
    template.addEdge(s, dp1);
    template.addEdge(s, vp);
    template.addEdge(vp, v);
    template.addEdge(vp, adj);
    template.addEdge(v, lexCopula);
    template.addEdge(adj, lexAdjective);

    return template;
  }

  /**
   * Generates a LTAG representing a comparative adjective.
   * @param adjective the adjective.
   * @param copula the copula.
   * @param comparation the comparative preposition.
   * @param subjectAnchor the subject anchor.
   * @param objectAnchor the object anchor.
   * @return the LTAG representing the specified comparative adjective.
   */
  public static Ltag adjectiveComparative(String adjective, String copula, String comparation,
                                          String subjectAnchor, String objectAnchor) {
    LtagNode s = new NonTerminalNode(SyntaxCategory.S);
    LtagNode dp1 = new NonTerminalNode(1, SyntaxCategory.DP, LtagNodeMarker.SUB, subjectAnchor);
    LtagNode vp = new NonTerminalNode(SyntaxCategory.VP);
    LtagNode v = new NonTerminalNode(SyntaxCategory.V);
    LtagNode ap = new NonTerminalNode(SyntaxCategory.AP);
    LtagNode adj = new NonTerminalNode(SyntaxCategory.ADJ);
    LtagNode pp = new NonTerminalNode(SyntaxCategory.PP);
    LtagNode p = new NonTerminalNode(SyntaxCategory.P);
    LtagNode dp2 = new NonTerminalNode(2, SyntaxCategory.DP, LtagNodeMarker.SUB, objectAnchor);
    LtagNode lexCopula = new TerminalNode(1, copula);
    LtagNode lexAdjective = new TerminalNode(2, adjective);
    LtagNode lexComparation = new TerminalNode(3, comparation);

    Ltag template = new SimpleLtag(s);
    template.addEdge(s, dp1);
    template.addEdge(s, vp);
    template.addEdge(vp, v);
    template.addEdge(vp, ap);
    template.addEdge(v, lexCopula);
    template.addEdge(ap, adj);
    template.addEdge(ap, pp);
    template.addEdge(adj, lexAdjective);
    template.addEdge(pp, p);
    template.addEdge(pp, dp2);
    template.addEdge(p, lexComparation);

    return template;
  }

  /**
   * Generates a LTAG representing a superlative adjective.
   * @param adjective the adjective.
   * @param determiner the determiner.
   * @param subjectAnchor the subject anchor.
   * @return the LTAG representing the specified superlative adjective.
   */
  public static Ltag adjectiveSuperlative(String adjective, String determiner, String subjectAnchor) {
    LtagNode dp = new NonTerminalNode(SyntaxCategory.DP);
    LtagNode det = new NonTerminalNode(SyntaxCategory.DET);
    LtagNode adj = new NonTerminalNode(SyntaxCategory.ADJ);
    LtagNode np = new NonTerminalNode(SyntaxCategory.NP, LtagNodeMarker.SUB, subjectAnchor);
    LtagNode lexDeterminer = new TerminalNode(1, determiner);
    LtagNode lexAdjective = new TerminalNode(2, adjective);

    Ltag template = new SimpleLtag(dp);
    template.addEdge(dp, det);
    template.addEdge(dp, adj);
    template.addEdge(dp, np);
    template.addEdge(det, lexDeterminer);
    template.addEdge(adj, lexAdjective);

    return template;
  }

  /**
   * Generates a LTAG representing a undeterminative article.
   * @param article the article.
   * @param subjectAnchor the subject anchor.
   * @return the LTAG representing the specified undeterminative article.
   */
  public static Ltag articleUndeterminative(String article, String subjectAnchor) {
    LtagNode dp = new NonTerminalNode(SyntaxCategory.DP);
    LtagNode det = new NonTerminalNode(SyntaxCategory.DET);
    LtagNode np = new NonTerminalNode(SyntaxCategory.NP, LtagNodeMarker.SUB, subjectAnchor);
    LtagNode lex = new TerminalNode(article);

    Ltag template = new SimpleLtag(dp);
    template.addEdge(dp, det);
    template.addEdge(dp, np);
    template.addEdge(det, lex);

    return template;
  }

  /**
   * Generates a LTAG representing a numeral.
   * @param numeral the numeral.
   * @return the LTAG representing the specified numeral.
   */
  public static Ltag num(String numeral) {
    LtagNode num = new NonTerminalNode(SyntaxCategory.NUM);
    LtagNode lexNumeral = new TerminalNode(numeral);

    Ltag template = new SimpleLtag(num);
    template.addEdge(num, lexNumeral);

    return template;
  }
}
