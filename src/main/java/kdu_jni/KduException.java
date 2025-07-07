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

public class KduException extends Exception {
	
	private static final long serialVersionUID = -7911477867999171739L;
	
	public KduException() {
      super();
      this.kdu_exception_code = Kdu_global.KDU_NULL_EXCEPTION;
    }
    public KduException(String message) {
      super(message);
      this.kdu_exception_code = Kdu_global.KDU_NULL_EXCEPTION;
    }
    public KduException(int exc_code) {
      super();
      this.kdu_exception_code = exc_code;
    }
    public KduException(int exc_code, String message) {
      super(message);
      this.kdu_exception_code = exc_code;
    }
    public int Get_kdu_exception_code() {
      return kdu_exception_code;
    }
    public boolean Compare(int exc_code) {
      if (exc_code == kdu_exception_code)
        return true;
      else
        return false;
    }
    private int kdu_exception_code;
}
