package cn.hm.gupiao.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import cn.hm.gupiao.config.PrivateConfig;
import cn.hm.gupiao.snatch.ChbtcSnatchServiceImpl;
import cn.hm.gupiao.snatch.OkCoinSnatchServiceImpl;

/**
 * Application Lifecycle Listener implementation class PalListener
 */
@WebListener
public class PalListener implements ServletContextListener {

    /**
     * Default constructor.
     */
    public PalListener() {
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
        // new ChbtcSnatchServiceImpl().sync();
        new OkCoinSnatchServiceImpl(60000).sync();
    }

}
