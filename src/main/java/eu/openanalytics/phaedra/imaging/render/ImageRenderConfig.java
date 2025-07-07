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
package eu.openanalytics.phaedra.imaging.render;

import java.util.HexFormat;

import eu.openanalytics.phaedra.imaging.util.ImageDataUtils;
import eu.openanalytics.phaedra.imaging.util.Rectangle;

public class ImageRenderConfig {

	public String format;
	public Float gamma;
	public Float scale;
	public Rectangle region;
	public ChannelRenderConfig[] channelConfigs;
	
	public ImageRenderConfig() {
		// Default constructor
	}
	
	public ImageRenderConfig(String... channels) {
		channelConfigs = new ChannelRenderConfig[channels.length];
		for (int i = 0; i < channels.length; i++) {
			channelConfigs[i] = new ChannelRenderConfig(channels[i]);
		}
	}
	
	public ImageRenderConfig withDefaults() {
		fillDefaults();
		return this;
	}
	
	public void fillDefaults() {
		format = "jpg";
		gamma = 1.0f;
		scale = 1.0f;
		if (channelConfigs != null) {
			for (int i = 0; i < channelConfigs.length; i++) {
				channelConfigs[i].fillDefaults();
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("ImageRenderConfig [format=%s, gamma=%f, scale=%f, region=%s]%s", format, gamma, scale, region, System.getProperty("line.separator")));
		if (channelConfigs != null) {
			for (int i = 0; i < channelConfigs.length; i++) {
				sb.append("-" + channelConfigs[i].toString() + System.getProperty("line.separator"));
			}
		}
		return sb.toString();
	}
	
	public static class ChannelRenderConfig {
		
		public String name;
		public Integer rgb;
		public Float alpha;
		public Float contrastMin;
		public Float contrastMax;
		
		public ChannelRenderConfig() {
			// Default constructor
		}
		
		public ChannelRenderConfig(String name) {
			this.name = name;
		}
		
		public ChannelRenderConfig withDefaults() {
			fillDefaults();
			return this;
		}
		
		public void fillDefaults() {
			rgb = ImageDataUtils.encodeToInteger(255, 255, 255, 255);
			alpha = 1.0f;
			contrastMin = 0.0f;
			contrastMax = 1.0f;
		}
		
		@Override
		public String toString() {
			return String.format("Channel [name=%s, rgb=%s, alpha=%f, contrast=%f-%f]", name, HexFormat.of().toHexDigits(rgb), alpha, contrastMin, contrastMax);
		}
	}
}
