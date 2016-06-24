package service.impl;

import config.DictUtil;
import dao.ClientRecordDao;
import dao.TransactionRecordDao;
import dao.impl.ClientRecordDaoImpl;
import dao.impl.TransactionRecordDaoImpl;
import domain.ClientRecord;
import domain.TransactionRecord;
import org.json.JSONArray;
import org.json.JSONObject;
import push.PushRegisterCenter;
import service.SnatchService;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OkCoinSnatchServiceImpl implements SnatchService {

    private ClientRecordDao recordDao = new ClientRecordDaoImpl();
    private TransactionRecordDao transactionRecordDao = new TransactionRecordDaoImpl();
    private PushRegisterCenter ytbCenter = PushRegisterCenter.getInstance(DictUtil.GOODSTYPE_YTB);
    private PushRegisterCenter ltbCenter = PushRegisterCenter.getInstance(DictUtil.GOODSTYPE_LTB);
    private PushRegisterCenter btbCenter = PushRegisterCenter.getInstance(DictUtil.GOODSTYPE_BTB);

    private String websocketUrl = "wss://real.okcoin.cn:10440/websocket/okcoinapi";


    @Override
    public void sync() {
        /**
         * 实时同步Cnbtc网站的交易数据.
         */
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        String uri = websocketUrl;
        try {
            container.connectToServer(new MyClient(), URI.create(uri));
            container.setAsyncSendTimeout(2000);
        } catch (DeploymentException e) {
            throw new Error(e);
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    @ClientEndpoint
    public class MyClient {

        private Session session;

        public MyClient() {
        }

        @OnOpen
        public void onOpen(Session session, EndpointConfig config) throws IOException {
            System.out.println("交易日志记录开启!");
            this.session = session;
            /*
            // 注册请求分析
			{"event":"addChannel","channel":"chbtcethbtc_kline_15min"}
			{'event':'addChannel','channel':'eth_btc_lasttrades'}
			{'event':'addChannel','channel':'eth_btc_depth'}
			{"event":"addChannel","channel":"chbtcethcny_kline_15min"}
			{'event':'addChannel','channel':'eth_cny_lasttrades'}
			{'event':'addChannel','channel':'eth_cny_lasttrades'}   ip.dst_host==kline.chbtc.com
			*/
            // 发送买卖委托单监听
            // session.getBasicRemote().sendText("{'event':'addChannel','channel':'eth_cny_depth'}");
            // 发送成交记录
            //42["addPushType","{millInterval : 300,type : 'ok_ltc_ticker',binary:true}"]
            session.getBasicRemote().sendText("{'event':'addChannel','channel':'ok_sub_spotcny_btc_trades','parameters':{'api_key':'c821db84-6fbd-11e4-a9e3-c86000d26d7c','sign':'4CBB1D1518F8BEE4040CE6B14F225C82'}}");
            session.getBasicRemote().sendText("{'event':'addChannel','channel':'ok_sub_spotcny_ltc_trades','parameters':{'api_key':'c821db84-6fbd-11e4-a9e3-c86000d26d7c','sign':'4CBB1D1518F8BEE4040CE6B14F225C82'}}");
            //session.getBasicRemote().sendText("{\"event\":\"addChannel\",\"channel\":\"chbtcethcny_kline_1min\"}");
            // session.getBasicRemote().sendText("{'event':'addChannel','channel':'btc_cny_lasttrades'}");
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
                            btbCenter.pushTR(record);
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
                            ltbCenter.pushTR(record);
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
