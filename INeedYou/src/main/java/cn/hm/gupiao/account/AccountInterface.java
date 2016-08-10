package cn.hm.gupiao.account;

import cn.hm.gupiao.account.domain.Account;

/**
 * 账户操作。
 * <p>
 * Created by huangming on 2016/6/26.
 */
public interface AccountInterface {

    /**
     * 账户初始化.
     */
    public void init();

    /**
     * 账户资金添加.
     */
    public void add(double mn);

    /**
     * 账户资金减少.
     */
    public void sub(double mn);

    /**
     * 账户资金借入.
     */
    public void borrow(double mn);

    /**
     * 账户资金偿还.
     */
    public void repay(double mn);

}
