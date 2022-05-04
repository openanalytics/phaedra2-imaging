package eu.openanalytics.phaedra.imaging.jp2k;

import java.awt.Rectangle;
import java.io.IOException;

import eu.openanalytics.phaedra.imaging.ImageData;

public interface ICodec extends AutoCloseable {

	public void open() throws IOException;
	
	public void close();
	
	public void setBgColor(int bgColor);
	
	public int[] getDimensions(ICodestreamSource source) throws IOException;
	
	public int getBitDepth(ICodestreamSource source) throws IOException;
	
	public ImageData renderImage(int w, int h, ICodestreamSource source) throws IOException;

	public ImageData renderImage(float scale, ICodestreamSource source) throws IOException;
	
	public ImageData renderImageRegion(float scale, Rectangle region, ICodestreamSource source) throws IOException;
	
}
