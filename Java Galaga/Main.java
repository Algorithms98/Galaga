import javax.swing.*;

public class Main
{
<<<<<<< Upstream, based on origin/master
=======
		
		public static final int WIDTH = 1000;
		public static final int HEIGHT = 1000;
		
>>>>>>> 46b35d8 menu added, work on flying and circular path taking

<<<<<<< Upstream, based on origin/master
    public static final int WIDTH = 500;
    public static final int HEIGHT = 700;

    public static void main(String[] args)
    {
        //create a JFrame (window) that will be visible on screen
        JFrame frame = new JFrame( "Game" );
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //make the red close button work
        frame.setLocation( 0, 0 ); //place the frame in the upper left corner
        Game game = new Game(Main.WIDTH, Main.HEIGHT); //create a Game object with width = 1000, height = 800
        frame.getContentPane().add(game); //add game to the frame so it will be on the screen
        frame.pack();
        frame.setVisible(true);
        game.playGame();//call the playGame() method to intitiate the game
    }
=======
		public static void main(String[] args)
		{
				//create a JFrame (window) that will be visible on screen
				JFrame frame = new JFrame( "Game" );
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //make the red close button work
				frame.setLocation( 0, 0 ); //place the frame in the upper left corner
				Game game = new Game(Main.WIDTH, Main.HEIGHT); //create a Game object with width = 1000, height = 800
				frame.getContentPane().add(game); //add game to the frame so it will be on the screen
				frame.pack();
				frame.setVisible(true);
				game.menu();//call the playGame() method to intitiate the game
		}
>>>>>>> 46b35d8 menu added, work on flying and circular path taking
}