package edutec.scale.online;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import heracles.util.SimpleCodec;
import heracles.util.UtilCollection;
import heracles.web.servlet.ServletRequestUtils;

public class RequestParser {
    static public final String DEFAULT_ENC = "UTF-8";
    private HttpServletRequest req;
    private Map<String, String> paramValues;
    private String enc = DEFAULT_ENC;
    private boolean fromQuery;

    public RequestParser() {

    }

    public RequestParser(final HttpServletRequest req) {
        setReq(req);
    }

    public String getStringParameter(String parameter, String defaultValue) {
        if (fromQuery) {
            parserQueryStr();
            String result = paramValues.get(parameter);
            return StringUtils.isBlank(result) ? defaultValue : SimpleCodec.deutf8url(result);
        } else {
            return ServletRequestUtils.getStringParameter(req, parameter, defaultValue);
        }
    }

    public int getIntParameter(String parameter, int defaultValue) {
        String value = getStringParameter(parameter, null);
        if (value == null)
            return defaultValue;
        return NumberUtils.toInt(value, defaultValue);
    }

    public int getIntParameter(String parameter) {
        return getIntParameter(parameter, -1);
    }

    public byte getByteParameter(String parameter, int defaultValue) {
        int value = getIntParameter(parameter, defaultValue);
        return (byte) value;
    }

    public byte getByteParameter(String parameter) {
        int value = getIntParameter(parameter, -1);
        return (byte) value;
    }

    public HttpServletRequest getReq() {
        return req;
    }

    public int[] getIntParameters(String parameter) {
        return ServletRequestUtils.getIntParameters(req, parameter);
    }

    private void parserQueryStr() {
        if (paramValues == null && StringUtils.isNotBlank(req.getQueryString())) {
            String str = req.getQueryString();
            paramValues = UtilCollection.toMap(str, '&', '=');
        }
    }

    public void setReq(HttpServletRequest req) {
        this.req = req;
        paramValues = null;
    }

    public String getEnc() {
        return enc;
    }

    public void setEnc(String enc) {
        this.enc = enc;
    }

    public boolean isFromQuery() {
        return fromQuery;
    }

    public void setFromQuery(boolean fromQuery) {
        this.fromQuery = fromQuery;
    }
}
