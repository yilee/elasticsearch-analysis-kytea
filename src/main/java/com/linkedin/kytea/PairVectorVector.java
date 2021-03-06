/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.7
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.linkedin.kytea;

public class PairVectorVector {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected PairVectorVector(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(PairVectorVector obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        LiKyteaJNI.delete_PairVectorVector(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public PairVectorVector() {
    this(LiKyteaJNI.new_PairVectorVector__SWIG_0(), true);
  }

  public PairVectorVector(long n) {
    this(LiKyteaJNI.new_PairVectorVector__SWIG_1(n), true);
  }

  public long size() {
    return LiKyteaJNI.PairVectorVector_size(swigCPtr, this);
  }

  public long capacity() {
    return LiKyteaJNI.PairVectorVector_capacity(swigCPtr, this);
  }

  public void reserve(long n) {
    LiKyteaJNI.PairVectorVector_reserve(swigCPtr, this, n);
  }

  public boolean isEmpty() {
    return LiKyteaJNI.PairVectorVector_isEmpty(swigCPtr, this);
  }

  public void clear() {
    LiKyteaJNI.PairVectorVector_clear(swigCPtr, this);
  }

  public void add(PairVector x) {
    LiKyteaJNI.PairVectorVector_add(swigCPtr, this, PairVector.getCPtr(x), x);
  }

  public PairVector get(int i) {
    return new PairVector(LiKyteaJNI.PairVectorVector_get(swigCPtr, this, i), false);
  }

  public void set(int i, PairVector val) {
    LiKyteaJNI.PairVectorVector_set(swigCPtr, this, i, PairVector.getCPtr(val), val);
  }

}
