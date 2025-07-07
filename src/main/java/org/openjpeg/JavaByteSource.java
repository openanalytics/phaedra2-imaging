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
package org.openjpeg;

/**
 * <p>
 * This class represents an image source, which could be a File,
 * but also another type of source, such as a stream, a byte array,
 * a HTTP response, ...
 * </p>
 * <p>
 * Note that instantiating this class creates native resources, so it
 * is very important to call {@link close} when the source is no longer needed.
 * </p>
 */
public class JavaByteSource implements AutoCloseable {

	private int srcType;
	private long nativePointer;
	
	public final static int SRC_TYPE_J2K = 0;
	public final static int SRC_TYPE_JP2 = 1;
	
	private static native void initClass();
	static {
		initClass();
	}
	
	public JavaByteSource(int srcType) {
		this.srcType = srcType;
		init();
	}
	
	private native void init();
	
	public boolean seek(long pos) {
		return false;
	}
	
	public long skip(long len) {
		return 0L;
	}
	
	public long getPos() {
		return 0L;
	}
	
	public long getSize() {
		return 0L;
	}
	
	public int read(int len) {
		return 0;
	}
	
	protected native void addBytesRead(byte[] buf, int offset, int len);
	
	public native void close();
}
