package cn.hm.gupiao.snatch.analysis.index;

import cn.hm.gupiao.domain.TransactionRecord;
import cn.hm.gupiao.util.CircleArray;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDataIndex implements DataIndex {

    public static final String maxprice = "maxprice";
    public static final String minprice = "minprice";
    public static final String incprice = "incprice";
    public static final String pincprice = "pincprice";
    public static final String volumntimes = "volumntimes";
    public static final String volumnamount = "volumnamount";
    public static final String volumnmoney = "volumnmoney";
    public static final String incvolumnmoney = "incvolumnmoney";
    public static final String pincvolumnmoney = "pincvolumnmoney";
    public static final String maxpriceb30 = "maxprice30";
    public static final String incpriceb30 = "maxprice30";
    public static final String maxpriceb5 = "maxprice5";
    public static final String incpriceb5 = "maxprice5";


    public BaseDataIndex() {
    }

    @Override
    public String getName() {
        return "基础指标库";
    }

    @Override
    public String getDescript() {
        return "一些基本的指标";
    }

    @Override
    public Map<String, Double> execute(List<TransactionRecord> transactionRecords, CircleArray<Map<String, Double>> historyIndexData) {
        BigDecimal maxPrice = null;
        BigDecimal minPrice = null;

        BigDecimal incPirce = new BigDecimal(0);
        BigDecimal pincPrice = new BigDecimal(0);

        BigDecimal maxPriceb5 = new BigDecimal(0);
        BigDecimal maxPriceb30 = new BigDecimal(0);

        BigDecimal incPirceb5 = new BigDecimal(0);
        BigDecimal incPirceb30 = new BigDecimal(0);

        BigDecimal volumnAmount = new BigDecimal(0);
        BigDecimal volumnMoney = new BigDecimal(0);
        BigDecimal incVolumn = new BigDecimal(0);
        BigDecimal pincVolumn = new BigDecimal(0);

        int times = 0;

        Map<String, Double> beforeIndexData = historyIndexData.getBefore();
        Map<String, Double> beforeIndexData5 = historyIndexData.getBefore(5);
        Map<String, Double> beforeIndexData30 = historyIndexData.getBefore(30);

        if (beforeIndexData == null || beforeIndexData.size() == 0) {
            return null;
        }

        if (transactionRecords.size() > 0) {
            // 有记录时进行记录分析.
            for (TransactionRecord transactionRecord : transactionRecords) {
                BigDecimal price = new BigDecimal(transactionRecord.getPrice());
                BigDecimal amount = new BigDecimal(transactionRecord.getAmount());

                volumnMoney = volumnMoney.add(price.multiply(amount));
                volumnAmount = volumnAmount.add(amount);

                if (maxPrice == null) {
                    maxPrice = price;
                } else {
                    maxPrice = maxPrice.max(price);
                }
                if (minPrice == null) {
                    minPrice = price;
                } else {
                    minPrice = minPrice.min(price);
                }

                times++;
            }
            if (beforeIndexData != null && beforeIndexData.get(maxprice) != null) {
                BigDecimal lastPrice = BigDecimal.valueOf(beforeIndexData.get(maxprice));
                BigDecimal lastVolumn = BigDecimal.valueOf(beforeIndexData.get(volumnmoney));
                // inc
                incPirce = maxPrice.subtract(lastPrice);
                pincPrice = maxPrice.subtract(lastPrice).abs();
                // pinc
                incVolumn = volumnMoney.subtract(lastVolumn);
                pincVolumn = volumnMoney.subtract(lastVolumn).abs();
            }
            if (beforeIndexData5 != null && beforeIndexData5.get(maxprice) != null) {
                maxPriceb5 = BigDecimal.valueOf(beforeIndexData5.get(maxprice));
                incPirceb5 = maxPrice.subtract(maxPriceb5);
            }
            if (beforeIndexData30 != null && beforeIndexData30.get(maxprice) != null) {
                maxPriceb30 = BigDecimal.valueOf(beforeIndexData30.get(maxprice));
                incPirceb30 = maxPrice.subtract(maxPriceb30);
            }
        } else {
            // 无记录时赋予默认值.
            if (beforeIndexData != null && beforeIndexData.get(maxprice) != null) {
                maxPrice = BigDecimal.valueOf(beforeIndexData.get(maxprice));
                minPrice = BigDecimal.valueOf(beforeIndexData.get(minprice));
            }
            if (beforeIndexData5 != null && beforeIndexData5.get(maxprice) != null) {
                maxPriceb5 = BigDecimal.valueOf(beforeIndexData5.get(maxprice));
                incPirceb5 = maxPrice.subtract(maxPriceb5);
            }
            if (beforeIndexData30 != null && beforeIndexData30.get(maxprice) != null) {
                maxPriceb30 = BigDecimal.valueOf(beforeIndexData30.get(maxprice));
                incPirceb30 = maxPrice.subtract(maxPriceb30);
            }

        }

        if (maxPrice == null) {
            maxPrice = new BigDecimal(0);
        }
        if (minPrice == null) {
            minPrice = new BigDecimal(0);
        }

        BigDecimal volumnTime = BigDecimal.valueOf(times);

        HashMap<String, Double> map = new HashMap<>();
        map.put(maxprice, maxPrice.doubleValue());
        map.put(minprice, minPrice.doubleValue());
        map.put(incprice, incPirce.doubleValue());
        map.put(pincprice, pincPrice.doubleValue());
        map.put(volumntimes, volumnTime.doubleValue());
        map.put(volumnamount, volumnAmount.doubleValue());
        map.put(volumnmoney, volumnMoney.doubleValue());
        map.put(incvolumnmoney, incVolumn.doubleValue());
        map.put(pincvolumnmoney, pincVolumn.doubleValue());
        map.put(maxpriceb5, maxPriceb5.doubleValue());
        map.put(incpriceb5, incPirceb5.doubleValue());
        map.put(maxpriceb30, maxPriceb30.doubleValue());
        map.put(incpriceb30, incPirceb30.doubleValue());

        return map;
    }


}
