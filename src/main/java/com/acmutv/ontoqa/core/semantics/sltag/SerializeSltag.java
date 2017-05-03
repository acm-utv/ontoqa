package com.acmutv.ontoqa.core.semantics.sltag;

import com.acmutv.ontoqa.core.lexicon.LexiconUsage;
import com.acmutv.ontoqa.core.semantics.base.statement.OperatorType;
import com.acmutv.ontoqa.core.semantics.dudes.Dudes;
import com.acmutv.ontoqa.core.semantics.dudes.DudesTemplates;
import com.acmutv.ontoqa.core.semantics.dudes.SimpleDudes;
import com.acmutv.ontoqa.core.syntax.ltag.Ltag;
import com.acmutv.ontoqa.core.syntax.ltag.LtagTemplates;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.acmutv.ontoqa.core.grammar.Grammar;
import com.acmutv.ontoqa.core.grammar.SimpleGrammar;
import com.acmutv.ontoqa.core.grammar.serial.GrammarJsonMapper;
import com.acmutv.ontoqa.core.lemon.Language;
import com.acmutv.ontoqa.core.lemon.LexicalEntry;
import com.acmutv.ontoqa.core.lemon.Reference;
import com.acmutv.ontoqa.core.lemon.Restriction;

public class SerializeSltag {

  private enum TYPE{properNoun, adjective, commonNoun, preposition, verb}

  private static List<String> auxiliaryVerb = Arrays.asList("do", "does", "did", "have", "has", "had");
  private static List<String> copula = Arrays.asList("is", "are", "was", "were");
  private static List<String> articles = Arrays.asList("the", "a", "an");
  private static List<String> whPronoun = Arrays.asList("who", "what", "where");


  /**
   *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) for a wh-pronoun (who, what,...)
   *  @param lexical the wh-pronoun
   *  @return the Elementary SLTAG representing the specified wh-pronoun
   **/
  public static ElementarySltag getSltagWh(String lexical)
  {
    Ltag ltagWh = LtagTemplates.wh(lexical);
    Dudes dudesWh = DudesTemplates.wh();
    ElementarySltag sltagWh = new SimpleElementarySltag(lexical, ltagWh, dudesWh);
    return sltagWh;
  }

  /**
   *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) for how-pronoun phrase (how many, how much, how long,...)
   *  @param pronoun the pronoun
   *  @param adverb the adverb
   *  @return the Elementary SLTAG representing the specified how-pronoun phrase
   **/
  public static ElementarySltag getSltagHowMany(String adverb, String pronoun)
  {
    Ltag ltagHowMany = LtagTemplates.how(adverb, pronoun, "np");
    Dudes dudesHowMany = DudesTemplates.howmany("np");
    ElementarySltag sltagHowMany = new SimpleElementarySltag("how many", ltagHowMany, dudesHowMany);
    return sltagHowMany;
  }

  /**
   *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) for definite and indefinite articles(the, a, an)
   *  @param article the article
   *  @return the Elementary SLTAG representing the specified article
   **/
  public static ElementarySltag getSltagDet(String article)
  {
    Ltag ltagDet = LtagTemplates.determiner(article, "np");
    Dudes dudesDet = DudesTemplates.determiner("np");
    ElementarySltag sltagDet = new SimpleElementarySltag(article, ltagDet, dudesDet);
    return sltagDet;
  }


  /**
   *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing a copula (is, are, was, were,...)
   *  @param copula the copula
   *  @return the Elementary SLTAG representing the specified copula
   **/
  public static ElementarySltag getSltagCopula(String copula)
  {
    Ltag ltagCopula = LtagTemplates.copula(copula, "1", "2");
    Dudes dudesCopula = DudesTemplates.copula("1", "2" );
    ElementarySltag sltagCopula = new SimpleElementarySltag(copula, ltagCopula, dudesCopula);
    return sltagCopula;
  }

  /**
   *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing an interrogative copula (is, are, was, were,...)
   *  @param copula the copula
   *  @return the Elementary SLTAG representing the specified interrogative copula
   **/
  public static ElementarySltag getSltagCopulaInterrogative(String copula)
  {
    Ltag ltagCopula = LtagTemplates.copulaInterrogative(copula, "1", "2");
    Dudes dudesCopula = DudesTemplates.copula("1", "2" );
    ElementarySltag sltagCopula = new SimpleElementarySltag(copula, ltagCopula, dudesCopula);
    return sltagCopula;
  }


  /**
   *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing an auxiliary verb (do, does, did, have, has, had...)
   *  @param auxVerb the auxiliary verb
   *  @return the Elementary SLTAG representing the specified auxiliary verb
   **/
  public static ElementarySltag getSltagAuxiliaryVerb(String auxVerb)
  {
    Ltag ltag = LtagTemplates.questioningDo(auxVerb);
    Dudes dudes = new SimpleDudes();
    ElementarySltag sltag = new SimpleElementarySltag(auxVerb, ltag, dudes);
    return sltag;
  }

  /**
   *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing the words "name of"
   *  @return the Elementary SLTAG representing "name of".
   **/
  public static ElementarySltag getSltagNameOf()
  {
    Ltag ltag = LtagTemplates.relationalPrepositionalNoun("name", "of", "subj", false);
    Dudes dudes = new SimpleDudes();
    ElementarySltag sltag = new SimpleElementarySltag("name of", ltag, dudes);
    return sltag;
  }

  /**
   *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing a proper noun (Microsoft, Google,...)
   *  @param properNoun the proper noun.
   *  @param properNounIRI the reference to ontology.
   *  @return the Elementary SLTAG representing the specified proper noun.
   **/
  public static ElementarySltag getSltagProperNoun(String properNoun, String properNounIRI)
  {
    Ltag ltag =  LtagTemplates.properNoun(properNoun);
    Dudes dudes = DudesTemplates.properNoun(properNounIRI);
    ElementarySltag sltag = new SimpleElementarySltag(properNoun, ltag, dudes);
    return sltag;
  }


  /**
   *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing a relational prepositional noun (founder of, chairman of,...)
   *  @param relNoun the relational prepositional noun.
   *  @param preposition the preposition.
   *  @param anchor the syntax category of the anchor.
   *  @param propertyIRI the reference to ontology.
   *  @return the Elementary SLTAG representing the specified relational noun.
   **/
  public static ElementarySltag getSltagRelPrepNoun(String relNoun, String preposition, String anchor, String propertyIRI)
  {
    Ltag ltag = LtagTemplates.relationalPrepositionalNoun(relNoun, preposition, "obj", false);
    Dudes dudes = DudesTemplates.relationalNoun(propertyIRI, "obj", false);
    ElementarySltag sltag = new SimpleElementarySltag(relNoun+" "+preposition, ltag, dudes);
    return sltag;
  }


  /**
   *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing a class noun.
   *  @param noun the class noun.
   *  @param predicateIRI the reference to ontology.
   *  @return the Elementary SLTAG representing the specified class noun.
   **/
  public static ElementarySltag getSltagClassNoun(String noun, String predicateIRI)
  {
    Ltag ltag = LtagTemplates.classNoun(noun, false);
    Dudes dudes = DudesTemplates.type("http://www.w3.org/1999/02/22-rdf-syntax-ns#type",predicateIRI);
    ElementarySltag sltag = new SimpleElementarySltag(noun, ltag, dudes);
    return sltag;
  }

  /**
   *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing an attributive adjective.
   *  @param attrAdj the attribute adjective.
   *  @param predicateIRI the reference to ontology.
   *  @return the Elementary SLTAG representing the specified attributive adjective.
   **/
  public static ElementarySltag getSltagAttributiveAdj(String attrAdj,String predicateIRI)
  {
    Ltag ltag = LtagTemplates.adjectiveAttributive(attrAdj, "n");
    Dudes dudes = DudesTemplates.adjective(predicateIRI);
    ElementarySltag sltag = new SimpleElementarySltag(attrAdj, ltag, dudes);
    return sltag;
  }

  /**
   *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing an attributive adjective with a restriction in the reference to ontology.
   *  @param attrAdj the attribute adjective.
   *  @param propertyIRI the IRI of the property.
   *  @param entityIRI the IRI of the entity.
   *  @return the Elementary SLTAG representing the specified attributive adjective.
   **/
  public static ElementarySltag getSltagAttributiveAdjWithRestriction(String attrAdj,String propertyIRI, String entityIRI)
  {
    Ltag ltag = LtagTemplates.adjectiveAttributive(attrAdj, "n");
    Dudes dudes = DudesTemplates.adjectiveWithRestriction(propertyIRI, entityIRI);
    ElementarySltag sltag = new SimpleElementarySltag(attrAdj, ltag, dudes);
    return sltag;
  }

  /**
   *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing a covariant attributive adjective.
   *  @param covAdj the attribute adjective.
   *  @param predicateIRI reference to ontology
   *  @return the Elementary SLTAG representing the specified CovariantScalar adjective.
   **/
  public static ElementarySltag getSltagCovariantScalarAdj(String covAdj,String predicateIRI, String adverb)
  {
    Ltag ltag = LtagTemplates.adjectiveCovariantScalar(covAdj, "np", adverb);
    Dudes dudes = DudesTemplates.adjective(predicateIRI);
    ElementarySltag sltag = new SimpleElementarySltag(covAdj, ltag, dudes);
    return sltag;
  }

  /**
   *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing a covariant attributive adjective with superlative form.
   *  @param covAdj the attribute adjective.
   *  @param predicateIRI reference to ontology.
   *  @return the Elementary SLTAG representing the specified attributive adjective.
   **/
  public static ElementarySltag getSltagCovariantScalarAdjSuperlative(String covAdj,String predicateIRI, String adverb)
  {
    Ltag ltag = LtagTemplates.adjectiveSuperlative("most "+covAdj, "the", "np");
    Dudes dudes = DudesTemplates.adjectiveSuperlative(OperatorType.MAX, predicateIRI, "np");
    ElementarySltag sltag = new SimpleElementarySltag("most "+covAdj, ltag, dudes);
    return sltag;
  }


  /**
   *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing a prepositional adjective.
   *  @param ppAdj the adjective.
   *  @param predicateIRI reference to ontology.
   *  @return the Elementary SLTAG representing the specified prepositional adjective.
   **/
  public static ElementarySltag getSltagAdjectivePP(String ppAdj,String predicateIRI)
  {
    Ltag ltag = LtagTemplates.transitiveVerbPassiveIndicativeInterrogative("headquartered", "is","subj", "obj");
    Dudes dudes = DudesTemplates.adjectivePP(predicateIRI, "subj", "obj");
    ElementarySltag sltag = new SimpleElementarySltag("is \\w* " +ppAdj, ltag, dudes);
    return sltag;
  }

  /**
   *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing a prepositional adjective.
   *  @param ppAdj the adjective.
   *  @param predicateIRI reference to ontology.
   *  @return the Elementary SLTAG representing the specified prepositional adjective.
   **/
  public static ElementarySltag getSltagAdjectivePP2(String ppAdj,String predicateIRI)
  {
    Ltag ltag = LtagTemplates.adjectivePP(ppAdj, "np");
    Dudes dudes = DudesTemplates.adjective(predicateIRI);
    ElementarySltag sltag = new SimpleElementarySltag(ppAdj, ltag, dudes);
    return sltag;
  }


  /**
   *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing a prepositional adjective with a marker.
   *  @param ppAdj the adjective.
   *  @param marker the marker.
   *  @param predicateIRI the reference to ontology.
   *  @return the Elementary SLTAG representing the specified prepositional adjective.
   **/
  public static ElementarySltag getSltagAdjectivePPWithMArker(String ppAdj, String marker, String predicateIRI)
  {
    Ltag ltag = LtagTemplates.adjectivePPWithMarker(ppAdj, marker, "np", "dp");
    Dudes dudes = DudesTemplates.adjective(predicateIRI);
    ElementarySltag sltag = new SimpleElementarySltag(ppAdj+" "+marker, ltag, dudes);
    return sltag;
  }

  /**
   *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing a predicative adjective.
   *  @param predAdj the predicative adjective.
   *  @param predicateIRI the reference to ontology.
   *  @return the Elementary SLTAG representing the specified predicative adjective.
   **/
  public static ElementarySltag getSltagPredicativeAdj(String predAdj,String predicateIRI)
  {
    Ltag ltag = LtagTemplates.adjectivePredicative(predAdj);
    Dudes dudes = DudesTemplates.adjective(predicateIRI);
    ElementarySltag sltag = new SimpleElementarySltag(predAdj, ltag, dudes);
    return sltag;
  }

  /**
   *  Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing a predicative adjective with a restriction in the reference to ontology.
   *  @param predAdj the predicative adjective.
   *  @param predicateIRI the predicate IRI.
   *  @param entityIRI the entity IRI.
   *  @return the Elementary SLTAG representing the specified predicative adjective.
   **/
  public static ElementarySltag getSltagPredicativeAdjWithRestriction(String predAdj,String predicateIRI, String entityIRI)
  {
    Ltag ltag = LtagTemplates.adjectivePredicative(predAdj);
    Dudes dudes = DudesTemplates.adjectiveWithRestriction(predicateIRI, entityIRI);
    ElementarySltag sltag = new SimpleElementarySltag(predAdj, ltag, dudes);
    return sltag;
  }

  /**
   * Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing a transitive verb with active voice and indicative mood.
   * @param verb the verb.
   * @param predicateIRI the reference to ontology.
   * @return the Elementary SLTAG representing the specified verb.
   * */
  public static ElementarySltag getSltagTransitiveVerbActiveIndicative(String verb,String predicateIRI){

    Ltag ltag =  LtagTemplates.transitiveVerbActiveIndicative(verb, "subj", "obj");
    Dudes dudes = DudesTemplates.transitiveVerb(predicateIRI, "subj", "obj");

    ElementarySltag sltag = new SimpleElementarySltag(verb, ltag, dudes);
    return sltag;
  }

  /**
   * Generates a Elementary SLTAG (LTAG with the corresponding DUDES) representing a preposition.
   * @param preposition the preposition.
   * @param predicateIRI the reference to ontology.
   * @return the Elementary SLTAG representing the specified preposition.
   * */
  public static ElementarySltag getSltagPreposition(String preposition,String predicateIRI){

    Ltag ltag =  LtagTemplates.prepositionAdj(preposition, "dp1","dp2");
    Dudes dudes = new SimpleDudes();
    ElementarySltag sltag = new SimpleElementarySltag(preposition, ltag, dudes);
    return sltag;
  }

  /**
   * Writes the whole {@link Grammar} on File.
   * @param grammar the whole {@link Grammar}.
   * @param file the file.
   * */
  public static void writeGrammarOnFile(Grammar grammar, File file) throws InstantiationException, IllegalAccessException, IOException
  {
    GrammarJsonMapper jsonMapper = new GrammarJsonMapper();
    jsonMapper.writeValue(file, grammar);
  }

  /**
   * Reads a file containing a {@link Grammar}.
   * @param file the file to read.
   * @return the read {@link Grammar}.
   * */
  public static Grammar readGrammarFromFile(File file) throws JsonParseException, JsonMappingException, IOException
  {
    GrammarJsonMapper jsonMapper = new GrammarJsonMapper();
    Grammar grammar = jsonMapper.readValue(file, new TypeReference<Grammar>(){});

    return grammar;
  }

  /**
   * Generates all Elementary SLTAG we need in the form of {@link Grammar}.
   * @param list the list of all Lexical Entries.
   * @return the {@link Grammar} containing the {@link ElementarySltag}.
   * @throws IllegalAccessException
   * @throws InstantiationException
   **/
  public static Grammar getAllElementarySltag(List<LexicalEntry> list) throws IOException, InstantiationException, IllegalAccessException
  {
    LexicalEntry lEntry = new LexicalEntry(Language.EN);
    Grammar grammar= new SimpleGrammar();
    int i;
    String uri = new String();
    for(i=0; i<list.size(); i++)
    {
      lEntry = list.get(i);
      switch(TYPE.valueOf(lEntry.getPOS())) {
        case properNoun:
        {
          uri = lEntry.getReferences().toString();
          uri = uri.replace("[", "");
          uri = uri.replace("]", "");
          grammar.addElementarySLTAG(SerializeSltag.getSltagProperNoun(lEntry.getCanonicalForm(), uri));
          break;

        }
        case adjective:
        {
          boolean attr= false;
          boolean pred = false;
          List<String> frames = LexiconUsage.getFrames(lEntry.getSenseBehaviours());
          for(int k=0; k< frames.size(); k++){
            if(frames.get(k).contains("AdjectiveAttributiveFrame") && !attr){

              if(lEntry.isCovariantScalar()){
                String reference = LexiconUsage.getOneReference(lEntry.getReferences());
                uri = reference.replace("[", "");
                uri = uri.replace("]", "");
                grammar.addElementarySLTAG(SerializeSltag.getSltagCovariantScalarAdjSuperlative(lEntry.getCanonicalForm(), uri, "most"));
              }else{
                //controllare parentesi quadre
                for( Reference ref: lEntry.getReferences()){
                  Restriction restriction = (Restriction) ref;
                  if( restriction.getURI() != null){
                    grammar.addElementarySLTAG(SerializeSltag.getSltagAttributiveAdjWithRestriction(lEntry.getCanonicalForm(), restriction.getProperty(), restriction.getValue()));
                  }else{
                    grammar.addElementarySLTAG(SerializeSltag.getSltagAttributiveAdj(lEntry.getCanonicalForm(), ref.toString()));
                  }
                }
              }
              attr= true;
            }else if(frames.get(k).equals("AdjectivePPFrame")){
              uri = lEntry.getReferences().toString();
              uri = uri.replace("[", "");
              uri = uri.replace("]", "");
              grammar.addElementarySLTAG(SerializeSltag.getSltagAdjectivePP(lEntry.getCanonicalForm(), uri));
              grammar.addElementarySLTAG(SerializeSltag.getSltagAdjectivePPWithMArker(lEntry.getCanonicalForm(), "in", uri));
            }
            else if(frames.get(k).equals("AdjectivePredicativeFrame") && !pred ){
              //controllare parentesi quadre
              if(!lEntry.isCovariantScalar()){
                for( Reference ref: lEntry.getReferences()){
                  Restriction restriction = (Restriction) ref;
                  if( restriction.getURI() != null){
                    grammar.addElementarySLTAG(SerializeSltag.getSltagPredicativeAdjWithRestriction(lEntry.getCanonicalForm(), restriction.getProperty(), restriction.getValue()));
                  }else{
                    grammar.addElementarySLTAG(SerializeSltag.getSltagPredicativeAdj(lEntry.getCanonicalForm(), ref.toString()));
                  }

                }
                pred= true;
              }
            }
          }

          break;
        }
        case commonNoun:
        {
          if(lEntry.getReferences().size() > 0 ) {
            String ref = LexiconUsage.getReferencePossessiveAdjunct(lEntry.getSenseBehaviours());
            if(ref!= null)
            {
              uri = ref.replace("[", "");
              uri = uri.replace("]", "");
              grammar.addElementarySLTAG(SerializeSltag.getSltagClassNoun(lEntry.getCanonicalForm(), uri));
              grammar.addElementarySLTAG(SerializeSltag.getSltagRelPrepNoun(lEntry.getCanonicalForm(), "of", "DP", uri));
              System.out.println(lEntry.getCanonicalForm());
              for(int j=0; j<lEntry.getForms().size(); j++)
              {
                grammar.addElementarySLTAG(SerializeSltag.getSltagClassNoun(lEntry.getForms().get(j).getWrittenRep(), uri));
                grammar.addElementarySLTAG(SerializeSltag.getSltagRelPrepNoun(lEntry.getForms().get(j).getWrittenRep(), "of", "DP", uri));

              }
            }
            else
            {
              String reference = LexiconUsage.getReference(lEntry.getSenseBehaviours());
              uri = reference.replace("[", "");
              uri = uri.replace("]", "");
              grammar.addElementarySLTAG(SerializeSltag.getSltagClassNoun(lEntry.getCanonicalForm(), uri));
              for( int j=0; j<lEntry.getForms().size(); j++)
              {
                grammar.addElementarySLTAG(SerializeSltag.getSltagClassNoun(lEntry.getForms().get(j).getWrittenRep(), uri));
              }

            }
          }

          break;
        }
        case preposition:
        {
          uri = lEntry.getReferences().toString();
          uri = uri.replace("[", "");
          uri = uri.replace("]", "");
          grammar.addElementarySLTAG(SerializeSltag.getSltagPreposition(lEntry.getCanonicalForm(), uri));
          break;
        }
        case verb:
        {
          uri = lEntry.getReferences().toString();
          uri = uri.replace("[", "");
          uri = uri.replace("]", "");
          grammar.addElementarySLTAG(SerializeSltag.getSltagTransitiveVerbActiveIndicative(lEntry.getCanonicalForm(), uri));
          for(int j=0; j<lEntry.getForms().size(); j++)
          {
            uri = lEntry.getReferences().toString();
            uri = uri.replace("[", "");
            uri = uri.replace("]", "");
            grammar.addElementarySLTAG(SerializeSltag.getSltagTransitiveVerbActiveIndicative(lEntry.getForms().get(j).getWrittenRep(), uri));

          }
          break;
        }
      }
    }

	    /* how many */
    ElementarySltag sltag = SerializeSltag.getSltagHowMany("how", "many");
    grammar.addElementarySLTAG(sltag);
//	    grammar.addElementarySLTAG(SerializeSltag.getSltagHowMany("how", "many"));

//		//test print ltag node
//		for(i=0; i<sltag.getEdges().size(); i++)
//	    {
//	    	System.out.println(i+": "+sltag.getEdges().get(i));
//	    	System.out.println(i+"1: "+sltag.getEdges().get(i).getLhs());
//	    	System.out.println(i+"2: "+sltag.getEdges().get(i).getRhs());
//	    	System.out.println(i+"2: "+sltag.getEdges().get(i).getRhs());
//	    }
//
	    /* name of */
    grammar.addElementarySLTAG(SerializeSltag.getSltagNameOf());


	    /* is, are, was, were */
    for(i=0; i<SerializeSltag.copula.size(); i++){
      grammar.addElementarySLTAG(SerializeSltag.getSltagCopula(copula.get(i)));
      grammar.addElementarySLTAG(SerializeSltag.getSltagCopulaInterrogative(copula.get(i)));
    }

	    /* do, does, did, have, has, had */
    for(i=0; i<SerializeSltag.auxiliaryVerb.size(); i++){
      grammar.addElementarySLTAG(SerializeSltag.getSltagAuxiliaryVerb(auxiliaryVerb.get(i)));
    }

	    /* the, a, an */
    for(i=0; i<SerializeSltag.articles.size(); i++){
      grammar.addElementarySLTAG(SerializeSltag.getSltagDet(articles.get(i)));
    }

	    /* who, what, where */
    for(i=0; i<SerializeSltag.whPronoun.size(); i++){
      grammar.addElementarySLTAG(SerializeSltag.getSltagWh(whPronoun.get(i)));
    }

//	    List<ElementarySltag> eSl = grammar.getAllElementarySLTAG();
//	    for(ElementarySltag k : eSl){
//	    	System.out.println(k.getEntry());
//	    	System.out.println(k.getEdges());
//
//	    }

    return grammar;
  }

}
