package eu.openanalytics.phaedra.imaging.jp2k;

import java.io.IOException;

public interface ICodestreamSourceDescriptor {

	public ICodestreamSource create() throws IOException;

}
