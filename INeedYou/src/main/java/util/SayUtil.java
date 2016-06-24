package util;

import config.PrivateConfig;

import java.io.IOException;

/**
 * Created by huangming on 2016/6/22.
 */
public class SayUtil {

    public static void say(String msg) {
        try {
            Runtime.getRuntime().exec("cmd.exe /C wscript  " + PrivateConfig.NOWPATH + "/say.vbs " + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
