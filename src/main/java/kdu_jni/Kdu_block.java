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

public class Kdu_block {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  public native int Get_max_passes() throws KduException;
  public native void Set_max_passes(int _new_passes, boolean _copy_existing) throws KduException;
  public void Set_max_passes(int _new_passes) throws KduException
  {
    Set_max_passes(_new_passes,(boolean) true);
  }
  public native int Get_max_bytes() throws KduException;
  public native void Set_max_bytes(int _new_bytes, boolean _copy_existing) throws KduException;
  public void Set_max_bytes(int _new_bytes) throws KduException
  {
    Set_max_bytes(_new_bytes,(boolean) true);
  }
  public native void Set_max_samples(int _new_samples) throws KduException;
  public native void Set_max_contexts(int _new_contexts) throws KduException;
  public native int Map_storage(int _contexts, int _samples, int _retained_state) throws KduException;
  public native Kdu_coords Get_size() throws KduException;
  public native void Set_size(Kdu_coords _new_size) throws KduException;
  public native Kdu_dims Get_region() throws KduException;
  public native void Set_region(Kdu_dims _new_region) throws KduException;
  public native boolean Get_transpose() throws KduException;
  public native void Set_transpose(boolean _new_transpose) throws KduException;
  public native boolean Get_vflip() throws KduException;
  public native void Set_vflip(boolean _new_vflip) throws KduException;
  public native boolean Get_hflip() throws KduException;
  public native void Set_hflip(boolean _new_hflip) throws KduException;
  public native int Get_modes() throws KduException;
  public native void Set_modes(int _new_modes) throws KduException;
  public native int Get_orientation() throws KduException;
  public native void Set_orientation(int _new_orientation) throws KduException;
  public native int Get_missing_msbs() throws KduException;
  public native void Set_missing_msbs(int _new_msbs) throws KduException;
  public native int Get_num_passes() throws KduException;
  public native void Set_num_passes(int _new_passes) throws KduException;
  public native void Get_pass_lengths(int[] _buffer) throws KduException;
  public native void Set_pass_lengths(int[] _buffer) throws KduException;
  public native void Get_pass_slopes(int[] _buffer) throws KduException;
  public native void Set_pass_slopes(int[] _buffer) throws KduException;
  public native void Get_buffered_bytes(byte[] _buffer, int _first_idx, int _num_bytes) throws KduException;
  public native void Set_buffered_bytes(byte[] _buffer, int _first_idx, int _num_bytes) throws KduException;
  public native int Start_timing() throws KduException;
  public native void Finish_timing() throws KduException;
  public native void Initialize_timing(int _iterations) throws KduException;
  public native double Get_timing_stats(long[] _unique_samples, double[] _time_wasted) throws KduException;
}
