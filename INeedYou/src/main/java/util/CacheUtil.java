package util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

public class CacheUtil {

	private static Map<Class, ArrayBlockingQueue> list = new HashMap<Class, ArrayBlockingQueue>();
	private static int max_cache = 50;

	public static <T> ArrayBlockingQueue<T> getQueue(Class<T> clazz) {
		ArrayBlockingQueue<T> queue = list.get(clazz);
		if (queue == null) {
			queue = new ArrayBlockingQueue<T>(max_cache);
			list.put(clazz, queue);
		}
		return queue;
	}

}
