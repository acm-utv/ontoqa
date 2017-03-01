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
import com.acmutv.ontoqa.core.lemon.model.LexicalSense;
import com.acmutv.ontoqa.core.lemon.model.Lexicon;
import com.acmutv.ontoqa.core.lemon.model.Text;
import com.acmutv.ontoqa.core.lemon.model.Property;
import com.acmutv.ontoqa.core.lemon.model.PropertyValue;

import java.io.*;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
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
  @SuppressWarnings("static-access")
  public static Collection<Lexicon> importLexicon(String resource, String prefix, LexiconFormat format) throws IOException {
	    LOGGER.traceEntry("resource={} prefix={} format={}", resource, prefix, format);
	    
	    final LemonSerializer serializer = LemonSerializer.newInstance();
	    Path path = FileSystems.getDefault().getPath(resource).toAbsolutePath();
	    URI url = path.toUri();
	    LemonModel model= serializer.modelFromURL(url.toURL());
	    Collection<Lexicon> lexicon= model.getLexica();
	    return LOGGER.traceExit(lexicon);
	  }
  
  
  /**
   * Reads all Lexical Entry
   * @param args
   * @throws IOException
   */
  @SuppressWarnings({ "unused", "rawtypes", "unchecked"})
//public static List<LexiconElement> getAllLexiconElement() throws IOException{
  public static void main(String args[]) throws IOException{	  
	  Lexicon lexiconOne = null;
	  LexiconUsage importL = new LexiconUsage();
	  LexiconFormat[] format= LexiconFormat.values();
	  LexiconFormat  ff= format[2];
	List<LexiconElement> allLexiconElement= new ArrayList();
	  
	  Collection<Lexicon> lexicon =LexiconUsage.importLexicon("data/lexicon/organization.rdf", "", ff);
	  if( lexicon.size() == 1){
		  lexiconOne = lexicon.iterator().next();
	  }
	  Path path = FileSystems.getDefault().getPath("data/lexicon/organization.rdf").toAbsolutePath();
	  URI url = path.toUri();

	  Collection<LexicalEntry> allLexical =lexiconOne.getEntrys();
	  Iterator<LexicalEntry> iterator = allLexical.iterator();
	  while(iterator.hasNext()){
		  
		  LexiconElement lexiconElement = new LexiconElement();
		  
		  LexicalEntry entry = iterator.next();
		  lexiconElement.setSenses(entry.getSenses());
		  lexiconElement.setForms(entry.getForms());
		  lexiconElement.setName(entry.getURI().toString());
		  lexiconElement.setSynBeh(entry.getSynBehaviors());
		 
		  /*Lexinfo */
		  Map<Property,Collection<PropertyValue>> pp= entry.getPropertys(); 
		  lexiconElement.setType(pp.values().toString(), entry.getTypes());
		  
		  
		  
		  System.out.println("Name: "+ lexiconElement.getName() );
		  System.out.println("Sense: "+ lexiconElement.getSenses());
		  System.out.println("Forms: "+ lexiconElement.getForms());
		  System.out.println("Type: "+ lexiconElement.getType());
		  System.out.println("WrittenRep: "+ lexiconElement.getWrittenRep());
		  System.out.println("SynBeh: "+ lexiconElement.getSynBeh() );
		  System.out.println("PropertyURI: "+ lexiconElement.getReferenceURI() + "\n\n\n");
		  
		  
		  allLexiconElement.add(lexiconElement);
	
	  }
	  
//	  return allLexiconElement;
	  
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
