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

package com.acmutv.ontoqa.core.knowledge.query;

import lombok.Data;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import java.util.function.Consumer;

/**
 * The task of submitting a {@code SELECT} SPARQL query to a {@link Repository}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see RepositoryConnection
 */
@Data
public class SelectQuerySubmitter implements Consumer<RepositoryConnection> {

  private static final Logger LOGGER = LogManager.getLogger(SelectQuerySubmitter.class);

  /**
   * The {@code SELECT} SPARQL query to submit.
   */
  @NonNull
  private String query;

  /**
   * The result to fill.
   */
  @NonNull
  private QueryResult result;

  /**
   * The variable to retrieve.
   */
  @NonNull
  private String variable;

  /**
   * Submits the {@code SELECT} SPARQL query to the ontology.
   * @param repoConn the connection to the ontology.
   */
  @Override
  public void accept(RepositoryConnection repoConn) {
    TupleQuery query = repoConn.prepareTupleQuery(this.getQuery());
    query.setIncludeInferred(true);
    try (TupleQueryResult queryResults = query.evaluate()) {
      while (queryResults.hasNext()) {
        BindingSet solution = queryResults.next();
        Value value = solution.getValue(this.variable);
        if (value != null) {
          LOGGER.debug("Found value {}", value);
          this.result.add(value);
        }
      }
    }
  }
}
