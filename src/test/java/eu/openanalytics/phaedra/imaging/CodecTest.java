package eu.openanalytics.phaedra.imaging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Rectangle;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import eu.openanalytics.phaedra.imaging.jp2k.ICodec;
import eu.openanalytics.phaedra.imaging.jp2k.openjpeg.source.ByteArraySource;

public class CodecTest {

	@Test
	public void testDecodeFull() throws IOException {
		try (ICodec codec = CodecFactory.createCodec()) {
			ByteArraySource src = ByteArraySource.fromClassPath("sample.j2k");
			
			long start = System.currentTimeMillis();
			ImageData data = codec.renderImage(1.0f, src);
			long duration = System.currentTimeMillis() - start;
			
			assertNotNull(data);
			assertEquals(data.width * data.height, data.pixels.length);
			assertTrue(data.depth > 0);
			
			System.out.println(String.format("Decoded %d pixels in %d ms", data.pixels.length, duration));
		}
	}

	@Test
	public void testDecodeScaled() throws IOException {
		try (ICodec codec = CodecFactory.createCodec()) {
			ByteArraySource src = ByteArraySource.fromClassPath("sample.j2k");
			
			long start = System.currentTimeMillis();
			ImageData data = codec.renderImage(0.25f, src);
			long duration = System.currentTimeMillis() - start;
			
			assertNotNull(data);
			assertEquals(data.width * data.height, data.pixels.length);
			assertTrue(data.depth > 0);
			
			System.out.println(String.format("Decoded %d pixels in %d ms", data.pixels.length, duration));
		}
	}
	
	@Test
	public void testDecodeFixedSize() throws IOException {
		try (ICodec codec = CodecFactory.createCodec()) {
			ByteArraySource src = ByteArraySource.fromClassPath("sample.j2k");
			
			long start = System.currentTimeMillis();
			ImageData data = codec.renderImage(1000, 1000, src);
			long duration = System.currentTimeMillis() - start;
			
			assertNotNull(data);
			assertEquals(data.width * data.height, data.pixels.length);
			assertTrue(data.depth > 0);
			
			System.out.println(String.format("Decoded %d pixels in %d ms", data.pixels.length, duration));
		}
	}
	
	@Test
	public void testDecodeRegion() throws IOException {
		try (ICodec codec = CodecFactory.createCodec()) {
			ByteArraySource src = ByteArraySource.fromClassPath("sample.j2k");
			
			long start = System.currentTimeMillis();
			ImageData data = codec.renderImageRegion(1.0f, new Rectangle(10, 10, 10, 10), src);
			long duration = System.currentTimeMillis() - start;
			
			assertNotNull(data);
			assertEquals(data.width * data.height, data.pixels.length);
			assertTrue(data.depth > 0);
			
			System.out.println(String.format("Decoded %d pixels in %d ms", data.pixels.length, duration));
		}
	}
}
