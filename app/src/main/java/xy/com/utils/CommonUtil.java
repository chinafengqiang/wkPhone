package xy.com.utils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by FQ.CHINA on 2016/6/14.
 */
public class CommonUtil {
    public static boolean isValidMobiNumber(String paramString) {
        String regex = "^1[2,3,4,5,6,7,8,9]\\d{9}$";
        if (paramString.matches(regex)) {
            return true;
        }
        return false;
    }

    public synchronized static String getRandomCode(int length) {
        String sRand = "";
        for (int i = 0; i < length; i++) {
            Random random = new Random();
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
        }
        return sRand;
    }

    public static String getDynamicPassword(String str) {
        Pattern continuousNumberPattern = Pattern.compile("[0-9\\.]+");
        Matcher m = continuousNumberPattern.matcher(str);
        String dynamicPassword = "";
        while (m.find()) {
            if (m.group().length() == 4) {
                System.out.print(m.group());
                dynamicPassword = m.group();
            }
        }
        return dynamicPassword;
    }
}
