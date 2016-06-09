package api;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

import listener.ChartListener;

@ServerEndpoint(value = "/chart")
public class ChartSerlvet {

	@OnMessage
	public void onMessage(String message, Session session) {
		JSONObject mess = new JSONObject(message);
		String type = mess.getString("type");
		switch (type) {
		case "detail":
			ChartListener.chartThread.regsiter("detail", session);
			break;
		case "avgprice":
			ChartListener.chartThread.regsiter("avgprice", session);
			break;
		case "maxprice":
			ChartListener.chartThread.regsiter("maxprice", session);
			break;
		case "volume":
			ChartListener.chartThread.regsiter("volume", session);
			break;
		case "amountsum":
			ChartListener.chartThread.regsiter("amountsum", session);
			break;
		}
	}

	@OnClose
	public void onClose() {
		System.out.println("关闭websocket");
	}

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("开启websocket");
	}

	@OnError
	public void onError(Throwable e) {
		e.printStackTrace();
		System.out.println("错误websocket");
	}

}
