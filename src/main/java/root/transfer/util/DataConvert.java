package root.transfer.util;

import oracle.sql.CLOB;
import oracle.sql.TIMESTAMP;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.sql.SQLException;

public class DataConvert {
    /**
     * 把各数据库特有的值类型转化为通用java类型
     *
     * @param obj
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public static Object getVal(Object obj) throws SQLException, IOException {
        if (obj == null) return null;
        if (obj instanceof TIMESTAMP) {
            return ((TIMESTAMP) obj).timestampValue();
        } else if (obj instanceof CLOB) {
            return IOUtils.toString(((CLOB) obj).getCharacterStream());
        }
        return obj;
    }
}
