package analysis;

import java.util.Date;

import domain.TransactionRecord;
import push.PushRegisterCenter;
import push.repository.PushInterface;

/**
 * 针对当前交易情况，返回后10s的涨幅情况.
 * <p>
 * Created by huangming on 2016/6/17.
 */
public abstract class BaseDataAnalysis {

    private PushRegisterCenter center;
    private TRPushInterface trpush = new TRPushInterface();

    protected BaseDataAnalysis(String type) {
        center = PushRegisterCenter.getInstance(type);
        center.register(trpush);
    }

    /**
     * 获取指定时间预测的值.
     *
     * @param date
     * @return
     */
    public abstract double getPrice(Date date);

    /**
     * 分析数据，并存好.
     *
     * @param t
     */
    protected abstract void save(TransactionRecord t);

    private class TRPushInterface implements PushInterface<TransactionRecord> {
        @Override
        public void push(TransactionRecord t) {
            save(t);
        }
    }

}
