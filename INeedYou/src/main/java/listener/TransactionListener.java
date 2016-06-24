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
        PushRegisterCenter.getInstance(DictUtil.GOODSTYPE_LTB).start();
        PushRegisterCenter.getInstance(DictUtil.GOODSTYPE_BTB).start();
        PushRegisterCenter.getInstance(DictUtil.GOODSTYPE_YTB).start();
        // new VolumeDataAnalysis(DictUtil.GOODSTYPE_BTB);
        new VolumeDataAnalysis(DictUtil.GOODSTYPE_YTB);
        // new VolumeDataAnalysis(DictUtil.GOODSTYPE_LTB);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
