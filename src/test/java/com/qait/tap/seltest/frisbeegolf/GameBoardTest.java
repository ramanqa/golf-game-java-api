package com.qait.tap.seltest.frisbeegolf;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import org.testng.annotations.*;
import static org.testng.Assert.*;

/**
 *
 * @author ramandeep
 */
public class GameBoardTest {
    
    UUID uuid;
    
//    @Test
    public void test() throws FileNotFoundException, UnsupportedEncodingException{
        GameBoard board = new GameBoard(3);
        uuid = board.getUUID();
        System.out.println(uuid);
        System.out.println(board.toJson());
        System.out.println(board);
//        board.throwFrisbee(Integer.SIZE, direction);
    }
    
    @Test
    public void runF() throws Exception{
        uuid = UUID.fromString("325448c5-e922-4045-8dd8-87d2ef699043");
        GameBoard board = new GameBoard(uuid);
        System.out.println(board.toJson());        
        System.out.println(board);
        board.throwFrisbee(5, "R");
        System.out.println(board.toJson());        
        System.out.println(board);
        System.out.println(board.getResult());
//        board
    }
    
}
