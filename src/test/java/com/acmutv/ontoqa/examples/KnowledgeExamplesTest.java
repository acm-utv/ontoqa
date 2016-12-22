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

import com.acmutv.ontoqa.core.knowledge.Commons;
import com.acmutv.ontoqa.core.knowledge.ontology.Ontology;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.OWL;
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
import org.eclipse.rdf4j.repository.util.Repositories;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.junit.Assert;
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
  public void test_ontologyCreation_model() throws IOException {
    String ns = "http://example.org/";

    ValueFactory vf = SimpleValueFactory.getInstance();
    IRI mortalIRI = vf.createIRI(ns, "Mortal");
    IRI personIRI = vf.createIRI(ns, "Person");
    IRI socratesIRI = vf.createIRI(ns, "Socrates");

    Model modelOne = new LinkedHashModel();

    modelOne.add(mortalIRI, RDF.TYPE, RDFS.CLASS);
    modelOne.add(mortalIRI, RDFS.LABEL, vf.createLiteral("Mortal", "en"));
    modelOne.add(mortalIRI, RDFS.LABEL, vf.createLiteral("Mortale", "it"));

    modelOne.add(personIRI, RDF.TYPE, RDFS.CLASS);
    modelOne.add(personIRI, RDFS.LABEL, vf.createLiteral("Person", "en"));
    modelOne.add(personIRI, RDFS.LABEL, vf.createLiteral("Persona", "it"));
    modelOne.add(personIRI, RDFS.SUBCLASSOF, mortalIRI);

    modelOne.add(socratesIRI, RDF.TYPE, personIRI);
    modelOne.add(socratesIRI, RDFS.LABEL, vf.createLiteral("Socrates", "en"));
    modelOne.add(socratesIRI, RDFS.LABEL, vf.createLiteral("Socrate", "it"));

    ModelBuilder modelBuilder = new ModelBuilder();

    Model modelTwo =
        modelBuilder
            .setNamespace("example", ns)
            .subject("example:Mortal")
            .add("rdf:type", "rdfs:Class")
            .add("rdfs:label", vf.createLiteral("Mortal", "en"))
            .add("rdfs:label", vf.createLiteral("Mortale", "it"))
            .subject("example:Person")
            .add("rdf:type", RDFS.CLASS)
            .add("rdfs:label", vf.createLiteral("Person", "en"))
            .add("rdfs:label", vf.createLiteral("Persona", "it"))
            .add("rdfs:subClassOf", "example:Mortal")
            .subject("example:Socrates")
            .add("rdf:type", "example:Person")
            .add("rdfs:label", vf.createLiteral("Socrates", "en"))
            .add("rdfs:label", vf.createLiteral("Socrate", "it"))
            .build();

    Model modelThree = Rio.parse(
        KnowledgeExamplesTest.class.getResourceAsStream("/examples/sample1.ttl"),
        "http://example.org/", RDFFormat.TURTLE);

    Assert.assertEquals(modelOne, modelTwo);
    Assert.assertEquals(modelOne, modelThree);

    Rio.write(modelOne, new OutputStreamWriter(System.out),
        RDFFormat.TURTLE);
  }

  @Test
  public void test_ontologyQuery_model() throws IOException {
    Ontology model = Commons.buildOntology(1);

    System.out.println("\n--------------\n");
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
  public void test_ontologyCreation_repository() {
    String ns = "http://example.org/";

    ValueFactory vf = SimpleValueFactory.getInstance();
    IRI mortalIRI = vf.createIRI(ns, "Mortal");
    IRI personIRI = vf.createIRI(ns, "Person");
    IRI socratesIRI = vf.createIRI(ns, "Socrates");

    Repository repoOne = new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore()));

    repoOne.initialize();

    try (RepositoryConnection repoConn = repoOne.getConnection()) {
      repoConn.add(mortalIRI, RDF.TYPE, RDFS.CLASS);
      repoConn.add(personIRI, RDF.TYPE, RDFS.CLASS);
      repoConn.add(personIRI, RDFS.SUBCLASSOF, mortalIRI);
      repoConn.add(socratesIRI, RDF.TYPE, personIRI);
    }

    Repository repoTwo = new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore()));

    repoTwo.initialize();

    Repositories.consume(repoTwo, repoConn -> {
      try {
        repoConn.add(
            KnowledgeExamplesTest.class.getResource("/examples/sample1.ttl"),
            null, RDFFormat.TURTLE);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

    repoOne.shutDown();
    repoTwo.shutDown();
  }

  @Test
  public void test_ontologyQuery_repository() throws Exception {
    Repository repo = Commons.buildRepository();

    Repositories.consume(repo, repoConn ->
        repoConn.export(Rio.createWriter(RDFFormat.TURTLE, System.out)));

    // METHOD #1
    String ns = "http://example.org/";

    ValueFactory vf = SimpleValueFactory.getInstance();
    IRI socratesIRI = vf.createIRI(ns, "Socrates");
    Set<Value> socratesTypes =
        Repositories.get(repo, repoConn ->
          QueryResults.asModel(
              repoConn.getStatements(socratesIRI, RDF.TYPE, null, true))
              .objects()
        );

    System.out.println(socratesTypes);

    // METHOD #2
    Repositories.consume(repo, repoConn -> {
      TupleQuery query = repoConn.prepareTupleQuery(
          "SELECT ?x WHERE {<http://example.org/Socrates> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?x}");
      query.setIncludeInferred(true);
      try (TupleQueryResult queryResults = query.evaluate()) {
        while (queryResults.hasNext()) {
          BindingSet solution = queryResults.next();
          System.out.println("## " + solution.getValue("x"));
        }
      }
    });

    //METHOD #3
    ParsedTupleQuery parsedQuery =
        QueryBuilderFactory.select("x")
            .group()
            .atom(
                vf.createIRI(ns, "Socrates"),
                RDF.TYPE,
                "x"
            )
            .closeGroup()
            .query();

    SPARQLQueryRenderer sparqlQueryRenderer = new SPARQLQueryRenderer();
    String queryString = sparqlQueryRenderer.render(parsedQuery);

    System.out.println(queryString);
    System.out.println("\n-------\n");

    //noinspection Duplicates
    Repositories.consume(repo, repoConn -> {
      TupleQuery tupleQuery = repoConn.prepareTupleQuery(queryString);
      try (TupleQueryResult queryResult = tupleQuery.evaluate()) {
        while (queryResult.hasNext()) {
          BindingSet bindingSet = queryResult.next();
          System.out.println("## " + bindingSet.getValue("x"));
        }
      }
    });

    repo.shutDown();
  }

  @Test
  public void test_ontologyQuery_repo_withContext() {
    String ns = "http://example.org/";

    Repository repo = new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore()));

    try {
      repo.initialize();

      try (RepositoryConnection repoConn = repo.getConnection()) {
        ValueFactory vf = repoConn.getValueFactory();
        IRI mortalIRI = vf.createIRI(ns, "Mortal");
        IRI personIRI = vf.createIRI(ns, "Person");
        IRI socratesIRI = vf.createIRI(ns, "Socrates");
        IRI socrates2IRI = vf.createIRI(ns, "Socrates2");
        IRI philisophers = vf.createIRI(ns, "Philosophers");
        IRI mathematicians = vf.createIRI(ns, "Mathematicians");
        repoConn.add(mortalIRI, RDF.TYPE, OWL.CLASS);
        repoConn.add(personIRI, RDF.TYPE, OWL.CLASS);
        repoConn.add(personIRI, RDFS.SUBCLASSOF, mortalIRI);
        repoConn.add(socratesIRI, RDF.TYPE, personIRI, philisophers);
        repoConn.add(socrates2IRI, RDF.TYPE, personIRI, mathematicians);
        System.out.println(QueryResults.asSet(repoConn.getStatements(null, RDF.TYPE, personIRI, false)));
        System.out.println(QueryResults.asSet(repoConn.getStatements(null, RDF.TYPE, personIRI, false, (Resource) null)));
        System.out.println(QueryResults.asSet(repoConn.getStatements(null, RDF.TYPE, personIRI, false, (Resource) philisophers)));
        System.out.println(QueryResults.asSet(repoConn.getStatements(null, RDF.TYPE, personIRI, false, (Resource) mathematicians)));

        // do not use inference with null context: it could generate duplicates.
        System.out.println(QueryResults.asSet(repoConn.getStatements(null, RDF.TYPE, personIRI, true)));
      }
    } finally {
      repo.shutDown();
    }
  }

 }
