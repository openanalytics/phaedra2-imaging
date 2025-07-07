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

import java.io.IOException;

import org.junit.jupiter.api.Test;

import eu.openanalytics.phaedra.imaging.jp2k.ICodestreamSourceDescriptor;
import eu.openanalytics.phaedra.imaging.jp2k.openjpeg.OpenJPEGLibLoader;
import eu.openanalytics.phaedra.imaging.jp2k.source.ClassPathSourceDescriptor;
import eu.openanalytics.phaedra.imaging.render.ImageRenderConfig;
import eu.openanalytics.phaedra.imaging.render.ImageRenderConfig.ChannelRenderConfig;
import eu.openanalytics.phaedra.imaging.render.ImageRenderService;

public class ImageRenderServiceTest {
	
	@Test
	public void testRender() throws IOException {
		OpenJPEGLibLoader.load();
		
		ICodestreamSourceDescriptor[] sourceDescriptors = {
				new ClassPathSourceDescriptor("samples/sample-16bit-raw1.jp2k"),
				new ClassPathSourceDescriptor("samples/sample-16bit-raw2.jp2k"),
				new ClassPathSourceDescriptor("samples/sample-16bit-raw3.jp2k")
		};
		ImageRenderConfig cfg = new ImageRenderConfig().withDefaults();
		cfg.channelConfigs = new ChannelRenderConfig[sourceDescriptors.length];
		for (int i = 0; i < sourceDescriptors.length; i++) {
			cfg.channelConfigs[i] = new ChannelRenderConfig().withDefaults();
			cfg.channelConfigs[i].name = "Raw" + (i + 1);
			cfg.channelConfigs[i].rgb = 255 << (i*8);
		}
		
		ImageRenderService svc = null;
		try {
			long start = System.currentTimeMillis();
			svc = new ImageRenderService();
			long duration = System.currentTimeMillis() - start;
			System.out.println(String.format("ImageRenderService initialized in %d ms", duration));
			
			start = System.currentTimeMillis();
			byte[] rendered = svc.renderImage(sourceDescriptors, cfg);
			duration = System.currentTimeMillis() - start;
			System.out.println(String.format("Composite image (%d channels) rendered to %d bytes in %d ms", sourceDescriptors.length, rendered.length, duration));
		} finally {
			if (svc != null) svc.close();
		}
	}
}
