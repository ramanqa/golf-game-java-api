package com.qait.tap.seltest.golf;

import java.util.Random;
import org.json.JSONArray;

/**
 * 
 * @author Ramandeep RamandeepSingh[AT]QAInfoTech.com
 */
public class Wind {
    Integer speedY = 0;
    Integer speedX = 0;
   
    public Integer[] getSpeed(){
        return new Integer[]{speedX, speedY};
    }
    
    @Override
    public String toString(){
        return speedX + " " + speedY;
    }
    
    public JSONArray toJson(){
        JSONArray windJson = new JSONArray();
        windJson.put(speedX);
        windJson.put(speedY);
        return windJson;
    }
    
    Wind(Integer difficultyLevel){
        switch(difficultyLevel){
            case 0: // no speed
                    break;
            case 1: // up down -1 to 1
                speedX = new Random().nextInt(3) - 1; // 012, -101
                break;
            case 2: // up down -2 to 2
                speedX = new Random().nextInt(5) - 2; // 01234, -2-1012
                break;
            case 3: // up down -3 to 3, left right -1 to 1
                speedX = new Random().nextInt(7) - 3; // 0123456, -3-2-10123
                speedY = new Random().nextInt(3) - 1; // 012, -101
                break;
            case 4: // up down -4 to 4, left right -2 to 2
                speedX = new Random().nextInt(9) - 4; // 012345678, -4-3-2-101234
                speedY = new Random().nextInt(5) - 2; // 01234, -2-1012
                break;
            case 5: // up down -5 to 5, left right -4 to 4
                speedX = new Random().nextInt(11) - 5; // 0123456789 10, -5-3-2-101234
                speedY = new Random().nextInt(9) - 4; // 01234, -2-1012 
                break;
            default:
                break;
        }
    }
    
    public Wind(Integer speedX, Integer speedY){
        this.speedX = speedX;
        this.speedY = speedY;
    }
}
