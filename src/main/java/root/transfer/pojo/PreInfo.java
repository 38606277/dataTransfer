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
        "preItem"
})
@XmlRootElement(name = "preInfo")
public class PreInfo {

    @XmlElement(required = true)
    protected List<PreItem> preItem;

    public void setItem(List<PreItem> item) {
        this.preItem = item;
    }

    public List<PreItem> getItem() {
        if (preItem == null) {
            preItem = new ArrayList<PreItem>();
        }
        return this.preItem;
    }

}
