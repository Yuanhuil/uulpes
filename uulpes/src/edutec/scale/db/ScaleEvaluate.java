package edutec.scale.db;

import java.io.File;
import java.util.Map;

import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.njpes.www.constant.Constants;

import edutec.scale.util.QuestionnaireConsts;
import edutec.scale.util.ScaleConstants;
import heracles.db.hsqldb.HsqlDb;
import heracles.util.UtilCollection;
import heracles.util.UtilMisc;
import heracles.web.config.ApplicationConfiguration;

public class ScaleEvaluate extends HsqlDb implements QuestionnaireConsts {
    private static final Log logger = LogFactory.getLog(ScaleEvaluate.class);

    private static ScaleEvaluate singleTon;

    static final String ABILITY_SCALE_SCORE_SQL = "select case when r < 0 then ROUND(0.5-p,5) else ROUND(p+0.5,5) end as val from "
            + "(select (${wrsore}-M)/SD as r, (case when abs((${wrsore}-M)/SD) > 3.99 then 3.99 else abs((${wrsore}-M)/SD) end) AS v "
            + "from ${table} where gradeOrderId=${gradeOrderId} AND gender=${gender} and wid='${wid}') a , zp b where ROUND(a.v,2)=b.z";

    public double getAbilityScore(String scaleId, String dimId, Object dimScore, Object gradeOrderId, Object gender) {
        Map<?, ?> params = UtilMisc.toMap(WR_SORE, dimScore, WID, dimId, TABLE, "data" + scaleId,
                Constants.GRADE_ORDER_ID_PROP, gradeOrderId, Constants.GENDER_PROP, gender);
        try {
            String sql = UtilCollection.substitutStr(ABILITY_SCALE_SCORE_SQL, params);
            // System.out.println(sql);
            return (Double) this.query(sql, new ScalarHandler());
        } catch (Exception e) {
            e.printStackTrace();
            return 0D;
        }
    }

    public static ScaleEvaluate $() {
        if (singleTon == null) {
            try {
                singleTon = new ScaleEvaluate();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return singleTon;
    }

    private ScaleEvaluate() throws Exception {
        super(scaledbDir(), "scaledb");
        logger.info("open scale db...");
    }

    public static void shutdownQuietly(ScaleEvaluate db) {
        if (db != null) {
            db.closeQuietly();
        }
    }

    private static String scaledbDir() {
        try {
            String work = ApplicationConfiguration.getInstance().getWorkDir();
            String dbscale = ApplicationConfiguration.getInstance().getString(ScaleConstants.SCALE_DB_DIR);
            String dir = FilenameUtils.concat(work, dbscale);
            File f = new File(dir);
            if (!f.exists()) {
                f.mkdirs();
                logger.info("created ScaleDB directory:" + f);
            }
            logger.info("find ScaleDB directory:" + f);
            return dir;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

}
