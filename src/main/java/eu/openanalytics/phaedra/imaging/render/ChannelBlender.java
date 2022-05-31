package eu.openanalytics.phaedra.imaging.render;

import org.springframework.util.Assert;

import eu.openanalytics.phaedra.imaging.ImageData;
import eu.openanalytics.phaedra.imaging.render.ImageRenderConfig.ChannelRenderConfig;
import eu.openanalytics.phaedra.imaging.util.ImageDataUtils;

public class ChannelBlender {

	/**
	 * Blend a number of image channels onto a single RGB image, starting from a black background.
	 * 
	 * @param channels The image data for each channel.
	 * @param cfg The image render config, containing channel render config for each channel.
	 * @return A blended RGB image.
	 */
	public ImageData blend(ImageData[] channels, ImageRenderConfig cfg) {

		Assert.notEmpty(channels, "At least one channel must be provided for blending");
		
		// Start with an opaque, black RGB image.
		ImageData output = ImageDataUtils.initNew(channels[0].width, channels[0].height, 24);
		
		for (int i=0; i < channels.length; i++) {
			if (channels[i].depth == 1) blendOverlay(channels[i], output, cfg.channelConfigs[i]);
			else if (channels[i].depth <= 16) blendRaw(channels[i], output, cfg.channelConfigs[i]);
			else throw new IllegalArgumentException("Unsupported channel depth: " + channels[i].depth + ", unable to blend channel " + cfg.channelConfigs[i].name);
		}
		
		return output;
	}
	
	private void blendRaw(ImageData source, ImageData target, ChannelRenderConfig config) {
		for (int i=0; i<source.pixels.length; i++) {

			int srcValue = source.pixels[i];
			int destValue = target.pixels[i];
			
			// Apply contrast stretching and reduce to 8bit
			double contrastCeil = Math.pow(2, source.depth) - 1;
			int[] contrast = new int[] {
					(int) (config.contrastMin * contrastCeil),
					(int) (config.contrastMax * contrastCeil)
			};
			int grayValue = (int) (applyContrastStretch(srcValue, contrast) * 255);

			// Start from the configured color mask.
			int maskR = (config.rgb & 0xFF0000) >> 16;
			int maskG = (config.rgb & 0xFF00) >> 8;
			int maskB = (config.rgb & 0xFF);

			// Convert to HSB, adjust brightness using grayValue, convert back to RGB
			float[] colorMaskHSB = java.awt.Color.RGBtoHSB(maskR, maskG, maskB, null);
			colorMaskHSB[2] = colorMaskHSB[2] * (grayValue / 255.0f);
			srcValue = java.awt.Color.HSBtoRGB(colorMaskHSB[0], colorMaskHSB[1], colorMaskHSB[2]);

			int srcR = (srcValue & 0xFF0000) >> 16;
			int srcG = (srcValue & 0xFF00) >> 8;
			int srcB = (srcValue & 0xFF);

			int destR = (destValue & 0xFF0000) >> 16;
			int destG = (destValue & 0xFF00) >> 8;
			int destB = (destValue & 0xFF);

			// The maximum of the two pixels in each [R,G,B] component is taken.
			int newR = Math.max(destR,srcR);
			int newG = Math.max(destG,srcG);
			int newB = Math.max(destB,srcB);
			int newValue = (newR << 16) | (newG << 8) | newB;

			target.pixels[i] = newValue;
		}
	}
	
	private double applyContrastStretch(int pixelValue, int[] contrast) {
		// Clip value to [min,max]
		int clippedValue = Math.max(Math.min(pixelValue, contrast[1]), contrast[0]);
		// Scale withing contrast range
		double stretchedFraction = (clippedValue - contrast[0]) / (double) (contrast[1] - contrast[0]);
		return stretchedFraction;
	}
	
	private void blendOverlay(ImageData source, ImageData target, ChannelRenderConfig config) {
		for (int i=0; i<source.pixels.length; i++) {
			// If the overlay contains a nonzero pixel, completely replace the target pixel with the desired color.
			if (source.pixels[i] > 0) target.pixels[i] = config.rgb;
		}
	}
}
