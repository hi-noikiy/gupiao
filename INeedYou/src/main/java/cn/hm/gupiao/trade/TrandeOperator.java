package cn.hm.gupiao.trade;

import cn.hm.gupiao.account.AccountManager;
import cn.hm.gupiao.config.DictUtil;
import cn.hm.gupiao.domain.TransactionRecord;

import java.math.BigDecimal;

/**
 * Created by huangming on 2016/6/26.
 */
public class TrandeOperator {

    private OrderInterface orderInterface;
    private AccountManager accountManager;

    public TrandeOperator(AccountManager accountManager, OrderInterface orderInterface) {
        this.accountManager = accountManager;
        this.orderInterface = orderInterface;
    }

    /**
     * 根据交易记录进行交易操作.
     *
     * @param record
     * @return
     */
    public boolean buy(TransactionRecord record) {
        if (record == null)
            return false;

        /** 第一步，余额验证. */
        BigDecimal money = BigDecimal.valueOf(record.getAmount()).subtract(BigDecimal.valueOf(record.getPrice())).setScale(2, BigDecimal.ROUND_CEILING);
        Double aDouble = accountManager.getFree(DictUtil.GOODSTYPE_CNY);
        if (aDouble < money.doubleValue()) {
            System.out.print("交易失败：余额不足!");
            return false;
        }

        /** 第二步，发出交易. */
        boolean isSuccess = orderInterface.buy(record);

        /** 第三步，验证成功. */
        if (!isSuccess) {
            return false;
        }

        /** 第四步，更新账户信息. */
        accountManager.updateAccount();

        return true;
    }

    public boolean sell(TransactionRecord record) {
        if (record == null)
            return false;

        /** 第一步，余额验证. */
        BigDecimal money = BigDecimal.valueOf(record.getAmount()).subtract(BigDecimal.valueOf(record.getPrice())).setScale(2, BigDecimal.ROUND_CEILING);
        Double aDouble = accountManager.getFree(record.getGoodType());
        if (aDouble < money.doubleValue()) {
            System.out.print("交易失败：余额不足!");
            return false;
        }

        /** 第二步，发出交易. */
        boolean isSuccess = orderInterface.sell(record);

        /** 第三步，验证成功. */
        if (!isSuccess) {
            return false;
        }

        /** 第四步，更新账户信息. */
        accountManager.updateAccount();

        return true;
    }


}
