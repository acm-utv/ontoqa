package com.acmutv.ontoqa.core.semantics.base.statement;

import com.acmutv.ontoqa.core.semantics.base.term.*;
import com.acmutv.ontoqa.core.semantics.drs.Drs;
import lombok.Data;
import lombok.NonNull;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementGroup;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class Replace implements Statement {

  public static final String REGEXP = "^REPLACE\\((.+),(.+)\\)$";

  private static final Pattern PATTERN = Pattern.compile(REGEXP);

  @NonNull
  private Term source;

  @NonNull
  private Term target;

  @Override
  public Set<Integer> collectVariables() {
    HashSet<Integer> vars = new HashSet<>();
    if (this.source.isVariable()) {
      vars.add(((Variable) this.source).getI());
    }
    if (this.target.isVariable()) {
      vars.add(((Variable) this.target).getI());
    }
    return vars;
  }

  @Override
  public void union(Drs drs, int label) {
    //TODO
  }

  @Override
  public void rename(int i_old, int i_new) {
    this.source.rename(i_old,i_new);
    this.target.rename(i_old,i_new);
  }

  @Override
  public void rename(String s_old, String s_new) {
    this.source.rename(s_old, s_new);
    this.target.rename(s_old, s_new);
  }

  @Override
  public void replace(Term t_old, Term t_new) {
    if (this.source.equals(t_old)) {
      this.source = t_new;
    }
    if (this.target.equals(t_old)) {
      this.target = t_new;
    }
  }

  @Override
  public Set<Replace> collectReplacements() {
    Set<Replace> replacements = new HashSet<>();
    replacements.add(this);
    return replacements;
  }

  @Override
  public Element convertToRDF(Query top) {
    ElementGroup group = new ElementGroup();
    group.addTriplePattern(
        new Triple(
            this.source.convertToNode(top),
            NodeFactory.createURI("http://www.w3.org/2002/07/owl#sameAs"),
            this.target.convertToNode(top)
        )
    );

    if (group.getElements().size() == 1) {
      return group.getElements().get(0);
    } else {
      return group;
    }
  }

  @Override
  public String toString() {
      return "REPLACE(" + this.source.toString() + "," + this.target.toString() + ")";
  }

  /**
   * Parses {@link Replace} from string.
   * @param string the string to parse.
   * @return the parsed {@link Replace}.
   * @throws IllegalArgumentException when {@code string} cannot be parsed.
   */
  public static Replace valueOf(String string) throws IllegalArgumentException {
    if (string == null) throw new IllegalArgumentException();
    Matcher matcher = PATTERN.matcher(string);
    if (!matcher.matches()) throw new IllegalArgumentException();
    String strTerm1 = matcher.group(1);
    String strTerm2 = matcher.group(2);
    Term term1 = Terms.valueOf(strTerm1);
    Term term2 = Terms.valueOf(strTerm2);
    return new Replace(term1, term2);
  }

  @Override
  public Replace clone() {
      return new Replace(this.source.clone(), this.target.clone());
  }
        
}
