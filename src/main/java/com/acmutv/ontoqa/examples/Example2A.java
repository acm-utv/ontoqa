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

package com.acmutv.ontoqa.examples;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.util.Repositories;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.Sail;
import org.eclipse.rdf4j.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

import java.util.Set;

/**
 * This class realizes the example 2.A
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @since 1.0
 */
public class Example2A {

  public static void main(String[] args) {
    String ns = "http://example.org/";

    ValueFactory vf = SimpleValueFactory.getInstance();
    IRI mortalIRI = vf.createIRI(ns, "Mortal");
    IRI personIRI = vf.createIRI(ns, "Person");
    IRI socratesIRI = vf.createIRI(ns, "Socrates");

    // buona strategia in cui posso penalizzare le scritture mantenendo efficienti le lettura.
    Sail sail = new ForwardChainingRDFSInferencer(new MemoryStore());
    Repository repo = new SailRepository(sail);

    try {
      repo.initialize();

      try (RepositoryConnection conn = repo.getConnection()) {
        conn.add(mortalIRI, RDF.TYPE, RDFS.CLASS);
        conn.add(mortalIRI, RDFS.LABEL, vf.createLiteral("Mortal","en"));
        conn.add(mortalIRI, RDFS.LABEL, vf.createLiteral("Mortale","it"));

        conn.add(personIRI, RDF.TYPE, RDFS.CLASS);
        conn.add(personIRI, RDFS.LABEL, vf.createLiteral("Person","en"));
        conn.add(personIRI, RDFS.LABEL, vf.createLiteral("Persona","it"));
        conn.add(personIRI, RDFS.SUBCLASSOF, mortalIRI);

        conn.add(socratesIRI, RDF.TYPE, personIRI);
        conn.add(socratesIRI, RDFS.LABEL, vf.createLiteral("Socrates","en"));
        conn.add(socratesIRI, RDFS.LABEL, vf.createLiteral("Socrate","it"));
      }

      Repositories.consume(repo, conn -> conn.export(Rio.createWriter(RDFFormat.TURTLE, System.out)));

      Set<Value> socratesTypes =  Repositories.get(repo, repoConn -> QueryResults.asModel(repoConn.getStatements(socratesIRI, RDF.TYPE, null, true)).objects());

      System.out.println("# RESULT #");
      System.out.println(socratesTypes);

    } finally {
      repo.shutDown();
    }
  }
}
