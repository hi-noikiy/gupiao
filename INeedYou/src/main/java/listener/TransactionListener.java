package listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import analysis.FixedIndexAndSecondDataAnalysis;
import analysis.VariableIndexAndSecondDataAnalysis;
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
        // new FixedIndexAndSecondDataAnalysis(DictUtil.GOODSTYPE_BTB);
        //new FixedIndexAndSecondDataAnalysis(DictUtil.GOODSTYPE_YTB);
        new VariableIndexAndSecondDataAnalysis(DictUtil.GOODSTYPE_YTB);
        // new FixedIndexAndSecondDataAnalysis(DictUtil.GOODSTYPE_LTB);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
