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

public class Kdu_serve {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected Kdu_serve(long ptr) {
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
  public Kdu_serve() {
    this(Native_create());
  }
  public native void Initialize(Kdu_serve_target _target, int _max_chunk_size, int _chunk_prefix_bytes, boolean _ignore_relevance_info) throws KduException;
  public void Initialize(Kdu_serve_target _target, int _max_chunk_size, int _chunk_prefix_bytes) throws KduException
  {
    Initialize(_target,_max_chunk_size,_chunk_prefix_bytes,(boolean) false);
  }
  public native void Destroy() throws KduException;
  public native void Set_window(Kdu_window _window, Kdu_window_prefs _pref_updates, Kdu_window_model _model_instructions, boolean _is_stateless, int _context_id) throws KduException;
  public void Set_window(Kdu_window _window) throws KduException
  {
    Kdu_window_prefs pref_updates = null;
    Kdu_window_model model_instructions = null;
    Set_window(_window,pref_updates,model_instructions,(boolean) false,(int) 0);
  }
  public void Set_window(Kdu_window _window, Kdu_window_prefs _pref_updates) throws KduException
  {
    Kdu_window_model model_instructions = null;
    Set_window(_window,_pref_updates,model_instructions,(boolean) false,(int) 0);
  }
  public void Set_window(Kdu_window _window, Kdu_window_prefs _pref_updates, Kdu_window_model _model_instructions) throws KduException
  {
    Set_window(_window,_pref_updates,_model_instructions,(boolean) false,(int) 0);
  }
  public void Set_window(Kdu_window _window, Kdu_window_prefs _pref_updates, Kdu_window_model _model_instructions, boolean _is_stateless) throws KduException
  {
    Set_window(_window,_pref_updates,_model_instructions,_is_stateless,(int) 0);
  }
  public native boolean Get_window(Kdu_window _window, int _context_id) throws KduException;
  public boolean Get_window(Kdu_window _window) throws KduException
  {
    return Get_window(_window,(int) 0);
  }
  public native boolean Get_image_done() throws KduException;
  public native int Push_extra_data(byte[] _data, int _num_bytes) throws KduException;
  public native void Window_finished(int _context_id) throws KduException;
}
