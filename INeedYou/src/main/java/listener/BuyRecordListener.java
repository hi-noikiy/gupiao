package listener;

import java.io.IOException;
import java.net.URI;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
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

import service.impl.ChbtcSnatchServiceImpl;

/**
 * Application Lifecycle Listener implementation class BuyRecordListener
 *
 */
public class BuyRecordListener implements ServletContextListener {

	/**
	 * Default constructor.
	 */
	public BuyRecordListener() {
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
		// 完成WebSocket对交易记录的日志监听.
		ChbtcSnatchServiceImpl service = new ChbtcSnatchServiceImpl();
		service.sync();
	}

}
