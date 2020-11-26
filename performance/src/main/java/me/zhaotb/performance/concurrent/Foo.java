package me.zhaotb.performance.concurrent;


public class Foo {
    private volatile int val = 0;

    public Foo() {

    }

    public void first(Runnable printFirst) throws InterruptedException {
        while (true) {
            if (val % 3 == 0) {
                // printFirst.run() outputs "first". Do not change or remove this line.
                printFirst.run();
                val++;
                break;
            }
        }


    }

    public void second(Runnable printSecond) throws InterruptedException {
        while (true) {
            if (val % 3 == 1) {
                // printSecond.run() outputs "second". Do not change or remove this line.
                printSecond.run();
                val++;
                break;
            }
        }
    }

    public void third(Runnable printThird) throws InterruptedException {
        while (true) {
            if (val % 3 == 2) {
                // printThird.run() outputs "third". Do not change or remove this line.
                printThird.run();
                val++;
                break;
            }
        }

    }
}