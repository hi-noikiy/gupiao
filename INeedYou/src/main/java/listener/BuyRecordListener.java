package listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import config.PrivateConfig;
import service.impl.ChbtcSnatchServiceImpl;
import service.impl.OkCoinSnatchServiceImpl;

/**
 * Application Lifecycle Listener implementation class BuyRecordListener
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
        PrivateConfig.NOWPATH = sce.getServletContext().getRealPath("/WEB-INF/classes");
        // 完成WebSocket对交易记录的日志监听.
        new ChbtcSnatchServiceImpl().sync();
        // new OkCoinSnatchServiceImpl().sync();
    }

}
