package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import dao.BaseDao;
import dao.ClientRecordDao;
import domain.ClientRecord;
import util.JdbcUtil;

public class ClientRecordDaoImpl extends BaseDao implements ClientRecordDao {

	@Override
	public int insert(ClientRecord clientRecord) {
		Connection conn = JdbcUtil.open();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(
					"insert into client_record(client,pal_type,direction,amount,price,op_time,good_type) value(?,?,?,?,?,?,?)");
			ps.setObject(1, clientRecord.getClient());
			ps.setObject(2, clientRecord.getPalType());
			ps.setObject(3, clientRecord.getDirection());
			ps.setObject(4, clientRecord.getAmount());
			ps.setObject(5, clientRecord.getPrice());
			ps.setObject(6, new Timestamp(clientRecord.getOpTime().getTime()));
			ps.setObject(7, clientRecord.getGoodType());
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn, ps);
		}
	}

	@Override
	public void insertBatch(List<ClientRecord> list) {
		Connection conn = JdbcUtil.open();
		try {
			insertMoreValue(conn, "insert into client_record(client,pal_type,direction,amount,price,op_time,good_type)",
					(ps, startIndex, record) -> {
						ps.setObject(startIndex + 0, record.getClient());
						ps.setObject(startIndex + 1, record.getPalType());
						ps.setObject(startIndex + 2, record.getDirection());
						ps.setObject(startIndex + 3, record.getAmount());
						ps.setObject(startIndex + 4, record.getPrice());
						ps.setObject(startIndex + 5, new Timestamp(record.getOpTime().getTime()));
						ps.setObject(startIndex + 6, record.getGoodType());
					} , list);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

}
