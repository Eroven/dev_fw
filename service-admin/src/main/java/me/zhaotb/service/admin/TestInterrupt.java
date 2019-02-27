package me.zhaotb.service.admin;

/**
 * @author zhaotangbo
 * @date 2019/2/26
 */
public class TestInterrupt {

    private static volatile int val = 0;

    public static void main(String[] args) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                Thread.currentThread().interrupt();
                System.out.println("inner: " + Thread.currentThread().isInterrupted());
                val = 1;

                while (true){
                    if (val == 2){
                        break;
                    }
                }

            }
        });
         thread.start();

        while (true){
            if (val == 1){
                break;
            }
        }

        System.out.println("outer: " + thread.isInterrupted());
        val = 2;

    }
}
