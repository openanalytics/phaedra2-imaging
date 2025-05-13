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

public class Jp2_palette {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected Jp2_palette(long ptr) {
    _native_ptr = ptr;
  }
  public Jp2_palette() {
      this(0);
  }
  public native boolean Exists() throws KduException;
  public native void Copy(Jp2_palette _src) throws KduException;
  public native void Init(int _num_luts, int _num_entries) throws KduException;
  public native boolean Set_lut(int _lut_idx, int[] _lut, int _bit_depth, boolean _is_signed) throws KduException;
  public boolean Set_lut(int _lut_idx, int[] _lut, int _bit_depth) throws KduException
  {
    return Set_lut(_lut_idx,_lut,_bit_depth,(boolean) false);
  }
  public native int Get_num_entries() throws KduException;
  public native int Get_num_luts() throws KduException;
  public native int Get_bit_depth(int _lut_idx) throws KduException;
  public native boolean Get_signed(int _lut_idx) throws KduException;
  public native boolean Get_lut(int _lut_idx, float[] _lut) throws KduException;
}
