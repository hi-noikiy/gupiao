package cn.hm.gupiao.config;

public class DictUtil {

	/** 货币类型：以太币. */
	public final static String GOODSTYPE_YTB = "eth";
	/** 货币类型：比特币. */
	public final static String GOODSTYPE_BTB = "btc";
	/** 货币类型：莱特币. */
	public final static String GOODSTYPE_LTB = "ltc";
	/** 货币类型：人民币. */
	public final static String GOODSTYPE_CNY = "cny";
	/** 货币类型：美元. */
	public final static String GOODSTYPE_USD = "usd";

    /** 交易类型：以太币转人民币. buy 则是用人民币买，sell就是用以太币卖，下面的交易类型一样.*/
	public final static String TRADE_TYPE_YTBTOCNY = "eth";
    /** 交易类型：莱特币转人民币.*/
	public final static String TRADE_TYPE_LTBTOCNY = "ltc";
    /** 交易类型：以太币转比特币.*/
    public final static String TRADE_TYPE_ETHTOBTB = "eth_btc";
    /** 交易类型：比特币转人民币.*/
    public final static String TRADE_TYPE_BTBTOCNY = "btc";

	/** 交易方向：买入. */
	public final static String TRADEDIRECT_BUY = "buy";
	/** 交易方向：卖出. */
	public final static String TRADEDIRECT_SELL = "sell";
	/** 平台类型：比特币中国. */
	public final static String PALTYPE_BTC = "cnbtc";
	/** 平台类型: OKCOIN. */
	public final static String PALTYPE_OKCOIN = "okcoin";

}
