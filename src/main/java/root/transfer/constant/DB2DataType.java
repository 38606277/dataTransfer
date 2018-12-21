package root.transfer.constant;

public enum DB2DataType {
    //整数型
    INT("int"),
    BIGINT("bigint"),
    INTEGER("integer"),
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

    private DB2DataType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public static DB2DataType forName(String typeName) {
        DB2DataType dataType = null;
        for (DB2DataType type : values()) {
            if (type.getTypeName().equals(typeName)) {
                dataType = type;
            }
        }
        return dataType;
    }
}
