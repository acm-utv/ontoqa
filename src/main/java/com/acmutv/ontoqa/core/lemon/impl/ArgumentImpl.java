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

import com.acmutv.ontoqa.core.lemon.init.LemonModel;
import com.acmutv.ontoqa.core.lemon.init.LinguisticOntology;
import com.acmutv.ontoqa.core.lemon.init.URIElement;
import  com.acmutv.ontoqa.core.lemon.impl.io.ReaderAccepter;
import  com.acmutv.ontoqa.core.lemon.impl.io.UnactualizedAccepter;
import com.acmutv.ontoqa.core.lemon.model.Argument;
import com.acmutv.ontoqa.core.lemon.model.SyntacticRoleMarker;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Instantiated via {@link LemonFactoryImpl}
 * @author John McCrae
 */
public class ArgumentImpl extends LemonElementImpl<ArgumentImpl> implements Argument {
    private static final long serialVersionUID = -7363823973988514256L;
    
    private boolean optional;
    
    ArgumentImpl(URI uri, LemonModelImpl model) {
        super(uri, "Argument",model);
    }

    ArgumentImpl(String id, LemonModelImpl model) {
        super(id, "Argument",model);
    }

    @Override
    public SyntacticRoleMarker getMarker() {
        return (SyntacticRoleMarker) getStrElem("marker");
    }

    @Override
    public void setMarker(final SyntacticRoleMarker marker) {
        setStrElem("marker", marker);
    }

    @Override
    public boolean isOptional() {
        return optional;
    }

    @Override
    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    
    private boolean isPredLemon(URI pred, String name) {
        return pred.toString().equals(LemonModel.NEW_LEMON_URI + name) ||
            pred.toString().equals(LemonModel.MONNET_LEMON_URI + name);
    }


    
    @Override
    public ReaderAccepter accept(URI pred, URI value, LinguisticOntology lingOnto, AccepterFactory factory) {
        if (isPredLemon(pred, "marker")) {
            setStrElemDirect("marker",new UntypedMarker(value));
            return new UnactualizedAccepter() {

                @Override
                public Map<Object, ReaderAccepter> actualizedAs(ReaderAccepter actual, LinguisticOntology lingOnto, AccepterFactory factory) {
                    setStrElemDirect("marker",(SyntacticRoleMarker)actual);
                    return super.actualizedAs(actual, lingOnto,factory);
                }
            };
        } else {
            return defaultAccept(pred, value,lingOnto);
        }
    }

    @Override
    public ReaderAccepter accept(URI pred, String bNode, LinguisticOntology lingOnto, AccepterFactory factory) {
        if (isPredLemon(pred, "marker")) {
            setStrElemDirect("marker",new UntypedMarker(bNode));
            return new UnactualizedAccepter() {

                @Override
                public Map<Object, ReaderAccepter> actualizedAs(ReaderAccepter actual, LinguisticOntology lingOnto, AccepterFactory factory) {
                    setStrElemDirect("marker",(SyntacticRoleMarker)actual);
                    return super.actualizedAs(actual, lingOnto,factory);
                }
            };
        } else {
            return defaultAccept(pred, bNode);
        }
    }

    @Override
    public void accept(URI pred, String value, String lang, LinguisticOntology lingOnto, AccepterFactory factory) {
        if(isPredLemon(pred, "optional")) {
            optional = Boolean.parseBoolean(value);
        }
        super.defaultAccept(pred, value, lang);
    }

    @Override
    public void merge(ReaderAccepter accepter, LinguisticOntology lingOnto, AccepterFactory factory) {
        defaultMerge(accepter, lingOnto, factory);
    }

    @Override
    public Map<URI, Collection<Object>> getElements() {
        final Map<URI, Collection<Object>> elements = super.getElements();
        if(optional) {
            elements.put(URI.create(LemonModel.NEW_LEMON_URI + "optional"), Collections.singletonList((Object)"true"));
        }
        return elements;
    }
    
    
    
    private static class UntypedMarker extends URIElement implements SyntacticRoleMarker {

        public UntypedMarker(String id) {
            super(id);
        }

        public UntypedMarker(URI uri) {
            super(uri);
        }
        
    }
}
