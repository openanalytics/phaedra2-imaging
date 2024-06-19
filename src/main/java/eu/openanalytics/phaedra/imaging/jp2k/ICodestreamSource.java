package eu.openanalytics.phaedra.imaging.jp2k;

import java.io.IOException;

public interface ICodestreamSource {

	public long getSize() throws IOException;

	public byte[] getBytes(long pos, int len) throws IOException;

}
