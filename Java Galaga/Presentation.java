import java.awt.*;
<<<<<<< HEAD
import javax.swing.*; 
import java.util.ArrayList; 
=======
import java.util.ArrayList;

import javax.swing.JLabel;
>>>>>>> b999c81d... Lots of python and Chris' updates

public class Presentation{
  
  private ArrayList<DrawableObject> drawables = new ArrayList<>();
  private ArrayList<JLabel> jLabels = new ArrayList<>();

  // @Override 
  // void paintComponent(Graphics page){
  //   super.paintComponent(page);
  //   for(Drawable object: drawables){
  //     object.draw(page);
  //   }
  // }
  
  public void addDrawable(DrawableObject drawable, int x, int y) {
  //set drawable positions add to it arraylist 
    drawable.setPosition(x,y);
    drawables.add(drawable);

}
  public void addJLabel(String text, int x, int y, int width, int height){
    //set the font and the balance add it to jlabels list 
    JLabel guides = new JLabel(text);

    guides.setBounds(x, y, width, height);
    guides.setForeground(Color.RED);
    guides.setFont(new Font("Lava", Font.BOLD, 30));
    jLabels.add(guides);  
}

}
