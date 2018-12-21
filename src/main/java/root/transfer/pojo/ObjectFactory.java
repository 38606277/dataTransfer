//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2018.01.23 时间 02:05:29 PM CST 
//


package root.transfer.pojo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.pccw.pojo package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Params_QNAME = new QName("http://www.pccw.com", "params");
    private final static QName _Sql_QNAME = new QName("http://www.pccw.com", "sql");
    private final static QName _Table_QNAME = new QName("http://www.pccw.com", "table");
    private final static QName _Engine_QNAME = new QName("http://www.pccw.com", "engine");
    private final static QName _DbName_QNAME = new QName("http://www.pccw.com", "dbName");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.pccw.pojo
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Item }
     */
    public Item createItem() {
        return new Item();
    }

    /**
     * Create an instance of {@link TargetInfo }
     */
    public TargetInfo createTargetInfo() {
        return new TargetInfo();
    }

    /**
     * Create an instance of {@link CallBackInfo }
     */
    public CallBackInfo createCallBackInfo() {
        return new CallBackInfo();
    }

    /**
     * Create an instance of {@link Root }
     */
    public Root createRoot() {
        return new Root();
    }

    /**
     * Create an instance of {@link TransferInfo }
     */
    public TransferInfo createTransferInfo() {
        return new TransferInfo();
    }

    /**
     * Create an instance of {@link SrcInfo }
     */
    public SrcInfo createSrcInfo() {
        return new SrcInfo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.pccw.com", name = "params")
    public JAXBElement<String> createParams(String value) {
        return new JAXBElement<String>(_Params_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.pccw.com", name = "sql")
    public JAXBElement<String> createSql(String value) {
        return new JAXBElement<String>(_Sql_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.pccw.com", name = "table")
    public JAXBElement<String> createTable(String value) {
        return new JAXBElement<String>(_Table_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.pccw.com", name = "engine")
    public JAXBElement<String> createEngine(String value) {
        return new JAXBElement<String>(_Engine_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.pccw.com", name = "dbName")
    public JAXBElement<String> createDbName(String value) {
        return new JAXBElement<String>(_DbName_QNAME, String.class, null, value);
    }

}
