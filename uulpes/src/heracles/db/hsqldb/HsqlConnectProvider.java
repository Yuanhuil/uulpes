package heracles.db.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.hsqldb.DatabaseURL;

import heracles.util.Resources;

/*******************************************************************************
 * ����ֻ�ṩConnection�Ĵ���
 * 
 * @author Administrator
 */

public class HsqlConnectProvider {
    static final String URL_FILE = "jdbc:hsqldb:%s/xdb";
    static final String DRIVER = "org.hsqldb.jdbcDriver";
    static final String URL_MEM = "jdbc:hsqldb:mem:xdb";
    static final String FILE_URL_PREFIX = DatabaseURL.S_URL_PREFIX + DatabaseURL.S_FILE;

    public Connection getResouceConnection(String resource) throws Exception {
        String url = Resources.getResourceURL(resource).toString();
        url = StringUtils.replace(URL_FILE, "%s", url);

        return getConnection(url);
    }

    public Connection getFileConnection(String rootPath, String dbname) throws Exception {
        Class.forName(DRIVER).newInstance();
        String url = hsqlFileUrlStr(rootPath, dbname);
        Connection fileConn = DriverManager.getConnection(url, "sa", "");
        return fileConn;
    }

    public Connection getConnection(String url) throws Exception {
        Class.forName(DRIVER).newInstance();
        Connection fileConn = DriverManager.getConnection(url, "sa", "");
        return fileConn;
    }

    public Connection getMemConnection() throws Exception {
        Class.forName(DRIVER).newInstance();
        return DriverManager.getConnection(URL_MEM, "sa", "");
    }

    private String hsqlFileUrlStr(String rootPath, String dbname) {
        String urlstr = FilenameUtils.concat(rootPath, dbname);
        urlstr = urlstr.replace("\\", "/");
        urlstr = FILE_URL_PREFIX + urlstr;
        return urlstr;
    }

}
