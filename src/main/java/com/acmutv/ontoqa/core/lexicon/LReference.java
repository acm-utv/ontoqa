package com.acmutv.ontoqa.core.lexicon;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.citec.sc.lemon.core.LexicalEntry;
import de.citec.sc.lemon.core.Reference;

public class LReference {

	
	public LReference() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void setRestriction(List<LexicalEntry> lEntries,LSense sense ){
		Iterator it = lEntries.iterator();
		while(it.hasNext()){
			LexicalEntry lE = (LexicalEntry) it.next();
			Set<Reference> references = lE.getReferences();
			Iterator refs= references.iterator();
			while(refs.hasNext()){
				Reference ref = (Reference) refs.next();
				String allRef = ref.toString();
				if(allRef.contains("Restriction")){
				    String arrays[]= allRef.split(",");
				    String hasValues[] = arrays[0].split("=");
				    String onProperty[] = arrays[1].split("=");	
				    sense.setHasValue(hasValues[1]);
				    onProperty[1] =onProperty[1].substring(0, onProperty[1].length()-1);
				    sense.setOnProperty( onProperty[1]);
				}

				
			}
		}
		
	}
}
