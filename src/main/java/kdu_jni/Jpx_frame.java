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

public class Jpx_frame {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected long _native_param = 0;
  protected Jpx_frame(long ptr, long param) {
    _native_ptr = ptr;
    _native_param = param;
  }
  public Jpx_frame() {
      this(0,0);
  }
  public native boolean Exists() throws KduException;
  public native boolean Equals(Jpx_frame _rhs) throws KduException;
  public native int Get_global_info(Kdu_coords _size) throws KduException;
  public native int Get_frame_idx() throws KduException;
  public native long Get_track_idx(boolean[] _last_in_context) throws KduException;
  public native Jpx_frame Access_next(long _track_idx, boolean _must_exist) throws KduException;
  public native Jpx_frame Access_prev(long _track_idx, boolean _must_exist) throws KduException;
  public native int Get_info(long[] _start_time, long[] _duration) throws KduException;
  public native boolean Is_persistent() throws KduException;
  public native int Get_num_persistent_instructions() throws KduException;
  public native boolean Get_instruction(int _instruction_idx, int[] _layer_idx, Kdu_dims _source_dims, Kdu_dims _target_dims, Jpx_composited_orientation _orientation) throws KduException;
  public native int Find_last_instruction_for_layer(int _layer_idx, int _lim_inst_idx) throws KduException;
  public int Find_last_instruction_for_layer(int _layer_idx) throws KduException
  {
    return Find_last_instruction_for_layer(_layer_idx,(int) 0);
  }
  public native boolean Get_original_iset(int _instruction_idx, int[] _iset_idx, int[] _inum_idx) throws KduException;
  public native long Get_old_ref(int[] _instruction_idx, int[] _instance_idx) throws KduException;
}
