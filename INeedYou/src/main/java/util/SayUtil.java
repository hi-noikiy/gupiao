package util;

import java.io.IOException;

/**
 * Created by huangming on 2016/6/22.
 */
public class SayUtil {

    public static void say(String msg) {
        try {
            Runtime.getRuntime().exec("cmd.exe /C wscript D:\\workspace\\codespace\\IdeaProject\\gupiao\\INeedYou\\src\\main\\resources\\say.vbs " + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
