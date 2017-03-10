package com.acmutv.ontoqa.core.lexicon;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.acmutv.ontoqa.core.lemon.model.Frame;
import com.acmutv.ontoqa.core.lemon.model.LexicalForm;
import com.acmutv.ontoqa.core.lemon.model.LexicalSense;
import com.acmutv.ontoqa.core.lemon.model.Property;
import com.acmutv.ontoqa.core.lemon.model.PropertyValue;
import com.acmutv.ontoqa.core.lemon.model.Text;

import com.acmutv.ontoqa.core.lemon.model.LexicalSense.ReferencePreference;

/**
 * This class defines the lexicon element data structure.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class LexiconElement {
	
	private String name;
	private String type;
	private List<String> synBeh;
	private List<String> forms;
	private List<String> senses;
	private List<String> writtenRep;
	private List<String> referenceURI;
	private List<String> temp;
	private List<String> frame;


	public LexiconElement() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}
	
	@SuppressWarnings("unused")
	public void setName(String name) {
		
		  String[] allPart=name.split("/");
		  String[] interestPart= allPart[allPart.length-1].split("#");
		  String[] tupla= interestPart[1].split("__");	  
		  String type= tupla[1];
		  String nn = tupla[0];
		  String[] nameP = nn.split("\\+");
		  String nameTemp = nameP[0];
		  
		  for( int i = 1; i< nameP.length; i++){
			String p = nameP[i];
			  nameTemp += " "+ nameP[i];
		  }	  
		this.name = nameTemp;
	}
	
	public String getType() {
		return type;
	}
	
	@SuppressWarnings("rawtypes")
	public void setType(String type, Collection<URI> values) {
		
		  String lexinfo= " ";
		  if(!type.equals("[]")){
			  String[] allPart=type.split("/");
			  String[] interestPart= allPart[allPart.length-1].split("#");
			  lexinfo = interestPart[1];
			  int num= lexinfo.length() -2;
			  lexinfo = lexinfo.substring(0, num);
			  
		  }else{
			  Iterator it = values.iterator();
			  it.next();
			  if( it.hasNext()){
				  URI value = (URI) it.next();  
				  String val = value.toString();
				  String[] allPart=val.split("/");
				  String[] interestPart= allPart[allPart.length-1].split("#");
				  lexinfo = interestPart[1];	  
			  }
		  }
		this.type = lexinfo;
	}
	
	public void setTypeLexInfo(String type) {
		
		  String lexinfo= " ";
		  this.type = lexinfo;
	}
	
	
	public List<String>getSynBeh() {
		return synBeh;
	}

	@SuppressWarnings("rawtypes")
	public void setSynBeh(Collection<Frame> values) {
		  List<String> allFrame = new ArrayList<String>();
		  Iterator it = values.iterator();
		  List<String> frames= new ArrayList<String>();
		  while(it.hasNext()){
			  Frame oneFrame= (Frame) it.next();
			  frames.add(setFrame(oneFrame.getTypes()));
			  String lexForm = oneFrame.toString();
			  String[] allPart = lexForm.split("/");
			  String interestPart= allPart[allPart.length-1];
			  allFrame.add(interestPart);
		  }
		this.synBeh = allFrame;
		this.frame= frames;
	}

	public List<String> getForms() {
		return forms;
	}

	@SuppressWarnings("rawtypes")
	public void setForms(Collection<LexicalForm> values) {
		
		 List<String> allForms = new ArrayList<String>();
		 List<String> writtenRep = new ArrayList<String>();
		 List<String> tempV= new ArrayList<String>();
		 Iterator it = values.iterator();
		  while(it.hasNext()){
			//  String lexForm = it.next().toString();
			  
			  /*written Rep*/
			  Object object = it.next();
			  String lexForm = object.toString();
			  object = (LexicalForm) object;
			  String oneWrittenRep = this.setPrettyWrittenRep(((LexicalForm) object).getWrittenRep());
			  writtenRep.add(oneWrittenRep);
			  /*Temp*/
			  Map<Property, Collection<PropertyValue>> pp= ((LexicalForm) object).getPropertys();
			  tempV.addAll(setTemp(pp));
			    
			  /*forms*/
			  //String lexForm = object.toString();
			  String[] allPart = lexForm.split("/");
			  String interestPart= allPart[allPart.length-1];
			  allForms.add(interestPart);


		  }
		this.writtenRep = writtenRep;
		this.forms = allForms;
		this.temp = tempV;
	}
	

	public List<String> getSenses() {
		return senses;
	}

	@SuppressWarnings("rawtypes")
	public void setSenses(Collection<LexicalSense> values) {
		
		  List<String> allSense = new ArrayList<String>();
		  List<String> allReference = new ArrayList<String>();
		  
		  Iterator it = values.iterator();
		  while(it.hasNext()){
			  Object object = it.next();
			  String lexForm = object.toString();
			  LexicalSense lS = (LexicalSense) object;
			  object = (LexicalSense) object;
				  ((LexicalSense) object).getReference().toString();		
				  allReference.add( ((LexicalSense) object).getReference().toString());
			  String[] allPart = lexForm.split("/");
			  String interestPart= allPart[allPart.length-1];
			  allSense.add(interestPart);
		  }
		this.referenceURI = allReference;
		this.senses = allSense;
	}

	public List<String> getWrittenRep() {
		return writtenRep;
	}

	public void setWrittenRep(List<String> writtenRep) {
		this.writtenRep = writtenRep;
	}
	
	public String setPrettyWrittenRep(Text writtenRep){
		String writtenR= writtenRep.value;
		return writtenR;
	}

	public List<String> getReferenceURI() {
		return referenceURI;
	}

	public void setReferenceURI(List<String> referenceURI) {
		this.referenceURI = referenceURI;
	}
	
	public List<String> setTemp( Map<Property, Collection<PropertyValue>> pp){
		
		  Collection<Collection<PropertyValue>> pp2= pp.values();
		  Iterator<Collection<PropertyValue>> it = pp2.iterator();
		  List<String> temp = new ArrayList();
		  while(it.hasNext()){
			  String type =it.next().toString();
			  if( type != null){
				  String[] allPart=type.split("/");
				  String[] interestPart= allPart[allPart.length-1].split("#");
				  String t = interestPart[1];
				  t = t.substring(0, t.length()-1);
				  temp.add(t);
			  }

		  }  
		  return temp;
		
	}

	public List<String> getTemp() {
		return temp;
	}

	public List<String> getFrame() {
		return frame;
	}

	public String setFrame(Collection<URI> uri) {
		String nameFrame = null;
		Iterator it = uri.iterator();
		while(it.hasNext()){
			String url = it.next().toString();
			if( url.contains((CharSequence) "lexinfo")){
				  String[] allPart=url.split("/");
				  String[] interestPart= allPart[allPart.length-1].split("#");
				  String t = interestPart[1];
				  if(t.contains((CharSequence)"]")){
					  t= t.substring(0, t.length()-6);
					  nameFrame=t;
				  }else{
					  t= t.substring(0, t.length()-5);
					  nameFrame= t;
				  }
			}
		}
		
		return nameFrame;
	}
	

	
}
