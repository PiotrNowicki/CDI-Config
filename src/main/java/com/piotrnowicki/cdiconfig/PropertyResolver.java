package com.piotrnowicki.cdiconfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

/**
 * <p>
 * Reads all valid property files within the classpath and prepare them to be fetched.
 * </p>
 * 
 * <p>
 * This class <strong>can</strong> be accessed concurrently by multiple clients. The inner representation of
 * properties <strong>should not</strong> be leaked out; if this is absolutely required, use unmodifiable
 * collection.
 * </p>
 * 
 * <p>
 * This resolver <strong>doesn't pay attention</strong> to multiple properties defined with the same name in
 * different files. It's impossible to determine which one will take precedence, so the responsibility for
 * name-clash is a deployer concern.
 * </p>
 * 
 * @author Piotr Nowicki
 * 
 */
@Singleton
public class PropertyResolver {

    // TODO: Change it to some hierarchical structure if required.
    Map<String, Object> properties = new HashMap<>();

    /**
     * Initializes the properties by reading and uniforming them.
     * 
     * This method is called by the container only. It's not supposed to be invoked by the client directly.
     * 
     * @throws IOException
     *             in case of any property file access problem
     * @throws URISyntaxException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @PostConstruct
    private void init() throws IOException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        List<File> propertyFiles = getPropertyFiles(cl);

        for (File file : propertyFiles) {
            Properties p = new Properties();
            p.load(new FileInputStream(file));

            // TODO: If required - notify if added key was already present in the map
            properties.putAll(new HashMap<String, Object>((Map) p));
        }
    }

    /**
     * Gets flat-file properties files accessible from the root of the given classloader.
     * 
     * @param cl
     *            classpath to be used when scanning for files.
     * 
     * @return found property files.
     * 
     * @throws IOException
     *             if there was a problem while accessing resources using the <code>cl</code>.
     */
    List<File> getPropertyFiles(ClassLoader cl) throws IOException {
        List<File> result = new ArrayList<>();

        Enumeration<URL> resources = cl.getResources("");

        while (resources.hasMoreElements()) {
            File resource = getFileFromURL(resources.nextElement());

            File[] files = resource.listFiles(new PropertyFileFilter());
            result.addAll(Arrays.asList(files));
        }

        return result;
    }

    /**
     * Converts URL resource to a File. Makes sure that invalid URL characters (e.g. whitespaces) won't
     * prevent us from accessing the valid file location.
     * 
     * @param url
     *            URL to be transformed
     * 
     * @return File pointing to the given <code>url</code>.
     */
    File getFileFromURL(URL url) {
        File result;

        try {
            result = new File(url.toURI());
        } catch (URISyntaxException e) {
            result = new File(url.getPath());
        }

        return result;
    }

    /**
     * Returns property held under specified <code>key</code>. If the value is supposed to be of any other
     * type than {@link String}, it's up to the client to do appropriate casting.
     * 
     * @param key
     * @return value for specified <code>key</code> or null if not defined.
     */
    public String getValue(String key) {
        Object value = properties.get(key);

        return (value != null) ? String.valueOf(value) : null;
    }
}
