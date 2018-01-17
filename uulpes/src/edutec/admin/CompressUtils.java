package edutec.admin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import heracles.util.LogUtils;

public class CompressUtils {
    static private final Log logger = LogFactory.getLog(CompressUtils.class);

    public CompressUtils() {
    }

    /*
     * inputFileName 输入一个文件夹 zipFileName 输出一个压缩文件夹
     */
    static public void zip(String dir, String zipFileName) throws IOException {
        LogUtils.debug(logger, "zip " + zipFileName + "....");
        zip(zipFileName, new File(dir));
    }

    static private void zip(String dir, File inputFile) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dir));
        ZipOutputStream out = new ZipOutputStream(bos);
        zip(out, inputFile, "");
        LogUtils.debug(logger, "zip  " + dir + " done");
        out.close();
    }

    static private void zip(ZipOutputStream out, File f, String base) throws IOException {
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < fl.length; i++) {
                zip(out, fl[i], base + fl[i].getName());
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream in = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(in);
            byte[] buf = new byte[1024 * 2];
            int len;
            LogUtils.debug(logger, base);
            while ((len = bis.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            in.close();
        }
    }

    static public void upzip(String zipFileName, String toDir) throws IOException {
        LogUtils.debug(logger, "Extracting to: " + toDir);
        final int BUFFER = 2048;
        BufferedOutputStream dest = null;
        FileInputStream fis = new FileInputStream(zipFileName);
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            if (entry.getName().equals("/")) {
                continue;
            }
            LogUtils.debug(logger, "Extracting: " + entry);
            int count;
            byte data[] = new byte[BUFFER];
            FileOutputStream fos = new FileOutputStream(toDir + "/" + entry.getName());
            dest = new BufferedOutputStream(fos, BUFFER);
            while ((count = zis.read(data, 0, BUFFER)) != -1) {
                dest.write(data, 0, count);
            }
            dest.flush();
            dest.close();
        }
        zis.close();
    }

    /**
     * shibin add 接口是inputsteam的upzip
     * 
     * @param zipFileName
     * @param toDir
     * @throws IOException
     * @throws ArchiveException
     */
    static public void upzip(InputStream fis, String toDir) throws IOException, ArchiveException {
        LogUtils.debug(logger, "Extracting to: " + toDir);
        final int BUFFER = 2048;
        BufferedOutputStream dest = null;
        ZipArchiveInputStream zais = new ZipArchiveInputStream(fis, "GBK");
        ZipArchiveEntry entry;
        while ((entry = zais.getNextZipEntry()) != null) {
            if (entry.getName().equals("/")) {
                continue;
            }
            LogUtils.debug(logger, "Extracting: " + entry);
            int count;
            byte data[] = new byte[BUFFER];
            File dir = new File(toDir + "/" + entry.getName());
            if (entry.isDirectory()) {
                if (dir.exists()) {
                    dir.delete();
                }
                dir.mkdir();
            } else {
                FileOutputStream fos = new FileOutputStream(dir);
                ZipOutputStream out = new ZipOutputStream(fos);
                // //获取出该压缩实体的输入流
                dest = new BufferedOutputStream(fos, BUFFER);
                while ((count = zais.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, count);
                }
                dest.flush();
                dest.close();
            }
        }
    }

    public static void main(String[] temp) {
        try {
            FileInputStream fis = new FileInputStream("C:/Users/s/Desktop/scales/testscale.zip");
            CompressUtils.upzip(fis, "E:/");
            // CompressUtils.zip("E:/mywork/pes/WebRoot/mywork/data_tmp",
            // "E:/mywork/pes/WebRoot/mywork/data_mass/t.zip");
            // System.out.println(FilenameUtils.getBaseName("C:/Users/s/Desktop/scales/testscale.zip"));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}