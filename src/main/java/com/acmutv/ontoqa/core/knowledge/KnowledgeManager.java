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

import com.acmutv.ontoqa.core.exception.QueryException;
import com.acmutv.ontoqa.core.knowledge.ontology.*;
import com.acmutv.ontoqa.core.knowledge.query.AskQuerySubmitter;
import com.acmutv.ontoqa.core.knowledge.query.QueryResult;
import com.acmutv.ontoqa.core.knowledge.query.SelectQuerySubmitter;
import com.acmutv.ontoqa.core.knowledge.query.SimpleQueryResult;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.core.Var;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.util.Repositories;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;

/**
 * The knowledge management services.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class KnowledgeManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(KnowledgeManager.class);

  /**
   * Reads an ontology from a resource.
   * @param resource the resource to read.
   * @param prefix the default prefix for the ontology.
   * @param format the ontology format.
   * @return the ontology.
   * @throws IOException when ontology cannot be read.
   */
  public static Ontology read(String resource, String prefix, OntologyFormat format) throws IOException {
    LOGGER.trace("resource={} prefix={} format={}", resource, prefix, format);
    Ontology ontology = new SimpleOntology();
    Path path = FileSystems.getDefault().getPath(resource).toAbsolutePath();
    try (InputStream in = Files.newInputStream(path)) {
      Model model = Rio.parse(in, prefix, format.getFormat());
      ontology.merge(model);
    }
    return ontology;
  }

  /**
   * Reads an ontology from a reader.
   * @param reader the reader to read.
   * @param prefix the default prefix for the ontology.
   * @param format the ontology format.
   * @return the ontology.
   * @throws IOException when ontology cannot be read.
   */
  public static Ontology read(Reader reader, String prefix, OntologyFormat format) throws IOException {
    LOGGER.trace("reader={} prefix={} format={}", reader, prefix, format);
    Ontology ontology = new SimpleOntology();
    Model model = Rio.parse(reader, prefix, format.getFormat());
    ontology.merge(model);
    return ontology;
  }

  /**
   * Writes an ontology on a resource.
   * @param resource the resource to write on.
   * @param ontology the ontology to write.
   * @param format the ontology format.
   * @throws IOException when ontology cannot be written.
   */
  public static void write(String resource, Ontology ontology, OntologyFormat format) throws IOException {
    LOGGER.trace("resource={} ontology={} format={}", resource, ontology, format);
    Path path = FileSystems.getDefault().getPath(resource).toAbsolutePath();
    try (OutputStream out = Files.newOutputStream(path)) {
      Rio.write(ontology, out, format.getFormat());
    }
  }

  /**
   * Writes an ontology on a writer.
   * @param writer the writer to write on.
   * @param ontology the ontology to write.
   * @param format the ontology format.
   * @throws IOException when ontology cannot be written.
   */
  public static void write(Writer writer, Ontology ontology, OntologyFormat format) throws IOException {
    LOGGER.trace("writer={} ontology={} format={}", writer, ontology, format);
    Rio.write(ontology, writer, format.getFormat());
  }

  /**
   * Submits a SPARQL query to an ontology and retrieves the result.
   * @param ontology the ontology to address.
   * @param query the query to submit.
   * @return the query result.
   * @throws QueryException when the query cannot be submitted.
   */
  public static QueryResult submit(Ontology ontology, Query query) throws QueryException {
    if (query.isAskType()) {
      return submitAsk(ontology, query);
    } else if (query.isSelectType()) {
      return submitSelect(ontology, query);
    } else {
      throw new QueryException("Unrecognized query type.");
    }
  }

  /**
   * Submits a {@code ASK} SPARQL query to an ontology and retrieves the result.
   * @param query the query to submit.
   * @param ontology the ontology to address.
   * @return the query result.
   */
  public static QueryResult submitAsk(Ontology ontology, Query query) {
    LOGGER.trace("query={}", query);

    QueryResult result = new SimpleQueryResult();

    Repository repo = new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore()));

    repo.initialize();

    Repositories.consume(repo, new OntologyFiller(ontology));

    Repositories.consume(repo, new AskQuerySubmitter(query.toString(), result));

    repo.shutDown();

    return result;
  }

  /**
   * Submits a {@code SELECT} SPARQL query to an ontology and retrieves the result.
   * @param query the query to submit.
   * @param ontology the ontology to address.
   * @return the query result.
   */
  public static QueryResult submitSelect(Ontology ontology, Query query) {
    LOGGER.trace("query={}", query);

    QueryResult result = new SimpleQueryResult();

    Repository repo = new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore()));

    repo.initialize();

    Repositories.consume(repo, new OntologyFiller(ontology));

    String variable = getVariableName(query);

    LOGGER.debug("Variable: {}", variable);

    Repositories.consume(repo, new SelectQuerySubmitter(query.toString(), result, variable));

    repo.shutDown();

    return result;
  }

  /**
   * Retrieves the result variable name.
   * @param query the query
   * @return the result variable name.
   */
  public static String getVariableName(Query query) {
    //String varname = query.getProjectVars().get(0).getVarName();
    //String varname = query.getProject().getExpr(query.getProjectVars().get(0)).getVarName();
    String varname;
    if (query.getProject().getExprs().isEmpty()) {
      varname = query.getProjectVars().get(0).getVarName();
    } else {
      varname = query.getProject().getExpr(query.getProjectVars().get(0)).getVarName();
    }
    if (varname == null) {
      for (Var v : query.getProject().getVars()) {
        if (v.getVarName().startsWith("fout")) {
          varname = v.getVarName();
          break;
        }
      }
    }
    return varname;
  }
}
