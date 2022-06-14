package eu.openanalytics.phaedra.imaging;

import java.util.ArrayList;
import java.util.List;

import eu.openanalytics.phaedra.imaging.jp2k.CompressionConfig;
import eu.openanalytics.phaedra.imaging.jp2k.ICodec;
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
		if (args[0].equalsIgnoreCase("encode")) encode(args);
		else throw new IllegalArgumentException("Unsupported operation: " + args[0]);
	}

	public void encode(String[] args) throws Exception {
		List<String> argList = new ArrayList<>();
		for (int i = 0; i < args.length; i++) {
			argList.add(args[i]);
		}
		
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
			codec.compressCodeStream(config, inputData, outputFile);
		}
	}
	
	private String getArg(String name, List<String> args) {
		int argIndex = args.indexOf(name);
		if (argIndex < 0) return null;
		if (args.size() <= argIndex) throw new IllegalArgumentException("Not enough arguments");
		return args.get(argIndex + 1);
	}
}
