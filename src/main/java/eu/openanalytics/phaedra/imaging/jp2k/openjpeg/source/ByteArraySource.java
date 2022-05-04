package eu.openanalytics.phaedra.imaging.jp2k.openjpeg.source;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.springframework.util.FileCopyUtils;

public class ByteArraySource extends GenericByteSource {

	private byte[] array;
	
	public ByteArraySource(byte[] array) {
		super();
		this.array = array;
	}
	
	@Override
	protected long doGetSize() {
		return array.length;
	}
	
	@Override
	protected byte[] doGetBytes(long pos, int len) throws IOException {
		int start = (int) pos;
		return Arrays.copyOfRange(array, start, start + len);
	}
	
	public static ByteArraySource fromClassPath(String path) throws IOException {
		InputStream input = ByteArraySource.class.getClassLoader().getResourceAsStream(path);
		byte[] bytes = FileCopyUtils.copyToByteArray(input);
		return new ByteArraySource(bytes);
	}
}
