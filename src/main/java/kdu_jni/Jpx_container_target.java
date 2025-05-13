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

public class Jpx_container_target {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected Jpx_container_target(long ptr) {
    _native_ptr = ptr;
  }
  public Jpx_container_target() {
      this(0);
  }
  public native boolean Exists() throws KduException;
  public native int Get_container_id() throws KduException;
  public native int Get_num_top_codestreams() throws KduException;
  public native int Get_num_top_layers() throws KduException;
  public native int Get_base_codestreams(int[] _num_base_codestreams) throws KduException;
  public native int Get_base_layers(int[] _num_base_layers) throws KduException;
  public native Jpx_layer_target Access_layer(int _which) throws KduException;
  public native Jpx_codestream_target Access_codestream(int _which) throws KduException;
  public native Jpx_composition Add_presentation_track(int _track_layers) throws KduException;
}
