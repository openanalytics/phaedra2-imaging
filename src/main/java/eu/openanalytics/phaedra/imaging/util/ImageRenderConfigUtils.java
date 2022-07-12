package eu.openanalytics.phaedra.imaging.util;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;

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
	
	public static ImageRenderConfig parseFromParameters(Map<String, String[]> parameterMap) {
		ImageRenderConfig cfg = new ImageRenderConfig();
		findParameterValue(parameterMap, "format", false, v -> cfg.format = v);
		findParameterValue(parameterMap, "gamma", true, v-> cfg.gamma = Float.valueOf(v));
		findParameterValue(parameterMap, "scale", true, v-> cfg.scale = Float.valueOf(v));
		findParameterValue(parameterMap, "region", true, v-> {
			int[] args = Arrays.stream(v.split(",")).mapToInt(a -> Integer.valueOf(a)).toArray();
			cfg.region = new Rectangle(args[0], args[1], args[2], args[3]);	
		});
		return cfg;
	}
	
	private static void findParameterValue(Map<String, String[]> parameterMap, String key, boolean skipMissing, Consumer<String> valueConsumer) {
		String[] values = parameterMap.get(key);
		if (values == null || values.length == 0) {
			if (skipMissing) return;
			else valueConsumer.accept(null);
		} else {
			valueConsumer.accept(values[0]);
		}
	}
	
	public static void merge(ImageRenderConfig from, ImageRenderConfig to) {
		if (from.format != null) to.format = from.format;
		if (from.gamma != 0) to.gamma = from.gamma;
		if (from.scale != 0) to.scale = from.scale;
		if (from.region != null) to.region = from.region;
	}
}
