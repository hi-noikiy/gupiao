package push;

import java.io.IOException;
import java.util.Collection;

import javax.websocket.Session;

import com.alibaba.fastjson.JSONObject;

import domain.TransactionRecord;
import push.repository.PushDataRepository;
import push.repository.PushInterface;
import push.repository.RealtimePushDataRepository;

public class PushRegisterCenter {

	private static PushRegisterCenter center = new PushRegisterCenter();

	/**
	 * 交易记录数据缓存仓库.
	 */
	private PushDataRepository<TransactionRecord> detailRepo = new RealtimePushDataRepository<TransactionRecord>(
			TransactionRecord.class);

	private PushRegisterCenter() {
	}

	/**
	 * 开启仓库缓存.
	 */
	public void start() {
		detailRepo.start();
	}

	public static PushRegisterCenter getInstance() {
		return center;
	}

	public void pushTR(Collection<TransactionRecord> collect) {
		detailRepo.push(collect);
	}

	public void pushTR(TransactionRecord tr) {
		detailRepo.push(tr);
	}

	/**
	 * 注册监听.
	 * 
	 * @param type
	 * @param session
	 */
	public void register(String type, Session session) {
		switch (type) {
		case "detail":
			detailRepo.register(new TRPushInterface(session));
			break;
		}
	}

	/**
	 * 解除注册.
	 * 
	 * @param session
	 */
	public void unregsiter(Session session) {
		detailRepo.unregister(new TRPushInterface(session));
	}

	/**
	 * 交易记录监听接口实现.
	 * 
	 * @author huangming
	 *
	 */
	private static class TRPushInterface implements PushInterface<TransactionRecord> {
		private Session session;

		public TRPushInterface(Session session) {
			this.session = session;
		}

		@Override
		public void push(TransactionRecord[] list) {
			for (TransactionRecord transactionRecord : list) {
				String jsonString = JSONObject.toJSONString(transactionRecord);
				try {
					if (session.isOpen()) {
						session.getBasicRemote().sendText(jsonString);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
