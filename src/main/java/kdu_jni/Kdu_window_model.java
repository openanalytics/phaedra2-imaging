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

public class Kdu_window_model {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected Kdu_window_model(long ptr) {
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
  public Kdu_window_model() {
    this(Native_create());
  }
  private static native long Native_create(Kdu_window_model _src);
  public Kdu_window_model(Kdu_window_model _src) {
    this(Native_create(_src));
  }
  public native void Copy_from(Kdu_window_model _src) throws KduException;
  public native void Clear() throws KduException;
  public native void Init(boolean _stateless) throws KduException;
  public native void Init(boolean _stateless, boolean _background_full, int _default_stream_idx) throws KduException;
  public native boolean Is_stateless() throws KduException;
  public native boolean Is_empty() throws KduException;
  public native void Append(Kdu_window_model _src) throws KduException;
  public native void Set_codestream_context(int _stream_min, int _stream_max) throws KduException;
  public native void Add_instruction(int _databin_class, long _bin_id, int _flags, int _limit) throws KduException;
  public native void Add_instruction(int _tmin, int _tmax, int _cmin, int _cmax, int _rmin, int _rmax, long _pmin, long _pmax, int _flags, int _limit) throws KduException;
  public native int Get_meta_instructions(long[] _bin_id, int[] _buf) throws KduException;
  public native int Get_first_atomic_stream() throws KduException;
  public native int Get_header_instructions(int _stream_idx, int _tnum, int[] _buf) throws KduException;
  public native int Get_header_instructions(int _stream_idx, int[] _tnum, int[] _buf) throws KduException;
  public native int Get_precinct_instructions(int _stream_idx, int[] _tnum, int[] _cnum, int[] _rnum, long[] _pnum, int[] _buf) throws KduException;
  public native boolean Get_precinct_block(int _stream_idx, int _tnum, int _cnum, int _rnum, int _t_across, int _p_across, long _id_base, long _id_gap, Kdu_dims _region, int[] _buf) throws KduException;
}
