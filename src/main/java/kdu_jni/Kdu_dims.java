/**
 * Phaedra II
 *
 * Copyright (C) 2016-2025 Open Analytics
 *
 * ===========================================================================
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Apache License as published by
 * The Apache Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Apache License for more details.
 *
 * You should have received a copy of the Apache License
 * along with this program.  If not, see <http://www.apache.org/licenses/>
 */
package kdu_jni;

public class Kdu_dims {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected Kdu_dims(long ptr) {
    _native_ptr = ptr;
  }
  public native void Native_destroy();
  public void finalize() {
    if ((_native_ptr & 1) != 0)
      { // Resource created and not donated
        Native_destroy();
      }
  }
  private static native long Native_create();
  public Kdu_dims() {
    this(Native_create());
  }
  public native void Assign(Kdu_dims _src) throws KduException;
  public native Kdu_coords Access_pos() throws KduException;
  public native Kdu_coords Access_size() throws KduException;
  public native long Area() throws KduException;
  public native void Transpose() throws KduException;
  public native Kdu_dims Intersection(Kdu_dims _rhs) throws KduException;
  public native boolean Intersects(Kdu_dims _rhs) throws KduException;
  public native boolean Is_empty() throws KduException;
  public native boolean Equals(Kdu_dims _rhs) throws KduException;
  public native boolean Contains(Kdu_coords _rhs) throws KduException;
  public native boolean Contains(Kdu_dims _rhs) throws KduException;
  public native void Augment(Kdu_coords _p) throws KduException;
  public native void Augment(Kdu_dims _src) throws KduException;
  public native boolean Clip_point(Kdu_coords _pt) throws KduException;
  public native void From_apparent(boolean _transp, boolean _vflip, boolean _hflip) throws KduException;
  public native void To_apparent(boolean _transp, boolean _vflip, boolean _hflip) throws KduException;
}
