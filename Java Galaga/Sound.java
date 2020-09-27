import java.applet.*;

public class Sound
{
	private final AudioClip clip;

	public Sound(final String file)
	{
		clip = Applet.newAudioClip(getClass().getResource(file));
	}

	// pre: none
	// post: plays the sound 
	public void play()
	{
		clip.play();
	}

	// pre: none
	// post: stops playing the sound
	public void stop()
	{
		clip.stop();
	}

	// pre: none
	// post: makes the audio loop
	public void loop()
	{
		clip.loop();
	}
}