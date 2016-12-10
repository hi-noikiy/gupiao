package cn.hm.gupiao.snatch;

import cn.hm.gupiao.account.SimpleAccountImpl;
import cn.hm.gupiao.snatch.analysis.VariableIndexAndSecondDataAnalysis;
import cn.hm.gupiao.snatch.analysis.feel.BaseIndexDataFeel;
import cn.hm.gupiao.snatch.analysis.feel.ICanBuyDataFeel;
import cn.hm.gupiao.snatch.analysis.index.BaseDataIndex;
import cn.hm.gupiao.snatch.analysis.index.MACDDataIndex;
import cn.hm.gupiao.snatch.analysis.index.VolumnDataIndex;
import cn.hm.gupiao.snatch.client.OkCoinClient;
import cn.hm.gupiao.config.DictUtil;
import cn.hm.gupiao.trade.AccountTradeController;
import cn.hm.gupiao.domain.Account;
import cn.hm.gupiao.push.repository.PushDataRepository;
import cn.hm.gupiao.trade.SimpleOrder;
import cn.hm.gupiao.trade.TrandeOperator;
import cn.hm.gupiao.push.PushRegisterCenter;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

public class OkCoinSnatchServiceImpl implements SnatchService {

    private String websocketUrl = "wss://real.okcoin.cn:10440/client/okcoinapi";
    private PushRegisterCenter center = new PushRegisterCenter();
    private VariableIndexAndSecondDataAnalysis analysis;

    public OkCoinSnatchServiceImpl(int mill) {
        analysis = new VariableIndexAndSecondDataAnalysis(mill);
    }

    @Override
    public void sync() {
        /**
         * 开启币种和监听.
         */
        // centor.getInstance(DictUtil.GOODSTYPE_YTB).start();
        // centor.getInstance(DictUtil.GOODSTYPE_BTB).start();
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            container.setAsyncSendTimeout(5000);
            container.connectToServer(new OkCoinClient(center), URI.create(websocketUrl));
        } catch (DeploymentException e) {
            throw new Error(e);
        } catch (IOException e) {
            throw new Error(e);
        }

        Account account = new Account();
        account.setFree(new HashMap<>());
        account.setUsername("aaa");
        account.setBorrow(new HashMap<>());
        account.getFree().put(DictUtil.GOODSTYPE_CNY, Double.valueOf(10000));

        PushDataRepository instance = center.getInstance(DictUtil.GOODSTYPE_YTB);
        PushDataRepository ltbinstance = center.getInstance(DictUtil.GOODSTYPE_LTB);

        /** 配置指标. */
        analysis.registerIndex(new BaseDataIndex());
        analysis.registerIndex(new VolumnDataIndex());analysis.registerIndex(new MACDDataIndex());


        /** 配置感知. */
        analysis.registerFeel(new BaseIndexDataFeel(DictUtil.GOODSTYPE_YTB, DictUtil.PALTYPE_BTC));
        analysis.registerFeel(new ICanBuyDataFeel(new TrandeOperator(new AccountTradeController(account, new SimpleAccountImpl()), new SimpleOrder())));

        ltbinstance.register(analysis);

        instance.start();
        ltbinstance.start();
    }

}
