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

package com.acmutv.ontoqa.core.lexicon;


import com.acmutv.ontoqa.core.lemon.Lexicon;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleNamespace;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

/**
 * This class realizes common tasks for JUnit tests related to lexicons.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class Commons {

//  public static Lexicon buildLexicon(int type) {
//    Lexicon lexicon = new SimpleLexicon();
//
//    Namespace lexiconNs = new SimpleNamespace("exl", "http://example.org/lexicon#");
//    Namespace ontologyNs = new SimpleNamespace("exo", "http://example.org/ontology#");
//
//    lexicon.getNamespaces().add(lexiconNs);
//    lexicon.getNamespaces().add(ontologyNs);
//    lexicon.getNamespaces().add(Lemon.NS);
//    lexicon.getNamespaces().add(Lexinfo.NS);
//
//    ValueFactory vf = SimpleValueFactory.getInstance();
//    IRI exlLexiconIRI = vf.createIRI(lexiconNs.getName(), "lexicon");
//    IRI exlMortalIRI = vf.createIRI(lexiconNs.getName(), "mortal");
//    IRI exlPersonIRI = vf.createIRI(lexiconNs.getName(), "person");
//    IRI exlSocratesIRI = vf.createIRI(lexiconNs.getName(), "Socrates");
//    IRI exlElonMuskIRI = vf.createIRI(lexiconNs.getName(), "Elon_Musk");
//
//    lexicon.add(exlLexiconIRI, RDF.TYPE, Lemon.LEXICON);
//    lexicon.add(exlLexiconIRI, Lemon.ENTRY, exlMortalIRI);
//    lexicon.add(exlLexiconIRI, Lemon.ENTRY, exlPersonIRI);
//
//    lexicon.add(exlMortalIRI, RDF.TYPE, Lemon.WORD);
//    lexicon.add(exlMortalIRI, Lexinfo.POS, Lexinfo.NOUN);
//
//    lexicon.add(exlPersonIRI, RDF.TYPE, Lemon.WORD);
//    lexicon.add(exlPersonIRI, Lexinfo.POS, Lexinfo.NOUN);
//
//    if (type == 1 || type == 3) {
//      lexicon.add(exlLexiconIRI, Lemon.ENTRY, exlSocratesIRI);
//      lexicon.add(exlSocratesIRI, RDF.TYPE, Lemon.LEXICAL_ENTRY);
//      lexicon.add(exlSocratesIRI, Lexinfo.POS, Lexinfo.PROPER_NOUN);
//    }
//
//    if (type == 2 || type == 3) {
//      lexicon.add(exlLexiconIRI, Lemon.ENTRY, exlElonMuskIRI);
//      lexicon.add(exlElonMuskIRI, RDF.TYPE, Lemon.LEXICAL_ENTRY);
//      lexicon.add(exlElonMuskIRI, Lexinfo.POS, Lexinfo.PROPER_NOUN);
//    }
//
//    return lexicon;
//  }

  public static Repository buildLexicon() {
    String ns = "http://example.org/";

    ValueFactory vf = SimpleValueFactory.getInstance();
    IRI mortalIRI = vf.createIRI(ns, "Mortal");
    IRI personIRI = vf.createIRI(ns, "Person");
    IRI socratesIRI = vf.createIRI(ns, "Socrates");

    Repository repo = new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore()));

    repo.initialize();

    try (RepositoryConnection repoConn = repo.getConnection()) {
      repoConn.add(mortalIRI, RDF.TYPE, RDFS.CLASS);
      repoConn.add(personIRI, RDF.TYPE, RDFS.CLASS);
      repoConn.add(personIRI, RDFS.SUBCLASSOF, mortalIRI);
      repoConn.add(socratesIRI, RDF.TYPE, personIRI);
    }

    return repo;
  }
}
