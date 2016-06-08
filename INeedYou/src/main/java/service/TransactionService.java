package service;

import java.util.Date;
import java.util.List;

import domain.TransactionRecord;

public interface TransactionService {

	/**
	 * 查询明细.
	 * 
	 * @param date
	 * @return
	 */
	public List<TransactionRecord> getDetail(Date date);

	/**
	 * 查询平均值.
	 * 
	 * @param date
	 * @return
	 */
	public Double getAvgPriceRecord(Date date);

	/**
	 * 查询最大值.
	 * 
	 * @param date
	 * @return
	 */
	public TransactionRecord getMaxPriceRecord(Date date);

	/**
	 * 查询当前成交额.
	 * 
	 * @param date
	 * @return
	 */
	public double getVolume(Date date);

	/**
	 * 查询当前数量总数.
	 * 
	 * @param date
	 * @return
	 */
	public double getAmountSum(Date date);
}
