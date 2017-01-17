/**********************************************************************************
 * Copyright (c) 2011, Monnet Project
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Monnet Project nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE MONNET PROJECT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *********************************************************************************/
package com.acmutv.ontoqa.core.lemon.impl;

import com.acmutv.ontoqa.core.lemon.ElementVisitor;
import com.acmutv.ontoqa.core.lemon.LemonModel;
import com.acmutv.ontoqa.core.lemon.LinguisticOntology;
import com.acmutv.ontoqa.core.lemon.impl.io.ListAccepter;
import com.acmutv.ontoqa.core.lemon.impl.io.ReaderAccepter;
import com.acmutv.ontoqa.core.lemon.model.Component;
import com.acmutv.ontoqa.core.lemon.model.Frame;
import com.acmutv.ontoqa.core.lemon.model.LemonElement;
import com.acmutv.ontoqa.core.lemon.model.LemonPredicate;
import com.acmutv.ontoqa.core.lemon.model.LexicalEntry;
import com.acmutv.ontoqa.core.lemon.model.LexicalForm;
import com.acmutv.ontoqa.core.lemon.model.LexicalSense;
import com.acmutv.ontoqa.core.lemon.model.LexicalTopic;
import com.acmutv.ontoqa.core.lemon.model.LexicalVariant;
import com.acmutv.ontoqa.core.lemon.model.MorphPattern;
import com.acmutv.ontoqa.core.lemon.model.Node;
import com.acmutv.ontoqa.core.lemon.model.Property;
import com.acmutv.ontoqa.core.lemon.model.PropertyValue;
import com.acmutv.ontoqa.core.lemon.model.SyntacticRoleMarker;
import java.net.URI;
import java.util.*;

/**
 * Instantiated via {@link LemonFactoryImpl}
 * @author John McCrae
 */
public class LexicalEntryImpl extends LemonElementImpl<LexicalEntryImpl> implements LexicalEntry {
    private static final String ABSTRACT_FORM = "abstractForm";
    private static final String CANONICAL_FORM = "canonicalForm";
    private static final String DECOMPOSITION = "decomposition";
    private static final String FORM = "form";
    private static final String OTHER_FORM = "otherForm";
    private static final String PHRASE_ROOT = "phraseRoot";
    private static final String SENSE = "sense";
    private static final String TOPIC = "topic";
    private static final String SYN_BEHAVIOR = "synBehavior";
    private static final long serialVersionUID = -4744607952919065833L;

    private final HashSet<List<Component>> components = new HashSet<List<Component>>();

    LexicalEntryImpl(URI uri, LemonModelImpl model) {
        super(uri, "LexicalEntry",model);
    }

    LexicalEntryImpl(String id, LemonModelImpl model) {
        super(id, "LexicalEntry",model);
    }

    LexicalEntryImpl(URI uri, String type, LemonModelImpl model) {
        super(uri, type,model);
    }

    LexicalEntryImpl(String id, String type, LemonModelImpl model) {
        super(id, type,model);
    }

    @Override
    public LexicalForm getCanonicalForm() {
        return (LexicalForm) getStrElem(CANONICAL_FORM);
    }

    @Override
    public void setCanonicalForm(final LexicalForm canonicalForm) {
        setStrElem(CANONICAL_FORM, canonicalForm);
    }
    
    public SyntacticRoleMarker getMarker() {
        return (SyntacticRoleMarker) getStrElem("marker");
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<LexicalForm> getOtherForms() {
        return (Collection) getStrElems(OTHER_FORM);
    }

    @Override
    public boolean addOtherForm(final LexicalForm otherForm) {
        return addStrElem(OTHER_FORM, otherForm);
    }

    @Override
    public boolean removeOtherForm(final LexicalForm otherForm) {
        return removeStrElem(OTHER_FORM, otherForm);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<LexicalForm> getAbstractForms() {
        return (Collection) getStrElems(ABSTRACT_FORM);
    }

    @Override
    public boolean addAbstractForm(final LexicalForm abstractForm) {
        return addStrElem(ABSTRACT_FORM, abstractForm);
    }

    @Override
    public boolean removeAbstractForm(final LexicalForm abstractForm) {
        return removeStrElem(ABSTRACT_FORM, abstractForm);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<LexicalTopic> getTopics() {
        return (Collection) getStrElems(TOPIC);
    }

    @Override
    public boolean addTopic(final LexicalTopic topic) {
        return addStrElem(TOPIC, topic);
    }

    @Override
    public boolean removeTopic(final LexicalTopic topic) {
        return removeStrElem(TOPIC, topic);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<LexicalVariant, Collection<LexicalEntry>> getLexicalVariants() {
        return (Map)getPredElems(LexicalVariant.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<LexicalEntry> getLexicalVariant(final LexicalVariant lexicalVariant) {
        return (Collection) getPredElem(lexicalVariant);
    }

    @Override
    public boolean addLexicalVariant(final LexicalVariant lexicalVariant, final LexicalEntry lexicalVariantVal) {
        return addPredElem(lexicalVariant, lexicalVariantVal);
    }

    @Override
    public boolean removeLexicalVariant(final LexicalVariant lexicalVariant, final LexicalEntry lexicalVariantVal) {
        return removePredElem(lexicalVariant, lexicalVariantVal);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Frame> getSynBehaviors() {
        return (Collection) getStrElems(SYN_BEHAVIOR);
    }

    @Override
    public boolean addSynBehavior(final Frame synBehavior) {
        return addStrElem(SYN_BEHAVIOR, synBehavior);
    }

    @Override
    public boolean removeSynBehavior(final Frame synBehavior) {
        return removeStrElem(SYN_BEHAVIOR, synBehavior);
    }

    boolean resolveRemoteList = model.allowRemote();
    
    @Override
    public Collection<List<Component>> getDecompositions() {
        if(resolveRemoteList) {
            if(checkRemote) {
                resolveRemote();
            }
            final ArrayList<List<Component>> compCopy = new ArrayList<List<Component>>(components);
            for(List<Component> comps : compCopy) {
                if(comps instanceof ListAccepter) {
                    final List<Component> list = model.resolver().resolveRemoteList(((ListAccepter)comps).head(),Component.class,model);
                    if(list != null) {
                        components.remove(comps);
                        components.add(list);
                    } 
                } 
            }
            resolveRemoteList = false;
        }
        return Collections.unmodifiableSet(components);
    }

    @Override
    public void addDecomposition(List<Component> comps) {
        checkRemote = false;
        if(model.allowUpdate()) {
            List<Object> compIds = new ArrayList<Object>(comps.size());
            for(Component comp : comps) {
                compIds.add(comp.getURI() == null ? comp.getID() : comp.getURI());
            }
            if(getURI() != null) {
                model.updater().addList(getURI(), URI.create(LemonModel.NEW_LEMON_URI+DECOMPOSITION), compIds);
            } else {
                model.updater().addList(getID(), URI.create(LemonModel.NEW_LEMON_URI+DECOMPOSITION), compIds);
            }
        }
        for (Component comp : comps) {
            if (comp instanceof LemonElementImpl) {
                ((LemonElementImpl<?>) comp).addReference(this);
            }
        }
        components.add(comps);
    }

    @Override
    public boolean removeDecomposition(List<Component> comps) {
        checkRemote = false;
        if(model.allowUpdate()) {
            List<Object> compIds = new ArrayList<Object>(comps.size());
            for(Component comp : comps) {
                compIds.add(comp.getURI() == null ? comp.getID() : comp.getURI());
            }
            if(getURI() != null) {
                model.updater().removeList(getURI(), URI.create(LemonModel.NEW_LEMON_URI+DECOMPOSITION), compIds);
            } else {
                model.updater().removeList(getID(), URI.create(LemonModel.NEW_LEMON_URI+DECOMPOSITION), compIds);
            }
        }
        for (Component comp : comps) {
            if (comp instanceof LemonElementImpl) {
                ((LemonElementImpl<?>) comp).removeReference(this);
            }
        }
        return components.remove(comps);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<LexicalSense> getSenses() {
        return (Collection) getStrElems(SENSE);
    }

    @Override
    public boolean addSense(final LexicalSense sense) {
        sense.setIsSenseOf(this);
        return addStrElem(SENSE, sense);
    }

    @Override
    public boolean removeSense(final LexicalSense sense) {
        sense.setIsSenseOf(null);
        return removeStrElem(SENSE, sense);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Node> getPhraseRoots() {
        return (Collection) getStrElems(PHRASE_ROOT);
    }

    @Override
    public boolean addPhraseRoot(final Node phraseRoot) {
        return addStrElem(PHRASE_ROOT, phraseRoot);
    }

    @Override
    public boolean removePhraseRoot(final Node phraseRoot) {
        return addStrElem(PHRASE_ROOT, phraseRoot);
    }

    @Override
    public Collection<LexicalForm> getForms() {
        @SuppressWarnings("unchecked")
        LinkedList<LexicalForm> forms = new LinkedList<LexicalForm>((Collection)getStrElems(FORM));
        if(getCanonicalForm() != null) {
            forms.add(getCanonicalForm());
        }
        forms.addAll(getOtherForms());
        forms.addAll(getAbstractForms());
        return Collections.unmodifiableCollection(forms);
    }

    @Override
    public boolean addForm(final LexicalForm form) {
        return addStrElem(FORM, form);
    }

    @Override
    public boolean removeForm(final LexicalForm form) {
        return removeStrElem(FORM, form);
    }

    @Override
    protected void updateReference(LemonElement from, LemonElement to) {
        super.updateReference(from, to);
        for (List<Component> comps : components) {
            for (int i = 0; i < comps.size(); i++) {
                if (comps.get(i).equals(from)) {
                    comps.set(i, (Component) to);
                }
            }
        }
    }

    @Override
    protected void mergeIn(LexicalEntryImpl elem) {
        super.mergeIn(elem);
        getDecompositions();
        components.addAll(elem.getDecompositions());
    }

    @Override
    protected boolean refers() {
        return super.refers() || !getDecompositions().isEmpty();
    }

    @Override
    protected void printAsBlankNode(java.io.PrintWriter stream, SerializationState state, boolean first) {
        boolean first2 = true;
        for (List<Component> componentList : getDecompositions()) {
            if (!first) {
                stream.println(" ;");
                stream.print(" lemon:decomposition (");
            } else if (first2) {
                stream.println(" lemon:decomposition (");
            } else {
                stream.println(", (");
            }
            for (Component c : componentList) {
                ((ComponentImpl) c).visit(stream, state);
                state.postponed.add(c);
            }
            stream.print(") ");
        }
    }

    @Override
    protected boolean follow(LemonPredicate predicate) {
        return !(predicate instanceof LexicalVariant);
    }

    @Override
    public void doAccept(ElementVisitor visitor) {
        for (List<Component> compList : components) {
            for (Component comp : compList) {
                if (comp instanceof LemonElementImpl) {
                    ((LemonElementImpl) comp).accept(visitor);
                }
            }
        }
    }

    @Override
    public Map<URI, Collection<Object>> getElements() {
        final Map<URI, Collection<Object>> elements = super.getElements();
        final URI decomposition = URI.create(LemonModel.NEW_LEMON_URI+DECOMPOSITION);
        final Collection<List<Component>> decomps = getDecompositions();
        if(!decomps.isEmpty()) {
            elements.put(decomposition,new LinkedList<Object>());
        }
        for(List<Component> compList : decomps) {
            elements.get(decomposition).add(compList);
        }
        return elements;
    }

    
    
    @Override
    public void clearAll() {
        for (List<Component> compList : components) {
            for (Component comp : compList) {
                if (comp instanceof LemonElementImpl) {
                    ((LemonElementImpl) comp).referencers.remove(this);
                }
            }
        }
        components.clear();
        super.clearAll();
    }

    private boolean isPredLemon(URI pred, String name) {
        return pred.toString().equals(LemonModel.NEW_LEMON_URI + name) ||
            pred.toString().equals(LemonModel.MONNET_LEMON_URI + name);
    }


    @Override
    public ReaderAccepter accept(URI pred, URI value, LinguisticOntology lingOnto, AccepterFactory factory) {
        if (isPredLemon(pred, ABSTRACT_FORM)) {
            final FormImpl formImpl = factory.getFormImpl(value);
            addStrElemDirect(ABSTRACT_FORM,formImpl);
            return formImpl;
        } else if (isPredLemon(pred, CANONICAL_FORM)) {
            final FormImpl formImpl = factory.getFormImpl(value);
            setStrElemDirect(CANONICAL_FORM,formImpl);
            return formImpl;
        } else if (isPredLemon(pred, OTHER_FORM)) {
            final FormImpl formImpl = factory.getFormImpl(value);
            addStrElemDirect(OTHER_FORM,formImpl);
            return formImpl;
        } else if (isPredLemon(pred, FORM)) {
            final FormImpl formImpl = factory.getFormImpl(value);
            addStrElemDirect(FORM,formImpl);
            return formImpl;
        } else if (isPredLemon(pred, DECOMPOSITION)) {
            final ListAccepter listAccepter = new ListAccepter(value);
            components.add(listAccepter);
            return listAccepter;
        } else if (isPredLemon(pred, PHRASE_ROOT)) {
            final NodeImpl nodeImpl = factory.getNodeImpl(value);
            addStrElemDirect(PHRASE_ROOT,nodeImpl);
            return nodeImpl;
        } else if (isPredLemon(pred, SENSE)) {
            final LexicalSenseImpl lexicalSenseImpl = factory.getLexicalSenseImpl(value);
            addStrElemDirect(SENSE,lexicalSenseImpl);
            lexicalSenseImpl.setEntry(this);
            return lexicalSenseImpl;
        } else if (isPredLemon(pred, SYN_BEHAVIOR)) {
            final FrameImpl frameImpl = factory.getFrameImpl(value);
            addStrElemDirect(SYN_BEHAVIOR, frameImpl);
            return frameImpl;
        } else if (isPredLemon(pred, TOPIC)) {
            final TopicImpl topicImpl = factory.getTopicImpl(value);
            addStrElemDirect(TOPIC, topicImpl);
            return topicImpl;
        }
        if(lingOnto != null) {
            for(LexicalVariant lexicalVariant : lingOnto.getLexicalVariant()) {
                if(lexicalVariant.getURI().equals(pred)) {
                    final LexicalEntryImpl lexicalEntryImpl = factory.getLexicalEntryImpl(value);
                    addPredElemDirect(lexicalVariant, lexicalEntryImpl);
                    return lexicalEntryImpl;
                }
            }
            for(Property property : lingOnto.getProperties()) {
                if(property.getURI().equals(pred)) {
                    final PropertyValue propertyValue = lingOnto.getPropertyValue(value.getFragment());
                    if(propertyValue != null) {
                        addPredElemDirect(property, propertyValue);
                        return null;
                    }
                }
            }
        }
        return defaultAccept(pred, value,lingOnto);
    }

    @Override
    public ReaderAccepter accept(URI pred, String value, LinguisticOntology lingOnto, AccepterFactory factory) {
        if (isPredLemon(pred, ABSTRACT_FORM)) {
            final FormImpl formImpl = factory.getFormImpl(value);
            addStrElemDirect(ABSTRACT_FORM,formImpl);
            return formImpl;
        } else if (isPredLemon(pred, CANONICAL_FORM)) {
            final FormImpl formImpl = factory.getFormImpl(value);
            setStrElemDirect(CANONICAL_FORM,formImpl);
            return formImpl;
        } else if (isPredLemon(pred, OTHER_FORM)) {
            final FormImpl formImpl = factory.getFormImpl(value);
            addStrElemDirect(OTHER_FORM,formImpl);
            return formImpl;
        } else if (isPredLemon(pred, FORM)) {
            final FormImpl formImpl = factory.getFormImpl(value);
            addStrElemDirect(FORM,formImpl);
            return formImpl;
        } else if (isPredLemon(pred, DECOMPOSITION)) {
            final ListAccepter listAccepter = new ListAccepter(value);
            components.add(listAccepter);
            return listAccepter;
        } else if (isPredLemon(pred, PHRASE_ROOT)) {
            final NodeImpl nodeImpl = factory.getNodeImpl(value);
            addStrElemDirect(PHRASE_ROOT,nodeImpl);
            return nodeImpl;
        } else if (isPredLemon(pred, SENSE)) {
            final LexicalSenseImpl lexicalSenseImpl = factory.getLexicalSenseImpl(value);
            addStrElemDirect(SENSE,lexicalSenseImpl);
            lexicalSenseImpl.setEntry(this);
            return lexicalSenseImpl;
        } else if (isPredLemon(pred, SYN_BEHAVIOR)) {
            final FrameImpl frameImpl = factory.getFrameImpl(value);
            addStrElemDirect(SYN_BEHAVIOR, frameImpl);
            return frameImpl;
        } else if (isPredLemon(pred, TOPIC)) {
            final TopicImpl topicImpl = factory.getTopicImpl(value);
            addStrElemDirect(TOPIC, topicImpl);
            return topicImpl;
        }
        if(lingOnto != null) {
            for(LexicalVariant lexicalVariant : lingOnto.getLexicalVariant()) {
                if(lexicalVariant.getURI().equals(pred)) {
                    final LexicalEntryImpl lexicalEntryImpl = factory.getLexicalEntryImpl(value);
                    //addLexicalVariant(lexicalVariant, lexicalEntryImpl);
                    addPredElemDirect(lexicalVariant, lexicalEntryImpl);
                    return lexicalEntryImpl;
                }
            }
        }
        return defaultAccept(pred, value);
    }

    @Override
    public void accept(URI pred, String value, String lang, LinguisticOntology lingOnto, AccepterFactory factory) {
        defaultAccept(pred, value, lang);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<MorphPattern> getPatterns() {
        return (Collection)getStrElems("pattern");
    }

    @Override
    public boolean addPattern(MorphPattern mp) {
        return addStrElem("pattern", mp);
    }

    @Override
    public boolean removePattern(MorphPattern mp) {
        return removeStrElem("pattern", mp);
    }

    @Override
    public Component getHead() {
        return (Component)getStrElem("head");
    }

    @Override
    public void setHead(Component cmpnt) {
        setStrElem("head", cmpnt);
    }
    
    @Override
    public void merge(ReaderAccepter accepter, LinguisticOntology lingOnto, AccepterFactory factory) {
        if(accepter instanceof LexicalEntryImpl) {
            this.components.addAll(((LexicalEntryImpl)accepter).components);
        }
    
        defaultMerge(accepter, lingOnto, factory);
    }

    @Override
    protected void resolveRemote() {
        checkRemote = false;
        model.resolver().resolveRemote(model, this,3);
    }
}
