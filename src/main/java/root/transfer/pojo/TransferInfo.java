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
 *         &lt;element ref="{http://www.pccw.com}srcInfo"/>
 *         &lt;element ref="{http://www.pccw.com}targetInfo"/>
 *       &lt;/sequence>
 *       &lt;attribute name="backup" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "srcInfo",
        "targetInfo"
})
@XmlRootElement(name = "transferInfo")
public class TransferInfo {

    @XmlElement(required = true)
    protected SrcInfo srcInfo;
    @XmlElement(required = true)
    protected TargetInfo targetInfo;
    @XmlAttribute(name = "backup")
    protected Boolean backup;

    @XmlAttribute(name = "createtable")
    protected Boolean createtable=false;

    /**
     * 获取srcInfo属性的值。
     *
     * @return possible object is
     * {@link SrcInfo }
     */
    public SrcInfo getSrcInfo() {
        return srcInfo;
    }

    /**
     * 设置srcInfo属性的值。
     *
     * @param value allowed object is
     *              {@link SrcInfo }
     */
    public void setSrcInfo(SrcInfo value) {
        this.srcInfo = value;
    }

    /**
     * 获取targetInfo属性的值。
     *
     * @return possible object is
     * {@link TargetInfo }
     */
    public TargetInfo getTargetInfo() {
        return targetInfo;
    }

    /**
     * 设置targetInfo属性的值。
     *
     * @param value allowed object is
     *              {@link TargetInfo }
     */
    public void setTargetInfo(TargetInfo value) {
        this.targetInfo = value;
    }

    /**
     * 获取backup属性的值。
     *
     * @return possible object is
     * {@link Boolean }
     */
    public boolean isBackup() {
        if (backup == null) {
            return false;
        } else {
            return backup;
        }
    }

    /**
     * 设置backup属性的值。
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setBackup(Boolean value) {
        this.backup = value;
    }


    /**
     * createTable。
     *
     * @return possible object is
     * {@link Boolean }
     */
    public boolean isCreatetable() {
        if (createtable == null) {
            return false;
        } else {
            return createtable;
        }
    }

    /**
     * createTable。
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setCreatetable(Boolean value) {
        this.createtable = value;
    }
}
