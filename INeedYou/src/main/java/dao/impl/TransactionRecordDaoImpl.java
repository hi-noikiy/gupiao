package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import dao.BaseDao;
import dao.TransactionRecordDao;
import domain.TransactionRecord;
import util.JdbcUtil;

public class TransactionRecordDaoImpl extends BaseDao implements TransactionRecordDao {

	@Override
	public int insert(TransactionRecord record) {
		Connection conn = JdbcUtil.open();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(
					"insert into transaction_record(direction,amount,price,op_time,pal_type,good_type) value(?,?,?,?,?,?)");
			ps.setObject(1, record.getDirection());
			ps.setObject(2, record.getAmount());
			ps.setObject(3, record.getPrice());
			ps.setObject(4, new Timestamp(record.getOpTime().getTime()));
			ps.setObject(5, record.getPalType());
			ps.setObject(6, record.getGoodType());
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn, ps);
		}
	}

	@Override
	public void insertBatch(List<TransactionRecord> list) {
		Connection conn = JdbcUtil.open();
		try {
			insertMoreValue(conn, "insert into transaction_record(direction,amount,price,op_time,pal_type,good_type)",
					(ps, startIndex, record) -> {
						ps.setObject(startIndex, record.getDirection());
						ps.setObject(startIndex + 1, record.getAmount());
						ps.setObject(startIndex + 2, record.getPrice());
						ps.setObject(startIndex + 3, new Timestamp(record.getOpTime().getTime()));
						ps.setObject(startIndex + 4, record.getPalType());
						ps.setObject(startIndex + 5, record.getGoodType());
					} , list);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

}
