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

package com.acmutv.ontoqa.core.knowledge;

import com.acmutv.ontoqa.core.knowledge.ontology.Ontology;
import com.acmutv.ontoqa.core.knowledge.ontology.SimpleOntology;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

/**
 * Common tasks for JUnit tests related to ontologies.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class Commons {

  public static Ontology buildOntology(int type, String ontologyName) {
    Ontology ontology = new SimpleOntology(ontologyName);

    String px = "example";
    String ns = "http://example.org/";

    ontology.setNamespace(px, ns);

    ValueFactory vf = SimpleValueFactory.getInstance();
    IRI mortalIRI = vf.createIRI(ns, "Mortal");
    IRI personIRI = vf.createIRI(ns, "Person");
    IRI socratesIRI = vf.createIRI(ns, "Socrates");
    IRI elonmuskIRI = vf.createIRI(ns, "Elon_Musk");

    ontology.add(mortalIRI, RDF.TYPE, RDFS.CLASS);
    ontology.add(mortalIRI, RDFS.LABEL, vf.createLiteral("Mortal", "en"));
    ontology.add(mortalIRI, RDFS.LABEL, vf.createLiteral("Mortale", "it"));

    ontology.add(personIRI, RDF.TYPE, RDFS.CLASS);
    ontology.add(personIRI, RDFS.LABEL, vf.createLiteral("Person", "en"));
    ontology.add(personIRI, RDFS.LABEL, vf.createLiteral("Persona", "it"));
    ontology.add(personIRI, RDFS.SUBCLASSOF, mortalIRI);

    if (type == 1 || type == 3) {
      ontology.add(socratesIRI, RDF.TYPE, personIRI);
      ontology.add(socratesIRI, RDFS.LABEL, vf.createLiteral("Socrates", "en"));
      ontology.add(socratesIRI, RDFS.LABEL, vf.createLiteral("Socrate", "it"));
    }

    if (type == 2 || type == 3) {
      ontology.add(elonmuskIRI, RDF.TYPE, personIRI);
      ontology.add(elonmuskIRI, RDFS.LABEL, vf.createLiteral("Elon Musk", "en"));
      ontology.add(elonmuskIRI, RDFS.LABEL, vf.createLiteral("Elon Musk", "it"));
    }

    return ontology;
  }

  public static Repository buildRepository() {
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
