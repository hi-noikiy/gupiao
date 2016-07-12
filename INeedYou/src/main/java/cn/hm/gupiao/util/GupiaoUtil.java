package cn.hm.gupiao.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by huangming on 2016/6/28.
 */
public class GupiaoUtil {


    public static double ema(double[] arr, int e) {
        return ema(arr, 0, e);
    }

    public static double ema(double[] arr, int start, int e) {
        // 循环解法.
        double result = 0;
        for (int i = 0; i < e; i++) {
            double v = arr[start + e - i - 1];
            double ed = i + 1;
            double f = 2.0 / (ed + 1.0);
            double p = (ed - 1.0) / (ed + 1.0);
            result = (f * v + p * result);
        }


        /*
        // 递归解法
        if (e == 1) {
            return arr[start];
        }

        double v = arr[start];
        double ed = e;
        double f = 2.0 / (ed + 1.0);
        double p = (ed - 1.0) / (ed + 1.0);
        return f * v + p * (ema2(arr, start + 1, e - 1));
        */
        return result;
    }

    public static double sma(double[] arr, int start, int e, double m) {
        // 循环解法.
        double result = 0;
        for (int i = 0; i < e; i++) {
            double v = arr[start + e - i - 1];
            double ed = i + 1;
            double f = m / (ed + 1.0);
            double p = (ed - 1.0) / (ed + 1.0);
            result = (f * v + p * result);
        }
        return result;
    }

    public static double dma(double[] arr, int start, double m) {
        // 循环解法.
        double result = 0;
        for (int i = 0; i < arr.length; i++) {
            double v = arr[start + arr.length - i - 1];
            result = (m * v + (1 - m) * result);
        }
        return result;
    }


    public static double macd(double[] arr, int st, int lg, int mid) {
        double ema12 = ema(arr, st);
        double ema26 = ema(arr, lg);
        double dif = ema12 - ema26;
        double[] deaArr = new double[mid + 1];

        deaArr[0] = dif;
        for (int i = 1; i <= mid; i++) {
            double bema12 = ema(arr, i, st);
            double bema26 = ema(arr, i, lg);
            deaArr[i] = bema12 - bema26;
        }
        double dea = ema(deaArr, mid);
        double macd = 2 * (dif - dea);
        System.out.println(String.format("dif:%.2f\t\tdea:%.2f\t\tmacd:%.2f\t\tem7:%.2f\t\tema12:%.2f\t\tema26:%.2f\t\tema30:%.2f", dif, dea, macd, ema(arr, 7), ema12, ema26, ema(arr, 30)));
        return macd;
    }

    public static void rsi(double[] arr, int d) {
        double tsum = 0;
        double fsum = 0;
        double lastprice = arr[0];
        for (int i = 1; i <= d; i++) {
            sma(arr, 0, i, 1);
            double c = lastprice - arr[i];
            if (c > 0) {
                tsum += c;
            } else {
                fsum += c;
            }
        }
        double rsi = tsum / (tsum - fsum) * 100;
        System.out.print(String.format("RSI(%d):%.2f\t", d, rsi));
    }

    public static void kdj(double[] arr, int d) {

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
        dif = diffList.get(0);
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

        double[] priceHistory3 = new double[]{28.07, 28.10, 28.09, 28.09, 28.10};
        System.out.println(ema(priceHistory, 5));

        double xxx = ema(new double[]{1, 2, 3, 4}, 4);
        System.out.println(xxx);

        // double sum = 1.0 / 3.0 * 28.22 + 4.0 / 15.0 * 28.11 + 1.0 / 5.0 * 28.10 + 2.0 / 15.0 * 28.11 + 1.0 / 15.0 * 28.06;
        double sum = 1.0 / 3.0 * 28.22 + 4.0 / 15.0 * 28.22 + 1.0 / 5.0 * 28.22 + 2.0 / 15.0 * 28.11 + 1.0 / 15.0 * 28.10;
        System.out.println(sum);

        double xxx1 = ema(new double[]{1, 2, 3, 4}, 4);
        double pp = ema(new double[]{28.22, 28.22, 28.22, 28.11, 28.10, 28.11, 28.06}, 0, 5);
        System.out.print(pp);
    }
}
