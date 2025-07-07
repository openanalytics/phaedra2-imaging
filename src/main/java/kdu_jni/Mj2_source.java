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

public class Mj2_source {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected Mj2_source(long ptr) {
    _native_ptr = ptr;
  }
  private native void Native_destroy();
  public void finalize() {
    if ((_native_ptr & 1) != 0)
      { // Resource created and not donated
        Native_destroy();
      }
  }
  private static native long Native_create();
  public Mj2_source() {
    this(Native_create());
  }
  public native boolean Exists() throws KduException;
  public native int Open(Jp2_family_src _src, boolean _return_if_incompatible) throws KduException;
  public int Open(Jp2_family_src _src) throws KduException
  {
    return Open(_src,(boolean) false);
  }
  public native void Close() throws KduException;
  public native Jp2_family_src Get_ultimate_src() throws KduException;
  public native Kdu_dims Get_movie_dims() throws KduException;
  public native long Get_next_track(long _prev_track_idx) throws KduException;
  public native int Get_track_type(long _track_idx) throws KduException;
  public native Mj2_video_source Access_video_track(long _track_idx) throws KduException;
  public native boolean Find_stream(int _stream_idx, long[] _track_idx, int[] _frame_idx, int[] _field_idx) throws KduException;
  public native boolean Count_codestreams(int[] _count) throws KduException;
}
