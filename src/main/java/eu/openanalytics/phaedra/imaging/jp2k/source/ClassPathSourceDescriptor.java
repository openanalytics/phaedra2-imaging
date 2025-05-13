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
package eu.openanalytics.phaedra.imaging.jp2k.source;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.springframework.util.FileCopyUtils;

import eu.openanalytics.phaedra.imaging.jp2k.ICodestreamSource;
import eu.openanalytics.phaedra.imaging.jp2k.ICodestreamSourceDescriptor;
import loci.formats.cache.ByteArraySource;

public class ClassPathSourceDescriptor implements ICodestreamSourceDescriptor {

	private String path;
	
	public ClassPathSourceDescriptor(String path) {
		this.path = path;
	}
	
	@Override
	public ICodestreamSource create() throws IOException {
		InputStream input = ByteArraySource.class.getClassLoader().getResourceAsStream(path);
		byte[] bytes = FileCopyUtils.copyToByteArray(input);
		return new ICodestreamSource() {
			@Override
			public long getSize() throws IOException {
				return bytes.length;
			}
			@Override
			public byte[] getBytes(long pos, int len) throws IOException {
				int start = (int) pos;
				return Arrays.copyOfRange(bytes, start, start + len);
			}
		};
	}
}
