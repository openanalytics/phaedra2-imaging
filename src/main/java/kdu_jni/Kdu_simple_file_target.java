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

public class Kdu_simple_file_target extends Kdu_compressed_target {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected Kdu_simple_file_target(long ptr) {
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
  public Kdu_simple_file_target() {
    this(Native_create());
  }
  private static native long Native_create(String _fname, boolean _append_to_existing);
  public Kdu_simple_file_target(String _fname, boolean _append_to_existing) {
    this(Native_create(_fname, _append_to_existing));
  }
  private static long Native_create(String _fname)
  {
    return Native_create(_fname,(boolean) false);
  }
  public Kdu_simple_file_target(String _fname) {
    this(Native_create(_fname));
  }
  public native boolean Exists() throws KduException;
  public native boolean Open(String _fname, boolean _append_to_existing, boolean _return_on_failure) throws KduException;
  public boolean Open(String _fname) throws KduException
  {
    return Open(_fname,(boolean) false,(boolean) false);
  }
  public boolean Open(String _fname, boolean _append_to_existing) throws KduException
  {
    return Open(_fname,_append_to_existing,(boolean) false);
  }
  public native boolean Strip_tail(byte[] _buf, int _num_bytes) throws KduException;
}
