package cn.hm.gupiao.push;

import cn.hm.gupiao.push.repository.PushDataRepository;
import cn.hm.gupiao.push.repository.RealtimePushDataRepository;

import java.util.HashMap;
import java.util.Map;

public class PushRegisterCenter {

    /**
     * 交易记录数据缓存仓库.
     */
    private Map<String, PushDataRepository> objMap = new HashMap<>();

    public PushRegisterCenter() {
    }

    /**
     * 开启仓库缓存.
     */
    public PushDataRepository getInstance(String type) {
        if (type != null && !"".equals(type)) {
            PushDataRepository pushRegisterCenter = objMap.get(type);
            if (pushRegisterCenter == null) {
                synchronized (PushRegisterCenter.class) {
                    if (pushRegisterCenter == null) {
                        pushRegisterCenter = new RealtimePushDataRepository();
                        objMap.put(type, pushRegisterCenter);
                    }
                }
            }
            return pushRegisterCenter;
        } else {
            return null;
        }
    }


}
