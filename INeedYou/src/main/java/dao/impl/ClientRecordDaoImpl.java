package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import dao.ClientRecordDao;
import domain.ClientRecord;
import util.JdbcUtil;

public class ClientRecordDaoImpl implements ClientRecordDao {

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

	/**
	 * 批量快速插入.
	 * 
	 * @param clientRecords
	 * @return
	 */
	public int insertSuperTen(List<ClientRecord> clientRecords) {
		StringBuffer sb = new StringBuffer(
				"insert into client_record(client,pal_type,direction,amount,price,op_time,good_type) values");
		for (int i = 0; i < clientRecords.size(); i++) {
			sb.append("(?,?,?,?,?,?,?),");
		}
		sb.replace(sb.length()-1, sb.length(), ";");

		Connection conn = JdbcUtil.open();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sb.toString());
			int index = 0;
			for (int i = 0; i < clientRecords.size(); i++) {
				ClientRecord clientRecord = clientRecords.get(i);
				ps.setObject(index + 1, clientRecord.getClient());
				ps.setObject(index + 2, clientRecord.getPalType());
				ps.setObject(index + 3, clientRecord.getDirection());
				ps.setObject(index + 4, clientRecord.getAmount());
				ps.setObject(index + 5, clientRecord.getPrice());
				ps.setObject(index + 6, new Timestamp(clientRecord.getOpTime().getTime()));
				ps.setObject(index + 7, clientRecord.getGoodType());
				index = index + 7;
			}
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn, ps);
		}
	}

}
