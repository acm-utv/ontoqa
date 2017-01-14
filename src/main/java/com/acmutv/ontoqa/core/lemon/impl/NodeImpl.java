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

import com.acmutv.ontoqa.core.lemon.LemonModel;
import com.acmutv.ontoqa.core.lemon.LinguisticOntology;
import com.acmutv.ontoqa.core.lemon.impl.io.ReaderAccepter;
import com.acmutv.ontoqa.core.lemon.impl.io.UnactualizedAccepter;
import com.acmutv.ontoqa.core.lemon.model.Constituent;
import com.acmutv.ontoqa.core.lemon.model.Edge;
import com.acmutv.ontoqa.core.lemon.model.Node;
import com.acmutv.ontoqa.core.lemon.model.PhraseTerminal;
import com.acmutv.ontoqa.core.lemon.model.Text;
import java.net.URI;
import java.util.*;

/**
 * Instantiated via {@link LemonFactoryImpl}
 * @author John McCrae
 */
public class NodeImpl extends LemonElementImpl implements Node {
    private static final long serialVersionUID = 6829501075252056350L;

    private Text separator;

    NodeImpl(URI uri, LemonModelImpl model) {
        super(uri, "Node",model);
    }

    NodeImpl(String id, LemonModelImpl model) {
        super(id, "Node",model);
    }

    @Override
    public Constituent getConstituent() {
        return (Constituent) getStrElem("constituent");
    }

    @Override
    public void setConstituent(final Constituent constituent) {
        setStrElem("constituent", constituent);
    }

    @Override
    public Map<Edge, Collection<Node>> getEdges() {
        return (Map<Edge, Collection<Node>>) getPredElems(Edge.class);
    }

    @Override
    public Collection<Node> getEdge(final Edge edge) {
        return (Collection<Node>) getPredElem(edge);
    }

    @Override
    public boolean addEdge(final Edge edge, final Node edgeVal) {
        return addPredElem(edge, edgeVal);
    }

    @Override
    public boolean removeEdge(final Edge edge, final Node edgeVal) {
        return removePredElem(edge, edgeVal);
    }

    @Override
    public PhraseTerminal getLeaf() {
        return (PhraseTerminal) getStrElem("leaf");
    }

    @Override
    public void setLeaf(final PhraseTerminal product) {
        setStrElem("leaf", product);
    }

    @Override
    public Text getSeparator() {
        if(checkRemote) {
            resolveRemote();
        }
        return separator;
    }

    @Override
    public void setSeparator(final Text separator) {
        checkRemote = false;
        if(model.allowUpdate()) {
            if(this.separator != null) {
                if(getURI() != null) {
                    model.updater().remove(getURI(), URI.create(LemonModel.NEW_LEMON_URI+"language"), this.separator.value, this.separator.language);
                } else {
                    model.updater().remove(getID(), URI.create(LemonModel.NEW_LEMON_URI+"language"), this.separator.value, this.separator.language);
                }
            }
            if(separator != null) {
                if(getURI() != null) {
                    model.updater().add(getURI(), URI.create(LemonModel.NEW_LEMON_URI+"language"), separator.value, separator.language);
                } else {
                    model.updater().add(getID(), URI.create(LemonModel.NEW_LEMON_URI+"language"), separator.value, separator.language);
                }
            }
        }
        this.separator = separator;
    }

    private boolean isPredLemon(URI pred, String name) {
        return pred.toString().equals(LemonModel.NEW_LEMON_URI + name) ||
            pred.toString().equals(LemonModel.MONNET_LEMON_URI + name);
    }


    @Override
    public ReaderAccepter accept(URI pred, URI value, LinguisticOntology lingOnto, AccepterFactory factory) {
        if(isPredLemon(pred, "constituent")) {
            final ConstituentImpl constituentImpl = factory.getConstituentImpl(value);
            setStrElemDirect("constituent",constituentImpl);
            return constituentImpl;
        } else if(isPredLemon(pred, "leaf")) {
            return new UnactualizedAccepter() {

                @Override
                public Map<Object, ReaderAccepter> actualizedAs(ReaderAccepter actual, LinguisticOntology lingOnto, AccepterFactory factory) {
                    if(actual instanceof PhraseTerminal) {
                        setStrElemDirect("leaf",(PhraseTerminal)actual);
                    }
                    return super.actualizedAs(actual, lingOnto, factory);
                }
                
            };
        } else if(lingOnto != null) {
            for(Edge edge : lingOnto.getEdge()) {
                if(edge.getURI().equals(pred)) {
                    final NodeImpl nodeImpl = factory.getNodeImpl(value);
                    addPredElemDirect(edge, nodeImpl);
                    return nodeImpl;
                }
            }
        }
        return defaultAccept(pred, value, lingOnto);
    }

    @Override
    public ReaderAccepter accept(URI pred, String value, LinguisticOntology lingOnto, AccepterFactory factory) {
        if(isPredLemon(pred, "constituent")) {
            final ConstituentImpl constituentImpl = factory.getConstituentImpl(value);
            setStrElemDirect("constituent",constituentImpl);
            return constituentImpl;
        } else if(isPredLemon(pred, "leaf")) {
            return new UnactualizedAccepter() {

                @Override
                public Map<Object, ReaderAccepter> actualizedAs(ReaderAccepter actual, LinguisticOntology lingOnto, AccepterFactory factory) {
                    if(actual instanceof PhraseTerminal) {
                        setStrElemDirect("leaf",(PhraseTerminal)actual);
                    }  else {
                        System.err.println("leaf object was not a node but " + actual.getClass());
                    }
                    return super.actualizedAs(actual, lingOnto, factory);
                }
                
            };
        } else if(lingOnto != null) {
            for(Edge edge : lingOnto.getEdge()) {
                if(edge.getURI().equals(pred)) {
                    final NodeImpl nodeImpl = factory.getNodeImpl(value);
                    addPredElemDirect(edge, nodeImpl);
                    return nodeImpl;
                }
            }
        }
        return defaultAccept(pred, value);
    }

    @Override
    public void accept(URI pred, String value, String lang, LinguisticOntology lingOnto, AccepterFactory factory) {
        if(isPredLemon(pred, "separator")) {
            separator = new Text(value,lang);
        } else {
            defaultAccept(pred, value, lang);
        }
    }
    
    @Override
    public void merge(ReaderAccepter accepter, LinguisticOntology lingOnto, AccepterFactory factory) {
        if(accepter instanceof NodeImpl) {
            final NodeImpl ni = (NodeImpl)accepter;
            if(separator == null && ni.separator != null) {
                this.separator = ni.separator;
            } else if(separator != null && ni.separator != null && !separator.equals(ni.separator)) {
                throw new RuntimeException("Merge exception");
            }
        }
        defaultMerge(accepter, lingOnto, factory);
    }
}
