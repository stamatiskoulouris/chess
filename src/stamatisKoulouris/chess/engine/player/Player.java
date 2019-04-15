/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stamatisKoulouris.chess.engine.player;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import stamatisKoulouris.chess.engine.Alliance;
import stamatisKoulouris.chess.engine.board.Board;
import stamatisKoulouris.chess.engine.board.Move;
import stamatisKoulouris.chess.engine.pieces.King;
import stamatisKoulouris.chess.engine.pieces.Piece;

/**
 *
 * @author Stamatis Koulouris
 */
public abstract class Player {
    
    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;
    
   Player(final Board board, 
          final Collection<Move> legalMoves,
          final Collection<Move>opponentMoves){
       
       this.board=board;
       this.playerKing=establishKing();
       this.legalMoves=ImmutableList.copyOf(Iterables.concat(legalMoves,calculateKingCastle(legalMoves,
       opponentMoves)));
       /*prepei na kserw kai tou adipalou tis kinisis wste na paiksw bala me ton Castle Px an eimai underAttack 
       to kanw save edw */
        
       
      this.isInCheck=!Player.calculateAttackOnTile(this.playerKing.getPiecePosition(),opponentMoves).isEmpty();
      /* mark the enenemy attack + if list == empty then player is NOT check */
     
      
   }    

   
   public King getPlayerKing (){
       return this.playerKing;
   }
   
   public Collection <Move>getLegalMoves(){
       return this.legalMoves;
   }
   protected static Collection<Move> calculateAttackOnTile(int piecePosition,Collection<Move> moves){
       final List<Move> attackMoves= new ArrayList<>();
       
       for ( final Move move : moves){
           if(piecePosition==move.getDestinationCoordinate()){ 
               attackMoves.add(move);
           }
       }
       return ImmutableList.copyOf(attackMoves);
   }
   
    private King establishKing()  {  // GIVE US A REASON state or to Live :P
       
         
    for(final Piece piece : getActivePieces()){
          
      if(piece.getPieceType().isKing()) {
                return (King) piece;
            }
      
      
      //throw new RuntimeException("Should not reach here , bad BOARD!!" ); 
    
          }
      return null;
    }
    
    
    public boolean isMoveLegal(final Move move){
        return this.legalMoves.contains(move);
    }
    // we need work here 
    
    public boolean isInCheck(){
        return this.isInCheck; 
    }
    
    public boolean isInCheckMate(){
        return isInCheck && !hasEscapeMoves();  // Magic
           }
    
    
  
    public boolean isInStaleMate(){
         return !isInCheck && !hasEscapeMoves(); //TA DAAAAAAAAAA!!!
    }
    
      public boolean isCastled() {
        return false;
    }
      
        public boolean hasEscapeMoves(){
        for(final Move move : this.legalMoves){
            final MoveTransition transition = makeMove(move);
            if(transition.getMoveStatus().isDone()){
                return true;
            } // pada kserw kathe kinish se ena board (asxeto me auto poy pezw ) apla na kserw oles tis pithanes kinisis    
            
        }
        return false;
    }
      
   public  MoveTransition makeMove(final Move move){
        
       if(!isMoveLegal(move)){
           return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
       }
       
       final Board transitionBoard = move.execute();
       
      Collection<Move> kingAttacks=
      Player.calculateAttackOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                       transitionBoard.currentPlayer().getLegalMoves());
      
      if(!kingAttacks.isEmpty()){
          
          return new MoveTransition(this.board,move,MoveStatus.LEAVES_PLAYER_IN_CHECK);// No moves
      }
          
         return new MoveTransition(transitionBoard,move,MoveStatus.DONE); // ok Keep Going
      }
    
     
        
      
    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
    protected abstract Collection<Move> calculateKingCastle(Collection<Move>playerLegals,Collection<Move> opponentsLegals);    
    
}
