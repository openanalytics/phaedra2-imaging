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

public class Kdu_block_encoder {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected long _native_ptr = 0;
  protected Kdu_block_encoder(long ptr) {
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
  public Kdu_block_encoder() {
    this(Native_create());
  }
  public native void Encode(Kdu_block _block, boolean _reversible, double _msb_wmse, int _estimated_slope_threshold) throws KduException;
  public void Encode(Kdu_block _block) throws KduException
  {
    Encode(_block,(boolean) false,(double) 0.0F,(int) 0);
  }
  public void Encode(Kdu_block _block, boolean _reversible) throws KduException
  {
    Encode(_block,_reversible,(double) 0.0F,(int) 0);
  }
  public void Encode(Kdu_block _block, boolean _reversible, double _msb_wmse) throws KduException
  {
    Encode(_block,_reversible,_msb_wmse,(int) 0);
  }
}
