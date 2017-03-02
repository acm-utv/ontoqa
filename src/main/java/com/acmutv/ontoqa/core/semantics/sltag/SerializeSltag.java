package com.acmutv.ontoqa.core.semantics.sltag;

import com.acmutv.ontoqa.core.lexicon.LexiconUsage;
import com.acmutv.ontoqa.core.semantics.dudes.Dudes;
import com.acmutv.ontoqa.core.semantics.dudes.DudesTemplates;
import com.acmutv.ontoqa.core.syntax.ltag.Ltag;
import com.acmutv.ontoqa.core.syntax.ltag.LtagTemplates;

import java.io.IOException;
import java.util.List;

import com.acmutv.ontoqa.core.lexicon.LexiconElement;

public class SerializeSltag {

	public static List<LexiconElement> getAllLexiconElement() throws IOException
	{
		List<LexiconElement> listLexElem = LexiconUsage.getAllLexiconElement();
		return listLexElem;
	}
	public static void serializeElements() throws IOException
	{
		List<LexiconElement> list = SerializeSltag.getAllLexiconElement();
		LexiconElement lexEl = new LexiconElement();
		int i;
		for(i=0; i<list.size(); i++)
		{
			lexEl = list.get(i);
			switch(lexEl.getType()) {
				case "properNoun":
					System.out.println("properNoun: "+list.get(i).getName());				
					LtagTemplates.properNoun(lexEl.getName());
					//serialize.......
				case "adjective":
//					LtagTemplates.
					
			}
		}
	}
	
	private enum TYPE{properNoun, adjective, NounPhrase, commonNoun, preposition, verb, AdjectivePhrase};
	
	public static void main(String args[]) throws IOException
	{
		
		List<LexiconElement> list = SerializeSltag.getAllLexiconElement();
		LexiconElement lexEl = new LexiconElement();
		int i=0;
		while(i<list.size())
		{
			lexEl = list.get(i);
			switch(TYPE.valueOf(lexEl.getType())) {
				case properNoun:
				{
					
					/* LTAG */
					Ltag ltag =  LtagTemplates.properNoun(lexEl.getName());
					
					/* DUDES */
					
					
					//serialize.......
					i++;

					break;
				}
				case adjective:
				{
//					LtagTemplates.....
					System.out.println("adjective: "+list.get(i).getName());
					i++;

					break;
				}	
				case NounPhrase:
				{
					//chief executive officer separato? e net income!!
//					System.out.println("NounPhrase: "+list.get(i).getName());
					i++;
					break;
				}
				case commonNoun:
				{
					
					/* LTAG */
					Ltag ltag = LtagTemplates.classNoun(list.get(i).getName(), true);
					
					/* DUDES */
//				    Dudes dudes = DudesTemplates.classNoun(list.get(i).getReferenceURI()...., false);

					
					/* SERIALIZE */
					
					
					i++;
					break;
				}
				case preposition:
				{
					System.out.println("preposition: "+list.get(i).getName());
					i++;
					break;
				}
				case verb:
				{
					System.out.println("verb: "+list.get(i).getName());
					System.out.println("verb: "+list.get(i).getForms());
//					for founded il template è il seguente
					Ltag ltag =  LtagTemplates.transitiveVerbActiveIndicative(list.get(i).getName(), "REL", "DP1");
//					for acquire il template è il seguente
					//....
					i++;
					break;
				}
				case AdjectivePhrase:
				{
					//da chiedere a Fiorelli "most valuable company".
//					System.out.println("AdjectivePhrase: "+list.get(i).getName());
					i++;
					break;
				}					
			}
		}
	}
	
	
//	public static void main(String args[]) throws IOException
//	{
//		SerializeSltag serSltag = new SerializeSltag();
//		List<LexiconElement> list = SerializeSltag.getAllLexiconElement();
//		
//		int i;
//		for(i=0; i<list.size(); i++)
//		{
//			switch(list.get(i).getType()) {
//			if(list.get(i).getType().equals("properNoun"))
//				LtagTemplates.properNoun(list.get(i).getName());
//			}
//		}
//	}
}
