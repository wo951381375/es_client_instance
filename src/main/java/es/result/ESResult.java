package es.result;

import java.io.Serializable;

public class ESResult<T> implements Serializable {

    private static final long serialVersionUID = 2090772820018579144L;

    private boolean retBool;
    private Long total;
    private String message;
    private int code;
    private T t;
    private String other;

    public ESResult() {
        super();
    }

    public ESResult(boolean retBool, Long total) {
        super();
        this.retBool = retBool;
        this.total = total;
    }

    public ESResult(boolean retBool, String message) {
        super();
        this.retBool = retBool;
        this.message = message;
    }

    public ESResult(boolean retBool, T t) {
        super();
        this.retBool = retBool;
        this.t = t;
    }

    public ESResult(boolean retBool, T t, Long total) {
        super();
        this.retBool = retBool;
        this.t = t;
        this.total = total;
    }

    public ESResult(boolean retBool, String message, int code) {
        super();
        this.retBool = retBool;
        this.message = message;
        this.code = code;
    }

    public ESResult(boolean retBool, String message, T t) {
        super();
        this.retBool = retBool;
        this.message = message;
        this.t = t;
    }

    public ESResult(boolean retBool, Long total, T t) {
        super();
        this.retBool = retBool;
        this.total = total;
        this.t = t;
    }

    public ESResult<T> format(boolean retBool, String message, T t) {
        this.retBool = retBool;
        this.message = message;
        this.t = t;
        return this;
    }

    public ESResult<T> format(boolean retBool, String message) {
        this.retBool = retBool;
        this.message = message;
        return this;
    }

    public ESResult<T> format(boolean retBool, String message, T t, int code) {
        this.retBool = retBool;
        this.message = message;
        this.t = t;
        this.code = code;
        return this;
    }

    public static <T> ESResult<T> newResult() {
        return new ESResult<T>();
    }

    public boolean isRetBool() {
        return retBool;
    }

    public void setRetBool(boolean retBool) {
        this.retBool = retBool;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ResultVO [retBool=" + retBool + ", total=" + total
                + ", code=" + code + ", message=" + message + "]";
    }
}
