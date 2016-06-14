package listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import push.PushRegisterCenter;

@WebListener
public class TransactionListener implements ServletContextListener {

	public TransactionListener() {
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		PushRegisterCenter.getInstance().start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

}
