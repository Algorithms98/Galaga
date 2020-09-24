import java.awt.*;
import javax.swing.*;

public class Background extends JLabel
{
    private ImageIcon image;
    
    public Background()
    {
        image = new ImageIcon("Images//stillBackground.png");
        this.setBounds(0, 0, 1000, 800);
        this.setVisible(true);
        this.setIcon(image);
        this.setLocation(0, 0);
    }
}