
package eu.openanalytics.phaedra.imaging.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

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
	
	public static byte[] encodeToFormat(ImageData data, String format) throws IOException {
		BufferedImage bi = toBufferedImage(data);
		BufferedImage bi2 = bi;
		
		if (data.depth != 24) {
			int w = bi.getWidth();
		    int h = bi.getHeight();
		    int[] pixels = new int[w * h];
		    bi.getRGB(0, 0, w, h, pixels, 0, w);
		    bi2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		    bi2.setRGB(0, 0, w, h, pixels, 0, w);
		}
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		//TODO Look for alternative methods. This has pretty bad performance!
		ImageIO.write(bi2, format, bos);
		return bos.toByteArray();
	}
}
