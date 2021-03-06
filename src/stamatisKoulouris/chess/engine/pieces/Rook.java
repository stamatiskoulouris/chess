
package stamatisKoulouris.chess.engine.pieces;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import stamatisKoulouris.chess.engine.Alliance;
import stamatisKoulouris.chess.engine.board.Board;
import stamatisKoulouris.chess.engine.board.BoardUtils;
import stamatisKoulouris.chess.engine.board.Move;
import stamatisKoulouris.chess.engine.board.Tile;

/**
 *
 * @author Stamatis Koulouris
 */
public class Rook extends Piece {

     private final static int [] CANDIDATE_MOVE_VECTOR_COORDINATES={-8,-1,1,8};
    
    public Rook(final Alliance pieceAlliance ,final int piecePosition) {
        super(piecePosition, pieceAlliance,PieceType.ROOK,true);
    }

    public Rook(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove){
        super(piecePosition, pieceAlliance,PieceType.ROOK,isFirstMove);
    }
 @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        
         final List<Move> legalMoves = new ArrayList<>();
         
         for(final int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES){
          
             int candidateDestinationCoordinate=this.piecePosition;
             
             while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                 candidateDestinationCoordinate += candidateCoordinateOffset;
                 try{
                 if( isFirstColumnExclution(candidateDestinationCoordinate,candidateCoordinateOffset) ||
                     isEightColumnExclution(candidateDestinationCoordinate,candidateCoordinateOffset)){
                     break;
                 }
                 }
                 catch(ArrayIndexOutOfBoundsException e){}
                 if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                        final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                
                if(!candidateDestinationTile.isTileOccupied()){
                    
                    legalMoves.add(new Move.MajorMove(board,this,candidateDestinationCoordinate));
                } 
                else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    
                     if(this.pieceAlliance != pieceAlliance){ // an einai exthros or alliance
                         legalMoves.add(new Move.AttackMove(board,this,candidateDestinationCoordinate,pieceAtDestination));
                     }
                        break; //loop;
                }
                
             
                
                 }
             }
         }
        return ImmutableList.copyOf(legalMoves);
    }
    
     @Override
    public String toString(){
        return PieceType.ROOK.toString();
    }
    
     private static boolean isFirstColumnExclution(final int currentPosition,final int candidateOffset)throws ArrayIndexOutOfBoundsException{// otan o Knight einai stin gwnia A ,A+1,H-1,H
     
        
         return  BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1);
                
     }
    
      
    private static boolean isEightColumnExclution(final int currentPosition,final int candidateOffset )throws ArrayIndexOutOfBoundsException{
         
        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffset == 1);
          
         
             
          
    }

    @Override
    public Rook movePiece(Move move) {
       return new Rook(move.getMovedPiece().getPieceAlliance(),move.getDestinationCoordinate());
    }
    
}

