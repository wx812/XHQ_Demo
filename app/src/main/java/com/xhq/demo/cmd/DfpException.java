package com.xhq.demo.cmd;

/**
 * Created by Akmm at 14-1-6 上午10:30
 * 业务异常，此类异常将报告给前端
 */
public class DfpException extends Exception{
    private int error = 0;//其他错误，未知

    public DfpException() {
        super();
    }

    public DfpException(int error) {
        super();
        this.error = error;
    }

    public DfpException(int error, String message) {
        super(message);
        this.error = error;
    }

    public DfpException(String message) {
        super(message);
    }

    public DfpException(String message, Throwable cause) {
        super(message, cause);
    }

    public DfpException(Throwable cause) {
        super(cause);
    }

/*    protected DfpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }*/

    public int getError() {
        return error;
    }
}
