package cn.hm.gupiao.account;

import cn.hm.gupiao.domain.Account;

/**
 * 账户管理.
 * <p>
 * Created by huangming on 2016/6/26.
 */
public class AccountManager {
    private Account account;
    private AccountInterface accountInterface;

    public AccountManager(Account account, AccountInterface accountInterface) {
        this.account = account;
        this.accountInterface = accountInterface;
        SimpleAccount.account = account;
    }

    public Double getFree(String goodType) {
        updateAccount();
        return account.getFree().get(goodType);
    }

    public void updateAccount() {
        accountInterface.update(account);
    }

}
