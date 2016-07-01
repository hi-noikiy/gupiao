package cn.hm.gupiao.snatch;

import cn.hm.gupiao.account.SimpleAccountImpl;
import cn.hm.gupiao.analysis.VariableIndexAndSecondDataAnalysis;
import cn.hm.gupiao.analysis.feel.BaseIndexDataFeel;
import cn.hm.gupiao.analysis.feel.ICanBuyDataFeel;
import cn.hm.gupiao.analysis.index.BaseDataIndex;
import cn.hm.gupiao.analysis.index.MACDDataIndex;
import cn.hm.gupiao.analysis.index.VolumnDataIndex;
import cn.hm.gupiao.client.ChbtcClient;
import cn.hm.gupiao.config.DictUtil;
import cn.hm.gupiao.controller.AccountTradeController;
import cn.hm.gupiao.domain.Account;
import cn.hm.gupiao.push.PushRegisterCenter;
import cn.hm.gupiao.push.repository.PushDataRepository;
import cn.hm.gupiao.trade.SimpleOrder;
import cn.hm.gupiao.trade.TrandeOperator;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.*;

public class ChbtcSnatchServiceImpl implements SnatchService {

    private String uri = "wss://kline.chbtc.com/websocket";
    private PushRegisterCenter center = new PushRegisterCenter();
    private VariableIndexAndSecondDataAnalysis analysis = new VariableIndexAndSecondDataAnalysis(60000);

    @Override
    public void sync() {
        // center.getInstance(DictUtil.GOODSTYPE_LTB).start();
        // center.getInstance(DictUtil.GOODSTYPE_BTB).start();

        /**
         * 实时同步Cnbtc网站的交易数据.
         */
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            container.setAsyncSendTimeout(5000);
            container.connectToServer(new ChbtcClient(center), URI.create(uri));
        } catch (DeploymentException e) {
            throw new Error(e);
        } catch (IOException e) {
            throw new Error(e);
        }

        Account account = new Account();
        account.setFree(new HashMap<>());
        account.setUsername("aaa");
        account.setBorrow(new HashMap<>());
        account.getFree().put(DictUtil.GOODSTYPE_YTB, Double.valueOf(1000));
        account.getFree().put(DictUtil.GOODSTYPE_CNY, Double.valueOf(1000));

        PushDataRepository instance = center.getInstance(DictUtil.GOODSTYPE_YTB);
        PushDataRepository ltbinstance = center.getInstance(DictUtil.GOODSTYPE_LTB);

        /** 配置指标. */
        analysis.registerIndex(new BaseDataIndex());
        analysis.registerIndex(new VolumnDataIndex());
        analysis.registerIndex(new MACDDataIndex());

        /** 配置感知. */
        analysis.registerFeel(new BaseIndexDataFeel(DictUtil.GOODSTYPE_YTB, DictUtil.PALTYPE_BTC));
        // analysis.registerFeel(new ICanBuyDataFeel(new TrandeOperator(new AccountTradeController(account, new SimpleAccountImpl()), new SimpleOrder())));

        instance.register(analysis);

        instance.start();
        ltbinstance.start();
    }

}
