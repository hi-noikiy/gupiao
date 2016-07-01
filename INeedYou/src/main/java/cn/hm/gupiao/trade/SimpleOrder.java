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
        Double aDouble = SimpleAccountImpl.account.getFree().get(transactionRecord.getGoodType());
        BigDecimal money = BigDecimal.valueOf(transactionRecord.getAmount()).multiply(BigDecimal.valueOf(transactionRecord.getPrice()));
        BigDecimal amount = BigDecimal.valueOf(transactionRecord.getAmount());
        Double cnyDouble = SimpleAccountImpl.account.getFree().get(DictUtil.GOODSTYPE_CNY);
        Double ytbDouble = SimpleAccountImpl.account.getFree().get(DictUtil.GOODSTYPE_YTB);
        if (aDouble == null) {
            SimpleAccountImpl.account.getFree().put(transactionRecord.getGoodType(), BigDecimal.valueOf(0).subtract(amount).setScale(2, BigDecimal.ROUND_CEILING).doubleValue());
            SimpleAccountImpl.account.getFree().put(DictUtil.GOODSTYPE_CNY, BigDecimal.valueOf(0).add(money).setScale(2, BigDecimal.ROUND_CEILING).doubleValue());
        } else {
            SimpleAccountImpl.account.getFree().put(transactionRecord.getGoodType(), BigDecimal.valueOf(ytbDouble).subtract(amount).setScale(2, BigDecimal.ROUND_CEILING).doubleValue());
            SimpleAccountImpl.account.getFree().put(DictUtil.GOODSTYPE_CNY, BigDecimal.valueOf(cnyDouble).add(money).setScale(2, BigDecimal.ROUND_CEILING).doubleValue());
        }
        return true;
    }

    @Override
    public boolean buy(TransactionRecord transactionRecord) {
        Double aDouble = SimpleAccountImpl.account.getFree().get(transactionRecord.getGoodType());
        BigDecimal money = BigDecimal.valueOf(transactionRecord.getAmount()).multiply(BigDecimal.valueOf(transactionRecord.getPrice()));
        BigDecimal amount = BigDecimal.valueOf(transactionRecord.getAmount());

        Double cnyDouble = SimpleAccountImpl.account.getFree().get(DictUtil.GOODSTYPE_CNY);
        Double ytbDouble = SimpleAccountImpl.account.getFree().get(DictUtil.GOODSTYPE_YTB);

        if (aDouble == null) {
            SimpleAccountImpl.account.getFree().put(transactionRecord.getGoodType(), BigDecimal.valueOf(0).subtract(money).setScale(2, BigDecimal.ROUND_CEILING).doubleValue());
            SimpleAccountImpl.account.getFree().put(transactionRecord.getGoodType(), BigDecimal.valueOf(0).add(amount).setScale(2, BigDecimal.ROUND_CEILING).doubleValue());
        } else {
            SimpleAccountImpl.account.getFree().put(DictUtil.GOODSTYPE_CNY, BigDecimal.valueOf(cnyDouble).subtract(money).setScale(2, BigDecimal.ROUND_CEILING).doubleValue());
            SimpleAccountImpl.account.getFree().put(DictUtil.GOODSTYPE_YTB, BigDecimal.valueOf(ytbDouble).add(amount).setScale(2, BigDecimal.ROUND_CEILING).doubleValue());
        }
        return true;
    }
}