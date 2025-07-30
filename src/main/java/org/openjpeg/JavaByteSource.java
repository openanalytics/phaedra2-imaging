package org.openjpeg;

/**
 * <p>
 * This class represents an image source, which could be a File,
 * but also another type of source, such as a stream, a byte array,
 * a HTTP response, ...
 * </p>
 * <p>
 * Note that instantiating this class creates native resources, so it
 * is very important to call {@link close} when the source is no longer needed.
 * </p>
 */
public class JavaByteSource implements AutoCloseable {

	private int srcType;
	private long nativePointer;
	
	public final static int SRC_TYPE_J2K = 0;
	public final static int SRC_TYPE_JP2 = 1;
	
	private static native void initClass();
	static {
		initClass();
	}
	
	public JavaByteSource(int srcType) {
		this.srcType = srcType;
		init();
	}
	
	private native void init();
	
	public boolean seek(long pos) {
		return false;
	}
	
	public long skip(long len) {
		return 0L;
	}
	
	public long getPos() {
		return 0L;
	}
	
	public long getSize() {
		return 0L;
	}
	
	public int read(int len) {
		return 0;
	}
	
	protected native void addBytesRead(byte[] buf, int offset, int len);
	
	public native void close();
}
