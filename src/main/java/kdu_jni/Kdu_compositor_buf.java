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

public class Kdu_compositor_buf {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected Kdu_compositor_buf(long ptr) {
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
  public Kdu_compositor_buf() {
    this(Native_create());
  }
  public native void Init(long _buf, int _row_gap) throws KduException;
  public native void Init_float(long _float_buf, int _row_gap) throws KduException;
  public native boolean Is_read_access_allowed() throws KduException;
  public native boolean Set_read_accessibility(boolean _read_access_required) throws KduException;
  public native long Get_buf(int[] _row_gap, boolean _read_write) throws KduException;
  public native long Get_float_buf(int[] _row_gap, boolean _read_write) throws KduException;
  public native boolean Get_region(Kdu_dims _src_region, int[] _tgt_buf, int _tgt_offset, int _tgt_row_gap) throws KduException;
  public boolean Get_region(Kdu_dims _src_region, int[] _tgt_buf) throws KduException
  {
    return Get_region(_src_region,_tgt_buf,(int) 0,(int) 0);
  }
  public boolean Get_region(Kdu_dims _src_region, int[] _tgt_buf, int _tgt_offset) throws KduException
  {
    return Get_region(_src_region,_tgt_buf,_tgt_offset,(int) 0);
  }
  public native boolean Get_float_region(Kdu_dims _src_region, float[] _tgt_buf, int _tgt_offset, int _tgt_row_gap) throws KduException;
  public boolean Get_float_region(Kdu_dims _src_region, float[] _tgt_buf) throws KduException
  {
    return Get_float_region(_src_region,_tgt_buf,(int) 0,(int) 0);
  }
  public boolean Get_float_region(Kdu_dims _src_region, float[] _tgt_buf, int _tgt_offset) throws KduException
  {
    return Get_float_region(_src_region,_tgt_buf,_tgt_offset,(int) 0);
  }
}
