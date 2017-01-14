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
package com.acmutv.ontoqa.core.lemon.init;

import com.acmutv.ontoqa.core.lemon.model.FormVariant;
import com.acmutv.ontoqa.core.lemon.model.LemonPredicate;
import com.acmutv.ontoqa.core.lemon.model.LexicalVariant;
import com.acmutv.ontoqa.core.lemon.model.SenseRelation;
import java.net.URI;
import java.util.HashSet;

/**
 *
 * @author John McCrae
 */
public abstract class AbstractVisitor implements ElementVisitor {
    private final HashSet<String> nofollows;

    public AbstractVisitor(LinguisticOntology lingOnto) {
        nofollows = getNoFollow(lingOnto);
    }
    
    private static HashSet<String> getNoFollow(LinguisticOntology lingOnto) {
        HashSet<String> noFollow = new HashSet<String>();
        noFollow.add(LemonModel.NEW_LEMON_URI + "lexicalVariant");
        noFollow.add(LemonModel.NEW_LEMON_URI + "formVariant");
        noFollow.add(LemonModel.NEW_LEMON_URI + "senseRelation");
        noFollow.add(LemonModel.NEW_LEMON_URI + "broader");
        noFollow.add(LemonModel.NEW_LEMON_URI + "narrower");
        noFollow.add(LemonModel.NEW_LEMON_URI + "equivalent");
        noFollow.add(LemonModel.NEW_LEMON_URI + "incompatible");
        noFollow.add(LemonModel.NEW_LEMON_URI + "element");
        noFollow.add(LemonModel.NEW_LEMON_URI + "marker");
        noFollow.add(LemonModel.NEW_LEMON_URI + "isSenseOf");
        noFollow.add(LemonModel.MONNET_LEMON_URI + "lexicalVariant");
        noFollow.add(LemonModel.MONNET_LEMON_URI + "formVariant");
        noFollow.add(LemonModel.MONNET_LEMON_URI + "senseRelation");
        noFollow.add(LemonModel.MONNET_LEMON_URI + "broader");
        noFollow.add(LemonModel.MONNET_LEMON_URI + "narrower");
        noFollow.add(LemonModel.MONNET_LEMON_URI + "equivalent");
        noFollow.add(LemonModel.MONNET_LEMON_URI + "incompatible");
        noFollow.add(LemonModel.MONNET_LEMON_URI + "element");
        noFollow.add(LemonModel.MONNET_LEMON_URI + "marker");
        noFollow.add(LemonModel.MONNET_LEMON_URI + "isSenseOf");
        for (FormVariant var : lingOnto.getFormVariant()) {
            noFollow.add(var.getURI().toString());
        }
        for (LexicalVariant var : lingOnto.getLexicalVariant()) {
            noFollow.add(var.getURI().toString());
        }
        for (SenseRelation var : lingOnto.getSenseRelation()) {
            noFollow.add(var.getURI().toString());
        }
        return noFollow;
    }
    
    @Override
    public boolean follow(URI uri) {
        return !nofollows.contains(uri.toString());
    }

    @Override
    public boolean follow(LemonPredicate uri) {
        return !(uri instanceof FormVariant) &&
                !(uri instanceof LexicalVariant) &&
                !(uri instanceof SenseRelation);
    }

    @Override
    public boolean visitFirst() {
        return false;
    }
    
    
}
