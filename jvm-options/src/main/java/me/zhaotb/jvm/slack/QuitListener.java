package me.zhaotb.jvm.slack;


import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author zhaotangbo
 * @date 2021/1/18
 */
class QuitListener implements Runnable {

    private char[] quitSign = {'Q', 'U', 'I', 'T'};

    private CallBack callBack;

    QuitListener(CallBack callBack) {
        this.callBack = callBack;
    }

    @SneakyThrows
    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int read;
        int cacheIdx = 0;
        while ((read = reader.read()) > 0) {
            if (quitSign[cacheIdx] == read) {
                cacheIdx += 1;
            } else {
                cacheIdx = 0;
            }
            if (cacheIdx == quitSign.length) {
                callBack.call();
                break;
            }
        }

    }

    @FunctionalInterface
    public interface CallBack {
        /**
         * 回调方法
         */
        void call();
    }
}
