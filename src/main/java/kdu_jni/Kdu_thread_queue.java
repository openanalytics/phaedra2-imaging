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

public class Kdu_thread_queue {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected Kdu_thread_queue(long ptr) {
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
  public Kdu_thread_queue() {
    this(Native_create());
  }
  public native boolean Is_attached() throws KduException;
  public native void Force_detach(Kdu_thread_entity _caller) throws KduException;
  public void Force_detach() throws KduException
  {
    Kdu_thread_entity caller = null;
    Force_detach(caller);
  }
  public native long Get_sequence_idx() throws KduException;
  public native int Get_max_jobs() throws KduException;
  public native boolean Update_dependencies(int _new_dependencies, int _delta_max_dependencies, Kdu_thread_entity _caller) throws KduException;
}
