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
package eu.openanalytics.phaedra.imaging;

/**
 * Simple in-memory representation of a single 2D image.
 * Different bit depths are supported, up to 32 bits per pixel.
 * 
 * No information is kept about how the channels or components are encoded in the pixel integers,
 * but usually the following conventions are used:
 * 
 * <ul>
 * 	<li>depth 1: pixel is either zero or not zero</li>
 * 	<li>depth 8: pixel represents a single grayscale channel, value between 0-255</li>
 * 	<li>depth 16: pixel represents a single grayscale channel, value between 0-65535</li>
 * 	<li>depth 24: pixel represents an RGB value, bits 0-7 = B, 8-15 = G, 16-23 = R, 24-31 = unused</li>
 *  <li>depth 32: pixel represents an ARGB value, bits 0-7 = B, 8-15 = G, 16-23 = R, 24-31 = alpha</li>
 * </ul>
 */
public class ImageData {

	public int width;
	public int height;
	public int depth;
	
	public int[] pixels;

}
