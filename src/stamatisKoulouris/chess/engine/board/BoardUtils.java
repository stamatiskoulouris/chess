/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stamatisKoulouris.chess.engine.board;

/**
 *
 * @author Stamatis Koulouris
 */
public class BoardUtils {

    public static final boolean[] FIRST_COLUMN=initColumn(0); // topology. EIMAI EKEI? (Knight , Bishop , Rook , Queen)
    public static final boolean[] SECOND_COLUMN=initColumn(1);
    public static final boolean[] SEVEN_COLUMN=initColumn(6);
    public static final boolean[] EIGHT_COLUMN=initColumn(7);
    
    public static final boolean[] EIGHTH_RANK=initRow(0); 
    public static final boolean[] SEVENTH_RANK=initRow(8);  // initRow (tile) 
    public static final boolean[] SIXTH_RANK=initRow(16); 
    public static final boolean[] FIFTH_RANK=initRow(24);
    public static final boolean[] FORTH_RANK=initRow(32);
    public static final boolean[] THIRD_RANK=initRow(40); 
    public static final boolean[] SECOND_RANK=initRow(48);
    public static final boolean[] FIRST_RANK=initRow(56); 
      
    
    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_ROW=8;

    
    private static boolean[] initColumn(int columnNumber) {
        
        final boolean [] column = new boolean[NUM_TILES];
        do {
            column[columnNumber]=true;
            columnNumber+=NUM_TILES_ROW; // next line for board
            
        }while(columnNumber<NUM_TILES);
        
        return column;
    }
    
    private static boolean[] initRow(int rowNumber){
        final boolean [] row = new boolean[NUM_TILES];
        do{
           row[rowNumber]=true;
           rowNumber++;
        }while(rowNumber % NUM_TILES_ROW !=0);
 
        return row;
    }

  

    private BoardUtils(){
     throw new RuntimeException("You cannot instantiate me!");   
    }
    
    public static boolean isValidTileCoordinate(final int coordinate) {
        return coordinate >= 0 && coordinate < NUM_TILES; //board size einai mesa?
    }
    
}
