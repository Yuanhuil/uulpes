package heracles.web.util;

public final class HtmlStr {

    public static String blankIfEmpty(Object o) {
        if (o == null) {
            return "&nbsp;";
        }
        String str = o.toString().trim();
        return str.length() == 0 ? "&nbsp;" : str;
    }

    public static String htmlEncode(String s) {
        if (s == null)
            return "";
        StringBuilder str = new StringBuilder();
        for (int j = 0; j < s.length(); j++) {
            char c = s.charAt(j);
            switch (c) {
            case '"':
                str.append("&quot;");
                break;
            case '&':
                str.append("&amp;");
                break;
            case '<':
                str.append("&lt;");
                break;
            case '>':
                str.append("&gt;");
                break;
            case '\'':
                str.append("&#039;");
                break;
            case '\t':
            case ' ':
                str.append("&nbsp;");
                str.append(" ");
                break;
            default:
                str.append(c);
                break;
            }
        }
        return str.toString();
    }

    public static String htmlEncode(char c) {
        switch (c) {
        case '"':
            return "&quot;";
        case '&':
            return "&amp;";
        case '<':
            return "&lt;";
        case '>':
            return "&gt;";
        case '\'':
            return "&#039;";
        case '\t':
        case ' ':
            return "&nbsp;";
        default:
            return "" + c;
        }
    }

    /**
     * 还原字符串中特殊字符
     */
    public static String decodeString(String strData) {
        strData = replaceString(strData, "&lt;", "<");
        strData = replaceString(strData, "&gt;", ">");
        strData = replaceString(strData, "&#039;", "\'");
        strData = replaceString(strData, "&quot;", "\"");
        strData = replaceString(strData, "&amp;", "&");
        strData = replaceString(strData, "&nbsp;", " ");
        return strData;
    }

    public static String replaceString(String strData, String regex, String replacement) {
        if (strData == null) {
            return null;
        }
        int index;
        index = strData.indexOf(regex);
        String strNew = "";
        if (index >= 0) {
            while (index >= 0) {
                strNew += strData.substring(0, index) + replacement;
                strData = strData.substring(index + regex.length());
                index = strData.indexOf(regex);
            }
            strNew += strData;
            return strNew;
        }
        return strData;
    }
}
