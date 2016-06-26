package cn.hm.gupiao.dao;

import java.util.Date;
import java.util.List;

import cn.hm.gupiao.domain.TransactionRecord;

public interface TransactionRecordDao {

	/**
	 * 插入交易记录.
	 * 
	 * @param record
	 * @return
	 */
	public int insert(TransactionRecord record);

	/**
	 * 批量插入交易记录.
	 * 
	 * @param list
	 */
	public void insertBatch(List<TransactionRecord> list);

	/**
	 * 查询明细.
	 * 
	 * @param date
	 * @return
	 */
	public List<TransactionRecord> selectDetail(Date date);

	/**
	 * 查询平均值.
	 * 
	 * @param date
	 * @return
	 */
	public double selectAvgPriceRecord(Date date);

	/**
	 * 查询最大值.
	 * 
	 * @param date
	 * @return
	 */
	public TransactionRecord selectMaxPriceRecord(Date date);

	/**
	 * 查询当前成交额.
	 * 
	 * @param date
	 * @return
	 */
	public double selectVolume(Date date);

	/**
	 * 查询当前数量总数.
	 * 
	 * @param date
	 * @return
	 */
	public double selectAmountSum(Date date);

}
