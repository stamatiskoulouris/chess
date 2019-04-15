/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stamatisKoulouris.chess.engine.pieces;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import stamatisKoulouris.chess.engine.Alliance;
import stamatisKoulouris.chess.engine.board.Board;
import stamatisKoulouris.chess.engine.board.Move;
import java.util.ArrayList;
import stamatisKoulouris.chess.engine.board.BoardUtils;
import stamatisKoulouris.chess.engine.board.Tile;

/**
 *
 * @author Stamatis Koulouris
 */
public class Bishop extends Piece{

    private final static int [] CANDIDATE_MOVE_VECTOR_COORDINATES={-9,-7,7,9};
    
    public Bishop(final Alliance pieceAlliance ,final int piecePosition) {
        super(piecePosition, pieceAlliance,PieceType.BISHOP,true);
    }
    
     public Bishop(final Alliance pieceAlliance ,final int piecePosition ,final boolean isFirstMove) {
        super(piecePosition, pieceAlliance,PieceType.BISHOP,isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        
         final List<Move> legalMoves = new ArrayList<>();
         for(final int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES){
             
             int candidateDestinationCoordinate=this.piecePosition;
             
             while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                 candidateDestinationCoordinate += candidateCoordinateOffset;
                 
              try{
                 if(isFirstColumnExclution(candidateDestinationCoordinate,candidateCoordinateOffset)||
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
        return PieceType.BISHOP.toString();
    }
    
     private static boolean isFirstColumnExclution(final int currentPosition,final int candidateOffset)throws ArrayIndexOutOfBoundsException{// otan o Knight einai stin gwnia A ,A+1,H-1,H
      return  BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset== -9 || candidateOffset== 7);
                     
     }
    
      
    private static boolean isEightColumnExclution(final int currentPosition,final int candidateOffset ){
        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffset== -7 || candidateOffset== 9);
                
    }

    @Override
    public Bishop movePiece(Move move) {
        return new Bishop(move.getMovedPiece().getPieceAlliance(),move.getDestinationCoordinate());
    }
    
}
