package util;

import java.io.IOException;
import java.util.List;

import javax.websocket.Session;

public class WebSocketUtil {

	/**
	 * 推送所有WebSocket
	 * 
	 * @param list
	 * @param message
	 */
	public static void pushAll(List<Session> list, String message) {
		try {
			for (Session session : list) {
				if (session.isOpen())
					session.getBasicRemote().sendText(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
