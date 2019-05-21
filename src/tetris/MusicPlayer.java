/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author alu20925322z
 */
public class MusicPlayer {
    
    public void playMusic(){                                              
        
        try{                                               
            File musicPath = new File("src/tetris/Music/Tetris_99_theme.wav");
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath); 
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
            clip.loop(clip.LOOP_CONTINUOUSLY);

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    
    
    
}
