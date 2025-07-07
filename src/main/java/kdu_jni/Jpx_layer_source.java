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

public class Jpx_layer_source {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected long _native_param = 0;
  protected Jpx_layer_source(long ptr, long param) {
    _native_ptr = ptr;
    _native_param = param;
  }
  public Jpx_layer_source() {
      this(0,0);
  }
  public native boolean Exists() throws KduException;
  public native int Get_layer_id() throws KduException;
  public native Jp2_locator Get_header_loc() throws KduException;
  public native Jp2_channels Access_channels() throws KduException;
  public native Jp2_resolution Access_resolution() throws KduException;
  public native Jp2_colour Access_colour(int _which) throws KduException;
  public native int Get_num_codestreams() throws KduException;
  public native int Get_codestream_id(int _which) throws KduException;
  public native Kdu_coords Get_layer_size() throws KduException;
  public native boolean Have_stream_headers() throws KduException;
  public native int Get_codestream_registration(int _which, Kdu_coords _alignment, Kdu_coords _sampling, Kdu_coords _denominator) throws KduException;
}
