
package eu.openanalytics.phaedra.imaging.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import eu.openanalytics.phaedra.imaging.ImageData;

public class ImageDataUtils {

	public static ImageData scale(ImageData data, int w, int h) {
		BufferedImage image = toBufferedImage(data);
		
		BufferedImage resizedImage = new BufferedImage(w, h, BufferedImage.TYPE_USHORT_GRAY);
	    Graphics2D graphics2D = resizedImage.createGraphics();
	    graphics2D.drawImage(image, 0, 0, w, h, null);
	    graphics2D.dispose();
	    
	    return toImageData(resizedImage);
	}
	
	public static BufferedImage toBufferedImage(ImageData data) {
		BufferedImage image = new BufferedImage(data.width, data.height, BufferedImage.TYPE_USHORT_GRAY);
		WritableRaster raster = image.getRaster();
		for (int x = 0; x < data.width; x++) {
		    for (int y = 0; y < data.height; y++) {
		    	int index = x + (y * data.width);
		    	raster.setDataElements(x, y, new short[] { (short) data.pixels[index] });
		    }
		}
		return image;
	}
	
	public static ImageData toImageData(BufferedImage image) {
		ImageData data = new ImageData();
		data.width = image.getWidth();
		data.height = image.getHeight();
		data.depth = 16;
		data.pixels = new int[ data.width * data.height ];
		
		WritableRaster raster = image.getRaster();
		for (int x = 0; x < data.width; x++) {
		    for (int y = 0; y < data.height; y++) {
		    	int index = x + (y * data.width);
		    	data.pixels[index] = raster.getPixel(x, y, (int[]) null)[0];
		    }
		}
		
		return data;
	}

}
