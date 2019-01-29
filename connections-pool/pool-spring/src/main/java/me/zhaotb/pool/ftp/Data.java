package me.zhaotb.pool.ftp;

public class Data {

    public static int i = 0;

    public void init(){
        System.out.println("init" + i++);
    }

    public void close(){
        System.out.println("close");
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("finalize");
    }
}
