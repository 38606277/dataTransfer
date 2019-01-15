package root.transfer.util;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import root.configuration.ErpUtil;
import root.transfer.constant.DB2DataType;
import root.transfer.constant.MysqlDataType;
import root.transfer.constant.OracleDataType;
import root.transfer.pojo.SrcInfo;
import root.transfer.pojo.TargetInfo;
import root.transfer.pojo.TransferInfo;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.function.Consumer;

public class DbHelper {
    private static final Logger log = Logger.getLogger(DbHelper.class);

    public static Connection getConnection(String dbName) throws SQLException, ClassNotFoundException {
        Map<String, String> map = DbManager.getDBConnectionByName(dbName);
        String driver = map.get("driver");
        String url = map.get("url");
        if (driver.indexOf("mysql") != -1) {
            url += "?serverTimezone=UTC&useSSL=true&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&rewriteBatchedStatements=true";
        }
        String username = map.get("username");
        String password = "";
                ErpUtil erpUtil = new ErpUtil();
        try {
            password = erpUtil.decode(map.get("password"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // String password = map.get("password");   //  连接不上数据库??   可能密码加密了 需要解密下
        Class.forName(driver);

        Connection conn = DriverManager.getConnection(url, username, password);

        return conn;
    }

    public static String getInserSql(TargetInfo target, ResultSetMetaData rsmd) throws SQLException {
        String insertSql = "insert into " + target.getTable() + " (#columns#) values (#placeHolders#)";
        StringBuilder columns = new StringBuilder();
        StringBuilder placeHolders = new StringBuilder();
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            columns.append(rsmd.getColumnLabel(i) + ",");
            placeHolders.append("?,");
        }
        columns.deleteCharAt(columns.lastIndexOf(","));
        placeHolders.deleteCharAt(placeHolders.lastIndexOf(","));
        insertSql = insertSql.replace("#columns#", columns.toString()).replace("#placeHolders#", placeHolders.toString());
        return insertSql;
    }

    public void executeQuery(SrcInfo src, Consumer<ResultSet> consumer) {
        Connection conn = null;
        PreparedStatement stm = null;
        try {
            conn = getConnection(src.getDbName());

            String sql = src.getSql();
            if (sql == null || sql.equals("")) {
                sql = "select * from " + src.getTable();
            }
            // log.debug("=================SQL是");
            // log.debug(sql);
            // log.debug("=================");
            // 实现任意滚动
          //   stm = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stm = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
           // stm.setQueryTimeout(180);    // 设置 3分钟查询超时时间
            stm.setFetchSize(1000);
            stm.setFetchDirection(ResultSet.FETCH_REVERSE);
            if (src.getParams() != null) {
                String[] params = src.getParams().split(",");
                for (int i = 1; i <= params.length; i++) {
                    stm.setObject(i, params[i - 1]);
                }
            }
            ResultSet rs = stm.executeQuery();

            consumer.accept(rs);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            try {
                if (stm != null) stm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                log.error("关闭连接时发生异常", e);
            }
        }
    }

    public List<Map<String, Object>> executeQuery(SrcInfo src) {
        long time = System.currentTimeMillis();
        final List<Map<String, Object>> list = new ArrayList<>();
        this.executeQuery(src, rs -> {
            try {
                if (rs != null) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int cc = rsmd.getColumnCount();
                    while (rs.next()) {
                        Map<String, Object> retMap = new HashMap<>(cc);
                        list.add(retMap);
                        for (int i = 1; i <= cc; i++) {
                            retMap.put(rsmd.getColumnLabel(i), rs.getObject(i));
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("遍历获取数据时发生异常：", e);
            }
        });
        log.info("耗时：" + (System.currentTimeMillis() - time));
        return list;
    }


    // 传递SQL 以及要连接的数据库
    public static List<String> executeQuery(String db_name,String execute_sql) {
        Connection conn = null;
        PreparedStatement stm = null;
        List<String> list = new ArrayList<>();
        try {
            conn = getConnection(db_name);

            String sql = execute_sql;
            if (StringUtils.isBlank(sql)) {
               return null;
            }
            log.debug("=================SQL是");
            log.debug(sql);
            log.debug("=================");
            // 实现任意滚动
            stm = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // stm.setQueryTimeout(180);    // 设置 3分钟查询超时时间
           //  stm.setFetchSize(1000);
            // stm.setFetchDirection(ResultSet.FETCH_REVERSE);
            ResultSet rs = stm.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    list.add(rs.getString("DEPARTMENT_ID"));
                }
            }
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            try {
                if (stm != null) stm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                log.error("关闭连接时发生异常", e);
            }
        }
    }


    /**
     * 建表及执行建表后的sql
     *
     * @param rs
     */
    public static void createTableInDB(ResultSet rs, TransferInfo info,int year) {
        SrcInfo src = info.getSrcInfo();
        TargetInfo target = info.getTargetInfo();
        Connection t_conn = null;
        Statement stm = null;
        try {
            ResultSetMetaData s_meta = rs.getMetaData();
            //查询目的表是否存在
            t_conn = getConnection(target.getDbName());
            DatabaseMetaData t_meta = t_conn.getMetaData();
            String target_url = DbManager.getDBConnectionByName(target.getDbName()).get("url");
            String catalog = target_url.substring(target_url.lastIndexOf("/") + 1);
            ResultSet t_rs = t_meta.getTables(catalog, null, target.getTable().toUpperCase(), new String[]{"TABLE"});
            if (t_rs.next()) {
                //目的表已经存在
                if (info.isBackup()) {
                    String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
                    //需要备份，先执行备份操作
                    stm = t_conn.createStatement();
                    String backupSql = "create table " + target.getTable() + "_bak_" + dateStr + " as select * from " + target.getTable();
                    stm.execute(backupSql);
                }
            } else {
                //获取源及目标库的数据库类型
                String s_dbType = DbManager.getDBConnectionByName(src.getDbName()).get("dbtype");
                String t_dbType = DbManager.getDBConnectionByName(target.getDbName()).get("dbtype");

                int columnCount = s_meta.getColumnCount();
                StringBuilder sb = new StringBuilder();
                sb.append("create table " + target.getTable() + "(");
                for (int i = 1; i < columnCount + 1; i++) {
                    sb.append(s_meta.getColumnLabel(i) + " ");
                    String columnTypeName = getColumnTypeName(s_meta, i, s_dbType, t_dbType);
                    //如果是字符串类型，添加字符串长度
                    if (columnTypeName.toLowerCase().indexOf("char") != -1) {
                        columnTypeName = DbHelper.appendColumnSize(columnTypeName, s_meta.getPrecision(i));
                    }
                    sb.append(columnTypeName);

                    if ("NO".equals(s_meta.isNullable(i))) {
                        sb.append(" not null ");
                    }
					/*if(s_rs instanceof ResultSetImpl && "YES".equals(s_rs.getString("IS_AUTOINCREMENT")) && t_rs instanceof ResultSetImpl ){
						sb.append(" auto_increment primary key ");
					}*/
                    sb.append(",");
                }
                // 增加year  --> 改到SQL 当中增加去
                // sb.append(" year int(4) not null ");
                sb.deleteCharAt(sb.length() - 1);   // 删除最后一个逗号
                sb.append(")");
                if ("mysql".equalsIgnoreCase(t_dbType) && StringUtils.isNotBlank(target.getEngine())) {
                    sb.append("ENGINE=" + target.getEngine() + " DEFAULT CHARSET=utf8");
                }
                log.info("【create】创建表:" + sb.toString());
                stm = t_conn.createStatement();
                stm.execute(sb.toString());
            }
            //添加索引或其他sql
            if (target.getSql() != null && target.getSql().size() > 0) {
                for (String sql : target.getSql()) {
                    log.info("执行sql:" + sql);
                    stm.execute(sql);
                }
            }
        } catch (Exception e) {
            log.error("创建表异常", e);
        } finally {
            try {
                if (stm != null) stm.close();
                if (t_conn != null) t_conn.close();
            } catch (SQLException e) {
                log.error("关闭连接时发生异常");
            }
        }
    }

    private static String getColumnTypeName(ResultSetMetaData sm, int i, String s_dbType, String t_dbType) throws SQLException {
        String typeName = sm.getColumnTypeName(i).toLowerCase();
        int columnSize = sm.getPrecision(i);
        int decimal = sm.getScale(i);
        String columnTypeName = null;
        if (s_dbType.equals("Oracle")) {
            if (t_dbType.equals("Mysql")) {
                switch (OracleDataType.forName(typeName)) {
                    case NUMBER:
                        if (decimal == 0) {
                            if (columnSize != 0) {
                                int size = Integer.valueOf(columnSize);
                                if (size <= 10) {
                                    columnTypeName = MysqlDataType.INT.name();
                                } else {
                                    columnTypeName = MysqlDataType.BIGINT.name();
                                }
                            } else {
                                columnTypeName = MysqlDataType.DOUBLE.name();
                            }
                        } else {
                            columnTypeName = MysqlDataType.DOUBLE.name();
                        }
                        break;
                    case VARCHAR2:
                        columnTypeName = MysqlDataType.VARCHAR.name();
                        break;
                    case BLOB:
                        columnTypeName = MysqlDataType.BLOB.name();
                        break;
                    case CHAR:
                        columnTypeName = MysqlDataType.CHAR.name();
                        break;
                    case DATE:
                        columnTypeName = MysqlDataType.DATETIME.name();
                        break;
                    case TIMESTAMP:
                        columnTypeName = MysqlDataType.DATETIME.name();
                        break;
                    case CLOB:
                        columnTypeName = MysqlDataType.LONGTEXT.name();
                        break;
                }
            } else {
                columnTypeName = OracleDataType.forName(typeName).name();
            }
        } else if (s_dbType.equals("Mysql")) {
            if (t_dbType.equals("Oracle")) {
                switch (MysqlDataType.forName(typeName)) {
                    case INT:
                        columnTypeName = OracleDataType.NUMBER.name();
                        break;
                    case BIGINT:
                        columnTypeName = OracleDataType.NUMBER.name();
                        break;
                    case DOUBLE:
                        columnTypeName = OracleDataType.NUMBER.name();
                        break;
                    case CHAR:
                        columnTypeName = OracleDataType.CHAR.name();
                        break;
                    case VARCHAR:
                        columnTypeName = OracleDataType.VARCHAR2.name();
                        break;
                    case BLOB:
                        columnTypeName = OracleDataType.BLOB.name();
                        break;
                    case LONGTEXT:
                        columnTypeName = OracleDataType.CLOB.name();
                        break;
                    case DATE:
                        columnTypeName = OracleDataType.DATE.name();
                        break;
                    case DATETIME:
                        columnTypeName = OracleDataType.DATE.name();
                        break;
                    case BIT:
                        columnTypeName = OracleDataType.NUMBER.name();
                        break;
                }
            } else {
                columnTypeName = MysqlDataType.forName(typeName).name();
            }
        }else if (s_dbType.equals("DB2")) {
            if (t_dbType.equals("Mysql")) {
                switch (DB2DataType.forName(typeName)) {
                    case INTEGER:
                        columnTypeName = MysqlDataType.INT.name();
                        break;
                    case INT:
                        columnTypeName = MysqlDataType.INT.name();
                        break;
                    case BIGINT:
                        columnTypeName = MysqlDataType.BIGINT.name();
                        break;
                    case DOUBLE:
                        columnTypeName = MysqlDataType.DOUBLE.name();
                        break;
                    case DECIMAL:
                        columnTypeName = MysqlDataType.DECIMAL.name();
                        break;
                    case CHAR:
                        columnTypeName = MysqlDataType.CHAR.name();
                        break;
                    case VARCHAR:
                        columnTypeName = MysqlDataType.VARCHAR.name();
                        break;
                    case BLOB:
                        columnTypeName = MysqlDataType.BLOB.name();
                        break;
                    case DATE:
                        columnTypeName = MysqlDataType.DATE.name();
                        break;
                    case DATETIME:
                        columnTypeName = MysqlDataType.DATE.name();
                        break;
                    case BIT:
                        columnTypeName = MysqlDataType.DECIMAL.name();
                        break;
                }
            } else {
                columnTypeName = MysqlDataType.forName(typeName).name();
            }
        }
        return columnTypeName;
    }

    public static String appendColumnSize(String columnTypeName, int columnSize) {
        return columnTypeName + " (" + columnSize + ")";
    }
}
