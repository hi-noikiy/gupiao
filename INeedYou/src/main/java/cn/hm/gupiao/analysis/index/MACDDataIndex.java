package cn.hm.gupiao.analysis.index;

import cn.hm.gupiao.analysis.BaseDataAnalysis;
import cn.hm.gupiao.domain.TransactionRecord;
import cn.hm.gupiao.util.CircleArray;
import cn.hm.gupiao.util.GupiaoUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;

/**
 * Created by huangming on 2016/6/30.
 */
public class MACDDataIndex implements DataIndex {


    public static final String lastprice = "lastprice";

    private CircleArray<Double> ema = new CircleArray<Double>(2);
    private CircleArray<Double> dea = new CircleArray<Double>(2);
    private CircleArray<Double> dif = new CircleArray<Double>(2);

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
        BigDecimal lasPrice = new BigDecimal(0);

        if (transactionRecords.size() > 0) {
            // 有记录时进行记录分析.
            for (TransactionRecord transactionRecord : transactionRecords) {
                lasPrice = new BigDecimal(transactionRecord.getPrice());
            }
        } else {
            Map<String, Double> beforeIndexData = historyIndexData.getBefore();
            // 无记录时赋予默认值.
            if (beforeIndexData != null && beforeIndexData.get(lastprice) != null) {
                lasPrice = BigDecimal.valueOf(beforeIndexData.get(lastprice));
            }
        }

        List<Double> list = new ArrayList<>(50);

        int st = 12;
        int lg = 26;
        int mid = 9;
        for (Map<String, Double> map : historyIndexData) {
            map.get(lastprice);
        }

        return null;
    }
}
