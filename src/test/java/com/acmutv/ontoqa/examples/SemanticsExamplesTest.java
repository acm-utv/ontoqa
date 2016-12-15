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

import de.citec.sc.dudes.*;
import de.citec.sc.dudes.rdf.ExpressionFactory;
import de.citec.sc.dudes.rdf.RDFDUDES;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.Arrays;

/**
 * This class realizes JUnit tests for semantics management examples.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class SemanticsExamplesTest {

  private static final Logger LOGGER = LogManager.getLogger(SemanticsExamplesTest.class);

  @Test
  public void test_example1() {
    // --- Albert Einstein --- //

    RDFDUDES albertEinsteinDUDES =
        new RDFDUDES(RDFDUDES.Type.INDIVIDUAL);

    System.out.println(
        "An uninstantiated DUDES associated with a proper noun");
    System.out.println(albertEinsteinDUDES);
    System.out.println();

    albertEinsteinDUDES.instantiateIndividual(
        "http://dbpedia.org/resource/Albert_Einstein");

    System.out.println(
        "A DUDES associated with the proper noun Albert Einstein");
    System.out.println(albertEinsteinDUDES);
    System.out.println();

    // --- Elsa Einstein --- //

    RDFDUDES elsaEinsteinDUDES =
        new RDFDUDES(RDFDUDES.Type.INDIVIDUAL);
    elsaEinsteinDUDES.instantiateIndividual(
        "http://dbpedia.org/resource/Elsa_Einstein");

    System.out.println(
        "A DUDES associated with the proper noun Elsa Einstein");
    System.out.println(elsaEinsteinDUDES);
    System.out.println();

    // --- marry --- //
    RDFDUDES marryDUDES = new RDFDUDES(RDFDUDES.Type.PROPERTY, "subj", "dobj");
    System.out.println(
        "An uninstantiated DUDES associated with a subject and one argument");
    System.out.println(marryDUDES);
    System.out.println();

    System.out.println("A DUDES associated with the verb marry");
    marryDUDES.instantiateProperty(
        "http://dbpedia.org/ontology/spouse");
    System.out.println(marryDUDES);
    System.out.println();

    // --- Meaning composition --- //

		/*
			@formatter:off

		 	S
		 		DP[subj]
		 			Albert Einstein
		 		VP
		 			V
		 				married
		 			DP[dobj]
		 				Elsa Einstein

		 	@formatter:on
		*/

    System.out.println("Albert Einstein married Elsa Einstein");
    System.out.println();

    RDFDUDES albertEinsteinMarriedDUDES =
        marryDUDES.merge(albertEinsteinDUDES, "subj");
    System.out.println("Composing the meaning of the subject");
    System.out.println(albertEinsteinMarriedDUDES);
    System.out.println();

    RDFDUDES albertEinsteinMarriedElsaEinsteinDUDES =
        albertEinsteinMarriedDUDES.merge(elsaEinsteinDUDES, "dobj");

    System.out.println("Composing the meaning of the subject");
    System.out.println(albertEinsteinMarriedElsaEinsteinDUDES);
    System.out.println();

    System.out.println("ASK Query (without post-processing)");
    System.out.println(albertEinsteinMarriedElsaEinsteinDUDES
        .convertToSPARQL(false));
    System.out.println();

    albertEinsteinMarriedElsaEinsteinDUDES.postprocess();

    System.out.println("ASK Query (with post-processing)");
    System.out.println(albertEinsteinMarriedElsaEinsteinDUDES
        .convertToSPARQL(false));
  }

  @Test
  public void test_example2() {
    // --- Albert Einstein --- //

    RDFDUDES albertEinsteinDUDES =
        new RDFDUDES(RDFDUDES.Type.INDIVIDUAL);

    System.out.println(
        "An uninstantiated DUDES associated with a proper noun");
    System.out.println(albertEinsteinDUDES);
    System.out.println();

    albertEinsteinDUDES.instantiateIndividual(
        "http://dbpedia.org/resource/Albert_Einstein");

    System.out.println(
        "A DUDES associated with the proper noun Albert Einstein");
    System.out.println(albertEinsteinDUDES);
    System.out.println();

    // --- Elsa Einstein --- //

    RDFDUDES elsaEinsteinDUDES =
        new RDFDUDES(RDFDUDES.Type.INDIVIDUAL);
    elsaEinsteinDUDES.instantiateIndividual(
        "http://dbpedia.org/resource/Elsa_Einstein");

    System.out.println(
        "A DUDES associated with the proper noun Elsa Einstein");
    System.out.println(elsaEinsteinDUDES);
    System.out.println();

    // --- marry --- //

    DRS marryDRS = new DRS(0);
    Variable marryVar1 = new Variable(1);
    Variable marryVar2 = new Variable(2);
    Variable marryVar3 = new Variable(3);

    marryDRS.addStatement(new Proposition(marryVar1,
        Arrays.asList(marryVar2, marryVar3)));

    DUDES marryDUDESTemp = new DUDES();
    marryDUDESTemp.setDRS(marryDRS);
    marryDUDESTemp.setMainDRS(0);
    marryDUDESTemp.addSlot(new Slot(marryVar2, "subj", 0));
    marryDUDESTemp.addSlot(new Slot(marryVar3, "dobj", 0));
    RDFDUDES marryDUDES =
        new RDFDUDES(marryDUDESTemp, RDFDUDES.Type.OTHER);

    System.out.println(
        "An uninstantiated DUDES associated with a subject and one argument");
    System.out.println(marryDUDES);
    System.out.println();

    System.out.println("A DUDES associated with the verb marry");
    marryDUDESTemp.replace(marryVar1,
        new Constant("http://dbpedia.org/ontology/spouse"));
    System.out.println(marryDUDES);
    System.out.println();

    // --- Meaning composition --- //

		/*
			@formatter:off

		 	S
		 		DP[subj]
		 			Albert Einstein
		 		VP
		 			V
		 				married
		 			DP[dobj]
		 				Elsa Einstein

		 	@formatter:on
		*/

    System.out.println("Albert Einstein married Elsa Einstein");
    System.out.println();

    RDFDUDES albertEinsteinMarriedDUDES =
        marryDUDES.merge(albertEinsteinDUDES, "subj");
    System.out.println("Composing the meaning of the subject");
    System.out.println(albertEinsteinMarriedDUDES);
    System.out.println();

    RDFDUDES albertEinsteinMarriedElsaEinsteinDUDES =
        albertEinsteinMarriedDUDES.merge(elsaEinsteinDUDES, "dobj");

    System.out.println("Composing the meaning of the subject");
    System.out.println(albertEinsteinMarriedElsaEinsteinDUDES);
    System.out.println();

    System.out.println("ASK Query (without post-processing)");
    System.out.println(albertEinsteinMarriedElsaEinsteinDUDES
        .convertToSPARQL(false));
    System.out.println();

    albertEinsteinMarriedElsaEinsteinDUDES.postprocess();

    System.out.println("ASK Query (with post-processing)");
    System.out.println(albertEinsteinMarriedElsaEinsteinDUDES
        .convertToSPARQL(false));
  }

  @Test
  public void test_example3() {
    ExpressionFactory expressions = new ExpressionFactory();

    RDFDUDES whoDUDES = expressions.what();

    System.out.println("DUDUES associated with Who");
    System.out.println(whoDUDES);
    System.out.println();

    RDFDUDES elsaEinsteinDUDES =
        new RDFDUDES(RDFDUDES.Type.INDIVIDUAL);
    elsaEinsteinDUDES.instantiateIndividual(
        "http://dbpedia.org/resource/Elsa_Einstein");

    System.out.println("DUDUES associated with Elsa Einstein");
    System.out.println(elsaEinsteinDUDES);
    System.out.println();

    DRS marryDRS = new DRS(0);
    Variable marryVar1 = new Variable(1);
    Variable marryVar2 = new Variable(2);
    Variable marryVar3 = new Variable(3);

    marryDRS.addStatement(new Proposition(marryVar1,
        Arrays.asList(marryVar2, marryVar3)));

    DUDES marryDUDESTemp = new DUDES();
    marryDUDESTemp.setDRS(marryDRS);
    marryDUDESTemp.setMainDRS(0);
    marryDUDESTemp.addSlot(new Slot(marryVar2, "subj", 0));
    marryDUDESTemp.addSlot(new Slot(marryVar3, "dobj", 0));
    RDFDUDES marriedDUDES =
        new RDFDUDES(marryDUDESTemp, RDFDUDES.Type.OTHER);

    marryDUDESTemp.replace(marryVar1,
        new Constant("http://dbpedia.org/ontology/spouse"));
    System.out.println("A DUDES associated with married");
    System.out.println(marriedDUDES);
    System.out.println();


    RDFDUDES whoMarriedDUDES = marriedDUDES.merge(whoDUDES, "subj");

    System.out.println("Who married ...");
    System.out.println(whoMarriedDUDES);
    System.out.println();

    RDFDUDES whoMarriedElsaEinsteinDUDES =
        whoMarriedDUDES.merge(elsaEinsteinDUDES, "dobj");

    System.out.println("Who married Elsa Einstein");
    System.out.println(whoMarriedElsaEinsteinDUDES);
    System.out.println();

    System.out.println("Query (without post-processing)");
    System.out
        .println(whoMarriedElsaEinsteinDUDES.convertToSPARQL());
    System.out.println();

    whoMarriedElsaEinsteinDUDES.postprocess();

    System.out.println("Query (with post-processing)");
    System.out
        .println(whoMarriedElsaEinsteinDUDES.convertToSPARQL());
    System.out.println();
  }

  @Test
  public void test_example4() {
    ExpressionFactory expressions = new ExpressionFactory();

    // --- Who --- //

    RDFDUDES whoDUDES = expressions.what();

    System.out.println("DUDUES associated with Who");
    System.out.println(whoDUDES);
    System.out.println();

    // --- is (copula) --- //

    RDFDUDES copula = expressions.copula("1", "2");
    System.out.println("DUDES associated with copula");
    System.out.println(copula);
    System.out.println();

    // --- the --- //

    DRS theDRS = new DRS(0);
    Variable theVar1 = new Variable(1);
    theDRS.addVariable(theVar1);
    DUDES theDUDESTemp = new DUDES();
    theDUDESTemp.setDRS(theDRS);
    theDUDESTemp.setMainDRS(0);
    theDUDESTemp.setMainVariable(theVar1);
    theDUDESTemp.addSlot(new Slot(theVar1, "np"));

    RDFDUDES theDUDES = new RDFDUDES(theDUDESTemp, RDFDUDES.Type.OTHER);

    System.out.println("DUDUES associated with the");
    System.out.println(theDUDES);
    System.out.println();

    // --- spouse (of) --- //

    DRS spouseDRS = new DRS(0);
    Variable spouseVar1 = new Variable(1);
    Variable spouseVar2 = new Variable(2);
    spouseDRS.addStatement(new Proposition(
        new Constant("http://dbpedia.org/ontology/spouse"),
        Arrays.asList(spouseVar2, spouseVar1)));
    DUDES spouseDUDESTemp = new DUDES();
    spouseDUDESTemp.setDRS(spouseDRS);
    spouseDUDESTemp.setMainDRS(0);
    spouseDUDESTemp.setMainVariable(spouseVar1);
    spouseDUDESTemp.addSlot(new Slot(spouseVar2, "dp"));

    RDFDUDES spouseDUDES =
        new RDFDUDES(spouseDUDESTemp, RDFDUDES.Type.OTHER);

    System.out.println("DUDUES associated with spouse (of)");
    System.out.println(spouseDUDES);
    System.out.println();

    // --- Elsa Einstein --- //

    RDFDUDES elsaEinsteinDUDES =
        new RDFDUDES(RDFDUDES.Type.INDIVIDUAL);
    elsaEinsteinDUDES.instantiateIndividual(
        "http://dbpedia.org/resource/Elsa_Einstein");

    System.out.println("DUDUES associated with Elsa Einstein");
    System.out.println(elsaEinsteinDUDES);
    System.out.println();

    // --- Semantic composition --- //
    // Who is the spouse of Elsa Einstein? //

    RDFDUDES whoIsTheSposeOfElsaEinstein = copula
        .merge(whoDUDES, "1")
        .merge(theDUDES.merge(
            spouseDUDES.merge(elsaEinsteinDUDES, "dp"), "np"),
            "2");

    System.out.println("Who is the spouse of Elsa Einstein?");
    System.out.println(whoIsTheSposeOfElsaEinstein);
    System.out.println();

    System.out.println("Query (without post-processing)");
    System.out
        .println(whoIsTheSposeOfElsaEinstein.convertToSPARQL());
    System.out.println();

    whoIsTheSposeOfElsaEinstein.postprocess();

    System.out.println("Query (with post-processing)");
    System.out
        .println(whoIsTheSposeOfElsaEinstein.convertToSPARQL());
    System.out.println();
  }

  @Test
  public void test_example5() {
    ExpressionFactory expressions = new ExpressionFactory();

    // --- how many --- //

    RDFDUDES howmanyDUDES = expressions.howmany("np");

    System.out.println("DUDUES associated with how many");
    System.out.println(howmanyDUDES);
    System.out.println();

    // --- women --- //

    RDFDUDES womenDUDES = new RDFDUDES(RDFDUDES.Type.CLASS);
    womenDUDES.instantiateObject(
        "http://dbpedia.org/class/yago/Woman110787470");
    womenDUDES.instantiateProperty(
        "http://www.w3.org/1999/02/22-rdf-syntax-ns#type");

    System.out.println("DUDUES associated with women");
    System.out.println(womenDUDES);
    System.out.println();

    // --- Albert Einstein --- //

    RDFDUDES albertEinsteinDUDES =
        new RDFDUDES(RDFDUDES.Type.INDIVIDUAL);

    albertEinsteinDUDES.instantiateIndividual(
        "http://dbpedia.org/resource/Albert_Einstein");

    System.out.println("DUDUES associated with Albert Einstein");
    System.out.println(albertEinsteinDUDES);
    System.out.println();

    // --- married --- //

    RDFDUDES marryDUDES =
        new RDFDUDES(RDFDUDES.Type.PROPERTY, "subj", "dobj");
    System.out.println(marryDUDES);
    marryDUDES.instantiateProperty(
        "http://dbpedia.org/ontology/spouse");

    System.out.println("DUDUES associated with marry");
    System.out.println(marryDUDES);
    System.out.println();

    // --- did --- //

    RDFDUDES didDUDES = expressions.did();

    System.out.println("DUDUES associated with did");
    System.out.println(didDUDES);
    System.out.println();

    // --- Meaning composition --- //
    // How many women did Albert Einstein marry? //

    RDFDUDES howmanyWomenDUDES =
        howmanyDUDES.merge(womenDUDES, "np");

    // --- How many women --- //

    System.out.println("DUDES associated with how many women");
    System.out.println(howmanyWomenDUDES);
    System.out.println();

    // --- Albert Einstein marry --- //

    RDFDUDES albertEinsteinMarryDUDES =
        marryDUDES.merge(albertEinsteinDUDES, "subj");

    System.out
        .println("DUDES associated with Albert Einstein marry");
    System.out.println(albertEinsteinMarryDUDES);
    System.out.println();

    // --- How many women did Albert Einstein marry --- //

    RDFDUDES howManyWomenDidAlbertEinstenMarryDUDES =
        didDUDES.merge(albertEinsteinMarryDUDES
            .merge(howmanyWomenDUDES, "dobj"));

    System.out.println(
        "DUDES associated with How many women Albert Einstein marry");
    System.out.println(howManyWomenDidAlbertEinstenMarryDUDES);
    System.out.println();

    System.out.println("Query (without post-processing)");
    howManyWomenDidAlbertEinstenMarryDUDES.postprocess();
    System.out.println();

    System.out.println("Query (with post-processing)");
    System.out.println(
        howManyWomenDidAlbertEinstenMarryDUDES.convertToSPARQL());
    System.out.println();
  }

  @Test
  public void test_example6() {
    ExpressionFactory expressions = new ExpressionFactory();

    // --- what --- //

    RDFDUDES whatDUDES = expressions.what();

    System.out.println("DUDES associated with what");
    System.out.println(whatDUDES);
    System.out.println();

    // --- is (copula) --- //

    RDFDUDES copulaDUDES = expressions.copula("1", "2");

    System.out.println("DUDES associated with copula");
    System.out.println(copulaDUDES);
    System.out.println();

    // --- the highest --- //

    RDFDUDES theHighestDUDES =
        expressions.superlative("np", OperatorStatement.Operator.MAX, true);
    theHighestDUDES.instantiateProperty(
        "http://dbpedia.org/ontology/prominence");

    System.out.println("DUDES associated with the highest");
    System.out.println(theHighestDUDES);
    System.out.println();

    // --- mountain --- //

    RDFDUDES mountainDUDES = new RDFDUDES(RDFDUDES.Type.CLASS);
    mountainDUDES.instantiateObject(
        "http://dbpedia.org/ontology/Mountain");
    mountainDUDES.instantiateProperty(
        "http://www.w3.org/1999/02/22-rdf-syntax-ns#type");

    System.out.println("DUDES associated with mountain");
    System.out.println(mountainDUDES);
    System.out.println();

    // --- meaning composition --- //

    RDFDUDES whatIsTheHighestMountain =
        copulaDUDES.merge(whatDUDES, "1").merge(
            theHighestDUDES.merge(mountainDUDES, "np"), "2");
    System.out.println("What is the highest mountain");
    System.out.println(whatIsTheHighestMountain);
    System.out.println();

    System.out.println("Query (without post-processing)");
    System.out
        .println(whatIsTheHighestMountain.convertToSPARQL());
    System.out.println();

    whatIsTheHighestMountain.postprocess();

    System.out.println("Query (with post-processing)");
    System.out
        .println(whatIsTheHighestMountain.convertToSPARQL());
    System.out.println();
  }
}
