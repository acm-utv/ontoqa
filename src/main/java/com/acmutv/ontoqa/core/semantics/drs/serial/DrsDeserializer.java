/*
  The MIT License (MIT)

  Copyright (c) 2016 Giacomo Marciani and Michele Porretta

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

package com.acmutv.ontoqa.core.semantics.drs.serial;

import com.acmutv.ontoqa.core.semantics.base.statement.Statement;
import com.acmutv.ontoqa.core.semantics.base.statement.Statements;
import com.acmutv.ontoqa.core.semantics.base.term.Variable;
import com.acmutv.ontoqa.core.semantics.drs.Drs;
import com.acmutv.ontoqa.core.semantics.drs.SimpleDrs;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Iterator;

/**
 * The JSON deserializer for {@link Drs}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Drs
 * @see DrsSerializer
 */
public class DrsDeserializer extends StdDeserializer<Drs> {

  /**
   * The singleton of {@link DrsDeserializer}.
   */
  private static DrsDeserializer instance;

  /**
   * Returns the singleton of {@link DrsDeserializer}.
   * @return the singleton.
   */
  public static DrsDeserializer getInstance() {
    if (instance == null) {
      instance = new DrsDeserializer();
    }
    return instance;
  }

  /**
   * Initializes the singleton of {@link DrsDeserializer}.
   */
  private DrsDeserializer() {
    super((Class<Drs>) null);
  }


  @Override
  public Drs deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
    JsonNode node = parser.getCodec().readTree(parser);

    if (!node.hasNonNull("label")) {
      throw new IOException("Cannot read field [label]");
    }

    final int label = node.get("label").asInt();

    Drs drs = new SimpleDrs(label);

    if (node.hasNonNull("variables")) {
      try {
        Iterator<JsonNode> iter = node.get("variables").elements();
        while (iter.hasNext()) {
          Variable v = Variable.valueOf(iter.next().asText());
          drs.getVariables().add(v);
        }
      } catch (IllegalArgumentException exc) {
        throw new IOException("Cannot read [variables].");
      }
    }

    if (node.hasNonNull("statements")) {
      try {
        Iterator<JsonNode> iter = node.get("statements").elements();
        while (iter.hasNext()) {
          Statement s = Statements.valueOf(iter.next().asText());
          drs.getStatements().add(s);
        }
      } catch (IllegalArgumentException exc) {
        throw new IOException("Cannot read [statements].");
      }
    }

    return drs;
  }

}
