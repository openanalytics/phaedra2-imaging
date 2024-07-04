package eu.openanalytics.phaedra.imaging;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.openjpeg.OpenJPEGDecoder;

import eu.openanalytics.phaedra.imaging.jp2k.ICodestreamSource;
import eu.openanalytics.phaedra.imaging.jp2k.openjpeg.OpenJPEGLibLoader;
import eu.openanalytics.phaedra.imaging.jp2k.openjpeg.source.GenericByteSource;
import eu.openanalytics.phaedra.imaging.jp2k.source.ClassPathSourceDescriptor;

public class OpenJPEGLibTest {

	@Test
	public void testLoadLibraries() {
		OpenJPEGLibLoader.load();
		assertTrue(OpenJPEGLibLoader.isLoaded());
	}
	
	@Test
	public void testDecode() throws IOException {
		OpenJPEGDecoder decoder = new OpenJPEGDecoder();
		ICodestreamSource src = new ClassPathSourceDescriptor("samples/sample.j2k").create();
		GenericByteSource byteSource = new GenericByteSource() {
			@Override
			protected long doGetSize() {
				try {
					return src.getSize();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			@Override
			protected byte[] doGetBytes(long pos, int len) throws IOException {
				return src.getBytes(pos, len);
			}
		};
		try {
			long start = System.currentTimeMillis();
			int[] pixels = decoder.decode(byteSource, 0).pixels;
			long duration = System.currentTimeMillis() - start;
			System.out.println(String.format("Decoded %d pixels in %d ms", pixels.length, duration));
		} finally {
			if (byteSource != null) byteSource.close();			
		}
	}
}