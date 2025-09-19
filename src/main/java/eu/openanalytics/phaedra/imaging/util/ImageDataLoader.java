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
import java.util.function.Supplier;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import eu.openanalytics.phaedra.imaging.ImageData;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.IFormatReader;
import loci.formats.ImageReader;
import loci.formats.in.TiffReader;

public class ImageDataLoader {
	private static final Logger logger = LoggerFactory.getLogger(ImageDataLoader.class);

	public static ImageData load(String path) throws IOException {
		Supplier<ImageData> bioLoader = () -> {
			try {
				return loadWithBioFormats(path);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		};
		Supplier<ImageData> imageIOLoader = () -> {
			try {
				BufferedImage image = ImageIO.read(new File(path));
				return (image == null) ? null : ImageDataUtils.toImageData(image);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		};

		ImageData data = null;
		String codecId = System.getProperty("phaedra2.imaging.codec", "bioformats");
		if (codecId.equalsIgnoreCase("bioformats")) {
			data = bioLoader.get();
			if (data == null) data = imageIOLoader.get();
		} else {
			data = imageIOLoader.get();
			if (data == null) data = bioLoader.get();
		}

		return data;
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
		BufferedImage bi = ImageDataUtils.toBufferedImage(data);
		BufferedImage biRGB = bi;
		if (data.depth != 24) {
			logger.info("here");
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
