package cn.hm.gupiao.controller;

import cn.hm.gupiao.account.AccountInterface;
import cn.hm.gupiao.account.SimpleAccountImpl;
import cn.hm.gupiao.domain.Account;

/**
 * 账户管理.
 * <p>
 * Created by huangming on 2016/6/26.
 */
public class AccountTradeController {
    private Account account;
    private Account oldAccount;
    private AccountInterface accountInterface;

    public AccountTradeController(Account account, AccountInterface accountInterface) {
        this.account = account;
        this.accountInterface = accountInterface;
        SimpleAccountImpl.account = account;
    }

    public Double getFree(String goodType) {
        updateAccount();
        return account.getFree().get(goodType);
    }

    public void updateAccount() {
        accountInterface.update(account);
    }

}
