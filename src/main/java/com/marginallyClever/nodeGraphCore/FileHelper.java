package com.marginallyClever.nodeGraphCore;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Convenient methods for searching files in a directory and finding the user's donatello/extensions path.
 */
public class FileHelper {
    public static void createDirectoryIfMissing(String dir) {
        File directory = new File(dir);
        if(!directory.exists()) directory.mkdirs();
    }

    /**
     * Returns The contents of a path.
     * @param dir the path to enumerate.
     * @return The contents of a path.
     */
    public static Set<String> listFilesInDirectory(String dir) {
        return Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }

    /**
     * Returns the extension path ~/Donatello/extensions/
     * @return the extension path ~/Donatello/extensions/
     */
    public static String getExtensionPath() {
        String sep = FileSystems.getDefault().getSeparator();
        return System.getProperty("user.home") + sep + "Donatello" + sep +"extensions";
    }
}
