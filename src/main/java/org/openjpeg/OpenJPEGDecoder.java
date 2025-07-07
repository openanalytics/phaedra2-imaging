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
	public native ImagePixels decode(JavaByteSource src, int discardLevels);
	
	/**
	 * Decode a region of a JPEG2000 image, optionally discarding resolution levels.
	 * 
	 * @param src The source representing the image.
	 * @param discardLevels The number of resolution levels to discard.
	 * @param region The region of the image to decode as an array of coordinates: [x0, y0, x1, y1].
	 * @return The first component of the decoded image.
	 */
	public native ImagePixels decode(JavaByteSource src, int discardLevels, int[] region);
}
