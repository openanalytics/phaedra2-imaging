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

public class Kdu_message_formatter extends Kdu_message {
  static {
    //System.loadLibrary("kdu_jni");
    Native_init_class();
  }
  private static native void Native_init_class();
  protected Kdu_message_formatter(long ptr) {
    super(ptr);
  }
  public native void Native_destroy();
  public void finalize() {
    if ((_native_ptr & 1) != 0)
      { // Resource created and not donated
        Native_destroy();
      }
  }
  private static native long Native_create(Kdu_message _output, int _max_line);
  public Kdu_message_formatter(Kdu_message _output, int _max_line) {
    this(Native_create(_output, _max_line));
  }
  private static long Native_create(Kdu_message _output)
  {
    return Native_create(_output,(int) 79);
  }
  public Kdu_message_formatter(Kdu_message _output) {
    this(Native_create(_output));
  }
  public native void Set_master_indent(int _val) throws KduException;
  public native void Put_text(String _string) throws KduException;
  public native void Flush(boolean _end_of_message) throws KduException;
  public void Flush() throws KduException
  {
    Flush((boolean) false);
  }
  public native void Start_message() throws KduException;
}
