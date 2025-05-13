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

public class Jpx_layer_target {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected Jpx_layer_target(long ptr) {
    _native_ptr = ptr;
  }
  public Jpx_layer_target() {
      this(0);
  }
  public native boolean Exists() throws KduException;
  public native int Get_layer_id() throws KduException;
  public native Jp2_channels Access_channels() throws KduException;
  public native Jp2_resolution Access_resolution() throws KduException;
  public native Jp2_colour Add_colour(int _prec, byte _approx) throws KduException;
  public Jp2_colour Add_colour() throws KduException
  {
    return Add_colour((int) 0,(byte) 0);
  }
  public Jp2_colour Add_colour(int _prec) throws KduException
  {
    return Add_colour(_prec,(byte) 0);
  }
  public native Jp2_colour Access_colour(int _which) throws KduException;
  public native void Set_codestream_registration(int _codestream_id, Kdu_coords _alignment, Kdu_coords _sampling, Kdu_coords _denominator) throws KduException;
  public native void Copy_attributes(Jpx_layer_source _src) throws KduException;
}
