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

public class Kdu_message {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected Kdu_message(long ptr) {
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
  private native void Native_init();
  public Kdu_message() {
    this(Native_create());
    this.Native_init();
  }
  public void Put_text(String _string) throws KduException
  {
    // Override in a derived class to respond to the callback
    return;
  }
  public native void Put_text(int[] _string) throws KduException;
  public void Flush(boolean _end_of_message) throws KduException
  {
    // Override in a derived class to respond to the callback
    return;
  }
  public void Flush() throws KduException
  {
    Flush((boolean) false);
  }
  public void Start_message() throws KduException
  {
    // Override in a derived class to respond to the callback
    return;
  }
  public native boolean Set_hex_mode(boolean _new_mode) throws KduException;
}
