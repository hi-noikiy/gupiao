package cn.hm.gupiao.analysis.feel;

import cn.hm.gupiao.analysis.index.BaseDataIndex;
import cn.hm.gupiao.config.DictUtil;
import cn.hm.gupiao.domain.TransactionRecord;
import cn.hm.gupiao.trade.TrandeOperator;
import cn.hm.gupiao.util.SayUtil;

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

        if (maxprice == null || minprice == null) {
            return;
        }

        if (minprice < 26.3 && minprice > 0) {
            TransactionRecord t = new TransactionRecord();
            t.setAmount(10);
            t.setPrice(26);
            t.setGoodType(DictUtil.GOODSTYPE_YTB);
            trandeOperator.buy(t);
        }

        if (maxprice > 26.3) {
            TransactionRecord t = new TransactionRecord();
            t.setAmount(10);
            t.setPrice(26);
            t.setGoodType(DictUtil.GOODSTYPE_YTB);
            trandeOperator.sell(t);
        }
    }
}
