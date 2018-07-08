package com.qait.tap.seltest.golf;

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
public class GolfCourse {
    private Integer size;
    private Integer holeXCoord;
    private Integer holeYCoord;
    private Integer golfBallXCoord = 0;
    private Integer golfBallYCoord = 0;
    private Wind wind;
    private Integer par=-5;
    private UUID uuid;
    private Integer difficultyLevel = 0;
    /**
     * difficultLevel:
     *   0-1: golfCourse 11x1, x=0
     *   2-3: golfCourse 21x21
     *   4-5: golfCourse 51x51
     *   >5 : Exception IlleagalGameDIfficultyLevelException
     */
    
    
    /**
     * get the Unique ID of current game
     * 
     * @return 
     */
    public UUID getUUID(){
        return this.uuid;
    }
    
    /**
     * get JSON desrialized representation of current game <br>
     * This included: <br>
     *   - Hole field size <br>
     *   - Ball's current coordinates <br>
     *   - Hole coordinates <br>
     *   - Current par score <br>
     *   - Wind speed/direction details <br>
     *   - Computed Results <br>
     * @return 
     */
    public JSONObject toJson(){
        JSONObject golfCourseJson = new JSONObject();
        golfCourseJson.put("difficultyLevel", this.difficultyLevel);
        golfCourseJson.put("size", this.size);
        golfCourseJson.put("holeXCoord", this.holeXCoord);
        golfCourseJson.put("holeYCoord", this.holeYCoord);
        golfCourseJson.put("golfBallXCoord", this.golfBallXCoord);
        golfCourseJson.put("golfBallYCoord", this.golfBallYCoord);
        golfCourseJson.put("par", this.par);
        golfCourseJson.put("wind", this.wind.toJson());
        golfCourseJson.put("result", this.getResult());
        return golfCourseJson;
    }
    
    /**
     * get computed result for the game
     * 
     * @return 
     */
    public String getResult(){
        if(this.golfBallXCoord == this.holeXCoord && this.golfBallYCoord == this.holeYCoord){
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
            return "Ball not potted. Stroke score: " + this.par;
        }
    }
    
    /** 
     * get printable string representation of current game
     * 
     * @return 
     */
    @Override
    public String toString(){
        String golfCourse="UUID: " + this.uuid + "\n"
                + "Game JSON: " + this.toJson().toString() + "\n"
                + "Result: " + this.getResult() + "\n"
                + "Board Visual: \n"
                + "    Legend: [ ]->Empty block"
                + ", [B]->Current position of ball"
                + ", [H]->Position of hole"
                + ", [P]->Ball rolled in hole. Game Over\n";
        if(this.difficultyLevel < 2){
            for(int column = 0; column <= this.size-1; column++){
                if(column == this.holeXCoord){
                    if(column == this.golfBallXCoord){
                        golfCourse += "[P]";
                    }else{
                        golfCourse += "[H]";
                    }
                }else if(column == this.golfBallXCoord){
                    golfCourse += "[B]";
                }else{
                    golfCourse += "[ ]";
                }
            }
        }else {
            for(int row = 0-(this.size-1)/2; row<=(this.size-1)/2; row++){
               for(int column = 0; column <= this.size-1; column++){
                    if(column == this.holeXCoord && row == this.holeYCoord){
                        if(column == this.golfBallXCoord && row == this.golfBallYCoord){
                            golfCourse += "[P]";
                        }else{
                            golfCourse += "[H]";
                        }
                    }else if(column == this.golfBallXCoord && row == this.golfBallYCoord){
                        golfCourse += "[B]";
                    }else{
                        golfCourse += "[ ]";
                    }
                }
                golfCourse += "\n";
            }
        }
        return golfCourse;
    }
    
    /**
     * hit gold ball with power and to which direction on field. 
     *   Ball will end on the block on field depending on the direction
     * supplied, power and wind speed. <br>
     * For instance if <br>
     *    ball is at [0,0]; <br>
     *    Wind speed is =[2,-2]; <br>
     *    Power = 5; <br>
     *    Direction = RD; <br>
     *    Final ball position after the shot will be [0+5+2, 0+5+(-2)] => [7,3] <br>
     * Wind [x,y]: <br>
     *   x - positive means wind direction is right - R <br>
     *   -x - negative means wind direction is left - L <br>
     *   y - positive means wind direction is downwards - D <br>
     *   -y - negative means wind direction is upwards - U <br>
     * 
     * @param power - Strength of your shot. Max power is 10. <br>
     *   This is the number of blocks ball will travel when hit before <br>
     *   considering wind resistance.
     * @param direction - Direction you want to hit the ball. <br>
     *   You can hit ball in right, left, up and down direction and also diagonally. <br>
     *   Options: Any one of R, RD, D, LD, L, LU, U, RU
     * @throws com.qait.tap.seltest.golf.GameOverException - When trying to hit the ball and ball is already in hole.
     * @throws com.qait.tap.seltest.golf.PowerExceedsMaxRangeException - Max power is 10
     * @throws com.qait.tap.seltest.golf.BallOutOfBoundsException - When ball lands outside field grid
     * @throws java.io.UnsupportedEncodingException
     * @throws java.io.FileNotFoundException
     */
    public void hitGolFBall(Integer power, String direction) 
            throws GameOverException, PowerExceedsMaxRangeException
                , FileNotFoundException, BallOutOfBoundsException
                , UnsupportedEncodingException{
        if(this.golfBallXCoord == this.holeXCoord && this.golfBallYCoord == this.holeYCoord){
            throw new GameOverException();
        }
        // MAXCoord Power == 10
        if(power > 10){
            throw new PowerExceedsMaxRangeException();
        }
        this.par++;
        saveToDB();
        
        Integer posXCoord = this.golfBallXCoord;
        Integer posYCoord = this.golfBallYCoord;
        
        if(direction.toUpperCase().contains("R")){
            posXCoord += power;
        }
        if(direction.toUpperCase().contains("L")){
            posXCoord -= power;
        }
        if(direction.toUpperCase().contains("D")){
            posYCoord += power;
        }
        if(direction.toUpperCase().contains("U")){
            posYCoord -= power;
        }
        posXCoord += this.wind.getSpeed()[0];
        posYCoord += this.wind.getSpeed()[1];
        
        if(posXCoord >= this.size){
            throw new BallOutOfBoundsException();
        }
        if(posXCoord < 0){
            throw new BallOutOfBoundsException();
        }
        if(posYCoord > (this.size-1)/2){
            throw new BallOutOfBoundsException();
        }
        if(posYCoord < 0-((this.size-1)/2)){
            throw new BallOutOfBoundsException();
        }
        
        this.golfBallXCoord = posXCoord;
        this.golfBallYCoord = posYCoord;
        saveToDB();
    }
    
    private void saveToDB() throws FileNotFoundException, UnsupportedEncodingException{
        PrintWriter writer = new PrintWriter(this.uuid.toString() + ".golfCourse.json", "UTF-8");
        writer.write(this.toJson().toString());
        writer.close();
    }
    
    private void loadFromDB() throws IOException{
        JSONObject golfCourseJson = new JSONObject(new String(
                Files.readAllBytes(Paths.get(this.uuid.toString() + ".golfCourse.json"))));
        this.difficultyLevel = golfCourseJson.getInt("difficultyLevel");
        this.size = golfCourseJson.getInt("size");
        this.holeXCoord = golfCourseJson.getInt("holeXCoord");
        this.holeYCoord = golfCourseJson.getInt("holeYCoord");
        this.golfBallXCoord = golfCourseJson.getInt("golfBallXCoord");
        this.golfBallYCoord = golfCourseJson.getInt("golfBallYCoord");
        this.wind = new Wind(golfCourseJson.getJSONArray("wind").getInt(0),
                golfCourseJson.getJSONArray("wind").getInt(1));
        this.par = golfCourseJson.getInt("par");
    }
    
    /**
     * 
     * @param difficultyLevel - 0 to 5 <br>
     *  difficultLevel: <br>
     *   0: golfCourse 11x1, x=0, no wind <br>
     *   1: golfCourse 11x1, x=0 <br>
     *   2-3: golfCourse 21x21 <br>
     *   4-5: golfCourse 51x51 <br>
     *   >5 or <1: Exception IllegalGolfCourseDIfficultyLevelException
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IllegalGolfCourseDifficultyException 
     */
    public GolfCourse(Integer difficultyLevel)
            throws FileNotFoundException, UnsupportedEncodingException
                , IllegalGolfCourseDifficultyException{
        this.uuid = UUID.randomUUID();
        this.difficultyLevel = difficultyLevel;
        this.wind = new Wind(difficultyLevel);
        switch(difficultyLevel){
            case 0:
            case 1:
                this.size = 11;
                this.holeXCoord = new Random().nextInt(10) + 1; // 0123456789
                this.holeYCoord = 0;
                break;
            case 2:
            case 3:
                this.size = 21;
                this.holeXCoord = new Random().nextInt(10) + 11; // 0to20
                this.holeYCoord = new Random().nextInt(21) - 10; // -10to10
                break;
            case 4:
            case 5:
                this.size = 51;
                this.holeXCoord = new Random().nextInt(30) + 21; // 0to20
                this.holeYCoord = new Random().nextInt(51) - 25; // -10to10
                break;
            default: 
                throw new IllegalGolfCourseDifficultyException();
        }
        saveToDB();
    }
    
    /**
     * load an existing game with unique id/uuid
     * 
     * @param uuid
     * @throws IOException 
     */
    public GolfCourse(UUID uuid) throws IOException{
        this.uuid = uuid;
        loadFromDB();
    }
}
