package eu.openanalytics.phaedra.imaging;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import eu.openanalytics.phaedra.imaging.jp2k.ICodec;
import eu.openanalytics.phaedra.imaging.jp2k.openjpeg.source.ByteArraySource;
import eu.openanalytics.phaedra.imaging.util.ImageDataUtils;

public class ImageFormatTest {

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
			ByteArraySource src = ByteArraySource.fromClassPath("sample.j2k");
			
			long start = System.currentTimeMillis();
			ImageData data = codec.renderImage(1.0f, src);
			byte[] bytes = ImageDataUtils.encodeToFormat(data, format);
//			FileCopyUtils.copy(bytes, new File("output." + format));
			long duration = System.currentTimeMillis() - start;
			
			assertNotNull(bytes);
			assertTrue(bytes.length > 0, "Zero-length byte array received from encoder");
			
			System.out.println(String.format("Rendered %d pixels from JPEG2000 to %s (%d bytes) in %d ms", data.pixels.length, format, bytes.length, duration));
		}
	}
}
