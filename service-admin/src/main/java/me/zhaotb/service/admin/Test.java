package me.zhaotb.service.admin;

import java.util.HashMap;
import java.util.Scanner;

/**
 * @author zhaotangbo
 * @date 2018/12/17
 */
public class Test {

    public static void main(String[] args) {

        Object obj = new Object();
        Scanner scanner = new Scanner(System.in);
        HashMap<String, Thread> tasks = new HashMap<>();
        int num = 0;
        while (scanner.hasNext()){
            String input = scanner.nextLine();
            if ("START".equalsIgnoreCase(input)){
                num ++;
                String adminName = "service-admin-" + num;
                String taskName = "service-task-" + num;
                System.out.println("开始任务: " + adminName);

                ThreadGroup group = new ThreadGroup("service-group-" + num);
                Thread serviceAdmin = new Thread(group, () -> {
                    Thread serviceTask = new Thread(group, () -> {
                        while(true) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("hello");
                        }
                    }, taskName);
                    System.out.println("daemon: " + serviceTask.isDaemon());
                    serviceTask.start();

                    synchronized (obj){
                        try {
                            System.out.println("等待");
                            obj.wait();
                            System.out.println("结束等待");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    serviceTask.interrupt();
                    boolean interrupted = serviceTask.isInterrupted();
                    boolean interrupted1 = Thread.interrupted();
                }, adminName);
                serviceAdmin.setDaemon(true);
                serviceAdmin.start();
                tasks.put(adminName, serviceAdmin);
                serviceAdmin.stop();
            }else if ("END".equalsIgnoreCase(input)){
                synchronized (obj){
                    obj.notifyAll();
                }
            }else if ("STOP".equalsIgnoreCase(input)){
                break;
            }else
                System.out.println("unknown input: " + input);


        }

    }

}
