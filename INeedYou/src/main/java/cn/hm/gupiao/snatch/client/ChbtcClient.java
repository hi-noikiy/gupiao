package cn.hm.gupiao.snatch.client;

import cn.hm.gupiao.config.DictUtil;
import cn.hm.gupiao.domain.ClientRecord;
import cn.hm.gupiao.domain.TransactionRecord;
import cn.hm.gupiao.push.PushRegisterCenter;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.websocket.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;

/**
 * Created by huangming on 2016/6/27.
 */
@ClientEndpoint
public class ChbtcClient {

    private PushRegisterCenter center;

    public ChbtcClient(PushRegisterCenter center) {
        this.center = center;
    }


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
        // session.getBasicRemote().sendText("{'event':'addChannel','channel':'ltc_cny_lasttrades'}");
        // session.getBasicRemote().sendText("{'event':'addChannel','channel':'btc_cny_lasttrades'}");
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
            for (int i = 0; i < asksArr.length(); i++) {
                JSONArray item = asksArr.getJSONArray(i);
                double price = item.getDouble(0);
                double amount = item.getDouble(1);
                ClientRecord c = (ClientRecord) record.clone();
                c.setAmount(amount);
                c.setDirection(DictUtil.TRADEDIRECT_BUY);
                c.setGoodType(DictUtil.GOODSTYPE_YTB);
                c.setOpTime(now);
                c.setPalType(DictUtil.PALTYPE_BTC);
                c.setPrice(price);
            }
            for (int i = 0; i < bidsArr.length(); i++) {
                JSONArray item = bidsArr.getJSONArray(i);
                double price = item.getDouble(0);
                double amount = item.getDouble(1);
                ClientRecord c = (ClientRecord) record.clone();
                c.setAmount(amount);
                c.setDirection(DictUtil.TRADEDIRECT_SELL);
                c.setGoodType(DictUtil.GOODSTYPE_YTB);
                c.setOpTime(now);
                c.setPalType(DictUtil.PALTYPE_BTC);
                c.setPrice(price);
            }
        } else if ("eth_cny_lasttrades".equals(channel)) {
            JSONArray dataArr = jsonT.getJSONArray("data");
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
                center.getInstance(DictUtil.GOODSTYPE_YTB).push(record);
            }
        } else if ("btc_cny_lasttrades".equals(channel)) {
            JSONArray dataArr = jsonT.getJSONArray("data");
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
                center.getInstance(DictUtil.GOODSTYPE_BTB).push(record);
            }
        } else if ("ltc_cny_lasttrades".equals(channel)) {
            JSONArray dataArr = jsonT.getJSONArray("data");
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
                center.getInstance(DictUtil.GOODSTYPE_LTB).push(record);
            }
        } else {
            System.out.println(message);
        }
    }

    @OnClose
    public void onClose() {
        System.out.println("交易日志记录关闭!");
    }

    @OnError
    public void onError(Throwable t) {
        System.out.println("交易日志记录关闭!");
        t.printStackTrace();
    }
}
