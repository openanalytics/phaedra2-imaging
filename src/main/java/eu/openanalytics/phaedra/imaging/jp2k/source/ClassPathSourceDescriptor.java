package eu.openanalytics.phaedra.imaging.jp2k.source;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.springframework.util.FileCopyUtils;

import eu.openanalytics.phaedra.imaging.jp2k.ICodestreamSource;
import eu.openanalytics.phaedra.imaging.jp2k.ICodestreamSourceDescriptor;
import loci.formats.cache.ByteArraySource;

public class ClassPathSourceDescriptor implements ICodestreamSourceDescriptor {

	private String path;
	
	public ClassPathSourceDescriptor(String path) {
		this.path = path;
	}
	
	@Override
	public ICodestreamSource create() throws IOException {
		InputStream input = ByteArraySource.class.getClassLoader().getResourceAsStream(path);
		byte[] bytes = FileCopyUtils.copyToByteArray(input);
		return new ICodestreamSource() {
			@Override
			public long getSize() throws IOException {
				return bytes.length;
			}
			@Override
			public byte[] getBytes(long pos, int len) throws IOException {
				int start = (int) pos;
				return Arrays.copyOfRange(bytes, start, start + len);
			}
		};
	}
}
