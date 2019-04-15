/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class King extends Piece{

    private final static int [] CANDIDATE_MOVE={-9,-8,-7,-1,1,7,8,9}; // KING MOVES
    
    public King(final Alliance pieceAlliance ,final int piecePosition) {
        super(piecePosition, pieceAlliance,PieceType.KING,true);
    }
     
    public King(final Alliance pieceAlliance ,final int piecePosition,final boolean isFirstMove) {
        super(piecePosition, pieceAlliance,PieceType.KING,isFirstMove);
    }
    

    @Override
    public Collection<Move> calculateLegalMoves(Board board){
        
        final List<Move> legalMoves = new ArrayList<>();
       
        
        for(final int currentCandidateOffset : CANDIDATE_MOVE ){
            
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            
            try{
            if(isFirstColumnExclution(this.piecePosition,currentCandidateOffset) ||
               isEightColumnExclution(this.piecePosition,currentCandidateOffset) ){
                
                continue;
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
                }
                
                
            }
            
        }
        
        return ImmutableList.copyOf(legalMoves);
    }
    
    
     @Override
    public String toString(){
        return PieceType.KING.toString();
    }
    
     private static boolean isFirstColumnExclution(final int currentPosition,final int candidateOffset)throws ArrayIndexOutOfBoundsException{// otan o Knight einai stin gwnia A ,A+1,H-1,H
      return  BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset== -9 || candidateOffset== -1 || 
              candidateOffset== 7);
        
    }
    
    private static boolean isEightColumnExclution(final int currentPosition,final int candidateOffset )throws ArrayIndexOutOfBoundsException{
        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffset== -7  || candidateOffset== 1 || candidateOffset== 9);
    }

    @Override
    public King movePiece(Move move) {
         return new King(move.getMovedPiece().getPieceAlliance(),move.getDestinationCoordinate());
        }
    
}
