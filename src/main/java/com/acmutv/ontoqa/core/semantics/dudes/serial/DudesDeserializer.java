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

import com.acmutv.ontoqa.core.semantics.base.slot.Slot;
import com.acmutv.ontoqa.core.semantics.base.term.Term;
import com.acmutv.ontoqa.core.semantics.base.term.Terms;
import com.acmutv.ontoqa.core.semantics.base.term.Variable;
import com.acmutv.ontoqa.core.semantics.drs.Drs;
import com.acmutv.ontoqa.core.semantics.dudes.BaseDudes;
import com.acmutv.ontoqa.core.semantics.dudes.Dudes;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Iterator;

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

    if (!node.hasNonNull("return") ||
        !node.hasNonNull("main") ||
        !node.get("main").hasNonNull("var") ||
        !node.get("main").hasNonNull("drs") ||
        !node.hasNonNull("drs") ||
        !node.hasNonNull("slots")) {
      throw new IOException("[return,main,drs,slots] required");
    }

    Dudes dudes = new BaseDudes();

    Iterator<JsonNode> projections = node.get("return").elements();
    try {
      while(projections.hasNext()) {
        Term term = Terms.valueOf(projections.next().asText());
        dudes.getProjection().add(term);
      }
    } catch (IllegalArgumentException exc) {
      throw new IOException("Cannot read [return]");
    }

    Variable mainVariable = Variable.valueOf(node.get("main").get("var").asText());
    dudes.setMainVariable(mainVariable);

    int mainDrs = node.get("main").get("drs").asInt();
    dudes.setMainDrs(mainDrs);

    Drs drs = ctx.readValue(node.get("drs").traverse(parser.getCodec()), Drs.class);
    dudes.setDrs(drs);

    Iterator<JsonNode> slots = node.get("slots").elements();
    try {
      while(slots.hasNext()) {
        Slot slot = Slot.valueOf(slots.next().asText());
        dudes.getSlots().add(slot);
      }
    } catch (IllegalArgumentException exc) {
      throw new IOException("Cannot read [slots].");
    }

    if (node.hasNonNull("select")) {
      dudes.setSelect(node.get("select").asBoolean(false));
    }

    return dudes;
  }
}
