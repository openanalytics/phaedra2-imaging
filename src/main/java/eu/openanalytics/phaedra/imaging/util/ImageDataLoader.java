package eu.openanalytics.phaedra.imaging.util;

import java.awt.image.BufferedImage;
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
		BufferedImage image = ImageIO.read(input);
		if (image == null) {
			// BioFormats requires reading from a file, so copy the image data into a temp file.
			Path path = Files.createTempFile("ph2-img-", ".bin");
			FileCopyUtils.copy(input, new FileOutputStream(path.toString()));
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
			
			ImageData data = new ImageData();
			data.width = reader.getSizeX();
			data.height = reader.getSizeY();
			data.depth = reader.getBitsPerPixel();
			data.pixels = new int[data.width * data.height];
			
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
		
		//TODO Look for alternative methods. This has pretty bad performance, especially for PNG!
		ImageIO.write(biRGB, format, out);
	}
}
