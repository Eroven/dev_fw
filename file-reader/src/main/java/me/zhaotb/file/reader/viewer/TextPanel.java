package me.zhaotb.file.reader.viewer;

import me.zhaotb.file.reader.loader.ByteLoader;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.IOException;

public class TextPanel extends JPanel implements Viewer {

    private String filePath;
    private String fileName;

    private int fontSize = 10;
    private ByteLoader loader;

    private JLabel label;

    public TextPanel(String filePath, String fileName) throws IOException {
        this.filePath = filePath;
        this.fileName = fileName;
        loader = new ByteLoader(filePath, fileName);
        Dimension size = getSize();
        int area = size.width * size.height;
        int wordCount = area / fontSize;
        loader.setBufferSize(wordCount);
    }

    public synchronized String readFrame(){
        if (label == null) {
            label = new JLabel();
            add(label);
        }
        String str = "";
//                = loader.read(0, 10);

        label.setText(str);

        return str;
    }




    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }
}
