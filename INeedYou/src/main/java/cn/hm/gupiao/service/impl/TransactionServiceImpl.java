package cn.hm.gupiao.service.impl;

import java.util.Date;
import java.util.List;

import cn.hm.gupiao.dao.TransactionRecordDao;
import cn.hm.gupiao.dao.impl.TransactionRecordDaoImpl;
import cn.hm.gupiao.domain.TransactionRecord;
import cn.hm.gupiao.service.TransactionService;

public class TransactionServiceImpl implements TransactionService {

	private TransactionRecordDao dao = new TransactionRecordDaoImpl();

	@Override
	public List<TransactionRecord> getDetail(Date date) {
		return dao.selectDetail(date);
	}

	@Override
	public Double getAvgPriceRecord(Date date) {
		return dao.selectAvgPriceRecord(date);
	}

	@Override
	public TransactionRecord getMaxPriceRecord(Date date) {
		return dao.selectMaxPriceRecord(date);
	}

	@Override
	public double getVolume(Date date) {
		return dao.selectVolume(date);
	}

	@Override
	public double getAmountSum(Date date) {
		return dao.selectAmountSum(date);
	}

}
