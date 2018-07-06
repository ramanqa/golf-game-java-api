package com.qait.tap.seltest.frisbeegolf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.UUID;
import org.json.JSONObject;

/**
 * 
 * 
 * 
 * @author Ramandeep RamandeepSingh[AT]QAInfoTech.com
 */
public class GameBoard {
    private Integer size;
    private Integer targetX;
    private Integer targetY;
    private Integer frisbeeX = 0;
    private Integer frisbeeY = 0;
    private Wind wind;
    private Integer par=-5;
    private UUID uuid;
    private Integer difficultyLevel = 0;
    /**
     * difficultLevel:
     *   0-1: board 11x1, x=0
     *   2-3: board 21x21
     *   4-5: board 51x51
     *   >5 : Exception IlleagalGameDIfficultyLevelException
     */
    
    
    public UUID getUUID(){
        return this.uuid;
    }
    
    public JSONObject toJson(){
        JSONObject boardJson = new JSONObject();
        boardJson.put("difficultyLevel", this.difficultyLevel);
        boardJson.put("size", this.size);
        boardJson.put("targetX", this.targetX);
        boardJson.put("targetY", this.targetY);
        boardJson.put("frisbeeX", this.frisbeeX);
        boardJson.put("frisbeeY", this.frisbeeY);
        boardJson.put("par", this.par);
        boardJson.put("wind", this.wind.toJson());
        return boardJson;
    }
    
    public String getResult(){
        if(this.frisbeeX == this.targetX && this.frisbeeY == this.targetY){
            switch(this.par){
                case -4: return "Hole in one! Condor! 4 under par";
                case -3: return  "Albatross! 3 under par";
                case -2: return "Eagle! 2 under par";
                case -1: return "Birdie! 1 under par";
                case 0: return "Par!";
                case 1: return "Bogey! 1 over par";
                case 2: return "Double Bogey! 2 over par";
                case 3: return "Triple Bogey! 3 over par";
                case 4: return "Quadruple Bogey! 4 over par";
                default: return "UNDEFINED";
            }
        }else{
            return "Frisbee not potted. Stroke score: " + this.par;
        }
    }
    
    @Override
    public String toString(){
        String board="";
        if(this.difficultyLevel < 2){
            for(int column = 0; column <= this.size-1; column++){
                if(column == this.targetX){
                    if(column == this.frisbeeX){
                        board += "[P]";
                    }else{
                        board += "[T]";
                    }
                }else if(column == this.frisbeeX){
                    board += "[F]";
                }else{
                    board += "[ ]";
                }
            }
        }else {
            for(int row = 0-(this.size-1)/2; row<=(this.size-1)/2; row++){
               for(int column = 0; column <= this.size-1; column++){
                    if(column == this.targetX && row == this.targetY){
                        if(column == this.frisbeeX && row == this.frisbeeY){
                            board += "[P]";
                        }else{
                            board += "[T]";
                        }
                    }else if(column == this.frisbeeX && row == this.frisbeeY){
                        board += "[F]";
                    }else{
                        board += "[ ]";
                    }
                }
                board += "\n";
            }
        }
        return board;
    }
    
    public void throwFrisbee(Integer power, String direction) throws Exception{
        if(this.frisbeeX == this.targetX && this.frisbeeY == this.targetY){
            throw new Exception("Game over!");
        }
        // MAX Power == 10
        if(power > 10){
            throw new Exception("Power exceeds max");
        }
        this.par++;
        save();
        
        Integer posX = this.frisbeeX;
        Integer posY = this.frisbeeY;
        
        if(direction.toUpperCase().contains("R")){
            posX += power;
        }
        if(direction.toUpperCase().contains("L")){
            posX -= power;
        }
        if(direction.toUpperCase().contains("D")){
            posX += power;
        }
        if(direction.toUpperCase().contains("U")){
            posX -= power;
        }
        posX += this.wind.getSpeed()[0];
        posY += this.wind.getSpeed()[1];
        
        System.out.println(posX);
        System.out.println(posY);
        if(posX >= this.size){
            throw new Exception("Out of bounds");
        }
        if(posX <= 0-this.size){
            throw new Exception("Out of bounds");
        }
        if(posY > (this.size-1)/2){
            throw new Exception("Out of bounds");
        }
        if(posY < 0-((this.size-1)/2)){
            throw new Exception("Out of bounds");
        }
        
        this.frisbeeX = posX;
        this.frisbeeY = posY;
        save();
    }
    
    private void save() throws FileNotFoundException, UnsupportedEncodingException{
        PrintWriter writer = new PrintWriter(this.uuid.toString() + ".board.json", "UTF-8");
        writer.write(this.toJson().toString());
        writer.close();
    }
    
    private void load() throws IOException{
        JSONObject boardJson = new JSONObject(new String(
                Files.readAllBytes(Paths.get(this.uuid.toString() + ".board.json"))));
        this.difficultyLevel = boardJson.getInt("difficultyLevel");
        this.size = boardJson.getInt("size");
        this.targetX = boardJson.getInt("targetX");
        this.targetY = boardJson.getInt("targetY");
        this.frisbeeX = boardJson.getInt("frisbeeX");
        this.frisbeeY = boardJson.getInt("frisbeeY");
        this.wind = new Wind(boardJson.getJSONArray("wind").getInt(0),
                boardJson.getJSONArray("wind").getInt(0));
        this.par = boardJson.getInt("par");
    }
    
    public GameBoard(Integer difficultyLevel)
            throws FileNotFoundException, UnsupportedEncodingException{
        this.uuid = UUID.randomUUID();
        this.difficultyLevel = difficultyLevel;
        this.wind = new Wind(difficultyLevel);
        switch(difficultyLevel){
            case 0:
            case 1:
                this.size = 11;
                this.targetX = new Random().nextInt(10) + 1; // 0123456789
                this.targetY = 0;
                break;
            case 2:
            case 3:
                this.size = 21;
                this.targetX = new Random().nextInt(10) + 11; // 0to20
                this.targetY = new Random().nextInt(21) - 10; // -10to10
                break;
            case 4:
            case 5:
                this.size = 51;
                this.targetX = new Random().nextInt(30) + 21; // 0to20
                this.targetY = new Random().nextInt(51) - 25; // -10to10
                break;
        }
        save();
    }
    
    public GameBoard(UUID uuid) throws IOException{
        this.uuid = uuid;
        load();
    }
}
