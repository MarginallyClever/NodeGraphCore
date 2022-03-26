package com.marginallyclever.nodegraphcore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Creates and stores a single {@link ClassLoader} so that services create all instances in the same... class space?
 * If they have different {@link ClassLoader} then they cannot cast to each other.
 * @author Dan Royer
 * @since 2022-03-14
 */
public class ServiceLoaderHelper {
    private static final Logger logger = LoggerFactory.getLogger(ServiceLoaderHelper.class);
    private static ClassLoader myLoader=null;
    private static List<URL> sourcesList = new ArrayList<>();

    public ServiceLoaderHelper() {
        super();
    }

    /**
     * Call this before the first call to #getExtensionClassLoader() to add extra paths to the classpath.
     * @param path the path to add
     */
    public static void addPath(String path) {
        logger.debug("extension path = {}",path);
        try {
            sourcesList.add((new File(path).toURI()).toURL());
        } catch (MalformedURLException e) {
            logger.warn("Could not add to classpath: {}",path);
        }
    }

    /**
     * Returns the static {@link ClassLoader} for all services.  Be sure to call addPath() before this.
     * @return the static {@link ClassLoader} for all services.
     */
    public ClassLoader getExtensionClassLoader() {
        if(myLoader==null) createClassLoader();
        return myLoader;
    }

    private void createClassLoader() {
        myLoader = new URLClassLoader(sourcesList.toArray(new URL[]{}),this.getClass().getClassLoader());
    }

    private static List<URL> listFilesIn(String extensionPath) {
        String sep = FileSystems.getDefault().getSeparator();
        logger.debug("list files in path = {}",extensionPath);

        List<URL> list = new ArrayList<>();

        List<String> listOfFiles = FileHelper.listFilesInDirectory(extensionPath);
        for(String pathname : listOfFiles) {
            logger.debug("found file = {}",pathname);
            try {
                list.add((new File(pathname).toURI()).toURL());
            } catch (MalformedURLException e) {
                logger.warn("Could not add file to list: {}",pathname);
            }
        }

        return list;
    }
}
