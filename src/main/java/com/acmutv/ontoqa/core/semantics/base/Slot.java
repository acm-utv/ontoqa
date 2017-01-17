package com.acmutv.ontoqa.core.semantics.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class Slot {

  @NonNull
  private Variable variable;

  private String anchor = "*";

  private int label = 0;

  public Slot(Variable variable, String anchor) {
    this.variable = variable;
    this.anchor = anchor;
  }

  public void replace(int i_old, int i_new) {
    this.variable.rename(i_old,i_new);
    if (this.label == i_old) {
      this.label = i_new;
    }
  }

  @Override
  public String toString() {
    return "(" + this.variable.toString() + "," + this.anchor + "," + this.label + ")";
  }

  @Override
  public Slot clone() {
    return new Slot(new Variable(this.variable.getI()),this.anchor,this.label);
  }
    
}
