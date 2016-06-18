package push;

import java.io.IOException;
import java.util.Collection;

import javax.websocket.Session;

import com.alibaba.fastjson.JSONObject;

import domain.TransactionRecord;
import push.repository.PushDataRepository;
import push.repository.PushInterface;
import push.repository.RealtimePushDataRepository;

public class PushRegisterCenter {

    private static PushRegisterCenter center = new PushRegisterCenter();

    /**
     * 交易记录数据缓存仓库.
     */
    private PushDataRepository<TransactionRecord> detailRepo = new RealtimePushDataRepository<TransactionRecord>();

    private PushRegisterCenter() {
    }

    /**
     * 开启仓库缓存.
     */
    public void start() {
        detailRepo.start();
    }

    public static PushRegisterCenter getInstance() {
        return center;
    }

    public void pushTR(Collection<TransactionRecord> collect) {
        detailRepo.push(collect);
    }

    public void pushTR(TransactionRecord tr) {
        detailRepo.push(tr);
    }


    /**
     * 注册监听.
     *
     * @param pushInterface
     */
    public void register(PushInterface pushInterface) {
        detailRepo.register(pushInterface);
    }


    /**
     * 解除注册.
     *
     * @param pushInterface
     */
    public void unregsiter(PushInterface pushInterface) {
        detailRepo.unregister(pushInterface);
    }


}
