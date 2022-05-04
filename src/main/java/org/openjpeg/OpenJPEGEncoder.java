package org.openjpeg;

public class OpenJPEGEncoder {

	public native void encode(ImagePixels image, String outputFile, EncodingParameters parameters);

}
