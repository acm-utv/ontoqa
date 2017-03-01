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
import com.acmutv.ontoqa.core.semantics.base.statement.Statement;
import com.acmutv.ontoqa.core.semantics.base.statement.Statements;
import com.acmutv.ontoqa.core.semantics.base.term.Term;
import com.acmutv.ontoqa.core.semantics.base.term.Terms;
import com.acmutv.ontoqa.core.semantics.base.term.Variable;
import com.acmutv.ontoqa.core.semantics.drs.Drs;
import com.acmutv.ontoqa.core.semantics.drs.SimpleDrs;
import com.acmutv.ontoqa.core.semantics.dudes.SimpleDudes;
import com.acmutv.ontoqa.core.semantics.dudes.Dudes;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

  private static final String REGEXP = "^(v[0-9]+)?(@)?([0-9]+)?$";

  private static final Pattern PATTERN = Pattern.compile(REGEXP);

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

    Dudes dudes = new SimpleDudes();

    if (node.hasNonNull("return")) {
      Iterator<JsonNode> projections = node.get("return").elements();
      try {
        while(projections.hasNext()) {
          Term term = Terms.valueOf(projections.next().asText());
          dudes.getProjection().add(term);
        }
      } catch (IllegalArgumentException exc) {
        throw new IOException("Cannot read [return]");
      }
    }

    if (node.hasNonNull("main")) {
      String mainField = node.get("main").asText();
      if (mainField == null) throw new IOException("Cannot read [main]");
      Matcher matcher = PATTERN.matcher(mainField);
      if (!matcher.matches()) throw new IOException("Cannot read [main]");
      String strMainVar = matcher.group(1);
      String strMainDrs = matcher.group(2);
      if (strMainVar != null) {
        Variable mainVar = Variable.valueOf(strMainVar);
        dudes.setMainVariable(mainVar);
      }
      if (strMainDrs != null) {
        int mainDrs = Integer.valueOf(strMainDrs);
        dudes.setMainDrs(mainDrs);
      }
    }

    int label = 0;
    if (node.hasNonNull("label")) {
      label = node.get("label").asInt();
    }

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
          JsonNode n = iter.next();
          Statement s = Statements.valueOf(n.asText());
          drs.getStatements().add(s);
        }
      } catch (IllegalArgumentException exc) {
        throw new IOException("Cannot read [statements].");
      }
    }

    dudes.setDrs(drs);

    if (node.hasNonNull("slots")) {
      Iterator<JsonNode> slots = node.get("slots").elements();
      try {
        while(slots.hasNext()) {
          Slot slot = Slot.valueOf(slots.next().asText());
          dudes.getSlots().add(slot);
        }
      } catch (IllegalArgumentException exc) {
        throw new IOException("Cannot read [slots].");
      }
    }

    if (node.hasNonNull("select")) {
      dudes.setSelect(node.get("select").asBoolean(false));
    }

    return dudes;
  }
}
