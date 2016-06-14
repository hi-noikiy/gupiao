package api;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

import push.PushRegisterCenter;

@ServerEndpoint(value = "/chart")
public class ChartSerlvet {

	private Session session;

	@OnMessage
	public void onMessage(String message, Session session) {
		JSONObject mess = new JSONObject(message);
		String type = mess.getString("type");
		switch (type) {
		case "detail":
			PushRegisterCenter.getInstance().register("detail", session);
			break;
		}
	}

	@OnClose
	public void onClose() {
		PushRegisterCenter.getInstance().unregsiter(session);
		System.out.println("关闭websocket");
	}

	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		System.out.println("开启websocket");
	}

	@OnError
	public void onError(Throwable e) {
		e.printStackTrace();
		PushRegisterCenter.getInstance().unregsiter(session);
		System.out.println("错误websocket");
	}

}
