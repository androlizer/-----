package org.chinamil.ui.library;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.javamex.arcmexer.ArchiveEntry;
import com.javamex.arcmexer.ArchiveReader;

import de.idyl.winzipaes.AesZipFileDecrypter;
import de.idyl.winzipaes.impl.AESDecrypterBC;
import de.idyl.winzipaes.impl.ExtZipEntry;

/**
 * 解密
 * 解压
 * 
 * @author zhang
 * 
 */
@SuppressWarnings("unused")
public class ZipDecrypter {
    /**
     * 获取html实体
     * 
     * @param path
     * @return
     */

    public static List<String> getList(File path) {

        List<String> url = new ArrayList<String>();
      
        FileOutputStream fos = null;
        ArchiveReader r;
        try {
            FileInputStream is = new FileInputStream(path);
            r = ArchiveReader.getReader(is);
            r.setDefaultPassword("helloJfjbv1");
            ArchiveEntry ss;
            while ((ss = r.nextEntry()) != null) {
                if (ss.getFilename().contains("htm")) {
                    url.add(ss.getFilename());
                }
            }
            is.close();
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

        /*
         * System.out.println(path.getAbsolutePath()+"缩略图路径");
         * try {
         * AesZipFileDecrypter decrypter = new AesZipFileDecrypter(path,
         * new AESDecrypterBC());
         * List<ExtZipEntry> url = decrypter.getEntryList();
         * if (url != null) {
         * ListIterator<ExtZipEntry> iterator = url.listIterator();
         * while (iterator.hasNext()) {
         * ExtZipEntry entry = iterator.next();
         * String nameString = entry.getName();
         * if (entry.isDirectory()) {
         * iterator.remove();
         * }
         * if (nameString.contains(".jpg") || nameString.contains(".db")) {
         * iterator.remove();
         * }
         * 
         * }
         * }
         * return url;
         * } catch (Exception e) {
         * e.printStackTrace();
         * }
         * ;
         * return null;
         */}

    /**
     * 获取缩略图实体
     * thumbs/01.jpg
     * @param path
     * @returnthumbs
     */
    public static List<String> getThumb(File path) {
        List<String> url = new ArrayList<String>();
        FileOutputStream fos = null;
        ArchiveReader r;
        try {
            FileInputStream is = new FileInputStream(path);
            r = ArchiveReader.getReader(is);
            r.setDefaultPassword("helloJfjbv1");
            ArchiveEntry ss;
            while ((ss = r.nextEntry()) != null) {
                if (ss.getFilename().contains("thumbs")) {
                    url.add(ss.getFilename());
                }
            }
            is.close();
            url.remove(0);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密 解压
     * 创建根目录
     * 
     * @param path
     * @param topcache
     */
    public static boolean decrypter(File path, File topcache) {
        AesZipFileDecrypter decrypter = null;
        String zipfile = path.getName();
        topcache = new File(topcache, zipfile.substring(0, zipfile.lastIndexOf(".")));
        if (topcache.exists()) {
            topcache.mkdirs();
        }
        try {
            decrypter = new AesZipFileDecrypter(path, new AESDecrypterBC());
            List<ExtZipEntry> entrylist = decrypter.getEntryList();
            String filename = null;
            ExtZipEntry entry = null;
            File rootFile = null;
            File subcatalog = null;
            for (int i = 0; i < entrylist.size(); i++) {
                entry = entrylist.get(i);
                filename = entry.getName();
                if (entry.isDirectory()) {
                    filename = filename.substring(0, filename.lastIndexOf("/"));
                    subcatalog = new File(topcache, filename);
                    subcatalog.mkdirs();
                } else {
                    decrypter.extractEntry(entry,
                            new File(subcatalog, filename.substring(filename.lastIndexOf("/") + 1)), "helloJfjbv1");
                }

            }
            return true;
        } catch (Exception e) {

            return false;
        }

    }

    /**
     * 另一种解压
     * 方式
     */

    public static boolean decryptercrypto(File target, File location) {
          int bufferSize = 1024 * 4;
        ByteArrayOutputStream bos = null;
        FileOutputStream fos = null;
        ArchiveReader r;
        byte[] buffer = new byte[bufferSize];
        try {
            FileInputStream is = new FileInputStream(target);
            String zipfile = target.getName();
            location = new File(location, zipfile.substring(0, zipfile.lastIndexOf(".")));
            r = ArchiveReader.getReader(is);
            //r.setDefaultPassword("123");
           r.setDefaultPassword("helloJfjbv1");
            ArchiveEntry ss;
            int len = 0;
             while ((ss = r.nextEntry()) != null) {
                InputStream inputStream = ss.getInputStream();
                if (ss.getFilename().contains(".")) {
                    FileOutputStream acf = new FileOutputStream(new File(location, ss.getFilename()));
                      while ((len = inputStream.read(buffer)) != -1) {
                        acf.write(buffer, 0, len);
                    }
                    acf.close();
                } else {

                    new File(location, ss.getFilename()).mkdirs();
                }

            }
            is.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

}
