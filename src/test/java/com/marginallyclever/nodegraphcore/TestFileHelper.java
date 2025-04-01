package com.marginallyclever.nodegraphcore;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class TestFileHelper {
    @Test
    public void testListingFiles() {
        String testPath = "./doesNotExist";
        File testDir = new File(testPath);
        assert(!testDir.exists());
        try {
            FileHelper.createDirectoryIfMissing(testPath);
            var list = FileHelper.listFilesInDirectory(testPath);
            assert(list.isEmpty());
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
        finally {
            if(testDir.exists()) {
                if(testDir.delete()) {
                    System.out.println("Deleted test directory: " + testPath);
                } else {
                    System.out.println("Failed to delete test directory: " + testPath);
                }
            }
        }
    }
}
