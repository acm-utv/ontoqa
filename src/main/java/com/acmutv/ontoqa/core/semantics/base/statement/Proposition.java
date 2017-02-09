package com.acmutv.ontoqa.core.semantics.base.statement;

import com.acmutv.ontoqa.core.semantics.base.term.Term;
import com.acmutv.ontoqa.core.semantics.base.term.Terms;
import com.acmutv.ontoqa.core.semantics.base.term.Variable;
import com.acmutv.ontoqa.core.semantics.drs.Drs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementGroup;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class Proposition implements Statement {

  public static final String REGEXP = "^(\\w+)\\((\\w+)(,\\s*.+)*\\)$";

  private static final Pattern PATTERN = Pattern.compile(REGEXP);

  @NonNull
  private Term predicate;

  @NonNull
  private List<Term> arguments = new ArrayList<>();

  public Proposition(Term predicate, Term...arguments) {
    this.predicate = predicate;
    Collections.addAll(this.arguments, arguments);
  }

  @Override
  public Set<Integer> collectVariables() {
    HashSet<Integer> vars = new HashSet<>();
    if (predicate.isVariable()) {
      vars.add(((Variable) predicate).getI());
    }
    for (Term a : arguments) {
      if (a.isVariable()) {
        vars.add(((Variable) a).getI());
      }
    }

    return vars;
  }

  @Override
  public void union(Drs drs, int label) {
    //TODO
  }

  @Override
  public void rename(int i_old, int i_new) {
    this.predicate.rename(i_old, i_new);
    for (Term a : this.arguments) a.rename(i_old, i_new);
  }

  @Override
  public void rename(String s_old, String s_new) {
    this.predicate.rename(s_old, s_new);
    for (Term a : this.arguments) a.rename(s_old, s_new);
  }

  @Override
  public void replace(Term t_old, Term t_new) {
    this.predicate = this.predicate.replace(t_old,t_new);

    List<Term> new_arguments = new ArrayList<>();
    for (Term a : this.arguments) {
         new_arguments.add(a.replace(t_old,t_new));
    }
    this.arguments = new_arguments;
  }

  @Override
  public Set<Replace> collectReplacements() {
    return new HashSet<>();
  }

  @Override
  public Element convertToRDF(Query top) {
    ElementGroup group = new ElementGroup();

    if (this.arguments.size() == 2) {
      group.addTriplePattern(
          new Triple(
              this.arguments.get(0).convertToNode(top),
              this.predicate.convertToNode(top),
              this.arguments.get(1).convertToNode(top)
          )
      );
    }

    if (group.getElements().size() == 1) {
        return group.getElements().get(0);
    } else {
        return group;
    }
  }

  @Override
  public String toString() {
    return String.format("%s(%s)",
        this.predicate.toString(),
        this.arguments.stream().map(String::valueOf).collect(Collectors.joining(",")));
  }

  /**
   * Parses {@link Proposition} from string.
   * @param string the string to parse.
   * @return the parsed {@link Proposition}.
   * @throws IllegalArgumentException when {@code string} cannot be parsed.
   */
  public static Proposition valueOf(String string) throws IllegalArgumentException{
    if (string == null) throw new IllegalArgumentException();
    Matcher matcher = PATTERN.matcher(string);
    if (!matcher.matches()) throw new IllegalArgumentException();;
    String strPredicate = matcher.group(1);
    String strTerms[] = matcher.group(2).trim().split(",");
    Term predicate = Terms.valueOf(strPredicate);
    List<Term> terms = new ArrayList<>();
    for (String strTerm : strTerms) {
      Term term = Terms.valueOf(strTerm);
      terms.add(term);
    }
    return new Proposition(predicate, terms);
  }

  @Override
  public Proposition clone() {
    List<Term> clonedArguments = new ArrayList<>();
    for (Term a : this.arguments) {
         clonedArguments.add(a.clone());
    }
    return new Proposition(predicate.clone(), clonedArguments);
  }
    
}
