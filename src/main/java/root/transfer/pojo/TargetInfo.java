//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2018.01.23 时间 02:05:29 PM CST 
//


package root.transfer.pojo;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


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
 *         &lt;element ref="{http://www.pccw.com}table"/>
 *         &lt;element ref="{http://www.pccw.com}engine" minOccurs="0"/>
 *         &lt;element ref="{http://www.pccw.com}sql" maxOccurs="unbounded" minOccurs="0"/>
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
        "engine",
        "sql"
})
@XmlRootElement(name = "targetInfo")
public class TargetInfo {

    @XmlElement(required = true)
    protected String dbName;
    @XmlElement(required = true)
    protected String table;
    protected String engine;
    protected List<String> sql;

    public void setSql(List<String> sql) {
        this.sql = sql;
    }

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
     * 获取engine属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getEngine() {
        return engine;
    }

    /**
     * 设置engine属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEngine(String value) {
        this.engine = value;
    }

    /**
     * Gets the value of the sql property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sql property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSql().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getSql() {
        if (sql == null) {
            sql = new ArrayList<String>();
        }
        return this.sql;
    }

}
