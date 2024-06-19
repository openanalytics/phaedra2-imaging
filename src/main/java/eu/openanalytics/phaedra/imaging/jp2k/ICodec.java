package eu.openanalytics.phaedra.imaging.jp2k;

import java.io.IOException;

import eu.openanalytics.phaedra.imaging.ImageData;
import eu.openanalytics.phaedra.imaging.util.Rectangle;

public interface ICodec extends AutoCloseable {

	/**
	 * Initialize this codec, making it ready for use.
	 */
	public void open() throws IOException;

	/**
	 * Close this codec, cleaning up any resources it was consuming.
	 */
	public void close();
	
	/**
	 * Set the default background color to be used when rendering images.
	 */
	public void setBgColor(int bgColor);
	
	/**
	 * Obtain the image dimensions (x,y) of a codestream source.
	 */
	public int[] getDimensions(ICodestreamSource source) throws IOException;
	
	/**
	 * Obtain the bit depth (bits per pixel) of a codestream source.
	 */
	public int getBitDepth(ICodestreamSource source) throws IOException;

	/**
	 * Render an image using the specified dimensions. If needed, scaling will be applied.
	 */
	public ImageData renderImage(int w, int h, ICodestreamSource source) throws IOException;

	/**
	 * Render an image using the specified scale factor.
	 */
	public ImageData renderImage(float scale, ICodestreamSource source) throws IOException;
	
	/**
	 * Render an image region using the specified scale factor.
	 */
	public ImageData renderImageRegion(float scale, Rectangle region, ICodestreamSource source) throws IOException;
	
	/**
	 * Compress an image into a JPEG2000 codestream, using the specified compression config.
	 */
	public void compressCodeStream(CompressionConfig config, ImageData data, String outputFile) throws IOException;
}
