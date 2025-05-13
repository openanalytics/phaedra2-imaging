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

public class Kdu_codestream_comment {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected Kdu_codestream_comment(long ptr) {
    _native_ptr = ptr;
  }
  public Kdu_codestream_comment() {
      this(0);
  }
  public native boolean Exists() throws KduException;
  public native String Get_text() throws KduException;
  public native int Get_data(byte[] _buf, int _offset, int _length) throws KduException;
  public native boolean Check_readonly() throws KduException;
  public native boolean Put_data(byte[] _data, int _num_bytes) throws KduException;
  public native boolean Put_text(String _string) throws KduException;
}
