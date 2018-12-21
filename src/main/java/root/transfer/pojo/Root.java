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
 *         &lt;element ref="{http://www.pccw.com}transferInfo" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.pccw.com}callBackInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "preInfo",
        "transferInfo",
        "callBackInfo"
})
@XmlRootElement(name = "root")
public class Root {

    @XmlElement(required = true)
    protected List<TransferInfo> transferInfo;
    protected CallBackInfo callBackInfo;

    protected PreInfo preInfo;

    public PreInfo getPreInfo() {
        return preInfo;
    }

    public void setPreInfo(PreInfo preInfo) {
        this.preInfo = preInfo;
    }



    public void setTransferInfo(List<TransferInfo> transferInfo) {
        this.transferInfo = transferInfo;
    }

    /**
     * Gets the value of the transferInfo property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the transferInfo property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *
     *    getTransferInfo().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TransferInfo }
     */
    public List<TransferInfo> getTransferInfo() {
        if (transferInfo == null) {
            transferInfo = new ArrayList<TransferInfo>();
        }
        return this.transferInfo;
    }

    /**
     * 获取callBackInfo属性的值。
     *
     * @return possible object is
     * {@link CallBackInfo }
     */
    public CallBackInfo getCallBackInfo() {
        return callBackInfo;
    }

    /**
     * 设置callBackInfo属性的值。
     *
     * @param value allowed object is
     *              {@link CallBackInfo }
     */
    public void setCallBackInfo(CallBackInfo value) {
        this.callBackInfo = value;
    }

}
