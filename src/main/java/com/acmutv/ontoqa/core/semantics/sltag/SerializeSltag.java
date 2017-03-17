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

import com.acmutv.ontoqa.core.lemon.Language;
import com.acmutv.ontoqa.core.lemon.LexicalEntry;
import com.acmutv.ontoqa.core.lexicon.LexiconFormat;

public class SerializeSltag {
	
	private enum TYPE{properNoun, adjective, commonNoun, preposition, verb};
	private static List<String> auxiliaryVerb = Arrays.asList("do", "does", "did", "have", "has", "had");
	private static List<String> copula = Arrays.asList("is", "are", "was", "were");
	private static List<String> articles = Arrays.asList("the", "a", "an");
	private static List<String> whPronoun = Arrays.asList("who", "what", "where");
	
	/**
	 * Generates a list of Lexicon Elements
	 **/
	public static List<LexicalEntry> getLexicalEntries() throws IOException
	{
		List<LexicalEntry> listLexElem = LexiconUsage.getLexicalEntries("data/lexicon/organization.rdf","", LexiconFormat.RDFXML);
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
	 *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing a proper noun (Microsoft, Google,...)
	 *  @param relNoun the proper noun.
	 *  @return the Elementary SLTAG representing the specified proper noun.
	 **/
	public static ElementarySltag getSltagProperNoun(String properNoun, String propertyIRI)
	{
		Ltag ltag =  LtagTemplates.properNoun(properNoun);
	    Dudes dudes = DudesTemplates.properNoun(propertyIRI);
	    ElementarySltag sltag = new SimpleElementarySltag(properNoun, ltag, dudes);
		return sltag;
	}
	
	
	/**
	 *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing a relational prepositional noun (founder of, chairman of,...)
	 *  @param relNoun the relational noun.
	 *  @return the Elementary SLTAG representing the specified relational noun.
	 **/
	public static ElementarySltag getSltagRelPrepNoun(String relNoun, String preposition, String anchor, String propertyIRI)
	{
		Ltag ltag = LtagTemplates.relationalPrepositionalNoun(relNoun, preposition, anchor, false);
		Dudes dudes = DudesTemplates.relationalNoun(propertyIRI, anchor, false);
		ElementarySltag sltag = new SimpleElementarySltag(relNoun, ltag, dudes);
		return sltag;
	}

	/**
	 *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing a class noun.
	 *  @param noun the class noun.
	 *  @return the Elementary SLTAG representing the specified class noun.
	 **/
	public static ElementarySltag getSltagClassNoun(String noun, String predicateIRI)
	{
		Ltag ltag = LtagTemplates.classNoun(noun, false);
		Dudes dudes = DudesTemplates.classNoun(predicateIRI, false);
		ElementarySltag sltag = new SimpleElementarySltag(noun, ltag, dudes);
		return sltag;
	}
	
	/**
	 *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing a attributive Adjective.
	 *  @param noun the attribute adjective.
	 *  @return the Elementary SLTAG representing the specified class noun.
	 **/
	public static ElementarySltag getSltagAttributiveAdj(String attrAdj)
	{
		Ltag ltag = LtagTemplates.adjectiveAttributive(attrAdj, "S");
		//Modify
		Dudes dudes = new SimpleDudes();
		ElementarySltag sltag = new SimpleElementarySltag(attrAdj, ltag, dudes);
		return sltag;
	}
	
	public static ElementarySltag getSltagTransitiveVerbActiveIndicative(String verb,String predicateIRI){
		
		Ltag ltag =  LtagTemplates.transitiveVerbActiveIndicative(verb, "DP1", "DP2");
		Dudes dudes = DudesTemplates.transitiveVerb(predicateIRI, "DP1", "DP2");
		
		ElementarySltag sltag = new SimpleElementarySltag(verb, ltag, dudes);
		return sltag;
	}
	
	
	
	/**
	 * Generates all Elementary SLTAG we need
	 * @return the list of all Elementary SLTAG
	 **/
	public static List<ElementarySltag> getAllElementarySltag() throws IOException
	{
		List<LexicalEntry> list = SerializeSltag.getLexicalEntries();
		LexicalEntry lEntry = new LexicalEntry(Language.EN);
		List<ElementarySltag> listSltag = new ArrayList<ElementarySltag>();
		int i;
		for(i=0; i<list.size(); i++)
		{
			lEntry = list.get(i);
			switch(TYPE.valueOf(lEntry.getPOS())) {
				case properNoun:
				{
				    listSltag.add(SerializeSltag.getSltagProperNoun(lEntry.getCanonicalForm(), lEntry.getReferences().toString()));
				    break;
				}
				case adjective:
				{
					//TODO ...
					System.out.println("commonNoun: "+lEntry.getCanonicalForm());
					List<String> frames = LexiconUsage.getFrames(lEntry.getSenseBehaviours());
					for(int k=0; k<lEntry.getSenseBehaviours().size(); k++){
						if(frames.get(k).equals(" AdjectiveAttributiveFrame")){
							listSltag.add(SerializeSltag.getSltagAttributiveAdj(lEntry.getCanonicalForm() ));
								
						}else if(frames.get(k).equals(" AdjectivePredicativeFrame")){
							
							
						}else if(frames.get(k).equals(" AdjectivePPFrame")){
							
						}
					}

					break;
				}	
				case commonNoun:
				{
					System.out.println("commonNoun: "+lEntry.getCanonicalForm());
								
					if(lEntry.getReferences().size() > 0 ) {
					      String ref = LexiconUsage.getReferencePossessiveAdjunct(lEntry.getSenseBehaviours());
						  if(ref!= null)
						  {
							  
							  listSltag.add(SerializeSltag.getSltagRelPrepNoun(lEntry.getCanonicalForm(), "of", "DP", ref));
							  for(int j=0; j<lEntry.getForms().size(); j++)
							  {
								  listSltag.add(SerializeSltag.getSltagRelPrepNoun(lEntry.getForms().get(j).getWrittenRep(), "of", "DP", ref));
							  }
						  }
						  else
						  {
							
							  listSltag.add(SerializeSltag.getSltagClassNoun(lEntry.getCanonicalForm(), lEntry.getReferences().toString()));
							  for( int j=0; j<lEntry.getForms().size(); j++)
							  {
								  listSltag.add(SerializeSltag.getSltagClassNoun(lEntry.getForms().get(j).getWrittenRep(), ref));
							  }
							
						  }
					}

					break;
				}
				case preposition:
				{
					// Come preposition abbiamo solo in
					//System.out.println("preposition: "+list.get(i).getWrittenRep());
					break;
				}
				case verb:
				{
					 listSltag.add(SerializeSltag.getSltagTransitiveVerbActiveIndicative(lEntry.getCanonicalForm(), lEntry.getReferences().toString()));
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
		
	    /* do, does, did, have, has, had */
//	    for(i=0; i<SerializeSltag.auxiliaryVerbSub.size(); i++)
//	    	listSltag.add(SerializeSltag.getSltagAuxiliaryVerb(auxiliaryVerb.get(i)));

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

