package cn.hm.gupiao.analysis;

import cn.hm.gupiao.domain.TransactionRecord;
import cn.hm.gupiao.push.PushRegisterCenter;
import cn.hm.gupiao.util.SayUtil;

import java.util.*;

/**
 * 成交量数据分析器。 根据当前时间段成交量简答判断下一步的涨跌情况，较为简单粗暴。 基本规则： 在成交量急剧上升时，同时价格在10秒内猛涨。
 * 预测价格上升。按照10秒前后价格推算。 在成交量急剧上升时，同时价格在5秒内猛跌。 预测价格下降。 按照5秒前后价格推算。
 * <p>
 * 统计指标： 每秒： 均价、最高价、最低价、价差、正差合计、负差合计、资金成交值、成交数量 每15秒：
 * <p>
 * 时间算法架构. 采用一个缓存区域，共享至多个时间频率的算法中. 秒<分
 * <p>
 * 1、保存数据对象 2、清洗数据，获取需要的指标数据.
 * <p>
 * <p>
 * Created by huangming on 2016/6/17.
 */
public class FixedIndexAndSecondDataAnalysis extends BaseDataAnalysis {

    /**
     * 缓存20秒的数据作为分析依据.
     */
    private final static int MAX_SIZE = 60;

    /** 以下是各个指标. **/

    /**
     * 当前最小价格.
     **/
    private final static int COUNTER_PRICE_MIN = 1;
    /**
     * 当前最大价格.
     **/
    private final static int COUNTER_PRICE_MAX = 2;
    /**
     * 当前均价.
     **/
    private final static int COUNTER_PRICE_AVG = 3;
    /**
     * 价位蛇形折线差合计.
     **/
    private final static int COUNTER_PRICE_INCREASE = 4;
    /**
     * 正价位蛇形折线差合计.
     **/
    private final static int COUNTER_PRICE_PINCREASE = 5;

    /**
     * 成交次数.
     */
    private final static int COUNTER_VOLUMN_TIME = 6;
    /**
     * 成交量总净额.
     */
    private final static int COUNTER_VOLUMN_MONEY = 7;
    /**
     * 成交总数量.
     */
    private final static int COUNTER_VOLUMN_AMOUNT = 8;
    /**
     * 成交量蛇形折线差合计.
     */
    private final static int COUNTER_VOLUMN_INCREASE = 9;
    /**
     * 成交量正蛇形折线差合计.
     */
    private final static int COUNTER_VOLUMN_PINCREASE = 10;

    /**
     * 当前价格方差.
     */
    private final static int COUNTER_OTHER_VARIANCE = 11;

    private final static int COUNTER_MAXINDEX = 12;

    /**
     * 交易记录存储.
     */
    private LinkedList<TransactionRecord>[] tranList = new LinkedList[MAX_SIZE];
    private LinkedList<TransactionRecord> tempList = new LinkedList<>();

    /**
     * 指标数据汇总.
     */
    private double[][] counter = new double[MAX_SIZE][COUNTER_MAXINDEX];

    private int start_index = MAX_SIZE - 1;
    private int end_index = 0;

    private String pt = null;
    private String bz = null;


    /**
     * 声音提示间隔.
     */
    private long lastVoiceTime = 0;

    public FixedIndexAndSecondDataAnalysis(PushRegisterCenter center) {
        analysis();
    }

    @Override
    protected void save(TransactionRecord t) {
        if (pt == null)
            pt = t.getPalType();
        if (bz == null)
            bz = t.getGoodType();

        tempList.add(t);
    }

    public void analysis() {
        /** 定时汇总数据. */
        new Timer().schedule(new FetchSecondData(), 0, 1000);
        /** 启动定时决策. */
        // TODO
    }

    /**
     * 比较指定秒差数据.
     *
     * @param a
     * @param b
     * @return
     */
    public double[] compare(double[] a, double[] b) {
        if (a.length == b.length) {
            double[] c = new double[a.length];
            for (int i = 0; i < a.length; i++) {
                c[i] = a[i] - b[i];
            }
            return c;
        } else {
            return null;
        }
    }

    /**
     * 汇总几秒内的数据.
     *
     * @param start_index
     * @param size
     * @return
     */
    public double[] total(int start_index, int size) {
        double[] total = new double[COUNTER_MAXINDEX];
        for (int i = 0; i < size; i++) {
            int index = (start_index + MAX_SIZE - i) % MAX_SIZE;
            for (int j = 0; j < counter[i].length; j++) {
                total[j] += counter[index][j];
            }
        }
        return total;
    }


    /**
     * 定时汇总数据.
     */
    private class FetchSecondData extends TimerTask {
        @Override
        public void run() {
            start_index++;
            end_index++;
            start_index = start_index % 60;
            end_index = end_index % 60;
            tranList[start_index] = tempList;
            tempList = new LinkedList<>();

            // 分析器，记录本身分析时间.
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MILLISECOND, 0);

            // 当前时间数据清晰.
            /**
             * 当前秒段数据指标提取.
             */
            int innnerStartIndex = start_index;
            LinkedList<TransactionRecord> ll = tranList[innnerStartIndex];

            double lastPrice = 0;
            double lastVolumn = 0;
            if (ll != null && ll.size() > 0) {
                for (TransactionRecord transactionRecord : ll) {
                    double price = transactionRecord.getPrice();
                    double volume = transactionRecord.getAmount() * transactionRecord.getPrice();

                    if (price > counter[innnerStartIndex][COUNTER_PRICE_MAX]) {
                        counter[innnerStartIndex][COUNTER_PRICE_MAX] = price;
                    }
                    if (price < counter[innnerStartIndex][COUNTER_PRICE_MIN]) {
                        counter[innnerStartIndex][COUNTER_PRICE_MIN] = 0;
                    }
                    counter[innnerStartIndex][COUNTER_VOLUMN_AMOUNT] += transactionRecord.getAmount();
                    counter[innnerStartIndex][COUNTER_VOLUMN_MONEY] += volume;
                    counter[innnerStartIndex][COUNTER_VOLUMN_TIME]++;

                    if (lastPrice == 0 || lastVolumn == 0) {
                        counter[innnerStartIndex][COUNTER_VOLUMN_INCREASE] = 0;
                        counter[innnerStartIndex][COUNTER_PRICE_INCREASE] = 0;
                        counter[innnerStartIndex][COUNTER_PRICE_PINCREASE] = 0;
                        counter[innnerStartIndex][COUNTER_VOLUMN_PINCREASE] = 0;
                    } else {
                        counter[innnerStartIndex][COUNTER_VOLUMN_INCREASE] += price - lastPrice;
                        counter[innnerStartIndex][COUNTER_VOLUMN_PINCREASE] = price - lastPrice > 0 ? price - lastPrice : 0;
                        counter[innnerStartIndex][COUNTER_PRICE_INCREASE] += volume - lastPrice;
                        counter[innnerStartIndex][COUNTER_PRICE_PINCREASE] = volume - lastPrice > 0 ? volume - lastPrice : 0;
                    }
                    lastPrice = price;
                    lastVolumn = volume;
                }
                counter[innnerStartIndex][COUNTER_PRICE_AVG] = counter[innnerStartIndex][COUNTER_VOLUMN_MONEY]
                        / counter[innnerStartIndex][COUNTER_VOLUMN_AMOUNT];

            } else {
                int before_index = (start_index + MAX_SIZE - 1) % MAX_SIZE;
                counter[start_index][COUNTER_PRICE_MIN] = counter[before_index][COUNTER_PRICE_MIN];
                counter[start_index][COUNTER_PRICE_MAX] = counter[before_index][COUNTER_PRICE_MAX];
                counter[start_index][COUNTER_PRICE_AVG] = counter[before_index][COUNTER_PRICE_AVG];
                counter[start_index][COUNTER_PRICE_INCREASE] = 0;
                counter[start_index][COUNTER_PRICE_PINCREASE] = 0;
                counter[start_index][COUNTER_VOLUMN_TIME] = 0;
                counter[start_index][COUNTER_VOLUMN_MONEY] = 0;
                counter[start_index][COUNTER_VOLUMN_AMOUNT] = 0;
                counter[start_index][COUNTER_VOLUMN_INCREASE] = 0;
                counter[start_index][COUNTER_VOLUMN_PINCREASE] = 0;
                counter[start_index][COUNTER_OTHER_VARIANCE] = 0;
            }

            // 与过去时间合并比较.
            /** 汇总指标统计. */
            /** 5秒内汇总. */
            double[] total5 = total(start_index, 5);

            /** 求当前方差. */
            // TODO

            // System.out.print("\t平台:" + pt + "\t币种:" + bz);
            /** 三秒内差值比较. */
            //System.out.print("\t当前最高价:" + String.format("%.2f", counter[innnerStartIndex][COUNTER_PRICE_MAX]));

            /** 五秒内差值比较. */
            double[] compare5 = compare(counter[innnerStartIndex], counter[(innnerStartIndex + MAX_SIZE - 5) % MAX_SIZE]);

            /** 三十秒内差值比较. */
            double[] compare30 = compare(counter[innnerStartIndex], counter[(innnerStartIndex + MAX_SIZE - 30) % MAX_SIZE]);


            //System.out.print("\t5秒内交易量:" + String.format("%.2f", total5[COUNTER_VOLUMN_MONEY]));
            //System.out.print("\t  交易量:" + String.format("%.2f", counter[innnerStartIndex][COUNTER_VOLUMN_MONEY]));

            if (lastVoiceTime == 0 || lastVoiceTime + 5000 < System.currentTimeMillis()) {
                StringBuilder sayBu = new StringBuilder();
                if (counter[innnerStartIndex][COUNTER_PRICE_MAX] > 90) {
                    sayBu.append(String.format("%.2f", counter[innnerStartIndex][COUNTER_PRICE_MAX]));
                }
                if (counter[innnerStartIndex][COUNTER_PRICE_MAX] < 70) {
                    sayBu.append(String.format("%.2f", counter[innnerStartIndex][COUNTER_PRICE_MAX]));
                }
                if (counter[innnerStartIndex][COUNTER_VOLUMN_AMOUNT] > 1000) {
                    sayBu.append("成交" + counter[innnerStartIndex][COUNTER_VOLUMN_AMOUNT]);
                }
                if (compare30[COUNTER_PRICE_MAX] > 2) {
                    sayBu.append("涨了");
                }
                lastVoiceTime = System.currentTimeMillis();
                SayUtil.say(sayBu.toString());
            }

            System.out.println(String.format("%s 平台:%s\t币种:%s\t%s\t%s\t%s", String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":"
                            + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":"
                            + String.format("%02d", calendar.get(Calendar.SECOND)), pt, bz, "当前最高价:" + String.format("%.2f", counter[innnerStartIndex][COUNTER_PRICE_MAX]), simpleConsole(compare5, 5), simpleConsole(compare30, 30)
                    , "\t5秒内交易量:" + String.format("%.2f", total5[COUNTER_VOLUMN_MONEY])
                    , "\t  交易量:" + String.format("%.2f", counter[innnerStartIndex][COUNTER_VOLUMN_MONEY])));
        }
    }

    public String simpleConsole(double[] compare30, int sec) {
        StringBuilder sb = new StringBuilder();
        if (compare30 != null) {
            sb.append("\t" + sec + "秒内正在");
            if (compare30[COUNTER_PRICE_MAX] > 0) {
                if (compare30[COUNTER_PRICE_MAX] > 2) {
                    sb.append("涨↑");
                } else {
                    sb.append("涨↗");
                }
            } else if (compare30[COUNTER_PRICE_MAX] == 0) {
                sb.append("平-");
            } else {
                if (compare30[COUNTER_PRICE_MAX] < -2) {
                    sb.append("跌↓");
                } else {
                    sb.append("跌↘");
                }
            }
            sb.append("\t幅度:" + String.format("%.2f", compare30[COUNTER_PRICE_MAX]));
        }
        return sb.toString();
    }
}
