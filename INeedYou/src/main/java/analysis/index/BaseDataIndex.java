package analysis.index;

import domain.TransactionRecord;
import util.CircleArray;

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

    @Override
    public String getName() {
        return "基础指标库";
    }

    @Override
    public String getDescript() {
        return "一些基本的指标";
    }

    @Override
    public Map<String, BigDecimal> execute(List<TransactionRecord> transactionRecords, CircleArray<Map<String, BigDecimal>> historyIndexData) {
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

        Map<String, BigDecimal> beforeIndexData = historyIndexData.getBefore();
        Map<String, BigDecimal> beforeIndexData5 = historyIndexData.getBefore(5);
        Map<String, BigDecimal> beforeIndexData30 = historyIndexData.getBefore(30);

        if (transactionRecords.size() > 0) {
            // 有记录时进行记录分析.
            for (TransactionRecord transactionRecord : transactionRecords) {
                BigDecimal price = new BigDecimal(transactionRecord.getPrice());
                BigDecimal amount = new BigDecimal(transactionRecord.getAmount());

                volumnMoney = volumnMoney.add(price.subtract(amount));
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
            if (beforeIndexData != null) {
                BigDecimal lastPrice = beforeIndexData.get(maxprice);
                BigDecimal lastVolumn = beforeIndexData.get(volumnmoney);
                // inc
                incPirce = maxPrice.subtract(lastPrice);
                pincPrice = maxPrice.subtract(lastPrice).abs();
                // pinc
                incVolumn = volumnMoney.subtract(lastVolumn);
                pincVolumn = volumnMoney.subtract(lastVolumn).abs();
            }
            if (beforeIndexData5 != null) {
                maxPriceb5 = beforeIndexData5.get(maxprice);
                incPirceb5 = maxPrice.subtract(maxPriceb5);
            }
            if (beforeIndexData30 != null) {
                maxPriceb30 = beforeIndexData30.get(maxprice);
                incPirceb30 = maxPrice.subtract(maxPriceb30);
            }
        } else {
            // 无记录时赋予默认值.
            if (beforeIndexData != null) {
                BigDecimal lastMaxPrice = beforeIndexData.get(maxprice);
                BigDecimal lastMinPrice = beforeIndexData.get(minprice);
                if (maxPrice == null) {
                    maxPrice = lastMaxPrice;
                } else {
                    maxPrice = maxPrice.max(lastMaxPrice);
                }
                if (minPrice == null) {
                    minPrice = lastMinPrice;
                } else {
                    minPrice = minPrice.min(lastMinPrice);
                }
            }
            if (beforeIndexData5 != null) {
                maxPriceb5 = beforeIndexData5.get(maxprice);
                incPirceb5 = maxPrice.subtract(maxPriceb5);
            }
            if (beforeIndexData30 != null) {
                maxPriceb30 = beforeIndexData30.get(maxprice);
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

        HashMap<String, BigDecimal> map = new HashMap<>();
        map.put(maxprice, maxPrice);
        map.put(minprice, minPrice);
        map.put(incprice, incPirce);
        map.put(pincprice, pincPrice);
        map.put(volumntimes, volumnTime);
        map.put(volumnamount, volumnAmount);
        map.put(volumnmoney, volumnMoney);
        map.put(incvolumnmoney, incVolumn);
        map.put(pincvolumnmoney, pincVolumn);
        map.put(maxpriceb5, maxPriceb5);
        map.put(incpriceb5, incPirceb5);
        map.put(maxpriceb30, maxPriceb30);
        map.put(incpriceb30, incPirceb30);

        return map;
    }


}
