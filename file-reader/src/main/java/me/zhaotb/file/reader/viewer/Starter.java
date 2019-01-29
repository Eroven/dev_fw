package me.zhaotb.file.reader.viewer;

import java.io.IOException;

public class Starter {

    public static void main(String[] args) throws InterruptedException, IOException {
        MainView mainView = new MainView("file-reader", "F:\\data", "reader.txt");

        mainView.readFrame();

    }
}
