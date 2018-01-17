package heracles.spring;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import heracles.util.UtilResources;

public class ResourceXmlApplicationContext extends AbstractXmlApplicationContext {

    private String[] configLocations;

    /**
     * Create a new ResourceXmlApplicationContext, loading the definitions from
     * the given XML file and automatically refreshing the context.
     * 
     * @param configLocation
     *            file path
     * @throws BeansException
     *             if context creation failed
     * @throws IOException
     */
    public ResourceXmlApplicationContext(String configLocation) throws BeansException, IOException {
        this(new String[] { configLocation });
    }

    /**
     * Create a new ResourceXmlApplicationContext, loading the definitions from
     * the given XML files and automatically refreshing the context.
     * 
     * @param configLocations
     *            array of file paths
     * @throws BeansException
     *             if context creation failed
     * @throws IOException
     */
    public ResourceXmlApplicationContext(String[] configLocations) throws BeansException, IOException {
        this(configLocations, null);
    }

    /**
     * Create a new ResourceXmlApplicationContext with the given parent, loading
     * the definitions from the given XML files and automatically refreshing the
     * context.
     * 
     * @param configLocations
     *            array of file paths
     * @param parent
     *            the parent context
     * @throws BeansException
     *             if context creation failed
     * @throws IOException
     */
    public ResourceXmlApplicationContext(String[] configLocations, ApplicationContext parent)
            throws BeansException, IOException {

        super(parent);
        this.configLocations = updateLocations(configLocations);
        refresh();
    }

    /**
     * Create a new ResourceXmlApplicationContext, loading the definitions from
     * the given XML files.
     * 
     * @param configLocations
     *            array of file paths
     * @param refresh
     *            whether to automatically refresh the context, loading all bean
     *            definitions and creating all singletons. Alternatively, call
     *            refresh manually after further configuring the context.
     * @throws BeansException
     *             if context creation failed
     * @see #refresh()
     */
    public ResourceXmlApplicationContext(String[] configLocations, boolean refresh) throws BeansException {
        this(configLocations, refresh, null);
    }

    /**
     * Create a new ResourceXmlApplicationContext with the given parent, loading
     * the definitions from the given XML files.
     * 
     * @param configLocations
     *            array of file paths
     * @param refresh
     *            whether to automatically refresh the context, loading all bean
     *            definitions and creating all singletons. Alternatively, call
     *            refresh manually after further configuring the context.
     * @param parent
     *            the parent context
     * @throws BeansException
     *             if context creation failed
     * @see #refresh()
     */
    public ResourceXmlApplicationContext(String[] configLocations, boolean refresh, ApplicationContext parent)
            throws BeansException {

        super(parent);
        this.configLocations = configLocations;
        if (refresh) {
            refresh();
        }
    }

    @Override
    protected String[] getConfigLocations() {
        return this.configLocations;
    }

    /**
     * Resolve resource paths as file system paths.
     * <p>
     * Note: Even if a given path starts with a slash, it will get interpreted
     * as relative to the current VM working directory. This is consistent with
     * the semantics in a Servlet container.
     * 
     * @param path
     *            path to the resource
     * @return Resource handle
     * @see org.springframework.web.context.support.XmlWebApplicationContext#getResourceByPath
     */
    protected Resource getResourceByPath(String path) {
        if (path != null && path.startsWith("/")) {
            path = path.substring(1);
        }
        return new FileSystemResource(path);
    }

    private String[] updateLocations(String[] configLocations) throws IOException {
        String[] result = new String[configLocations.length];
        int i = 0;
        for (String configLoc : configLocations) {
            result[i++] = UtilResources.getFile(configLoc).getAbsolutePath();
        }
        return result;
    }

}
