package edutec.scale.util;

import java.io.File;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import edutec.scale.model.Global;
import heracles.spring.MainBeanFactory;
import heracles.web.config.ApplicationConfiguration;

public class ScaleBeanFactory extends MainBeanFactory {
    /*
     * public static void main(String[] args) { ScaleBeanFactory f = new
     * ScaleBeanFactory(); Global g = (Global)f.getBean("global"); //
     * System.out.println(g.getProps().get("guidance"));
     * //System.out.println(g.getCategory()); }
     */
    public static final String GUIDANCE = "guidance";
    public static final String QUESTYPE = "questiontype";
    private static Resource res;
    private static ScaleBeanFactory factory;
    static {
        String work = ApplicationConfiguration.getInstance().getWorkDir();
        String scaleFiles = ApplicationConfiguration.getInstance().getString(ScaleConstants.SCALE_XML_DIR);
        String xmlf = FilenameUtils.concat(work, scaleFiles + "/global.xml");
        res = new FileSystemResource(new File(xmlf));
        factory = new ScaleBeanFactory();
    }

    public static ScaleBeanFactory instance() {
        return factory;
    }

    private ScaleBeanFactory() {
        super(res);
    }

    public Global getGlobal() {
        Global g = (Global) getBean("global");
        return g;
    }

    public static String getGlobalProp(String key) {
        Global g = factory.getGlobal();
        return g.getProps().get(key);
    }

    public Map<String, String> getGlobalCategory() {
        Global g = (Global) getBean("global");
        return g.getCategory();
    }
}
