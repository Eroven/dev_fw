package me.zhaotb.pool.ftp;

import java.util.Objects;

public class FtpInfo {

    private String ip;
    private int port = 21;
    private String user = "anonymous";
    private String password;

    //控制台编码方式
    private String ctlEncode = "utf-8";

    /**
     * 本地是否启动主动模式,默认被动模式
     */
    private boolean activeMode = false;

    public FtpInfo() {
    }

    public FtpInfo(String ip, int port, String user, String password) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCtlEncode() {
        return ctlEncode;
    }

    public void setCtlEncode(String ctlEncode) {
        this.ctlEncode = ctlEncode;
    }

    public boolean isActiveMode() {
        return activeMode;
    }

    public void setActiveMode(boolean activeMode) {
        this.activeMode = activeMode;
    }

    @Override
    public String toString() {
        return user + "@" + ip + ":" + port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FtpInfo)) return false;
        FtpInfo info = (FtpInfo) o;
        return port == info.port &&
                Objects.equals(ip, info.ip) &&
                Objects.equals(user, info.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port, user);
    }
}
