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

import com.acmutv.ontoqa.core.syntax.ltag.Ltag;
import com.acmutv.ontoqa.core.syntax.ltag.LtagNode;
import com.acmutv.ontoqa.core.syntax.ltag.LtagProduction;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The JSON serializer for {@link Ltag}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Ltag
 * @see LtagDeserializer
 */
public class LtagSerializer extends StdSerializer<Ltag> {

  /**
   * The singleton of {@link LtagSerializer}.
   */
  private static LtagSerializer instance;

  /**
   * Returns the singleton of {@link LtagSerializer}.
   * @return the singleton.
   */
  public static LtagSerializer getInstance() {
    if (instance == null) {
      instance = new LtagSerializer();
    }
    return instance;
  }

  /**
   * Initializes the singleton of {@link LtagSerializer}.
   */
  private LtagSerializer() {
    super((Class<Ltag>) null);
  }

  @Override
  public void serialize(Ltag value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();

    gen.writeStringField("axiom", value.getAxiom().toString());

    gen.writeArrayFieldStart("productions");
    Queue<LtagNode> frontier = new ConcurrentLinkedQueue<>();
    frontier.add(value.getAxiom());
    while (!frontier.isEmpty()) {
      LtagNode curr = frontier.poll();
      List<LtagNode> children = value.getRhs(curr);
      if (children == null) continue;
      for (LtagNode child : children) {
        gen.writeString(String.format("%s->%s", curr, child));
        frontier.add(child);
      }
    }
    gen.writeEndArray();

    gen.writeEndObject();
  }
}