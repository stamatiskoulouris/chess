/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stamatisKoulouris.chess.engine.player;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import stamatisKoulouris.chess.engine.Alliance;
import stamatisKoulouris.chess.engine.board.Board;
import stamatisKoulouris.chess.engine.board.Move;
import stamatisKoulouris.chess.engine.board.Tile;
import stamatisKoulouris.chess.engine.pieces.Piece;
import stamatisKoulouris.chess.engine.pieces.Rook;

/**
 *
 * @author Stamatis Koulouris
 */
public class BlackPlayer extends Player {
    
     public BlackPlayer(final Board board,final  Collection<Move> whiteStandarLegalMoves ,final Collection<Move>blackStandarLegalMoves ){
         
        super(board,blackStandarLegalMoves, whiteStandarLegalMoves);
        
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
       return Alliance.BLACK;    
    }
    
    @Override
    public Player getOpponent() { 
        return this.board.blackPlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastle(Collection<Move> playerLegals, Collection<Move> opponentsLegals) {
         final List<Move> kingCastles = new ArrayList<>();
      
        if(this.playerKing.isFirstMove() && !this.isInCheck()){
            if(!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()){ //one side (king)
                final Tile rookTile = this.board.getTile(0);
                
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    if(Player.calculateAttackOnTile(5, opponentsLegals).isEmpty() && 
                       Player.calculateAttackOnTile(6, opponentsLegals).isEmpty() &&
                           rookTile.getPiece().getPieceType().isRook() ){
                    
                    kingCastles.add(new Move.KingSideCastleMove(this.board,this.playerKing, 6,
                               (Rook) rookTile.getPiece(),rookTile.getTileCoordinate(), 5)); // I AM TIRED WORK IT ALTER;
                    }
                    
                }
            }// Dama Castle
                if(!this.board.getTile(1).isTileOccupied() && !this.board.getTile(2).isTileOccupied() && 
                   !this.board.getTile(3).isTileOccupied()){
                    
                    final Tile rookTile = this.board.getTile(0);
                    if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() &&
                            Player.calculateAttackOnTile(2, opponentsLegals).isEmpty() &&
                            Player.calculateAttackOnTile(3, opponentsLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()){
                        // CASTLED IT HERE 
                       kingCastles.add(new Move.QueenSideCastleMove(this.board,this.playerKing, 2,
                               (Rook) rookTile.getPiece(),rookTile.getTileCoordinate(), 3));
                    
                }
                    
                }
        }
           return ImmutableList.copyOf(kingCastles);
    
    }
    
    
    
}
