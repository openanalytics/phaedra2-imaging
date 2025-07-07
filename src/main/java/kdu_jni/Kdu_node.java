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

public class Kdu_node {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected Kdu_node(long ptr) {
    _native_ptr = ptr;
  }
  public Kdu_node() {
      this(0);
  }
  public native boolean Exists() throws KduException;
  public native boolean Compare(Kdu_node _src) throws KduException;
  public native Kdu_node Access_child(int _child_idx) throws KduException;
  public native int Get_directions() throws KduException;
  public native int Get_num_descendants(int[] _num_leaf_descendants) throws KduException;
  public native Kdu_subband Access_subband() throws KduException;
  public native Kdu_resolution Access_resolution() throws KduException;
  public native void Get_dims(Kdu_dims _dims) throws KduException;
  public native int Get_kernel_id() throws KduException;
  public native long Get_kernel_coefficients(boolean _vertical) throws KduException;
  public native long Get_bibo_gains(int[] _num_steps, boolean _vertical) throws KduException;
}
