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

public class Mj2_target {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected Mj2_target(long ptr) {
    _native_ptr = ptr;
  }
  public Mj2_target() {
      this(0);
  }
  public native boolean Exists() throws KduException;
  public native void Open(Jp2_family_tgt _tgt) throws KduException;
  public native void Close() throws KduException;
  public native Mj2_video_target Add_video_track() throws KduException;
}
