package analysis.impl;

import analysis.BaseDataAnalysis;
import domain.TransactionRecord;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 成交量数据分析器。
 * 根据当前时间段成交量简答判断下一步的涨跌情况，较为简单粗暴。
 * 基本规则：
 * 在成交量急剧上升时，同时价格在10秒内猛涨。 预测价格上升。按照10秒前后价格推算。
 * 在成交量急剧上升时，同时价格在5秒内猛跌。 预测价格下降。 按照5秒前后价格推算。
 * <p>
 * Created by huangming on 2016/6/17.
 */
public class VolumeDataAnalysis extends BaseDataAnalysis {

    private final static int MAX_SIZE = 15;
    //private Queue<Queue<Double>> volumnTimeArr = new ArrayBlockingQueue(MAX_SIZE);
    private LinkedList<Double> volumnTimeArr[] = new LinkedList[MAX_SIZE];
    private double maxPriceTimeArr[] = new double[MAX_SIZE];
    private int lastupdatetime = 0;
    private int now_size = 0;
    private int start_index = MAX_SIZE - 1;
    private int end_index = 0;
    private LinkedHashMap<Long, Double> timeAndPrice = new LinkedHashMap<>();
    private LinkedHashMap<Long, LinkedList<Thread>> threadRegister = new LinkedHashMap<>();
    private final static int analyTimeUnit = 1000; // 1秒;

    public VolumeDataAnalysis() {
        analysis();
    }

    @Override
    public double getPrice(Date date) {
        Double aDouble = timeAndPrice.get(date.getTime());
        if (aDouble == null) {
            LinkedList<Thread> threads = threadRegister.get(date.getTime());
            synchronized (threadRegister) {
                if (threads == null) {
                    threads = new LinkedList<>();
                }
            }
            threads.add(Thread.currentThread());
            synchronized (date) {
                try {
                    Thread.currentThread().wait(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
            return timeAndPrice.get(date.getTime());
        } else {
            return aDouble;
        }
    }

    @Override
    protected void save(TransactionRecord t) {
        /**
         * 数据存储.
         */
        Calendar calendar = Calendar.getInstance();
        int seconds = calendar.get(Calendar.SECOND);
        if (lastupdatetime == 0) {
            lastupdatetime = seconds;
        } else {
            if (seconds != lastupdatetime) {
                // 切换到下一秒.
                start_index++;
                start_index = start_index % MAX_SIZE;
                end_index++;
                end_index = end_index % MAX_SIZE;
                if (volumnTimeArr[start_index] != null) {
                    volumnTimeArr[start_index].clear();
                }
                maxPriceTimeArr[start_index] = 0;
            }
        }

        if (volumnTimeArr[start_index] == null) {
            volumnTimeArr[start_index] = new LinkedList();
        }
        volumnTimeArr[start_index].push(t.getPrice() * t.getAmount());
        if (maxPriceTimeArr[start_index] < t.getPrice()) {
            maxPriceTimeArr[start_index] = t.getPrice();
        }
    }

    public void analysis() {
        new Timer().schedule(new VolumeAnalysis(), 0, 10000);
    }

    /**
     * 分析器.
     */
    private class VolumeAnalysis extends TimerTask {

        @Override
        public void run() {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MILLISECOND, 0);

            LinkedList<Double>[] volumnTimeArrClone = volumnTimeArr.clone();
            LinkedHashMap<Long, Double> timeAndPriceClone = (LinkedHashMap<Long, Double>) timeAndPrice.clone();
            int start_index = VolumeDataAnalysis.this.start_index;
            int end_index = VolumeDataAnalysis.this.end_index;


            // 最大成交量差值..
            double maxC = 0;
            // 成交量合计.
            double sum = 0;
            // 正差值合计
            double sumR = 0;
            // 正负差值合计
            double sumRL = 0;

            // 成交次数合计.
            int times = 0;
            // 最大成交差值.
            int maxTimes = 0;
            // 正差值合计.
            int sumTimesR = 0;
            // 正负差值合计.
            int sumTimesRL = 0;

            // 价格正负差值合计.
            double priceSumRL = 0;
            // 价格正差值合计
            double priceSumR = 0;


            /**
             * 分析成交量数据.
             */
            double lastSum = 0;
            int lastTimes = 0;

            for (int i = end_index; ; i++) {
                LinkedList<Double> doubles = volumnTimeArrClone[i % MAX_SIZE];
                if (doubles != null) {
                    double temp = 0;
                    for (Double aDouble : doubles) {
                        temp += aDouble;
                    }
                    int t = doubles.size() - lastTimes;
                    if (maxTimes < t) {
                        maxTimes = t;
                    }
                    double c = temp - lastSum;
                    if (maxC < c) {
                        maxC = c;
                    }

                    sum += temp;
                    times += doubles.size();
                    double chazhi = temp - lastSum;
                    if (lastSum > 0) {
                        sumRL += chazhi;
                        sumR += chazhi > 0 ? chazhi : 0;
                    }
                    int chazhiTimes = doubles.size() - lastTimes;
                    if (lastTimes > 0) {
                        sumTimesR += chazhiTimes > 0 ? chazhiTimes : 0;
                        sumTimesRL += chazhiTimes;
                    }
                    lastSum = temp;
                    lastTimes = times;
                }

                if (i % MAX_SIZE == start_index) {
                    break;
                }
            }

            /**
             * 分析价格数据.
             */
            System.out.print("获取最大价格：");
            double lastPrice = 0;

            for (int i = end_index; ; i++) {
                double aDouble = maxPriceTimeArr[i % MAX_SIZE];
                if (lastPrice > 0) {
                    double chazhi = aDouble - lastPrice;
                    priceSumR += chazhi > 0 ? chazhi : 0;
                    priceSumRL += chazhi;
                }
                System.out.print(String.format("%.2f", aDouble));
                System.out.print("\t");
                lastPrice = aDouble;

                if (i % MAX_SIZE == start_index) {
                    break;
                }
            }
            System.out.println();

            /**
             * 针对增长数据进行调整.
             */
            double increment = priceSumRL / maxPriceTimeArr.length;
            int inv = 1;
            if (sumR > 0) {
                if (sumRL / sumR > 0.5) {
                    increment *= 1.1;
                    inv += 1;
                }
            }
            if (sumTimesR > 0) {
                if (sumTimesRL / sumTimesR > 0.5) {
                    increment *= 1.1;
                    inv += 1;
                }
            }
            if (priceSumR > 9) {
                if (priceSumRL / priceSumR > 0.5) {
                    increment *= 1.1;
                    inv += 1;
                }
            }
            if (inv == 1) {
                // 无增幅。
                increment *= 0.8;
            }

            System.out.print("预测十秒(" + (increment > 0 ? "升" : increment == 0 ? "平" : "降") + ")：");
            double now = maxPriceTimeArr[0];
            if (now > 0) {
                for (int i = 0; i < maxPriceTimeArr.length; i++) {
                    now += increment;

                    System.out.print(String.format("%.2f", now));
                    System.out.print("\t");

                    LinkedList<Thread> threads = threadRegister.get(calendar.getTimeInMillis());
                    if (threads != null) {
                        threads.notify();
                    }
                }
            }
            System.out.println();
        }

    }

}
