package me.zhaotb.jvm.slack;


import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.awt.AWTException;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.io.Closeable;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhaotangbo
 * @date 2021/1/18
 */
@Slf4j
public class AutoMove implements Closeable {

    public static void main(String[] args) throws Throwable {
        AutoMove autoMove = new AutoMove();
        autoMove.start();

    }

    @ToString
    class Coor {
        int x;
        int y;

        Coor(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    class Speed {
        /**
         * 设定，0为向右，1/2 * π 向下
         * 0 ~ 2π
         */
        double angle;
        /**
         * 速度：单位/毫秒
         */
        double value;

        Speed(double angle, double value) {
            this.angle = angle;
            this.value = value;
        }
    }

    /**
     * 设定左上角为0, 0
     * 右下角为maxX, maxY
     */
    private int maxX;
    private int maxY;

    private ThreadPoolExecutor executor;
    private Random random;
    private Queue<Coor> queue;
    private Robot robot;
    private Coor currentCoor;
    private Speed currentSpeed;

    /**
     * 当前目的坐标
     */
    private Coor targetCoor;
    /**
     * <p>
     * 基础加速度(单位/毫秒^2)，加速度方向为当坐标指向目的坐标。加速度方向永远指向 {@link #targetCoor}
     * <p>
     * 实际加速度还受计算频率影响
     */
    private int acceleration;

    public AutoMove() {
    }

    public void start() throws AWTException {
        init();

        executor.execute(new QuitListener(this::close));

        //100毫秒一次
        final int frequency = 10;
        while (!executor.isShutdown()) {
            if (queue.size() < 2) {
                queue.offer(randomTarget());
            }
            if (isAround()) {
                targetCoor = queue.poll();
                System.out.println(targetCoor);
            }
            computeSpeed();
            move();
            sleep(frequency);
        }
    }

    private void init() throws AWTException {
        random = new Random();
        queue = new LinkedBlockingQueue<>(100);
        executor = new ThreadPoolExecutor(3, 3,
                30, TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
                new ThreadFactory() {
                    AtomicInteger count = new AtomicInteger();

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "执行器-" + count.incrementAndGet());
                    }
                });
        robot = new Robot();
        int[] bounds = getBounds();
        this.maxX = bounds[0];
        this.maxY = bounds[1];
        this.currentCoor = new Coor(maxX / 2, maxY / 2);
        this.currentSpeed = new Speed(0, 0);
        this.acceleration = 1;
    }

    private boolean isAround() {
        if (targetCoor == null) {
            return true;
        }

        return Math.pow(Math.abs(targetCoor.x - currentCoor.x), 2)
                + Math.pow(Math.abs(targetCoor.y - currentCoor.y), 2) <= 2;
    }

    /**
     * @return 屏幕宽高
     */
    private int[] getBounds() {
        GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = genv.getScreenDevices();
        for (GraphicsDevice device : devices) {
            GraphicsConfiguration[] conf = device.getConfigurations();
            for (GraphicsConfiguration c : conf) {
                Rectangle bounds = c.getBounds();
                System.out.println(bounds.width);
                System.out.println(bounds.height);
                return new int[]{bounds.width, bounds.height};
            }
        }

        return new int[]{1, 1};
    }

    /**
     * 随机目的地
     *
     * @return 返回坐标
     */
    private Coor randomTarget() {
        return new Coor(random.nextInt(maxX), random.nextInt(maxY));
    }

    /**
     * 根据加速度、目的地，重新计算速度
     */
    private void computeSpeed() {
        Coor currentCoor = this.currentCoor;
        Coor targetCoor = this.targetCoor;

        double dx = targetCoor.x - currentCoor.x;
        double dy = targetCoor.y - currentCoor.y;

        Speed speed = this.currentSpeed;
        //加速度后的速度值 this.acceleration * 1， 省略了 * 1
        int vnValue = this.acceleration;

        if (targetCoor.x > currentCoor.x && targetCoor.y > currentCoor.y) {
            //坐标方向第一象限
            //角度
            double coorAngle = Math.atan(dy / dx);

            if (speed.angle >= 0 && speed.angle < Math.PI / 2) {
                //速度在第一象限
                //原速度分解成水平速度和垂直速度
                double srcHor = speed.value * Math.cos(speed.angle);
                double srcVer = speed.value * Math.sin(speed.angle);

                //新速度分解成水平速度和垂直速度
                double newHor = vnValue * Math.cos(coorAngle);
                double newVer = vnValue * Math.sin(coorAngle);

                double newSpeed = Math.sqrt(Math.pow(srcHor + newHor, 2) + Math.pow(srcVer + newVer, 2));
                double newAngle = Math.atan((srcVer + newVer) / (srcHor + newHor));
                this.currentSpeed = new Speed(newAngle, newSpeed);
            }
            //TODO 速度在其他象限
        } else if (targetCoor.x < currentCoor.x && targetCoor.y > currentCoor.y) {
            //坐标方向第二象限
            //角度 涉及公式： atan(-x) = -atan(x)
            double coorAngle = Math.PI + Math.atan(dy / dx);

            if (speed.angle >= 0 && speed.angle < Math.PI / 2) {
                //速度在第一象限
                //原速度分解成水平速度和垂直速度
                double srcHor = speed.value * Math.cos(speed.angle);
                double srcVer = speed.value * Math.sin(speed.angle);

                //新速度分解成水平速度和垂直速度
                double newHor = vnValue * Math.cos(coorAngle);
                double newVer = vnValue * Math.sin(coorAngle);

                double newSpeed = Math.sqrt(Math.pow(srcHor + newHor, 2) + Math.pow(srcVer + newVer, 2));
                double newAngle = Math.atan((srcVer + newVer) / (srcHor + newHor));
                if (srcHor < newHor) {
                    newAngle += Math.PI;
                }
                this.currentSpeed = new Speed(newAngle, newSpeed);

            }

        }
        //TODO 坐标在其他象限

    }

    /**
     * 移动一次
     */
    private void move() {
        Speed speed = this.currentSpeed;
        Coor coor = this.currentCoor;

        double x = Math.cos(speed.angle) * speed.value;
        double y = Math.sin(speed.angle) * speed.value;
        int expectX = coor.x + (x > 0 ? (int) Math.ceil(x) : (int) Math.floor(x));
        int expectY = coor.y + (y > 0 ? (int) Math.ceil(y) : (int) Math.floor(y));
        if (expectX > maxX) {
            expectX = maxX;
        }
        if (expectY > maxY) {
            expectY = maxY;
        }
        if (expectX < 0) {
            expectX = 0;
        }
        if (expectY < 0) {
            expectY = 0;
        }
        int tx = coor.x;
        int ty = coor.y;
        while (expectX > tx || expectY > ty) {
            sleep(1);
            if (expectX > tx) {
                tx = tx + 1;
            }
            if (expectY > ty) {
                ty = ty + 1;
            }
            robot.mouseMove(tx, ty);
            this.currentCoor = new Coor(tx, ty);
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.error("中断休眠: {}", millis, e);
        }
    }

    @Override
    public void close() {
        executor.shutdown();
    }
}
