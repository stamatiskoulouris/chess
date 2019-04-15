/*
 * ta moves ginode se diaforetiko Board ena board players + ena to machine
 * 
 * 
 */
package stamatisKoulouris.chess.engine.board;

import stamatisKoulouris.chess.engine.board.Board.Builder;
import stamatisKoulouris.chess.engine.pieces.Pawn;
import stamatisKoulouris.chess.engine.pieces.Piece;
import stamatisKoulouris.chess.engine.pieces.Rook;

/**
 * @author Stamatis Koulouris
 */
public abstract class Move {
    
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;
    
    public static final Move NULL_MOVE = new NullMove();
    
 private Move(final Board board, final Piece movedPiece,final int destinationCoordinate){
        this.board=board;
        this.movedPiece=movedPiece;
        this.destinationCoordinate=destinationCoordinate;
    }
 
 @Override
 public int hashCode(){
     final int prime = 33 ;
     int result = 1;
     result= prime * result + this.destinationCoordinate;
     result= prime * result + this.movedPiece.hashCode();
     
     return result;
 }
 
 @Override
 public boolean equals(final Object other){
     if(this == other){
         return true;
     }
     if(!(other instanceof Move)){
         return false;
     }
     
     final Move otherMove= (Move) other ;
     return getDestinationCoordinate() == otherMove.getDestinationCoordinate() && getMovedPiece().equals(otherMove
     .getMovedPiece());
             
 }
 
 
 public int getCurrentCoordinate(){
     return this.movedPiece.getPiecePosition();
 }
 
 public int getDestinationCoordinate(){
     return this.destinationCoordinate;
 }
 
 public Piece getMovedPiece(){
     return this.movedPiece;
 }
    
 public boolean isAttacked(){
     return false;
 }
 
 public boolean isCastlingMove(){
     return false;
 }
 
 public Piece getAttackedPiece(){
     return null;
 }
       
 
     public Board execute() {
            final Builder builder = new Builder();
            
            for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                
                if(!this.movedPiece.equals(piece) )
                    builder.setPiece(piece);
            }
            
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            //hope to move the moved piece!!!
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }

   
   
 
    public static final class MajorMove extends Move{
    
        public MajorMove(final Board board,final Piece movedPiece,final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        
      
       
       
}
    public static class AttackMove extends Move{
        
        final Piece attackedPiece;
        
        public AttackMove(final Board board,final Piece movedPiece,final int destinationCoordinate,final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece=attackedPiece;
        }

        @Override
        public Board execute() {
            return null;
        }
        @Override
        public int hashCode(){
            return this.attackedPiece.hashCode() + super.hashCode();
        }
        
         @Override
         public boolean equals(Object other){
             if(this == other){
                 return true; 
             }
             if(!(other instanceof AttackMove)){
                 return false;
             }
             final AttackMove otherAttackMove = (AttackMove) other;
             return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
         }
             
        
        @Override
        public boolean isAttacked(){
            return true; 
        }
        
         @Override
        public Piece getAttackedPiece(){
            return this.attackedPiece; 
        }
        
        
        }
  
      public static final class PawnMove extends Move{
    
        public PawnMove(final Board board,final Piece movedPiece,final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

      }
      
        public static  class PawnAttackMove extends AttackMove{
    
        public PawnAttackMove(final Board board,final Piece movedPiece,final int destinationCoordinate, 
                final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate,attackedPiece);
            }
         } 
      
       public static final class PawnEnPassantAttackMove extends PawnAttackMove{
    
        public PawnEnPassantAttackMove(final Board board,final Piece movedPiece,final int destinationCoordinate, 
                final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate,attackedPiece);
            }
         } 
       
       
           public static final class PawnJump extends Move{
    
        public PawnJump(final Board board,final Piece movedPiece,final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
        
        @Override
        public Board execute(){
            final Builder  builder = new Builder();
            for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece); 
            }
            final Pawn movePawn=(Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movePawn); // Go litle SOldier!!!
            builder.setEnPassantPawn(movePawn); 
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
            
        }

      }
           
        static abstract  class CastleMove extends Move{
         protected final Rook castleRook;
            protected final int castleRookStart;
            protected final int castleRookDestination;
            
         public CastleMove(final Board board,final Piece movedPiece,final int destinationCoordinate,
                 final Rook castleRook,final int castleRookStart,final int castleRookDestination  ) {
            super(board, movedPiece, destinationCoordinate);
           this.castleRook=castleRook;
           this.castleRookStart=castleRookStart;
           this.castleRookDestination=castleRookDestination;
        }   
         public Rook getCastleRook(){
             return this.castleRook;
         }
         
         @Override
         public boolean isCastlingMove(){
             return true;
         }
         
          @Override
          public Board execute(){
              
              final Builder builder = new Builder();
             for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece) ){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece); 
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            //i have to make FALSE THE CASTLE (cause We can do again and again) o constructoras isws ftiaksei
            builder.setPiece(new Rook(this.castleRook.getPieceAlliance(),this.castleRookDestination));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
                
            return builder.build();
           
          }
    }
        
          public static final class KingSideCastleMove extends CastleMove{
    
        public KingSideCastleMove(final Board board,final Piece movedPiece,final int destinationCoordinate,
                final Rook castleRook,final int castleRookStart,final int castleRookDestination ) {
            super(board, movedPiece, destinationCoordinate,castleRook,castleRookStart,castleRookDestination);
        }
        
        @Override
        public String toString(){
            return "O-O";
        }

      }
          
          public static final class QueenSideCastleMove extends CastleMove{
    
        public QueenSideCastleMove(final Board board,final Piece movedPiece,final int destinationCoordinate,
                 final Rook castleRook,final int castleRookStart,final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate,castleRook,castleRookStart,castleRookDestination);
        }
        
         @Override
        public String toString(){
            return "O-O-o";
        }


      }
          
          public static final class NullMove extends Move{
    
        public NullMove() {
            super(null,null,-1); // des ton parent ( papou) 
        }
        
        @Override
        public Board execute(){
            throw new RuntimeException("Cannot execute the null move ");
        }

      }
          
          public static class  MoveFactory{
              
              private MoveFactory(){
                  throw new RuntimeException("Not instantiable");
              }
              
              public static Move createMove(final Board board, final int currentCoordinate,
                      final int destinationCoordinate){
                  
                  for (final Move move : board.getAllLegalMoves()){
                      if(move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate()
                              == destinationCoordinate){
                          return move;
                      }
                  }
                  return NULL_MOVE;
              }
              
          }
    
}

