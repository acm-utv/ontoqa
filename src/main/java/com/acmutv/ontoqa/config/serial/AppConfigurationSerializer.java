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

package com.acmutv.ontoqa.config.serial;

import com.acmutv.ontoqa.config.AppConfiguration;
import com.acmutv.ontoqa.core.knowledge.ontology.OntologyFormat;
import com.acmutv.ontoqa.core.lexicon.LexiconFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * The JSON serializer for {@link AppConfiguration}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see AppConfiguration
 * @see AppConfigurationDeserializer
 */
public class AppConfigurationSerializer extends StdSerializer<AppConfiguration> {

  /**
   * The singleton of {@link AppConfigurationSerializer}.
   */
  private static AppConfigurationSerializer instance;

  /**
   * Returns the singleton of {@link AppConfigurationSerializer}.
   * @return the singleton.
   */
  public static AppConfigurationSerializer getInstance() {
    if (instance == null) {
      instance = new AppConfigurationSerializer();
    }
    return instance;
  }

  /**
   * Initializes the singleton of {@link AppConfigurationSerializer}.
   */
  private AppConfigurationSerializer() {
    super((Class<AppConfiguration>) null);
  }

  @Override
  public void serialize(AppConfiguration value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();

    final String ontologyPath = value.getOntologyPath();
    gen.writeStringField("ontologyPath", ontologyPath);

    final OntologyFormat ontologyFormat = value.getOntologyFormat();
    gen.writeStringField("ontologyFormat", ontologyFormat.name());

    final String lexiconPath = value.getLexiconPath();
    gen.writeStringField("lexiconPath", lexiconPath);

    final LexiconFormat lexiconFormat = value.getLexiconFormat();
    gen.writeStringField("lexiconFormat", lexiconFormat.name());

    gen.writeEndObject();
  }

}