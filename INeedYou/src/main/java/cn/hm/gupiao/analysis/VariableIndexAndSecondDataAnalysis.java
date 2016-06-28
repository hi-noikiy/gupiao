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

    private DataIndex[] analysisListCache;
    private IndexDataFeel[] feelListCache;

    /**
     * 存储指标.
     */
    private CircleArray<Map<String, Double>> indexDataList = new CircleArray<>(MAX_SIZE);

    public VariableIndexAndSecondDataAnalysis() {
        tempList = new ArrayList<>(300);

        analysisList = new ArrayList<>(10);
        feelList = new ArrayList<>(10);
        analysisListCache = new DataIndex[0];
        feelListCache = new IndexDataFeel[0];

        analysis();
    }

    public VariableIndexAndSecondDataAnalysis(List<DataIndex> dList) {
        tempList = new ArrayList<>(300);

        analysisList = dList;
        feelList = new ArrayList<>(10);
        analysisListCache = analysisList.toArray(new DataIndex[analysisList.size()]);
        feelListCache = new IndexDataFeel[0];

        analysis();
    }

    public VariableIndexAndSecondDataAnalysis(List<DataIndex> dList, List<IndexDataFeel> iList) {
        tempList = new ArrayList<>(300);

        analysisList = dList;
        feelList = iList;

        analysisListCache = analysisList.toArray(new DataIndex[analysisList.size()]);
        feelListCache = feelList.toArray(new IndexDataFeel[feelList.size()]);

        analysis();
    }

    /**
     * 注册指标.
     *
     * @param dataIndex
     */
    public void registerIndex(DataIndex dataIndex) {
        analysisList.add(dataIndex);
        analysisListCache = analysisList.toArray(new DataIndex[analysisList.size()]);
    }

    /**
     * 注册感知.
     *
     * @param feel
     */
    public void registerFeel(IndexDataFeel feel) {
        feelList.add(feel);
        feelListCache = feelList.toArray(new IndexDataFeel[feelList.size()]);
    }

    @Override
    protected void save(TransactionRecord t) {
        tempList.add(t);
    }

    public void analysis() {
        /** 定时汇总数据，并触发分析. */
        new Timer().schedule(new FetchSecondData(), 0, TIME_SIZE);
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
            for (DataIndex dataIndex : analysisListCache) {
                Map<String, Double> map = dataIndex.execute(nowRecordList, indexDataList);
                if (map != null)
                    anaIndexMap.putAll(map);
            }
            if (anaIndexMap.size() > 0) {
                for (IndexDataFeel feel : feelListCache) {
                    feel.feel(anaIndexMap);
                }
            }
            indexDataList.add(anaIndexMap);
        }
    }
}
