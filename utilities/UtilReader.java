package ecs160.visitor.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * A simple utility file to read in the Java source
 * files.
 *
 */
public class UtilReader {
	public static String read(File file) throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
			StringBuilder content = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				if (content.length() > 0) content.append('\n');
				content.append(line);
			}
			return content.toString();
		}
	}
}
