package eu.openanalytics.phaedra.imaging.jp2k.openjpeg.source;

import java.io.IOException;

import org.openjpeg.JavaByteSource;

import eu.openanalytics.phaedra.imaging.jp2k.ICodestreamSource;

public abstract class GenericByteSource extends JavaByteSource implements ICodestreamSource {
	
	private long pos;
	
	public GenericByteSource() {
		super(SRC_TYPE_J2K);
		this.pos = 0;
	}
	
	@Override
	public boolean seek(long pos) {
		this.pos = pos;
		return true;
	}
	
	@Override
	public long skip(long len) {
		long currentPos = getPos();
		long size = getSize();
		long canSkip = Math.min(len, size - currentPos);
		long newPos = currentPos + canSkip;
		
		seek(newPos);
		if (newPos == size) return -1;
		else return canSkip;
	}
	
	@Override
	public long getPos() {
		return pos;
	}
	
	@Override
	public long getSize() {
		return doGetSize();
	}
	
	@Override
	public int read(int len) {
		try {
			byte[] data = doGetBytes(pos, len);
			pos += data.length;
			addBytesRead(data, 0, data.length);
			return data.length;
		} catch (IOException e) {
			return 0;
		}
	}
	
	protected abstract long doGetSize();
	
	protected abstract byte[] doGetBytes(long pos, int len) throws IOException;
}
