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

public class Kdu_subband {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected Kdu_subband(long ptr) {
    _native_ptr = ptr;
  }
  public Kdu_subband() {
      this(0);
  }
  public native boolean Exists() throws KduException;
  public native int Get_band_idx() throws KduException;
  public native Kdu_resolution Access_resolution() throws KduException;
  public native boolean Is_top_level_band() throws KduException;
  public native int Get_K_max() throws KduException;
  public native int Get_K_max_prime() throws KduException;
  public native boolean Get_reversible() throws KduException;
  public native float Get_delta() throws KduException;
  public native float Get_msb_wmse() throws KduException;
  public native boolean Get_roi_weight(float[] _energy_weight) throws KduException;
  public native void Get_dims(Kdu_dims _dims) throws KduException;
  public native void Get_valid_blocks(Kdu_dims _indices) throws KduException;
  public native void Get_block_size(Kdu_coords _nominal_size, Kdu_coords _first_size) throws KduException;
  public native int Get_block_geometry(boolean[] _transpose, boolean[] _vflip, boolean[] _hflip) throws KduException;
  public native void Block_row_generated(int _block_height, boolean _subband_finished, Kdu_thread_env _env) throws KduException;
  public native boolean Attach_block_notifier(Kdu_thread_queue _client_queue, Kdu_thread_env _env) throws KduException;
  public native void Advance_block_rows_needed(Kdu_thread_queue _client_queue, long _delta_rows_needed, long _quantum_bits, long _num_quantum_blocks, Kdu_thread_env _env) throws KduException;
  public native boolean Detach_block_notifier(Kdu_thread_queue _client_queue, Kdu_thread_env _env) throws KduException;
  public native Kdu_block Open_block(Kdu_coords _block_idx, int[] _return_tpart, Kdu_thread_env _env) throws KduException;
  public Kdu_block Open_block(Kdu_coords _block_idx) throws KduException
  {
    Kdu_thread_env env = null;
    return Open_block(_block_idx,null,env);
  }
  public Kdu_block Open_block(Kdu_coords _block_idx, int[] _return_tpart) throws KduException
  {
    Kdu_thread_env env = null;
    return Open_block(_block_idx,_return_tpart,env);
  }
  public native void Close_block(Kdu_block _block, Kdu_thread_env _env) throws KduException;
  public void Close_block(Kdu_block _block) throws KduException
  {
    Kdu_thread_env env = null;
    Close_block(_block,env);
  }
  public native int Get_conservative_slope_threshold() throws KduException;
}
