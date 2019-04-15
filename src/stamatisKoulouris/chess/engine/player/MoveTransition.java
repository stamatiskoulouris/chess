//need to work on moves this weak!!!
package stamatisKoulouris.chess.engine.player;

import stamatisKoulouris.chess.engine.board.Board;
import stamatisKoulouris.chess.engine.board.Move;

/**
 *
 * @author Stamatis Koulouris
 */
public class MoveTransition {
    
    private final Board transitionBoard;
    private final Move move; 
    private final MoveStatus moveStatus; // we can do the move ??? legal or not?
    
    public MoveTransition(final Board transitionBoard, final Move move, final MoveStatus moveStatus){
        this.transitionBoard=transitionBoard;
        this.move=move;
        this.moveStatus=moveStatus;
    }
    
    
  public MoveStatus getMoveStatus(){
      return this.moveStatus;
  }
  
  public Board getTransionBoard(){
      return this.transitionBoard;
  }
}
