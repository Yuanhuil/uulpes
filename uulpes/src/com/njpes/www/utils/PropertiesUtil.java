package com.njpes.www.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesUtil {
    private String properiesName = "";

    public PropertiesUtil() {
    }

    public PropertiesUtil(String fileName) {
        this.properiesName = fileName;
    }

    public String readProperty(String key) {
        String value = "";
        InputStream is = null;
        try {
            is = PropertiesUtil.class.getClassLoader().getResourceAsStream(this.properiesName);
            Properties p = new Properties();
            p.load(is);
            value = p.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                is.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public Properties getProperties() {
        Properties p = new Properties();
        InputStream is = null;
        try {
            is = PropertiesUtil.class.getClassLoader().getResourceAsStream(this.properiesName);
            p.load(is);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                is.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return p;
    }

    public void writeProperty(String key, String value) {
        InputStream is = null;
        OutputStream os = null;
        Properties p = new Properties();
        try {
            is = new FileInputStream(this.properiesName);
            p.load(is);
            os = new FileOutputStream(PropertiesUtil.class.getClassLoader().getResource(this.properiesName).getFile());

            p.setProperty(key, value);
            p.store(os, key);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (is != null)
                    is.close();
                if (os != null)
                    os.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (is != null)
                    is.close();
                if (os != null)
                    os.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        PropertiesUtil p = new PropertiesUtil("sysConfig.properties");
        p.writeProperty("namess", "wang");
        System.out.println(p.readProperty("namess"));
    }
}