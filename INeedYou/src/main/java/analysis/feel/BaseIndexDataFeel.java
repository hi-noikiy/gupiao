package analysis.feel;

import analysis.index.BaseDataIndex;
import util.SayUtil;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by huangming on 2016/6/25.
 */
public class BaseIndexDataFeel implements IndexDataFeel {

    @Override
    public void feel(Map<String, BigDecimal> map) {
        BigDecimal maxprice = map.get(BaseDataIndex.maxprice);
        if (maxprice.doubleValue() > 85) {
            SayUtil.say(maxprice.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        }
    }


}
