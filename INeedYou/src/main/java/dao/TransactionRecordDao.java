package dao;

import java.util.List;

import domain.TransactionRecord;

public interface TransactionRecordDao {
	
	/** 插入交易记录. */
	public int insert(TransactionRecord record);

	/** 批量插入交易记录. */
	public void insertBatch(List<TransactionRecord> list);

}
