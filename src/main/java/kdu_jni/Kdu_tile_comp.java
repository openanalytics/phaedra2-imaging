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

public class Kdu_tile_comp {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected Kdu_tile_comp(long ptr) {
    _native_ptr = ptr;
  }
  public Kdu_tile_comp() {
      this(0);
  }
  public native boolean Exists() throws KduException;
  public native boolean Get_reversible() throws KduException;
  public native void Get_subsampling(Kdu_coords _factors) throws KduException;
  public native int Get_bit_depth(boolean _internal) throws KduException;
  public int Get_bit_depth() throws KduException
  {
    return Get_bit_depth((boolean) false);
  }
  public native boolean Get_signed() throws KduException;
  public native int Get_num_resolutions() throws KduException;
  public native Kdu_resolution Access_resolution(int _res_level) throws KduException;
  public native Kdu_resolution Access_resolution() throws KduException;
}
