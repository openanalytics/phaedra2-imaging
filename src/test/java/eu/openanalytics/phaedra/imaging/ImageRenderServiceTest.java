package eu.openanalytics.phaedra.imaging;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import eu.openanalytics.phaedra.imaging.jp2k.ICodestreamSource;
import eu.openanalytics.phaedra.imaging.jp2k.openjpeg.OpenJPEGLibLoader;
import eu.openanalytics.phaedra.imaging.jp2k.openjpeg.source.ByteArraySource;
import eu.openanalytics.phaedra.imaging.render.ImageRenderConfig;
import eu.openanalytics.phaedra.imaging.render.ImageRenderConfig.ChannelRenderConfig;
import eu.openanalytics.phaedra.imaging.render.ImageRenderService;

public class ImageRenderServiceTest {

	public static void main(String[] args) {
		try {
			new ImageRenderServiceTest().testRender();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRender() throws IOException {
		OpenJPEGLibLoader.load();
		
		ICodestreamSource[] sources = {
				ByteArraySource.fromClassPath("samples/sample-16bit-raw1.jp2k"),
				ByteArraySource.fromClassPath("samples/sample-16bit-raw2.jp2k"),
				ByteArraySource.fromClassPath("samples/sample-16bit-raw3.jp2k")
		};
		ImageRenderConfig cfg = new ImageRenderConfig();
		cfg.channelConfigs = new ChannelRenderConfig[sources.length];
		for (int i = 0; i < sources.length; i++) {
			cfg.channelConfigs[i] = new ChannelRenderConfig();
			cfg.channelConfigs[i].name = "Raw" + (i + 1);
			cfg.channelConfigs[i].contrastMax = 5000;
			cfg.channelConfigs[i].rgb = 255 << (i*8);
		}
		
		long start = System.currentTimeMillis();
		ImageRenderService svc = new ImageRenderService();
		long duration = System.currentTimeMillis() - start;
		System.out.println(String.format("ImageRenderService initialized in %d ms", duration));
		
		start = System.currentTimeMillis();
		byte[] rendered = svc.renderImage(sources, cfg);
		duration = System.currentTimeMillis() - start;
		System.out.println(String.format("Composite image (%d channels) rendered to %d bytes in %d ms", sources.length, rendered.length, duration));
	}
}
