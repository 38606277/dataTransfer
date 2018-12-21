package root.configuration;

import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

/**
 * @Auther: pccw
 * @Date: 2018/12/11 12:12
 * @Description:
 *  解密 对pwd  解密以达到 能够正常连接到数据库  (********  这里是唯一的没被解耦出去的，若平常项目引用
 *      此包，则需要考虑到此处的 【加密、解密】过程 *******)
 */
public class ErpUtil {

    private static final byte[] DESkey = "11111111".getBytes();// 设置密钥

    private static final byte[] DESIV = "12345678".getBytes();// 设置向量
    private static Key key = null;

    static AlgorithmParameterSpec iv = null;// 设置向量;// 加密算法的参数接口，IvParameterSpec是它的一个实现


    public ErpUtil() {
        try {
            DESKeySpec keySpec = new DESKeySpec(DESkey);// 设置密钥参数

            iv = new IvParameterSpec(DESIV);// 设置向量

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 获得密钥工厂

            key = keyFactory.generateSecret(keySpec);// 得到密钥对象
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String decode(String data) throws Exception {

        Cipher deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        deCipher.init(Cipher.DECRYPT_MODE, key, iv);

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] buffer = decoder.decode(data);

        return new String(deCipher.doFinal(buffer), "UTF-8");
    }


    public String encode(String data) throws Exception {

        Cipher enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");// 得到加密对象Cipher

        enCipher.init(Cipher.ENCRYPT_MODE, key, iv);// 设置工作模式为加密模式，给出密钥和向量

        byte[] pasByte = enCipher.doFinal(data.getBytes("utf-8"));

        BASE64Encoder base64Encoder = new BASE64Encoder();

        return base64Encoder.encode(pasByte);

    }
}
