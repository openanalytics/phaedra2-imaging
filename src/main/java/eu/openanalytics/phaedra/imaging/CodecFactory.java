package eu.openanalytics.phaedra.imaging;

import java.io.IOException;

import eu.openanalytics.phaedra.imaging.jp2k.ICodec;
import eu.openanalytics.phaedra.imaging.jp2k.kdu.KakaduCodec;
import eu.openanalytics.phaedra.imaging.jp2k.openjpeg.OpenJPEGCodec;

public class CodecFactory {

	public static ICodec createCodec() throws IOException {
		ICodec codec = null;
		
		String codecId = System.getProperty("phaedra2.imaging.jp2k.codec", "openjpeg");
		if (codecId.equalsIgnoreCase("kakadu") || codecId.equalsIgnoreCase("kdu")) {
			codec = new KakaduCodec();
		} else {
			codec = new OpenJPEGCodec();
		}
		
		codec.open();
		return codec;
	}

}
