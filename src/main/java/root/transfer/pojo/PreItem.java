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

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "dbName",
        "sql"
})
@XmlRootElement(name = "preItem")
public class PreItem {

    @XmlElement(required = true)
    protected String dbName;
    @XmlElement(required = true)
    protected List<String> sql;

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

    public void setSql(List<String> sql) {
        this.sql = sql;
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
