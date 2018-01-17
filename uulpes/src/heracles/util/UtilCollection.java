package heracles.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang.text.StrSubstitutor;

public class UtilCollection {
    public static final char EQ = '=';
    public static final char COMMA = ',';
    public static final char COMMACHINA = '，';
    public static final char DUNHAO = '、';
    public static final char COLON = ':';
    public static final char SEMICOLON = ';';
    public static final char DEFAULT_SPARATOR = COMMA;
    public static final char A_SPACE_CHAR = ' ';
    public static final char RGN_CHAR = '-';
    public static final char RIGHT_BRACE_CHAR = '}';
    public static final char LEFT_BRACE_CHAR = '{';
    public static final String RIGHT_BRACE = "}";
    public static final String LEFT_BRACE = "{";
    public static final char AMP_CHAR = '&';

    /**
     * Convert a str that have format to map
     * 
     * @param str
     *            the string will be convert;
     * @param sep1
     *            e.g key1=1&key2=2&key2=3, sep1 that is "&";
     * @param sep2
     *            e.g key1=1&key2=2&key2=3, sep2 that is "=";
     * @return a map
     */
    public static Map<String, String> toMap(String str, String sep1, String sep2) {
        if (sep1.length() == 1 && sep2.length() == 1) {
            return toMap(str, sep1.charAt(0), sep2.charAt(0));
        }
        Map<String, String> result = new HashMap<String, String>();
        String[] strs = StringUtils.split(str, sep1);
        for (int i = 0; i < strs.length; ++i) {
            String[] pv = StringUtils.split(strs[i], sep2);
            if (pv.length == 2)
                result.put(pv[0], pv[1]);
        }
        return result;
    }

    /**
     * Convert a str that have format to map,如果分隔符是char的话
     * 
     * @param str
     *            the string will be convert;
     * @param sep1
     *            e.g key1=1&key2=2&key2=3, sep1 that is '&';
     * @param sep2
     *            e.g key1=1&key2=2&key2=3, sep2 that is '=';
     * @return a map map = {key3=3, key2=2, key1=1 }
     */
    public static Map<String, String> toMap(String str, char sep1, char sep2) {
        if (StringUtils.isEmpty(str)) {
            return new LinkedHashMap<String, String>();
        }
        Map<String, String> result = new LinkedHashMap<String, String>();
        toMap(str, sep1, sep2, result);
        return result;
    }

    public static void toMap(String str, char sep1, char sep2, Map<String, String> result) {
        result.clear();
        int state = 0;
        StrBuilder key = new StrBuilder();
        StrBuilder value = new StrBuilder();
        for (int idx = 0; idx < str.length(); ++idx) {
            char c = str.charAt(idx);
            switch (state) {
            case 0:
                if (c == sep2) {
                    state = 1;
                } else {
                    key.append(c);
                }
                break;
            case 1:
                if (c == sep1) {
                    result.put(key.toString().trim(), value.toString().trim());
                    key.clear();
                    value.clear();
                    state = 0;
                } else {
                    value.append(c);
                }
                break;
            }
        }
        if (key.length() > 0 && value.length() > 0) {
            result.put(key.toString().trim(), value.toString().trim());
            key.clear();
            value.clear();
        }
    }

    /**
     * 先以sep1分割，再以sep2分割
     * 
     * @param str
     * @param sep1
     * @param sep2
     * @return
     */
    public static List<List<String>> toStringList(String str, char sep1, char sep2) {
        List<String> list1 = toList(str, sep1);
        List<List<String>> result = new ArrayList<List<String>>(list1.size());
        for (String s : list1) {
            result.add(toList(s, sep2));
        }
        return result;
    }

    /**
     * 先以sep1分割，再以sep2分割
     * 
     * @param str
     * @param sep1
     * @param sep2
     * @return
     */
    public static void toStrings(String str, char sep1, String sep2, List<String[]> result, List<String> tmp) {
        result.clear();
        tmp.clear();
        toList(str, sep1, tmp);
        for (String s : tmp) {
            result.add(s.split(sep2));
        }
    }

    public static List<IntRange> toIntRangeList(String str, char sep1, char sep2) {
        Map<String, String> map = toMap(str, sep1, sep2);
        List<IntRange> result = new ArrayList<IntRange>(map.size());
        for (Map.Entry<String, String> ent : map.entrySet()) {
            int lo = NumberUtils.toInt(ent.getKey());
            int hi = NumberUtils.toInt(ent.getValue());
            IntRange range = new IntRange(lo, hi);
            result.add(range);
        }
        return result;
    }

    public static IntRange toIntRange(String str, char sep) {
        List<String> list = toList(str, sep);
        Validate.isTrue(list.size() == 2);
        int lo = NumberUtils.toInt(list.get(0));
        int hi = NumberUtils.toInt(list.get(1));
        IntRange range = new IntRange(lo, hi);
        return range;
    }

    public static DoubleRange toDoubleRange(String str, char sep) {
        List<String> list = toList(str, sep);
        Validate.isTrue(list.size() == 2);
        double lo = NumberUtils.toDouble(list.get(0));
        double hi = NumberUtils.toDouble(list.get(1));
        DoubleRange range = new DoubleRange(lo, hi);
        return range;
    }

    public static List<DoubleRange> toDoubleRanges(String str, char sep1, char sep2) {
        List<String> list = toList(str, sep1);
        List<DoubleRange> result = new ArrayList<DoubleRange>(list.size());
        for (String s : list) {
            DoubleRange range = toDoubleRange(s, sep2);
            result.add(range);
        }
        return result;
    }

    /**
     * Convert a str that have format to map
     * 
     * @param str
     *            the string will be convert;
     * @param sep1
     *            e.g key1=1&key2=2&key2=3, ok that is &;
     * @param sep2
     *            e.g key1=1&key2=2&key2=3, ok that is =;
     * @return a map
     */
    public static Map<String, Integer> toIntValueMap(String str, String sep1, String sep2, int defaultValue) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        String[] strs = StringUtils.split(str, sep1);
        for (int i = 0; i < strs.length; ++i) {
            String[] pv = StringUtils.split(strs[i], sep2);
            if (pv.length == 2)
                result.put(pv[0], NumberUtils.toInt(pv[1], defaultValue));
        }
        return result;
    }

    /**
     * Convert a str that have format to map
     * 
     * @param str
     *            the string will be convert;
     * @param sep1
     *            e.g key1=1&key2=2&key2=3, ok that is &;
     * @param sep2
     *            e.g key1=1&key2=2&key2=3, ok that is =;
     * @return a map
     */
    public static Map<Integer, Integer> toIntIntMap(String str, String sep1, String sep2, int defaultValue) {
        Map<Integer, Integer> result = new HashMap<Integer, Integer>();
        String[] strs = StringUtils.split(str, sep1);
        for (int i = 0; i < strs.length; ++i) {
            String[] pv = StringUtils.split(strs[i], sep2);
            if (pv.length == 2)
                result.put(NumberUtils.toInt(pv[0]), NumberUtils.toInt(pv[1], defaultValue));
        }
        return result;
    }

    /**
     * Convert a str that have format to map
     * 
     * @param str
     *            the string will be convert;
     * @param sep1
     *            e.g key1=1&key2=2&key2=3, ok that is &;
     * @param sep2
     *            e.g key1=1&key2=2&key2=3, ok that is =;
     * @return a map
     */
    public static Map<String, Integer> toIntValueMap(String str, String sep1, String sep2) {
        return toIntValueMap(str, sep1, sep2, 0);
    }

    public static int[] toIntArray(String[] strs) {
        int[] result = new int[strs.length];
        for (int i = 0; i < strs.length; ++i) {
            result[i] = NumberUtils.toInt(strs[i]);
        }
        return result;
    }

    public static int[] toIntArray(String str) {
        return UtilCollection.toIntArray(str, DEFAULT_SPARATOR);
    }

    public static int[] toIntArray(String str, char sep) {
        if (StringUtils.isEmpty(str)) {
            return new int[0];
        }
        String strs[] = toList(str, sep).toArray(new String[0]);
        return toIntArray(strs);
    }

    /**
     * Convert a str that have format to map
     * 
     * @param str
     *            the string will be convert;
     * @param sep1
     *            e.g key1=1&key2=2&key2=3, ok that is &;
     * @param sep2
     *            e.g key1=1&key2=2&key2=3, ok that is =;
     * @return a map
     */
    public static String[][] toStringTable(String str, String sep1, String sep2) {
        String[] strs = StringUtils.split(str, sep1);
        String[][] result = new String[strs.length][];
        for (int i = 0; i < strs.length; ++i) {
            result[i] = StringUtils.split(strs[i], sep2);
        }
        return result;
    }

    public static String[] splitTwo(String toSplit, String delimiter) {
        int offset = toSplit.indexOf(delimiter);
        if (offset < 0) {
            return null;
        }
        String beforeDelimiter = toSplit.substring(0, offset);
        String afterDelimiter = toSplit.substring(offset + delimiter.length());
        return new String[] { beforeDelimiter, afterDelimiter };
    }

    public static String substitutStr(String source, Map<?, ?> valueMap) {
        if (valueMap == null) {
            return source;
        }
        StrSubstitutor ss = new StrSubstitutor(valueMap);
        return ss.replace(source);
    }

    public static void forEach(int[] array, ArrayIntClosure close) {
        if (!ArrayUtils.isEmpty(array)) {
            close.setArrayLength(array.length);
            for (int i = 0; i < array.length; ++i) {
                close.setIndex(i);
                close.execute(array[i]);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static String toString(Map<?, ?> map, char sep1, char sep2) {
        if (MapUtils.isEmpty(map)) {
            return StringUtils.EMPTY;
        }
        StrBuilder sb = new StrBuilder(map.size() * 8 * 2);
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry ent = (Map.Entry) it.next();
            sb.append(ent.getKey()).append(sep2);
            sb.append(ent.getValue());
            if (it.hasNext()) {
                sb.append(sep1);
            }
        }
        return sb.toString();
    }

    public static String toString(Collection<String> coll, char sep) {
        if (CollectionUtils.isEmpty(coll)) {
            return StringUtils.EMPTY;
        }
        StrBuilder sb = new StrBuilder(coll.size() * 8);
        Iterator<String> it = coll.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext())
                sb.append(sep);
        }
        return sb.toString();
    }

    public static String toString(Collection<String> coll) {
        return toString(coll, DEFAULT_SPARATOR);
    }

    public static String replaceAny(String text, char[] replchars, char withchar) {
        StrBuilder sb = new StrBuilder(text.length());
        boolean found = false;
        for (int idx = 0; idx < text.length(); ++idx) {
            char c = text.charAt(idx);
            for (int j = 0; j < replchars.length; ++j) {
                if (c == replchars[j]) {
                    found = true;
                    break;
                }
            }
            if (found) {
                sb.append(withchar);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static List<String> toList(String str, char sep) {
        if (StringUtils.isEmpty(str)) {
            return Collections.emptyList();
        }
        List<String> result = new ArrayList<String>();
        toList(str, sep, result);
        return result;
    }

    public static void toList(String str, char sep, List<String> list) {
        StrBuilder sb = null;
        try {
            list.clear();
            sb = Pools.getInstance().borrowStrBuilder();
            for (int idx = 0; idx < str.length(); ++idx) {
                char c = str.charAt(idx);
                if (c == sep) {
                    list.add(sb.toString().trim());
                    sb.clear();
                } else {
                    sb.append(c);
                }
            }
            if (!sb.isEmpty()) {
                list.add(sb.toString().trim());
                sb.clear();
            }
        } finally {
            Pools.getInstance().returnStrBuilder(sb);
        }
    }

    public static String[] toArray(String str, char sep) {
        return toList(str, sep).toArray(new String[0]);
    }

    public static boolean startsWithAny(String str, String[] prefixes) {
        for (int i = 0; i < prefixes.length; ++i) {
            if (str.startsWith(prefixes[i])) {
                return true;
            }
        }
        return false;
    }

    public static boolean endsWithAny(String str, String[] suffixes) {
        for (int i = 0; i < suffixes.length; ++i) {
            if (str.endsWith(suffixes[i])) {
                return true;
            }
        }
        return false;
    }

    public static boolean endsWithIgnorecaseAny(String str, String[] suffixes) {
        str = str.toLowerCase();
        for (int i = 0; i < suffixes.length; ++i) {
            if (str.endsWith(suffixes[i].toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static Map<String, Object> toMap(Map.Entry<Object, Object> ent) {
        return UtilMisc.toMap(ent.getKey().toString(), ent.getValue());
    }

    /**
     * 解析如下串为Map String s =
     * "{k1=v1;k2=v2}{k3=v3;k4=v4;k5={k6=123};k7=v7;}{k8=v8}";
     * 
     * @param str
     * @param sep1
     * @param sep2
     * @return
     */
    public static Map<String, String> compositConvertMap(String str, char sep1, char sep2) {
        int len = str.length();
        Map<String, String> result = new LinkedHashMap<String, String>();
        StringBuilder sbKey = new StringBuilder(256);
        StringBuilder sbValue = new StringBuilder();
        int state = 0;
        for (int i = 0; i < len; ++i) {
            char c = str.charAt(i);
            switch (state) {
            case 0:
                if (c == UtilCollection.LEFT_BRACE_CHAR) {
                    state = 1;
                }
                break;
            case 1:
                if (c == sep2) {
                    state = 2;
                } else {
                    sbKey.append(c);
                }
                break;
            case 2:
                if (c == sep1 || c == UtilCollection.RIGHT_BRACE_CHAR) { // 出现一个key
                    // and value
                    if (c == sep1)
                        state = 3;
                    if (c == UtilCollection.RIGHT_BRACE_CHAR)
                        state = 0;
                    result.put(sbKey.toString().trim(), sbValue.toString().trim());
                    sbKey.setLength(0);
                    sbValue.setLength(0);
                } else if (c == UtilCollection.LEFT_BRACE_CHAR) {
                    state = 4;
                } else {
                    sbValue.append(c); // 填写value
                }
                break;
            case 3:
                if (c == UtilCollection.RIGHT_BRACE_CHAR) {
                    state = 0;
                } else if (!Character.isWhitespace(c)) {
                    state = 1;
                    sbKey.append(c);
                }
                break;
            case 4:
                if (c == UtilCollection.RIGHT_BRACE_CHAR) {
                    state = 2;
                } else {
                    sbValue.append(c);
                }
                break;
            }
        }
        if (result.isEmpty()) {
            return toMap(str, sep1, sep2);
        }
        return result;
    }

    /**
     * 把以 “{”和“}”间的字符串看成一行,且可能内嵌一个xx={}的字符串, 将多个这样的{}分割成一个字符串列表
     * 
     * @param str
     * @return
     */
    public static List<String> linesToList(String str) {
        List<String> result = new ArrayList<String>();
        StringBuilder sb = new StringBuilder(512);
        int len = str.length();
        int stat = 0;
        for (int i = 0; i < len; ++i) {
            char c = str.charAt(i);
            sb.append(c);
            switch (stat) {
            case 0:
                if (c == UtilCollection.LEFT_BRACE.charAt(0)) {
                    stat = 1;
                }
                break;
            case 1:
                if (c == UtilCollection.LEFT_BRACE.charAt(0)) {
                    stat = 2;
                    break;
                }
                if (c == UtilCollection.RIGHT_BRACE.charAt(0)) {
                    result.add(sb.toString());
                    sb.setLength(0);
                    stat = 0;
                }
                break;
            case 2:
                if (c == UtilCollection.RIGHT_BRACE.charAt(0)) {
                    stat = 1;
                }
                break;
            }
        }
        // 表示没有{}
        if (result.isEmpty()) {
            result.add(str);
        }
        return result;
    }

    /**
     * 将一行字符串，或被“{”和“}”所括起来的字符串，转换成Map
     * 
     * @param line
     * @return
     */
    public static Map<String, String> lineConvertMap(String line) {
        if (StringUtils.isEmpty(line))
            return Collections.emptyMap();
        line = line.trim();
        if (line.startsWith(UtilCollection.LEFT_BRACE) && line.endsWith(UtilCollection.RIGHT_BRACE)) {
            line = line.substring(1, line.length() - 1);
        }
        Map<String, String> ss = toMap(line, SEMICOLON, EQ);
        return ss;
    }

    /**
     * 将多个 “{”和“}”间的字符串，转换成map
     * 
     * @param str
     *            被“{”和“}”所括起来的字符串
     * @return
     */
    public static Map<String, String> linesConvertMap(String str) {
        Map<String, String> result = new HashMap<String, String>();
        List<String> lines = linesToList(str);
        for (String line : lines) {
            result.putAll(lineConvertMap(line));
        }
        return result;
    }

    public static String splitFlag(String str) {
        if (str.contains(String.valueOf(COMMA)))
            return String.valueOf(COMMA);
        else if (str.contains(String.valueOf(COMMACHINA)))
            return String.valueOf(COMMACHINA);
        else
            return String.valueOf(DUNHAO);
    }
}
