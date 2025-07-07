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

public class Jpx_fragment_list {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected Jpx_fragment_list(long ptr) {
    _native_ptr = ptr;
  }
  public Jpx_fragment_list() {
      this(0);
  }
  public native boolean Exists() throws KduException;
  public native void Add_fragment(int _url_idx, long _offset, long _length) throws KduException;
  public native long Get_total_length() throws KduException;
  public native int Get_num_fragments() throws KduException;
  public native boolean Get_fragment(int _frag_idx, int[] _url_idx, long[] _offset, long[] _length) throws KduException;
  public native int Locate_fragment(long _pos, long[] _bytes_into_fragment) throws KduException;
  public native boolean Any_local_fragments() throws KduException;
}
