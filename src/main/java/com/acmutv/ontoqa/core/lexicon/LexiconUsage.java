package com.acmutv.ontoqa.core.lexicon;

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
		
		//Example
		String resource="organization.rdf";
		Path path= FileSystems.getDefault().getPath(resource).toAbsolutePath();
		final LemonSerializer serializer = LemonSerializer.newInstance();

		 try (InputStream in = Files.newInputStream(path)) {
			 Reader source = Files.newBufferedReader(path);
			 
			 LemonModelImpl lemModel= new LemonModelImpl(null);
			 RDFXMLReader rr= new RDFXMLReader(lemModel, true);
			 rr.parse(in);
			 
			 LexicalEntry lex= serializer.readEntry(source);
			 
			 System.out.println(lex.getCanonicalForm());
			 System.out.println(lex.getSenses());
			 
			 
			 LemonModel Model= rr.getModel();
			 URI uri2=Model.getContext();
			 LexiconImpl lexicon = new LexiconImpl(uri2, lemModel);
			 System.out.println(lexicon.getElements());
			 
			 
		}catch(Exception e){
			System.out.println(e);
		}
		
	 
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
