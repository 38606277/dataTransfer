package root.transfer.constant;

public enum OracleDataType {
    NUMBER("number"),
    VARCHAR2("varchar2"),
    BLOB("blob"),
    CLOB("clob"),
    CHAR("char"),
    TIMESTAMP("timestamp"),
    DATE("date");

    private String typeName;

    private OracleDataType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public static OracleDataType forName(String typeName) {
        OracleDataType dataType = null;
        for (OracleDataType type : values()) {
            if (type.getTypeName().equals(typeName)) {
                dataType = type;
            }
        }
        return dataType;
    }
}
