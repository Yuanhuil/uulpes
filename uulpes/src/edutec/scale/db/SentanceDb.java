package edutec.scale.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.common.jdbc.ScriptRunner;

import edutec.scale.util.ScaleConstants;
import heracles.db.hsqldb.HsqlDb;
import heracles.web.config.ApplicationConfiguration;

public class SentanceDb extends HsqlDb {
    public static void main(String[] args) {
        try {
            /*
             * ArrayList list = SentanceDb.$().query("select * from SENTENCE
             * where scaleId=200011 AND userFlag=2 AND rank=2 AND category=2 AND
             * dimId=5 AND subDimId=0");
             * 
             * for (Object o : list) {
             * System.out.println(ArrayUtils.toString(o)); }//
             */
            // SentanceDb.$().shutdownAndCompact();
            // SentanceDb.$().shutdownAndCompact();
            // SentanceDb.$().importData();
            // SentanceDb db = SentanceDb.$();
            // SentanceDb.$().shutdownAndCompact();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static final Log logger = LogFactory.getLog(SentanceDb.class);

    private static SentanceDb singleTon;

    static {
        if (singleTon == null) {
            try {
                singleTon = new SentanceDb();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    public static SentanceDb $() {
        return singleTon;
    }

    private SentanceDb() throws Exception {
        super(scaledbDir(), "sentencedb");
        logger.info("open sentencedb ...");
    }

    protected void importData() throws Exception {
        Connection conn = getConnection();
        update("CREATE CACHED TABLE SENTENCE(ID INTEGER NOT NULL PRIMARY KEY,RANK INTEGER,SENTENCECONTENT VARCHAR(150),DIMID INTEGER,CATEGORY INTEGER,SCALEID INTEGER,SENTENCETAG CHAR(1),SENTENCEORDER INTEGER,SUBDIMID INTEGER,USERFLAG INTEGER)");
        update("CREATE INDEX KEY_1 ON SENTENCE(SCALEID,USERFLAG,RANK,CATEGORY,DIMID,SUBDIMID)");
        update("delete from SENTENCE");
        ScriptRunner run = new ScriptRunner(conn, false, true);
        String filename = FilenameUtils.concat(scaledbDir(), "t.sql");
        FileInputStream fs = new FileInputStream(filename);
        run.runScript(new InputStreamReader(fs, CharEncoding.UTF_8));
        // shutdownAndScript();
        shutdownAndCompact();
        // this.shutdownAndCompact();
    }

    private static String scaledbDir() {
        try {
            ApplicationConfiguration cfg = ApplicationConfiguration.getInstance();
            String work = cfg.getWorkDir();
            String dbscale = cfg.getString(ScaleConstants.SCALE_DB_DIR);
            String dir = FilenameUtils.concat(work, dbscale);
            File f = new File(dir);
            if (!f.exists()) {
                f.mkdirs();
                logger.info("created sentencedb directory:" + f);
            }
            logger.info("find sentencedb directory:" + f);
            return dir;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
