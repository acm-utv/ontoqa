package com.acmutv.ontoqa.core.semantics.base;


import lombok.Data;
import lombok.NonNull;
import org.apache.jena.graph.Node;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprAggregator;
import org.apache.jena.sparql.expr.aggregate.AggCountVarDistinct;

@Data
public class Function implements Term {

    public enum Func { COUNT }

  @NonNull
    private Func function;

    @NonNull
    private Term term;

   @Override
    public boolean isVariable() {
        return false;
    }

    @Override
    public boolean isFunction() {
        return true;
    }

    @Override
    public void rename(int i_old, int i_new) {
        this.getTerm().rename(i_old,i_new);
    }

    @Override
    public void rename(String s_old, String s_new) {
        this.getTerm().rename(s_old,s_new);
    }

    @Override
    public Term replace(Term t_old, Term t_new) {
        if (this.equals(t_old)) {
          return t_new;
        }
        if (this.getTerm().equals(t_old)) {
          this.term = t_new;
        }

        return this;
    }
    
    @Override
    public Node convertToNode(Query top) {
        
        return this.getTerm().convertToNode(top);
    }
    
    @Override
    public Expr convertToExpr(Query top) {
        if (this.getTerm().isVariable()) {
          return new ExprAggregator(null,
              new AggCountVarDistinct(this.getTerm().convertToExpr(top)));
        }
            
        return null;
    }
    
    @Override
    public String toString() {
        return this.getFunction() + "(" + this.getTerm().toString() + ")";
    }
    
    @Override
    public Function clone() {
        return new Function(this.getFunction(), this.getTerm().clone());
    }
    
}
