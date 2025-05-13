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

public class Kdu_simple_video_target extends Kdu_compressed_video_target {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected Kdu_simple_video_target(long ptr) {
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
  public Kdu_simple_video_target() {
    this(Native_create());
  }
  private static native long Native_create(String _fname, long _timescale, long _frame_period, long _flags);
  public Kdu_simple_video_target(String _fname, long _timescale, long _frame_period, long _flags) {
    this(Native_create(_fname, _timescale, _frame_period, _flags));
  }
  public native boolean Exists() throws KduException;
  public native void Open(String _fname, long _timescale, long _frame_period, long _flags) throws KduException;
}
