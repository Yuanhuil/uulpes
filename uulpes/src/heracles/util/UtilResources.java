package heracles.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import heracles.web.config.ApplicationConfiguration;

/**
 * 此类由ApplicationConfiguration配合使用, 1)ApplicationConfiguration要事先创建；
 * 能指定三类路径下的文件： a.指定的配置工作路径； b.类路径； c.web程序根路径 //默认为类路径
 * 
 * @author 王文
 */
public final class UtilResources {
    public static final String CLASSPATH_URL_PREFIX = "classpath:";
    public static final String WORKIDR_URL_PREFIX = "workdir:";
    public static final String WEBROOT_URL_PREFIX = "webroot:";

    public static File getFile(String resourceLocation) throws IOException {
        if (StringUtils.startsWithIgnoreCase(resourceLocation, CLASSPATH_URL_PREFIX)) {
            String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
            return Resources.getResourceAsFile(path);
        }
        ApplicationConfiguration cfg = ApplicationConfiguration.getInstance();
        // 指定的工作目录；
        if (StringUtils.startsWithIgnoreCase(resourceLocation, WORKIDR_URL_PREFIX)) {
            String path = resourceLocation.substring(WORKIDR_URL_PREFIX.length());
            path = FilenameUtils.concat(cfg.getWorkDir(), path);
            return new File(path);
            // web目录
        } else if (StringUtils.startsWithIgnoreCase(resourceLocation, WEBROOT_URL_PREFIX)) {
            String path = resourceLocation.substring(WEBROOT_URL_PREFIX.length());
            path = FilenameUtils.concat(cfg.getWebappRoot(), path);
            return new File(path);
        } else {
            // 默认为类路径
            return Resources.getResourceAsFile(resourceLocation);
        }
    }

    private UtilResources() {

    }
}
