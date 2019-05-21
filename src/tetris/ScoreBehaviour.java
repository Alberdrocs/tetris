/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

/**
 *
 * @author alu20925322z
 */
public interface ScoreBehaviour {
    public void increment(int increment);
    public void reset();
    public int getScore();
}
