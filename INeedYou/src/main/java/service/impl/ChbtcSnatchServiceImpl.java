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

import dao.impl.ClientRecordDaoImpl;
import domain.ClientRecord;
import service.SnatchService;

public class ChbtcSnatchServiceImpl implements SnatchService {
	private ClientRecordDaoImpl recordDao = new ClientRecordDaoImpl();

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
			int no = jsonT.getInt("no");
			if ("eth_cny_depth".equals(channel)) {
				JSONArray asksArr = jsonT.getJSONArray("asks");
				JSONArray bids = jsonT.getJSONArray("bids");
				Date now = new Date();
				int size= 8;
				ClientRecord record = new ClientRecord();
				List<ClientRecord> cacheList = new ArrayList<ClientRecord>(size);
				for (int i = 0; i < asksArr.length(); i++) {
					JSONArray item = asksArr.getJSONArray(i);
					double price = item.getDouble(0);
					double amount = item.getDouble(1);
					// System.out.println("卖\t单价：" + price + "\t 数量：" + amount);
					ClientRecord c = (ClientRecord)record.clone();
					c.setAmount(amount);
					c.setDirection("in");
					c.setGoodType("ytb");
					c.setOpTime(now);
					c.setPalType("btc");
					c.setPrice(price);
					cacheList.add(c);
					if(cacheList.size() >= size){
						recordDao.insertSuperTen(cacheList);
						cacheList.clear();
					}
				}
				
				/*
				for (int i = 0; i < bids.length(); i++) {
					JSONArray item = bids.getJSONArray(i);
					double price = item.getDouble(0);
					double amount = item.getDouble(1);
					System.out.println("买\t单价：" + price + "\t 数量：" + amount);
				}
				*/
			} else if ("eth_cny_lasttrades".equals(channel)) {
				/*
				JSONArray dataArr = jsonT.getJSONArray("data");
				for (int i = 0; i < dataArr.length(); i++) {
					JSONObject item = dataArr.getJSONObject(i);
					double amount = item.getDouble("amount");
					int date = item.getInt("date");
					double price = item.getDouble("price");
					String trade_type = item.getString("trade_type");
					String type = item.getString("type");
					System.out.println("数量：" + amount + "\t 价格：" + price + "\t 交易类型：" + trade_type + "\t 类型：" + type);
				}
				*/
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
