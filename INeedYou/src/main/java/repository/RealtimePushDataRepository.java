package repository;

import java.lang.reflect.Array;
import java.util.concurrent.TimeUnit;

/**
 * 实时数据推送.
 * 
 * @author huangming
 *
 * @param <T>
 */
public class RealtimePushDataRepository<T> extends PushDataRepository<T> {

	private Class<T> clazz;

	public RealtimePushDataRepository(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					// Get Queue Object
					try {
						queue.poll(3000, TimeUnit.MILLISECONDS);
					} catch (InterruptedException e) {
						e.printStackTrace();
						continue;
					}

					T[] arr = (T[]) Array.newInstance(clazz, queue.size());
					arr = queue.toArray(arr);
					queue.clear();

					// Slow Push
					for (PushInterface<T> inf : list) {
						inf.push(arr);
					}

				}
			}
		}).start();
	}

}
