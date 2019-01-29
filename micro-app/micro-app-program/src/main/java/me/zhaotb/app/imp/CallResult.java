package me.zhaotb.app.imp;

/**
 * @author zhaotangbo
 * @date 2018/12/18
 */
public class CallResult {

    private boolean success;

    private String message;

    private String key;

    public CallResult() {
    }

    public CallResult(boolean success, String key) {
        this.success = success;
        this.key = key;
    }

    public CallResult(boolean success, String message, String key) {
        this.success = success;
        this.message = message;
        this.key = key;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "CallResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
