package cn.hm.gupiao.service.impl;

import cn.hm.gupiao.analysis.VariableIndexAndSecondDataAnalysis;
import cn.hm.gupiao.analysis.feel.BaseIndexDataFeel;
import cn.hm.gupiao.analysis.index.BaseDataIndex;
import cn.hm.gupiao.analysis.index.VolumnDataIndex;
import cn.hm.gupiao.config.DictUtil;
import cn.hm.gupiao.dao.ClientRecordDao;
import cn.hm.gupiao.dao.TransactionRecordDao;
import cn.hm.gupiao.dao.impl.ClientRecordDaoImpl;
import cn.hm.gupiao.dao.impl.TransactionRecordDaoImpl;
import cn.hm.gupiao.domain.ClientRecord;
import cn.hm.gupiao.domain.TransactionRecord;
import org.json.JSONArray;
import org.json.JSONObject;
import cn.hm.gupiao.push.PushRegisterCenter;
import cn.hm.gupiao.service.SnatchService;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ChbtcSnatchServiceImpl implements SnatchService {

    private ClientRecordDao recordDao = new ClientRecordDaoImpl();
    private TransactionRecordDao transactionRecordDao = new TransactionRecordDaoImpl();
    private PushRegisterCenter center = new PushRegisterCenter();

    private String uri = "wss://kline.chbtc.com/websocket";

    @Override
    public void sync() {
        // center.getInstance(DictUtil.GOODSTYPE_LTB).start();
        // center.getInstance(DictUtil.GOODSTYPE_BTB).start();
        center.getInstance(DictUtil.GOODSTYPE_YTB).register(new VariableIndexAndSecondDataAnalysis(Arrays.asList(new BaseDataIndex(), new VolumnDataIndex()), Arrays.asList(new BaseIndexDataFeel(DictUtil.GOODSTYPE_YTB, DictUtil.PALTYPE_BTC)))).start();

        /**
         * 实时同步Cnbtc网站的交易数据.
         */
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
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
            session.getBasicRemote().sendText("{'event':'addChannel','channel':'eth_cny_lasttrades'}");
            session.getBasicRemote().sendText("{'event':'addChannel','channel':'ltc_cny_lasttrades'}");
            session.getBasicRemote().sendText("{'event':'addChannel','channel':'btc_cny_lasttrades'}");
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
            JSONObject jsonT = new JSONObject(message);
            String channel = jsonT.getString("channel");
            /** 获取当前时间. */
            Date now = new Date();
            if ("eth_cny_depth".equals(channel)) {
                if (1 == 1) return;
                /** 获取买入委托记录. */
                JSONArray asksArr = jsonT.getJSONArray("asks");
                /** 获取卖出委托记录. */
                JSONArray bidsArr = jsonT.getJSONArray("bids");

                ClientRecord record = new ClientRecord();
                List<ClientRecord> cacheList = new ArrayList<>(50);
                for (int i = 0; i < asksArr.length(); i++) {
                    JSONArray item = asksArr.getJSONArray(i);
                    double price = item.getDouble(0);
                    double amount = item.getDouble(1);
                    ClientRecord c = (ClientRecord) record.clone();
                    c.setAmount(amount);
                    c.setDirection(DictUtil.TRADEDIRECT_IN);
                    c.setGoodType(DictUtil.GOODSTYPE_YTB);
                    c.setOpTime(now);
                    c.setPalType(DictUtil.PALTYPE_BTC);
                    c.setPrice(price);
                    cacheList.add(c);
                }
                for (int i = 0; i < bidsArr.length(); i++) {
                    JSONArray item = bidsArr.getJSONArray(i);
                    double price = item.getDouble(0);
                    double amount = item.getDouble(1);
                    ClientRecord c = (ClientRecord) record.clone();
                    c.setAmount(amount);
                    c.setDirection(DictUtil.TRADEDIRECT_OUT);
                    c.setGoodType(DictUtil.GOODSTYPE_YTB);
                    c.setOpTime(now);
                    c.setPalType(DictUtil.PALTYPE_BTC);
                    c.setPrice(price);
                    cacheList.add(c);
                }
                recordDao.insertBatch(cacheList);
                cacheList.clear();
                cacheList = null;
            } else if ("eth_cny_lasttrades".equals(channel)) {
                JSONArray dataArr = jsonT.getJSONArray("data");
                List<TransactionRecord> cacheList = new ArrayList<TransactionRecord>(50);
                for (int i = 0; i < dataArr.length(); i++) {
                    JSONObject item = dataArr.getJSONObject(i);
                    double amount = item.getDouble("amount");
                    double price = item.getDouble("price");
                    String trade_type = item.getString("trade_type");
                    String type = item.getString("type");

                    TransactionRecord record = new TransactionRecord();
                    record.setAmount(amount);
                    record.setDirection(type);
                    record.setGoodType(DictUtil.GOODSTYPE_YTB);
                    record.setOpTime(now);
                    record.setPalType(DictUtil.PALTYPE_BTC);
                    record.setPrice(price);
                    cacheList.add(record);
                    center.getInstance(DictUtil.GOODSTYPE_YTB).push(record);
                }
                transactionRecordDao.insertBatch(cacheList);
                cacheList.clear();
            } else if ("btc_cny_lasttrades".equals(channel)) {
                JSONArray dataArr = jsonT.getJSONArray("data");
                List<TransactionRecord> cacheList = new ArrayList<TransactionRecord>(50);
                for (int i = 0; i < dataArr.length(); i++) {
                    JSONObject item = dataArr.getJSONObject(i);
                    double amount = item.getDouble("amount");
                    double price = item.getDouble("price");
                    String trade_type = item.getString("trade_type");
                    String type = item.getString("type");

                    TransactionRecord record = new TransactionRecord();
                    record.setAmount(amount);
                    record.setDirection(type);
                    record.setGoodType(DictUtil.GOODSTYPE_BTB);
                    record.setOpTime(now);
                    record.setPalType(DictUtil.PALTYPE_BTC);
                    record.setPrice(price);
                    cacheList.add(record);
                    center.getInstance(DictUtil.GOODSTYPE_BTB).push(record);
                }
                transactionRecordDao.insertBatch(cacheList);
                cacheList.clear();
            } else if ("ltc_cny_lasttrades".equals(channel)) {
                JSONArray dataArr = jsonT.getJSONArray("data");
                List<TransactionRecord> cacheList = new ArrayList<TransactionRecord>(50);
                for (int i = 0; i < dataArr.length(); i++) {
                    JSONObject item = dataArr.getJSONObject(i);
                    double amount = item.getDouble("amount");
                    double price = item.getDouble("price");
                    String trade_type = item.getString("trade_type");
                    String type = item.getString("type");

                    TransactionRecord record = new TransactionRecord();
                    record.setAmount(amount);
                    record.setDirection(type);
                    record.setGoodType(DictUtil.GOODSTYPE_LTB);
                    record.setOpTime(now);
                    record.setPalType(DictUtil.PALTYPE_BTC);
                    record.setPrice(price);
                    cacheList.add(record);
                    center.getInstance(DictUtil.GOODSTYPE_LTB).push(record);
                }
                transactionRecordDao.insertBatch(cacheList);
                cacheList.clear();
            } else {
                System.out.println(message);
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
