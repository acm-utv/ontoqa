package com.acmutv.ontoqa.core.semantics.sltag;

import com.acmutv.ontoqa.core.lexicon.LexiconUsage;
import com.acmutv.ontoqa.core.semantics.dudes.Dudes;
import com.acmutv.ontoqa.core.semantics.dudes.DudesTemplates;
import com.acmutv.ontoqa.core.semantics.dudes.SimpleDudes;
import com.acmutv.ontoqa.core.syntax.ltag.Ltag;
import com.acmutv.ontoqa.core.syntax.ltag.LtagTemplates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.base.Sys;

import com.acmutv.ontoqa.core.lexicon.LEntry;
import com.acmutv.ontoqa.core.lexicon.LOtherForm;
import com.acmutv.ontoqa.core.lexicon.LSense;
import com.acmutv.ontoqa.core.lexicon.LSynBehavior;
import com.acmutv.ontoqa.core.lexicon.LexiconElement;

public class SerializeSltag {
	
	private enum TYPE{properNoun, adjective, NounPhrase, commonNoun, preposition, verb};
	private static List<String> auxiliaryVerb = Arrays.asList("do", "does", "did", "have", "has", "had");
	private static List<String> copula = Arrays.asList("is", "are", "was", "were");
	private static List<String> articles = Arrays.asList("the", "a", "an");
	private static List<String> whPronoun = Arrays.asList("who", "what", "where");
	
	/**
	 * Generates a list of Lexicon Elements
	 **/
	public static List<LEntry> getAllLexiconElement() throws IOException
	{
		List<LEntry> listLexElem = LexiconUsage.getAllLexiconElement();
		return listLexElem;
	}
	
	/**
	 *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) for a wh-pronoun (who, what,...)
	 *  @param lexical the wh-pronoun
	 *  @return the Elementary SLTAG representing the specified wh-pronoun
	 **/
	public static ElementarySltag getSltagWh(String lexical)
	{
		Ltag ltagWh = LtagTemplates.wh(lexical);
	    Dudes dudesWh = DudesTemplates.wh();
	    ElementarySltag sltagWh = new SimpleElementarySltag(lexical, ltagWh, dudesWh);
	    return sltagWh;
	}
	
	/**
	 *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) for how-pronoun phrase (how many, how much, how long,...)
	 *  @param pronoun the pronoun
	 *  @param adverb the adverb
	 *  @return the Elementary SLTAG representing the specified how-pronoun phrase
	 **/
	public static ElementarySltag getSltagHowMany(String adverb, String pronoun)
	{
		Ltag ltagHowMany = LtagTemplates.how(adverb, pronoun, "NP");
		Dudes dudesHowMany = DudesTemplates.howmany("NP");
		ElementarySltag sltagHowMany = new SimpleElementarySltag("how many", ltagHowMany, dudesHowMany);
		return sltagHowMany;
	}
	
	/**
	 *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) for definite and indefinite articles(the, a, an)
	 *  @param article the article
	 *  @return the Elementary SLTAG representing the specified article
	 **/
	public static ElementarySltag getSltagDet(String article)
	{
		Ltag ltagDet = LtagTemplates.determiner(article, "NP");
		Dudes dudesDet = DudesTemplates.determiner("NP");
		ElementarySltag sltagDet = new SimpleElementarySltag(article, ltagDet, dudesDet);
		return sltagDet;
	}

	
	/**
	 *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing a copula (is, are, was, were,...)
	 *  @param copula the copula
	 *  @return the Elementary SLTAG representing the specified article
	 **/
	public static ElementarySltag getSltagCopula(String copula)
	{
		Ltag ltagCopula = LtagTemplates.copula(copula, "DP1", "DP2");
		Dudes dudesCopula = DudesTemplates.copula("DP1", "DP2" );
		ElementarySltag sltagCopula = new SimpleElementarySltag(copula, ltagCopula, dudesCopula);
		return sltagCopula;
	}
	
//	/**
//	 *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing an auxiliary verb (do, does, did, have, has, had...)
//	 *  @param auxVerb the auxiliary verb
//	 *  @return the Elementary SLTAG representing the specified ausiliary verb
//	 **/
//	public static ElementarySltag getSltagAuxiliaryVerbSub(String auxVerb)
//	{
//		Ltag ltag = LtagTemplates.auxiliaryVerbSub(auxVerb, "V", "DP1", "DP2");
//		Dudes dudes = new SimpleDudes();
//		ElementarySltag sltag = new SimpleElementarySltag(auxVerb, ltag, dudes);
//		return sltag;
//	}

	/**
	 *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing an auxiliary verb (do, does, did, have, has, had...)
	 *  @param auxVerb the auxiliary verb
	 *  @return the Elementary SLTAG representing the specified ausiliary verb
	 **/
	public static ElementarySltag getSltagAuxiliaryVerbAdj(String auxVerb)
	{
		Ltag ltag = LtagTemplates.auxiliaryVerbAdj(auxVerb, "S");
		Dudes dudes = new SimpleDudes();
		ElementarySltag sltag = new SimpleElementarySltag(auxVerb, ltag, dudes);
		return sltag;
	}

	/**
	 *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing the words "name of"
	 *  @return the Elementary SLTAG representing "name of".
	 **/
	public static ElementarySltag getSltagNameOf()
	{
		Ltag ltag = LtagTemplates.nameOf("DP");
		Dudes dudes = new SimpleDudes();
		ElementarySltag sltag = new SimpleElementarySltag("name of", ltag, dudes);
		return sltag;
	}
	
	/**
	 * Generates all Elementary SLTAG we need
	 * @return the list of all Elementary SLTAG
	 **/
	public static List<ElementarySltag> getAllElementarySltag() throws IOException
	{
		List<LEntry> list = SerializeSltag.getAllLexiconElement();
		LEntry lEntry = new LEntry();
		List<ElementarySltag> listSltag = new ArrayList<ElementarySltag>();
		int i;
		for(i=0; i<list.size(); i++)
		{
			lEntry = list.get(i);
			switch(TYPE.valueOf(lEntry.getPartOfSpeech())) {
				case properNoun:
				{
					Ltag ltag =  LtagTemplates.properNoun(lEntry.getWrittenRep());
				    Dudes dudes = DudesTemplates.properNoun(lEntry.getSenses().iterator().next().getReference());
				    ElementarySltag sltag = new SimpleElementarySltag(lEntry.getWrittenRep(), ltag, dudes);
				    listSltag.add(sltag);
				    break;
				}
				case adjective:
				{
					//TODO ...
					  
					break;
				}	
				case NounPhrase:
				{
					//chief executive officer e net income!! TODO ...
//					System.out.println("NounPhrase: "+list.get(i).getName());
					break;
				}
				case commonNoun:
				{
					System.out.println("commonNoun: "+lEntry.getWrittenRep());
					
					
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
					  
					  
//					  System.out.println("\n\n\n\n");

//					if(/*possessiveAdjunct = true*/)
						
					
//					Iterator ls = lexEl.getSenses().iterator();
////					Ltag ltag = LtagTemplates.classNoun(list.get(i).getPartOfSpeech(), true);
//					while(ls.hasNext()){
////					System.out.println("edges "+ltag.getEdges());
//						LSense lSense = (LSense) ls.next();
//						
//						/* LTAG */
//						Ltag ltag = LtagTemplates.classNoun(lexEl.getWrittenRep(), true);
////						System.out.println("edges "+ltag.getEdges());
//
//						/* DUDES TODO */ 
//					    Dudes dudes = DudesTemplates.classNoun(lSense.getReference(), false);
//						
//					    ElementarySltag sltag = new SimpleElementarySltag(lexEl.getWrittenRep(), ltag, dudes);
//					    listSltag.add(sltag);
//					    
//					}


					break;
				}
				case preposition:
				{
//					System.out.println("preposition: "+list.get(i).getName());
					break;
				}
				case verb:
				{
//					for founded il template è il seguente
//					Ltag ltag =  LtagTemplates.transitiveVerbActiveIndicative(list.get(i).getName(), "DP1", "DP2");

					System.out.println("verb: "+lEntry.getWrittenRep());
					
//					for acquire TODO ....
					break;
				}
			}
		}		
		
	    /* how many */
	    listSltag.add(SerializeSltag.getSltagHowMany("how", "many"));
	    
	    /* name of */
	    listSltag.add(SerializeSltag.getSltagNameOf());
	 	    
	    /* is, are, was, were */
	    for(i=0; i<SerializeSltag.copula.size(); i++)
	    	listSltag.add(SerializeSltag.getSltagCopula(copula.get(i)));
	    
	    /* do, does, did, have, has, had */
//	    for(i=0; i<SerializeSltag.auxiliaryVerbSub.size(); i++)
//	    	listSltag.add(SerializeSltag.getSltagAuxiliaryVerb(auxiliaryVerb.get(i)));
	    
	    /* do, does, did, have, has, had */
	    for(i=0; i<SerializeSltag.auxiliaryVerb.size(); i++)
	    	listSltag.add(SerializeSltag.getSltagAuxiliaryVerbAdj(auxiliaryVerb.get(i)));
	    
	    /* the, a, an */
	    for(i=0; i<SerializeSltag.articles.size(); i++)
	    	listSltag.add(SerializeSltag.getSltagDet(articles.get(i)));
	    
	    /* who, what, where */
	    for(i=0; i<SerializeSltag.whPronoun.size(); i++)
	    	listSltag.add(SerializeSltag.getSltagWh(whPronoun.get(i)));
	    
		for(i=0; i<listSltag.size(); i++)
		{
			System.out.println("entry: "+listSltag.get(i).getEntry());
//			System.out.println("semantics: "+listSltag.get(i).getSemantics());
			System.out.println("edges: "+listSltag.get(i).getEdges());
		}

		
//		System.out.println(LtagTemplates.classNoun("prova", true).getEdges());
		return listSltag;
	}
	
	
	public static void main(String args[]) throws IOException
	{
		SerializeSltag.getAllElementarySltag();
	}
	
}


//System.out.println("adjective: "+list.get(i).getWrittenRep());
//System.out.println("senses: "+list.get(i).getSenses().get(0).getSense());
//System.out.println("Reference :     " +list.get(i).getSenses().get(0).getReference());
//  System.out.println("ObjOfProp :     " +list.get(i).getSenses().get(0).getObjOfProp());
//  System.out.println("SubjOfProp :    " +list.get(i).getSenses().get(0).getSubjOfProp());
//  System.out.println("Is A :          "+ list.get(i).getSenses().get(0).getIsA() +"\n\n");
//  Iterator synBehIt = lexEl.getSynBehaviors().iterator();
//  int k=0;
//	System.out.println("Syn Beh size: "+list.get(i).getSynBehaviors().size());
//  while(synBehIt.hasNext()){
//	  System.out.println("SynBeh n.  "+ k);
//	  LSynBehavior synB = (LSynBehavior) synBehIt.next();
//	  System.out.println("SynB URI   "+   synB.getFrame());
//	  System.out.println("AdverbialComplement   "+   synB.isFrameAdverbialComplement());
//	  System.out.println("FrameAttributiveArg   "+    synB.isFrameAttributiveArg());
//	  System.out.println(" CopulativeSubject    "+synB.isFrameCopulativeSubject());
//	  System.out.println("DirectObject          "+synB.isFrameDirectObject());
//	  System.out.println("PossessiveAdjunct     "+synB.isFramePossessiveAdjunct());
//	  System.out.println("PrepositionalObject   "+synB.isFramePrepositionalObject());
//	  System.out.println("Subject                "+synB.isFrameSubject()+ "\n");
//	  k++;
//  }

