package com.acmutv.ontoqa.core.lexicon;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.Rio;

import com.acmutv.ontoqa.core.lemon.*;
import com.acmutv.ontoqa.core.lemon.LemonFactory;
import com.acmutv.ontoqa.core.lemon.LemonModel;
import com.acmutv.ontoqa.core.lemon.LemonModels;
import com.acmutv.ontoqa.core.lemon.LemonSerializer;
import com.acmutv.ontoqa.core.lemon.LinguisticOntology;
import com.acmutv.ontoqa.core.lemon.impl.AccepterFactory;
import com.acmutv.ontoqa.core.lemon.impl.LexiconImpl;
import com.acmutv.ontoqa.core.lemon.impl.io.ReaderAccepter;
import com.acmutv.ontoqa.core.lemon.lexinfo.LexInfo;
import com.acmutv.ontoqa.core.lemon.model.LexicalEntry;
import com.acmutv.ontoqa.core.lemon.model.LexicalForm;
import com.acmutv.ontoqa.core.lemon.model.Text;
import com.acmutv.ontoqa.core.lemon.model.Lexicon;

public class LexiconUsage {
	
	

	 /**
	 * Return all elements in the lemon model matching some certain SPARQL query
	 * @param target The class of the target
	 * @param sparqlQuery The query in sparql, must be a select query returning a single variable
	 * @return A Collection of elements matching the given query
	 */
	
	//	public <Elem extends LemonElementOrPredicate> Iterator<Elem> query(Class<Elem> target, String sparqlQuery) {
	//		private final RemoteResolver resolver;
	//       if (resolver != null && resolver instanceof SPARQLResolver) {
	//           try {
	//               return ((SPARQLResolver) resolver).query(target, sparqlQuery, this);   
	//           } catch(IOException x) {
	//               throw new RuntimeException(x);
	//           } catch(ParserConfigurationException x) {
	//               throw new RuntimeException(x);
	//           } catch(SAXException x) {
	//               throw new RuntimeException(x);
	//           }
	//       } else {
	//           throw new RuntimeException("No SPARQL support in this model " + (resolver == null ? "null" : resolver.getClass().toString()));
	//       }
	//   }
	
	
	public static void main(String[] args) {
		String resource="organization.rdf";
		Path path= FileSystems.getDefault().getPath(resource).toAbsolutePath();
		final LemonSerializer serializer = LemonSerializer.newInstance();

		 try (Reader reader = Files.newBufferedReader(path)) {
			 final LemonModel model = serializer.read(reader);
			 //serializer.read(model, reader);
			 final Collection<Lexicon> collectionLexicon = model.getLexica();
			 System.out.println(model);
		}catch(Exception e){
			System.out.println(e);
		}
		 
		 
		//	public static void main(String[] args) {
		//	System.out.println("Main");
		//	final LemonSerializer serializer = LemonSerializer.newInstance();
		//	final LemonModel model = serializer.create();
		//	final Lexicon lexicon = model.addLexicon(
		//	        URI.create("http://www.example.com/mylexicon"),
		//	        "en" /*English*/);
		//	final LexicalEntry entry = LemonModels.addEntryToLexicon(
		//	        lexicon,
		//	        URI.create("http://www.example.com/mylexicon/cat"),
		//	        "cat",
		//	        URI.create("http://dbpedia.org/resource/Cat"));
		//
		//	final LemonFactory factory = model.getFactory();
		//	final LexicalForm pluralForm = factory.makeForm(entry.getURI());
		//	pluralForm.setWrittenRep(new Text("cats", "en"));
		//	final LinguisticOntology lingOnto = new LexInfo();
		//	pluralForm.addProperty(
		//	        lingOnto.getProperty("number"),
		//	        lingOnto.getPropertyValue("plural"));
		//	entry.addOtherForm(pluralForm);
		//
		//	serializer.writeEntry(model, entry, lingOnto, 
		//	        new OutputStreamWriter(System.out));
		//	
		//	 }

}
