package cn.hm.gupiao.util;

/**
 * Created by huangming on 2016/6/28.
 */
public class GupiaoUtil {

    public double ema(double[] arr, int e) {
        double a = 1 / e;
        double total = 0;
        for (int i = 0; i < e; i++) {
            total += Math.pow((1 - a), i) * arr[i];
        }
        total *= a;
        return total;
    }

    public double macd(double[] arr, int st, int lg, int mid) {
        double ema12 = ema(arr, 12);
        double ema24 = ema(arr, 24);
        double diff = ema12 - ema24;
        double dea = diff / 9;
        return 0;
    }


    public static void main(String[] args) {

    }
}
