package com.marginallyClever.nodeGraphCore;

import org.assertj.core.util.introspection.FieldUtils;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestFileHelper {
    @Test
    public void testListingFiles() {
        String testPath = "./doesNotExist";
        assert(!(new File(testPath)).exists());
        assertThrows(NullPointerException.class,()->FileHelper.listFilesInDirectory(testPath));
        try {
            FileHelper.createDirectoryIfMissing(testPath);
            FileHelper.listFilesInDirectory(testPath);
        } finally {
            (new File(testPath)).delete();
        }

    }
}
