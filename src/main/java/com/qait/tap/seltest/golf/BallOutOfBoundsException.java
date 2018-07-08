package com.qait.tap.seltest.golf;

/**
 *
 * @author Ramandeep RamandeepSingh[AT]QAInfoTech.com
 */
public class BallOutOfBoundsException extends Exception{
    public BallOutOfBoundsException(){
        super("Ball has rolled out of the field boundary");
    }
}
