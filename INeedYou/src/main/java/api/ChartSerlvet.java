package api;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import domain.TransactionRecord;
import org.json.JSONObject;

import push.PushRegisterCenter;
import push.repository.PushInterface;

import java.io.IOException;

@ServerEndpoint(value = "/chart")
public class ChartSerlvet {

    private Session session;
    private TRPushInterface trpush;

    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject mess = new JSONObject(message);
        String type = mess.getString("type");
        switch (type) {
            case "detail":
                PushRegisterCenter.getInstance().register(trpush);
                break;
        }
    }

    @OnClose
    public void onClose() {
        PushRegisterCenter.getInstance().unregsiter(trpush);
        System.out.println("关闭websocket");
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        trpush = new TRPushInterface(session);
        System.out.println("开启websocket");

    }

    @OnError
    public void onError(Throwable e) {
        e.printStackTrace();
        PushRegisterCenter.getInstance().unregsiter(trpush);
        System.out.println("错误websocket");
    }

    /**
     * 交易记录监听接口实现.
     *
     * @author huangming
     */
    private class TRPushInterface implements PushInterface<TransactionRecord> {
        private Session session;

        public TRPushInterface(Session session) {
            this.session = session;
        }

        @Override
        public void push(TransactionRecord t) {
            String jsonString = com.alibaba.fastjson.JSONObject.toJSONString(t);
            try {
                if (session.isOpen()) {
                    session.getBasicRemote().sendText(jsonString);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
