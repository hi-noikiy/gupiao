package push;

import java.io.IOException;
import java.util.Collection;

import javax.websocket.Session;

import com.alibaba.fastjson.JSONObject;

import config.DictUtil;
import domain.TransactionRecord;
import push.repository.PushDataRepository;
import push.repository.PushInterface;
import push.repository.RealtimePushDataRepository;

public class PushRegisterCenter {

    private static PushRegisterCenter center = new PushRegisterCenter();

    /**
     * 交易记录数据缓存仓库.
     */
    private PushDataRepository<TransactionRecord> detailRepo = new RealtimePushDataRepository<>();
    private PushDataRepository<TransactionRecord> btcDetailRepo = new RealtimePushDataRepository<>();

    private PushRegisterCenter() {
    }

    /**
     * 开启仓库缓存.
     */
    public void start() {
        detailRepo.start();
        btcDetailRepo.start();
    }

    public static PushRegisterCenter getInstance() {
        return center;
    }

    public void pushTR(TransactionRecord tr) {
        detailRepo.push(tr);
    }

    public void pushBtc(TransactionRecord tr) {
        btcDetailRepo.push(tr);
    }


    /**
     * 注册监听.
     *
     * @param pushInterface
     */
    public void register(String type, PushInterface pushInterface) {
        switch (type) {
            case DictUtil.GOODSTYPE_BTB:
                btcDetailRepo.register(pushInterface);
                break;
            case DictUtil.GOODSTYPE_YTB:
                detailRepo.register(pushInterface);
                break;
        }
    }


    /**
     * 解除注册.
     *
     * @param pushInterface
     */
    public void unregsiter(PushInterface pushInterface) {
        detailRepo.unregister(pushInterface);
        btcDetailRepo.unregister(pushInterface);
    }


}
