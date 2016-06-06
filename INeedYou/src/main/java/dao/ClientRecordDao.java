package dao;

import java.util.List;

import domain.ClientRecord;

public interface ClientRecordDao {

	/** 插入委托记录. */
	public int insert(ClientRecord clientRecord);

	/** 批量插入委托记录. */
	public void insertBatch(List<ClientRecord> list);

}
