import java.awt.*;
import javax.swing.*;

public class Projectile
{
    private int x; // x and y location
    private int y;
    private Image image;

    public Projectile(Image img, int xLoc, int yLoc )
    {
        x = xLoc;
        y = yLoc;
        image = img;
    }
    
    public Projectile(String path, int xLoc, int yLoc)
    {
        this(new ImageIcon(path).getImage(), xLoc, yLoc);
    }

    public void draw(Graphics page)
    {
		page.drawImage(image, x + 43, y - 10, null);
    }

    public boolean update()
    {
        y -= 10;
        return y < 0;
    }
    
    public int getX() // returns x value
    {
        return this.x;
    }

    public int getY() // returns y value
    {
        return this.y;
    }
        
    public boolean isInside(Enemy en)
    {
        if(y <= en.getY() + 55 && y >= en.getY())
        {
            if(x <= en.getX() + 5 && x >= en.getX() - 55)
            {
                return true;
            }
        }
        return false;
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