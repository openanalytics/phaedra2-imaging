package eu.openanalytics.phaedra.imaging.render;

import java.awt.Rectangle;

import eu.openanalytics.phaedra.imaging.util.ImageDataUtils;

public class ImageRenderConfig {

	public String format = "jpg";
	public float gamma = 1.0f;
	public float scale = 1.0f;
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
	
	public static class ChannelRenderConfig {
		
		public String name;
		public int rgb = ImageDataUtils.encodeToInteger(255, 255, 255, 255);
		public float alpha = 1.0f;
		public float contrastMin = 0.0f;
		public float contrastMax = 1.0f;
		
		public ChannelRenderConfig() {
			// Default constructor
		}
		
		public ChannelRenderConfig(String name) {
			this.name = name;
		}
	}
}
