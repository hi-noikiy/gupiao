package cn.hm.gupiao.snatch.analysis.index;

import java.util.List;
import java.util.Map;

import cn.hm.gupiao.domain.TransactionRecord;
import cn.hm.gupiao.util.CircleArray;

public interface DataIndex {

    public String getName();

    public String getDescript();

    public Map<String, Double> execute(List<TransactionRecord> transactionRecords, CircleArray<Map<String, Double>> historyIndexData);

}
