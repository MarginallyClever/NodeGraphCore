package com.marginallyclever.nodegraphcore;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Convenient methods for searching files in a directory and finding the user's donatello/extensions path.
 * @author Dan Royer
 * @since 2022-03-??
 */
public class FileHelper {
    /**
     * Attempts to create a directory if it does not exist.
     * @param dir the desired path
     * @throws IOException if the directory cannot be created
     */
    public static void createDirectoryIfMissing(String dir) throws IOException {
        File directory = new File(dir);
        if(!directory.exists()) {
            if(!directory.mkdirs()) throw new IOException("Unable to create directory: "+dir);
        }
    }

    public static @Nonnull List<String> listFilesInDirectory(String dir) {
        List<String> absoluteNames = new ArrayList<>();
        File[] found = new File(dir).listFiles();
        if(!Objects.isNull(found)) {
            for( File f : found ) {
                absoluteNames.add(f.getAbsolutePath());
            }
        }
        return absoluteNames;
    }
}
