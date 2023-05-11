package com.zetcode;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

/**
 * Taken from http://www.java2s.com/Code/Java/Development-Class/AnexampleofloadingandplayingasoundusingaClip.htm
 */
public class Sound {

  public static void playGameOverSound() throws Exception {

    File soundFile = new File("src/resources/GameOver.wav");
    AudioInputStream sound = AudioSystem.getAudioInputStream(soundFile);

    DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
    Clip clip = (Clip) AudioSystem.getLine(info);
    clip.open(sound);

    clip.start();
  }
  public static void playAppleSound() throws Exception {
	File appleSound = new File("src/resources/Apple.wav");
    AudioInputStream sound = AudioSystem.getAudioInputStream(appleSound);

    DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
    Clip clip = (Clip) AudioSystem.getLine(info);
    clip.open(sound);

    clip.start();
  }
  public static void playpAppleSound() throws Exception {

	    File pAppleSound = new File("src/resources/pApple.wav");
	    AudioInputStream sound = AudioSystem.getAudioInputStream(pAppleSound);

	    DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
	    Clip clip = (Clip) AudioSystem.getLine(info);
	    clip.open(sound);

	    clip.start();
	  }
  public static void playpGoldAppleSound() throws Exception {

	    File goldAppleSound = new File("src/resources/GoldApple.wav");
	    AudioInputStream sound = AudioSystem.getAudioInputStream(goldAppleSound);

	    DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
	    Clip clip = (Clip) AudioSystem.getLine(info);
	    clip.open(sound);

	    clip.start();
	  }
}