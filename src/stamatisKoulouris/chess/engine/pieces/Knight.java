
package stamatisKoulouris.chess.engine.pieces;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import stamatisKoulouris.chess.engine.Alliance;
import stamatisKoulouris.chess.engine.board.Board;
import stamatisKoulouris.chess.engine.board.BoardUtils;
import stamatisKoulouris.chess.engine.board.Move;
import stamatisKoulouris.chess.engine.board.Move.AttackMove;
import stamatisKoulouris.chess.engine.board.Move.MajorMove;
import stamatisKoulouris.chess.engine.board.Tile;

/**
 *
 * @author Stamatis Koulouris
 */
public class Knight extends Piece{

    private final static int[] CANDITATE_MOVE_COORDINATES = { -17,-15,-10,-6,6,10,15,17}; // efoson to piece einai kapou sto board 64 tiles 
    
    public Knight(final Alliance pieceAlliance ,final int piecePosition) {
        super(piecePosition, pieceAlliance,PieceType.KNIGHT,true);
    }
    
     public Knight(final Alliance pieceAlliance ,final int piecePosition,final boolean isFirstMove) {
        super(piecePosition, pieceAlliance,PieceType.KNIGHT,isFirstMove);
    }
    
    
    
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        
        
       final List<Move>legalMoves=new ArrayList<>();
        
        for (final int currentCandidateOffset : CANDITATE_MOVE_COORDINATES){
       
      final int candidateDestinationCoordinate=this.piecePosition + currentCandidateOffset;
            
            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){/*einai valid i kinish? or out of bounds?*/
               
                try{
                if(isFirstColumnExclution(this.piecePosition,currentCandidateOffset) || 
                        isSecondColumnExclution(this.piecePosition, currentCandidateOffset) ||
                        isSevenColumnExclution(this.piecePosition, currentCandidateOffset)  || 
                        isEightColumnExclution(this.piecePosition, currentCandidateOffset)) {
                    continue ;
                }
                }
                catch(ArrayIndexOutOfBoundsException e){}
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                
                if(!candidateDestinationTile.isTileOccupied()){
                    
                    legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));
                } 
                else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    
                     if(this.pieceAlliance != pieceAlliance){ // an einai exthros or alliance
                         legalMoves.add(new AttackMove(board,this,candidateDestinationCoordinate,pieceAtDestination));
                     }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
    
    
    
     @Override
    public String toString(){
        return PieceType.KNIGHT.toString();
    }
    
    
    private static boolean isFirstColumnExclution(final int currentPosition,final int candidateOffset)throws ArrayIndexOutOfBoundsException{// otan o Knight einai stin gwnia A ,A+1,H-1,H
      return  BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset== -17 || candidateOffset== -10 || 
              candidateOffset== 6 || candidateOffset == 15);
        
    }
    
    private static boolean isSecondColumnExclution(final int currentPosition,final int candidateOffset )throws ArrayIndexOutOfBoundsException{
        return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset== -10  || candidateOffset== 6);
    }
    
    private static boolean isSevenColumnExclution(final int currentPosition,final int candidateOffset )throws ArrayIndexOutOfBoundsException{
        return BoardUtils.SEVEN_COLUMN[currentPosition] && (candidateOffset== -6 || candidateOffset== 10);
    }
    
      
    private static boolean isEightColumnExclution(final int currentPosition,final int candidateOffset )throws ArrayIndexOutOfBoundsException{
        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffset== -15 || candidateOffset== -6 ||
                candidateOffset== 10 || candidateOffset== 17);
    }

    @Override
    public Knight movePiece(Move move) {
        return new Knight(move.getMovedPiece().getPieceAlliance(),move.getDestinationCoordinate());
    }
}
