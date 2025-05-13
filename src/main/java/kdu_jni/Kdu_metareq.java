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

public class Kdu_metareq {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected Kdu_metareq(long ptr) {
    _native_ptr = ptr;
  }
  private native void Native_destroy();
  public void finalize() {
    if ((_native_ptr & 1) != 0)
      { // Resource created and not donated
        Native_destroy();
      }
  }
  public native boolean Equals(Kdu_metareq _rhs) throws KduException;
  public native long Get_box_type() throws KduException;
  public native int Get_qualifier() throws KduException;
  public native boolean Get_priority() throws KduException;
  public native int Get_byte_limit() throws KduException;
  public native boolean Get_recurse() throws KduException;
  public native long Get_root_bin_id() throws KduException;
  public native int Get_max_depth() throws KduException;
  public native Kdu_metareq Get_next() throws KduException;
}
