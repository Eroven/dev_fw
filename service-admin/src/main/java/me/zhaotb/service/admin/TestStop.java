package me.zhaotb.service.admin;

/**
 * @author zhaotangbo
 * @date 2018/12/17
 */
public class TestStop {

    public static void main(String[] args) throws InterruptedException {
        final SynUser user = new SynUser();
//        Thread thread = new Thread(() -> user.print("ja", "123"));
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("A");
//                Thread.currentThread().interrupt();
//                try {
//                    Thread.sleep(50000);
//                } catch (InterruptedException e) {
//                    boolean interrupted = Thread.currentThread().isInterrupted();
////                    Thread.currentThread().interrupt();
//                    boolean interrupted3 = Thread.currentThread().isInterrupted();
//                    try {
//                        Thread.sleep(50000);
//                    } catch (InterruptedException e1) {
//                        boolean interrupted1 = Thread.currentThread().isInterrupted();
//                        System.out.println(interrupted1);
//                    }
//                    e.printStackTrace();
//                }
//            }
//        });
        Thread thread = new Thread(){
            @Override
            public void run() {
                System.out.println("RUN");
                interrupt();
                boolean interrupted = isInterrupted();
                System.out.println(interrupted);
            }
        };
//        Thread thread2 = new Thread(() -> user.print("j22a", "666"));

        thread.start();
//        thread2.start();
        Thread.sleep(100);
//        thread.interrupt();
        Thread.State state = thread.getState();
        boolean interrupted = thread.isInterrupted();
        boolean interrupted1 = thread.isInterrupted();
        boolean interrupted2 = thread.isInterrupted();

        boolean interrupted3 = thread.isInterrupted();
        thread.interrupt();
        boolean interrupted4 = thread.isInterrupted();

        Thread daemon = new Thread(() -> {

        });
        daemon.setDaemon(true);
        daemon.start();

        System.out.println(user.getName() + " " + user.getPwd());

    }
}
