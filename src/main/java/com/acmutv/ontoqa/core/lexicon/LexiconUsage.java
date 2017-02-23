/*
  The MIT License (MIT)

<<<<<<< HEAD
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import com.acmutv.ontoqa.core.lemon.*;
import com.acmutv.ontoqa.core.lemon.impl.LexiconImpl;
import com.acmutv.ontoqa.core.lemon.model.LexicalEntry;

import com.acmutv.ontoqa.core.lemon.impl.LemonModelImpl;
import com.acmutv.ontoqa.core.lemon.impl.io.xml.RDFXMLReader;

=======
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

package com.acmutv.ontoqa.core.lexicon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.acmutv.ontoqa.core.lemon.LemonFactory;
import com.acmutv.ontoqa.core.lemon.LemonModel;
import com.acmutv.ontoqa.core.lemon.LemonModels;
import com.acmutv.ontoqa.core.lemon.LemonSerializer;
import com.acmutv.ontoqa.core.lemon.LinguisticOntology;
import com.acmutv.ontoqa.core.lemon.lexinfo.LexInfo;
import com.acmutv.ontoqa.core.lemon.model.LexicalEntry;
import com.acmutv.ontoqa.core.lemon.model.LexicalForm;
import com.acmutv.ontoqa.core.lemon.model.Lexicon;
import com.acmutv.ontoqa.core.lemon.model.Text;
import com.acmutv.ontoqa.core.lemon.model.Property;
import com.acmutv.ontoqa.core.lemon.model.PropertyValue;

import java.io.*;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * This class realizes the lexicon management services.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class LexiconUsage {

  private static final Logger LOGGER = LogManager.getLogger(LexiconUsage.class);
  
  /**
   * Reads a lexicon from a resource.
   * @param resource the resource to read.
   * @param prefix the default prefix for the lexicon.
   * @param format the lexicon format.
   * @return the lexicon.
   * @throws IOException when lexicon cannot be read.
   */ 
  public static Collection<Lexicon> importLexicon(String resource, String prefix, LexiconFormat format) throws IOException {
	    LOGGER.traceEntry("resource={} prefix={} format={}", resource, prefix, format);
	    
	    final LemonSerializer serializer = LemonSerializer.newInstance();
	    Path path = FileSystems.getDefault().getPath(resource).toAbsolutePath();
	    URI url = path.toUri();
	    LemonModel model= serializer.modelFromURL(url.toURL());
	    Collection<Lexicon> lexicon= model.getLexica();
	    return LOGGER.traceExit(lexicon);
	  }
  
  public static void main(String args[]) throws IOException{
	  Lexicon lexiconOne = null;
	  LexiconUsage importL = new LexiconUsage();
	  LexiconFormat[] format= LexiconFormat.values();
	  LexiconFormat  ff= format[2];
	  
	  Collection<Lexicon> lexicon =LexiconUsage.importLexicon("C:/Users/Antonella/git/ontoqa/data/lexicon/organization.rdf", "", ff);
	  if( lexicon.size() == 1){
		  lexiconOne = lexicon.iterator().next();
	  }
	  Path path = FileSystems.getDefault().getPath("C:/Users/Antonella/git/ontoqa/data/lexicon/organization.rdf").toAbsolutePath();
	   URI url = path.toUri();

	  Collection<LexicalEntry> allLexical =lexiconOne.getEntrys();
	  Iterator<LexicalEntry> iterator = allLexical.iterator();
	  while(iterator.hasNext()){
		  LexicalEntry entry = iterator.next();
		  //System.out.println("sense "+entry.getSenses());
	//	  System.out.println(entry.getURI());
		 
//		  URI uri = URI.create("http://www.lexinfo.net/ontology/2.0/lexinfo#");
//		  PropertyImpl property= new PropertyImpl(uri);
//		  
//		  System.out.println("PROPRIETà"+ property.getURI());
//		  System.out.println("pp : " +entry.getProperty(property));
		  
		 
		  
		//  LexiconElement lexEl = new LexiconElement();
		//  pareserString(entry.getURI().toString(),lexEl);
		  
		 
		  
//		  System.out.println("synBe "+ entry.getSynBehaviors());
//		  System.out.println(entry.getForms());
	  System.out.println(entry.getURI()+"\n");
//		 System.out.println(entry.getTopics());
//		 System.out.println(entry.getCanonicalForm());
//		 System.out.println(entry.getOtherForms());
		System.out.print("GET TYPES" +entry.getTypes() +"\n");
	//	System.out.print(entry.getHead());
		
		  Map<Property,Collection<PropertyValue>> pp= entry.getPropertys(); 
		//  pp.values();//lexinfo
	    System.out.println(pp.values() +"\n\n\n");
			
			
		//	return;
  
	  }
	  
	  
  }
  
  public static LexiconElement pareserString(String text, LexiconElement lexEl){
	  
	  String[] allPart=text.split("/");
	  String[] interestPart= allPart[allPart.length-1].split("#");
	  String[] tupla= interestPart[1].split("__");
	  
	  String type= tupla[1];
	  String nn = tupla[0];
	  String[] nameP = nn.split("\\+");
	  String name = nameP[0];
	  for( int i = 1; i< nameP.length; i++){
		  String p = nameP[i];
		  System.out.println(nameP[i]);
		  name += " "+ nameP[i];
	  }
	  System.out.println("Tipo: "+type);
	  System.out.println("Name: " +name);
	  
	  LexInfo lex = new LexInfo();
	 System.out.println(lex.getContexts());
	  System.out.println(lex.getFormVariant());
//	  System.out.println(lex.getLexicalVariant(name));
	  System.out.println(lex.getProperties());
	  System.out.println(lex.getSynArgs());
	  System.out.println(lex.getRepresentation());
	  System.out.println(lex.getSenseRelation());
	  
	  
		  
	  return lexEl;
  }
  
  /**
   * Gets all lexicalEntry of a lexicon 
   * 
   * @param lexicon to read
   */
  public static Collection<LexicalEntry> getLexicalEntry(Lexicon lexicon){
	  
	  Collection<LexicalEntry> allLexicalEntry = lexicon.getEntrys();
	  return allLexicalEntry;
	  
	 
  }
  
  public static void getInfo(LexicalEntry lexicalEntry){
	  
	  Collection<LexicalForm> allLexicalForm =lexicalEntry.getOtherForms();
	  lexicalEntry.getCanonicalForm();
	  lexicalEntry.getOtherForms();
	  lexicalEntry.getSenses();
      lexicalEntry.getAbstractForms();
      lexicalEntry.getSynBehaviors();
	  lexicalEntry.getTypes();
	  Map<Property,Collection<PropertyValue>> pp=lexicalEntry.getPropertys(); 
	  pp.values();//lexinfo
	  lexicalEntry.getURI();
	  lexicalEntry.getMarker();
	  
	  
	 
  }
  		
  public static Text lexicalForm(LexicalForm lexicalForm){
	  return lexicalForm.getWrittenRep();
	  
  }
  
  
  /**
   * Create a new Lexicon
   * Example
   */
  public static void writeLexicon(){
	  
	  final LemonSerializer serializer = LemonSerializer.newInstance();
	  final LemonModel model = serializer.create();
	  final Lexicon lexicon = model.addLexicon(
				       URI.create("http://www.example.com/mylexicon"),
				        "en" /*English*/);
	  
	  final LexicalEntry entry = LemonModels.addEntryToLexicon(
					        lexicon,
					        URI.create("http://www.example.com/mylexicon/cat"),
					        "cat",
					        URI.create("http://dbpedia.org/resource/Cat"));
	  
	  final LemonFactory factory = model.getFactory();
			final LexicalForm pluralForm = factory.makeForm(entry.getURI());
			pluralForm.setWrittenRep(new Text("cats", "en"));
			final LinguisticOntology lingOnto = new LexInfo();
			pluralForm.addProperty(
			        lingOnto.getProperty("number"),
			        lingOnto.getPropertyValue("plural"));
			entry.addOtherForm(pluralForm);
		
			serializer.writeEntry(model, entry, lingOnto, 
			        new OutputStreamWriter(System.out));
			
	  
  }


}
