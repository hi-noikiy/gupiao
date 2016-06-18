package push.repository;

import java.lang.reflect.Array;
import java.util.concurrent.TimeUnit;

/**
 * 实时数据推送.
 *
 * @param <T>
 * @author huangming
 */
public class RealtimePushDataRepository<T> extends PushDataRepository<T> {

    private Thread t = null;

    @Override
    public void start() {
        t = new Thread(new PThread());
        t.start();
    }

    private class PThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                // Get Queue Object
                try {
                    T obj = queue.take();
                    // Slow Push
                    for (PushInterface<T> inf : list) {
                        inf.push(obj);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
