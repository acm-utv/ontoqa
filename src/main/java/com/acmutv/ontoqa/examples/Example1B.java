/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016 Antonella Botte, Giacomo Marciani and Debora Partigianoni
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.acmutv.ontoqa.examples;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.OutputStreamWriter;

/**
 * This class realizes ...
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @since 1.0.0
 * @see
 */
public class Example1B {

  public static void main(String[] args) {
    ValueFactory vf = SimpleValueFactory.getInstance();
    ModelBuilder builder = new ModelBuilder();

    Model model = builder
        .setNamespace("example", "http://example.org/")
        .subject("Mortal")
          .add(RDF.TYPE, RDFS.CLASS)
          .add(RDFS.LABEL, vf.createLiteral("Mortal", "en"))
          .add(RDFS.LABEL, vf.createLiteral("Mortale", "it"))
        .subject("Person")
          .add(RDF.TYPE, RDFS.CLASS)
          .add(RDFS.LABEL, vf.createLiteral("Person", "en"))
          .add(RDFS.LABEL, vf.createLiteral("Persona", "it"))
          .add(RDFS.SUBCLASSOF, "Mortal")
        .subject("Socrates")
          .add(RDF.TYPE, "Person")
          .add(RDFS.LABEL, vf.createLiteral("Socrates", "en"))
          .add(RDFS.LABEL, vf.createLiteral("Socrate", "it"))
        .build();

    Rio.write(model, new OutputStreamWriter(System.out), RDFFormat.TURTLE);
  }
}
