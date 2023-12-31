package eu.openanalytics.phaedra.imaging.jp2k.openjpeg.source;

import java.io.IOException;
import java.util.Arrays;

public class ByteArraySource extends GenericByteSource {

	private byte[] array;
	
//	private final Logger logger = LoggerFactory.getLogger(getClass());
	
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
//		logger.debug(String.format("Bytes requested from source: @%d + %d", pos, len));
		int start = (int) pos;
		return Arrays.copyOfRange(array, start, start + len);
	}
	
}
