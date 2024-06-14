package eu.openanalytics.phaedra.imaging.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.springframework.util.FileCopyUtils;

import eu.openanalytics.phaedra.imaging.ImageData;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.IFormatReader;
import loci.formats.ImageReader;
import loci.formats.in.TiffReader;

public class ImageDataLoader {

	public static ImageData load(String path) throws IOException {
		BufferedImage image = ImageIO.read(new File(path));
		if (image == null) return loadWithBioFormats(path);
		else return ImageDataUtils.toImageData(image);
	}
	
	public static ImageData load(InputStream input) throws IOException {
		return load(input, null);
	}
	
	public static ImageData load(InputStream input, String extension) throws IOException {
		BufferedInputStream bufferedInput = new BufferedInputStream(input);
		bufferedInput.mark(Integer.MAX_VALUE);
		
		BufferedImage image = ImageIO.read(bufferedInput);
		if (image == null) {
			// BioFormats requires reading from a file, so copy the image data into a temp file.
			if (extension == null) extension = "bin";
			Path path = Files.createTempFile("ph2-img-", "." + extension);
			
			bufferedInput.reset();
			FileCopyUtils.copy(bufferedInput, new FileOutputStream(path.toString()));
			
			ImageData data = loadWithBioFormats(path.toString());
			Files.delete(path);
			return data;
		}
		else return ImageDataUtils.toImageData(image);
	}
	
	private static ImageData loadWithBioFormats(String path) throws IOException {
		IFormatReader reader = null;
		
		// Shortcut for TIFF to avoid initializing the whole reader registry.
		String fileExt = FilenameUtils.getExtension(path);
		String[] tiffExtensions = { "tif", "tiff", "flex" };
		if (Arrays.stream(tiffExtensions).anyMatch(e -> e.equals(fileExt))) reader = new TiffReader();
		else reader = new ImageReader();
		
		try {
			reader.setGroupFiles(false);
			reader.setId(path);
			
			ImageData data = ImageDataUtils.initNew(reader.getSizeX(), reader.getSizeY(), reader.getBitsPerPixel());
			
			boolean le = true; // Access endianness only if needed.
			if (data.depth > 8) {
				@SuppressWarnings("deprecation")
				List<CoreMetadata> metadata = reader.getCoreMetadataList();
				if (!metadata.isEmpty()) le = metadata.get(0).littleEndian;
			}
			
			byte[] img = reader.openBytes(0);
			int bytesPerPixel = Math.max(1, data.depth/8);
			for (int i=0; i < data.pixels.length; i++) {
				int index = i*bytesPerPixel;
				if (data.depth == 16) {
					// The ImageData pixels are always little endian.
					if (le) data.pixels[i] = ((img[index+1] & 0xFF) << 8) + (img[index] & 0xFF);
					else data.pixels[i] = ((img[index] & 0xFF) << 8) + (img[index+1] & 0xFF);
				} else {
					data.pixels[i] = img[index];
				}
			}
			return data;
		} catch (FormatException e) {
			throw new IOException("Unsupported image format: " + path, e);
		} finally {
			reader.close();
		}
	}
	
	public static void write(ImageData data, String format, OutputStream out) throws IOException {
		// SWT is about 35% faster compared to ImageIO
		String codec = System.getProperty("phaedra2.imaging.writer.codec", "swt");
		
		if (codec.equals("swt")) {
			org.eclipse.swt.graphics.ImageData imgData = new org.eclipse.swt.graphics.ImageData(data.width, data.height, data.depth, new PaletteData(0xFF0000, 0xFF00, 0xFF));
			for (int line = 0; line < data.height; line++) {
				imgData.setPixels(0, line, data.width, data.pixels, line * data.width);
			}
			ImageLoader loader = new ImageLoader();
			loader.data = new org.eclipse.swt.graphics.ImageData[] { imgData };
			loader.save(out, toSWTFormat(format));
		} else {
			BufferedImage bi = ImageDataUtils.toBufferedImage(data);
			BufferedImage biRGB = bi;
			if (data.depth != 24) {
				int w = bi.getWidth();
				int h = bi.getHeight();
				int[] pixels = new int[w * h];
				bi.getRGB(0, 0, w, h, pixels, 0, w);
				biRGB = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
				biRGB.setRGB(0, 0, w, h, pixels, 0, w);
			}
			ImageIO.write(biRGB, format, out);
		}
	}
	
	private static int toSWTFormat(String format) {
		if (format == null) return 4;
		format = format.toLowerCase().trim();
		if (format.equals("jpg") || format.equals("jpeg")) return 4;
		if (format.equals("png")) return 5;
		if (format.equals("tif") || format.equals("tiff")) return 6; 
		return 4;
	}
}
