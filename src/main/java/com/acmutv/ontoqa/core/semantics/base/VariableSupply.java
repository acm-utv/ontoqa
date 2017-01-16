package com.acmutv.ontoqa.core.semantics.base;

public class VariableSupply {
    
  int fresh = 0;

  public int getFresh() {
    this.fresh++;
    return this.fresh;
  }

  public void reset() {
    this.reset(0);
  }

  public void reset(int i) {
    this.fresh = i;
  }
    
}
