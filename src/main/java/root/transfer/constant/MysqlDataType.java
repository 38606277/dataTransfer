package root.transfer.constant;

public enum MysqlDataType {
    //整数型
    INT("int"),
    BIGINT("bigint"),
    //小数型
    DOUBLE("double"),
    DECIMAL("decimal"),
    //字符型
    CHAR("char"),
    VARCHAR("varchar"),
    BLOB("blob"),
    //日期型
    DATE("date"),
    DATETIME("datetime"),
    //备注型
    LONGTEXT("longtext"),
    BIT("bit");
    private String typeName;

    private MysqlDataType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public static MysqlDataType forName(String typeName) {
        MysqlDataType dataType = null;
        for (MysqlDataType type : values()) {
            if (type.getTypeName().equals(typeName)) {
                dataType = type;
            }
        }
        return dataType;
    }
}
