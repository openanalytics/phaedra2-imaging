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

public class Jpx_source {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected Jpx_source(long ptr) {
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
  public Jpx_source() {
    this(Native_create());
  }
  public native boolean Exists() throws KduException;
  public native int Open(Jp2_family_src _src, boolean _return_if_incompatible) throws KduException;
  public native Jp2_family_src Get_ultimate_src() throws KduException;
  public native Jpx_compatibility Access_compatibility() throws KduException;
  public native Jp2_data_references Access_data_references() throws KduException;
  public native boolean Count_codestreams(int[] _count) throws KduException;
  public native boolean Count_compositing_layers(int[] _count) throws KduException;
  public native boolean Count_containers(int[] _count) throws KduException;
  public native Jpx_codestream_source Access_codestream(int _which, boolean _need_main_header) throws KduException;
  public Jpx_codestream_source Access_codestream(int _which) throws KduException
  {
    return Access_codestream(_which,(boolean) true);
  }
  public native Jpx_layer_source Access_layer(int _which, boolean _need_stream_headers) throws KduException;
  public Jpx_layer_source Access_layer(int _which) throws KduException
  {
    return Access_layer(_which,(boolean) true);
  }
  public native int Get_num_layer_codestreams(int _which_layer) throws KduException;
  public native int Get_layer_codestream_id(int _which_layer, int _which_stream) throws KduException;
  public native Jpx_container_source Access_container(int _which) throws KduException;
  public native Jpx_container_source Find_unique_compatible_container(int _num_codestreams, int[] _codestream_indices, int _num_compositing_layers, int[] _layer_indices) throws KduException;
  public native Jpx_composition Access_composition() throws KduException;
  public native int Generate_metareq(Kdu_window _client_window, int _min_frame_idx, int _max_frame_idx, int _max_layer_idx, int _max_codestream_idx, boolean _priority) throws KduException;
  public int Generate_metareq(Kdu_window _client_window) throws KduException
  {
    return Generate_metareq(_client_window,(int) 0,(int) -1,(int) -1,(int) -1,(boolean) true);
  }
  public int Generate_metareq(Kdu_window _client_window, int _min_frame_idx) throws KduException
  {
    return Generate_metareq(_client_window,_min_frame_idx,(int) -1,(int) -1,(int) -1,(boolean) true);
  }
  public int Generate_metareq(Kdu_window _client_window, int _min_frame_idx, int _max_frame_idx) throws KduException
  {
    return Generate_metareq(_client_window,_min_frame_idx,_max_frame_idx,(int) -1,(int) -1,(boolean) true);
  }
  public int Generate_metareq(Kdu_window _client_window, int _min_frame_idx, int _max_frame_idx, int _max_layer_idx) throws KduException
  {
    return Generate_metareq(_client_window,_min_frame_idx,_max_frame_idx,_max_layer_idx,(int) -1,(boolean) true);
  }
  public int Generate_metareq(Kdu_window _client_window, int _min_frame_idx, int _max_frame_idx, int _max_layer_idx, int _max_codestream_idx) throws KduException
  {
    return Generate_metareq(_client_window,_min_frame_idx,_max_frame_idx,_max_layer_idx,_max_codestream_idx,(boolean) true);
  }
  public native Jpx_meta_manager Access_meta_manager() throws KduException;
  public native boolean Close() throws KduException;
}
