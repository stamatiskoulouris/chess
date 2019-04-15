/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stamatisKoulouris.chess.engine.player;

/**
 *
 * @author Stamatis Koulouris
 */
public enum MoveStatus {
    DONE {
        @Override
       public boolean isDone() {
           return true;
        }
    },
    
    ILLEGAL_MOVE{
     @Override
       public boolean isDone() {
           return false;
        }
},
    LEAVES_PLAYER_IN_CHECK{
        @Override
       public boolean isDone() {
           return false;
        }   
    };
    
    public abstract boolean isDone();
}
