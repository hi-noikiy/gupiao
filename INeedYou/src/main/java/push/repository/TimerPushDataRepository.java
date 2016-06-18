package push.repository;

import java.lang.reflect.Array;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时数据推送.（每秒一次）
 *
 * @param <T>
 * @author huangming
 */
public class TimerPushDataRepository<T> extends PushDataRepository<T> {

    @Override
    public void start() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Object[] objects = queue.toArray();
                queue.clear();
                for (PushInterface<T> inf : list) {
                    for (Object obj : objects) {
                        inf.push((T) obj);
                    }
                }
            }
        }, 0, 1000);

    }

}
