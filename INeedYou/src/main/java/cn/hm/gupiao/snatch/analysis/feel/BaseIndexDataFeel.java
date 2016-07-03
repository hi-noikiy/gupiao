package cn.hm.gupiao.snatch.analysis.feel;

import cn.hm.gupiao.snatch.analysis.index.BaseDataIndex;
import cn.hm.gupiao.snatch.analysis.index.VolumnDataIndex;
import cn.hm.gupiao.config.DictUtil;
import cn.hm.gupiao.util.SayUtil;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 基础语音提示加日志提示.
 * <p>
 * Created by huangming on 2016/6/25.
 */
public class BaseIndexDataFeel implements IndexDataFeel {
    private String pal;
    private String type;

    public BaseIndexDataFeel(String pal, String type) {
        this.pal = pal;
        this.type = type;
    }

    @Override
    public void feel(Map<String, Double> map) {
        Double maxprice = map.get(BaseDataIndex.maxprice);
        Double incpriceb5 = map.get(BaseDataIndex.incpriceb5);
        Double incpriceb30 = map.get(BaseDataIndex.incpriceb30);
        Double volumnamount = map.get(BaseDataIndex.volumnamount);
        Double volummoney = map.get(BaseDataIndex.volumnmoney);
        Double volumnamount5 = map.get(VolumnDataIndex.volumnamount5);
        Double volumnamount30 = map.get(VolumnDataIndex.volumnamount30);

        if (maxprice == null || incpriceb5 == null || volummoney == null) {
            return;
        }

        if (DictUtil.GOODSTYPE_LTB.equals(type) && maxprice < 26.3) {
            SayUtil.say("买" + b());
        }
        if (DictUtil.GOODSTYPE_YTB.equals(type) && maxprice < 91 && maxprice > 70) {
            SayUtil.say("买" + b());
        }
        if (maxprice > 85) {
            // SayUtil.say(maxprice.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        }
        if (incpriceb5 > 0.5) {
            SayUtil.say(b() + "5秒涨" + BigDecimal.valueOf(incpriceb5).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        }
        if (incpriceb30 > 1) {
            SayUtil.say(b() + "30秒涨" + BigDecimal.valueOf(incpriceb30).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        } else {
            if (incpriceb30 > 0.3) {
                SayUtil.say(b() + "30秒微涨" + BigDecimal.valueOf(incpriceb30).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            }
        }
        if (volumnamount5 == 0) {
            // SayUtil.say("5秒无交易");
        }
        if (incpriceb5 < -0.5) {
            SayUtil.say(b() + "5秒跌" + BigDecimal.valueOf(incpriceb5).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        }
        if (incpriceb30 < -1) {
            SayUtil.say(b() + "30秒跌" + BigDecimal.valueOf(incpriceb30).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        } else {
            if (incpriceb30 < -0.2) {
                //SayUtil.say("30秒微跌" + BigDecimal.valueOf(incpriceb30).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            }
        }
        /*
        if (volumnamount > 1000) {
            SayUtil.say("成交量" + BigDecimal.valueOf(volumnamount).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        }
        if (volumnamount5 > 5000) {
            SayUtil.say("5秒成交量" + BigDecimal.valueOf(volumnamount5).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        }
        */

    }

    public String b() {
        switch (pal) {
            case DictUtil.GOODSTYPE_LTB:
                return "莱特币";
            case DictUtil.GOODSTYPE_YTB:
                return "以太币";
            default:
                return "";
        }
    }


}
