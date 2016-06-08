package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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

	@Override
	public List<TransactionRecord> selectDetail(Date date) {
		List<TransactionRecord> list = new ArrayList<TransactionRecord>(5);
		Connection conn = JdbcUtil.open();
		PreparedStatement ps = null;
		ResultSet reset = null;
		try {
			ps = conn.prepareStatement(
					"select id, direction,amount,price,op_time,pal_type,good_type from transaction_record where op_time=?");
			ps.setObject(1, new Timestamp(date.getTime()));
			reset = ps.executeQuery();
			while (reset.next()) {
				TransactionRecord record = new TransactionRecord();
				record.setAmount(reset.getDouble("amount"));
				record.setDirection(reset.getString("direction"));
				record.setGoodType(reset.getString("good_type"));
				record.setId(reset.getInt("id"));
				record.setOpTime(reset.getDate("op_time"));
				record.setPalType(reset.getString("pal_type"));
				record.setPrice(reset.getDouble("price"));
				list.add(record);
			}
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	@Override
	public double selectAvgPriceRecord(Date date) {
		Connection conn = JdbcUtil.open();
		PreparedStatement ps = null;
		ResultSet reset = null;
		try {
			ps = conn.prepareStatement("select avg(price) avgprice from transaction_record where op_time=?");
			ps.setObject(1, new Timestamp(date.getTime()));
			reset = ps.executeQuery();
			if (reset.next()) {
				return reset.getDouble("avgprice");
			}
			return 0;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	@Override
	public TransactionRecord selectMaxPriceRecord(Date date) {
		Connection conn = JdbcUtil.open();
		PreparedStatement ps = null;
		ResultSet reset = null;
		try {
			ps = conn.prepareStatement(
					"select id, direction,amount,price,op_time,pal_type,good_type from transaction_record where op_time=?");
			ps.setObject(1, new Timestamp(date.getTime()));
			reset = ps.executeQuery();
			if (reset.next()) {
				TransactionRecord record = new TransactionRecord();
				record.setAmount(reset.getDouble("amount"));
				record.setDirection(reset.getString("direction"));
				record.setGoodType(reset.getString("good_type"));
				record.setId(reset.getInt("id"));
				record.setOpTime(reset.getDate("op_time"));
				record.setPalType(reset.getString("pal_type"));
				record.setPrice(reset.getDouble("price"));
				return record;
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	@Override
	public double selectVolume(Date date) {
		Connection conn = JdbcUtil.open();
		PreparedStatement ps = null;
		ResultSet reset = null;
		try {
			ps = conn.prepareStatement("select sum(price*amount) volume from transaction_record where op_time=?");
			ps.setObject(1, new Timestamp(date.getTime()));
			reset = ps.executeQuery();
			if (reset.next()) {
				return reset.getDouble("volume");
			}
			return 0;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	@Override
	public double selectAmountSum(Date date) {
		Connection conn = JdbcUtil.open();
		PreparedStatement ps = null;
		ResultSet reset = null;
		try {
			ps = conn.prepareStatement("select sum(amount) amountsum from transaction_record where op_time=?");
			ps.setObject(1, new Timestamp(date.getTime()));
			reset = ps.executeQuery();
			if (reset.next()) {
				return reset.getDouble("amountsum");
			}
			return 0;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

}
