package repository;

import java.lang.reflect.Array;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时数据推送.（每秒一次）
 * 
 * @author huangming
 *
 * @param <T>
 */
public class TimerPushDataRepository<T> extends PushDataRepository<T> {

	private Class<T> clazz;

	public TimerPushDataRepository(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public void start() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// Get Queue Object
				T[] arr = (T[]) Array.newInstance(clazz, queue.size());
				arr = queue.toArray(arr);
				queue.clear();

				// Slow Push
				for (PushInterface<T> inf : list) {
					inf.push(arr);
				}
			}
		}, 0, 1000);

	}

}
