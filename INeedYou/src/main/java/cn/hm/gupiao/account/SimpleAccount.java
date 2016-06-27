package cn.hm.gupiao.account;

import cn.hm.gupiao.domain.Account;

/**
 * Created by huangming on 2016/6/26.
 */
public class SimpleAccount implements AccountInterface {

    public static Account account = new Account();

    @Override
    public void update(Account account) {
        System.out.println(account);
        account.setFree(this.account.getFree());
    }

}
