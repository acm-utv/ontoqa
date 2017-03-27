package com.acmutv.ontoqa.core.semantics.sltag;

import com.acmutv.ontoqa.core.lexicon.LexiconUsage;
import com.acmutv.ontoqa.core.semantics.dudes.Dudes;
import com.acmutv.ontoqa.core.semantics.dudes.DudesTemplates;
import com.acmutv.ontoqa.core.semantics.dudes.SimpleDudes;
import com.acmutv.ontoqa.core.syntax.ltag.Ltag;
import com.acmutv.ontoqa.core.syntax.ltag.LtagTemplates;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.acmutv.ontoqa.core.grammar.Grammar;
import com.acmutv.ontoqa.core.grammar.SimpleGrammar;
import com.acmutv.ontoqa.core.grammar.serial.GrammarJsonMapper;
import com.acmutv.ontoqa.core.lemon.Language;
import com.acmutv.ontoqa.core.lemon.LexicalEntry;
import com.acmutv.ontoqa.core.lemon.Reference;

public class SerializeSltag {
	
	private enum TYPE{properNoun, adjective, commonNoun, preposition, verb}

  private static List<String> auxiliaryVerb = Arrays.asList("do", "does", "did", "have", "has", "had");
	private static List<String> copula = Arrays.asList("is", "are", "was", "were");
	private static List<String> articles = Arrays.asList("the", "a", "an");
	private static List<String> whPronoun = Arrays.asList("who", "what", "where");
	
	
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
		Ltag ltagCopula = LtagTemplates.copula(copula, "DP", "DP");
		Dudes dudesCopula = DudesTemplates.copula("DP", "DP" );
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
	public static ElementarySltag getSltagRelPrepNounOf(String relNoun, String preposition, String anchor, String propertyIRI)
	{
		Ltag ltag = LtagTemplates.relationalPrepositionalNoun(relNoun, preposition, anchor, false);
		Dudes dudes = DudesTemplates.relationalNoun(propertyIRI, anchor, false);
		ElementarySltag sltag = new SimpleElementarySltag(relNoun, ltag, dudes);
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
		ElementarySltag sltag = new SimpleElementarySltag(relNoun + " of", ltag, dudes);
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
	 *  @param attrAdj the attribute adjective.
	 *  @param predicateIRI reference to ontology
	 *  @return the Elementary SLTAG representing the specified attributive adjective.
	 **/
	public static ElementarySltag getSltagAttributiveAdj(String attrAdj,String predicateIRI)
	{
		Ltag ltag = LtagTemplates.adjectiveAttributive(attrAdj, "N");
		Dudes dudes = DudesTemplates.adjective(predicateIRI);
		ElementarySltag sltag = new SimpleElementarySltag(attrAdj, ltag, dudes);
		return sltag;
	}
	
	/**
	 *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing a Adjective with CopulativeScalar.
	 *  @param copAdj the attribute adjective.
	 *  @param predicateIRI reference to ontology
	 *  @return the Elementary SLTAG representing the specified attributive adjective.
	 **/
	public static ElementarySltag getSltagCopulativeScalarAdj(String copAdj,String predicateIRI)
	{
		Ltag ltag = LtagTemplates.adjectiveCopulativeScalar(copAdj, "NP", "most");
		Dudes dudes = DudesTemplates.adjective(predicateIRI);
		ElementarySltag sltag = new SimpleElementarySltag(copAdj, ltag, dudes);
		return sltag;
	}
	
	/**
	 *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing a pp Adjective.
	 *  @param ppAdj the adjective.
	 *  @param predicateIRI reference to ontology
	 *  @return the Elementary SLTAG representing the specified pp adjective.
	 **/
	public static ElementarySltag getSltagPPAdj(String ppAdj,String predicateIRI)
	{
		Ltag ltag = LtagTemplates.adjectivePP(ppAdj, "NP");
		Dudes dudes = DudesTemplates.adjective(predicateIRI);
		ElementarySltag sltag = new SimpleElementarySltag(ppAdj, ltag, dudes);
		return sltag;
	}
	
	/**
	 *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing a predicative Adjective.
	 *  @param predAdj the predicative adjective.
	 *  @param predicateIRI reference to ontology
	 *  @return the Elementary SLTAG representing the specified predicative adjective.
	 **/
	public static ElementarySltag getSltagPredicativeAdj(String predAdj,String predicateIRI)
	{
		Ltag ltag = LtagTemplates.adjectivePredicative(predAdj);
		Dudes dudes = DudesTemplates.adjective(predicateIRI);
		ElementarySltag sltag = new SimpleElementarySltag(predAdj, ltag, dudes);
		return sltag;
	}
	
	public static ElementarySltag getSltagTransitiveVerbActiveIndicative(String verb,String predicateIRI){
		
		Ltag ltag =  LtagTemplates.transitiveVerbActiveIndicative(verb, "DP", "DP");
		Dudes dudes = DudesTemplates.transitiveVerb(predicateIRI, "DP", "DP");
		
		ElementarySltag sltag = new SimpleElementarySltag(verb, ltag, dudes);
		return sltag;
	}
	
	
	public static ElementarySltag getSltagPreposition(String preposition,String predicateIRI){
		
		Ltag ltag =  LtagTemplates.prepositionAdj(preposition, "DP","DP");
		Dudes dudes = new SimpleDudes();
		ElementarySltag sltag = new SimpleElementarySltag(preposition, ltag, dudes);
		return sltag;
	}
	
	public static void writeGrammarOnFile(Grammar grammar, File file) throws InstantiationException, IllegalAccessException, IOException
	{
		GrammarJsonMapper jsonMapper = new GrammarJsonMapper();		
		jsonMapper.writeValue(file, grammar);
	}
	
	public static Grammar readGrammarFromFile(File file) throws JsonParseException, JsonMappingException, IOException
	{
		GrammarJsonMapper jsonMapper = new GrammarJsonMapper();		
		Grammar grammar = jsonMapper.readValue(file, new TypeReference<Grammar>(){});
		
//		for(ElementarySltag k : myGrammar.getAllElementarySLTAG())
//		{
//			System.out.println("my grammar entry: "+k.getEntry());
//			System.out.println("my grammar edges: "+k.getEdges());
//		}
		
		return grammar;
	}
	
	/**
	 * Generates all Elementary SLTAG we need
	 * @return the list of all Elementary SLTAG
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 **/
	public static Grammar getAllElementarySltag(List<LexicalEntry> list) throws IOException, InstantiationException, IllegalAccessException
	{
		//List<LexicalEntry> list = SerializeSltag.getLexicalEntries();
		LexicalEntry lEntry = new LexicalEntry(Language.EN);
		List<ElementarySltag> listSltag = new ArrayList<ElementarySltag>();
		Grammar grammar= new SimpleGrammar();
		int i;
		for(i=0; i<list.size(); i++)
		{
			lEntry = list.get(i);
			switch(TYPE.valueOf(lEntry.getPOS())) {
				case properNoun:
				{
				    listSltag.add(SerializeSltag.getSltagProperNoun(lEntry.getCanonicalForm(), lEntry.getReferences().toString()));
				    grammar.addElementarySLTAG(SerializeSltag.getSltagProperNoun(lEntry.getCanonicalForm(), lEntry.getReferences().toString()));
				    break;
				    
				}
				case adjective:
				{
					//TODO ...
					//System.out.println("adjective: "+lEntry.getCanonicalForm());
					boolean attr= false;
					boolean pred = false;
					List<String> frames = LexiconUsage.getFrames(lEntry.getSenseBehaviours());
					for(int k=0; k< frames.size(); k++){
						if(frames.get(k).contains("AdjectiveAttributiveFrame") && !attr){
							
							if(lEntry.isCovariantScalar()){
								grammar.addElementarySLTAG(SerializeSltag.getSltagCopulativeScalarAdj(lEntry.getCanonicalForm(), lEntry.getReferences().toString()));
							}else{
								for( Reference ref: lEntry.getReferences()){
									listSltag.add(SerializeSltag.getSltagAttributiveAdj(lEntry.getCanonicalForm(), ref.toString()));	
									grammar.addElementarySLTAG(SerializeSltag.getSltagAttributiveAdj(lEntry.getCanonicalForm(), ref.toString()));
								}	
							}
							attr= true;
						}else if(frames.get(k).equals("AdjectivePPFrame")){
							listSltag.add(SerializeSltag.getSltagPPAdj(lEntry.getCanonicalForm(), lEntry.getReferences().toString()));
							grammar.addElementarySLTAG(SerializeSltag.getSltagPPAdj(lEntry.getCanonicalForm(), lEntry.getReferences().toString()));
						}
						else if(frames.get(k).equals("AdjectivePredicativeFrame") && !pred){
							
							for( Reference ref: lEntry.getReferences()){
								listSltag.add(SerializeSltag.getSltagPredicativeAdj(lEntry.getCanonicalForm(), ref.toString()));
								grammar.addElementarySLTAG(SerializeSltag.getSltagPredicativeAdj(lEntry.getCanonicalForm(), ref.toString()));
							 }
							pred= true;
						}
					}

					break;
				}	
				case commonNoun:
				{
					if(lEntry.getReferences().size() > 0 ) {
					      String ref = LexiconUsage.getReferencePossessiveAdjunct(lEntry.getSenseBehaviours());
						  if(ref!= null)
						  {
							  
							  listSltag.add(SerializeSltag.getSltagRelPrepNoun(lEntry.getCanonicalForm(), "of", "DP", ref));
							  grammar.addElementarySLTAG(SerializeSltag.getSltagRelPrepNoun(lEntry.getCanonicalForm(), "of", "DP", ref));
							  grammar.addElementarySLTAG(SerializeSltag.getSltagRelPrepNounOf(lEntry.getCanonicalForm(), "of", "DP", ref));
							  for(int j=0; j<lEntry.getForms().size(); j++)
							  {
								  listSltag.add(SerializeSltag.getSltagRelPrepNoun(lEntry.getForms().get(j).getWrittenRep(), "of", "DP", ref));
								  grammar.addElementarySLTAG(SerializeSltag.getSltagRelPrepNoun(lEntry.getForms().get(j).getWrittenRep(), "of", "DP", ref));
								  grammar.addElementarySLTAG(SerializeSltag.getSltagRelPrepNounOf(lEntry.getForms().get(j).getWrittenRep(), "of", "DP", ref));
								  
							  }
						  }
						  else
						  {
							  String reference = LexiconUsage.getReference(lEntry.getSenseBehaviours());
							  listSltag.add(SerializeSltag.getSltagClassNoun(lEntry.getCanonicalForm(), reference));
							  grammar.addElementarySLTAG(SerializeSltag.getSltagClassNoun(lEntry.getCanonicalForm(), reference));
							  for( int j=0; j<lEntry.getForms().size(); j++)
							  {
								  listSltag.add(SerializeSltag.getSltagClassNoun(lEntry.getForms().get(j).getWrittenRep(), reference));
								  grammar.addElementarySLTAG(SerializeSltag.getSltagClassNoun(lEntry.getForms().get(j).getWrittenRep(), reference));
							  }
							
						  }
					}

					break;
				}
				case preposition:
				{
					// Valutare l'aggiunta di PP a DP( Det, N, Adj, PP(Preposition, DP2)
					listSltag.add(SerializeSltag.getSltagPreposition(lEntry.getCanonicalForm(), lEntry.getReferences().toString()));
					grammar.addElementarySLTAG(SerializeSltag.getSltagPreposition(lEntry.getCanonicalForm(), lEntry.getReferences().toString()));
					break;
				}
				case verb:
				{
					 listSltag.add(SerializeSltag.getSltagTransitiveVerbActiveIndicative(lEntry.getCanonicalForm(), lEntry.getReferences().toString()));
					 grammar.addElementarySLTAG(SerializeSltag.getSltagTransitiveVerbActiveIndicative(lEntry.getCanonicalForm(), lEntry.getReferences().toString()));
					 
					  for(int j=0; j<lEntry.getForms().size(); j++)
					  {
						  listSltag.add(SerializeSltag.getSltagTransitiveVerbActiveIndicative(lEntry.getForms().get(j).getWrittenRep(), lEntry.getReferences().toString()));
						  grammar.addElementarySLTAG(SerializeSltag.getSltagTransitiveVerbActiveIndicative(lEntry.getForms().get(j).getWrittenRep(), lEntry.getReferences().toString()));
						  
					  }
					break;
				}
			}
		}		
		
	    /* how many */
	    listSltag.add(SerializeSltag.getSltagHowMany("how", "many"));
	    grammar.addElementarySLTAG(SerializeSltag.getSltagHowMany("how", "many"));
	    
	    /* name of */
	    listSltag.add(SerializeSltag.getSltagNameOf());
	    grammar.addElementarySLTAG(SerializeSltag.getSltagNameOf());
	    
	 	    	    
	    /* is, are, was, were */
	    for(i=0; i<SerializeSltag.copula.size(); i++){
	    	listSltag.add(SerializeSltag.getSltagCopula(copula.get(i)));
	        grammar.addElementarySLTAG(SerializeSltag.getSltagCopula(copula.get(i)));
	    }
	    
	    /* do, does, did, have, has, had */
	    for(i=0; i<SerializeSltag.auxiliaryVerb.size(); i++){
	    	listSltag.add(SerializeSltag.getSltagAuxiliaryVerbAdj(auxiliaryVerb.get(i)));
	        grammar.addElementarySLTAG(SerializeSltag.getSltagAuxiliaryVerbAdj(auxiliaryVerb.get(i)));
	    }
	    
	    /* the, a, an */
	    for(i=0; i<SerializeSltag.articles.size(); i++){
	    	listSltag.add(SerializeSltag.getSltagDet(articles.get(i)));
	        grammar.addElementarySLTAG(SerializeSltag.getSltagDet(articles.get(i)));
	    }
	    
	    /* who, what, where */
	    for(i=0; i<SerializeSltag.whPronoun.size(); i++){
	    	listSltag.add(SerializeSltag.getSltagWh(whPronoun.get(i)));
	    	grammar.addElementarySLTAG(SerializeSltag.getSltagWh(whPronoun.get(i)));
	    }
	    
		
	    /* do, does, did, have, has, had */
//	    for(i=0; i<SerializeSltag.auxiliaryVerbSub.size(); i++)
//	    	listSltag.add(SerializeSltag.getSltagAuxiliaryVerb(auxiliaryVerb.get(i)));
	    List<ElementarySltag> eSl = grammar.getAllElementarySLTAG();
	    for(ElementarySltag k : eSl){
	    	System.out.println(k.getEntry());
	    	System.out.println(k.getEdges());
	    	
	    }
		
		return grammar;
	}
	
	
}


