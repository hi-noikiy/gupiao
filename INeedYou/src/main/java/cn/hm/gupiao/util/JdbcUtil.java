package cn.hm.gupiao.util;

import cn.hm.gupiao.config.PrivateConfig;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.ArrayList;

/**
 * Jdbc工具类，内部实现了一个固定连接池.
 *
 * @author huangming
 */
@Deprecated
public class JdbcUtil {

    /**
     * 连接池固定长度.
     */
    private static int CONN_SIZE = 5;
    /**
     * 连接池队列.
     */
    private static ArrayList<Connection> conArr = null;
    private static int cur = CONN_SIZE - 1;

    static {
        /**
         * 加载类驱动.
         */
        try {
            Class.forName(PrivateConfig.driverClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        conArr = new ArrayList<Connection>(CONN_SIZE);
        for (int i = 0; i < CONN_SIZE; i++) {
            try {
                conArr.add(JdbcConnHandler.getInstance(DriverManager.getConnection(PrivateConfig.url,
                        PrivateConfig.username, PrivateConfig.password)));
            } catch (SQLException e) {
                throw new RuntimeException("当前数据库无法连接！", e);
            }
        }
    }

    /**
     * 获取链接.
     *
     * @return
     */
    public static Connection open() {
        synchronized (conArr) {
            while (cur < 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            cur--;
            return conArr.get(cur);
        }
    }

    /**
     * 链接回收.
     *
     * @param conn
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void close(Connection conn, Statement stat) {
        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void close(Connection conn, Statement stat, ResultSet set) {
        if (set != null) {
            try {
                set.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Connection 代理类.
     *
     * @author huangming
     */
    static class JdbcConnHandler implements InvocationHandler {
        private Connection conn;

        public JdbcConnHandler(Connection conn) {
            this.conn = conn;
        }

        @Override
        public Object invoke(Object arg0, Method arg1, Object[] arg2) throws Throwable {
            if ("close".equals(arg1.getName())) {
                cur++;
                return null;
            } else {
                return arg1.invoke(conn, arg2);
            }
        }

        public static Connection getInstance(Connection conn) {
            return (Connection) Proxy.newProxyInstance(JdbcConnHandler.class.getClassLoader(),
                    new Class[]{Connection.class}, new JdbcConnHandler(conn));
        }
    }

}
