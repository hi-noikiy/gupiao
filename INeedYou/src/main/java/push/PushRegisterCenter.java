package push;

import domain.TransactionRecord;
import push.repository.PushDataRepository;
import push.repository.PushInterface;
import push.repository.RealtimePushDataRepository;

import java.util.HashMap;
import java.util.Map;

public class PushRegisterCenter {

    /**
     * 交易记录数据缓存仓库.
     */
    private PushDataRepository<TransactionRecord> detailRepo = new RealtimePushDataRepository<>();
    private static Map<String, PushRegisterCenter> objMap = new HashMap<>();

    private PushRegisterCenter() {
    }

    /**
     * 开启仓库缓存.
     */
    public void start() {
        detailRepo.start();
    }

    public static PushRegisterCenter getInstance(String type) {
        if (type != null && !"".equals(type)) {
            PushRegisterCenter pushRegisterCenter = objMap.get(type);
            if (pushRegisterCenter == null) {
                synchronized (PushRegisterCenter.class) {
                    if (pushRegisterCenter == null) {
                        pushRegisterCenter = new PushRegisterCenter();
                        objMap.put(type, pushRegisterCenter);
                    }
                }
            }
            return pushRegisterCenter;
        } else {
            return null;
        }
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
