import java.awt.*;

public abstract class Drawable {
  protected int x;
  protected int y;
  protected Image image;

  public void draw(Graphics page) {
    page.drawImage(image, x-(this.getWidth()/2), y-(this.getHeight()/2), null);
  }

  public void setPosition(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getWidth() {
    return this.image.getWidth(null);
  }

  public int getHeight() {
    return this.image.getHeight(null);
  }
}
