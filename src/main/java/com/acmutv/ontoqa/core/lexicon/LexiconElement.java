package com.acmutv.ontoqa.core.lexicon;

public class LexiconElement {
	
	private String name;
	private String type;
	


	public LexiconElement(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}
	
	public LexiconElement() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	

}
