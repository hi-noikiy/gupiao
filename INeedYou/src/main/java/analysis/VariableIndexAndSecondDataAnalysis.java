package analysis;

import analysis.feel.BaseIndexDataFeel;
import analysis.feel.IndexDataFeel;
import analysis.index.BaseDataIndex;
import analysis.index.DataIndex;
import domain.TransactionRecord;
import util.CircleArray;
import util.SayUtil;

import java.math.BigDecimal;
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
     * 以下是各个指标.
     **/
    private int a = 0;

    /**
     * 交易记录存储.
     */
    private LinkedList<TransactionRecord> tempList = new LinkedList<>();
    /**
     * 指标算法汇总.
     */
    private LinkedList<DataIndex> analysisList = new LinkedList<>();
    private LinkedList<IndexDataFeel> feelList = new LinkedList<>();
    private CircleArray<Map<String, BigDecimal>> indexDataList = new CircleArray<>(MAX_SIZE);

    /**
     * 声音提示间隔.
     */
    private long lastVoiceTime = 0;

    public VariableIndexAndSecondDataAnalysis(String type) {
        super(type);
        analysis();

        analysisList.add(new BaseDataIndex());
        feelList.add(new BaseIndexDataFeel());
    }

    @Override
    public double getPrice(Date date) {
        return 0;
    }

    @Override
    protected void save(TransactionRecord t) {
        tempList.add(t);
    }

    public void analysis() {
        /** 定时汇总数据. */
        new Timer().schedule(new FetchSecondData(), 0, 1000);
        /** 启动定时决策. */
        // TODO
    }

    /**
     * 定时汇总数据.
     */
    private class FetchSecondData extends TimerTask {
        @Override
        public void run() {
            LinkedList<TransactionRecord> nowRecordList = VariableIndexAndSecondDataAnalysis.this.tempList;
            tempList = new LinkedList<>();

            // 分析器，记录本身分析时间.
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MILLISECOND, 0);
            Map<String, BigDecimal> anaIndexMap = new HashMap<>();
            for (DataIndex dataIndex : analysisList) {
                anaIndexMap.putAll(dataIndex.execute(nowRecordList, indexDataList));
            }
            for (IndexDataFeel feel : feelList) {
                feel.feel(anaIndexMap);
            }
            indexDataList.add(anaIndexMap);

            System.out.println(String.format("%s %s %s", String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":"
                            + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":"
                            + String.format("%02d", calendar.get(Calendar.SECOND)), anaIndexMap.get(BaseDataIndex.maxprice).setScale(2, BigDecimal.ROUND_HALF_EVEN),
                    anaIndexMap.get(BaseDataIndex.minprice).setScale(2, BigDecimal.ROUND_HALF_EVEN)));
        }
    }
}
