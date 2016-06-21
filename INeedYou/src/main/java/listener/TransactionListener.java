package listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import analysis.impl.VolumeDataAnalysis;
import config.DictUtil;
import push.PushRegisterCenter;

@WebListener
public class TransactionListener implements ServletContextListener {

    public TransactionListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        PushRegisterCenter.getInstance().start();
        // new VolumeDataAnalysis(DictUtil.GOODSTYPE_BTB);
        new VolumeDataAnalysis(DictUtil.GOODSTYPE_YTB);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
