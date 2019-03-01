package me.zhaotb.framework.util.windows;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author zhaotangbo
 * @date 2019/2/28
 */
public class WinCtrl {

    public static void main(String[] args) throws ParseException, InterruptedException, AWTException {
        DelayQueue<Msg> tasks = new DelayQueue<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tasks.add(new Msg(format.parse("2019-2-28 15:24:00"), "aaaa"));

        Msg task = tasks.take();

        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_Z);
        robot.keyPress(KeyEvent.VK_H);
        robot.keyPress(KeyEvent.VK_A);
        robot.keyPress(KeyEvent.VK_O);
        robot.keyPress(KeyEvent.VK_T);
        robot.keyPress(KeyEvent.VK_A);
        robot.keyPress(KeyEvent.VK_N);
        robot.keyPress(KeyEvent.VK_G);
        robot.keyPress(KeyEvent.VK_B);
        robot.keyPress(KeyEvent.VK_O);
        robot.keyPress(KeyEvent.VK_SPACE);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyPress(KeyEvent.VK_Z);
        robot.keyPress(KeyEvent.VK_H);
        robot.keyPress(KeyEvent.VK_A);
        robot.keyPress(KeyEvent.VK_O);
        robot.keyPress(KeyEvent.VK_T);
        robot.keyPress(KeyEvent.VK_A);
        robot.keyPress(KeyEvent.VK_N);
        robot.keyPress(KeyEvent.VK_G);
        robot.keyPress(KeyEvent.VK_B);
        robot.keyPress(KeyEvent.VK_O);
        robot.keyPress(KeyEvent.VK_SPACE);
        System.out.println("C");



//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    instance.SetForegroundWindow(hwnd);
//                }
//            }
//        });
//        thread.start();


//
//
//        instance.PostMessage(hwnd, 123, null, null);
//
//        WinUser.INPUT input = new WinUser.INPUT();
//
//        char[] chars = task.msg.toCharArray();
//
//        input.input.setType("hi");
//
//        for (char ch : chars) {
//            input.input.hi.uMsg = new WinDef.DWORD();
//        }
//
//
//        for (char ch : chars) {
//            input.type = new WinDef.DWORD(WinUser.INPUT.INPUT_KEYBOARD);
//
//            input.input.setType("ki");
//            input.input.ki.wScan = new WinDef.WORD( 0 );
//            input.input.ki.time = new WinDef.DWORD( 0 );
//            input.input.ki.dwExtraInfo = new BaseTSD.ULONG_PTR( 0 );
//            // Press
//            input.input.ki.wVk = new WinDef.WORD( Character.toUpperCase(ch) ); // 0x41
//            input.input.ki.dwFlags = new WinDef.DWORD( 0 );  // keydown
//
//            instance.SendInput( new WinDef.DWORD( 1 ), ( WinUser.INPUT[] ) input.toArray( 1 ), input.size() );
//
//            // Release
//            input.input.ki.wVk = new WinDef.WORD( Character.toUpperCase(ch) ); // 0x41
//            input.input.ki.dwFlags = new WinDef.DWORD( 2 );  // keyup
//
//            instance.SendInput( new WinDef.DWORD( 1 ), ( WinUser.INPUT[] ) input.toArray( 1 ), input.size() );
//        }


    }

    public static class Msg implements Delayed {
        private Date sendDate;
        private String msg;

        public Msg(Date sendDate, String msg) {
            this.sendDate = sendDate;
            this.msg = msg;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(sendDate.getTime() - new Date().getTime(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(getDelay(TimeUnit.MILLISECONDS), o.getDelay(TimeUnit.MILLISECONDS));
        }
    }

}
