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

package com.acmutv.ontoqa.core.lexicon.iri;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleNamespace;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

/**
 * This class enumrates notable Lemon IRI.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class Lemon {

  /** http://www.lemon-model.net/lemon# */
  public static final String NAMESPACE = "http://www.lemon-model.net/lemon#";

  /**
   * Recommended prefix for the Lemon namespace: "lemon"
   */
  public static final String PREFIX = "lemon";

  /**
   * An immutable {@link Namespace} constant that represents the Lemon namespace.
   */
  public static final Namespace NS = new SimpleNamespace(PREFIX, NAMESPACE);

  /** http://www.lemon-model.net/lemon#Lexicon */
  public final static IRI LEXICON;

  /** http://www.lemon-model.net/lemon#entry */
  public final static IRI ENTRY;

  /** http://www.lemon-model.net/lemon#Word */
  public final static IRI WORD;

  /** http://www.lemon-model.net/lemon#LexicalEntry */
  public final static IRI LEXICAL_ENTRY;

  /** http://www.lemon-model.net/lemon#canonicalForm */
  public final static IRI CANONICAL_FORM;

  /** http://www.lemon-model.net/lemon#otherForm */
  public final static IRI OTHER_FORM;

  /** http://www.lemon-model.net/lemon#synBehavior */
  public final static IRI SYN_BEHAVIOR;

  /** http://www.lemon-model.net/lemon#sense */
  public final static IRI SENSE;

  /** http://www.lemon-model.net/lemon#writtenRep */
  public final static IRI WRITTEN_REP;

  static {
    ValueFactory factory = SimpleValueFactory.getInstance();
    LEXICON = factory.createIRI(Lemon.NAMESPACE, "Lexicon");
    ENTRY = factory.createIRI(Lemon.NAMESPACE, "entry");
    WORD = factory.createIRI(Lemon.NAMESPACE, "Word");
    LEXICAL_ENTRY = factory.createIRI(Lemon.NAMESPACE, "LexicalEntry");
    CANONICAL_FORM = factory.createIRI(Lemon.NAMESPACE, "canonicalForm");
    OTHER_FORM = factory.createIRI(Lemon.NAMESPACE, "otherForm");
    SYN_BEHAVIOR = factory.createIRI(Lemon.NAMESPACE, "synBehavior");
    SENSE = factory.createIRI(Lemon.NAMESPACE, "sense");
    WRITTEN_REP = factory.createIRI(Lemon.NAMESPACE, "writtenRep");
  }
}
