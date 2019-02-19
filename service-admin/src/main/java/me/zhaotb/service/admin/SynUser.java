package me.zhaotb.service.admin;

/**
 * @author zhaotangbo
 * @date 2018/12/17
 */
public class SynUser {

    private String name = "init name";
    private String pwd = "init pwd";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public synchronized void print(String name, String pwd){
        this.name = name;
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        this.pwd = pwd;
    }
}
