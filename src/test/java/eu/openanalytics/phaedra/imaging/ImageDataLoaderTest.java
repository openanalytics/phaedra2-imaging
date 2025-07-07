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
package eu.openanalytics.phaedra.imaging;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileCopyUtils;

import eu.openanalytics.phaedra.imaging.jp2k.ICodec;
import eu.openanalytics.phaedra.imaging.jp2k.ICodestreamSource;
import eu.openanalytics.phaedra.imaging.jp2k.source.ClassPathSourceDescriptor;
import eu.openanalytics.phaedra.imaging.util.ImageDataLoader;

public class ImageDataLoaderTest {

	@Test
	public void testRenderToPNG() throws IOException {
		testRenderToFormat("png");
	}
	
	@Test
	public void testRenderToJPG() throws IOException {
		testRenderToFormat("jpg");
	}
	
	private void testRenderToFormat(String format) throws IOException {
		try (ICodec codec = CodecFactory.createCodec()) {
			ICodestreamSource src = new ClassPathSourceDescriptor("samples/sample.j2k").create();
			
			long start = System.currentTimeMillis();
			ImageData data = codec.renderImage(1.0f, src);
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageDataLoader.write(data, format, bos);
			byte[] bytes = bos.toByteArray();
			long duration = System.currentTimeMillis() - start;
			
			assertNotNull(bytes);
			assertTrue(bytes.length > 0, "Zero-length byte array received from encoder");
			
			System.out.println(String.format("Rendered %d pixels from JPEG2000 to %s (%d bytes) in %d ms", data.pixels.length, format, bytes.length, duration));
		}
	}
	
	@Test
	public void testLoadFromJPG() throws IOException {
		testLoadFromFormat("samples/sample.jpg");
	}
	
	@Test
	public void testLoadFromPNG() throws IOException {
		testLoadFromFormat("samples/sample.png");
	}
	
	@Test
	public void testLoadFromTIF() throws IOException {
		testLoadFromFormat("samples/sample.tif");
	}
	
	private void testLoadFromFormat(String path) throws IOException {
		try (InputStream input = CodecFactory.class.getClassLoader().getResourceAsStream(path)) {
			File tempFile = Files.createTempFile("ph2-testfile", "." + FilenameUtils.getExtension(path)).toFile();
			tempFile.deleteOnExit();
			
			FileCopyUtils.copy(input, new FileOutputStream(tempFile));
			long start = System.currentTimeMillis();
			ImageData data = ImageDataLoader.load(tempFile.getAbsolutePath());
			long duration = System.currentTimeMillis() - start;
			
			assertNotNull(data);
			assertTrue(data.width > 0 && data.height > 0 && data.depth > 0);
			
			System.out.println(String.format("Decoded %d pixels from %s in %d ms", data.pixels.length, path, duration));
		}
	}
}
