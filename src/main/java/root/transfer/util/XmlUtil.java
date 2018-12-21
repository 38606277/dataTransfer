package root.transfer.util;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;
import root.transfer.pojo.Root;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;

public class XmlUtil {
    // 解析指定路径下xml文件
    public static Document parseXmlToDom(String sqlPath) throws SAXException, DocumentException {
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        return reader.read(new File(sqlPath));
    }

    public static Document parseXmlToDom(File file) throws SAXException, DocumentException {
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        return reader.read(file);
    }

    public static Document parseXmlToDom(URL url) throws SAXException, DocumentException {
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        return reader.read(url);
    }

    public static Document parseXmlToDom(InputStream in) throws SAXException, DocumentException {
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        return reader.read(in);
    }

    /**
     * 解析配置文件生成java对象
     *
     * @param path
     * @return
     * @throws JAXBException
     */
    public static Root pareseXmlToJavaBean(InputStream in) throws JAXBException {
        JAXBContext jaxbC = JAXBContext.newInstance(Root.class);
        Unmarshaller us = jaxbC.createUnmarshaller();
        return (Root) us.unmarshal(in);
	/*	JAXBContext context = JAXBContext.newInstance(Root.class);
		byte[] data = IOUtils.toByteArray(in);
		Unmarshaller m = context.createUnmarshaller();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		Object obj = m.unmarshal(inputStream);
		return (Root) obj;*/
    }

    /**
     * 功能描述:
     * 把 java 对象生成对应的 xml 文件
     *
     * @param:
     * @return:
     * @auther: pccw
     * @date: 2018/12/2 21:30
     */
    public static void transferJavaBeanToXml(Object object, String encoding, File file) throws JAXBException {
        String result = null;

        if (StringUtils.isBlank(encoding)) {
            encoding = "UTF-8";
        }
        JAXBContext jaxbC = JAXBContext.newInstance(object.getClass());
        Marshaller marshaller = jaxbC.createMarshaller();
        // 指定是否使用换行和缩排对已编组 XML 数据进行格式化的属性名称。
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);

        StringWriter writer = new StringWriter();
        marshaller.marshal(object, file);
    }

}
