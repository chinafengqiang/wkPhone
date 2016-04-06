package xy.com.utils;

import java.io.File;

/**
 * Created by FQ.CHINA on 2015/9/14.
 */
public class FileUtils {
    public static String getExtName(String fileName)  {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos).toLowerCase();
    }

    public static String getFileNameNoExt(String fileName)  {
        int pos = fileName.lastIndexOf(".");
        String name = fileName.substring(0, pos);
        return name;
    }

    public static String getFileName(String urls) {
        if (urls != null) {
            String url = urls;
            int lastdotIndex = url.lastIndexOf("/");
            String fileName = url.substring(lastdotIndex + 1, url.length());
            return fileName;
        } else {
            return "";
        }
    }

    public static String getPhoneCourseFile(String filePath){
        String extName = getExtName(filePath);
        String fileNameNoExt = getFileNameNoExt(filePath);
        String resStr = fileNameNoExt+"_phone"+extName;
        return resStr;
    }

    public static void getFileDir(String filePath) {
		/* 设定目前所存路径 */
        File f = new File(filePath);
        if(!f.exists()){
            f.mkdirs();
        }
    }

    public static boolean delFile(String path){
        File file = new File(path);
        if (!file.exists()){
            return false;
        }
        return file.delete();
    }
}
