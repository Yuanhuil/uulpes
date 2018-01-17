package heracles.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public final class SimpleCodec {
    /**
     * 将字符转换为utf-8编码
     * 
     * @param str
     * @return
     */
    static public String utf_8(String str) {
        return decode(str, "utf-8");
    }

    /**
     * 将字符转换为ISO-8859-1的编码
     * 
     * @param str
     * @return
     */
    static public String ISO_8859_1(String str) {
        try {
            return new String(str.getBytes("utf-8"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * 将字符编码
     * 
     * @param str
     * @param enc
     * @return
     */
    static public String decode(String str, String enc) {
        try {
            return new String(str.getBytes("ISO-8859-1"), enc);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * 以gb2312编码url
     * 
     * @param str
     * @return
     */
    static public String gb2312url(String str) {
        return encodeurl(str, "gb2312");
    }

    /**
     * 以utf-8编码url
     * 
     * @param str
     * @return
     */
    static public String utf8url(String str) {
        return encodeurl(str, "utf-8");
    }

    /**
     * 以utf-8解码url
     * 
     * @param str
     * @return
     */
    static public String deutf8url(String str) {
        return decodeurl(str, "utf-8");
    }

    /**
     * 按提供的解码规则编码
     * 
     * @param str
     * @param enc
     *            解码规则
     * @return
     */
    static public String encodeurl(String str, String enc) {
        try {
            return URLEncoder.encode(str, enc);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * 按提供的解码规则解码
     * 
     * @param str
     * @param enc
     *            解码规则
     * @return
     */
    static public String decodeurl(String str, String enc) {
        try {
            return URLDecoder.decode(str, enc);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * 将字符编码为16进制字符串
     * 
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    static public String enhex(String str) {
        Hex h = new Hex();
        byte[] bs = null;
        try {
            bs = h.encode(str.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new String(bs);
    }

    /**
     * 将16进制的字符串解码
     * 
     * @param hexstr
     * @return
     * @throws DecoderException
     */

    static public String dehex(String hexstr) {
        Hex h = new Hex();
        byte[] bs = null;
        try {
            bs = h.decode(hexstr.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (DecoderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            return new String(bs, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    // 转化十六进制编码为字符串
    public static String toStringHex(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "utf-8");// UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    public static String stringToUnicode(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            if (ch > 255)
                str += "\\u" + Integer.toHexString(ch);
            else if (ch >= 65 && ch <= 90)
                str += "\\u00" + Integer.toHexString(ch);
            else if (ch >= 97 && ch <= 122)
                str += "\\u00" + Integer.toHexString(ch);
            else
                str += "\\" + Integer.toHexString(ch);
        }
        return str;
    }

    public static void main(String[] agv) {
        String s = null;

        s = enhex("第1次");
        s = stringToUnicode("女");

        System.out.println(s);
        s = dehex("e7acac31e6aca1");
        System.out.println(s);
    }
}
