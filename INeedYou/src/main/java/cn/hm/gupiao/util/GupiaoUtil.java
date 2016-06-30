package cn.hm.gupiao.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by huangming on 2016/6/28.
 */
public class GupiaoUtil {

    private double[] ema = new double[100];
    private double[] dea = new double[100];


    public static double ema(double[] arr, int e) {
        return ema(arr, 0, e);
    }

    public static double ema(double[] arr, int start, int e) {
        if (e == 1) {
            return arr[start];
        }
        return (2 * arr[e - 1 + start] + (e - 1) * ema(arr, start, e - 1)) / (e + 1);
    }

    public static double macd(double[] arr, int st, int lg, int mid) {
        double ema12 = ema(arr, st);
        double ema24 = ema(arr, lg);
        double dif = ema12 - ema24;
        double[] deaArr = new double[mid + 1];

        deaArr[0] = dif;
        for (int i = 1; i >= mid; i++) {
            double bema12 = ema(arr, i, st);
            double bema24 = ema(arr, i, lg);
            deaArr[i] = bema12 - bema24;
        }
        double dea = ema(deaArr, mid);
        System.out.print("dif:" + dif + "\t dea:" + dea + "\t macd:");
        return 2 * (dif - dea);
    }

    /**
     * Calculate EMA,
     *
     * @param list :Price list to calculate，the first at head, the last at tail.
     * @return
     */
    public static final Double getEXPMA(final List<Double> list, final int number) {
        // 开始计算EMA值，
        Double k = 2.0 / (number + 1.0);// 计算出序数
        Double ema = list.get(0);// 第一天ema等于当天收盘价
        for (int i = 1; i < list.size(); i++) {
            // 第二天以后，当天收盘 收盘价乘以系数再加上昨天EMA乘以系数-1
            ema = list.get(i) * k + ema * (1 - k);
        }
        return ema;
    }

    /**
     * calculate MACD values
     *
     * @param list        :Price list to calculate，the first at head, the last at tail.
     * @param shortPeriod :the short period value.
     * @param longPeriod  :the long period value.
     * @param midPeriod   :the mid period value.
     * @return
     */
    public static final HashMap<String, Double> getMACD(final List<Double> list, final int shortPeriod, final int longPeriod, int midPeriod) {
        HashMap<String, Double> macdData = new HashMap<String, Double>();
        List<Double> diffList = new ArrayList<Double>();
        Double shortEMA = 0.0;
        Double longEMA = 0.0;
        Double dif = 0.0;
        Double dea = 0.0;

        for (int i = list.size() - 1; i >= 0; i--) {
            List<Double> sublist = list.subList(0, list.size() - i);
            shortEMA = getEXPMA(sublist, shortPeriod);
            longEMA = getEXPMA(sublist, longPeriod);
            dif = shortEMA - longEMA;
            diffList.add(dif);
        }
        dea = getEXPMA(diffList, midPeriod);
        macdData.put("DIF", dif);
        macdData.put("DEA", dea);
        macdData.put("MACD", (dif - dea) * 2);
        return macdData;
    }


    public static void main(String[] args) {
        double[] priceHistory = new double[]{27.83, 27.84, 27.84, 27.86, 27.84, 27.83, 27.83, 27.82, 27.84, 27.82, 27.82, 27.80, 27.77, 27.78, 27.79, 27.77, 27.77, 27.77, 27.81, 27.77, 27.77, 27.74, 27.71, 27.78, 27.80, 27.80, 28.84, 27.86, 27.89, 27.88, 27.89, 27.81, 27.81, 27.77, 27.80, 27.77, 27.77, 27.77, 27.78, 27.75, 27.79, 27.76, 27.75, 27.75, 27.75, 27.74, 27.73, 27.76, 27.80, 27.83};
        double a = macd(priceHistory, 12, 26, 9);
        System.out.println(a);

        Double[] priceHistory2 = new Double[]{27.83, 27.84, 27.84, 27.86, 27.84, 27.83, 27.83, 27.82, 27.84, 27.82, 27.82, 27.80, 27.77, 27.78, 27.79, 27.77, 27.77, 27.77, 27.81, 27.77, 27.77, 27.74, 27.71, 27.78, 27.80, 27.80, 28.84, 27.86, 27.89, 27.88, 27.89, 27.81, 27.81, 27.77, 27.80, 27.77, 27.77, 27.77, 27.78, 27.75, 27.79, 27.76, 27.75, 27.75, 27.75, 27.74, 27.73, 27.76, 27.80, 27.83};
        HashMap<String, Double> macd = getMACD(Arrays.asList(priceHistory2), 12, 26, 9);
        System.out.println(macd);

        double xx = getEXPMA(Arrays.asList(new Double[]{Double.valueOf(1), Double.valueOf(2), Double.valueOf(3), Double.valueOf(4)}), 4);
        System.out.println(xx);

        double xxx = ema(new double[]{1, 2, 3, 4}, 4);
        System.out.println(xxx);
    }
}
