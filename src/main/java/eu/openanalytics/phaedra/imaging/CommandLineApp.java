/**
 * Phaedra II
 *
 * Copyright (C) 2016-2025 Open Analytics
 *
 * ===========================================================================
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Apache License as published by
 * The Apache Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Apache License for more details.
 *
 * You should have received a copy of the Apache License
 * along with this program.  If not, see <http://www.apache.org/licenses/>
 */
package eu.openanalytics.phaedra.imaging;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

		runCmd(cmd, argList);
	}

	public void server(List<String> argList) throws Exception {
		BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
		
		List<String> cmdLine = readInCmd(inReader);
		String cmd = cmdLine.get(0);
		
		while (!cmd.equalsIgnoreCase("quit")) {
			try {
				runCmd(cmd, cmdLine.subList(1, cmdLine.size()));
				System.out.println("ok");
			} catch (Exception e) {
				System.err.println("error: " + e.getMessage());
			}

			cmdLine = readInCmd(inReader);
			cmd = cmdLine.get(0);
		}
	}
	
	private List<String> readInCmd(BufferedReader inReader) throws IOException {
		String line = inReader.readLine();
		if (line == null) return Collections.singletonList("quit");
		List<String> args = Arrays.stream(line.split(" ")).toList();
		return args;
	}
	
	private void runCmd(String cmd, List<String> argList) throws Exception {
		if (cmd.equals("server")) server(argList);
		else if (cmd.equals("encode")) encode(argList);
		else if (cmd.equals("decode")) decode(argList);
		else if (cmd.equals("copylibs")) copyLibs(argList);
		else throw new IllegalArgumentException("Unsupported operation: " + cmd);
	}
	
	private void copyLibs(List<String> argList) throws Exception {
		String destination = getArg("-d", argList);
		OpenJPEGLibLoader.copyLibs(destination);
		KakaduLibLoader.copyLibs(destination);
	}
	
	private void encode(List<String> argList) throws Exception {
		String inputFile = getArg("-i", argList);
		String outputFile = getArg("-o", argList);
		
		long start = System.currentTimeMillis();
		ImageData inputData = ImageDataLoader.load(inputFile);
		long decodeInputDuration = System.currentTimeMillis() - start;
		
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
			start = System.currentTimeMillis();
			codec.compressCodeStream(config, inputData, outputFile);
			long encodeDuration = System.currentTimeMillis() - start;
			long fileSize = new File(outputFile).length();
			System.out.println(String.format("Encoded %d pixels to %d bytes: %dms to decode input, %dms to encode output", inputData.pixels.length, fileSize, decodeInputDuration, encodeDuration));
		}
	}
	
	private void decode(List<String> argList) throws Exception {
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
