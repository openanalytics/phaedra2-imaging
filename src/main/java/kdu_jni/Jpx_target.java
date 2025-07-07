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

public class Jpx_target {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected Jpx_target(long ptr) {
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
  public Jpx_target() {
    this(Native_create());
  }
  public native boolean Exists() throws KduException;
  public native void Open(Jp2_family_tgt _tgt) throws KduException;
  public native Jpx_compatibility Access_compatibility() throws KduException;
  public native Jp2_data_references Access_data_references() throws KduException;
  public native Jpx_codestream_target Add_codestream() throws KduException;
  public native Jpx_layer_target Add_layer() throws KduException;
  public native Jpx_composition Access_composition() throws KduException;
  public native void Expect_containers() throws KduException;
  public native Jpx_container_target Add_container(int _num_base_codestreams, int _num_base_layers, int _repetition_factor) throws KduException;
  public native Jpx_container_target Access_container(int _which) throws KduException;
  public native void Configure_codestream_aggregation(int _min_j2cx_streams, int _max_j2cx_streams) throws KduException;
  public native Jpx_meta_manager Access_meta_manager() throws KduException;
  public native Jp2_output_box Write_headers(int[] _i_param) throws KduException;
  public Jp2_output_box Write_headers() throws KduException
  {
    return Write_headers(null);
  }
  public native Jp2_output_box Write_metadata(int[] _i_param) throws KduException;
  public Jp2_output_box Write_metadata() throws KduException
  {
    return Write_metadata(null);
  }
  public native boolean Close() throws KduException;
}
