package edutec.scale.descriptor;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.CharEncoding;

import edutec.scale.util.ScaleConstants;
import heracles.web.config.ApplicationConfiguration;

public class DescnFile {
    private String workDir;

    public DescnFile() {
        ApplicationConfiguration cfg = ApplicationConfiguration.getInstance();
        File file = cfg.makeWorkSubDir(ScaleConstants.SCALE_FILE_DIR);
        workDir = file.getAbsolutePath();
    }

    public String getString(String filename) throws IOException {
        filename = FilenameUtils.concat(workDir, filename);
        return FileUtils.readFileToString(new File(filename), CharEncoding.UTF_8);
    }

    public String getString(String filename, String defaultFn) throws IOException {
        filename = FilenameUtils.concat(workDir, filename);
        if (!new File(filename).exists()) {
            filename = FilenameUtils.concat(workDir, defaultFn);
        }
        return FileUtils.readFileToString(new File(filename), CharEncoding.UTF_8);
    }

}
