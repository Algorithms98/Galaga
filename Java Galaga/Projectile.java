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
        page.setColor( Color.WHITE );
        //page.fillRect( this.x + 50, this.y - 20, 4, 20 );
    }

    public void move()
    {
        for(int i = 0; i < 1000; i++)
        {
            if(y > 0 && i % 100 == 0)
            {
                this.y -= 1;
            }
        }
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