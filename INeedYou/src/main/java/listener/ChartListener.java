package listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.websocket.Session;

import org.json.JSONObject;

import com.alibaba.fastjson.JSONArray;

import domain.TransactionRecord;
import service.TransactionService;
import service.impl.TransactionServiceImpl;

/**
 * Application Lifecycle Listener implementation class BuyRecordListener
 *
 */
@WebListener
public class ChartListener implements ServletContextListener {

	public static ChartThread chartThread = new ChartThread();

	/**
	 * Default constructor.
	 */
	public ChartListener() {
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent sce) {
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent sce) {
		Timer timer = new Timer(true);
		/**
		 * 定时推送数据.
		 */
		timer.schedule(chartThread, 0, 1000);
	}

	public static class ChartThread extends TimerTask {
		private TransactionService service = new TransactionServiceImpl();
		private Map<String, List<Session>> map = new HashMap<String, List<Session>>(5);

		public ChartThread() {

		}

		public void regsiter(String type, Session register) {
			List<Session> list = map.get(type);
			if (list == null) {
				list = new ArrayList<Session>(5);
				map.put(type, list);
			}
			list.add(register);
		}

		public void unregsiter(Session register) {
			Set<Entry<String, List<Session>>> entrySet = map.entrySet();
			for (Entry<String, List<Session>> entry : entrySet) {
				entry.getValue().remove(register);
			}
		}

		@Override
		public void run() {
			/**
			 * 获取当前时间，并查询当前时间的记录值，通过推送的方式推送出去.
			 */
			Date now = new Date();
			now.setTime(now.getTime() - 1000);

			List<TransactionRecord> detail = service.getDetail(now);
			String detailStr = JSONArray.toJSONString(detail);

			

			Double priceRecord = service.getAvgPriceRecord(now);
			String avgpriceStr = "{date:" + now.getTime() + ",value:" + priceRecord + "}";

			TransactionRecord maxPriceRecord = service.getMaxPriceRecord(now);
			String maxPriceStr = JSONObject.valueToString(maxPriceRecord);

			Double volume = service.getVolume(now);
			String volumeStr = "{date:" + now.getTime() + ",value:" + volume + "}";

			Double amountSum = service.getVolume(now);
			String amountSumStr = "{date:" + now.getTime() + ",value:" + amountSum + "}";

			String[] mess = new String[] { detailStr, avgpriceStr, maxPriceStr, volumeStr, amountSumStr };

			try {
				Set<Entry<String, List<Session>>> entrySet = map.entrySet();
				for (Entry<String, List<Session>> entry : entrySet) {
					String key = entry.getKey();
					List<Session> list = entry.getValue();
					for (Session r : list) {
						try {
							if (r != null && r.isOpen()) {
								switch (key) {
								case "detail":
									r.getBasicRemote().sendText(mess[0]);
									break;
								case "avgprice":
									r.getBasicRemote().sendText(mess[1]);
									break;
								case "maxprice":
									r.getBasicRemote().sendText(mess[2]);
									break;
								case "volume":
									r.getBasicRemote().sendText(mess[3]);
									break;
								case "amountsum":
									r.getBasicRemote().sendText(mess[4]);
									break;
								}
							}
							r.getBasicRemote().flushBatch();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
