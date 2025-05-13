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

public class Jpx_composited_orientation {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected Jpx_composited_orientation(long ptr) {
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
  public Jpx_composited_orientation() {
    this(Native_create());
  }
  private static native long Native_create(boolean _transpose_first, boolean _flip_vert, boolean _flip_horz);
  public Jpx_composited_orientation(boolean _transpose_first, boolean _flip_vert, boolean _flip_horz) {
    this(Native_create(_transpose_first, _flip_vert, _flip_horz));
  }
  public native boolean Is_non_trivial() throws KduException;
  public native boolean Equals(Jpx_composited_orientation _rhs) throws KduException;
  public native void Init(boolean _transpose_first, boolean _flip_vert, boolean _flip_horz) throws KduException;
  public native void Init(int _rotation, boolean _flip) throws KduException;
  public native void Append(Jpx_composited_orientation _rhs) throws KduException;
}
