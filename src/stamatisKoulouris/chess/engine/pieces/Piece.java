
package stamatisKoulouris.chess.engine.pieces;


import java.util.Collection;
import stamatisKoulouris.chess.engine.Alliance;


import stamatisKoulouris.chess.engine.board.Board;
import stamatisKoulouris.chess.engine.board.Move;



/**
 *
 * @author Stamatis Koulouris
 */
public abstract class Piece {
    
    protected final PieceType pieceType;
    protected final int piecePosition;// x , y
    protected final Alliance pieceAlliance;//white or black piece?
    
    
    
    
    protected final boolean isFirstMove;
    private final int cachedHashCode;
            
    Piece (final int piecePosition,final Alliance pieceAlliance,final PieceType pieceType,final boolean isFirstMove){
        this.piecePosition=piecePosition;
        this.pieceAlliance=pieceAlliance;
        this.isFirstMove=isFirstMove;
        this.pieceType=pieceType;
        this.cachedHashCode=computeHashCode();
    }
    
    
    private int computeHashCode() {
       int result = pieceType.hashCode();
    result=31 * result + pieceAlliance.hashCode();
    result=31 * result + piecePosition;
    result=31 * result + (isFirstMove ? 1 : 0 );
    
     return result;
    }
    
    public PieceType getPieceType(){
        return this.pieceType;
    }
    
    public int getPiecePosition(){
        return this.piecePosition;
    }
    
    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }
    
    public boolean isFirstMove(){
        return this.isFirstMove;
    }
    
    @Override
    public boolean equals(final Object other){
        if(this == other ){
            return true;
        }
        if(!(other instanceof Piece )){
         return false;
    }
        final Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() && 
                pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove();
    }
    
    @Override
    public int hashCode(){
        return this.cachedHashCode;
    }
    
    
    public abstract Collection<Move> calculateLegalMoves(final Board board);
    public abstract Piece movePiece(Move move);

  
    public enum PieceType{
        
        PAWN("P") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true; // yes you are !!!
            }
        },
        KNIGHT("N") {
          @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
               return false;
            }
        },
        QUEEN("Q") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
               return false;
            }
        },
        KING("K") {
           @Override
            public boolean isKing() {
                return true; // you are a real King :D
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };
        
        private String pieceName;
        
        PieceType(final String pieceName){
        this.pieceName=pieceName;
}
        @Override
        public String toString(){
            return this.pieceName;
        }
        
        public abstract boolean isKing();
        public abstract boolean isRook();
        
    
    }   

}