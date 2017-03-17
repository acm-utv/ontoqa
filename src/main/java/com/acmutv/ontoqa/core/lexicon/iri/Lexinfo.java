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
 * This class enumerates notable Lexinfo IRI.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class Lexinfo {

  /** http://lexinfo.net/ontology/2.0/lexinfo# */
  public static final String NAMESPACE = "http://www.lexinfo.net/ontology/2.0/lexinfo#";  

  /**
   * Recommended prefix for the Lexinfo namespace: "lexinfo"
   */
  public static final String PREFIX = "lexinfo";

  /**
   * An immutable {@link Namespace} constant that represents the Lexinfo namespace.
   */
  public static final Namespace NS = new SimpleNamespace(PREFIX, NAMESPACE);

  /** http://lexinfo.net/ontology/2.0/lexinfo#partOfSpeech */
  public final static IRI POS;

  /** http://lexinfo.net/ontology/2.0/lexinfo#noun */
  public final static IRI NOUN;

  /** http://lexinfo.net/ontology/2.0/lexinfo#properNoun */
  public final static IRI PROPER_NOUN;

  static {
    ValueFactory factory = SimpleValueFactory.getInstance();
    POS = factory.createIRI(Lexinfo.NAMESPACE, "partOfSpeech");
    NOUN = factory.createIRI(Lexinfo.NAMESPACE, "noun");
    PROPER_NOUN = factory.createIRI(Lexinfo.NAMESPACE, "properNoun");
  }


}
