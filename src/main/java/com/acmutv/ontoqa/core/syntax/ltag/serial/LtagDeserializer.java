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

package com.acmutv.ontoqa.core.syntax.ltag.serial;

import com.acmutv.ontoqa.core.syntax.ltag.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Iterator;

/**
 * The JSON deserializer for {@link Ltag}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Ltag
 * @see LtagSerializer
 */
public class LtagDeserializer extends StdDeserializer<Ltag> {

  /**
   * The singleton of {@link LtagDeserializer}.
   */
  private static LtagDeserializer instance;

  /**
   * Returns the singleton of {@link LtagDeserializer}.
   * @return the singleton.
   */
  public static LtagDeserializer getInstance() {
    if (instance == null) {
      instance = new LtagDeserializer();
    }
    return instance;
  }

  /**
   * Initializes the singleton of {@link LtagDeserializer}.
   */
  private LtagDeserializer() {
    super((Class<Ltag>) null);
  }


  @Override
  public Ltag deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
    JsonNode node = parser.getCodec().readTree(parser);

    if (!node.hasNonNull("root") ||
        !node.hasNonNull("edges")) {
      throw new IOException("Cannot read [root,edges].");
    }

    final LtagNode root = LtagNodes.valueOf(node.get("root").asText());

    Ltag ltag = new SimpleLtag(root);

    Iterator<JsonNode> iter = node.get("edges").elements();
    String element = null;
    try {
      while (iter.hasNext()) {
        element = iter.next().asText();
        LtagEdge edge = LtagEdge.valueOf(element);
        ltag.addEdge(edge.getLhs(), edge.getRhs());
      }
    } catch (IllegalArgumentException exc) {
      throw new IOException("Cannot read [edges]. Wrong syntax: " + element);
    }

    return ltag;
  }
}
