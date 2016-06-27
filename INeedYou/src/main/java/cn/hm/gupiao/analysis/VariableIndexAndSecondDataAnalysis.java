package cn.hm.gupiao.analysis;

import cn.hm.gupiao.analysis.feel.IndexDataFeel;
import cn.hm.gupiao.analysis.index.DataIndex;
import cn.hm.gupiao.domain.TransactionRecord;
import cn.hm.gupiao.util.CircleArray;

import java.util.*;

/**
 * 动态指标配置分析器(最低单位：秒)
 * <p>
 * Created by huangming on 2016/6/24.
 */
public class VariableIndexAndSecondDataAnalysis extends BaseDataAnalysis {

    /**
     * 缓存20秒的数据作为分析依据.
     */
    private final static int MAX_SIZE = 120;

    /**
     * 时间间隔.
     */
    private final static int TIME_SIZE = 1000;

    /**
     * 交易记录存储.
     */
    private ArrayList<TransactionRecord> tempList;
    /**
     * 指标算法汇总.
     */
    private List<DataIndex> analysisList;
    private List<IndexDataFeel> feelList;
    private CircleArray<Map<String, Double>> indexDataList = new CircleArray<>(MAX_SIZE);

    public VariableIndexAndSecondDataAnalysis(List<DataIndex> dList, List<IndexDataFeel> iList) {
        analysisList = new ArrayList<>();
        tempList = new ArrayList<>(300);
        analysisList = dList;
        feelList = iList;
        analysis();
    }

    @Override
    protected void save(TransactionRecord t) {
        tempList.add(t);
    }

    public void analysis() {
        /** 定时汇总数据. */
        new Timer().schedule(new FetchSecondData(), 0, TIME_SIZE);
        /** 启动定时决策. */
        // TODO
    }

    /**
     * 定时汇总数据.
     */
    private class FetchSecondData extends TimerTask {
        @Override
        public void run() {
            ArrayList<TransactionRecord> nowRecordList = VariableIndexAndSecondDataAnalysis.this.tempList;
            tempList = new ArrayList<>(300);
            // 分析器，记录本身分析时间.
            Map<String, Double> anaIndexMap = new HashMap<>();
            for (DataIndex dataIndex : analysisList) {
                anaIndexMap.putAll(dataIndex.execute(nowRecordList, indexDataList));
            }
            for (IndexDataFeel feel : feelList) {
                feel.feel(anaIndexMap);
            }
            indexDataList.add(anaIndexMap);
        }
    }
}
