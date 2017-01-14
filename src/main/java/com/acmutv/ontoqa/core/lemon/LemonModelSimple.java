package com.acmutv.ontoqa.core.lemon;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import eu.monnetproject.lemon.AbstractVisitor;
import eu.monnetproject.lemon.LemonFactory;
import eu.monnetproject.lemon.LemonModel;
import eu.monnetproject.lemon.LinguisticOntology;
import eu.monnetproject.lemon.RemoteUpdater;
import eu.monnetproject.lemon.RemoteUpdaterFactory;
import eu.monnetproject.lemon.impl.CopyVisitor;
import com.acmutv.ontoqa.core.lemon.LemonElementModel;
import eu.monnetproject.lemon.impl.LemonFactoryImpl;
import eu.monnetproject.lemon.impl.LexiconImpl;
import eu.monnetproject.lemon.impl.LexiconResolver;
import eu.monnetproject.lemon.impl.RemoteResolver;
import eu.monnetproject.lemon.impl.SPARQLResolver;
import eu.monnetproject.lemon.model.LemonElement;
import eu.monnetproject.lemon.model.LemonElementOrPredicate;
import eu.monnetproject.lemon.model.Lexicon;
import eu.monnetproject.lemon.model.MorphPattern;

public class LemonModelSimple implements LemonModel {


    private HashSet<Lexicon> lexica = new HashSet<Lexicon>();
    private HashSet<MorphPattern> patterns = new HashSet<MorphPattern>();
    private HashMap<URI, LemonElementOrPredicate> elements = new HashMap<URI, LemonElementOrPredicate>();
    private String baseURI;
    private final RemoteResolver resolver;
    private final RemoteUpdater updater;

    public LemonModelSimple(RemoteUpdaterFactory updaterFactory) {
        this.resolver = null;
        this.updater = updaterFactory == null ? null : updaterFactory.updaterForModel(this);
    }

    public LemonModelSimple(String baseURI, RemoteUpdaterFactory updaterFactory) {
        this.baseURI = baseURI;
        this.resolver = null;
        this.updater = updaterFactory == null ? null : updaterFactory.updaterForModel(this);
    }

    public LemonModelSimple(String baseURI, RemoteResolver resolver, RemoteUpdaterFactory updaterFactory) {
        this.baseURI = baseURI;
        this.resolver = resolver;
        this.updater = updaterFactory == null ? null : updaterFactory.updaterForModel(this);
    }

//    public void activate(Map properties) {
//        if (properties != null && properties.containsKey("baseURI")) {
//            baseURI = properties.get("baseURI").toString();
//        }
//    }
    @Override
    public Collection<Lexicon> getLexica() {
        if (resolver != null && resolver instanceof LexiconResolver && lexica.isEmpty()) {
            final Set<URI> lexicaURIs = ((LexiconResolver) resolver).getLexica();
            for (URI lexicaUri : lexicaURIs) {
                lexica.add(new LexiconImpl(lexicaUri, this));
            }
        }
        return lexica;
    }

    public LemonElementOrPredicate getLemonElement(URI uri) {
        return elements.get(uri);
    }

    @Override
    public URI getContext() {
        if (baseURI != null) {
            return URI.create(baseURI);
        } else {
            return null;
        }
    }
    private LemonFactory factory = new LemonFactoryImpl(elements, this);

    @Override
    public LemonFactory getFactory() {
        return factory;
    }

    @Override
    public Lexicon addLexicon(URI uri, String language) {
        Lexicon lexicon = new LexiconImpl(uri, this);
        if(updater != null) {
            updater.add(uri, URI.create("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), URI.create(LemonModel.NEW_LEMON_URI+"Lexicon"));
        } 
        lexicon.setLanguage(language);
        lexica.add(lexicon);
        return lexicon;
    }

    @Override
    public void removeLexicon(Lexicon lexicon) {
        lexica.remove(lexicon);
    }

    void addLexicon(Lexicon lexicon) {
        lexica.add(lexicon);
    }

    @Override
    public void addPattern(MorphPattern pattern) {
        patterns.add(pattern);
    }

    @Override
    public Collection<MorphPattern> getPatterns() {
        return patterns;
    }

    @Override
    public <Elem extends LemonElementOrPredicate> Iterator<Elem> query(Class<Elem> target, String sparqlQuery) {
        if (resolver != null && resolver instanceof SPARQLResolver) {
            try {
                return ((SPARQLResolver) resolver).query(target, sparqlQuery, this);   
            } catch(IOException x) {
                throw new RuntimeException(x);
            } catch(ParserConfigurationException x) {
                throw new RuntimeException(x);
            } catch(SAXException x) {
                throw new RuntimeException(x);
            }
        } else {
            throw new RuntimeException("No SPARQL support in this model " + (resolver == null ? "null" : resolver.getClass().toString()));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Elem extends LemonElement> void merge(Elem from, Elem to) {
        if (to instanceof LemonElementModel) {
            ((LemonElementModel<Elem>) to).mergeIn(from);
        } else {
            throw new IllegalArgumentException("Cannot merge element I did not create");
        }
    }

    @Override
    public void purgeLexicon(Lexicon lxcn, LinguisticOntology lo) {
        final PurgeVisitor purgeVisitor = new PurgeVisitor(lo);
        if (lxcn instanceof LemonElementModel) {
            ((LemonElementModel) lxcn).accept(purgeVisitor);
            lexica.remove(lxcn);
        } else {
            throw new IllegalArgumentException("Lexicon not created by me");
        }
    }

    @Override
    public void importLexicon(Lexicon lxcn, LinguisticOntology lo) {
        final CopyVisitor copyVisitor = new CopyVisitor(lo, this);
        if (lxcn instanceof LexiconImpl) {
            ((LexiconImpl) lxcn).accept(copyVisitor);
        }
        lexica.add(lxcn);
    }

    public final boolean allowRemote() {
        return resolver != null;
    }

    public final boolean allowUpdate() {
        return updater != null;
    }

    public final RemoteResolver resolver() {
        return resolver;
    }

    public final RemoteUpdater updater() {
        return updater;
    }

    private static class PurgeVisitor extends AbstractVisitor {

        private HashSet<LemonElement> visited = new HashSet<LemonElement>();

        public PurgeVisitor(LinguisticOntology lingOnto) {
            super(lingOnto);
        }

        @Override
        public void visit(LemonElement _element) {
            if(!(_element instanceof LemonElementModel)) {
                throw new IllegalArgumentException();
            }
            final LemonElementModel<?> element = (LemonElementModel)_element;
            element.clearAll();
            visited.add(element);
        }

        @Override
        public boolean hasVisited(LemonElement element) {
            return visited.contains(element);
        }
    }
    
    public void register(URI uri, LemonElementOrPredicate lep) {
        this.elements.put(uri, lep);
    }
}
