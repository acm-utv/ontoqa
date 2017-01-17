package com.acmutv.ontoqa.core.lexicon;
import java.io.*;
import java.net.URI;
import java.util.*;
import com.acmutv.ontoqa.core.exception.IORuntimeException;
import com.acmutv.ontoqa.core.lemon.LemonModel;
import com.acmutv.ontoqa.core.lemon.model.LexicalEntry;
import com.acmutv.ontoqa.core.lemon.model.LexicalTopic;
import com.acmutv.ontoqa.core.lemon.model.Lexicon;
import com.acmutv.ontoqa.core.lemon.model.MorphPattern;
import com.acmutv.ontoqa.core.lemon.model.Property;
import com.acmutv.ontoqa.core.lemon.model.PropertyValue;

public class SecondSimpleLexicon  implements Iterable<String>, Lexicon{
	
	
		private Set<String> words;
		private Set<String> knownPrefixes;
		
		public SecondSimpleLexicon() {
			words = new HashSet<>();
			knownPrefixes = new HashSet<>();
			knownPrefixes.add("");
			for (char c = 'A'; c <= 'Z'; c++) {
				knownPrefixes.add(String.valueOf(c));
			}
		}
		
		public SecondSimpleLexicon(String filename) {
			addWordsFromFile(filename);
		}
		
		public void add(String word) {
			word = word.toUpperCase();
			words.add(word);
			for (int i = 2; i <= word.length(); i++) {
				knownPrefixes.add(word.substring(0, i));
			}
		}
		
		public void addWordsFrom(InputStream input) {
			Scanner scan = new Scanner(input);
			addWordsFrom(scan);
		}
		
		public void addWordsFrom(Reader reader) {
			Scanner scan = new Scanner(reader);
			addWordsFrom(scan);
		}
		
		public void addWordsFrom(Scanner input) {
			while (input.hasNextLine()) {
				String line = input.nextLine().trim();
				if (!line.isEmpty()) {
					add(line);
				}
			}
		}
		
		public void addWordsFromFile(File file) {
			try {
				Scanner input = new Scanner(file);
				addWordsFrom(input);
			} catch (FileNotFoundException fnfe) {
				throw new IORuntimeException(fnfe);
			}
		}
		
		public void addWordsFromFile(String filename) {
			addWordsFromFile(new File(filename));
		}
		
		public boolean contains(String word) {
			return words.contains(word.toUpperCase());
		}
		
		public boolean containsPrefix(String prefix) {
			return knownPrefixes.contains(prefix.toUpperCase());
		}
		
		public boolean equals(Object o) {
			if (o instanceof SecondSimpleLexicon) {
				SecondSimpleLexicon lex = (SecondSimpleLexicon) o;
				return lex.words.equals(this.words);
			} else {
				return false;
			}
		}
		
		public int hashCode() {
			return words.hashCode();
		}

		public boolean isEmpty() {
			return size() == 0;
		}
		
		public Iterator<String> iterator() {
			return Collections.unmodifiableSet(words).iterator();
		}
		
		public int prefixCount() {
			return knownPrefixes.size();
		}
		
		public int size() {
			return words.size();
		}
		
		public String toString() {
			return words.toString();
		}

		@Override
		public Map<Property, Collection<PropertyValue>> getPropertys() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Collection<PropertyValue> getProperty(Property property) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean addProperty(Property property, PropertyValue propertyVal) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean removeProperty(Property property, PropertyValue propertyVal) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public String getID() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Collection<URI> getTypes() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void addType(URI uri) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removeType(URI uri) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Map<URI, Collection<Object>> getAnnotations() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Collection<Object> getAnnotations(URI annotation) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean addAnnotation(URI annotationProperty, Object annotation) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean removeAnnotation(URI annotationProperty, Object annotation) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public URI getURI() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getLanguage() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setLanguage(String language) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Collection<LexicalEntry> getEntrys() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean addEntry(LexicalEntry entry) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean removeEntry(LexicalEntry entry) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean hasEntry(LexicalEntry entry) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int countEntrys() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Collection<LexicalTopic> getTopics() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean addTopic(LexicalTopic topic) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean removeTopic(LexicalTopic topic) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public LemonModel getModel() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Collection<MorphPattern> getPatterns() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean addPattern(MorphPattern pattern) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean removePattern(MorphPattern pattern) {
			// TODO Auto-generated method stub
			return false;
		}
	}

