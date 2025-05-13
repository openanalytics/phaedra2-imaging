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

public class Kdu_compressed_target_nonnative extends Kdu_compressed_target {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected Kdu_compressed_target_nonnative(long ptr) {
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
  private native void Native_init();
  public Kdu_compressed_target_nonnative() {
    this(Native_create());
    this.Native_init();
  }
  public int Get_capabilities() throws KduException
  {
    // Override in a derived class to respond to the callback
    return (int) 0;
  }
  public void Start_tileheader(int _tnum, int _num_tiles) throws KduException
  {
    // Override in a derived class to respond to the callback
    return;
  }
  public void End_tileheader(int _tnum) throws KduException
  {
    // Override in a derived class to respond to the callback
    return;
  }
  public void Start_precinct(long _unique_id) throws KduException
  {
    // Override in a derived class to respond to the callback
    return;
  }
  public void Post_end_precinct(int _num_packets) throws KduException
  {
    // Override in a derived class to respond to the callback
    return;
  }
  public native int Retrieve_packet_lengths(int _num_packets, long[] _packet_lengths) throws KduException;
  public boolean Start_rewrite(long _backtrack) throws KduException
  {
    // Override in a derived class to respond to the callback
    return false;
  }
  public boolean End_rewrite() throws KduException
  {
    // Override in a derived class to respond to the callback
    return false;
  }
  public void Set_target_size(long _num_bytes) throws KduException
  {
    // Override in a derived class to respond to the callback
    return;
  }
  public boolean Post_write(int _num_bytes) throws KduException
  {
    // Override in a derived class to respond to the callback
    return false;
  }
  public native int Pull_data(byte[] _data, int _first_byte_pos, int _num_bytes) throws KduException;
}
