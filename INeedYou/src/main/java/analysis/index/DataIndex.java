package analysis.index;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import domain.TransactionRecord;
import util.CircleArray;

public interface DataIndex {

    public String getName();

    public String getDescript();

    public Map<String, BigDecimal> execute(List<TransactionRecord> transactionRecords, CircleArray<Map<String, BigDecimal>> historyIndexData);

}
