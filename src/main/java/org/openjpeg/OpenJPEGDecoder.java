package org.openjpeg;

public class OpenJPEGDecoder {

	/**
	 * Get the size of a JPEG2000 image.
	 * 
	 * @param src The source representing the image.
	 * @return The size of the image, as a 3-element array: [width, height, depth].
	 */
	public native int[] getSize(JavaByteSource src);
	
	/**
	 * Decode a JPEG2000 image, optionally discarding resolution levels.
	 * 
	 * @param src The source representing the image.
	 * @param discardLevels The number of resolution levels to discard.
	 * @return The first component of the decoded image.
	 */
	public native ImagePixels decode(JavaByteSource src, int discardLevels, int threads);
	
	/**
	 * Decode a region of a JPEG2000 image, optionally discarding resolution levels.
	 * 
	 * @param src The source representing the image.
	 * @param discardLevels The number of resolution levels to discard.
	 * @param region The region of the image to decode as an array of coordinates: [x0, y0, x1, y1].
	 * @return The first component of the decoded image.
	 */
	public native ImagePixels decode(JavaByteSource src, int discardLevels, int[] region, int threads);
}
