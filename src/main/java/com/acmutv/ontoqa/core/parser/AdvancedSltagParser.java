/*
  The MIT License (MIT)

  Copyright (c) 2017 Antonella Botte, Giacomo Marciani and Debora Partigianoni

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:


  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.


  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
 */

package com.acmutv.ontoqa.core.parser;

import com.acmutv.ontoqa.core.exception.LTAGException;
import com.acmutv.ontoqa.core.exception.OntoqaParsingException;
import com.acmutv.ontoqa.core.exception.QueryException;
import com.acmutv.ontoqa.core.grammar.Grammar;
import com.acmutv.ontoqa.core.knowledge.KnowledgeManager;
import com.acmutv.ontoqa.core.knowledge.answer.Answer;
import com.acmutv.ontoqa.core.knowledge.answer.SimpleAnswer;
import com.acmutv.ontoqa.core.knowledge.ontology.Ontology;
import com.acmutv.ontoqa.core.knowledge.query.QueryResult;
import com.acmutv.ontoqa.core.parser.conflict.Candidate;
import com.acmutv.ontoqa.core.parser.conflict.Conflict;
import com.acmutv.ontoqa.core.parser.conflict.ConflictList;
import com.acmutv.ontoqa.core.semantics.base.statement.Statement;
import com.acmutv.ontoqa.core.semantics.base.term.Variable;
import com.acmutv.ontoqa.core.semantics.dudes.Dudes;
import com.acmutv.ontoqa.core.semantics.sltag.ElementarySltag;
import com.acmutv.ontoqa.core.semantics.sltag.SimpleSltag;
import com.acmutv.ontoqa.core.semantics.sltag.Sltag;
import com.acmutv.ontoqa.core.syntax.SyntaxCategory;
import com.acmutv.ontoqa.core.syntax.ltag.LtagNode;
import com.acmutv.ontoqa.core.syntax.ltag.LtagNodeMarker;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.acmutv.ontoqa.core.parser.EnglishConstructs.isAskSentence;

/**
 * An advanced SLTAG parser.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class AdvancedSltagParser implements ReasoningSltagParser {

  private static final Logger LOGGER = LoggerFactory.getLogger(AdvancedSltagParser.class);

  /**
   * Parses {@code sentence} with {@code grammar}.
   * @param sentence the sentence to parse.
   * @param grammar the grammar to parse with.
   * @return the parsed SLTAG.
   * @throws OntoqaParsingException when parsing fails.
   */
  @Override
  public Sltag parse(String sentence, Grammar grammar, Ontology ontology) throws Exception {
    ParserStateNew state = new ParserStateNew(sentence);

    SltagTokenizer tokenizer = new SimpleSltagTokenizer(grammar, sentence);

    /* PRE-PROCESSING */
    LOGGER.debug("[STATUS] :: PRE-PROCESSING");
    if (isAskSentence(sentence)) {
      LOGGER.debug("[PRE-PROCESSING] :: found ASK structure");
      state.setAsk(true);
    } else {
      LOGGER.debug("[PRE-PROCESSING] :: found SELECT structure");
      state.setAsk(false);
    }

    /* TOKENIZATION */
    LOGGER.debug("[STATUS] :: PROCESSING");
    while (tokenizer.hasNext()) {
      Token token = tokenizer.next();

      String lexPattern = token.getLexicalPattern();
      List<ElementarySltag> candidates = token.getCandidates();
      state.setIdxPrev(token.getPrev());

      LOGGER.debug("[PROCESSING] :: entry '{}'", lexPattern);

      if (candidates.isEmpty()) {
        throw new OntoqaParsingException("Cannot find SLTAG for entry: %s", lexPattern);
      }

      /* AMBIGUITIES MANAGEMENT */
      LOGGER.debug("[STATUS] :: AMBIGUITIES MANAGEMENT");
      if (candidates.size() > 1) {
        LOGGER.debug("[AMBIGUITIES MANAGEMENT] :: found {} ambiguities for entry '{}' (idxPrev: {})\n{}",
            candidates.size(), lexPattern, state.getIdxPrev(),
            candidates.stream().map(ElementarySltag::toPrettyString).collect(Collectors.joining("\n")));
        filterAmbiguities(candidates, state, ontology);
      } else {
        LOGGER.debug("[AMBIGUITIES MANAGEMENT] :: no ambiguities found");
      }

      /* QUEUE INSERTION */
      LOGGER.debug("[STATUS] :: QUEUE INSERTION");
      if (candidates.size() == 1) {
        Sltag candidate = candidates.get(0);
        if (candidate.isAdjunctable()) {
          LOGGER.debug("[QUEUE] :: enqueueing adjunction (entry: '{}' | idxPrev: {}):\n{}", lexPattern, state.getIdxPrev(), candidate.toPrettyString());
          state.addWaitingAdjunction(candidate, state.getIdxPrev());
        } else if (candidate.isSentence()) {
          LOGGER.debug("[QUEUE] :: setting sentence (entry: '{}' | idxPrev: {}):\n{}", lexPattern, state.getIdxPrev(), candidate.toPrettyString());
          if (state.getCurr() != null) {
            throw new Exception("Cannot decide sentence root: multiple root found");
          }
          state.setCurr(candidate);
          state.getCurr().getSemantics().setSelect(!state.isAsk());
        } else {
          LOGGER.debug("[QUEUE] :: enqueueing substitution (entry: '{}' | idxPrev: {}) :\n{}", lexPattern, state.getIdxPrev(), candidate.toPrettyString());
          state.addWaitingSubstitution(candidate, state.getIdxPrev());
        }
      }

      /* QUEUE CONSUMPTION */
      LOGGER.debug("[STATUS] :: QUEUE CONSUMPTION");
      if (state.getCurr() != null) {
        consumeWaitingSubstitutions(state);
        consumeWaitingAdjunctions(state);
      }

      LOGGER.debug("[STATUS] :: current SLTAG\n{}", (state.getCurr() != null) ? state.getCurr().toPrettyString() : "NONE");
    }

    if (state.getCurr() == null) {
      throw new Exception("Cannot build SLTAG");
    }

    /* AMBIGUITIES RESOLUTION */
    LOGGER.debug("[STATUS] :: AMBIGUITIES RESOLUTION");
    if (!state.getConflictList().isEmpty()) {
      solveAmbiguities(state, ontology);
    }

    /* POST-PROCESSING */
    LOGGER.debug("[STATUS] :: POST-PROCESSING");
    if (state.isAsk()) {
      LOGGER.debug("[POST-PROCESSING] :: setting ASK semantics");
      state.getCurr().getSemantics().setSelect(false);
    } else {
      LOGGER.debug("[POST-PROCESSING] :: setting SELECT semantics");
      state.getCurr().getSemantics().setSelect(true);
    }

    LOGGER.debug("[STATUS] :: current SLTAG\n{}", state.getCurr().toPrettyString());

    return state.getCurr();
  }

  /**
   * Filters ambiguities.
   * @param candidates the list of colliding candidates.
   * @param state the parser state.
   */
  private static void filterAmbiguities(List<ElementarySltag> candidates, ParserStateNew state, Ontology ontology) {
    Integer idxPrev = state.getIdxPrev();
    ConflictList conflicts = state.getConflictList();

    Iterator<ElementarySltag> iterCandidates;

    /* SYNTACTICALLY SOLVABLE AMBIGUITIES */
    iterCandidates = candidates.iterator();
    while (candidates.size() > 1 && iterCandidates.hasNext()) {
      Sltag candidate = iterCandidates.next();
      if (candidate.isSentence()) {
        if (idxPrev == null && candidate.isLeftSub()) { // excludes is (affermative) when we are at the first word.
          LOGGER.debug("[AMBIGUITIES MANAGEMENT] :: excluded ambiguity (found S-rooted left-sub at the beginning of the sentence):\n{}", candidate.toPrettyString());
          iterCandidates.remove();
        } else if (idxPrev != null && !candidate.isLeftSub()) { // excludes is (interrogative) when we are in the middle of the sentence.
          LOGGER.debug("[AMBIGUITIES MANAGEMENT] :: excluded ambiguity (found S-rooted not left-sub candidate within the sentence):\n{}", candidate.toPrettyString());
          iterCandidates.remove();
        }
      } else {
        if (candidate.isLeftAdj()) {
          LtagNode target = state.getCurr().firstMatch(candidate.getRoot().getCategory(), state.getWords().get(idxPrev), null);
          if (target == null || LtagNodeMarker.ADJ.equals(target.getMarker())) {
            LOGGER.debug("[AMBIGUITIES MANAGEMENT] :: excluded ambiguity (found left adjunctable with no eligible target):\n{}", candidate.toPrettyString());
            iterCandidates.remove();
          }
        } else if (candidate.isRightAdj()) {
          LtagNode target = state.getCurr().firstMatch(candidate.getRoot().getCategory(), state.getWords().get(idxPrev), null);
          if (target == null || LtagNodeMarker.ADJ.equals(target.getMarker())) {
            LOGGER.debug("[AMBIGUITIES MANAGEMENT] :: excluded ambiguity (found right adjunctable with no eligible target):\n{}", candidate.toPrettyString());
            iterCandidates.remove();
          }
        } else if (!candidate.isAdjunctable() &&
            (state.getCurr().firstMatch(candidate.getRoot().getCategory(), state.getWords().get(idxPrev), LtagNodeMarker.SUB) == null)) {
          LOGGER.debug("[AMBIGUITIES MANAGEMENT] :: excluded ambiguity (found substitutable with no eligible target):\n{}", candidate.toPrettyString());
          iterCandidates.remove();
        }
      }
    }

    /* SEMANTICALLY SOLVABLE AMBIGUITIES (SUBSTITUTIONS) */
    iterCandidates = candidates.iterator();
    while (candidates.size() > 1 && iterCandidates.hasNext()) {
      Sltag candidate = iterCandidates.next();
      if (!candidate.isAdjunctable()) {
        if (!isFeasibleSubstitution(candidate, null, state, ontology)) {
          LOGGER.debug("[AMBIGUITIES MANAGEMENT] :: excluded ambiguity (not feasible substitution):\n{}", candidate.toPrettyString());
          iterCandidates.remove();
        } else {
          LOGGER.debug("[AMBIGUITIES MANAGEMENT] :: solved ambiguity (feasible substitution):\n{}", candidate.toPrettyString());
          //iterCandidates.remove();
        }
      }
    }

    /* SEMANTICALLY SOLVABLE AMBIGUITIES (ADJUNCTIONS) */
    iterCandidates = candidates.iterator();
    while (candidates.size() > 1 && iterCandidates.hasNext()) {
      Sltag candidate = iterCandidates.next();
      if (candidate.isAdjunctable() && candidate.isLeftAdj()) {
        if (!isFeasibleAdjunction(candidate, null, state, ontology)) {
          LOGGER.debug("[AMBIGUITIES MANAGEMENT] :: excluded ambiguity (not feasible adjunction):\n{}", candidate.toPrettyString());
          iterCandidates.remove();
        } else {
          LOGGER.debug("[AMBIGUITIES MANAGEMENT] :: solved ambiguity (feasible adjunction):\n{}", candidate.toPrettyString());
          //iterCandidates.remove();
        }
      }
    }

    if (candidates.size() == 1) {
      LOGGER.debug("[AMBIGUITIES MANAGEMENT] :: solved ambiguities (found unique feasible candidate):\n{}",
          candidates.get(0).toPrettyString());
      return;
    }

     /* UNSOLVABLE AMBIGUITIES */
    iterCandidates = candidates.iterator();
    while (iterCandidates.hasNext()) {
      Sltag candidate = iterCandidates.next();
      LOGGER.debug("[AMBIGUITIES MANAGEMENT] :: adding ambiguity (idxPrev: {}):\n{}", idxPrev, candidate.toPrettyString());
      conflicts.add(candidate, idxPrev);
      //iterCandidates.remove();
    }
  }

  /**
   * Consumes waiting substitutions.
   * @param state the parser state.
   */
  private static void consumeWaitingSubstitutions(ParserStateNew state) throws LTAGException {
    if (state.getWaitSubstitutions().isEmpty()) {
      LOGGER.debug("[QUEUE CONSUMPTION] :: no waiting substitutions to consume");
      return;
    }

    Integer idxPrev = state.getIdxPrev();
    Sltag curr = state.getCurr();

    Iterator<LtagNode> substitutionTargets = curr.getNodesDFS(LtagNodeMarker.SUB).iterator();
    while (substitutionTargets.hasNext()) {
      LtagNode substitutionTarget = substitutionTargets.next();
      Iterator<Candidate> waitingSubstitutionCandidates = state.getWaitSubstitutions().iterator();
      while (waitingSubstitutionCandidates.hasNext()) {
        Candidate waitingSubstitutionCandidate = waitingSubstitutionCandidates.next();
        Sltag substitutionCandidate = waitingSubstitutionCandidate.getSltag();
        if (substitutionTarget.getCategory().equals(substitutionCandidate.getRoot().getCategory())) { /* CAN MAKE SUBSTITUTION */
          if (curr.getSemantics().getMainVariable() == null
              && substitutionCandidate.getSemantics().getMainVariable() != null) { /* RECORD A MAIN VARIABLE MISS */
            int pos = (idxPrev != null) ? idxPrev + 1 : 0;
            Variable mainVar = substitutionCandidate.getSemantics().getMainVariable();
            Set<Statement> statements = substitutionCandidate.getSemantics().getStatements(mainVar);
            curr.substitution(substitutionCandidate, substitutionTarget);
            Variable renamedVar = curr.getSemantics().findRenaming(mainVar, statements);
            if (renamedVar != null) {
              Triple<Variable,Variable,Set<Statement>> missedRecord = new MutableTriple<>(mainVar, renamedVar, statements);
              state.getMissedMainVariables().put(pos, missedRecord);
              LOGGER.debug("[QUEUE CONSUMPTION] :: recorded main variable: pos: {} | mainVar: {} renamed to {} | statements: {} ", pos, mainVar, renamedVar, statements);
            }
          } else {
            curr.substitution(substitutionCandidate, substitutionTarget);
          }
          LOGGER.debug("[QUEUE CONSUMPTION] :: substituted {} with:\n{}", substitutionTarget, substitutionCandidate.toPrettyString());
          waitingSubstitutionCandidates.remove();
          substitutionTargets = curr.getNodesDFS(LtagNodeMarker.SUB).iterator();
          break;
        }
      }
    }
  }

  /**
   * Consumes waiting adjunctions.
   * @param state the parser state.
   */
  private static void consumeWaitingAdjunctions(ParserStateNew state) throws LTAGException {
    if (state.getWaitAdjunction().isEmpty()) {
      LOGGER.debug("[QUEUE CONSUMPTION] :: no waiting adjunctions to consume");
      return;
    }

    List<String> words = state.getWords();
    Sltag curr = state.getCurr();
    Map<Integer,Triple<Variable,Variable,Set<Statement>>> missedMainVariables = state.getMissedMainVariables();

    Iterator<Candidate> waitingAdjunctionCandidates = state.getWaitAdjunction().iterator();
    while (waitingAdjunctionCandidates.hasNext()) {
      Candidate waitingAdjunctionCandidate = waitingAdjunctionCandidates.next();
      Sltag adjunctionCandidate = waitingAdjunctionCandidate.getSltag();
      Integer start = waitingAdjunctionCandidate.getPosition();
      String startLexicalEntry = (start != null) ? words.get(start) : null;
      LtagNode localTarget = curr.firstMatch(adjunctionCandidate.getRoot().getCategory(), startLexicalEntry, null);
      if (localTarget != null && !LtagNodeMarker.SUB.equals(localTarget.getMarker())) { /* CAN MAKE ADJUNCTION */
        if (curr.getSemantics().getMainVariable() == null &&
            adjunctionCandidate.isLeftAdj() &&
            missedMainVariables.containsKey(start)) { /* INSPECT MAIN VARIABLE MISS */
          int lookup = (start != null) ? start : 0;
          Variable missedMainVar = missedMainVariables.get(lookup).getMiddle();
          LOGGER.warn("[QUEUE-CONSUMPTION] :: found possible main variable miss at pos {}: {}", lookup, missedMainVar);
          curr.getSemantics().setMainVariable(missedMainVar);
          LOGGER.warn("[QUEUE-CONSUMPTION] :: main variable temporarily set to: {}", missedMainVar);
          curr.adjunction(adjunctionCandidate, localTarget);
          curr.getSemantics().setMainVariable(null);
          LOGGER.warn("[QUEUE-CONSUMPTION] :: resetting main variable to NULL");
        } else if (curr.getSemantics().getMainVariable() == null  &&
            adjunctionCandidate.isRightAdj() &&
            missedMainVariables.containsKey((start != null) ? start + 2 : 1)) {
          int lookup = (start != null) ? start + 2 : 1;
          Variable missedMainVar = missedMainVariables.get(lookup).getMiddle();
          LOGGER.warn("[QUEUE-CONSUMPTION] :: found possible main variable miss at pos {}: {}", lookup, missedMainVar);
          curr.getSemantics().setMainVariable(missedMainVar);
          LOGGER.warn("[QUEUE-CONSUMPTION] :: main variable temporarily set to: {}", missedMainVar);
          curr.adjunction(adjunctionCandidate, localTarget);
          curr.getSemantics().setMainVariable(null);
          LOGGER.warn("[QUEUE-CONSUMPTION] :: resetting main variable to NULL");
        } else {
          curr.adjunction(adjunctionCandidate, localTarget);
        }
        LOGGER.debug("[QUEUE-CONSUMPTION] :: adjuncted {} on {}", adjunctionCandidate.toPrettyString(), localTarget);
        waitingAdjunctionCandidates.remove();
      }
    }
  }

  /**
   * Solves ambiguities.
   * @param state the parser state.
   * @param ontology the ontology.
   * @throws LTAGException
   */
  private static void solveAmbiguities(ParserStateNew state, Ontology ontology) throws LTAGException {
    List<String> words = state.getWords();
    ConflictList conflictsList = state.getConflictList();
    Map<Integer,Triple<Variable,Variable,Set<Statement>>> missedMainVariables = state.getMissedMainVariables();
    Sltag curr = state.getCurr();

    LOGGER.debug("[AMBIGUITIES RESOLUTION] :: inspecting substitutions");
    Iterator<Integer> conflictPositions = conflictsList.keySet().iterator();
    while (conflictPositions.hasNext()) {
      Integer conflictPosition = conflictPositions.next();
      Conflict conflict = conflictsList.get(conflictPosition);
      Iterator<Candidate> conflictingCandidates = conflict.iterator();

      while (conflictingCandidates.hasNext()) {
        Candidate conflictingCandidate = conflictingCandidates.next();
        Sltag candidate = conflictingCandidate.getSltag();
        Integer position = conflictingCandidate.getPosition();
        if (isFeasibleSubstitution(candidate, conflictPosition, state, ontology)) {
          String startLexicalEntry = (position != null) ? words.get(position) : null;
          LOGGER.debug("[AMBIGUITIES RESOLUTION] :: looking for target substitution target starting at {} ({}):\n{}", position, startLexicalEntry, candidate.toPrettyString());
          LtagNode target = curr.firstMatch(candidate.getRoot().getCategory(), startLexicalEntry, LtagNodeMarker.SUB);
          try {
            curr.substitution(candidate, target);
            LOGGER.debug("[AMBIGUITIES RESOLUTION] :: substituted {} with:\n{}", target, candidate.toPrettyString());
            conflictPositions.remove();
            conflictsList.remove(position);
            break;
          } catch (LTAGException exc) {
            LOGGER.warn(exc.getMessage());
          }
        }
      }
    }

    LOGGER.debug("[AMBIGUITIES RESOLUTION] :: inspecting adjunctions");
    conflictPositions = conflictsList.keySet().iterator();
    while (conflictPositions.hasNext()) {
      Integer conflictPosition = conflictPositions.next();
      Conflict conflict = conflictsList.get(conflictPosition);
      Iterator<Candidate> conflictingCandidates = conflict.iterator();

      while (conflictingCandidates.hasNext()) {
        Candidate conflictingCandidate = conflictingCandidates.next();
        Sltag candidate = conflictingCandidate.getSltag();
        Integer position = conflictingCandidate.getPosition();
        if (isFeasibleAdjunction(candidate, conflictPosition, state, ontology)) {
          String startLexicalEntry = (position != null) ? words.get(position) : null;
          SyntaxCategory category = candidate.getRoot().getCategory();
          LOGGER.debug("[AMBIGUITIES RESOLUTION] :: adjunction starting at {} ({}):\n{}", position, startLexicalEntry, candidate.toPrettyString());
          LtagNode localTarget = curr.firstMatch(category, startLexicalEntry, null);
          if (localTarget != null && !LtagNodeMarker.SUB.equals(localTarget.getMarker())) { /* CAN MAKE ADJUNCTION */
            if (curr.getSemantics().getMainVariable() == null &&
                candidate.isLeftAdj() &&
                missedMainVariables.containsKey(position)) { /* INSPECT MAIN VARIABLE MISS */
              int lookup = (position != null) ? position : 0;
              Variable missedMainVar = missedMainVariables.get(lookup).getMiddle();
              LOGGER.warn("[AMBIGUITIES RESOLUTION] :: found possible main variable miss at pos {}: {}", lookup, missedMainVar);
              curr.getSemantics().setMainVariable(missedMainVar);
              LOGGER.warn("[AMBIGUITIES RESOLUTION] :: main variable temporarily set to: {}", missedMainVar);
              curr.adjunction(candidate, localTarget);
              curr.getSemantics().setMainVariable(null);
              LOGGER.warn("[AMBIGUITIES RESOLUTION] :: resetting main variable to NULL");
            } else if (curr.getSemantics().getMainVariable() == null &&
                candidate.isRightAdj() &&
                missedMainVariables.containsKey((position != null) ? position + 2 : 1)) {
              int lookup = (position != null) ? position + 2 : 1;
              Variable missedMainVar = missedMainVariables.get(lookup).getMiddle();
              LOGGER.warn("[AMBIGUITIES RESOLUTION] :: found possible main variable miss at pos {}: {}", lookup, missedMainVar);
              curr.getSemantics().setMainVariable(missedMainVar);
              LOGGER.warn("[AMBIGUITIES RESOLUTION] :: main variable temporarily set to: {}", missedMainVar);
              curr.adjunction(candidate, localTarget);
              curr.getSemantics().setMainVariable(null);
              LOGGER.warn("[AMBIGUITIES RESOLUTION] :: resetting main variable to NULL");
            } else {
              curr.adjunction(candidate, localTarget);
            }
          }
        }
      }
    }
  }

  /**
   * Checks if {@code candidate} is feasible for substitution.
   * @param candidate the candidate.
   * @param idxPrev the position of the previous lexical entry (if null, consider the {@code state}).
   * @param state the parser state.
   * @param ontology the ontology.
   * @return true, if {@code candidate} is feasible for substitution; false, otherwise.
   */
  private static boolean isFeasibleSubstitution(Sltag candidate, Integer idxPrev, ParserStateNew state, Ontology ontology) {
    LOGGER.debug("[FEASIBILITY CHECK] :: checking feasibility for substitution:\n{}", candidate.toPrettyString());

    if (candidate.isAdjunctable()) {
      LOGGER.debug("[FEASIBILITY CHECK] :: not feasible for substitution:\n{}", candidate.toPrettyString());
      return false;
    }

    Sltag tmp_curr = new SimpleSltag(state.getCurr());

    Integer position = (idxPrev == null) ? state.getIdxPrev() : idxPrev;
    List<String> words = state.getWords();

    String startLexicalEntry = (position != null) ? words.get(position) : null;
    LOGGER.debug("[FEASIBILITY CHECK] :: looking for feasible substitution target starting at {} ({}):\n{}", position, startLexicalEntry, candidate.toPrettyString());
    LtagNode target = tmp_curr.firstMatch(candidate.getRoot().getCategory(), startLexicalEntry, LtagNodeMarker.SUB);
    if (target != null) {
      LOGGER.debug("[FEASIBILITY CHECK] :: found substitution target {} for candidate:\n{}", target, candidate.toPrettyString());
      try {
        tmp_curr.substitution(candidate, target);
        LOGGER.debug("[FEASIBILITY CHECK] :: simulated substitution of {} with:\n{}", target, candidate.toPrettyString());
      } catch (LTAGException exc) {
        LOGGER.warn(exc.getMessage());
        return false;
      }
    } else {
      LOGGER.debug("[FEASIBILITY CHECK] :: no substitution target found for candidate:\n{}", candidate.toPrettyString());
      return false;
    }

    return isOntologicallyFeasible(tmp_curr, ontology);
  }

  /**
   * Checks if {@code candidate} is feasible for adjunction.
   * @param candidate the candidate.
   * @param idxPrev the position of the previous lexical entry (if null, consider the {@code state}).
   * @param state the parser state.
   * @param ontology the ontology.
   * @return true, if {@code candidate} is feasible for adjunction; false, otherwise.
   */
  private static boolean isFeasibleAdjunction(Sltag candidate, Integer idxPrev, ParserStateNew state, Ontology ontology) {
    LOGGER.debug("[FEASIBILITY CHECK] :: checking feasibility for adjunction:\n{}", candidate.toPrettyString());

    if (!candidate.isAdjunctable()) {
      LOGGER.debug("[FEASIBILITY CHECK] :: not feasibility for adjunction (not adjunctable):\n{}", candidate.toPrettyString());
      return false;
    }

    Sltag tmp_curr = new SimpleSltag(state.getCurr());
    Integer position = (idxPrev == null) ? state.getIdxPrev() : idxPrev;
    List<String> words = state.getWords();
    Map<Integer,Triple<Variable,Variable,Set<Statement>>> missedMainVariables = state.getMissedMainVariables();

    String startLexicalEntry = (position != null) ? words.get(position) : null;
    SyntaxCategory category = candidate.getRoot().getCategory();
    LOGGER.debug("[FEASIBILITY CHECK] :: simulating adjunction starting at {} ({}):\n{}", position, startLexicalEntry, candidate.toPrettyString());
    LtagNode localTarget = tmp_curr.firstMatch(category, startLexicalEntry, null);
    if (localTarget != null) { /* CAN MAKE ADJUNCTION */
      if (tmp_curr.getSemantics().getMainVariable() == null &&
          candidate.isLeftAdj() &&
          missedMainVariables.containsKey(position)) { /* INSPECT MAIN VARIABLE MISS */
        int lookup = (position != null) ? position : 0;
        Variable missedMainVar = missedMainVariables.get(lookup).getMiddle();
        LOGGER.warn("[FEASIBILITY CHECK] :: found possible main variable miss at pos {}: {}", lookup, missedMainVar);
        tmp_curr.getSemantics().setMainVariable(missedMainVar);
        LOGGER.warn("[FEASIBILITY CHECK] :: main variable temporarily set to: {}", missedMainVar);
        try {
          tmp_curr.adjunction(candidate, localTarget);
        } catch (LTAGException exc) {
          LOGGER.warn(exc.getMessage());
          return false;
        }
        tmp_curr.getSemantics().setMainVariable(null);
        LOGGER.warn("[FEASIBILITY CHECK] :: resetting main variable to NULL");
      } else if (tmp_curr.getSemantics().getMainVariable() == null &&
          candidate.isRightAdj() &&
          missedMainVariables.containsKey((position != null) ? position + 2 : 1)) {
        int lookup = (position != null) ? position + 2 : 1;
        Variable missedMainVar = missedMainVariables.get(lookup).getMiddle();
        LOGGER.warn("[FEASIBILITY CHECK] :: found possible main variable miss at pos {}: {}", lookup, missedMainVar);
        tmp_curr.getSemantics().setMainVariable(missedMainVar);
        LOGGER.warn("[FEASIBILITY CHECK] :: main variable temporarily set to: {}", missedMainVar);
        try {
          tmp_curr.adjunction(candidate, localTarget);
        } catch (LTAGException exc) {
          LOGGER.warn(exc.getMessage());
          return false;
        }
        tmp_curr.getSemantics().setMainVariable(null);
        LOGGER.warn("[FEASIBILITY CHECK] :: resetting main variable to NULL");
      } else {
        try {
          tmp_curr.adjunction(candidate, localTarget);
        } catch (LTAGException exc) {
          LOGGER.warn(exc.getMessage());
          return false;
        }
      }
    } else {
      LOGGER.debug("[FEASIBILITY CHECK] :: not feasibility for adjunction:\n{}", candidate.toPrettyString());
      return false;
    }

    return isOntologicallyFeasible(tmp_curr, ontology);
  }

  /**
   * Checks if {@code sltag} is ontologically feasible.
   * @param sltag the candidate.
   * @param ontology the ontology.
   * @return true, if {@code sltag} is ontologically feasible; false, otherwise.
   */
  private static boolean isOntologicallyFeasible(Sltag sltag, Ontology ontology) {
    Dudes dudes = sltag.getSemantics();
    Query query = dudes.convertToSPARQL();
    LOGGER.debug("[FEASIBILITY CHECK] :: candidate query:\n{}", query.toString());
    boolean feasible = KnowledgeManager.checkFeasibility2(ontology, QueryFactory.create(query));
    LOGGER.debug("[FEASIBILITY CHECK] :: candidate query {}", (feasible) ? "feasible" : "unfeasible");
    return feasible;
  }

}
