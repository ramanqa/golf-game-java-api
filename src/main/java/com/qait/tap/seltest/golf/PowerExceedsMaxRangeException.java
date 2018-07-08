package com.qait.tap.seltest.golf;

/**
 *
 * @author Ramandeep RamandeepSingh[AT]QAInfoTech.com
 */
public class PowerExceedsMaxRangeException extends Exception{
    public PowerExceedsMaxRangeException(){
        super("Power entered can not exceed max range of your Golf Club: 10");
    }
}
