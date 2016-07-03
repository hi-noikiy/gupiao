package cn.hm.gupiao.snatch.analysis.feel;

import cn.hm.gupiao.account.SimpleAccountImpl;
import cn.hm.gupiao.snatch.analysis.index.BaseDataIndex;
import cn.hm.gupiao.snatch.analysis.index.MACDDataIndex;
import cn.hm.gupiao.config.DictUtil;
import cn.hm.gupiao.domain.TransactionRecord;
import cn.hm.gupiao.trade.TrandeOperator;

import java.util.Map;

/**
 * 高端交易程序.
 * <p>
 * Created by huangming on 2016/6/26.
 */
public class ICanBuyDataFeel implements IndexDataFeel {

    private TrandeOperator trandeOperator;

    public ICanBuyDataFeel(TrandeOperator trandeOperatorde) {
        this.trandeOperator = trandeOperatorde;
    }

    @Override
    public void feel(Map<String, Double> map) {
        Double maxprice = map.get(BaseDataIndex.maxprice);
        Double minprice = map.get(BaseDataIndex.minprice);
        Double macd = map.get(MACDDataIndex.macd);
        Double bmacd = map.get(MACDDataIndex.macd);
        Double lastprice = map.get(MACDDataIndex.lastprice);

        if (maxprice == null || minprice == null) {
            return;
        }

        TransactionRecord t = new TransactionRecord();
        t.setAmount(100);
        t.setPrice(lastprice);
        t.setGoodType(DictUtil.GOODSTYPE_LTB);

        /*
        if (minprice < 26.3 && minprice > 0) {
            trandeOperator.buy(t);
        }

        if (maxprice > 26.3) {
            trandeOperator.sell(t);
        }
        */

        double i = 0;
        if (bmacd > 0 && macd > 0) {
            if (bmacd / macd > 1.5) {
                // 卖出信号
                i--;
            }
            if (macd / bmacd > 1.5) {
                // 买入信号
                i++;
            }
            if (bmacd > macd && macd < 1.0) {
                // 卖出信号
                i--;
            }
        }
        if (bmacd < -1.0 && macd >= -0.9) {
            // 买入信号
            i++;
        }
        if (macd > 0) {
            i = i + 0.5;
        }
        if (macd < 0) {
            i = i - 0.5;
        }

        if (i < 0) {
            trandeOperator.sell(t);
            System.out.println("卖:" + SimpleAccountImpl.account);
        } else if (i > 0) {
            trandeOperator.buy(t);
            System.out.println("买:" + SimpleAccountImpl.account);
        } else {

        }

    }
}
