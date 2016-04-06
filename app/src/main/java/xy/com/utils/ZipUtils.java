package xy.com.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by xiaominfc on 5/24/15.
 */
public class ZipUtils {

    public static void unZip(String unZipfileName, String mDestPath) throws Exception {
        //try {
        unZipFile(new File(unZipfileName), mDestPath);
        /*} catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public static int unZipFile(File zipFile, String folderPath) throws IOException {
        //public static void upZipFile() throws Exception{

        File dir = new File(folderPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        ZipFile zfile = new ZipFile(zipFile);
        Enumeration zList = zfile.entries();
        ZipEntry ze = null;
        byte[] buf = new byte[1024];
        Object obj = null;

        while (zList.hasMoreElements()) {
            obj = zList.nextElement();
            if(obj == null)
                continue;
            ze = (ZipEntry) obj;
            if (ze.isDirectory()) {
                String dirstr = folderPath + ze.getName();
                dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
                File f = new File(dirstr);
                f.mkdir();
                continue;
            }

            try {
                File file = new File(folderPath + ze.getName());
                File parent = file.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }

                OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
                int readLen = 0;
                while ((readLen = is.read(buf, 0, 1024)) != -1) {
                    os.write(buf, 0, readLen);
                }
                is.close();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        zfile.close();
        return 0;
    }



}
