package com.acmutv.ontoqa.core.lexicon;

import java.net.URI;

import com.acmutv.ontoqa.core.lemon.URIValue;
import com.acmutv.ontoqa.core.lemon.model.Property;

class PropertyImpl extends URIValue implements Property {

    public PropertyImpl(URI uri) {
        super(uri);
    }
    
}

