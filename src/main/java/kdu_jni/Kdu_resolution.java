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

public class Kdu_resolution {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected Kdu_resolution(long ptr) {
    _native_ptr = ptr;
  }
  public Kdu_resolution() {
      this(0);
  }
  public native boolean Exists() throws KduException;
  public native Kdu_resolution Access_next() throws KduException;
  public native int Which() throws KduException;
  public native int Get_dwt_level() throws KduException;
  public native void Get_dims(Kdu_dims _dims) throws KduException;
  public native void Get_valid_precincts(Kdu_dims _indices) throws KduException;
  public native Kdu_precinct Open_precinct(Kdu_coords _precinct_idx, Kdu_thread_env _env) throws KduException;
  public Kdu_precinct Open_precinct(Kdu_coords _precinct_idx) throws KduException
  {
    Kdu_thread_env env = null;
    return Open_precinct(_precinct_idx,env);
  }
  public native long Get_precinct_id(Kdu_coords _precinct_idx) throws KduException;
  public native double Get_precinct_relevance(Kdu_coords _precinct_idx) throws KduException;
  public native int Get_precinct_packets(Kdu_coords _precinct_idx, Kdu_thread_env _env, boolean _parse_if_necessary) throws KduException;
  public int Get_precinct_packets(Kdu_coords _precinct_idx) throws KduException
  {
    Kdu_thread_env env = null;
    return Get_precinct_packets(_precinct_idx,env,(boolean) true);
  }
  public int Get_precinct_packets(Kdu_coords _precinct_idx, Kdu_thread_env _env) throws KduException
  {
    return Get_precinct_packets(_precinct_idx,_env,(boolean) true);
  }
  public native long Get_precinct_samples(Kdu_coords _precinct_idx) throws KduException;
  public native Kdu_node Access_node() throws KduException;
  public native int Get_valid_band_indices(int[] _min_idx) throws KduException;
  public native Kdu_subband Access_subband(int _band_idx) throws KduException;
  public native boolean Get_reversible() throws KduException;
  public native boolean Propagate_roi() throws KduException;
}
