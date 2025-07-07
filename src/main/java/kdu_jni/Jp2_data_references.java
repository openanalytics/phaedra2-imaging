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

public class Jp2_data_references {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected Jp2_data_references(long ptr) {
    _native_ptr = ptr;
  }
  public Jp2_data_references() {
      this(0);
  }
  public native boolean Exists() throws KduException;
  public native int Add_url(String _url, int _url_idx) throws KduException;
  public int Add_url(String _url) throws KduException
  {
    return Add_url(_url,(int) 0);
  }
  public native int Add_file_url(String _pathname, int _url_idx) throws KduException;
  public int Add_file_url(String _pathname) throws KduException
  {
    return Add_file_url(_pathname,(int) 0);
  }
  public native int Get_num_urls() throws KduException;
  public native int Find_url(String _url) throws KduException;
  public native String Get_url(int _idx) throws KduException;
  public native String Get_file_url(int _idx) throws KduException;
}
