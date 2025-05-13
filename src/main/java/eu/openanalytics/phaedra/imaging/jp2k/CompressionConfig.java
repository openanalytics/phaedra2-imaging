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
package eu.openanalytics.phaedra.imaging.jp2k;

public class CompressionConfig {

	public int psnr;
	public int resolutionLevels;
	public String order;
	public boolean reversible;
	public String precincts;
	
	public CompressionConfig() {
		psnr = 70;
		resolutionLevels = 9;
		order = "RPCL";
		reversible = false;
		precincts = "{256,256},{256,256},{128,128},{64,128},{32,64},{16,64},{8,32},{4,32},{2,16}";
	}

}
