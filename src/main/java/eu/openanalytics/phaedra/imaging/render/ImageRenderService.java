package eu.openanalytics.phaedra.imaging.render;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import eu.openanalytics.phaedra.imaging.ImageData;
import eu.openanalytics.phaedra.imaging.jp2k.ICodec;
import eu.openanalytics.phaedra.imaging.jp2k.ICodestreamSource;
import eu.openanalytics.phaedra.imaging.jp2k.ICodestreamSourceDescriptor;
import eu.openanalytics.phaedra.imaging.util.ImageDataLoader;
import eu.openanalytics.phaedra.imaging.util.ImageDataUtils;

/**
 * A service for rendering composite images, where each image channel
 * is decoded from a JPEG2000 codestream.
 */
public class ImageRenderService {

	private CodecPool codecPool;
	private ExecutorService executor;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public ImageRenderService() {
		codecPool = new CodecPool();
		executor = Executors.newFixedThreadPool(10);
	}
	
	public void close() {
		if (codecPool != null) codecPool.close();
		if (executor != null) executor.shutdownNow();
	}
	
	public byte[] renderImage(ICodestreamSourceDescriptor[] sources, ImageRenderConfig cfg) throws IOException {
		
		Assert.notEmpty(sources, "At least one codestream source must be specified");
		Assert.notNull(cfg, "Image render config must be specified");
		Assert.notEmpty(cfg.channelConfigs, "Image render config must contain at least one channel config");
		Assert.isTrue(sources.length == cfg.channelConfigs.length, "Number of codestream sources must match number of channel configs");
		
		long startTime = System.currentTimeMillis();
		
		// Render channels in parallel.
		List<Future<ImageData>> dataFutures = new ArrayList<>();
		for (int i = 0; i < sources.length; i++) {
			ICodestreamSourceDescriptor sourceDescriptor = sources[i];
			Future<ImageData> dataFuture = useCodecAsync(codec -> {
				ICodestreamSource source = sourceDescriptor.create();
				if (cfg.region == null) {
					return codec.renderImage(cfg.scale, source);
				} else {
					return codec.renderImageRegion(cfg.scale, cfg.region, source);	
				}
			});
			dataFutures.add(dataFuture);
		}
		
		// Collect all rendered channels.
		ImageData[] datas = new ImageData[dataFutures.size()];
		for (int i = 0; i < datas.length; i++) {
			try {
				datas[i] = dataFutures.get(i).get();
			} catch (Exception e) {
				throw new IOException("Error rendering image", e);
			}
		}
		
		long durationMsDecode = System.currentTimeMillis() - startTime;
		startTime = System.currentTimeMillis();
		
		// Blend channels into a single result image.
		ImageData resultImage = new ChannelBlender().blend(datas, cfg);
		ImageDataUtils.applyGamma(resultImage, cfg.gamma);
		
		long durationMsBlend = System.currentTimeMillis() - startTime;
		startTime = System.currentTimeMillis();
		
		// Convert the result image to the desired image format.
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageDataLoader.write(resultImage, cfg.format, bos);
		
		long durationMsFormat = System.currentTimeMillis() - startTime;
		
		logger.debug(String.format("Rendered %d channels to %dx%d pixels in %s: decode [%d ms], blend [%d ms], format [%d ms]", sources.length, resultImage.width, resultImage.height, cfg.format, durationMsDecode, durationMsBlend, durationMsFormat));
		return bos.toByteArray();
	}
	
	@FunctionalInterface
	private static interface CodecOperation<T> {
		public T apply(ICodec codec) throws IOException;
	}
	
	private <T> Future<T> useCodecAsync(CodecOperation<T> operation) throws IOException {
		return executor.submit(() -> {
			return useCodec(operation);
		});
	}
	
	private <T> T useCodec(CodecOperation<T> operation) throws IOException {
		try {
			ICodec codec = codecPool.borrowObject();
			try {
				return operation.apply(codec);
			} finally {
				codecPool.returnObject(codec);
			}
		} catch (Throwable e) {
			if (e.getCause() instanceof IOException) throw (IOException) e.getCause();
			else throw new IOException("Failed to decode image", e);
		}
	}
}
