//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.3.0 生成的
// 请访问 <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2018.01.17 时间 03:28:59 PM CST 
//


package root.transfer.pojo;

import javax.xml.bind.annotation.*;


/**
 * <p>anonymous complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;group ref="{}srcSqlGroup"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "sql",
        "params"
})
@XmlRootElement(name = "execute")
public class Execute {

    @XmlElement(required = true)
    protected String sql;
    protected String params;

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
