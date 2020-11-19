import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound implements LineListener
{
	private Clip audioClip;
	private boolean playCompleted = true;
	
	public Sound(final String file)
	{
		File audioFile = new File(file);
		
		try {
			
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			AudioFormat format = audioStream.getFormat();
			 
	        DataLine.Info info = new DataLine.Info(Clip.class, format);
	        
	        audioClip = AudioSystem.getClip();
	        
	        audioClip = (Clip) AudioSystem.getLine(info);
	
	        audioClip.addLineListener(this);
	
	        audioClip.open(audioStream);
	        
	        // sets audio volume
	        FloatControl gainControl = 
	        	    (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
	        	gainControl.setValue(-20.0f);
		}
		catch (UnsupportedAudioFileException ex) {
            System.out.println("The specified audio file is not supported.");
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }
		
	}

	// pre: none
	// post: plays the sound 
	public void play()
	{
			if(playCompleted)
			{
				audioClip.setMicrosecondPosition(0);
				audioClip.start();
				playCompleted = false;
			}
		
	}

	// pre: none
	// post: stops playing the sound
	public void stop()
	{
		audioClip.stop();
	}

	// pre: none
	// post: makes the audio loop
	public void loop()
	{
		audioClip.loop(0);
	}
	
	public void update(LineEvent event) {
        LineEvent.Type type = event.getType();
         
        if (type == LineEvent.Type.START) {
            
             
        } else if (type == LineEvent.Type.STOP) {
            playCompleted = true;
            
        }
 
    }

	public Clip getClip()
	{
		return audioClip;
	}
}