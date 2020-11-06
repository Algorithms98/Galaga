import java.awt.*;//must be imported to use Graphics and Color
import java.util.ArrayList;

import javax.swing.*;

public class Enemy
{
    protected int x;                  // x position
    protected int y;                  // y position
    private int vx;                   // x velocity
    private int vy;
    private int numMoves;
    private int maxMoves;
    protected Image image;

    public Enemy(Image img, int xLoc, int yLoc )
    {
        x = xLoc;
        y = yLoc;
        vx = 1;
        vy = 20;
        numMoves = 0;
        maxMoves = 150;
        image = img;
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
        if (turnToShoot && enCanShoot) {
            game.enemyShoot(x + 10, y);
        }

        //if (y > 680)
            //game.gameOver();
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

    public void draw( Graphics page )
    {
        page.drawImage(image, x - 5, y, null);
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }
}