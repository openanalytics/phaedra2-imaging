package eu.openanalytics.phaedra.imaging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import org.springframework.util.FileCopyUtils;

import eu.openanalytics.phaedra.imaging.jp2k.CompressionConfig;
import eu.openanalytics.phaedra.imaging.jp2k.ICodec;
import eu.openanalytics.phaedra.imaging.jp2k.ICodestreamSource;
import eu.openanalytics.phaedra.imaging.jp2k.openjpeg.source.ByteArraySource;
import eu.openanalytics.phaedra.imaging.jp2k.source.ClassPathSourceDescriptor;
import eu.openanalytics.phaedra.imaging.util.ImageDataLoader;
import eu.openanalytics.phaedra.imaging.util.ImageDataUtils;

public class CodecTest {

	public static void main(String[] args) {
		try {
			new CodecTest().testDecodeFull();
			new CodecTest().testDecodeScaled();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDecodeFull() throws IOException {
		try (ICodec codec = CodecFactory.createCodec()) {
			ICodestreamSource src = new ClassPathSourceDescriptor("samples/sample.j2k").create();
			
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
			ICodestreamSource src = new ClassPathSourceDescriptor("samples/sample.j2k").create();
			
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
			ICodestreamSource src = new ClassPathSourceDescriptor("samples/sample.j2k").create();
			
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
			ICodestreamSource src = new ClassPathSourceDescriptor("samples/sample.j2k").create();
			
			long start = System.currentTimeMillis();
			ImageData data = codec.renderImageRegion(1.0f, new Rectangle(10, 10, 10, 10), src);
			long duration = System.currentTimeMillis() - start;
			
			assertNotNull(data);
			assertEquals(data.width * data.height, data.pixels.length);
			assertTrue(data.depth > 0);
			
			System.out.println(String.format("Decoded %d pixels in %d ms", data.pixels.length, duration));
		}
	}
	
	@Test
	public void testEncode8bit() throws IOException {
		InputStream input = ByteArraySource.class.getClassLoader().getResourceAsStream("samples/sample.tif");
		ImageData data = ImageDataLoader.load(input, "tif");
		data = ImageDataUtils.changeDepth(data, 8);
		CompressionConfig config = new CompressionConfig();
		testEncode(data, config);
	}

	@Test
	public void testEncode1bit() throws IOException {
		InputStream input = ByteArraySource.class.getClassLoader().getResourceAsStream("samples/sample.tif");
		ImageData data = ImageDataLoader.load(input, "tif");
		data = ImageDataUtils.changeDepth(data, 1);
		CompressionConfig config = new CompressionConfig();
		config.reversible = true;
		testEncode(data, config);
	}
	
	private void testEncode(ImageData data, CompressionConfig config) throws IOException {
		try (ICodec codec = CodecFactory.createCodec()) {
			String destination = Files.createTempFile("ph2-tmp-", ".jp2k").toString();
			
			long start = System.currentTimeMillis();
			codec.compressCodeStream(config, data, destination);
			long duration = System.currentTimeMillis() - start;
			
			File destinationFile = new File(destination);
			byte[] encoded = FileCopyUtils.copyToByteArray(destinationFile);
			destinationFile.delete();
	
			assertNotNull(encoded);
			assertTrue(encoded.length > 0);
			
			ImageData dataAfterDecode = codec.renderImage(1.0f, new ByteArraySource(encoded));
			
			assertEquals(data.width, dataAfterDecode.width);
			assertEquals(data.height, dataAfterDecode.height);
			
	//		ImageDataLoader.write(dataAfterDecode, "jpg", new FileOutputStream(Files.createTempFile("ph2-tmp-", ".jpg").toString()));
			
			System.out.println(String.format("Encoded %d pixels in %d ms", data.pixels.length, duration));
		}
	}
}
