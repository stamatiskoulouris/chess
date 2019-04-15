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
import stamatisKoulouris.chess.engine.board.Move.MajorMove;

/**
 *
 * @author Stamatis Koulouris
 */
public class Pawn extends Piece {

    private final static int [] CANDIDATE_MOVE={8,16,7,9}; // 8 || 16 straight , 7 || 9 diagnal
    
    public Pawn(final Alliance pieceAlliance ,final int piecePosition) {
        super(piecePosition, pieceAlliance,PieceType.PAWN,true);
    }
    
      public Pawn(final Alliance pieceAlliance ,final int piecePosition,final boolean isFirstMove) {
        super(piecePosition, pieceAlliance,PieceType.PAWN,isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        
       final List<Move> legalMoves=new ArrayList<>();
       
       for(final int currentCandidateOffset : CANDIDATE_MOVE){
         
           final int candidateDestinationCoordinate= this.piecePosition + (this.pieceAlliance.getDirection() * currentCandidateOffset);
           //an einai White tote -1 * 8 or Black 1 * 8 
           
           if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
               continue;
           }
           
           if(currentCandidateOffset == 8 && ! board.getTile(candidateDestinationCoordinate).isTileOccupied()){
               // WORK MORE HERE Promotions
               legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));
           }
           
           else if(currentCandidateOffset == 16 && this.isFirstMove() && 
                   ((BoardUtils.SEVENTH_RANK[this.piecePosition]  && this.getPieceAlliance().isBlack()) ||
                   (BoardUtils.SECOND_RANK[this.piecePosition] && this.getPieceAlliance().isWhite()))){
                  
               final int behindCandidateDestinationCoordinate = this.piecePosition+(this.pieceAlliance.getDirection() * 8); // an einai blockarismenos  
               
               if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&  //poy einai to Pawn kai an borei na proxorisei ( yparxei allo Piece brwsta?)
                  !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                   
                   legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));
               } 
           } else if(currentCandidateOffset == 7 &&
               !((BoardUtils.EIGHT_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
                (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))){  // attack out of board diagnal
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                        if(this.pieceAlliance!= pieceOnCandidate.getPieceAlliance()){
                            // Kane to attack edw 
                            legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));
                        }
                }
                
               
           } else if(currentCandidateOffset == 9 &&
                   !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
                (BoardUtils.EIGHT_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))){ // attack out of board diagnal
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                        if(this.pieceAlliance!= pieceOnCandidate.getPieceAlliance()){
                            // Kane to attack edw 
                            legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));
                        }
                }
           }
               
       }
       
       
        return ImmutableList.copyOf(legalMoves);
    }
    
     @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getMovedPiece().getPieceAlliance(),move.getDestinationCoordinate());
    }
    
}
