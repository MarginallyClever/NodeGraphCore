package com.marginallyclever.nodegraphcore;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Convenient methods for searching files in a directory and finding the user's donatello/extensions path.
 * @author Dan Royer
 * @since 2022-03-??
 */
public class FileHelper {
    /**
     * Attempts to create a directory if it does not exist.
     * @param dir the desired path
     */
    public static void createDirectoryIfMissing(String dir) {
        File directory = new File(dir);
        if(!directory.exists()) directory.mkdirs();
    }

    public static List<String> listFilesInDirectory(String dir) {
        File[] found = new File(dir).listFiles();
        List<String> absoluteNames = new ArrayList<>();
        for( File f : found ) {
            absoluteNames.add(f.getAbsolutePath());
        }
        return absoluteNames;
    }
}
