package eu.openanalytics.phaedra.imaging.render;

import java.awt.Rectangle;

public class ImageRenderConfig {

	public String format = "jpg";
	public float gamma = 1.0f;
	public float scale = 1.0f;
	public Rectangle region;
	public ChannelRenderConfig[] channelConfigs;
	
	public static class ChannelRenderConfig {
		
		public String name;
		public int rgb;
		public float alpha;
		public int contrastMin;
		public int contrastMax;
		
	}
}
