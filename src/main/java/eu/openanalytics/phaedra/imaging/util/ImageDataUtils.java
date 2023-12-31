
package eu.openanalytics.phaedra.imaging.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import eu.openanalytics.phaedra.imaging.ImageData;

/**
 * A collection of utilities for manipulating and converting ImageData objects.
 */
public class ImageDataUtils {

	/**
	 * Initialize a new ImageData object of the specified dimensions.
	 */
	public static ImageData initNew(int width, int height, int depth) {
		ImageData image = new ImageData();
		image.width = width;
		image.height = height;
		image.depth = depth;
		image.pixels = new int[ width * height ];
		return image;
	}
	
	/**
	 * Create an exact deep copy of an ImageData object.
	 */
	public static ImageData copy(ImageData image) {
		ImageData copy = initNew(image.width, image.height, image.depth);
		for (int i = 0; i< image.pixels.length; i++) {
			copy.pixels[i] = image.pixels[i];
		}
		return copy;
	}
	
	public static int[] createGammaLUT(float gamma) {
		gamma = 1f / gamma;
		int[] gammaLUT = new int[256];
		for (int i=0; i<gammaLUT.length; i++) {
			gammaLUT[i] = (int) (255 * (Math.pow((double) i / (double) 255, gamma)));
		}
		return gammaLUT;
	}
	
	public static void applyGammaLUT(ImageData image, int[] gammaLUT) {
		for (int i = 0; i< image.pixels.length; i++) {
			int pixel = image.pixels[i];
			int red = (pixel & 0xFF0000) >> 16;
			int green = (pixel & 0x00FF00) >> 8;
			int blue = (pixel & 0x0000FF);
			image.pixels[i] = (gammaLUT[red] << 16) + (gammaLUT[green] << 8) + gammaLUT[blue];
		}
	}
	
	public static void applyGamma(ImageData image, float gamma) {
		if (gamma == 1.0f) return;
		int[] lut = createGammaLUT(gamma);
		applyGammaLUT(image, lut);
	}
	
	/**
	 * Create a copy of an ImageData object with a different bit depth.
	 * See the docs of {@link ImageData} for details on pixel encoding.
	 */
	public static ImageData changeDepth(ImageData image, int newDepth) {
		ImageData copy = copy(image);
		if (copy.depth != newDepth) {
			copy.depth = newDepth;
			for (int i = 0; i< image.pixels.length; i++) {
				copy.pixels[i] = changePixelDepth(image.pixels[i], image.depth, newDepth);
			}
		}
		return copy;
	}
	
	/**
	 * Compute a new pixel value for the specified depth.
	 * The pixel's color value may be changed, for example when
	 * going from 24bpp RGB to 8bpp grayscale.
	 */
	public static int changePixelDepth(int pixel, int depth, int newDepth) {
		if (depth == newDepth) return pixel;
		
		switch (newDepth) {
		case 1:
			if (depth < 24) return (pixel == 0) ? 0 : 1;
			else {
				int[] argb = decodeToARGB(pixel);
				return (argb[1] == 0 && argb[2] == 0 && argb[3] == 0) ? 0 : 1;
			}
		case 8:
			if (depth < 16) return pixel;
			// Reduce grayscale depth
			if (depth == 16) return (int) Math.ceil(((double) pixel) / 257);
			// Turn the highest channel value (R,G,B) into the grayscale value
			else {
				int[] argb = decodeToARGB(pixel);
				return Math.max(argb[1], Math.max(argb[2], argb[3]));
			}
		case 16:
			if (depth == 1) return 65535;
			if (depth == 8) return pixel * 257;
			else return changePixelDepth(pixel, 24, 8) * 257;
		case 24:
			if (depth == 1) return encodeToInteger(0, 255, 255, 255);
			if (depth == 8) return encodeToInteger(0, pixel, pixel, pixel);
			if (depth == 16) {
				int pixel8bit = changePixelDepth(pixel, 16, 8);
				return encodeToInteger(0, pixel8bit, pixel8bit, pixel8bit);
			}
			return pixel;
		default:
			// Add a fully opaque alpha channel
			return pixel | 0xFF000000;
		}
	}
	
	/**
	 * Decode a pixel value to its ARGB components.
	 */
	public static int[] decodeToARGB(int pixel) {
		return new int[] {
				(pixel & 0xFF000000) >> 24,
				(pixel & 0xFF0000) >> 16,
				(pixel & 0xFF00) >> 8,
				(pixel & 0xFF)
		};
	}

	/**
	 * Encode ARGB components into a 32bit pixel value.
	 */
	public static int encodeToInteger(int a, int r, int g, int b) {
		return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
	}

	/**
	 * Create a copy of an ImageData object with an up- or downscaled resolution.
	 */
	public static ImageData scale(ImageData data, int w, int h) {
		// Convert to a BufferedImage, and use Graphics2D to draw on a resized image. 
		BufferedImage image = toBufferedImage(data);
		
		BufferedImage resizedImage = new BufferedImage(w, h, image.getType());
	    Graphics2D graphics2D = resizedImage.createGraphics();
	    graphics2D.drawImage(image, 0, 0, w, h, null);
	    graphics2D.dispose();
	    
	    return toImageData(resizedImage);
	}
	
	/**
	 * Convert an ImageData object to a BufferedImage.
	 */
	public static BufferedImage toBufferedImage(ImageData data) {
		BufferedImage image = new BufferedImage(data.width, data.height, getBufferedImageTypeForDepth(data.depth));
		for (int x = 0; x < data.width; x++) {
		    for (int y = 0; y < data.height; y++) {
		    	int index = x + (y * data.width);
		    	int value = data.pixels[index];
		    	image.getRaster().setPixel(x, y, toBufferedImagePixel(value, data.depth));
		    }
		}
		return image;
	}
	
	private static int getBufferedImageTypeForDepth(int depth) {
		switch (depth) {
		case 1: return BufferedImage.TYPE_BYTE_BINARY;
		case 8: return BufferedImage.TYPE_BYTE_GRAY;
		case 16: return BufferedImage.TYPE_USHORT_GRAY;
		case 24: return BufferedImage.TYPE_INT_RGB;
		case 32: return BufferedImage.TYPE_INT_ARGB;
		default: throw new IllegalArgumentException("Unsupported bit depth: " + depth);
		}
	}
	
	private static int[] toBufferedImagePixel(int pixel, int depth) {
		switch (depth) {
		case 1:
		case 8:
		case 16:
			// Grayscale -> int[1]
			return new int[] { pixel };
		case 24:
			// RGB -> int[3]
			int[] argb = decodeToARGB(pixel);
			return new int[] { argb[1], argb[2], argb[3] };
		case 32:
			// ARGB -> int[4]
			return decodeToARGB(pixel);
		default:
			throw new IllegalArgumentException("Unsupported bit depth: " + depth);
		}
	}
	
	/**
	 * Convert a BufferedImage to an ImageData object.
	 */
	public static ImageData toImageData(BufferedImage image) {
		ImageData data = initNew(image.getWidth(), image.getHeight(), getBitDepth(image));
		for (int x = 0; x < data.width; x++) {
		    for (int y = 0; y < data.height; y++) {
		    	int index = x + (y * data.width);
		    	int[] value = image.getRaster().getPixel(x, y, (int[]) null);
		    	data.pixels[index] = toImageDataPixel(value, data.depth);
		    }
		}
		return data;
	}
	
	private static int getBitDepth(BufferedImage image) {
		switch (image.getType()) {
			case BufferedImage.TYPE_BYTE_BINARY: return 1;
			case BufferedImage.TYPE_BYTE_GRAY: return 8;
			case BufferedImage.TYPE_USHORT_GRAY: return 16;
			case BufferedImage.TYPE_INT_RGB: return 24;
			default:
				if (image.getAlphaRaster() == null) return 24;
				else return 32;
		}
	}
	
	private static int toImageDataPixel(int[] pixel, int depth) {
		switch (depth) {
		case 1:
		case 8:
		case 16:
			return pixel[0];
		case 24:
			return encodeToInteger(0, pixel[0], pixel[1], pixel[2]);
		case 32:
			return encodeToInteger(pixel[0], pixel[1], pixel[2], pixel[3]);
		default:
			throw new IllegalArgumentException("Unsupported bit depth: " + depth);
		}
	}
}
