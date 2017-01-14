package com.acmutv.ontoqa.core.lexicon;

import java.io.OutputStreamWriter;
import java.net.URI;

import com.acmutv.ontoqa.core.lemon.LemonFactory;
import com.acmutv.ontoqa.core.lemon.LemonModel;
import com.acmutv.ontoqa.core.lemon.LemonModels;
import com.acmutv.ontoqa.core.lemon.LemonSerializer;
import com.acmutv.ontoqa.core.lemon.LinguisticOntology;
import com.acmutv.ontoqa.core.lemon.lexinfo.LexInfo;
import com.acmutv.ontoqa.core.lemon.model.LexicalEntry;
import com.acmutv.ontoqa.core.lemon.model.LexicalForm;
import com.acmutv.ontoqa.core.lemon.model.Text;
import com.acmutv.ontoqa.core.lemon.model.Lexicon;

public class LexiconUsage {
	
	
	public static void main(String[] args) {
	System.out.println("Main");
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
