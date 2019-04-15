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
public class WhitePlayer extends Player {
    
    public WhitePlayer(final Board board, 
            final  Collection<Move> whiteStandarLegalMoves,
            final Collection<Move> blackStandarLegalMoves){
        
        super(board,whiteStandarLegalMoves,blackStandarLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
            return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }
    
     @Override
    public Player getOpponent() { 
        return this.board.whitePlayer();
    } 

    @Override
    protected Collection<Move> calculateKingCastle(Collection<Move> playerLegals, Collection<Move> opponentsLegals) {
        final List<Move> kingCastles = new ArrayList<>();
      
        if(this.playerKing.isFirstMove() && !this.isInCheck()){
            if(!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()){ //one side (King)
                final Tile rookTile = this.board.getTile(63);
                
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    if(Player.calculateAttackOnTile(61, opponentsLegals).isEmpty() && 
                       Player.calculateAttackOnTile(62, opponentsLegals).isEmpty() &&
                           rookTile.getPiece().getPieceType().isRook() ){
                    
                    kingCastles.add(new Move.KingSideCastleMove(this.board,this.playerKing,62,
                            (Rook) rookTile.getPiece(),rookTile.getTileCoordinate(),61)); 
                   
                    }
                    
                }
            } //damas Castle
                if(!this.board.getTile(59).isTileOccupied() && !this.board.getTile(58).isTileOccupied() && 
                   !this.board.getTile(57).isTileOccupied()){
                    
                    final Tile rookTile = this.board.getTile(56);
                    if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() &&
                            Player.calculateAttackOnTile(58, opponentsLegals).isEmpty() &&
                            Player.calculateAttackOnTile(59, opponentsLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()){
                        // CASTLED IT HERE 
                       kingCastles.add(new Move.QueenSideCastleMove(board, playerKing, 58, 
                               (Rook) rookTile.getPiece(),rookTile.getTileCoordinate(), 59));
                    
                }
                    
                }
        }
           return ImmutableList.copyOf(kingCastles);
    }
}
