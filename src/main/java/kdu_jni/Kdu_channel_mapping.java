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

public class Kdu_channel_mapping {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected Kdu_channel_mapping(long ptr) {
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
  public Kdu_channel_mapping() {
    this(Native_create());
  }
  public native void Clear() throws KduException;
  public native boolean Configure(int _num_identical_channels, int _bit_depth, boolean _is_signed) throws KduException;
  public native boolean Configure(Kdu_codestream _codestream) throws KduException;
  public native boolean Configure(Jp2_colour _colour, Jp2_channels _channels, int _codestream_idx, Jp2_palette _palette, Jp2_dimensions _codestream_dimensions) throws KduException;
  public native boolean Configure(Jp2_source _jp2_in, boolean _ignore_alpha) throws KduException;
  public native boolean Add_alpha_to_configuration(Jp2_channels _channels, int _codestream_idx, Jp2_palette _palette, Jp2_dimensions _codestream_dimensions, boolean _ignore_premultiplied_alpha) throws KduException;
  public boolean Add_alpha_to_configuration(Jp2_channels _channels, int _codestream_idx, Jp2_palette _palette, Jp2_dimensions _codestream_dimensions) throws KduException
  {
    return Add_alpha_to_configuration(_channels,_codestream_idx,_palette,_codestream_dimensions,(boolean) true);
  }
  public native int Get_num_channels() throws KduException;
  public native void Set_num_channels(int _num) throws KduException;
  public native int Get_num_colour_channels() throws KduException;
  public native int Get_source_component(int _n) throws KduException;
  public native int Get_default_rendering_precision(int _n) throws KduException;
  public native boolean Get_default_rendering_signed(int _n) throws KduException;
  public native Jp2_colour_converter Get_colour_converter() throws KduException;
}
