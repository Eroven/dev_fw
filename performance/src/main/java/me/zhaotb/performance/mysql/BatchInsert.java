package me.zhaotb.performance.mysql;

import com.mysql.jdbc.MySQLConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author zhaotangbo
 * @date 2018/12/4
 */
@SpringBootApplication
@Component
public class BatchInsert {

    private final JdbcTemplate template;

    @Autowired
    public BatchInsert(JdbcTemplate template) {
        this.template = template;
    }

    @PostConstruct
    public void run() throws SQLException {
        String sql = "insert into acct_item(acct_item_id, custom_item, prod_inst_id, post_id, staff_id, acct_id)" +
                " values(?, ?, ?, ?, ?, ?)";
        StringBuilder sql2 = new StringBuilder("insert into acct_item(acct_item_id, custom_item, prod_inst_id, post_id, staff_id, acct_id) values");
        Object[] args = new Object[]{123L, "00011100011", 1111L, "255", "STQ111", 998L};
        ArrayList<Object[]> params = new ArrayList<>();
        ArrayList<Object> params2 = new ArrayList<>(10000 * 6);
        for (int i = 0; i < 10000; i++) {
            if (i == 0) {
                sql2.append("(?, ?, ?, ?, ?, ?)");
            } else {
                sql2.append(",(?, ?, ?, ?, ?, ?)");
            }
            params2.add(123L);
            params2.add("00011100011");
            params2.add(1111L);
            params2.add("255");
            params2.add("STQ111");
            params2.add(998L);
            params.add(args);
        }

        batchUpdate(sql, params);

//        execute(sql, params);

    }

    private void batchUpdate(String sql, ArrayList<Object[]> args) throws SQLException {
        DataSource dataSource = template.getDataSource();
        if (dataSource == null) {
            throw new RuntimeException("数据源为空");
        }
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = dataSource.getConnection();

            boolean rewriteBatchedStatements = setRewriteBatchedStatements(connection);

            ps = connection.prepareStatement(sql);
            for (Object[] values : args) {
                for (int j = 0; j < values.length; j++) {
                    ps.setObject(j + 1, values[j]);
                }
                ps.addBatch();
            }
            ps.executeBatch();

            recoverRewriteBatchedStatements(connection, rewriteBatchedStatements);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    /**
     * 还原连接的设置
     * @param connection 连接
     * @param rewriteBatchedStatements 旧设置
     * @throws SQLException 如果连接不是mysql链接 且不是包装的mysql连接
     */
    private void recoverRewriteBatchedStatements(Connection connection, boolean rewriteBatchedStatements) throws SQLException {
        if (rewriteBatchedStatements) {
            return;
        }
        if (connection instanceof MySQLConnection) {
            ((MySQLConnection) connection).setRewriteBatchedStatements(false);
        } else {
            if (connection.isWrapperFor(MySQLConnection.class)) {
                MySQLConnection mySQLConnection = connection.unwrap(MySQLConnection.class);
                mySQLConnection.setRewriteBatchedStatements(false);
            }
        }
    }

    /**
     * 将指定连接设置为可支持批量处理
     *
     * @param connection 连接
     * @return 返回设置之前的设置
     * @throws SQLException 如果连接不是mysql链接 且不是包装的mysql连接
     */
    private boolean setRewriteBatchedStatements(Connection connection) throws SQLException {
        boolean rewriteBatchedStatements = false;
        if (connection instanceof MySQLConnection) {
            rewriteBatchedStatements = ((MySQLConnection) connection).getRewriteBatchedStatements();
            if (!rewriteBatchedStatements) {
                ((MySQLConnection) connection).setRewriteBatchedStatements(true);
            }
        } else {
            if (connection.isWrapperFor(MySQLConnection.class)) {
                MySQLConnection mySQLConnection = connection.unwrap(MySQLConnection.class);
                rewriteBatchedStatements = mySQLConnection.getRewriteBatchedStatements();
                if (!rewriteBatchedStatements) {
                    mySQLConnection.setRewriteBatchedStatements(true);
                }
            }
        }
        return rewriteBatchedStatements;
    }

    private void execute(String sql, ArrayList<Object[]> params) {
        long start = System.currentTimeMillis();
        template.batchUpdate(sql, params);
        long end = System.currentTimeMillis();

        System.out.println("耗时:" + (end - start));
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BatchInsert.class, args);
    }
}
