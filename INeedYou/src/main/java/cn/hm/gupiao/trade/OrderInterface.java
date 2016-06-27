package cn.hm.gupiao.trade;

import cn.hm.gupiao.domain.TransactionRecord;

/**
 * Created by huangming on 2016/6/26.
 */
interface OrderInterface {

    /**
     * 出售.
     */
    public boolean sell(TransactionRecord transactionRecord);

    /**
     * 购买.
     */
    public boolean buy(TransactionRecord transactionRecord);

}
