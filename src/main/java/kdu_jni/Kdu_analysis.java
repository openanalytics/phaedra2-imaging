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

public class Kdu_analysis extends Kdu_push_ifc {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected Kdu_analysis(long ptr) {
    super(ptr);
  }
  private native void Native_destroy();
  public void finalize() {
    if ((_native_ptr & 1) != 0)
      { // Resource created and not donated
        Native_destroy();
      }
  }
  private static native long Native_create(Kdu_node _node, Kdu_sample_allocator _allocator, boolean _use_shorts, float _normalization, Kdu_roi_node _roi, Kdu_thread_env _env, Kdu_thread_queue _env_queue, int _flags);
  public Kdu_analysis(Kdu_node _node, Kdu_sample_allocator _allocator, boolean _use_shorts, float _normalization, Kdu_roi_node _roi, Kdu_thread_env _env, Kdu_thread_queue _env_queue, int _flags) {
    this(Native_create(_node, _allocator, _use_shorts, _normalization, _roi, _env, _env_queue, _flags));
  }
  private static long Native_create(Kdu_node _node, Kdu_sample_allocator _allocator, boolean _use_shorts)
  {
    Kdu_roi_node roi = null;
    Kdu_thread_env env = null;
    Kdu_thread_queue env_queue = null;
    return Native_create(_node,_allocator,_use_shorts,(float) 1.0F,roi,env,env_queue,(int) 0);
  }
  public Kdu_analysis(Kdu_node _node, Kdu_sample_allocator _allocator, boolean _use_shorts) {
    this(Native_create(_node, _allocator, _use_shorts));
  }
  private static long Native_create(Kdu_node _node, Kdu_sample_allocator _allocator, boolean _use_shorts, float _normalization)
  {
    Kdu_roi_node roi = null;
    Kdu_thread_env env = null;
    Kdu_thread_queue env_queue = null;
    return Native_create(_node,_allocator,_use_shorts,_normalization,roi,env,env_queue,(int) 0);
  }
  public Kdu_analysis(Kdu_node _node, Kdu_sample_allocator _allocator, boolean _use_shorts, float _normalization) {
    this(Native_create(_node, _allocator, _use_shorts, _normalization));
  }
  private static long Native_create(Kdu_node _node, Kdu_sample_allocator _allocator, boolean _use_shorts, float _normalization, Kdu_roi_node _roi)
  {
    Kdu_thread_env env = null;
    Kdu_thread_queue env_queue = null;
    return Native_create(_node,_allocator,_use_shorts,_normalization,_roi,env,env_queue,(int) 0);
  }
  public Kdu_analysis(Kdu_node _node, Kdu_sample_allocator _allocator, boolean _use_shorts, float _normalization, Kdu_roi_node _roi) {
    this(Native_create(_node, _allocator, _use_shorts, _normalization, _roi));
  }
  private static long Native_create(Kdu_node _node, Kdu_sample_allocator _allocator, boolean _use_shorts, float _normalization, Kdu_roi_node _roi, Kdu_thread_env _env)
  {
    Kdu_thread_queue env_queue = null;
    return Native_create(_node,_allocator,_use_shorts,_normalization,_roi,_env,env_queue,(int) 0);
  }
  public Kdu_analysis(Kdu_node _node, Kdu_sample_allocator _allocator, boolean _use_shorts, float _normalization, Kdu_roi_node _roi, Kdu_thread_env _env) {
    this(Native_create(_node, _allocator, _use_shorts, _normalization, _roi, _env));
  }
  private static long Native_create(Kdu_node _node, Kdu_sample_allocator _allocator, boolean _use_shorts, float _normalization, Kdu_roi_node _roi, Kdu_thread_env _env, Kdu_thread_queue _env_queue)
  {
    return Native_create(_node,_allocator,_use_shorts,_normalization,_roi,_env,_env_queue,(int) 0);
  }
  public Kdu_analysis(Kdu_node _node, Kdu_sample_allocator _allocator, boolean _use_shorts, float _normalization, Kdu_roi_node _roi, Kdu_thread_env _env, Kdu_thread_queue _env_queue) {
    this(Native_create(_node, _allocator, _use_shorts, _normalization, _roi, _env, _env_queue));
  }
  private static native long Native_create(Kdu_resolution _resolution, Kdu_sample_allocator _allocator, boolean _use_shorts, float _normalization, Kdu_roi_node _roi, Kdu_thread_env _env, Kdu_thread_queue _env_queue);
  public Kdu_analysis(Kdu_resolution _resolution, Kdu_sample_allocator _allocator, boolean _use_shorts, float _normalization, Kdu_roi_node _roi, Kdu_thread_env _env, Kdu_thread_queue _env_queue) {
    this(Native_create(_resolution, _allocator, _use_shorts, _normalization, _roi, _env, _env_queue));
  }
  private static long Native_create(Kdu_resolution _resolution, Kdu_sample_allocator _allocator, boolean _use_shorts)
  {
    Kdu_roi_node roi = null;
    Kdu_thread_env env = null;
    Kdu_thread_queue env_queue = null;
    return Native_create(_resolution,_allocator,_use_shorts,(float) 1.0,roi,env,env_queue);
  }
  public Kdu_analysis(Kdu_resolution _resolution, Kdu_sample_allocator _allocator, boolean _use_shorts) {
    this(Native_create(_resolution, _allocator, _use_shorts));
  }
  private static long Native_create(Kdu_resolution _resolution, Kdu_sample_allocator _allocator, boolean _use_shorts, float _normalization)
  {
    Kdu_roi_node roi = null;
    Kdu_thread_env env = null;
    Kdu_thread_queue env_queue = null;
    return Native_create(_resolution,_allocator,_use_shorts,_normalization,roi,env,env_queue);
  }
  public Kdu_analysis(Kdu_resolution _resolution, Kdu_sample_allocator _allocator, boolean _use_shorts, float _normalization) {
    this(Native_create(_resolution, _allocator, _use_shorts, _normalization));
  }
  private static long Native_create(Kdu_resolution _resolution, Kdu_sample_allocator _allocator, boolean _use_shorts, float _normalization, Kdu_roi_node _roi)
  {
    Kdu_thread_env env = null;
    Kdu_thread_queue env_queue = null;
    return Native_create(_resolution,_allocator,_use_shorts,_normalization,_roi,env,env_queue);
  }
  public Kdu_analysis(Kdu_resolution _resolution, Kdu_sample_allocator _allocator, boolean _use_shorts, float _normalization, Kdu_roi_node _roi) {
    this(Native_create(_resolution, _allocator, _use_shorts, _normalization, _roi));
  }
  private static long Native_create(Kdu_resolution _resolution, Kdu_sample_allocator _allocator, boolean _use_shorts, float _normalization, Kdu_roi_node _roi, Kdu_thread_env _env)
  {
    Kdu_thread_queue env_queue = null;
    return Native_create(_resolution,_allocator,_use_shorts,_normalization,_roi,_env,env_queue);
  }
  public Kdu_analysis(Kdu_resolution _resolution, Kdu_sample_allocator _allocator, boolean _use_shorts, float _normalization, Kdu_roi_node _roi, Kdu_thread_env _env) {
    this(Native_create(_resolution, _allocator, _use_shorts, _normalization, _roi, _env));
  }
}
