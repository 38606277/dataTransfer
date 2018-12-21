package root.configuration;

import org.apache.ibatis.type.*;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: pccw
 * @Date: 2018/12/13 14:25
 * @Description:
 */
@MappedJdbcTypes({JdbcType.TIMESTAMP})
@MappedTypes({Date.class})   // 哪个JAVA类型能被拦截
public class DateTypeHandle extends BaseTypeHandler<Date>{

    // 重新定义要往数据库写的 数据
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Date parameter, JdbcType jdbcType) throws SQLException {
        // 转为 如此格式再设置到当中去
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ps.setTimestamp(i,new Timestamp(parameter.getTime()));
    }

    // 原样返回  TODO ： 后续可能要更改为 指定格式的返回
    @Override
    public Date getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getDate(columnName);
    }

    @Override
    public Date getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getDate(columnIndex);
    }

    @Override
    public Date getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getDate(columnIndex);
    }
}
