package service.impl;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.json.JSONArray;
import org.json.JSONObject;

import config.DictUtil;
import dao.ClientRecordDao;
import dao.TransactionRecordDao;
import dao.impl.ClientRecordDaoImpl;
import dao.impl.TransactionRecordDaoImpl;
import domain.ClientRecord;
import domain.TransactionRecord;
import service.SnatchService;

public class ChbtcSnatchServiceImpl implements SnatchService {
	private ClientRecordDao recordDao = new ClientRecordDaoImpl();
	private TransactionRecordDao transactionRecordDao = new TransactionRecordDaoImpl();

	@Override
	public void sync() {
		/**
		 * 实时同步Cnbtc网站的交易数据.
		 */
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		String uri = "wss://tcp.chbtc.com/websocket";
		try {
			Session session = container.connectToServer(new MyClient(), URI.create(uri));
			// 发送买卖委托单监听
			session.getBasicRemote().sendText("{'event':'addChannel','channel':'eth_cny_depth'}");
			// 发送成交记录
			session.getBasicRemote().sendText("{'event':'addChannel','channel':'eth_cny_lasttrades'}");
		} catch (DeploymentException e) {
			throw new Error(e);
		} catch (IOException e) {
			throw new Error(e);
		}
	}

	@ClientEndpoint
	public class MyClient {

		public MyClient() {
		}

		@OnOpen
		public void onOpen(Session session) {
			System.out.println("交易日志记录开启!");
		}

		@OnMessage
		public void onMessage(String message) {
			JSONObject jsonT = new JSONObject(message);
			String channel = jsonT.getString("channel");
			if ("eth_cny_depth".equals(channel)) {
				/** 获取买入委托记录. */
				JSONArray asksArr = jsonT.getJSONArray("asks");
				/** 获取卖出委托记录. */
				JSONArray bidsArr = jsonT.getJSONArray("bids");
				/** 获取当前时间. */
				Date now = new Date();

				ClientRecord record = new ClientRecord();
				List<ClientRecord> cacheList = new ArrayList<ClientRecord>(50);
				for (int i = 0; i < asksArr.length(); i++) {
					JSONArray item = asksArr.getJSONArray(i);
					double price = item.getDouble(0);
					double amount = item.getDouble(1);
					ClientRecord c = (ClientRecord) record.clone();
					c.setAmount(amount);
					c.setDirection(DictUtil.TRADEDIRECT_IN);
					c.setGoodType(DictUtil.GOODSTYPE_YTB);
					c.setOpTime(now);
					c.setPalType(DictUtil.PALTYPE_BTC);
					c.setPrice(price);
					cacheList.add(c);
				}
				for (int i = 0; i < bidsArr.length(); i++) {
					JSONArray item = bidsArr.getJSONArray(i);
					double price = item.getDouble(0);
					double amount = item.getDouble(1);
					ClientRecord c = (ClientRecord) record.clone();
					c.setAmount(amount);
					c.setDirection(DictUtil.TRADEDIRECT_OUT);
					c.setGoodType(DictUtil.GOODSTYPE_YTB);
					c.setOpTime(now);
					c.setPalType(DictUtil.PALTYPE_BTC);
					c.setPrice(price);
					cacheList.add(c);
				}
				recordDao.insertBatch(cacheList);
				cacheList.clear();
				cacheList = null;
			} else if ("eth_cny_lasttrades".equals(channel)) {
				JSONArray dataArr = jsonT.getJSONArray("data");
				List<TransactionRecord> cacheList = new ArrayList<TransactionRecord>(50);
				for (int i = 0; i < dataArr.length(); i++) {
					JSONObject item = dataArr.getJSONObject(i);
					double amount = item.getDouble("amount");
					double price = item.getDouble("price");
					String trade_type = item.getString("trade_type");
					String type = item.getString("type");
					Date now = new Date();

					TransactionRecord record = new TransactionRecord();
					record.setAmount(amount);
					record.setDirection(type);
					record.setGoodType(DictUtil.GOODSTYPE_YTB);
					record.setOpTime(now);
					record.setPalType(DictUtil.PALTYPE_BTC);
					record.setPrice(price);
					cacheList.add(record);
				}
				transactionRecordDao.insertBatch(cacheList);
				cacheList.clear();
				cacheList = null;
			} else {
				System.out.println(message);
			}
		}

		@OnError
		public void onError(Throwable t) {
			t.printStackTrace();
		}
	}

}
