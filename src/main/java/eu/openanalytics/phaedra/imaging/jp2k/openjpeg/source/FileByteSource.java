package eu.openanalytics.phaedra.imaging.jp2k.openjpeg.source;

import java.io.IOException;
import java.io.RandomAccessFile;

import org.openjpeg.JavaByteSource;

import eu.openanalytics.phaedra.imaging.jp2k.ICodestreamSource;

public class FileByteSource extends JavaByteSource implements ICodestreamSource {
	
	private RandomAccessFile raf;
	private long fileSize;
	
	public FileByteSource(String filePath, int type) throws IOException {
		super(type);
		raf = new RandomAccessFile(filePath, "r");
		fileSize = raf.length();
	}
	
	@Override
	public void close() {
		super.close();
		try {
			raf.close();
		} catch (IOException e) {}
	}
	
	@Override
	public boolean seek(long pos) {
		if (pos >= fileSize) return false;
		try {
			raf.seek(pos);
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	@Override
	public long skip(long len) {
		long currentPos = getPos();
		long canSkip = Math.min(len, (fileSize - currentPos));
		long newPos = currentPos + canSkip;
		
		if (newPos == fileSize) {
			try { raf.seek(fileSize); } catch (IOException e) {}
			return -1;
		}
		else seek(newPos);
		return canSkip;
	}
	
	@Override
	public long getPos() {
		try {
			return raf.getFilePointer();
		} catch (IOException e) {
			return -1;
		}
	}
	
	@Override
	public long getSize() {
		return fileSize;
	}
	
	@Override
	public int read(int len) {
		byte[] buf = new byte[len];
		try {
			int bytesRead = raf.read(buf);
			addBytesRead(buf, 0, bytesRead);
			return bytesRead;
		} catch (IOException e) {
			return 0;
		}
	}
	
}
