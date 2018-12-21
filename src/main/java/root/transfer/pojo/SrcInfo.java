//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2018.01.23 时间 02:05:29 PM CST 
//


package root.transfer.pojo;

import javax.xml.bind.annotation.*;


/**
 * <p>anonymous complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.pccw.com}dbName"/>
 *         &lt;group ref="{http://www.pccw.com}srcOptionGroup"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "dbName",
        "table",
        "sql",
        "params"
})
@XmlRootElement(name = "srcInfo")
public class SrcInfo {

    @XmlElement(required = true)
    protected String dbName;
    protected String table;
    protected String sql;
    protected String params;

    /**
     * 获取dbName属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * 设置dbName属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDbName(String value) {
        this.dbName = value;
    }

    /**
     * 获取table属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getTable() {
        return table;
    }

    /**
     * 设置table属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTable(String value) {
        this.table = value;
    }

    /**
     * 获取sql属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getSql() {
        return sql;
    }

    /**
     * 设置sql属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSql(String value) {
        this.sql = value;
    }

    /**
     * 获取params属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getParams() {
        return params;
    }

    /**
     * 设置params属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setParams(String value) {
        this.params = value;
    }

}
