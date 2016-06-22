package analysis.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import analysis.BaseDataAnalysis;
import domain.TransactionRecord;

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
 * <p>
 * <p>
 * Created by huangming on 2016/6/17.
 */
public class VolumeDataAnalysis extends BaseDataAnalysis {

	/** 缓存20秒的数据作为分析依据. */
	private final static int MAX_SIZE = 60;

	/** 以下是各个指标. **/

	/** 当前最小价格. **/
	private final static int COUNTER_PRICE_MIN = 1;
	/** 当前最大价格. **/
	private final static int COUNTER_PRICE_MAX = 2;
	/** 当前均价. **/
	private final static int COUNTER_PRICE_AVG = 3;
	/** 价位蛇形折线差合计. **/
	private final static int COUNTER_PRICE_INCREASE = 4;
	/** 正价位蛇形折线差合计. **/
	private final static int COUNTER_PRICE_PINCREASE = 5;

	/** 成交次数. */
	private final static int COUNTER_VOLUMN_TIME = 6;
	/** 成交量总净额. */
	private final static int COUNTER_VOLUMN_MONEY = 7;
	/** 成交总数量. */
	private final static int COUNTER_VOLUMN_AMOUNT = 8;
	/** 成交量蛇形折线差合计. */
	private final static int COUNTER_VOLUMN_INCREASE = 9;
	/** 成交量正蛇形折线差合计. */
	private final static int COUNTER_VOLUMN_PINCREASE = 10;

	/** 当前价格方差. */
	private final static int COUNTER_OTHER_VARIANCE = 11;

	private final static int COUNTER_MAXINDEX = 11;

	/** 交易记录存储. */
	private LinkedList<TransactionRecord>[] tranList = new LinkedList[MAX_SIZE];
	/** 分析记录存储. */
	// private ArrayList<double[][]> analysisArr = new
	// ArrayList<double[][]>(MAX_SIZE);

	// private Map<Integer, double[][]> counterMap = new HashMap<>();

	/** 时间对应索引. */
	private long[] timeForCounter = new long[MAX_SIZE];

	private long lastupdatetime = 0;
	private int start_index = MAX_SIZE - 1;
	private int end_index = 0;

	// private LinkedHashMap<Long, Double> timeAndPrice = new LinkedHashMap<>();
	// private LinkedHashMap<Long, LinkedList<Thread>> threadRegister = new
	// LinkedHashMap<>();

	private long lastVoiceTime = 0;

	public VolumeDataAnalysis(String type) {
		super(type);
		analysis();
	}

	@Override
	public double getPrice(Date date) {
		return 0;
	}

	@Override
	protected void save(TransactionRecord t) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MILLISECOND, 0);
		long curtime = calendar.getTimeInMillis();
		/**
		 * 预提醒.
		 */
		if (t.getPrice() <= 75) {
			if (lastVoiceTime == 0 || lastVoiceTime + 2000 < System.currentTimeMillis()) {
				/*
				 * try { Runtime.getRuntime().exec(
				 * "wscript D:\\workspace\\codespace\\IdeaProject\\gupiao\\INeedYou\\src\\main\\resources\\down75.vbs"
				 * ); } catch (IOException e) { e.printStackTrace();
				 * Toolkit.getDefaultToolkit().beep(); }
				 */
				lastVoiceTime = curtime;
			}
		}

		if (t.getPrice() > 85) {
			// Toolkit.getDefaultToolkit().beep();
			if (lastVoiceTime == 0 || lastVoiceTime + 2000 < System.currentTimeMillis()) {
				/*
				 * try { Runtime.getRuntime().exec(
				 * "wscript D:\\workspace\\codespace\\IdeaProject\\gupiao\\INeedYou\\src\\main\\resources\\up85.vbs"
				 * ); } catch (IOException e) { e.printStackTrace();
				 * Toolkit.getDefaultToolkit().beep(); }
				 */
				lastVoiceTime = curtime;
			}
		}

		/**
		 * 每秒数据归集，但有上限.
		 */
		if (lastupdatetime == 0) {
			lastupdatetime = curtime;
		} else {
			if (curtime != lastupdatetime) {
				// 切换到下一秒.
				lastupdatetime = curtime;
				start_index++;
				start_index = start_index % MAX_SIZE;
				end_index++;
				end_index = end_index % MAX_SIZE;

				LinkedList<TransactionRecord> list = tranList[start_index];
				if (list != null) {
					list.clear();
				}
				timeForCounter[start_index] = curtime;
			}
		}

		if (tranList[start_index] == null) {
			tranList[start_index] = new LinkedList<>();
		}
		tranList[start_index].add(t);

	}

	public void analysis() {
		/** 启动定时分析. */
		new Timer().schedule(new VolumeAnalysis(), 0, 1000);
		/** 启动定时决策. */
		// TODO
	}

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
	 * 分析器.
	 */
	private class VolumeAnalysis extends TimerTask {

		@Override
		public void run() {
			// 分析器，记录本身分析时间.
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.MILLISECOND, 0);
			long curTime = calendar.getTimeInMillis();
			System.out.print(String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":"
					+ String.format("%02d", calendar.get(Calendar.MINUTE)) + ":"
					+ String.format("%02d", calendar.get(Calendar.SECOND)));

			/** 记录时间范围内对应的坐标. */
			/** 当前秒坐标，存在并发. */
			int start_index = VolumeDataAnalysis.this.start_index;
			/** 当截止，存在并发. */
			int end_index = VolumeDataAnalysis.this.end_index;
			/** 浅拷贝，防止数据被修改. */
			LinkedList<TransactionRecord>[] tranList2 = (LinkedList<TransactionRecord>[]) tranList.clone();

			/** 数据分析存储地. double[类型][秒] */
			double[][] counter = new double[MAX_SIZE][COUNTER_MAXINDEX];

			/** 汇总指标统计. */
			/** 全汇总. */
			double[] total = new double[MAX_SIZE];
			/** 30秒内汇总. */
			double[] total30 = new double[MAX_SIZE];
			/** 5秒内汇总. */
			double[] total5 = new double[MAX_SIZE];

			/**
			 * 当前秒段数据指标提取.
			 */
			for (int bi = start_index + MAX_SIZE, totalIndex = 0; bi > end_index; bi--, totalIndex++) {
				int i = bi % MAX_SIZE;
				LinkedList<TransactionRecord> list = tranList2[i];

				double lastPrice = 0;
				double lastVolumn = 0;
				if (list != null) {
					for (TransactionRecord transactionRecord : list) {
						double price = transactionRecord.getPrice();
						double volumn = transactionRecord.getAmount() * transactionRecord.getPrice();

						if (price > counter[i][COUNTER_PRICE_MAX]) {
							counter[i][COUNTER_PRICE_MAX] = price;
						}
						if (price < counter[i][COUNTER_PRICE_MIN]) {
							counter[i][COUNTER_PRICE_MIN] = 0;
						}
						counter[i][COUNTER_VOLUMN_AMOUNT] += transactionRecord.getAmount();
						counter[i][COUNTER_VOLUMN_MONEY] += volumn;
						counter[i][COUNTER_VOLUMN_TIME]++;

						if (lastPrice == 0 || lastVolumn == 0) {
							counter[i][COUNTER_VOLUMN_INCREASE] = 0;
							counter[i][COUNTER_PRICE_INCREASE] = 0;
							counter[i][COUNTER_PRICE_PINCREASE] = 0;
							counter[i][COUNTER_VOLUMN_PINCREASE] = 0;
						} else {
							counter[i][COUNTER_VOLUMN_INCREASE] += price - lastPrice;
							counter[i][COUNTER_VOLUMN_PINCREASE] = price - lastPrice > 0 ? price - lastPrice : 0;
							counter[i][COUNTER_PRICE_INCREASE] += volumn - lastPrice;
							counter[i][COUNTER_PRICE_PINCREASE] = volumn - lastPrice > 0 ? volumn - lastPrice : 0;
						}
						lastPrice = price;
						lastVolumn = volumn;
					}
					for (int j = 0; j < counter[i].length; j++) {
						total[j] += counter[i][j];
						if (i < 30) {
							total30[j] += counter[i][j];
						}
						if (i < 5) {
							total5[j] += counter[i][j];
						}
					}
					counter[i][COUNTER_PRICE_AVG] = counter[i][COUNTER_VOLUMN_MONEY]
							/ counter[i][COUNTER_VOLUMN_AMOUNT];
					i++;
				}
			}

			/** 求当前方差. */
			// TODO

			/** 三十秒内差值比较. */
			double[] compare30 = compare(counter[start_index], counter[(start_index + MAX_SIZE - 30) % MAX_SIZE]);
			/** 五秒内差值比较. */
			double[] compare5 = compare(counter[start_index], counter[(start_index + MAX_SIZE - 5) % MAX_SIZE]);

			System.out.print("\t当前最高价:" + counter[start_index][COUNTER_PRICE_MAX]);
			if (compare30 != null) {
				System.out.print("\t30秒内正在");
				if (compare30[COUNTER_PRICE_MAX] > 0) {
					System.out.print("涨");
				} else {
					System.out.print("跌");
				}
				System.out.print("\t幅度:" + compare30[COUNTER_PRICE_MAX]);
			}
			if (compare5 != null) {
				System.out.print("\t5秒内正在");
				if (compare5[COUNTER_PRICE_MAX] > 0) {
					System.out.print("涨");
				} else {
					System.out.print("跌");
				}
				System.out.print("\t幅度:" + String.format("%.2f", compare5[COUNTER_PRICE_MAX]));
			}
			System.out.println();

		}

	}

}
