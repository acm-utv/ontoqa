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

import com.acmutv.ontoqa.core.lemon.LexicalEntry;
import com.acmutv.ontoqa.core.lemon.Lexicon;
import com.acmutv.ontoqa.core.lemon.Reference;
import com.acmutv.ontoqa.core.lemon.io.LexiconLoader;

import com.acmutv.ontoqa.core.lemon.Sense;
import com.acmutv.ontoqa.core.lemon.SyntacticArgument;
import com.acmutv.ontoqa.core.lemon.SyntacticBehaviour;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
  public static Lexicon importLexicon(String resource, String prefix, LexiconFormat format) throws IOException {
	 
	  Path path = FileSystems.getDefault().getPath(resource).toAbsolutePath(); 
	  String baseUri = path.toUri().toString();
	  LexiconLoader lexiconLoad= new LexiconLoader();
	  Lexicon lexicon = lexiconLoad.loadFromFile(baseUri);
	  return lexicon;
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
public static String getReferencePossessiveAdjunct(HashMap<Sense, HashSet<SyntacticBehaviour>> senseSynB){
	  
	  	Set<Sense> senses= senseSynB.keySet();
	    Object[] sensesArray= senses.toArray();
	    Collection<HashSet<SyntacticBehaviour>> synBehaviourIt= senseSynB.values();
	    Iterator synBIt= synBehaviourIt.iterator();
	    int k=0;
	    while(synBIt.hasNext()){
	    	HashSet<SyntacticBehaviour> sBehavoiur = (HashSet<SyntacticBehaviour>) synBIt.next();
	    	Iterator itSb =sBehavoiur.iterator();
	    	
	    	while(itSb.hasNext()){
	    		SyntacticBehaviour synB = (SyntacticBehaviour) itSb.next();
	    		Iterator ArgType = synB.getSynArgs().iterator();
	    		
	    		while(ArgType.hasNext()){
	    			
	    			SyntacticArgument arg= (SyntacticArgument) ArgType.next();
	    			if( (arg.getArgumentType()).equals("http://www.lexinfo.net/ontology/2.0/lexinfo#possessiveAdjunct")){
	    				Sense s = (Sense) sensesArray[k];
		    			return s.getReference().toString();
	    			}
	    		
	    		}
	    		
	    		 
	    	}
	    	k++;
	    }
	    return null;
	    
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
public static String getReference(HashMap<Sense, HashSet<SyntacticBehaviour>> senseSynB){
	  
	  	Set<Sense> senses= senseSynB.keySet();
	    Object[] sensesArray= senses.toArray();
	    Collection<HashSet<SyntacticBehaviour>> synBehaviourIt= senseSynB.values();
	    Iterator synBIt= synBehaviourIt.iterator();
	    int k=0;
	    while(synBIt.hasNext()){
	    	HashSet<SyntacticBehaviour> sBehavoiur = (HashSet<SyntacticBehaviour>) synBIt.next();
	    	Iterator itSb =sBehavoiur.iterator();
	    	
	    	while(itSb.hasNext()){
	    		SyntacticBehaviour synB = (SyntacticBehaviour) itSb.next();
	    		Iterator ArgType = synB.getSynArgs().iterator();
	    		
	    		while(ArgType.hasNext()){
	    				Sense s = (Sense) sensesArray[k];
		    			return s.getReference().toString();
	    		}
	    		
	    		 
	    	}
	    	k++;
	    }
	    return null;
	    
}
  
  
  @SuppressWarnings({ "unchecked", "unused", "rawtypes" })
public static List<String> getFrames(HashMap<Sense, HashSet<SyntacticBehaviour>> senseSynB){
	  
	  	Set<Sense> senses= senseSynB.keySet();
	  	List<String> frames = new ArrayList();
	    Object[] sensesArray= senses.toArray();
	    Collection<HashSet<SyntacticBehaviour>> synBehaviourIt= senseSynB.values();
	    Iterator synBIt= synBehaviourIt.iterator();
	    int k=0;
	    while(synBIt.hasNext()){
	    	HashSet<SyntacticBehaviour> sBehavoiur = (HashSet<SyntacticBehaviour>) synBIt.next();
	    	Iterator itSb =sBehavoiur.iterator();
	    	
	    	while(itSb.hasNext()){
	    		SyntacticBehaviour synB = (SyntacticBehaviour) itSb.next();
	    		frames.add(synB.getFrame());
	 
	    	}
	    }
	    return frames;
}
  
  public static String getOneReference(Set<Reference> references){
	  String reference="";
	  Iterator it = references.iterator();
	  while(it.hasNext()){
		  String ref = it.next().toString();
		  if( ref.contains("www.semanticweb.org")){
			  reference=ref;
		  }
	  }
	  return reference;
  }
  
  
  /**
   * Reads all Lexical Entry
   * @param resource the resource to read.
   * @param prefix the default prefix for the lexicon.
   * @param format the lexicon format.
   * @return List all Lexical Entries
   * @throws IOException
   */
public static List<LexicalEntry> getLexicalEntries(String resource, String prefix, LexiconFormat format) throws IOException{
	  
	  Lexicon lexicon = importLexicon(resource, prefix, format);
	
	  List<LexicalEntry> lexicalEntries= lexicon.getEntries();
	   return lexicalEntries;
	  
  }
  

   

}