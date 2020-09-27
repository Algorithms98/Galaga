import java.awt.*;

import javax.swing.*;

public class Background extends JLabel {
	private static final long serialVersionUID = 1376221642883404767L;
	private Image image;

	public Background() {
		image = new ImageIcon("Images//stillBackground.png").getImage();
		this.setBounds(0, 0, 1000, 800);
		this.setVisible(true);
		this.setLocation(0, 0);
	}

	public void draw(Graphics page)
	{
		page.drawImage(image, 0, 0, null);
	}
}