package root.etl.exception;

/**
 * @Auther: pccw
 * @Date: 2018/11/27 15:22
 * @Description: 自定义 异常
 */
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BizException(String msg) {
        super(msg);
    }
}
