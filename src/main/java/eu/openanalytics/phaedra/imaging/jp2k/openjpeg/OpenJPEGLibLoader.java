package eu.openanalytics.phaedra.imaging.jp2k.openjpeg;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.util.FileCopyUtils;

public class OpenJPEGLibLoader {

	private static boolean isLoaded;
	private static List<String> libPaths = new ArrayList<>();
	
	private static final String[] LIBS_LINUX = {
			"native/linux/libopenjp2.so",
			"native/linux/libopenjp2.so.7",
			"native/linux/libopenjpegjni.so"
	};
	
	private static final String[] LIBS_WINDOWS = {
			"native/windows/openjp2.dll",
			"native/windows/openjpegjni.dll",
	};
	
	public static boolean isLoaded() {
		return isLoaded;
	}
	
	public static void load() {
		if (isLoaded) {
			return;
		}
		
		String[] libsToCopy = null;
		if (SystemUtils.IS_OS_LINUX) {
			libsToCopy = LIBS_LINUX;
		} else if (SystemUtils.IS_OS_WINDOWS) {
			libsToCopy = LIBS_WINDOWS;
		} else {
			throw new RuntimeException("Cannot load OpenJPEG: unsupported OS: " + System.getProperty("os.name"));
		}
		
		// Copy resources to tmp location
		try {
			Path tempPath = Files.createTempDirectory("phaedra2-openjpeg");
			for (String lib: libsToCopy) {
				String libName = Paths.get(lib).getFileName().toString();
				String libPath = tempPath.resolve(libName).toString();
				InputStream input = OpenJPEGLibLoader.class.getClassLoader().getResourceAsStream(lib);
				FileCopyUtils.copy(input, new FileOutputStream(libPath));
				libPaths.add(libPath);
			}
		} catch (IOException e) {
			throw new RuntimeException("Cannot load OpenJPEG: failed to copy native libraries", e);
		}
		
		// System load libraries from tmp location
		for (String libPath: libPaths) {
			System.load(libPath);
		}
		
		isLoaded = true;
	}

}
