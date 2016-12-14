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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.parser.ParsedTupleQuery;
import org.eclipse.rdf4j.queryrender.builder.QueryBuilderFactory;
import org.eclipse.rdf4j.queryrender.sparql.SPARQLQueryRenderer;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.repository.util.Repositories;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.Sail;
import org.eclipse.rdf4j.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class realizes JUnit tests for knowledge management examples.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class KnowledgeExamplesTest {

  private static final Logger LOGGER = LogManager.getLogger(KnowledgeExamplesTest.class);

  @Test
  public void test_example1() {
    String ns = "http://example.org/";

    ValueFactory vf = SimpleValueFactory.getInstance();
    IRI mortalIRI = vf.createIRI(ns, "Mortal");
    IRI personIRI = vf.createIRI(ns, "Person");
    IRI socratesIRI = vf.createIRI(ns, "socrates");

    Model model = new LinkedHashModel();

    model.add(mortalIRI, RDF.TYPE, RDFS.CLASS);
    model.add(mortalIRI, RDFS.LABEL, vf.createLiteral("Mortal", "en"));
    model.add(mortalIRI, RDFS.LABEL, vf.createLiteral("Mortale", "it"));

    model.add(personIRI, RDF.TYPE, RDFS.CLASS);
    model.add(personIRI, RDFS.LABEL, vf.createLiteral("Person", "en"));
    model.add(personIRI, RDFS.LABEL, vf.createLiteral("Persona", "it"));
    model.add(personIRI, RDFS.SUBCLASSOF, mortalIRI);

    model.add(socratesIRI, RDF.TYPE, personIRI);
    model.add(socratesIRI, RDFS.LABEL, vf.createLiteral("Socrates", "en"));
    model.add(socratesIRI, RDFS.LABEL, vf.createLiteral("Socrate", "it"));

    Rio.write(model, new OutputStreamWriter(System.out),
        RDFFormat.TURTLE);
  }

  @Test
  public void test_example2() {
    ValueFactory vf = SimpleValueFactory.getInstance();
    ModelBuilder modelBuilder = new ModelBuilder();

    //@formatter:off
    Model model =
        modelBuilder
            .setNamespace("example", "http://example.org/")
            .subject("example:Mortal")
            .add("rdf:type", "rdfs:Class")
            .add("rdfs:label",
                vf.createLiteral("Mortal", "en"))
            .add("rdfs:label", vf.createLiteral("Mortale", "it"))
            .subject("example:Person")
            .add("rdf:type", RDFS.CLASS)
            .add("rdfs:label", vf.createLiteral("Person", "en"))
            .add("rdfs:label", vf.createLiteral("Persona", "it"))
            .add("rdfs:subClassOf", "example:Mortal")
            .subject("example:socrates")
            .add("rdf:type", "example:Person")
            .add("rdfs:label", vf.createLiteral("Socrates", "en"))
            .add("rdfs:label", vf.createLiteral("Socrate", "it"))
            .build();
    //@formatter:on

    Rio.write(model, new OutputStreamWriter(System.out),
        RDFFormat.TURTLE);
  }

  @Test
  public void test_example3() throws IOException {
    Model model = Rio.parse(
        KnowledgeExamplesTest.class.getResourceAsStream("/examples/sample1.ttl"),
        "http://example.org/", RDFFormat.TURTLE);

    System.out.println("--------------\n");
    Rio.write(model, System.out, RDFFormat.RDFXML);
    System.out.println("\n--------------\n");

    Set<Resource> subjects = model.subjects();
    System.out.println(subjects);

    System.out.println("\n--------------\n");

    Set<String> subjects2 = subjects.stream()
        .filter(IRI.class::isInstance).map(IRI.class::cast)
        .map(IRI::getLocalName).collect(Collectors.toSet());
    System.out.println(subjects2);

    System.out.println("\n--------------\n");

    ValueFactory vf = SimpleValueFactory.getInstance();
    Resource socratesResource =
        vf.createIRI("http://example.org/Socrates");

    Set<Value> labels = model
        .filter(socratesResource, RDFS.LABEL, null).objects();

    System.out.println(labels);
  }

  @Test
  public void test_example4() {
    String ns = "http://example.org/";

    ValueFactory vf = SimpleValueFactory.getInstance();
    IRI mortalIRI = vf.createIRI(ns, "Mortal");
    IRI personIRI = vf.createIRI(ns, "Person");
    IRI socratesIRI = vf.createIRI(ns, "socrates");

    Sail sail =
        new ForwardChainingRDFSInferencer(new MemoryStore());
    Repository repo = new SailRepository(sail);
    try {
      repo.initialize();

      try (RepositoryConnection repoConn =
               repo.getConnection()) {
        repoConn.add(personIRI, RDF.TYPE, RDFS.CLASS);
        repoConn.add(mortalIRI, RDF.TYPE, RDFS.CLASS);
        repoConn.add(personIRI, RDFS.SUBCLASSOF, mortalIRI);
        repoConn.add(socratesIRI, RDF.TYPE, personIRI);
      }

      Repositories.consume(repo, repoConn -> {
        repoConn.export(
            Rio.createWriter(RDFFormat.TURTLE, System.out));
      });

      Set<Value> socratesTypes =
          Repositories.get(repo, repoConn -> {
            return QueryResults.asModel(
                repoConn.getStatements(socratesIRI,
                    RDF.TYPE, null, true))
                .objects();
          });

      System.out.println(socratesTypes);
    } finally {
      repo.shutDown();
    }
  }

  @Test
  public void test_example5() {
    Sail sail =
        new ForwardChainingRDFSInferencer(new MemoryStore());
    Repository repo = new SailRepository(sail);
    try {
      repo.initialize();

      Repositories.consume(repo, repoConn -> {
        try {
          repoConn.add(
              KnowledgeExamplesTest.class.getResource("/examples/sample2.ttl"),
              null, RDFFormat.TURTLE);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
        TupleQuery query = repoConn.prepareTupleQuery(
            "SELECT ?x WHERE {?x <http://example.org/locatedIn> <http://example.org/Italy>}");
        query.setIncludeInferred(false);
        try (TupleQueryResult queryResults = query.evaluate()) {
          while (queryResults.hasNext()) {
            BindingSet solution = queryResults.next();
            System.out
                .println("## " + solution.getValue("x"));
          }

        }
      });
    } finally {
      repo.shutDown();
    }
  }

  @Test
  public void test_example6() throws Exception {
    Sail sail =
        new ForwardChainingRDFSInferencer(new MemoryStore());
    Repository repo = new SailRepository(sail);
    try {
      repo.initialize();

      Repositories.consume(repo, repoConn -> {
        try {
          repoConn.add(
              KnowledgeExamplesTest.class.getResource("/examples/sample2.ttl"),
              null, RDFFormat.TURTLE);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });

      ValueFactory vf = repo.getValueFactory();
      // @formatter:off
      ParsedTupleQuery parsedQuery =
          QueryBuilderFactory.select("x")
              .group()
              .atom("x",
                  vf.createIRI("http://example.org/locatedIn"),
                  vf.createIRI("http://example.org/Italy")
              )
              .closeGroup()
              .query();
      // @formatter:on

      SPARQLQueryRenderer sparqlQueryRenderer =
          new SPARQLQueryRenderer();
      String queryString =
          sparqlQueryRenderer.render(parsedQuery);

      System.out.println(queryString);
      System.out.println("\n-------\n");
      Repositories.consume(repo, repoConn -> {
        TupleQuery tupleQuery =
            repoConn.prepareTupleQuery(queryString);
        try (TupleQueryResult queryResult =
                 tupleQuery.evaluate()) {
          while (queryResult.hasNext()) {
            BindingSet bindingSet = queryResult.next();
            System.out.println(
                "## " + bindingSet.getValue("x"));
          }
        }
      });
    } finally {
      repo.shutDown();
    }
  }

  @Test
  public void test_example8() {
    SPARQLRepository repo =
        new SPARQLRepository("http://dbpedia.org/sparql");
    try {
      repo.initialize();
      try (RepositoryConnection repoConn =
               repo.getConnection()) {
        TupleQuery query = repoConn.prepareTupleQuery(
            "select ?x where {<http://dbpedia.org/resource/Albert_Einstein> <http://dbpedia.org/ontology/spouse> ?x}");
        try (TupleQueryResult queryResult = query.evaluate()) {
          while (queryResult.hasNext()) {
            System.out.println(
                "## " + queryResult.next().getValue("x"));
          }
        }
      }
    } finally {
      repo.shutDown();
    }
  }
 }
