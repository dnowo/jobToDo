import javax.swing.*;
import java.awt.*;
/**
 * Class prints background image for frames, panels.
 * @author Daniel Nowosielecki
 * @version 1.0.0
 * */
public class ImagePrinter extends JComponent {
    private Image image;
    public ImagePrinter(Image image) {
        this.image = image;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}
