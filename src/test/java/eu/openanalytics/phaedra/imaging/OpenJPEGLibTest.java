package eu.openanalytics.phaedra.imaging;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.openjpeg.OpenJPEGDecoder;

import eu.openanalytics.phaedra.imaging.jp2k.openjpeg.OpenJPEGLibLoader;
import eu.openanalytics.phaedra.imaging.jp2k.openjpeg.source.ByteArraySource;

public class OpenJPEGLibTest {

	@Test
	public void testLoadLibraries() {
		OpenJPEGLibLoader.load();
		assertTrue(OpenJPEGLibLoader.isLoaded());
	}
	
	@Test
	public void testDecode() throws IOException {
		OpenJPEGDecoder decoder = new OpenJPEGDecoder();
		ByteArraySource src = ByteArraySource.fromClassPath("samples/sample.j2k");

		long start = System.currentTimeMillis();
		int[] pixels = decoder.decode(src, 0).pixels;
		long duration = System.currentTimeMillis() - start;
		
		System.out.println(String.format("Decoded %d pixels in %d ms", pixels.length, duration));
	}
}