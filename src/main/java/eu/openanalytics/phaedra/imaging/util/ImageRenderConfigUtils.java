package eu.openanalytics.phaedra.imaging.util;

import eu.openanalytics.phaedra.imaging.render.ImageRenderConfig;
import eu.openanalytics.phaedra.imaging.render.ImageRenderConfig.ChannelRenderConfig;

public class ImageRenderConfigUtils {

	public static ImageRenderConfig copy(ImageRenderConfig cfg) {
		ImageRenderConfig copy = new ImageRenderConfig();
		copy.format = cfg.format;
		copy.gamma = cfg.gamma;
		copy.scale = cfg.scale;
		copy.region = cfg.region;
		copy.channelConfigs = new ChannelRenderConfig[cfg.channelConfigs.length];
		for (int i = 0; i < copy.channelConfigs.length; i++) {
			copy.channelConfigs[i] = copy(cfg.channelConfigs[i]);
		}
		return copy;
	}
	
	public static ChannelRenderConfig copy(ChannelRenderConfig cfg) {
		ChannelRenderConfig copy = new ChannelRenderConfig();
		copy.alpha = cfg.alpha;
		copy.contrastMin = cfg.contrastMin;
		copy.contrastMax = cfg.contrastMax;
		copy.name = cfg.name;
		copy.rgb = cfg.rgb;
		return copy;
	}
}
