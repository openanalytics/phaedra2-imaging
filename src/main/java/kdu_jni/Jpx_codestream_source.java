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

public class Jpx_codestream_source {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected long _native_param = 0;
  protected Jpx_codestream_source(long ptr, long param) {
    _native_ptr = ptr;
    _native_param = param;
  }
  public Jpx_codestream_source() {
      this(0,0);
  }
  public native boolean Exists() throws KduException;
  public native int Get_codestream_id() throws KduException;
  public native Jp2_locator Get_header_loc() throws KduException;
  public native Jp2_dimensions Access_dimensions(boolean _finalize_compatibility) throws KduException;
  public Jp2_dimensions Access_dimensions() throws KduException
  {
    return Access_dimensions((boolean) false);
  }
  public native Jp2_palette Access_palette() throws KduException;
  public native int Enum_layer_ids(int _last_layer_id) throws KduException;
  public int Enum_layer_ids() throws KduException
  {
    return Enum_layer_ids((int) -1);
  }
  public native boolean Stream_ready() throws KduException;
  public native Jpx_fragment_list Access_fragment_list() throws KduException;
  public native Jpx_input_box Open_stream(Jpx_input_box _my_resource) throws KduException;
  public Jpx_input_box Open_stream() throws KduException
  {
    Jpx_input_box my_resource = null;
    return Open_stream(my_resource);
  }
}
