package cn.hm.gupiao.domain;

public class BuyRecord {

	/** ID. */
	private int id;
	/** 用户ID. */
	private String userId;
	/** 平台类型. */
	private String palType;
	/** 交易方向. */
	private String direction;
	/** 数量. */
	private String amount;
	/** 价格. */
	private String price;
	/** 操作时间. */
	private String opTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getOpTime() {
		return opTime;
	}
	public void setOpTime(String opTime) {
		this.opTime = opTime;
	}
	
}
