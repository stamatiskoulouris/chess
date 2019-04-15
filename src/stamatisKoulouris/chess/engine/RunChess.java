package stamatisKoulouris.chess.engine;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import stamatisKoulouris.chess.engine.board.Board;
import stamatisKoulouris.chess.gui.Table;

/**
 *
 * @author Stamatis Koulouris
 */
public class RunChess {
    
    public static void main(String[] args){
        
        Board board = Board.createStandarBoard();
        
        System.out.println(board);
   
        Table table = new Table();
    }
    
}
