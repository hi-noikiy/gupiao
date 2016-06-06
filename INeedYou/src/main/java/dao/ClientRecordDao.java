package dao;

import domain.ClientRecord;

public interface ClientRecordDao {

	/** 插入委托记录. */
	public int insert(ClientRecord clientRecord);

}
