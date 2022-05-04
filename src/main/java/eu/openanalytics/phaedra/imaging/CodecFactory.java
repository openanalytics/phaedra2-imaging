package eu.openanalytics.phaedra.imaging;

import java.io.IOException;

import eu.openanalytics.phaedra.imaging.jp2k.ICodec;
import eu.openanalytics.phaedra.imaging.jp2k.openjpeg.OpenJPEGCodec;

public class CodecFactory {

	public static ICodec createCodec() throws IOException {
		ICodec codec = new OpenJPEGCodec();
		codec.open();
		return codec;
	}

}
