package cn.hm.gupiao.util;

import cn.hm.gupiao.config.PrivateConfig;

import java.io.IOException;

/**
 * Created by huangming on 2016/6/22.
 */
public class SayUtil {

    private static long lasttime = 0;

    public static void say(String msg) {
        try {
            System.out.println(msg);
            long c = lasttime - System.currentTimeMillis();
            if (c == 0 || (c < -5000)) {
                Runtime.getRuntime().exec("cmd.exe /C wscript  " + PrivateConfig.NOWPATH + "/say.vbs " + msg);
                lasttime = System.currentTimeMillis();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
