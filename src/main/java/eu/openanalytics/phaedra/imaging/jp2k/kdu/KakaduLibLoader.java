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
package eu.openanalytics.phaedra.imaging.jp2k.kdu;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.util.FileCopyUtils;


public class KakaduLibLoader {

	private static boolean isLoaded;
	
	private static final String[] LIBS_LINUX = {
			"native/linux/libkdu_jni.so"
	};
	
	private static final String[] LIBS_WINDOWS = {
			"native/windows/kdu_jni.dll",
	};
	
	private static final String[] REQUIRED_LIBS = {
			"kdu_v74R",
			"kdu_a74R"
	};
	
	public static boolean isLoaded() {
		return isLoaded;
	}
	
	public static void load() {
		if (isLoaded) {
			return;
		}
		
		loadRequiredLibs();
		
		List<String> libPaths = Collections.emptyList();
		try {
			Path tempPath = Files.createTempDirectory("phaedra2-kdu");
			libPaths = copyLibs(tempPath.toString());
		} catch (IOException e) {
			throw new RuntimeException("Cannot load Kakadu: failed to copy native libraries", e);
		}
		
		// System load libraries from tmp location
		for (String libPath: libPaths) {
			System.load(libPath);
		}
		
		isLoaded = true;
	}
	
	private static void loadRequiredLibs() {
		for (String lib: REQUIRED_LIBS) {
			try {
				System.loadLibrary(lib);
			} catch (Throwable t) {
				throw new RuntimeException("Failed to load required Kakadu library, make sure they are available on the java.library.path: " + lib, t);
			}
		}
	}
	
	public static List<String> copyLibs(String destination) {
		String[] libsToCopy = null;
		if (SystemUtils.IS_OS_LINUX) {
			libsToCopy = LIBS_LINUX;
		} else if (SystemUtils.IS_OS_WINDOWS) {
			libsToCopy = LIBS_WINDOWS;
		} else {
			throw new RuntimeException("Cannot load Kakadu: unsupported OS: " + System.getProperty("os.name"));
		}
		
		// Copy resources to tmp location
		List<String> libPaths = new ArrayList<>();
		try {
			Path destinationPath = Paths.get(destination);
			for (String lib: libsToCopy) {
				String libName = Paths.get(lib).getFileName().toString();
				String libPath = destinationPath.resolve(libName).toString();
				InputStream input = KakaduLibLoader.class.getClassLoader().getResourceAsStream(lib);
				FileCopyUtils.copy(input, new FileOutputStream(libPath));
				libPaths.add(libPath);
			}
		} catch (IOException e) {
			throw new RuntimeException("Cannot load Kakadu: failed to copy native libraries", e);
		}
		
		return libPaths;
	}
}
