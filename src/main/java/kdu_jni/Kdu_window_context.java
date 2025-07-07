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

public class Kdu_window_context {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected long _native_param = 0;
  protected Kdu_window_context(long ptr, long param) {
    _native_ptr = ptr;
    _native_param = param;
  }
  public Kdu_window_context() {
      this(0,0);
  }
  public native boolean Exists() throws KduException;
  public native int Get_num_members(int[] _remapping_ids) throws KduException;
  public native int Get_codestream(int[] _remapping_ids, int _member_idx) throws KduException;
  public native long Get_components(int[] _remapping_ids, int _member_idx, int[] _num_components) throws KduException;
  public native boolean Perform_remapping(int[] _remapping_ids, int _member_idx, Kdu_coords _resolution, Kdu_dims _region) throws KduException;
}
