package me.zhaotb.file.reader.viewer;

import me.zhaotb.file.reader.loader.ByteLoader;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;

public class MainView extends JFrame implements MainViewer {

    private int width = MainViewer.DEFAULT_WIDTH;
    private int height = MainViewer.DEFAULT_HEIGHT;

    private int fontSize = 10;
    private ByteLoader loader;

    private JLabel label;

    public MainView(String title, String filePath, String fileName) throws HeadlessException, IOException {
        super(title);
        this.setSize(width , height);

        Point point = screenCenter();
        point.move(point.x - width/2, point.y - height/2);
        this.setLocation(point);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        loader = new ByteLoader(filePath, fileName);
    }


    public synchronized String readFrame(){
        if (label == null) {
            label = new JLabel();
            add(label);
            FontMetrics fontMetrics = label.getFontMetrics(label.getFont());
            Dimension size = getSize();
            int area = size.width * size.height;
//            int wordCount = area / fontMetrics.charWidth("A");
//            loader.setBufferSize(wordCount);
        }
        String str = "";// loader.read(0, 10);

        label.setText("<html>" + str.replaceAll("\n", "<br>") + "</html>");

        return str;
    }

    public Point screenCenter(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new Point(screenSize.width / 2, screenSize.height / 2 );
    }
}
