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

    synchronized public void print(String name, String pwd){
        this.name = name;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.pwd = pwd;
    }
}
