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

package com.acmutv.ontoqa.examples;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.OutputStreamWriter;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class realizes the example 1.A
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @since 1.0
 */
class Example1A {

  public static void main(String[] args) {
    String ns = "http://example.org/";

    ValueFactory vf = SimpleValueFactory.getInstance();
    IRI mortalIRI = vf.createIRI(ns, "Mortal");
    IRI personIRI = vf.createIRI(ns, "Person");
    IRI socratesIRI = vf.createIRI(ns, "Socrates");

    Model model = new LinkedHashModel();

    model.add(mortalIRI, RDF.TYPE, RDFS.CLASS);
    model.add(mortalIRI, RDFS.LABEL, vf.createLiteral("Mortal","en"));
    model.add(mortalIRI, RDFS.LABEL, vf.createLiteral("Mortale","it"));

    model.add(personIRI, RDF.TYPE, RDFS.CLASS);
    model.add(personIRI, RDFS.LABEL, vf.createLiteral("Person","en"));
    model.add(personIRI, RDFS.LABEL, vf.createLiteral("Persona","it"));
    model.add(personIRI, RDFS.SUBCLASSOF, mortalIRI);

    model.add(socratesIRI, RDF.TYPE, personIRI);
    model.add(socratesIRI, RDFS.LABEL, vf.createLiteral("Socrates","en"));
    model.add(socratesIRI, RDFS.LABEL, vf.createLiteral("Socrate","it"));

    Rio.write(model, new OutputStreamWriter(System.out), RDFFormat.TURTLE);

    Set<String> subjects =
        model.subjects().stream()
            .filter(IRI.class::isInstance)
            .map(IRI.class::cast)
            .map(IRI::getLocalName)
            .collect(Collectors.toSet());

    System.out.println(subjects);

    Set<Value> labels = model.filter(socratesIRI, RDFS.LABEL, null).objects();

    System.out.println(labels);
  }
}
