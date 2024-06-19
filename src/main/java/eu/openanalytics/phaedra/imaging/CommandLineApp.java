package eu.openanalytics.phaedra.imaging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.util.FileCopyUtils;

import eu.openanalytics.phaedra.imaging.jp2k.CompressionConfig;
import eu.openanalytics.phaedra.imaging.jp2k.ICodec;
import eu.openanalytics.phaedra.imaging.jp2k.ICodestreamSource;
import eu.openanalytics.phaedra.imaging.jp2k.kdu.KakaduLibLoader;
import eu.openanalytics.phaedra.imaging.jp2k.openjpeg.OpenJPEGLibLoader;
import eu.openanalytics.phaedra.imaging.util.ImageDataLoader;
import eu.openanalytics.phaedra.imaging.util.ImageDataUtils;

public class CommandLineApp {

	public static void main(String[] args) {
		try {
			new CommandLineApp().run(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run(String[] args) throws Exception {
		List<String> argList = new ArrayList<>();
		for (int i = 0; i < args.length; i++) {
			argList.add(args[i]);
		}
		String cmd = args[0].toLowerCase();
		
		if (cmd.equals("encode")) encode(argList);
		else if (cmd.equals("decode")) decode(argList);
		else if (cmd.equals("copylibs")) copyLibs(argList);
		else throw new IllegalArgumentException("Unsupported operation: " + cmd);
	}

	public void copyLibs(List<String> argList) throws Exception {
		String destination = getArg("-d", argList);
		OpenJPEGLibLoader.copyLibs(destination);
		KakaduLibLoader.copyLibs(destination);
	}
	
	public void encode(List<String> argList) throws Exception {
		String inputFile = getArg("-i", argList);
		String outputFile = getArg("-o", argList);
		
		ImageData inputData = ImageDataLoader.load(inputFile);
		
		String depthArg = getArg("-depth", argList);
		if (depthArg != null) inputData = ImageDataUtils.changeDepth(inputData, Integer.valueOf(depthArg));
		
		CompressionConfig config = new CompressionConfig();
		
		String reversibleArg = getArg("-reversible", argList);
		if (reversibleArg != null) config.reversible = Boolean.valueOf(reversibleArg);
		
		String psnrArg = getArg("-psnr", argList);
		if (psnrArg != null) config.psnr = Integer.valueOf(psnrArg);
		
		String resLevels = getArg("-resolution-levels", argList);
		if (resLevels != null) config.resolutionLevels = Integer.valueOf(resLevels);
	
		try (ICodec codec = CodecFactory.createCodec()) {
			long start = System.currentTimeMillis();
			codec.compressCodeStream(config, inputData, outputFile);
			long encodeDuration = System.currentTimeMillis() - start;
			long fileSize = new File(outputFile).length();
			System.out.println(String.format("Encoded %d pixels to %d bytes in %d ms", inputData.pixels.length, fileSize, encodeDuration));
		}
	}
	
	public void decode(List<String> argList) throws Exception {
		String inputFile = getArg("-i", argList);
		String outputFile = getArg("-o", argList);
		
		String outputFormat = FilenameUtils.getExtension(outputFile);
		
		float scale = 1.0f;
		String scaleString = getArg("-scale", argList);
		if (scaleString != null) scale = Float.parseFloat(scaleString);
		
		byte[] bytes = FileCopyUtils.copyToByteArray(new File(inputFile));
		
		try (ICodec codec = CodecFactory.createCodec()) {
			ICodestreamSource source = new ICodestreamSource() {
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
			long start = System.currentTimeMillis();
			ImageData imageData = codec.renderImage(scale, source);
			long decodeDuration = System.currentTimeMillis() - start;
			start = System.currentTimeMillis();
			try (FileOutputStream fOut = new FileOutputStream(outputFile)) {
				ImageDataLoader.write(imageData, outputFormat, fOut);
			}
			long writeDuration = System.currentTimeMillis() - start;
			System.out.println(String.format("Decoded %d bytes to %d pixels in %d ms, written to %s file in %d ms", bytes.length, imageData.pixels.length, decodeDuration, outputFormat, writeDuration));
		}
	}
	
	private String getArg(String name, List<String> args) {
		int argIndex = args.indexOf(name);
		if (argIndex < 0) return null;
		if (args.size() <= argIndex) throw new IllegalArgumentException("Not enough arguments");
		return args.get(argIndex + 1);
	}
}
