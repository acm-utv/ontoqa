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
import java.util.List;

import org.apache.jena.base.Sys;

import com.acmutv.ontoqa.core.lexicon.LEntry;
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
	
	/**
	 *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing an auxiliary verb (do, does, did, have, has, had...)
	 *  @param auxVerb the auxiliary verb
	 *  @return the Elementary SLTAG representing the specified ausiliary verb
	 **/
	public static ElementarySltag getSltagAuxiliaryVerb(String auxVerb)
	{
		Ltag ltag = LtagTemplates.auxiliaryVerb(auxVerb, "V", "DP1", "DP2");
		Dudes dudes = new SimpleDudes();
		ElementarySltag sltag = new SimpleElementarySltag(auxVerb, ltag, dudes);
		return sltag;
	}
	
	/**
	 * Generates all Elementary SLTAG we need
	 * @return the list of all Elementary SLTAG
	 **/
	@SuppressWarnings("static-access")
	public static List<ElementarySltag> getAllElementarySltag() throws IOException
	{
		List<LEntry> list = SerializeSltag.getAllLexiconElement();
		LEntry lexEl = new LEntry();
		List<ElementarySltag> listSltag = new ArrayList<ElementarySltag>();
		int i;
		for(i=0; i<list.size(); i++)
		{
			lexEl = list.get(i);
			switch(TYPE.valueOf(lexEl.getPartOfSpeech())) {
				case properNoun:
				{
					Ltag ltag =  LtagTemplates.properNoun(lexEl.getWrittenRep());
				    Dudes dudes = DudesTemplates.properNoun(lexEl.getSenses().iterator().next().getReference());
				    ElementarySltag sltag = new SimpleElementarySltag(lexEl.getWrittenRep(), ltag, dudes);
				    listSltag.add(sltag);
				    break;
				}
				case adjective:
				{
					//TODO ...
					System.out.println("adjective: "+list.get(i).getWrittenRep());
//					System.out.println();

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
					
					System.out.println("common noun: "+lexEl.getWrittenRep());
					
					/* LTAG */
					Ltag ltag = LtagTemplates.classNoun(list.get(i).getPartOfSpeech(), true);
//					System.out.println("edges "+ltag.getEdges());

					/* DUDES TODO */ 
//				    Dudes dudes = DudesTemplates.classNoun(list.get(i).getReferenceURI()...., false);

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

					System.out.println("verb: "+lexEl.getWrittenRep());
//					for acquire TODO ....
					break;
				}
			}
		}		
	    /* how many */
	    listSltag.add(SerializeSltag.getSltagHowMany("how", "many"));
	 	    
	    /* is, are, was, were */
	    for(i=0; i<SerializeSltag.copula.size(); i++)
	    	listSltag.add(SerializeSltag.getSltagCopula(copula.get(i)));
	    
	    /* do, does, did, have, has, had */
	    for(i=0; i<SerializeSltag.auxiliaryVerb.size(); i++)
	    	listSltag.add(SerializeSltag.getSltagAuxiliaryVerb(auxiliaryVerb.get(i)));
	    
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
		return listSltag;
	}
	
	
	public static void main(String args[]) throws IOException
	{
		SerializeSltag.getAllElementarySltag();
	}
	
}
