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

public class Kdu_compressed_video_source extends Kdu_compressed_source {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected Kdu_compressed_video_source(long ptr) {
    super(ptr);
  }
  public native void Native_destroy();
  public void finalize() {
    if ((_native_ptr & 1) != 0)
      { // Resource created and not donated
        Native_destroy();
      }
  }
  public native long Get_timescale() throws KduException;
  public native int Get_field_order() throws KduException;
  public native void Set_field_mode(int _which) throws KduException;
  public native int Get_num_frames() throws KduException;
  public native boolean Seek_to_frame(int _frame_idx) throws KduException;
  public native long Get_duration() throws KduException;
  public native int Time_to_frame(long _time_instant) throws KduException;
  public native long Get_frame_instant() throws KduException;
  public native long Get_frame_period() throws KduException;
  public native int Open_image() throws KduException;
  public native int Open_stream(int _field_idx, Jp2_input_box _input_box) throws KduException;
  public native void Close_image() throws KduException;
}
