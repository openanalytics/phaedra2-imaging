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

public class Jp2_output_box extends Kdu_compressed_target {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected Jp2_output_box(long ptr) {
    super(ptr);
  }
  public native void Native_destroy();
  public void finalize() {
    if ((_native_ptr & 1) != 0)
      { // Resource created and not donated
        Native_destroy();
      }
  }
  private static native long Native_create();
  public Jp2_output_box() {
    this(Native_create());
  }
  public native boolean Exists() throws KduException;
  public native void Open(Jp2_family_tgt _tgt, long _box_type, boolean _rubber_length, boolean _headerless) throws KduException;
  public void Open(Jp2_family_tgt _tgt, long _box_type) throws KduException
  {
    Open(_tgt,_box_type,(boolean) false,(boolean) false);
  }
  public void Open(Jp2_family_tgt _tgt, long _box_type, boolean _rubber_length) throws KduException
  {
    Open(_tgt,_box_type,_rubber_length,(boolean) false);
  }
  public native void Open(Jp2_output_box _super_box, long _box_type, boolean _rubber_length, boolean _headerless) throws KduException;
  public void Open(Jp2_output_box _super_box, long _box_type) throws KduException
  {
    Open(_super_box,_box_type,(boolean) false,(boolean) false);
  }
  public void Open(Jp2_output_box _super_box, long _box_type, boolean _rubber_length) throws KduException
  {
    Open(_super_box,_box_type,_rubber_length,(boolean) false);
  }
  public native void Open(long _box_type) throws KduException;
  public native void Open_next(long _box_type, boolean _rubber_length, boolean _headerless) throws KduException;
  public void Open_next(long _box_type) throws KduException
  {
    Open_next(_box_type,(boolean) false,(boolean) false);
  }
  public void Open_next(long _box_type, boolean _rubber_length) throws KduException
  {
    Open_next(_box_type,_rubber_length,(boolean) false);
  }
  public native long Get_box_type() throws KduException;
  public native long Get_box_length() throws KduException;
  public native long Get_start_pos() throws KduException;
  public native int Get_header_length() throws KduException;
  public native int Use_long_header() throws KduException;
  public native long Get_contents(long[] _contents_length) throws KduException;
  public native long Write_box(Jp2_family_tgt _tgt, boolean _force_headerless) throws KduException;
  public native void Set_rubber_length() throws KduException;
  public native void Write_header_last() throws KduException;
  public native long Reopen(long _box_type, long _offset) throws KduException;
  public native boolean Write_free_and_close(long _free_bytes) throws KduException;
  public native boolean Write(long _dword) throws KduException;
  public native boolean Write(int _dword) throws KduException;
  public native boolean Write(short _word) throws KduException;
  public native boolean Write(byte _byte) throws KduException;
}
