package push;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.websocket.Session;

import com.alibaba.fastjson.JSONObject;

import domain.TransactionRecord;
import util.CacheUtil;
import util.WebSocketUtil;

public class TransactionRecordPush {

	private List<Session> list = new ArrayList<Session>(5);
	private static TransactionRecordPush obj = new TransactionRecordPush();

	private TransactionRecordPush() {
	}

	public void register(Session register) {
		list.add(register);
	}

	public void unregister(Session register) {
		list.remove(register);
	}

	public static TransactionRecordPush getInstance() {
		return obj;
	}

	public void start() {
		new Thread() {
			@Override
			public void run() {
				ArrayBlockingQueue<TransactionRecord> queue = CacheUtil.getQueue(TransactionRecord.class);
				while (true) {
					TransactionRecord detail;
					try {
						detail = queue.poll(1000, TimeUnit.SECONDS);
						String jsonString = JSONObject.toJSONString(detail);
						WebSocketUtil.pushAll(list, jsonString);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
}
