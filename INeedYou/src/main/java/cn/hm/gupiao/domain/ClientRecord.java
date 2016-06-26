package cn.hm.gupiao.domain;

import java.util.Date;

public class ClientRecord implements Cloneable {

	/** ID. */
	private int id;
	/** 委托方. */
	private String client;
	/** 平台类型. */
	private String palType;
	/** 交易方向. */
	private String direction;
	/** 数量. */
	private double amount;
	/** 价格. */
	private double price;
	/** 操作时间. */
	private Date opTime;
	/** 物品类型. */
	private String goodType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getPalType() {
		return palType;
	}

	public void setPalType(String palType) {
		this.palType = palType;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getGoodType() {
		return goodType;
	}

	public void setGoodType(String goodType) {
		this.goodType = goodType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Date getOpTime() {
		return opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

}
