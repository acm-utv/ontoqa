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
import com.acmutv.ontoqa.core.semantics.base.term.Variable;
import com.acmutv.ontoqa.core.semantics.drs.Drs;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * The JSON serializer for {@link Drs}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Drs
 * @see DrsDeserializer
 */
public class DrsSerializer extends StdSerializer<Drs> {

  /**
   * The singleton of {@link DrsSerializer}.
   */
  private static DrsSerializer instance;

  /**
   * Returns the singleton of {@link DrsSerializer}.
   * @return the singleton.
   */
  public static DrsSerializer getInstance() {
    if (instance == null) {
      instance = new DrsSerializer();
    }
    return instance;
  }

  /**
   * Initializes the singleton of {@link DrsSerializer}.
   */
  private DrsSerializer() {
    super((Class<Drs>) null);
  }

  @Override
  public void serialize(Drs value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();

    gen.writeNumberField("label", value.getLabel());

    gen.writeArrayFieldStart("variables");
    for (Variable variable : value.getVariables()) {
      gen.writeString(variable.toString());
    }
    gen.writeEndArray();

    gen.writeArrayFieldStart("statements");
    for (Statement statement : value.getStatements()) {
      gen.writeString(statement.toString());
    }
    gen.writeEndArray();

    gen.writeEndObject();
  }
}