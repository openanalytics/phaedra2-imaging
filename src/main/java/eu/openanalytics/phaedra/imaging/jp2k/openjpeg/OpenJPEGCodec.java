package eu.openanalytics.phaedra.imaging.jp2k.openjpeg;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.openjpeg.ImagePixels;
import org.openjpeg.JavaByteSource;
import org.openjpeg.OpenJPEGDecoder;

import eu.openanalytics.phaedra.imaging.ImageData;
import eu.openanalytics.phaedra.imaging.jp2k.ICodec;
import eu.openanalytics.phaedra.imaging.jp2k.ICodestreamSource;
import eu.openanalytics.phaedra.imaging.util.ImageDataUtils;

public class OpenJPEGCodec implements ICodec {

	static {
		OpenJPEGLibLoader.load();
	}
	
	private Lock lock = new ReentrantLock();
	
	private int bgColor = 0x0;
	
	private OpenJPEGDecoder decoder;
	
	@Override
	public void open() throws IOException {
		if (decoder != null) return;
		lock.lock();
		try {
			decoder = new OpenJPEGDecoder();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void close() {
		lock.lock();
		decoder = null;
		lock.unlock();
	}

	@Override
	public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
	}

	@Override
	public int[] getDimensions(ICodestreamSource source) throws IOException {
		lock.lock();
		try {
			int[] size = decoder.getSize(asByteSource(source));
			return Arrays.copyOf(size, 2);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public int getBitDepth(ICodestreamSource source) throws IOException {
		lock.lock();
		try {
			int[] size = decoder.getSize(asByteSource(source));
			return size[2];
		} finally {
			lock.unlock();
		}
	}

	@Override
	public ImageData renderImage(int w, int h, ICodestreamSource source) throws IOException {
		lock.lock();
		try {
			// Decode while maintaining aspect ratio.
			int[] fullSize = decoder.getSize(asByteSource(source));
			float scale = Math.min(((float) w) / fullSize[0], ((float) h) / fullSize[1]);
			ImageData decodedImage = decode(source, fullSize, scale, null);
			
			if (decodedImage.width == w && decodedImage.height == h) {
				return decodedImage;
			} else {
				// The scaling factor may contain a rounding error.
				// In this case, copy onto an image that matches exactly the requested size, and apply scaling.
				return ImageDataUtils.scale(decodedImage, w, h);
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public ImageData renderImage(float scale, ICodestreamSource source) throws IOException {
		lock.lock();
		try {
			int[] fullSize = decoder.getSize(asByteSource(source));
			return decode(source, fullSize, scale, null);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public ImageData renderImageRegion(float scale, Rectangle region, ICodestreamSource source) throws IOException {
		lock.lock();
		try {
			int[] fullSize = decoder.getSize(asByteSource(source));
			// If the region falls outside the image, render only the part within the image.
			Rectangle clippedRegion = region.intersection(new Rectangle(0, 0, fullSize[0], fullSize[1]));
			if (clippedRegion.width == 0 || clippedRegion.height == 0) throw new RuntimeException("Cannot render image region: " + clippedRegion); 
			return decode(source, fullSize, scale, clippedRegion);
		} finally {
			lock.unlock();
		}
	}
	
	private ImageData decode(ICodestreamSource source, int[] fullSize, float scale, Rectangle region) {
		int discardLevels = calculateDiscardLevels(fullSize, scale);
		
		int[] regionPoints = null;
		if (region != null) {
			regionPoints = new int[] { region.x, region.y, region.x + region.width, region.y + region.height };
		}
		
		ImagePixels img = decoder.decode(asByteSource(source), discardLevels, regionPoints);
		ImageData data = createImageData(img.width, img.height, img.depth);
		data.pixels = img.pixels;
		
		// If the requested scale was not an exact pow2 scale, perform additional scaling.
		int[] expectedSize = { (int) Math.ceil(fullSize[0] * scale), (int) Math.ceil(fullSize[1] * scale) };
		if (region != null) {
			expectedSize[0] = (int) Math.ceil(region.width * scale);
			expectedSize[1] = (int) Math.ceil(region.height * scale);
		}
		if (expectedSize[0] != img.width || expectedSize[1] != img.height) {
			data = ImageDataUtils.scale(data, expectedSize[0], expectedSize[1]);	
		}
		
		return data;
	}

	private int calculateDiscardLevels(int[] fullSize, float scale) {
		int discard = 0;
		if (scale >= 1.0f) return discard;
		
		// Find the discard levels needed to obtain a scaled image.
		int[] size = { fullSize[0], fullSize[1] };
		while (size[0] > fullSize[0] * scale) {
			size[0] /= 2;
			size[1] /= 2;
			discard++;
		}
		return discard;
	}
	
	private ImageData createImageData(int w, int h, int depth) {
		ImageData data = new ImageData();
		data.width = w;
		data.height = h;
		data.depth = depth;
		data.pixels = new int[w * h];
		Arrays.fill(data.pixels, bgColor);
		return data;
	}
	
	private JavaByteSource asByteSource(ICodestreamSource source) {
		if (source instanceof JavaByteSource) return (JavaByteSource) source;
		else throw new RuntimeException("Unsupported OpenJPEG source type: " + source.getClass());
	}
}
