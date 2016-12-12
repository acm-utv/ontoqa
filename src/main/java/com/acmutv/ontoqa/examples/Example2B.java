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

import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.util.Repositories;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.sail.Sail;
import org.eclipse.rdf4j.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

import java.io.IOException;

/**
 * This class realizes the example 2.B
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @since 1.0
 */
public class Example2B {

  public static void main(String[] args) {
    String ns = "http://example.org/";

    ValueFactory vf = SimpleValueFactory.getInstance();
    //IRI mortalIRI = vf.createIRI(ns, "Mortal");
    //IRI personIRI = vf.createIRI(ns, "Person");
    //IRI socratesIRI = vf.createIRI(ns, "Socrates");

    Sail sail = new ForwardChainingRDFSInferencer(new MemoryStore());
    Repository repo = new SailRepository(sail);

    try {
      repo.initialize();

      Repositories.consume(repo, repoConn -> {
        try {
          repoConn.add(Example2B.class.getResource("/examples/example2B.ttl"), null, RDFFormat.TURTLE);
        } catch (IOException e) {
          e.printStackTrace();
        }
        TupleQuery query = repoConn.prepareTupleQuery("SELECT ?x WHERE {?x a <http://example.org/Mortal>}");
        query.setIncludeInferred(false);

        try (TupleQueryResult res = query.evaluate()) {
          while (res.hasNext()) {
            BindingSet solution = res.next();
            System.out.println("# RESULT # " + solution.getValue("x"));
          }
        }
      });

    } finally {
      repo.shutDown();
    }
  }
}
