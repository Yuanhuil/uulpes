package heracles.web.servlet;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import heracles.util.UtilCollection;

@SuppressWarnings("unchecked")
public class ServletRequestUtils {
    static public Map<String, Object> getRequestParams(HttpServletRequest req) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (req.getCharacterEncoding() == null && StringUtils.equalsIgnoreCase(req.getMethod(), "GET")) {
            String qryStr = req.getQueryString();
            Map<String, String> params = UtilCollection.toMap(qryStr, '&', '=');
            for (Map.Entry<String, String> ent : params.entrySet()) {
                try {
                    String value = URLDecoder.decode(ent.getValue(), CharEncoding.UTF_8);
                    result.put(ent.getKey(), value);
                } catch (UnsupportedEncodingException e) {
                }
            }
        } else {
            Map reqMap = req.getParameterMap();
            Iterator<Map.Entry<String, String[]>> iter = reqMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String[]> e = iter.next();
                String key = e.getKey();
                String[] values = e.getValue();
                if (values.length > 1) {
                    result.put(key, values);
                } else {
                    result.put(key, values[0]);
                }
            }
        }
        return result;
    }

    static public Map<String, Object> getSessionParams(HttpServletRequest req) {
        Map<String, Object> result = new HashMap<String, Object>();
        HttpSession session = req.getSession();
        if (session != null) {
            Enumeration enumer = session.getAttributeNames();
            while (enumer.hasMoreElements()) {
                String name = (String) enumer.nextElement();
                result.put(name, session.getAttribute(name));
            }
        }
        return result;

    }

    static public String getStringParameter(HttpServletRequest req, String parameter, String defaultValue) {
        String value = req.getParameter(parameter);
        return StringUtils.defaultIfEmpty(value, defaultValue);
    }

    public static int[] getIntParameters(ServletRequest request, String name) {
        String values[] = request.getParameterValues(name);
        if (values == null) {
            return new int[0];
        }
        int result[] = new int[values.length];
        for (int i = 0; i < values.length; ++i) {
            result[i] = NumberUtils.toInt(values[i]);
        }
        return result;
    }

    public static long getLongParameter(HttpServletRequest req, String parameter, long defaultValue) {
        return NumberUtils.toLong(req.getParameter(parameter), defaultValue);

    }

    public static int getIntParameter(HttpServletRequest req, String parameter, int defaultValue) {
        return NumberUtils.toInt(req.getParameter(parameter), defaultValue);

    }

    static public void putSession(HttpServletRequest req, Map<String, Object> map) {
        if (MapUtils.isEmpty(map)) {
            return;
        }
        HttpSession session = req.getSession(false);
        if (session != null) {
            for (Map.Entry<String, Object> ent : map.entrySet()) {
                session.setAttribute(ent.getKey(), ent.getValue());
            }
        }
    }

    static public void removeSession(HttpServletRequest req, List<String> removeKeys) {
        if (CollectionUtils.isEmpty(removeKeys)) {
            return;
        }
        HttpSession session = req.getSession();
        for (String name : removeKeys) {
            // session.removeAttribute(name);
            session.setAttribute(name, null);
        }
    }

    static public void setDownLoadXslHead(HttpServletResponse resp, String fileName) {
        String CONTENT_DISPOSOTION = "Content-Disposition";
        try {
            resp.reset();
            resp.setHeader(CONTENT_DISPOSOTION,
                    "attachment; filename=" + new String(fileName.getBytes("gb2312"), "iso-8859-1"));
            resp.setContentType("application/vnd.ms-excel");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    static public void setDownLoadDocHead(HttpServletResponse resp, String fileName) {
        String CONTENT_DISPOSOTION = "Content-Disposition";
        try {
            resp.reset();
            resp.setHeader(CONTENT_DISPOSOTION,
                    "attachment; filename=" + new String(fileName.getBytes("gb2312"), "iso-8859-1"));
            resp.setContentType("application/vnd.ms-word");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    static public void setDownLoadZipHead(HttpServletResponse resp, String fileName) {
        String CONTENT_DISPOSOTION = "Content-Disposition";
        try {
            resp.reset();
            resp.setHeader(CONTENT_DISPOSOTION,
                    "attachment; filename=" + new String(fileName.getBytes("gb2312"), "iso-8859-1"));
            resp.setContentType("application/zip");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
