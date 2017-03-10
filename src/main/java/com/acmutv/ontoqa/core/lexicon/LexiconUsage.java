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
import com.acmutv.ontoqa.core.lemon.model.Argument;
import com.acmutv.ontoqa.core.lemon.model.Component;
import com.acmutv.ontoqa.core.lemon.model.Frame;
import com.acmutv.ontoqa.core.lemon.model.LemonElement;
import com.acmutv.ontoqa.core.lemon.model.LexicalEntry;
import com.acmutv.ontoqa.core.lemon.model.LexicalForm;
import com.acmutv.ontoqa.core.lemon.model.LexicalSense;
import com.acmutv.ontoqa.core.lemon.model.Lexicon;
import com.acmutv.ontoqa.core.lemon.model.Text;
import com.acmutv.ontoqa.core.lemon.model.Property;
import com.acmutv.ontoqa.core.lemon.model.PropertyValue;
import com.acmutv.ontoqa.core.lemon.model.Representation;

import java.io.*;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
public static List<LEntry> getAllLexiconElement() throws IOException{
//public static void main(String args[]) throws IOException{
	  Lexicon lexiconOne = null;
	  LexiconUsage importL = new LexiconUsage();
	  LexiconFormat[] format= LexiconFormat.values();
	  LexiconFormat  ff= format[2];
	  
	  
	  
	  Collection<Lexicon> lexicon =LexiconUsage.importLexicon("data/lexicon/organization.rdf", "", ff);
	  if( lexicon.size() == 1){
		  lexiconOne = lexicon.iterator().next();
	  }
	  Path path = FileSystems.getDefault().getPath("data/lexicon/organization.rdf").toAbsolutePath();
	  URI url = path.toUri();

	  List<LEntry> allLexiconEntry= new ArrayList();
	  Collection<LexicalEntry> allLexical =lexiconOne.getEntrys();
	  Iterator iterator = allLexical.iterator();
	  int i =0;
	 
	  while(iterator.hasNext()){
		  
		  LexicalEntry entry =(LexicalEntry) iterator.next();
		  LEntry lEntry = new LEntry();
		  lEntry.setUri(entry);
		  lEntry.setWrittenRep(entry);
		  lEntry.setCanonicalForm(entry);
		  lEntry.setPartOfSpeech(entry);
		  lEntry.setOtherForms(entry);
		  lEntry.setSenses(entry);
		  lEntry.setSynBehavior(entry);
		  lEntry.setDecomposition(entry);
		  
		  allLexiconEntry.add(lEntry);
		  
		  
		  System.out.println("Entry numero:    " + i);
		  System.out.println("WrittenRep Form: " + lEntry.getWrittenRep());
		  System.out.println("CanonicalForm:   " + lEntry.getCanonicalForm());
		  System.out.println("Part OF Speech:  " + lEntry.getPartOfSpeech());
		  System.out.println("Decomposition:   " + lEntry.isDecomposition() +"\n");
		  
		  Iterator otherFormsIt = lEntry.getOtherForms().iterator();
		  int j=0;
		  while(otherFormsIt.hasNext()){
			  
			  LOtherForm fo = (LOtherForm) otherFormsIt.next();
			  System.out.println("Form n.      "+ j);
			  System.out.println("Number       "+ fo.getNumber());
			  System.out.println("Tense        "+ fo.getTense());
			  System.out.println("WrittenRep   "+fo.getWrittenRep()+ "\n");
			  j++;	  
		  }
		  
		  /* Get all SynBehaviors*/
		  Iterator synBehIt = lEntry.getSynBehaviors().iterator();
		  int k=0;
		  while(synBehIt.hasNext()){
			  System.out.println("SynBeh n.  "+ k);
			  LSynBehavior synB = (LSynBehavior) synBehIt.next();
			  System.out.println("SynB URI   "+   synB.getFrame());
			  System.out.println("AdverbialComplement   "+   synB.isFrameAdverbialComplement());
			  System.out.println("FrameAttributiveArg   "+    synB.isFrameAttributiveArg());
			  System.out.println(" CopulativeSubject    "+synB.isFrameCopulativeSubject());
			  System.out.println("DirectObject          "+synB.isFrameDirectObject());
			  System.out.println("PossessiveAdjunct     "+synB.isFramePossessiveAdjunct());
			  System.out.println("PrepositionalObject   "+synB.isFramePrepositionalObject());
			  System.out.println("Subject                "+synB.isFrameSubject()+ "\n");
			  k++;
			  
		  }
		  
		  /* Get all Senses*/
		  Iterator it =lEntry.getSenses().iterator();
		  int w = 0;
		  while(it.hasNext()){
			 
			  LSense ls = (LSense) it.next();
			  System.out.println("Sensw num:      "+ w);
			  System.out.println("Sense :         " + ls.getSense());
			  System.out.println("Reference :     " +ls.getReference());
			  System.out.println("ObjOfProp :     " +ls.getObjOfProp());
			  System.out.println("SubjOfProp :    " +ls.getSubjOfProp());
			  System.out.println("Is A :          "+ ls.getIsA() +"\n\n");
			  w++;
		  }
		  
		  System.out.println("\n\n\n\n");

		  
		  
		  i++;
	  }  
	  
		  

		  
		  

	
	  return  allLexiconEntry;
	  
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
