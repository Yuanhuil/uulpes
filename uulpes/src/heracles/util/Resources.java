package heracles.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Resources {
    public static final String DEFAULT_ENCODEING = "UTF-8";
    private static ClassLoader defaultClassLoader;
    private static Charset charset = Charset.forName(DEFAULT_ENCODEING);
    private static Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();

    private Resources() {
    }

    public static ClassLoader getDefaultClassLoader() {
        return defaultClassLoader;
    }

    public static void setDefaultClassLoader(ClassLoader defaultClassLoader) {
        Resources.defaultClassLoader = defaultClassLoader;
    }

    public static URL getResourceURL(String resource) throws IOException {
        return getResourceURL(getClassLoader(), resource);
    }

    public static URL getResourceURL(ClassLoader loader, String resource) throws IOException {
        URL url = null;
        if (loader != null)
            url = loader.getResource(resource);
        if (url == null)
            url = ClassLoader.getSystemResource(resource);
        if (url == null)
            throw new IOException("Could not find resource " + resource);
        return url;
    }

    public static InputStream getResourceAsStream(String resource) throws IOException {
        return getResourceAsStream(getClassLoader(), resource);
    }

    public static InputStream getResourceAsStream(ClassLoader loader, String resource) throws IOException {
        InputStream in = null;
        if (loader != null)
            in = loader.getResourceAsStream(resource);
        if (in == null)
            in = ClassLoader.getSystemResourceAsStream(resource);
        if (in == null)
            throw new IOException("Could not find resource " + resource);
        return in;
    }

    public static Properties getResourceAsProperties(String resource) throws IOException {
        Properties props = new Properties();
        InputStream in = null;
        String propfile = resource;
        in = getResourceAsStream(propfile);
        props.load(in);
        in.close();
        return props;
    }

    public static Properties getResourceAsProperties(ClassLoader loader, String resource) throws IOException {
        Properties props = new Properties();
        InputStream in = null;
        String propfile = resource;
        in = getResourceAsStream(loader, propfile);
        props.load(in);
        in.close();
        return props;
    }

    public static Reader getResourceAsReader(String resource) throws IOException {
        Reader reader;
        if (charset == null) {
            reader = new InputStreamReader(getResourceAsStream(resource));
        } else {
            reader = new InputStreamReader(getResourceAsStream(resource), charset);
        }

        return reader;
    }

    public static Reader getResourceAsReader(ClassLoader loader, String resource) throws IOException {
        Reader reader;
        if (charset == null) {
            reader = new InputStreamReader(getResourceAsStream(loader, resource));
        } else {
            reader = new InputStreamReader(getResourceAsStream(loader, resource), charset);
        }
        return reader;
    }

    public static File getResourceAsFile(String resource) throws IOException {
        return getResourceAsFile(getClassLoader(), resource);
    }

    public static File getResourceAsFile(ClassLoader loader, String resource) throws IOException {
        return new File(URLDecoder.decode(getResourceURL(loader, resource).getFile(), DEFAULT_ENCODEING));
    }

    public static InputStream getUrlAsStream(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        return conn.getInputStream();
    }

    public static Reader getUrlAsReader(String urlString) throws IOException {
        return new InputStreamReader(getUrlAsStream(urlString));
    }

    public static Properties getUrlAsProperties(String urlString) throws IOException {
        Properties props = new Properties();
        InputStream in = null;
        String propfile = urlString;
        in = getUrlAsStream(propfile);
        props.load(in);
        in.close();
        return props;
    }

    public static Class<?> classForName(String className) throws ClassNotFoundException {
        Class<?> clazz = classMap.get(className);
        if (clazz != null) {
            return clazz;
        }
        synchronized (classMap) {
            clazz = classMap.get(className);
            if (clazz != null) {
                return clazz;
            }
            try {
                clazz = getClassLoader().loadClass(className);
            } catch (Exception e) {
                // Ignore. Fail safe below.
            }
            if (clazz == null) {
                clazz = Class.forName(className);
            }
            classMap.put(className, clazz);
            return clazz;
        }
    }

    public static Object instantiate(String className)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        return instantiate(classForName(className));
    }

    public static Object instantiate(Class<?> clazz) throws InstantiationException, IllegalAccessException {
        return clazz.newInstance();
    }

    private static ClassLoader getClassLoader() {
        if (defaultClassLoader != null) {
            return defaultClassLoader;
        } else {
            ClassLoader cl = null;
            try {
                cl = Thread.currentThread().getContextClassLoader();
            } catch (Throwable ex) {
            }
            if (cl == null) {
                cl = Resources.class.getClassLoader();
            }
            return cl;
        }
    }

    public static Charset getCharset() {
        return charset;
    }

    public static void setCharset(Charset charset) {
        Resources.charset = charset;
    }

    public static String getRootPathOfClass(Class<?> clazz) throws URISyntaxException {
        File file = new File(clazz.getResource("").toURI());
        return file.getParent();
    }

    /**
     * 找到classes文件夹的全路径
     * 
     * @return
     */
    public static String getRootPathOfClasses() {
        String clazzPath = "";
        try {
            Enumeration<URL> uris = Thread.currentThread().getContextClassLoader().getResources("");
            for (; uris.hasMoreElements();) {
                String str = uris.nextElement().toString();
                if (str.indexOf("/classes/") != -1) {
                    str = URLDecoder.decode(str, DEFAULT_ENCODEING);
                    clazzPath = str;
                    clazzPath = clazzPath.substring("file:/".length());
                    return clazzPath;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 调用类没有参数的静态方法
     * 
     * @param methodOfClass
     * @return
     * @throws ClassNotFoundException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Object callStaticMethod(String methodOfClass) throws ClassNotFoundException, SecurityException,
            NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        int pos = methodOfClass.lastIndexOf(".");
        String className = methodOfClass.substring(0, pos);
        String methodName = methodOfClass.substring(pos + 1);
        Class<?> n = classForName(className);
        Method m = n.getMethod(methodName);
        Object o = m.invoke(null);
        return o;
    }

    /**
     * 调用类含有参数的静态方法
     * 
     * @param methodOfClass
     * @return
     */
    public static Object callStaticMethod(String methodOfClass, Object... args) throws Exception {
        Class<?>[] parameterTypes = new Class[args.length];
        for (int i = 0; i < args.length; ++i) {
            parameterTypes[i] = args[i].getClass();
        }
        int pos = methodOfClass.lastIndexOf(".");
        String className = methodOfClass.substring(0, pos);
        String methodName = methodOfClass.substring(pos + 1);
        Class<?> n = classForName(className);
        Method m = n.getMethod(methodName, parameterTypes);
        return m.invoke(null, args);
    }

    /**
     * 与自己目录下的资源文件
     * 
     * @param clazz
     * @param filePostfix
     * @return
     */
    public static String selfLocationResource(Class<?> clazz, String filePostfix) {
        String packageName = clazz.getPackage().getName();
        String reslocation = packageName.replace('.', '/') + "/" + clazz.getSimpleName() + filePostfix;
        return reslocation;
    }
}
