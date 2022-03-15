package com.marginallyClever.nodeGraphCore;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Creates and stores a single {@link ClassLoader} so that services create all instances in the same... class space?
 * If they have different {@link ClassLoader} then they cannot cast to each other.
 * @author Dan Royer
 * @since 2022-03-14
 */
public class ServiceLoaderHelper {
    private static ClassLoader myLoader=null;

    public ClassLoader getExtensionClassLoader() {
        if(myLoader==null) createClassLoader();
        return myLoader;
    }

    private void createClassLoader() {
        List<URL> list = new ArrayList<>();
        list.addAll(listFilesIn(FileHelper.getExtensionPath()));
        list.addAll(listFilesIn(System.getProperty("user.dir")));

        myLoader = new URLClassLoader(list.toArray(new URL[]{}),this.getClass().getClassLoader());
    }

    private List<URL> listFilesIn(String extensionPath) {
        String sep = FileSystems.getDefault().getSeparator();
        System.out.println("extensionPath = "+extensionPath);

        List<URL> list = new ArrayList<>();

        Set<String> listOfFiles = FileHelper.listFilesInDirectory(extensionPath);
        for(String fileName : listOfFiles) {
            String pathname = extensionPath + sep + fileName;
            System.out.println("extension = "+pathname);
            try {
                list.add((new File(pathname).toURI()).toURL());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return list;
    }
}