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

import com.acmutv.ontoqa.core.knowledge.ontology.*;
import com.acmutv.ontoqa.core.knowledge.query.Query;
import com.acmutv.ontoqa.core.knowledge.query.QueryResult;
import com.acmutv.ontoqa.core.knowledge.query.SimpleQueryResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.util.Repositories;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

import java.io.*;
import java.nio.file.*;

/**
 * This class realizes the knowledge management services.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class KnowledgeManager {

  private static final Logger LOGGER = LogManager.getLogger(KnowledgeManager.class);

  /**
   * Reads an ontology from a resource.
   * @param resource the resource to read.
   * @param prefix the default prefix for the ontology.
   * @param format the ontology format.
   * @return the ontology.
   * @throws IOException when ontology cannot be read.
   */
  public static Ontology read(String resource, String prefix, OntologyFormat format) throws IOException {
    LOGGER.traceEntry("resource={} prefix={} format={}", resource, prefix, format);
    Ontology ontology = null;
    Path path = FileSystems.getDefault().getPath(resource).toAbsolutePath();
    try (InputStream in = Files.newInputStream(path)) {
      Model model = Rio.parse(in, prefix, format.getFormat());
      ontology.merge(model);
    }
    return LOGGER.traceExit(ontology);
  }

  /**
   * Writes an ontology on a resource.
   * @param resource the resource to write on.
   * @param ontology the ontology to write.
   * @param format the ontology format.
   * @throws IOException when ontology cannot be written.
   */
  public static void write(String resource, Ontology ontology, OntologyFormat format) throws IOException {
    LOGGER.traceEntry("resource={} ontology={} format={}", resource, ontology, format);
    Path path = FileSystems.getDefault().getPath(resource).toAbsolutePath();
    try (OutputStream out = Files.newOutputStream(path)) {
      Rio.write(ontology, out, format.getFormat());
    }
  }

  /**
   * Submits a query to an ontology and retrieves the result.
   * @param query the query to submit.
   * @param ontology the ontology to address.
   * @return the query result.
   */
  public static QueryResult submit(Query query, Ontology ontology) {
    LOGGER.traceEntry("query={} ontology={}", query, ontology);

    QueryResult result = new SimpleQueryResult();

    Repository repo = new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore()));

    repo.initialize();

    Repositories.consume(repo, new OntologyFiller(ontology));

    Repositories.consume(repo, new OntologyQuerySubmitter(query, result));

    repo.shutDown();

    return LOGGER.traceExit(result);
  }

}
