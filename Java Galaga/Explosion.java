import java.awt.*;//must be imported to use Graphics and Color
import javax.swing.*;

public class Explosion
{
    private int x;                  // x position
    private int y;                  // y position
    private Image image;

    public Explosion(Image img, int xLoc, int yLoc )
    {
        x = xLoc;
        y = yLoc;
        image = img;
    }
    
    public Explosion(String path, int xLoc, int yLoc)
    {
        this(new ImageIcon(path).getImage(), xLoc, yLoc);
    }

    public void draw( Graphics page )
    {
        page.setColor( new Color( 255, 255, 255 ) ); //color defined using rgb values (0-255 each)
        //page.fillRect( x, y, 55, 55 ); //change the last two numbers and see what happens
    }
    
    public int getY()
    {
        return this.y;
    }
    
    public int getX()
    {
        return this.x;
    }
    
    public void setX(int val)
    {
        x = val;
    }
    
    public void setY(int val)
    {
        y = val;
    }
    
    public Image getImage()
    {
        return image;
    }
}