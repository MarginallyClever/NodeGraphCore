package com.marginallyclever.nodegraphcore;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Convenient methods for searching files in a directory and finding the user's donatello/extensions path.
 */
public class FileHelper {
    public static void createDirectoryIfMissing(String dir) {
        File directory = new File(dir);
        if(!directory.exists()) directory.mkdir();
    }

    /**
     * Returns The contents of a path.
     * @param dir the path to enumerate.
     * @return The contents of a path.
     */
    public static Set<String> listFilesInDirectory(String dir) {
        return Stream.of(Objects.requireNonNull(new File(dir).listFiles()))
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
        return getWorkPath() + sep +"extensions";
    }

    /**
     * Returns the path ~/Donatello/
     * @return the path ~/Donatello/
     */
    public static String getWorkPath() {
        String sep = FileSystems.getDefault().getSeparator();
        return System.getProperty("user.home") + sep + "Donatello";
    }

    /**
     * Returns the path ~/Donatello/Donatello.log
     * @return the path ~/Donatello/Donatello.log
     */
    public static String getLogFile() {
        String sep = FileSystems.getDefault().getSeparator();
        return getWorkPath() + sep + "Donatello.log";
    }

    /**
     * Convert from a filename to a file URL.
     */
    public static String convertToFileURL( String filename ) {
        // On JDK 1.2 and later, simplify this to:
        // "path = file.toURL().toString()".
        String path = new File( filename ).getAbsolutePath();
        if ( File.separatorChar != '/' ) {
            path = path.replace ( File.separatorChar, '/' );
        }
        if ( !path.startsWith ( "/" ) ) {
            path = "/" + path;
        }
        return "file:" + path;
    }
}
