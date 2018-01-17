package edutec.scale.db;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import edutec.scale.util.ScaleConstants;
import heracles.web.config.ApplicationConfiguration;

public class ScaleEvaluateData {

    public static void main(String[] args) throws IOException {
        ScaleEvaluateData.$().copyHsqlDb();
    }

    private static ScaleEvaluateData singleTon;

    public static ScaleEvaluateData $() {
        if (singleTon == null) {
            singleTon = new ScaleEvaluateData();
        }
        return singleTon;
    }

    public void copyHsqlDb() throws IOException {
        ApplicationConfiguration cfg = ApplicationConfiguration.getInstance();
        String work = cfg.getWorkDir();
        String dbscale = cfg.getString(ScaleConstants.SCALE_DB_DIR);
        String dbbkscale = cfg.getString(ScaleConstants.SCALE_DBBK_DIR);
        String dir = FilenameUtils.concat(work, dbscale);
        String dir_bk = FilenameUtils.concat(work, dbbkscale);
        File destFile = new File(dir);
        FileUtils.cleanDirectory(destFile);
        File[] files = new File(dir_bk).listFiles();
        for (int i = 0; i < files.length; i++) {
            FileUtils.copyFileToDirectory(files[i], destFile, false);
        }
    }

    public void initialize() throws Exception {
        ScaleEvaluate.$();
        SentanceDb.$();
    }

    public void dispose() {
        try {
            ScaleEvaluate.$().shutdownAndClose();
            SentanceDb.$().shutdownAndClose();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
