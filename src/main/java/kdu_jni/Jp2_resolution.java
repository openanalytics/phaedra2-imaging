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

public class Jp2_resolution {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected Jp2_resolution(long ptr) {
    _native_ptr = ptr;
  }
  public Jp2_resolution() {
      this(0);
  }
  public native boolean Exists() throws KduException;
  public native void Copy(Jp2_resolution _src) throws KduException;
  public native boolean Init(float _aspect_ratio) throws KduException;
  public native boolean Set_different_capture_aspect_ratio(float _aspect_ratio) throws KduException;
  public native boolean Set_resolution(float _resolution, boolean _for_display) throws KduException;
  public boolean Set_resolution(float _resolution) throws KduException
  {
    return Set_resolution(_resolution,(boolean) true);
  }
  public native float Get_aspect_ratio(boolean _for_display) throws KduException;
  public float Get_aspect_ratio() throws KduException
  {
    return Get_aspect_ratio((boolean) true);
  }
  public native float Get_resolution(boolean _for_display) throws KduException;
  public float Get_resolution() throws KduException
  {
    return Get_resolution((boolean) true);
  }
}
