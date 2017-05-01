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
import com.acmutv.ontoqa.core.syntax.ltag.LtagNodeMarker;
import com.acmutv.ontoqa.core.syntax.ltag.LtagNodeType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.acmutv.ontoqa.core.syntax.ltag.LtagNodeType.NON_TERMINAL;

/**
 * The JSON serializer for {@link Ltag}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Ltag
 * @see LtagDeserializer
 */
public class LtagWebSerializer extends StdSerializer<Ltag> {

  private static final Logger LOGGER = LoggerFactory.getLogger(LtagWebSerializer.class);

  /**
   * The singleton of {@link LtagWebSerializer}.
   */
  private static LtagWebSerializer instance;

  /**
   * Returns the singleton of {@link LtagWebSerializer}.
   * @return the singleton.
   */
  public static LtagWebSerializer getInstance() {
    if (instance == null) {
      instance = new LtagWebSerializer();
    }
    return instance;
  }

  /**
   * Initializes the singleton of {@link LtagWebSerializer}.
   */
  private LtagWebSerializer() {
    super((Class<Ltag>) null);
  }

  @Override
  public void serialize(Ltag value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartArray();

    LtagNode curr = value.getRoot();
    this._rec(value, curr, null, value.getRhs(curr), gen);

    gen.writeEndArray();
  }

  private void _rec(Ltag tree, LtagNode curr, LtagNode parent, List<LtagNode> children, JsonGenerator gen) throws IOException {
    if (curr == null) return;

    LOGGER.trace("curr={} | parent={} | children={}", curr, parent, children);

    final String name = (NON_TERMINAL.equals(curr.getType())) ?
        String.format("%s%d", curr.getCategory().name(), curr.getId())
        :
        curr.getLabel()
        ;
    final String prnt = (parent != null) ? String.format("%s%d", parent.getCategory().name(), parent.getId()) : null;
    LtagNodeMarker marker = curr.getMarker();
    final String operation = (marker != null) ? marker.getShortName() : null;
    gen.writeStartObject();
    gen.writeStringField("name", name);
    gen.writeStringField("parent", prnt);
    gen.writeStringField("operation", operation);
    if (children.isEmpty()) {
      gen.writeStringField("children", null);
      gen.writeEndObject();
      return;
    }
    gen.writeArrayFieldStart("children");
    for (LtagNode child : children) {
      _rec(tree, child, curr, tree.getRhs(child), gen);
    }
    gen.writeEndArray();
    gen.writeEndObject();
  }
}