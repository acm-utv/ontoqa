package com.acmutv.ontoqa.core.semantics.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.nodevalue.NodeValueInteger;
import org.apache.jena.sparql.expr.nodevalue.NodeValueNode;
import org.apache.jena.sparql.expr.nodevalue.NodeValueString;

@Data
@AllArgsConstructor
public class Constant implements Term {

  public enum Datatype {
    URI,
    STRING,
    INT,
    DATE,
    BOOLEAN,
    NONE
  }

  @NonNull
  private String value;

  private Datatype type = Datatype.URI;

  public Constant(String value) {
    this.value = value;
  }

  @Override
  public boolean isVariable() {
      return false;
  }

  @Override
  public boolean isFunction() {
      return false;
  }

  @Override
  public void rename(int i_old, int i_new) {
  }

  @Override
  public void rename(String s_old, String s_new) {
    if (this.value.equals(s_old)) this.value = s_new;
  }

  @Override
  public Term replace(Term t_old, Term t_new) {
    if (this.equals(t_old)) {
      return t_new;
    } else {
      return this;
    }
  }

  @Override
  public Node convertToNode(Query top) {
    Node node;

    switch (type) {
      case URI:
        node = NodeFactory.createURI(this.value); break;
      case STRING:
        node = NodeFactory.createLiteral(this.value); break;
      case INT:
        node = NodeFactory.createLiteral(this.value, NodeFactory.getType("http://www.w3.org/2001/XMLSchema#int")); break;
      case DATE:
        node = NodeFactory.createLiteral(this.value, NodeFactory.getType("http://www.w3.org/2001/XMLSchema#date")); break;
      case BOOLEAN:
        node = NodeFactory.createLiteral(this.value, NodeFactory.getType("http://www.w3.org/2001/XMLSchema#boolean")); break;
      default:
        node = NodeFactory.createLiteral(this.value); break;
    }

    return node;
  }

  @Override
  public Expr convertToExpr(Query top) {
    Expr expr;

    switch (type) {
      case STRING:
        expr = new NodeValueString(this.value); break;
      case INT:
        expr = new NodeValueInteger(Long.parseLong(this.value)); break;
      default:
        expr = new NodeValueNode(this.convertToNode(top)); break;
    }

    return expr;
  }

  @Override
  public String toString() {
      return this.value;
  }

  @Override
  public Constant clone() {
      return new Constant(this.value, this.type);
  }
    
}
