package cn.hm.gupiao.snatch.analysis.index;

import cn.hm.gupiao.domain.TransactionRecord;
import cn.hm.gupiao.util.CircleArray;
import cn.hm.gupiao.util.GupiaoUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangming on 2016/6/30.
 */
public class RSIDataIndex implements DataIndex {

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescript() {
        return null;
    }

    @Override
    public Map<String, Double> execute(List<TransactionRecord> transactionRecords, CircleArray<Map<String, Double>> historyIndexData) {
        HashMap<String, Double> map = new HashMap();
        return map;
    }
}
