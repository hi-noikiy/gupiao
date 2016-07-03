package cn.hm.gupiao.snatch.client;

import cn.hm.gupiao.config.DictUtil;
import cn.hm.gupiao.domain.TransactionRecord;
import cn.hm.gupiao.push.PushRegisterCenter;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.websocket.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;

/**
 * Created by huangming on 2016/7/2.
 */
@ClientEndpoint
public class OkCoinClient {

    private PushRegisterCenter center;

    public OkCoinClient(PushRegisterCenter center) {
        this.center = center;
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws IOException {
        System.out.println("交易日志记录开启!");
        // session.getBasicRemote().sendText("{'event':'addChannel','channel':'ok_sub_spotcny_btc_trades','parameters':{'api_key':'c821db84-6fbd-11e4-a9e3-c86000d26d7c','sign':'4CBB1D1518F8BEE4040CE6B14F225C82'}}");
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
                        center.getInstance(DictUtil.GOODSTYPE_BTB).push(record);
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
                        center.getInstance(DictUtil.GOODSTYPE_LTB).push(record);
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
    }

    @OnError
    public void onError(Throwable t) {
        System.out.println("交易日志记录关闭!");
        t.printStackTrace();
    }

}
