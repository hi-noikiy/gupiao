package cn.hm.gupiao.analysis.index;

import cn.hm.gupiao.domain.TransactionRecord;
import cn.hm.gupiao.util.CircleArray;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangming on 2016/6/25.
 */
public class VolumnDataIndex implements DataIndex {

    public static final String volumnamount5 = "volumnamount5";
    public static final String volumnamount30 = "volumnamount30";

    public VolumnDataIndex() {
    }

    @Override
    public String getName() {
        return "成交量统计分析";
    }

    @Override
    public String getDescript() {
        return "成交量统计分析";
    }

    @Override
    public Map<String, Double> execute(List<TransactionRecord> transactionRecords, CircleArray<Map<String, Double>> historyIndexData) {
        BigDecimal totalVolumn = new BigDecimal(0);
        BigDecimal totalVolumn5 = new BigDecimal(0);
        BigDecimal totalVolumn30 = new BigDecimal(0);
        for (TransactionRecord transactionRecord : transactionRecords) {
            totalVolumn = totalVolumn.add(BigDecimal.valueOf(transactionRecord.getAmount()));
        }

        for (int i = 0; i < 5; i++) {
            Map<String, Double> map = historyIndexData.getBefore(i);
            if (map != null) {
                totalVolumn5 = totalVolumn5.add(totalVolumn);
            }
        }
        for (int i = 0; i < 30; i++) {
            Map<String, Double> map = historyIndexData.getBefore(i);
            if (map != null) {
                totalVolumn30 = totalVolumn30.add(totalVolumn);
            }
        }
        HashMap<String, Double> map = new HashMap<>();
        map.put(volumnamount5, totalVolumn5.doubleValue());
        map.put(volumnamount30, totalVolumn30.doubleValue());
        return map;
    }
}
