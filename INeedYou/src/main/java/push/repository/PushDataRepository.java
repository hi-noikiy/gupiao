package push.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 推送数据中转仓库。 实时推送和定时推送的前提条件。
 * 
 * 实时推送部分： 所有数据推送入队列，定时刷新过期数据。
 * 
 * 定时推送部分： 所有数据推送入队列，定时推送指定时间数据。
 * 
 * @author huangming
 *
 */
public abstract class PushDataRepository<T> {

	protected List<PushInterface<T>> list = new ArrayList<PushInterface<T>>(10);
	protected ArrayBlockingQueue<T> queue = new ArrayBlockingQueue<>(10);

	protected PushDataRepository() {
	}

	public void push(T ele) {
		try {
			queue.put(ele);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void register(PushInterface<T> pi) {
		list.add(pi);
	}

	public void unregister(PushInterface<T> pi) {
		list.remove(pi);
	}

	public void push(Collection<T> collect) {
		for (T e : collect) {
			try {
				queue.put(e);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				throw new RuntimeException(e1);
			}
		}
	}

	public abstract void start();

}
