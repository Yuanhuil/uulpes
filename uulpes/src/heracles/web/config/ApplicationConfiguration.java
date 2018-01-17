package heracles.web.config;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StopWatch;
import org.springframework.web.context.ServletContextAware;

import heracles.util.Resources;
import heracles.util.SystemInfo;
import heracles.util.UtilCollection;

public class ApplicationConfiguration extends PropertiesConfiguration
        implements ServletContextAware, DisposableBean, InitializingBean {
    private static final String APPLICATION_PROPERTIES = "resource/application.properties";
    private static final String WORK_DIR_KEY = "application.work.dir";
    private static final String DEFAULT_WORK_DIR = "work";

    private final Log logger = LogFactory.getLog(getClass());

    private ServletContext servletContext;

    /**
     * The webapp root where the MY application is running in the servlet
     * container. This might differ from the application root.
     */
    String webappRoot;
    String workDir;
    private String contextPath;

    boolean isInit = false;

    private ApplicationConfiguration() {
    }

    static ApplicationConfiguration singleton = new ApplicationConfiguration();

    public static ApplicationConfiguration getInstance() {
        return singleton;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public static int getVersionAsInt() {
        String version = singleton.getString("appliction.version", "1.0.0");
        List<String> vx = UtilCollection.toList(version, '.');
        return NumberUtils.toInt(vx.get(0));
    }

    public static boolean isUsingCompany() {
        boolean company = singleton.getBoolean("using.company", false);
        return company;
    }

    /**
     * implements the ServletContextAware
     */
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * implements the InitializingBean
     */
    public void afterPropertiesSet() throws Exception {
        setup();
    }

    /**
     * implements the DisposableBean
     */
    public void destroy() throws Exception {

    }

    /**
     * 设置
     * 
     * @throws ConfigurationException
     */
    private void setup() throws ConfigurationException {
        try {
            if (!isInit) {
                StopWatch stopWatch = new StopWatch("ApplicationConfigurer");
                stopWatch.start("setup()");
                setupConfig();
                isInit = true;
                logInfos();
                stopWatch.stop();
                /* 计时 */
                logger.info(stopWatch.prettyPrint());
                logger.info("ApplicationConfigurer success end");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ConfigurationException(e.getMessage());
        }
    }

    /***************************************************************************
     * 此方法的主要目的是：1）确认核心配置文件存在 2）找到或创建工作目录
     * 
     * @throws ConfigurationException
     */
    private void setupConfig() throws ConfigurationException {
        if (servletContext != null) {
            webappRoot = servletContext.getRealPath("/");
            contextPath = servletContext.getContextPath();
            logger.info("WebappRoot: " + webappRoot + ", processing...");
            logger.info("ContextPath: " + contextPath + ", processing...");
        }

        /* first find in classpath */
        ClassPathResource resource = new ClassPathResource(APPLICATION_PROPERTIES);
        URL appCfgUrl = null;
        try {
            appCfgUrl = resource.getURL();
            logger.info("class path cfg url: " + appCfgUrl + ", processing...");
        } catch (Exception e) {
        }

        /* 如果class路径下没有找到核心配置文件，在webappRoot下找 */
        if (appCfgUrl == null && StringUtils.isNotEmpty(webappRoot)) {
            String path = null;
            String filename = null;
            List<String> dirs = Arrays
                    .asList(new String[] { "", "conf", "WEB-INF", "WEB-INF/conf", "META-INF", "META-INF/conf" });
            /* if there is no in class, find web root */
            for (String dir : dirs) {
                path = FilenameUtils.concat(webappRoot, dir);
                filename = FilenameUtils.concat(path, APPLICATION_PROPERTIES);
                File file = new File(filename);
                if (file.exists()) {
                    try {
                        appCfgUrl = file.toURI().toURL();
                    } catch (MalformedURLException e) {
                    }
                    break;
                }
            }
        }

        if (appCfgUrl == null) {
            throw new ConfigurationException("不能发现核心系统配置文件：" + APPLICATION_PROPERTIES);
        }

        /* 创建核心配置文件 */
        this.load(appCfgUrl);

        /* 开始创建工作目录 */
        logger.info("found [" + APPLICATION_PROPERTIES + "] on " + appCfgUrl + ", processing...");
        workDir = getString(WORK_DIR_KEY);
        if (StringUtils.isBlank(workDir)) {
            if (StringUtils.isNotBlank(webappRoot)) {
                workDir = FilenameUtils.concat(webappRoot, DEFAULT_WORK_DIR);
            } else {
                String classParentpath = Resources.getRootPathOfClasses();
                workDir = FilenameUtils.concat(classParentpath, DEFAULT_WORK_DIR);
            }
        }
        File workFile = new File(workDir);
        if (!workFile.exists()) {
            workFile.mkdirs();
            logger.info("create the dir:" + workDir + ", processing...");
        }
        logger.info("found " + workDir + ", processing...");
    }

    private void logInfos() throws MalformedURLException {
        if (servletContext != null) {
            logInfo("============ServletContext.Attributes===========");
            printInfoAttribues(servletContext.getAttributeNames());
            logInfo("============ServletContext.InitParameters=======");
            privateInfoInitParameters(servletContext.getInitParameterNames());
            logInfo("============ServletContext.otheres=======");
            logInfo("RealPath:" + servletContext.getRealPath("/"));
            logInfo("ServerInfo:" + servletContext.getServerInfo());
            logInfo("Servlet Version:" + servletContext.getMajorVersion() + "." + servletContext.getMinorVersion());
            logInfo("ServletContextName:" + servletContext.getServletContextName());
            logInfo("ContextPath:" + servletContext.getContextPath());
            logInfo("ResourcePaths:" + servletContext.getResourcePaths("/"));
            logInfo("Resource:" + servletContext.getResource("/"));

        }
        SystemInfo info = SystemInfo.getInstance(this);
        logInfo(info.toString());
    }

    private void printInfoAttribues(Enumeration<?> e) {
        while (e.hasMoreElements()) {
            Object o = e.nextElement();
            logger.info("{" + o + "-" + servletContext.getAttribute(o.toString()) + "}");
        }
    }

    private void privateInfoInitParameters(Enumeration<?> e) {
        while (e.hasMoreElements()) {
            Object o = e.nextElement();
            logger.info("{" + o + "-" + servletContext.getInitParameter(o.toString()) + "}");
        }
    }

    private void logInfo(String msg) {
        logger.info(msg);
    }

    public String getWebappRoot() {
        return webappRoot;
    }

    public String getWorkDir() {
        if (isInit) {
            return workDir;
        } else {
            try {
                setup();
            } catch (ConfigurationException e) {
                //
            }
            return workDir;
        }
    }

    public File makeWorkSubDir(String key) {
        String subDir = getString(key);
        String workDir = getWorkDir();
        String dir = FilenameUtils.concat(workDir, subDir);
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public File makeWorkSubDir1(String subDir) {
        String workDir = getWorkDir();
        String dir = FilenameUtils.concat(workDir, subDir);
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public String getContextPath() {
        return StringUtils.defaultIfEmpty(contextPath, StringUtils.EMPTY);
    }

}
