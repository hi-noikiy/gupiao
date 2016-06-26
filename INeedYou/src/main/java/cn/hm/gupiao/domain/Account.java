package cn.hm.gupiao.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * 账户信息.
 */
public class Account {
    private int id;
    private String username;
    private String palType;

    /**
     * 账户借款信息.
     */
    private Map<String, Double> borrow = new HashMap<>();
    /**
     * 账户净资产.
     */
    private Map<String, Double> asset = new HashMap<>();
    /**
     * 账户余额.
     */
    private Map<String, Double> free = new HashMap<>();
    /**
     * 冻结资产.
     */
    private Map<String, Double> freezed = new HashMap<>();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPalType() {
        return palType;
    }

    public void setPalType(String palType) {
        this.palType = palType;
    }

    public Map<String, Double> getBorrow() {
        return borrow;
    }

    public void setBorrow(Map<String, Double> borrow) {
        this.borrow = borrow;
    }

    public Map<String, Double> getAsset() {
        return asset;
    }

    public void setAsset(Map<String, Double> asset) {
        this.asset = asset;
    }

    public Map<String, Double> getFree() {
        return free;
    }

    public void setFree(Map<String, Double> free) {
        this.free = free;
    }

    public Map<String, Double> getFreezed() {
        return freezed;
    }

    public void setFreezed(Map<String, Double> freezed) {
        this.freezed = freezed;
    }

    @Override
    public String toString() {
        return "Account [id=" + id + ", username=" + username + ", palType=" + palType + "]";
    }
}
