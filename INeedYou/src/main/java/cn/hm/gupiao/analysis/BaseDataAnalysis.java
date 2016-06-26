package cn.hm.gupiao.analysis;

import cn.hm.gupiao.domain.TransactionRecord;
import cn.hm.gupiao.push.repository.PushInterface;

/**
 * 针对当前交易情况，返回后10s的涨幅情况.
 * <p>
 * Created by huangming on 2016/6/17.
 */
public abstract class BaseDataAnalysis implements PushInterface<TransactionRecord> {

    protected BaseDataAnalysis() {
    }

    /**
     * 分析数据，并存好.
     *
     * @param t
     */
    protected abstract void save(TransactionRecord t);

    @Override
    public final void push(TransactionRecord t) {
        save(t);
    }


}
