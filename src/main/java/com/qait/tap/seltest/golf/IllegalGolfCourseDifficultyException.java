package com.qait.tap.seltest.golf;

/**
 *
 * @author Ramandeep RamandeepSingh[AT]QAInfoTech.com
 */
public class IllegalGolfCourseDifficultyException extends Exception{
    public IllegalGolfCourseDifficultyException(){
        super("Difficulty range is 0-5");
    }
}
