package listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import service.impl.ChbtcSnatchServiceImpl;

/**
 * Application Lifecycle Listener implementation class BuyRecordListener
 *
 */
@WebListener
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
