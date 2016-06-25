package util;

import config.PrivateConfig;

import java.io.IOException;

/**
 * Created by huangming on 2016/6/22.
 */
public class SayUtil {

    private static long lasttime = 0;

    public static void say(String msg) {
        try {
            long c = lasttime - System.currentTimeMillis();
            if (c == 0 || (c < -3000)) {
                Runtime.getRuntime().exec("cmd.exe /C wscript  " + PrivateConfig.NOWPATH + "/say.vbs " + msg);
                lasttime = System.currentTimeMillis();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
