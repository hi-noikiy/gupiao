package cn.hm.gupiao.snatch;

import cn.hm.gupiao.analysis.VariableIndexAndSecondDataAnalysis;
import cn.hm.gupiao.analysis.feel.BaseIndexDataFeel;
import cn.hm.gupiao.analysis.index.BaseDataIndex;
import cn.hm.gupiao.analysis.index.VolumnDataIndex;
import cn.hm.gupiao.config.DictUtil;
import cn.hm.gupiao.dao.ClientRecordDao;
import cn.hm.gupiao.dao.TransactionRecordDao;
import cn.hm.gupiao.dao.impl.ClientRecordDaoImpl;
import cn.hm.gupiao.dao.impl.TransactionRecordDaoImpl;
import cn.hm.gupiao.domain.TransactionRecord;
import org.json.JSONArray;
import org.json.JSONObject;
import cn.hm.gupiao.push.PushRegisterCenter;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;

public class OkCoinSnatchServiceImpl implements SnatchService {

    private ClientRecordDao recordDao = new ClientRecordDaoImpl();
    private TransactionRecordDao transactionRecordDao = new TransactionRecordDaoImpl();

    private String websocketUrl = "wss://real.okcoin.cn:10440/client/okcoinapi";
    private PushRegisterCenter centor = new PushRegisterCenter();

    @Override
    public void sync() {
        /**
         * 开启币种和监听.
         */
        // centor.getInstance(DictUtil.GOODSTYPE_YTB).start();
        // centor.getInstance(DictUtil.GOODSTYPE_BTB).start();
        centor.getInstance(DictUtil.GOODSTYPE_LTB).register(new VariableIndexAndSecondDataAnalysis(Arrays.asList(new BaseDataIndex(), new VolumnDataIndex()), Arrays.asList(new BaseIndexDataFeel(DictUtil.GOODSTYPE_LTB, DictUtil.PALTYPE_OKCOIN)))).start();

        /**
         * 实时同步Cnbtc网站的交易数据.
         */
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        String uri = websocketUrl;
        try {
            container.setAsyncSendTimeout(5000);
            container.connectToServer(new MyClient(), URI.create(uri));
        } catch (DeploymentException e) {
            throw new Error(e);
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    @ClientEndpoint
    public class MyClient {

        @OnOpen
        public void onOpen(Session session, EndpointConfig config) throws IOException {
            System.out.println("交易日志记录开启!");
            session.getBasicRemote().sendText("{'event':'addChannel','channel':'ok_sub_spotcny_btc_trades','parameters':{'api_key':'c821db84-6fbd-11e4-a9e3-c86000d26d7c','sign':'4CBB1D1518F8BEE4040CE6B14F225C82'}}");
            session.getBasicRemote().sendText("{'event':'addChannel','channel':'ok_sub_spotcny_ltc_trades','parameters':{'api_key':'c821db84-6fbd-11e4-a9e3-c86000d26d7c','sign':'4CBB1D1518F8BEE4040CE6B14F225C82'}}");
        }

        @OnMessage
        public void onMessage(ByteBuffer message) {
            fenpei(new String(message.array()));
        }

        @OnMessage
        public void onMessage(String message) {
            fenpei(message);
        }

        public void fenpei(String message) {
            JSONArray jsonArr = new JSONArray(message);
            for (int index = 0; index < jsonArr.length(); index++) {
                JSONObject jsonT = jsonArr.getJSONObject(index);
                String channel = jsonT.getString("channel");
                Date now = new Date();
                if ("ok_sub_spotcny_btc_trades".equals(channel)) {
                    if (!jsonT.isNull("success")) {
                        if ("true".equals(jsonT.get("success"))) {
                            System.out.println("登陆成功!");
                        }
                    } else if (!jsonT.isNull("data")) {
                        JSONArray dataArr = jsonT.getJSONArray("data");
                        for (int i = 0; i < dataArr.length(); i++) {
                            JSONArray item = dataArr.getJSONArray(i);
                            TransactionRecord record = new TransactionRecord();
                            record.setAmount(item.getDouble(2));
                            record.setDirection(item.getString(4));
                            record.setGoodType(DictUtil.GOODSTYPE_BTB);
                            record.setOpTime(now);
                            record.setPalType(DictUtil.PALTYPE_OKCOIN);
                            record.setPrice(item.getDouble(1));
                            centor.getInstance(DictUtil.GOODSTYPE_BTB).push(record);
                        }
                    } else {
                        System.out.println(message);
                    }
                } else {
                    if (!jsonT.isNull("success")) {
                        if ("true".equals(jsonT.get("success"))) {
                            System.out.println("登陆成功!");
                        }
                    } else if (!jsonT.isNull("data")) {
                        JSONArray dataArr = jsonT.getJSONArray("data");
                        for (int i = 0; i < dataArr.length(); i++) {
                            JSONArray item = dataArr.getJSONArray(i);
                            TransactionRecord record = new TransactionRecord();
                            record.setAmount(item.getDouble(2));
                            record.setDirection(item.getString(4));
                            record.setGoodType(DictUtil.GOODSTYPE_LTB);
                            record.setOpTime(now);
                            record.setPalType(DictUtil.PALTYPE_OKCOIN);
                            record.setPrice(item.getDouble(1));
                            centor.getInstance(DictUtil.GOODSTYPE_LTB).push(record);
                        }
                    } else {
                        System.out.println(message);
                    }
                }
            }
        }

        @OnClose
        public void onClose() {
            System.out.println("交易日志记录关闭!");
            sync();
        }

        @OnError
        public void onError(Throwable t) {
            System.out.println("交易日志记录关闭!");
            t.printStackTrace();
        }
    }

}
