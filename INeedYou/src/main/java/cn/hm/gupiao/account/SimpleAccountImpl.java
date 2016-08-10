package cn.hm.gupiao.account;

import cn.hm.gupiao.account.domain.Account;

/**
 * Created by huangming on 2016/6/26.
 */
public class SimpleAccountImpl implements AccountInterface {

    public static Account account = new Account();
    private double money = 0;
    private double bmoney = 0;

    @Override
    public void init() {
        money = 0;
    }

    @Override
    public void add(double mn) {
        money += mn;
    }

    @Override
    public void sub(double mn) {
        money -= mn;
    }

    @Override
    public void borrow(double mn) {
        money += mn;
        bmoney += mn;
    }

    @Override
    public void repay(double mn) {
        money -= mn;
        bmoney -= mn;
    }
}