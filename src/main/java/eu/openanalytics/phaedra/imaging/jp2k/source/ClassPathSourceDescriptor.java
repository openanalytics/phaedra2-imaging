package eu.openanalytics.phaedra.imaging.jp2k.source;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.util.FileCopyUtils;

import eu.openanalytics.phaedra.imaging.jp2k.ICodestreamSource;
import eu.openanalytics.phaedra.imaging.jp2k.ICodestreamSourceDescriptor;
import eu.openanalytics.phaedra.imaging.jp2k.openjpeg.source.ByteArraySource;

public class ClassPathSourceDescriptor implements ICodestreamSourceDescriptor {

	private String path;
	
	public ClassPathSourceDescriptor(String path) {
		this.path = path;
	}
	
	@Override
	public ICodestreamSource create() throws IOException {
		InputStream input = ByteArraySource.class.getClassLoader().getResourceAsStream(path);
		byte[] bytes = FileCopyUtils.copyToByteArray(input);
		return new ByteArraySource(bytes);
	}
}
