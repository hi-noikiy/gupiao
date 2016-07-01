package cn.hm.gupiao.analysis.index;

import cn.hm.gupiao.analysis.BaseDataAnalysis;
import cn.hm.gupiao.domain.TransactionRecord;
import cn.hm.gupiao.util.CircleArray;
import cn.hm.gupiao.util.GupiaoUtil;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by huangming on 2016/6/30.
 */
public class MACDDataIndex implements DataIndex {


    public static final String lastprice = "lastprice";

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
        int index = 0;
        if (historyIndexData.size() > lg * 2) {
            for (Map<String, Double> map : historyIndexData) {
                if (index >= lg * 2) {
                    break;
                }
                if (map != null) {
                    Double aDouble = map.get(lastprice);
                    if (aDouble == null) {
                        list.add(Double.valueOf(0));
                    } else {
                        list.add(aDouble);
                    }
                } else {
                    list.add(Double.valueOf(0));
                }
                index++;
            }

            HashMap<String, Double> macd = GupiaoUtil.getMACD(list, st, lg, mid);
            System.out.println(macd);
        }

        HashMap<String, Double> map = new HashMap();
        map.put(lastprice, lasPrice.doubleValue());
        return map;
    }
}
