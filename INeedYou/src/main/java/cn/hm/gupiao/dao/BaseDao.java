package cn.hm.gupiao.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import cn.hm.gupiao.config.PrivateConfig;
import cn.hm.gupiao.util.JdbcUtil;

public class BaseDao {

	/**
	 * 批量插入升级方法，专门针对几十条乃至上百条数据批量插入时迅速提高效率的办法.
	 * 
	 * @param sql_prefix
	 *            SQL前缀，如:insert into
	 *            transaction_record(direct,amount,price,op_time,pal_type,
	 *            good_type)。
	 * @param paramCount
	 *            参数数量，照上面的例子，参数数量是6.
	 * @param m
	 *            预处理参数对应接口.
	 * @param list
	 *            批处理List对象.
	 * @throws SQLException
	 */
	protected static <T> void insertMoreValue(Connection conn, String sql_prefix, ParameterCorrespondence<T> m,
			List<T> list) throws SQLException {
		if (sql_prefix == null || list == null || list.size() == 0) {
			return;
		}

		/** 自动分析参数个数. */
		int paramCount = 1;
		for (int i = 0; i < sql_prefix.length(); i++) {
			if (sql_prefix.charAt(i) == ',') {
				paramCount++;
			}
		}

		/** 构造预处理参数串. */
		StringBuffer params = new StringBuffer();
		params.append("(?");
		for (int i = 2; i <= paramCount; i++) {
			params.append(",?");
		}
		params.append(")");
		String paramBuffer = params.toString();

		/** 计算构造语句需要执行几次. */
		int runCount = list.size() / PrivateConfig.BASEDAO_CACHE_SIZE;
		/** 计算最后剩下的记录数. */
		int otherCount = list.size() % PrivateConfig.BASEDAO_CACHE_SIZE;

		/** 构造完整预处理语句. */
		StringBuffer sqlSb = new StringBuffer(sql_prefix);
		sqlSb.append(" values").append(paramBuffer);
		for (int i = 1; i < PrivateConfig.BASEDAO_CACHE_SIZE; i++) {
			sqlSb.append(",").append(paramBuffer);
		}
		sqlSb.append(";");

		/** 构造剩余参数语句. */
		StringBuffer sqlSb2 = new StringBuffer(sql_prefix);
		sqlSb2.append(" values").append(paramBuffer);
		for (int i = 1; i < otherCount; i++) {
			sqlSb2.append(",").append(paramBuffer);
		}
		sqlSb2.append(";");

		/** 执行语句. */
		PreparedStatement ps = null;
		/** 批量语句处理. */
		try {
			ps = conn.prepareStatement(sqlSb.toString());
			/** 构造一个insert语句,这里处理预处理参数. */
			for (int i = 0, run = 0, index = 1, cur = 1; i < list.size() && run < runCount; i++, cur++) {
				T record = list.get(i);
				m.make(ps, index, record);
				index = index + paramCount;
				if (PrivateConfig.BASEDAO_CACHE_SIZE == cur) {
					ps.addBatch();
					ps.clearParameters();
					cur = 0;
					index = 1;
					run++;
				}
			}
			ps.executeBatch();
		} finally {
			JdbcUtil.close(null, ps);
		}
		/** 剩余参数处理. */
		if (otherCount > 0) {
			try {
				ps = conn.prepareStatement(sqlSb2.toString());
				for (int i = list.size() - otherCount, index = 1; i < list.size(); i++) {
					T record = list.get(i);
					m.make(ps, index, record);
					index = index + paramCount;
				}
				ps.executeUpdate();
			} finally {
				JdbcUtil.close(null, ps);
			}
		}

	}

	/**
	 * 用于预处理参数对应的接口.
	 * 
	 * @author huangming
	 *
	 * @param <T>
	 */
	protected interface ParameterCorrespondence<T> {
		/**
		 * 
		 * @param ps
		 *            预处理对象.
		 * @param startIndex
		 *            参数开始的下标.
		 * @param record
		 *            对象.
		 * @return
		 * @throws SQLException
		 */
		public void make(PreparedStatement ps, int startIndex, T record) throws SQLException;
	}
}
