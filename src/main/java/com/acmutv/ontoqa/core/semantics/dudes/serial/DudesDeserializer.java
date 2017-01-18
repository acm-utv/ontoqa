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

package com.acmutv.ontoqa.core.semantics.dudes.serial;

import com.acmutv.ontoqa.core.semantics.dudes.Dudes;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * The JSON deserializer for {@link Dudes}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Dudes
 * @see DudesSerializer
 */
public class DudesDeserializer extends StdDeserializer<Dudes> {

  /**
   * The singleton of {@link DudesDeserializer}.
   */
  private static DudesDeserializer instance;

  /**
   * Returns the singleton of {@link DudesDeserializer}.
   * @return the singleton.
   */
  public static DudesDeserializer getInstance() {
    if (instance == null) {
      instance = new DudesDeserializer();
    }
    return instance;
  }

  /**
   * Initializes the singleton of {@link DudesDeserializer}.
   */
  private DudesDeserializer() {
    super((Class<Dudes>) null);
  }


  @Override
  public Dudes deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
    JsonNode node = parser.getCodec().readTree(parser);

    /*
    if (!node.hasNonNull("method") || !node.hasNonNull("target")) {
      throw new IOException("[method,target] required");
    }

    final HttpMethod method = HttpMethod.valueOf(node.get("method").asText());
    final URL target = new URL(node.get("target").asText());

    HttpAttack attack = new HttpAttack(method, target);

    if (node.has("proxy")) {
      final HttpProxy proxy = HttpProxy.valueOf(node.get("proxy").asText());
      attack.setProxy(proxy);
    }

    if (node.hasNonNull("properties")) {
      Map<String,String> properties = new HashMap<>();
      node.get("properties").fields().forEachRemaining(f -> properties.put(f.getKey(), f.getValue().asText()));
      attack.setProperties(properties);
    }

    if (node.hasNonNull("executions")) {
      final int executions = node.get("executions").asInt();
      attack.setExecutions(executions);
    }

    if (node.hasNonNull("period")) {
      final Interval period = Interval.valueOf(node.get("period").asText());
      attack.setPeriod(period);
    }
    */

    return null;
  }
}
