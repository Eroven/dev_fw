package me.zhaotb.service.admin;

/**
 * @author zhaotangbo
 * @date 2018/12/17
 */
public class TestStop {

    public static void main(String[] args) throws InterruptedException {
        final SynUser user = new SynUser();
        Thread thread = new Thread(() -> user.print("ja", "123"));
        Thread thread2 = new Thread(() -> user.print("j22a", "666"));

        thread.start();
        thread2.start();
        Thread.sleep(100);
        thread.interrupt();
        Thread.State state = thread.getState();
        boolean interrupted = thread.isInterrupted();
        boolean interrupted1 = thread.isInterrupted();

        Thread deamon = new Thread(() -> {

        });
        deamon.setDaemon(true);
        deamon.start();

        System.out.println(user.getName() + " " + user.getPwd());

    }
}
