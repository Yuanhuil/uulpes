package heracles.web.freemarker;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.lang.Validate;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class FreemarkerCfg {
    private Configuration cfg = new Configuration();

    public FreemarkerCfg() {
        // cfg.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setOutputEncoding("UTF-8");
        // Higher value should be used in production environment.
        cfg.setTemplateUpdateDelay(0);
        // cfg.setTemplateUpdateDelay(60*60*120);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
    }

    public static FreemarkerCfg newInstance() {
        return new FreemarkerCfg();
    }

    public Template getTemplate(String name) throws IOException {
        return cfg.getTemplate(name);
    }

    public Configuration getCfg() {
        return cfg;
    }

    public void process(String templateName, Object rootMap, Writer out) throws IOException, TemplateException {
        Template t = getTemplate(templateName);
        t.process(rootMap, out);
    }

    public String process(Map<Object, Object> page) throws IOException, TemplateException {
        // File docFile = new File("e://demo.doc");
        // Writer docout = new BufferedWriter(new OutputStreamWriter(new
        // FileOutputStream(docFile)));
        Writer out = new StringWriter();
        String tmpl = page.get("template").toString();
        Validate.notEmpty(tmpl);
        Template t = getTemplate(tmpl);
        t.process(page, out);
        return out.toString();
        // return "";
    }

    public void setDirectoryForTemplateLoading(File file) throws IOException {
        cfg.setDirectoryForTemplateLoading(file);
    }

    public void setDirectoryForTemplateLoading(String filename) throws IOException {
        setDirectoryForTemplateLoading(new File(filename));
    }

    public void setServletContextForTemplateLoading(Object ctx, String path) {
        cfg.setServletContextForTemplateLoading(ctx, path);
    }
}
