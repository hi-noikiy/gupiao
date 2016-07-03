package cn.hm.gupiao.trade;

import cn.hm.gupiao.account.SimpleAccountImpl;
import cn.hm.gupiao.config.DictUtil;
import cn.hm.gupiao.domain.TransactionRecord;

import java.math.BigDecimal;

/**
 * Created by huangming on 2016/6/26.
 */
public class SimpleOrder implements OrderInterface {
    @Override
    public boolean sell(TransactionRecord transactionRecord) {
        Double money = transactionRecord.getAmount() * transactionRecord.getPrice();
        Double amount = transactionRecord.getAmount();
        Double cnyDouble = SimpleAccountImpl.account.getFree().get(DictUtil.GOODSTYPE_CNY);
        Double ytbDouble = SimpleAccountImpl.account.getFree().get(transactionRecord.getGoodType());
        if (cnyDouble == null) {
            cnyDouble = new Double(0);
        }

        if (ytbDouble == null) {
            ytbDouble = new Double(0);
        }
        SimpleAccountImpl.account.getFree().put(transactionRecord.getGoodType(), ytbDouble - amount).doubleValue();
        SimpleAccountImpl.account.getFree().put(DictUtil.GOODSTYPE_CNY, cnyDouble + money);
        return true;
    }

    @Override
    public boolean buy(TransactionRecord transactionRecord) {
        Double money = transactionRecord.getAmount() * transactionRecord.getPrice();
        Double amount = transactionRecord.getAmount();
        Double cnyDouble = SimpleAccountImpl.account.getFree().get(DictUtil.GOODSTYPE_CNY);
        Double ytbDouble = SimpleAccountImpl.account.getFree().get(transactionRecord.getGoodType());
        if (cnyDouble == null) {
            cnyDouble = new Double(0);
        }

        if (ytbDouble == null) {
            ytbDouble = new Double(0);
        }
        SimpleAccountImpl.account.getFree().put(DictUtil.GOODSTYPE_CNY, cnyDouble - money);
        SimpleAccountImpl.account.getFree().put(transactionRecord.getGoodType(), ytbDouble + amount);
        return true;
    }
}
