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
package eu.openanalytics.phaedra.imaging.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
		
		// The "channel=[...]" parameter may be provided multiple times
		List<ChannelRenderConfig> channelConfigs = new ArrayList<>();
		String[] channelConfigStrings = parameterMap.get("channel");
		if (channelConfigStrings != null) {
			for (int i = 0; i < channelConfigStrings.length; i++) {
				Map<String, String> channelSettings = Arrays.stream(channelConfigStrings[i].substring(1, channelConfigStrings[i].length() - 1).split(",")).collect(Collectors.toMap(e -> e.split("=")[0], e -> e.split("=")[1]));
				ChannelRenderConfig channelConfig = new ChannelRenderConfig();
				channelConfig.name = channelSettings.get("name");
				if (channelSettings.containsKey("rgb")) channelConfig.rgb = HexFormat.fromHexDigits("ff" + channelSettings.get("rgb")); // Make ARGB before converting to integer
				if (channelSettings.containsKey("alpha")) channelConfig.alpha = Float.parseFloat(channelSettings.get("alpha"));
				if (channelSettings.containsKey("contrast")) channelConfig.contrastMin = Float.parseFloat(channelSettings.get("contrast").split("-")[0]);
				if (channelSettings.containsKey("contrast")) channelConfig.contrastMax = Float.parseFloat(channelSettings.get("contrast").split("-")[1]);
				channelConfigs.add(channelConfig);
			}
		}
		if (!channelConfigs.isEmpty()) {
			cfg.channelConfigs = channelConfigs.toArray(new ChannelRenderConfig[0]);
		}
		
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
		if (from.gamma != null) to.gamma = from.gamma;
		if (from.scale != null) to.scale = from.scale;
		if (from.region != null) to.region = from.region;
		
		if (from.channelConfigs == null) return;
		
		for (int i = 0; i < from.channelConfigs.length; i++) {
			ChannelRenderConfig channelFrom = from.channelConfigs[i];
			
			// Find or create the matching destination channel
			ChannelRenderConfig channelTo = null;
			if (to.channelConfigs != null) channelTo = Arrays.stream(to.channelConfigs).filter(c -> c.name.equals(channelFrom.name)).findAny().orElse(null);
			if (channelTo == null) {
				channelTo = new ChannelRenderConfig().withDefaults();
				channelTo.name = channelFrom.name;
				if (to.channelConfigs == null) to.channelConfigs = new ChannelRenderConfig[] { channelTo };
				else {
					to.channelConfigs = Arrays.copyOf(to.channelConfigs, to.channelConfigs.length + 1);
					to.channelConfigs[to.channelConfigs.length - 1] = channelTo;
				}
			}
			
			if (channelFrom.alpha != null) channelTo.alpha = channelFrom.alpha;
			if (channelFrom.rgb != null) channelTo.rgb = channelFrom.rgb;
			if (channelFrom.contrastMin != null) channelTo.contrastMin = channelFrom.contrastMin;
			if (channelFrom.contrastMax != null) channelTo.contrastMax = channelFrom.contrastMax;
		}
	}
}
