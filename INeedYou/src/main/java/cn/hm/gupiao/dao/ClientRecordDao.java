package cn.hm.gupiao.dao;

import java.util.List;

import cn.hm.gupiao.domain.ClientRecord;

public interface ClientRecordDao {

	/** 插入委托记录. */
	public int insert(ClientRecord clientRecord);

	/** 批量插入委托记录. */
	public void insertBatch(List<ClientRecord> list);

}
