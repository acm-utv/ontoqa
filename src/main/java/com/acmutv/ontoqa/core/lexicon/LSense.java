package com.acmutv.ontoqa.core.lexicon;

import java.util.List;

public class LSense {

	private String sense;
	private List<String> isA;
	private String reference;
	private List<String> marker;
	private String subjOfProp;
	private String objOfProp;
	
	
	
	public LSense() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getSense() {
		return sense;
	}
	public void setSense(String sense) {
		this.sense = sense;
	}
	public List<String> getIsA() {
		return isA;
	}
	public void setIsA(List<String> isA) {
		this.isA = isA;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public List<String> getMarker() {
		return marker;
	}
	public void setMarker(List<String> marker) {
		this.marker = marker;
	}

	public String getSubjOfProp() {
		return subjOfProp;
	}

	public void setSubjOfProp(String subjOfProp) {
		this.subjOfProp = subjOfProp;
	}

	public String getObjOfProp() {
		return objOfProp;
	}

	public void setObjOfProp(String objOfProp) {
		this.objOfProp = objOfProp;
	}


	
	
	
}
