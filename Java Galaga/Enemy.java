import java.awt.*;//must be imported to use Graphics and Color
import java.util.ArrayList;

import javax.swing.*;

public class Enemy extends DrawableObject
{
    private int vx;                   // x velocity
    private int vy;
    private int numMoves;
    private int maxMoves;
    private int width;
    private int height;

    protected boolean allowedToShoot = false;
    public Enemy(Image img, int xLoc, int yLoc )
    {
        x = xLoc;
        y = yLoc;
        vx = 1;
        vy = 20;
        numMoves = 0;
        maxMoves = 150;
        image = img;
        width = image.getWidth(null);
        height = image.getHeight(null);
        
        
    }

    public Enemy(String path, int xLoc, int yLoc)
    {
        this(new ImageIcon(path).getImage(), xLoc, yLoc);
        vx = 1;
        vy = 20;
        numMoves = 0;
        maxMoves = 150;
    }

    public Projectile update(boolean turnToShoot, boolean enCanShoot, boolean gridMovingRight, ArrayList<Projectile> playerBullets, Game game)
    {

        for (Projectile bullet: playerBullets)
        {
            if (bullet.isInside(this)) {
                return bullet;
            }
        }
        move();
        if (turnToShoot && enCanShoot && allowedToShoot) {
            game.enemyShoot(x + 10, y);
        }
        
        return null;
    }

    public void move()
    {
        x += vx;
        if(numMoves > maxMoves)
        {
            vx = -vx;
            y += vy;
            numMoves = 0;
            maxMoves = 300;
        } 
        numMoves++;
    }

    @Override
    public void draw( Graphics page )
    {
        page.drawImage(image, x - 5, y, null);
    }
    
    public int getWidth()
    {
    	return width;
    	
    }
    
    public int getHeight()
    {
    	return height;
    }
}