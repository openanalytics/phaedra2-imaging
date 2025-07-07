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
package eu.openanalytics.phaedra.imaging.jp2k.openjpeg.source;

import java.io.IOException;

import org.openjpeg.JavaByteSource;

public abstract class GenericByteSource extends JavaByteSource {
	
	private long pos;
	
	public GenericByteSource() {
		super(SRC_TYPE_J2K);
		this.pos = 0;
	}
	
	@Override
	public boolean seek(long pos) {
		this.pos = pos;
		return true;
	}
	
	@Override
	public long skip(long len) {
		long currentPos = getPos();
		long size = getSize();
		long canSkip = Math.min(len, size - currentPos);
		long newPos = currentPos + canSkip;
		
		seek(newPos);
		if (newPos == size) return -1;
		else return canSkip;
	}
	
	@Override
	public long getPos() {
		return pos;
	}
	
	@Override
	public long getSize() {
		return doGetSize();
	}
	
	@Override
	public int read(int len) {
		try {
			byte[] data = doGetBytes(pos, len);
			pos += data.length;
			addBytesRead(data, 0, data.length);
			return data.length;
		} catch (IOException e) {
			return 0;
		}
	}
	
	protected abstract long doGetSize();
	
	protected abstract byte[] doGetBytes(long pos, int len) throws IOException;
}
