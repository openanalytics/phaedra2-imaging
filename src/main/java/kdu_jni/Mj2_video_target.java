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

public class Mj2_video_target extends Kdu_compressed_video_target {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected Mj2_video_target(long ptr) {
    super(ptr);
  }
  public native void Native_destroy();
  public void finalize() {
    if ((_native_ptr & 1) != 0)
      { // Resource created and not donated
        Native_destroy();
      }
  }
  public native long Get_track_idx() throws KduException;
  public native void Set_compositing_order(short _layer_idx) throws KduException;
  public native void Set_graphics_mode(short _graphics_mode, short _op_red, short _op_green, short _op_blue) throws KduException;
  public void Set_graphics_mode(short _graphics_mode) throws KduException
  {
    Set_graphics_mode(_graphics_mode,(short) 0,(short) 0,(short) 0);
  }
  public void Set_graphics_mode(short _graphics_mode, short _op_red) throws KduException
  {
    Set_graphics_mode(_graphics_mode,_op_red,(short) 0,(short) 0);
  }
  public void Set_graphics_mode(short _graphics_mode, short _op_red, short _op_green) throws KduException
  {
    Set_graphics_mode(_graphics_mode,_op_red,_op_green,(short) 0);
  }
  public native Jp2_colour Access_colour() throws KduException;
  public native Jp2_palette Access_palette() throws KduException;
  public native Jp2_channels Access_channels() throws KduException;
  public native Jp2_resolution Access_resolution() throws KduException;
  public native void Set_timescale(long _ticks_per_second) throws KduException;
  public native void Set_field_order(int _order) throws KduException;
  public native void Set_max_frames_per_chunk(long _max_frames) throws KduException;
  public native void Set_frame_period(long _num_ticks) throws KduException;
}
