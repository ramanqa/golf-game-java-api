package com.qait.tap.seltest.golf;

/**
 *
 * @author Ramandeep RamandeepSingh[AT]QAInfoTech.com
 */
public class GameOverException extends Exception{
    public GameOverException(){
        super("Game Over. Can not hit ball.");
    }
}
