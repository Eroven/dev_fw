package me.zhaotb.file.reader.test;

import me.zhaotb.file.reader.loader.ByteLoader;
import me.zhaotb.file.reader.loader.TextLoader;
import org.junit.Test;

import java.util.Scanner;


public class TestLoader {

    @Test
    public void testByteLoader() throws Exception {
        try (ByteLoader loader = new ByteLoader("f:/data", "reader.txt")){
            byte[] buffer = new byte[1024];
            int load;
            while ((load = loader.load(buffer)) > 0) {
                System.out.println(new String(buffer, 0, load));
            }


        }

    }

    @Test
    public void testTextLoader() throws Exception {
        try(ByteLoader loader = new ByteLoader("f:/data", "reader.txt");
            TextLoader textLoader = new TextLoader(loader, "utf-8")) {
//            loader.setBufferSize(1024);

            Scanner in = new Scanner(System.in);
            System.out.println("input command: ");
            while (in.hasNextInt()) {
                int ch = in.nextInt();
                switch (ch) {
                    case 1:
                        if (loader.nextFrame()) {
                            System.out.print(loader.readFrame());
                        }
                        break;
                    case 2:
                        if (loader.preFrame()) {
                            System.out.println(loader.readFrame());
                        }
                        break;
                    case 3:
                        if (textLoader.nextFrame()){
                            System.out.println(textLoader.readFrame());
                        }
                        break;
                        default:
                        return;
                }
            }
        }
    }

    public static void main(String[] args) throws Throwable {
        new TestLoader().testTextLoader();
    }
}
