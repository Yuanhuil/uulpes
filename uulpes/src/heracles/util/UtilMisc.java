package heracles.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;

/**
 * UtilMisc - Misc Utility Functions
 */
@SuppressWarnings("unchecked")
public final class UtilMisc {
    private static final String UNICODE = "unicode";

    /**
     * Get an iterator from a collection, returning null if collection is null
     * 
     * @param col
     *            The collection to be turned in to an iterator
     * @return The resulting Iterator
     */

    public static Iterator toIterator(Collection col) {
        if (col == null)
            return null;
        else
            return col.iterator();
    }

    // added by zhaowanfeng
    public static HashMap<String, Object> objToHash(Object obj)
            throws IllegalArgumentException, IllegalAccessException {

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        Class clazz = obj.getClass();
        List<Class> clazzs = new ArrayList<Class>();

        do {
            clazzs.add(clazz);
            clazz = clazz.getSuperclass();
        } while (!clazz.equals(Object.class));

        for (Class iClazz : clazzs) {
            Field[] fields = iClazz.getDeclaredFields();
            for (Field field : fields) {
                Object objVal = null;
                field.setAccessible(true);
                objVal = field.get(obj);
                hashMap.put(field.getName(), objVal);
            }
        }

        return hashMap;
    }

    /**
     * Create a map from passed nameX, valueX parameters
     * 
     * @return The resulting Map
     */
    public static Map<String, Object> toMap(String name1, Object value1) {
        return new UtilMisc.SimpleMap(name1, value1);

        /*
         * Map fields = FastMap.newInstance(); fields.put(name1, value1); return
         * fields;
         */
    }

    /**
     * Create a map from passed nameX, valueX parameters
     * 
     * @return The resulting Map
     */
    public static Map<String, Object> toMap(String name1, Object value1, String name2, Object value2) {
        return new UtilMisc.SimpleMap(name1, value1, name2, value2);
    }

    /**
     * Create a map from passed nameX, valueX parameters
     * 
     * @return The resulting Map
     */
    public static Map<String, Object> toMap(String name1, Object value1, String name2, Object value2, String name3,
            Object value3) {
        return new UtilMisc.SimpleMap(name1, value1, name2, value2, name3, value3);

    }

    /**
     * Create a map from passed nameX, valueX parameters
     * 
     * @return The resulting Map
     */
    public static Map<String, Object> toMap(String name1, Object value1, String name2, Object value2, String name3,
            Object value3, String name4, Object value4) {
        return new UtilMisc.SimpleMap(name1, value1, name2, value2, name3, value3, name4, value4);
    }

    /**
     * Create a map from passed nameX, valueX parameters
     * 
     * @return The resulting Map
     */
    public static Map<String, Object> toMap(String name1, Object value1, String name2, Object value2, String name3,
            Object value3, String name4, Object value4, String name5, Object value5) {
        Map<String, Object> fields = new HashMap<String, Object>();

        fields.put(name1, value1);
        fields.put(name2, value2);
        fields.put(name3, value3);
        fields.put(name4, value4);
        fields.put(name5, value5);
        return fields;
    }

    /**
     * Create a map from passed nameX, valueX parameters
     * 
     * @return The resulting Map
     */
    public static Map<String, Object> toMap(String name1, Object value1, String name2, Object value2, String name3,
            Object value3, String name4, Object value4, String name5, Object value5, String name6, Object value6) {
        Map<String, Object> fields = new HashMap<String, Object>();

        fields.put(name1, value1);
        fields.put(name2, value2);
        fields.put(name3, value3);
        fields.put(name4, value4);
        fields.put(name5, value5);
        fields.put(name6, value6);
        return fields;
    }

    /**
     * Create a map from passed nameX, valueX parameters
     * 
     * @return The resulting Map
     */
    public static Map<Object, Object> toMap(String name1, Object value1, String name2, Object value2, String name3,
            Object value3, String name4, Object value4, String name5, Object value5, String name6, Object value6,
            String name7, Object value7) {
        Map<Object, Object> fields = new HashMap<Object, Object>();

        fields.put(name1, value1);
        fields.put(name2, value2);
        fields.put(name3, value3);
        fields.put(name4, value4);
        fields.put(name5, value5);
        fields.put(name6, value6);
        fields.put(name7, value7);
        return fields;
    }

    /**
     * Create a map from passed nameX, valueX parameters
     * 
     * @return The resulting Map
     */
    public static Map<Object, Object> toMap(Object[] data) {
        if (data == null) {
            return null;
        }
        if (data.length % 2 == 1) {
            throw new IllegalArgumentException("You must pass an even sized array to the toMap method");
        }
        Map<Object, Object> map = new HashMap<Object, Object>();
        for (int i = 0; i < data.length;) {
            map.put(data[i++], data[i++]);
        }
        return map;
    }

    public static String printMap(Map theMap) {
        StringBuffer theBuf = new StringBuffer();
        Iterator entryIter = theMap.entrySet().iterator();
        while (entryIter.hasNext()) {
            Map.Entry entry = (Map.Entry) entryIter.next();
            theBuf.append(entry.getKey());
            theBuf.append(" --> ");
            theBuf.append(entry.getValue());
            theBuf.append("\n");
        }
        return theBuf.toString();
    }

    public static Object removeFirst(List lst) {
        return lst.remove(0);
    }

    /**
     * Create a list from passed objX parameters
     * 
     * @return The resulting List
     */
    public static List<Object> toList(Object obj1) {
        List<Object> list = Collections.singletonList(obj1);
        return list;
    }

    /**
     * Create a list from passed objX parameters
     * 
     * @return The resulting List
     */
    public static List<Object> toList(Object obj1, Object obj2) {
        List<Object> list = new ArrayList<Object>(2);

        list.add(obj1);
        list.add(obj2);
        return list;
    }

    /**
     * Create a list from passed objX parameters
     * 
     * @return The resulting List
     */
    public static List<Object> toList(Object obj1, Object obj2, Object obj3) {
        List<Object> list = new ArrayList<Object>(3);

        list.add(obj1);
        list.add(obj2);
        list.add(obj3);
        return list;
    }

    /**
     * Create a list from passed objX parameters
     * 
     * @return The resulting List
     */
    public static List<Object> toList(Object obj1, Object obj2, Object obj3, Object obj4) {
        List<Object> list = new ArrayList<Object>(4);

        list.add(obj1);
        list.add(obj2);
        list.add(obj3);
        list.add(obj4);
        return list;
    }

    /**
     * Create a list from passed objX parameters
     * 
     * @return The resulting List
     */
    public static List<Object> toList(Object obj1, Object obj2, Object obj3, Object obj4, Object obj5) {
        List<Object> list = new ArrayList<Object>(5);

        list.add(obj1);
        list.add(obj2);
        list.add(obj3);
        list.add(obj4);
        list.add(obj5);
        return list;
    }

    /**
     * Create a list from passed objX parameters
     * 
     * @return The resulting List
     */
    public static List<Object> toList(Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
        List<Object> list = new ArrayList<Object>(6);

        list.add(obj1);
        list.add(obj2);
        list.add(obj3);
        list.add(obj4);
        list.add(obj5);
        list.add(obj6);
        return list;
    }

    public static List toList(Collection collection) {
        if (collection == null)
            return null;
        if (collection instanceof List) {
            return (List) collection;
        } else {
            return new ArrayList(collection);
        }
    }

    public static List<Object> toListArray(Object[] data) {
        if (data == null) {
            return null;
        }
        List<Object> list = new ArrayList<Object>(data.length);
        for (int i = 0; i < data.length; i++) {
            list.add(data[i]);
        }
        return list;
    }

    public static long toLong(Object value) {
        if (value != null) {
            if (value instanceof Long) {
                return ((Long) value).longValue();
            } else if (value instanceof String) {
                return Long.parseLong((String) value);
            } else if (value instanceof Integer) {
                return Long.parseLong((value.toString()));
            }
        }
        return 0;
    }

    /**
     * Returns a double from value, where value could either be a Double or a
     * String
     * 
     * @param value
     * @return
     */
    public static double toDouble(Object value) {
        if (value != null) {
            if (value instanceof Double) {
                return ((Double) value).doubleValue();
            } else if (value instanceof String) {
                return Double.parseDouble((String) value);
            }
        }
        return 0.0;
    }

    /**
     * Adds value to the key entry in theMap, or creates a new one if not
     * already there
     * 
     * @param theMap
     * @param key
     * @param value
     */
    public static void addToDoubleInMap(Map<Object, Double> theMap, Object key, Double value) {
        Double curValue = theMap.get(key);
        if (curValue != null) {
            theMap.put(key, new Double(curValue.doubleValue() + value.doubleValue()));
        } else {
            theMap.put(key, value);
        }
    }

    /**
     * Parse a locale string Locale object
     * 
     * @param localeString
     *            The locale string (en_US)
     * @return Locale The new Locale object or null if no valid locale can be
     *         interpreted
     */
    public static Locale parseLocale(String localeString) {
        if (localeString == null || localeString.length() == 0) {
            return null;
        }

        Locale locale = null;
        if (localeString.length() == 2) {
            // two letter language code
            locale = new Locale(localeString);
        } else if (localeString.length() == 5) {
            // positions 0-1 language, 3-4 are country
            String language = localeString.substring(0, 2);
            String country = localeString.substring(3, 5);
            locale = new Locale(language, country);
        } else if (localeString.length() > 6) {
            // positions 0-1 language, 3-4 are country, 6 and on are special
            // extensions
            String language = localeString.substring(0, 2);
            String country = localeString.substring(3, 5);
            String extension = localeString.substring(6);
            locale = new Locale(language, country, extension);
        } else {
        }

        return locale;
    }

    /**
     * The input can be a String, Locale, or even null and a valid Locale will
     * always be returned; if nothing else works, returns the default locale.
     * 
     * @param localeObject
     *            An Object representing the locale
     */
    public static Locale ensureLocale(Object localeObject) {
        if (localeObject != null && localeObject instanceof String) {
            localeObject = UtilMisc.parseLocale((String) localeObject);
        }
        if (localeObject != null && localeObject instanceof Locale) {
            return (Locale) localeObject;
        }
        return Locale.getDefault();
    }

    /**
     * This is meant to be very quick to create and use for small sized maps,
     * perfect for how we usually use UtilMisc.toMap
     */
    protected static class SimpleMap implements Map<String, Object>, java.io.Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -3477512558867607926L;

        protected Map<String, Object> realMapIfNeeded = null;

        String[] names;
        Object[] values;

        public SimpleMap() {
            names = new String[0];
            values = new Object[0];
        }

        public SimpleMap(String name1, Object value1) {
            names = new String[1];
            values = new Object[1];
            this.names[0] = name1;
            this.values[0] = value1;
        }

        public SimpleMap(String name1, Object value1, String name2, Object value2) {
            names = new String[2];
            values = new Object[2];
            this.names[0] = name1;
            this.values[0] = value1;
            this.names[1] = name2;
            this.values[1] = value2;
        }

        public SimpleMap(String name1, Object value1, String name2, Object value2, String name3, Object value3) {
            names = new String[3];
            values = new Object[3];
            this.names[0] = name1;
            this.values[0] = value1;
            this.names[1] = name2;
            this.values[1] = value2;
            this.names[2] = name3;
            this.values[2] = value3;
        }

        public SimpleMap(String name1, Object value1, String name2, Object value2, String name3, Object value3,
                String name4, Object value4) {
            names = new String[4];
            values = new Object[4];
            this.names[0] = name1;
            this.values[0] = value1;
            this.names[1] = name2;
            this.values[1] = value2;
            this.names[2] = name3;
            this.values[2] = value3;
            this.names[3] = name4;
            this.values[3] = value4;
        }

        protected void makeRealMap() {
            realMapIfNeeded = new HashMap<String, Object>();
            for (int i = 0; i < names.length; i++) {
                realMapIfNeeded.put(names[i], values[i]);
            }
            this.names = null;
            this.values = null;
        }

        public void clear() {
            if (realMapIfNeeded != null) {
                realMapIfNeeded.clear();
            } else {
                realMapIfNeeded = new HashMap<String, Object>();
                names = null;
                values = null;
            }
        }

        public boolean containsKey(Object obj) {
            if (realMapIfNeeded != null) {
                return realMapIfNeeded.containsKey(obj);
            } else {
                for (int i = 0; i < names.length; i++) {
                    if (obj == null && names[i] == null)
                        return true;
                    if (names[i] != null && names[i].equals(obj))
                        return true;
                }
                return false;
            }
        }

        public boolean containsValue(Object obj) {
            if (realMapIfNeeded != null) {
                return realMapIfNeeded.containsValue(obj);
            } else {
                for (int i = 0; i < names.length; i++) {
                    if (obj == null && values[i] == null)
                        return true;
                    if (values[i] != null && values[i].equals(obj))
                        return true;
                }
                return false;
            }
        }

        public java.util.Set entrySet() {
            if (realMapIfNeeded != null) {
                return realMapIfNeeded.entrySet();
            } else {
                this.makeRealMap();
                return realMapIfNeeded.entrySet();
            }
        }

        public Object get(Object obj) {
            if (realMapIfNeeded != null) {
                return realMapIfNeeded.get(obj);
            } else {
                for (int i = 0; i < names.length; i++) {
                    if (obj == null && names[i] == null)
                        return values[i];
                    if (names[i] != null && names[i].equals(obj))
                        return values[i];
                }
                return null;
            }
        }

        public boolean isEmpty() {
            if (realMapIfNeeded != null) {
                return realMapIfNeeded.isEmpty();
            } else {
                if (this.names.length == 0)
                    return true;
                return false;
            }
        }

        public java.util.Set<String> keySet() {
            if (realMapIfNeeded != null) {
                return realMapIfNeeded.keySet();
            } else {
                this.makeRealMap();
                return realMapIfNeeded.keySet();
            }
        }

        public Object put(String obj, Object obj1) {
            if (realMapIfNeeded != null) {
                return realMapIfNeeded.put(obj, obj1);
            } else {
                this.makeRealMap();
                return realMapIfNeeded.put(obj, obj1);
            }
        }

        public void putAll(java.util.Map map) {
            if (realMapIfNeeded != null) {
                realMapIfNeeded.putAll(map);
            } else {
                this.makeRealMap();
                realMapIfNeeded.putAll(map);
            }
        }

        public Object remove(Object obj) {
            if (realMapIfNeeded != null) {
                return realMapIfNeeded.remove(obj);
            } else {
                this.makeRealMap();
                return realMapIfNeeded.remove(obj);
            }
        }

        public int size() {
            if (realMapIfNeeded != null) {
                return realMapIfNeeded.size();
            } else {
                return this.names.length;
            }
        }

        public java.util.Collection<Object> values() {
            if (realMapIfNeeded != null) {
                return realMapIfNeeded.values();
            } else {
                this.makeRealMap();
                return realMapIfNeeded.values();
            }
        }

        public String toString() {
            if (realMapIfNeeded != null) {
                return realMapIfNeeded.toString();
            } else {
                StringBuffer outString = new StringBuffer("{");
                for (int i = 0; i < names.length; i++) {
                    if (i > 0)
                        outString.append(',');
                    outString.append('{');
                    outString.append(names[i]);
                    outString.append(',');
                    outString.append(values[i]);
                    outString.append('}');
                }
                outString.append('}');
                return outString.toString();
            }
        }

        public int hashCode() {
            if (realMapIfNeeded != null) {
                return realMapIfNeeded.hashCode();
            } else {
                int hashCode = 0;
                for (int i = 0; i < names.length; i++) {
                    // note that this calculation is done based on the calc
                    // specified in the Java java.util.Map interface
                    int tempNum = (names[i] == null ? 0 : names[i].hashCode())
                            ^ (values[i] == null ? 0 : values[i].hashCode());
                    hashCode += tempNum;
                }
                return hashCode;
            }
        }

        public boolean equals(Object obj) {
            if (realMapIfNeeded != null) {
                return realMapIfNeeded.equals(obj);
            } else {
                Map mapObj = (Map) obj;

                // first check the size
                if (mapObj.size() != names.length)
                    return false;

                // okay, same size, now check each entry
                for (int i = 0; i < names.length; i++) {
                    // first check the name
                    if (!mapObj.containsKey(names[i]))
                        return false;

                    // if that passes, check the value
                    Object mapValue = mapObj.get(names[i]);
                    if (mapValue == null) {
                        if (values[i] != null)
                            return false;
                    } else {
                        if (!mapValue.equals(values[i]))
                            return false;
                    }
                }

                return true;
            }
        }
    }

    // Begin Neogia specific : FR1356553 : This method tests if a integer is
    // peer or not and returns true if
    /**
     * return true if numberTotest % 2 == 0
     */
    public static boolean isPeer(int numberToTest) {
        if (numberToTest % 2 == 0)
            return true;
        return false;
    }

    public static String substrIfStartsWith(String str, String prefix) {
        if (str.startsWith(prefix)) {
            return str.substring(prefix.length());
        }
        return StringUtils.EMPTY;
    }

    public static String quote(String str) {
        return (str != null ? "\"" + str + "\"" : null);
    }

    public static String quote(int val) {
        return quote(String.valueOf(val));
    }

    public static String quote(Object val) {
        return quote(String.valueOf(val));
    }

    public static String extractDigit(String str) {
        StrBuilder sb = new StrBuilder(str.length());
        for (int i = 0; i < str.length(); ++i) {
            if (Character.isDigit(str.charAt(i))) {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }

    /**
     * 除去数字字符串的后缀0和.
     * 
     * @param str
     * @return
     */
    public static String ereaseZeros(String str) {
        str = str.trim();
        int len = str.length();
        int count = str.length();
        while (str.charAt(len - 1) == '0') {
            len--;
        }
        while (str.charAt(len - 1) == '.') {
            len--;
        }
        return len < count ? str.substring(0, len) : str;
    }

    /**
     * 将全角转换成半角 男，女
     * 
     * @return
     */
    public static String toDBCase(String str) {
        StringBuilder outStr = new StringBuilder(str.length());
        String temp = "";
        byte[] bytes = null;
        boolean isSbc = false;
        for (int i = 0; i < str.length(); i++) {
            try {
                temp = str.substring(i, i + 1);
                bytes = temp.getBytes(UNICODE);
            } catch (java.io.UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            isSbc = false;
            // utf8
            if (bytes[2] == -1) {
                bytes[3] = (byte) (bytes[3] + 32);
                bytes[2] = 0;
                isSbc = true;
            } else if (bytes[3] == -1) { // gb2312
                bytes[2] = (byte) (bytes[2] + 32);
                bytes[3] = 0;
                isSbc = true;
            }
            if (isSbc) {
                try {
                    outStr.append(new String(bytes, UNICODE));
                } catch (java.io.UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                outStr.append(temp);
            }
        }
        return outStr.toString();
    }

    public static boolean isEmpty(Object o) {
        if (o == null)
            return true;
        return StringUtils.isBlank(o.toString());
    }

    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    public static boolean startsChar(String str, char ch) {
        int len = str.length();
        int st = 0;
        char c = 0;
        while (st < len) {
            c = str.charAt(st);
            if (!Character.isWhitespace(c)) {
                break;
            }
            st++;
        }
        return c == ch;
    }
}
