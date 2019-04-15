
package stamatisKoulouris.chess.engine.board;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import stamatisKoulouris.chess.engine.Alliance;
import stamatisKoulouris.chess.engine.pieces.*;
import stamatisKoulouris.chess.engine.player.BlackPlayer;
import stamatisKoulouris.chess.engine.player.Player;
import stamatisKoulouris.chess.engine.player.WhitePlayer;

/**
 *
 * @author Stamatis Koulouris
 */
public class Board {
    
    private final List<Tile> gameBoard;
    private final Collection <Piece> whitePieces;
    private final Collection <Piece> blackPieces;
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;
    
    private Board(final Builder builder){
        this.gameBoard = createGameBoard(builder);
        this.whitePieces=calculateActivePieces(this.gameBoard,Alliance.WHITE);
         this.blackPieces=calculateActivePieces(this.gameBoard,Alliance.BLACK);
         
         //tracking 
    final Collection<Move> whiteStandarLegalMoves=calculateLegalMoves(this.whitePieces); //wtf i did WRONG
    final Collection<Move> blackStandarLegalMoves=calculateLegalMoves(this.blackPieces); //same here 
         
    this.whitePlayer=new WhitePlayer(this,whiteStandarLegalMoves,blackStandarLegalMoves);
    this.blackPlayer=new BlackPlayer(this,whiteStandarLegalMoves,blackStandarLegalMoves);
    
    this.currentPlayer=builder.nextMoveMaker.choosePlayer(this.whitePlayer,this.blackPlayer);
    }
    
  
    
    @Override
    public String toString(){
        final StringBuilder builder = new StringBuilder();
        
        for ( int i = 0 ; i < BoardUtils.NUM_TILES; i++){
            final String tileText=this.gameBoard.get(i).toString();
            builder.append(String.format("%3s",tileText));
            if(( i + 1) % BoardUtils.NUM_TILES_ROW == 0){
                builder.append("\n");
            }
        }
        return builder.toString();
    }
    
    public Player currentPlayer(){
        return this.currentPlayer;
    }
    
    public Player whitePlayer(){
        return this.whitePlayer; 
    }
    
    public Player blackPlayer(){
        return this.blackPlayer; 
    }
       
    public Collection<Piece> getBlackPieces(){
        return this.blackPieces;
    }
    
     public Collection<Piece> getWhitePieces(){
        return this.whitePieces;
    }
    
    private  Collection<Move> calculateLegalMoves(final Collection<Piece> pieces){
        
        final List<Move> legalMoves= new ArrayList<>();
        
        
        for(final Piece piece : pieces){
        legalMoves.addAll(piece.calculateLegalMoves(this)); // same of piece 
         }
        return ImmutableList.copyOf(legalMoves);
    }
    
    
    //katagrafi energwn Pieces sto board ( bwri na thelei Veltiwsh (tsekare to sto Testing));
    public static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard,final Alliance alliance){ 
        
        final List<Piece> activePieces = new ArrayList<>();
        
        for(final Tile tile : gameBoard){
            if(tile.isTileOccupied()){
                final Piece piece = tile.getPiece();
                    if(piece.getPieceAlliance()==alliance){
                        activePieces.add(piece);
                    }
            }
        }
        
        return ImmutableList.copyOf(activePieces);
    }
    

    public Tile getTile(final int tileCoordinate) {
        return gameBoard.get(tileCoordinate);
    }
    
    //melodikh Skepsi px setPiece(3) , new Queen
    
    public static List<Tile> createGameBoard(final Builder builder){
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES]; // 0 - 64
        
        for (int i = 0; i<BoardUtils.NUM_TILES; i++){
            tiles[i]=Tile.createTile(i,builder.boardConfiguration.get(i));  // Mapping Pieces to id's  
        }
        return ImmutableList.copyOf(tiles);
    }
    
 
    
    public static Board createStandarBoard(){
        
        final Builder builder = new Builder();
        
        
        //Black first cause 0 - 15
        builder.setPiece(new Rook(Alliance.BLACK,0));
        builder.setPiece(new Knight(Alliance.BLACK,1));
        builder.setPiece(new Bishop(Alliance.BLACK,2));
        builder.setPiece(new Queen(Alliance.BLACK,3));
        builder.setPiece(new King(Alliance.BLACK,4));
        builder.setPiece(new Bishop(Alliance.BLACK,5));
        builder.setPiece(new Knight(Alliance.BLACK,6));
        builder.setPiece(new Rook(Alliance.BLACK,7));
        builder.setPiece(new Pawn(Alliance.BLACK,8));
        builder.setPiece(new Pawn(Alliance.BLACK,9));
        builder.setPiece(new Pawn(Alliance.BLACK,10));
        builder.setPiece(new Pawn(Alliance.BLACK,11));
        builder.setPiece(new Pawn(Alliance.BLACK,12));
        builder.setPiece(new Pawn(Alliance.BLACK,13));
        builder.setPiece(new Pawn(Alliance.BLACK,14));
        builder.setPiece(new Pawn(Alliance.BLACK,15));
        
        
         // White 48 - 63 cound and you will find out why (0-63) Board size
         //Black first cause 0 - 15
        builder.setPiece(new Pawn(Alliance.WHITE,48));
        builder.setPiece(new Pawn(Alliance.WHITE,49));
        builder.setPiece(new Pawn(Alliance.WHITE,50));
        builder.setPiece(new Pawn(Alliance.WHITE,51));
        builder.setPiece(new Pawn(Alliance.WHITE,52));
        builder.setPiece(new Pawn(Alliance.WHITE,53));
        builder.setPiece(new Pawn(Alliance.WHITE,54));
        builder.setPiece(new Pawn(Alliance.WHITE,55));
        builder.setPiece(new Rook(Alliance.WHITE,56));
        builder.setPiece(new Knight(Alliance.WHITE,57));
        builder.setPiece(new Bishop(Alliance.WHITE,58));
        builder.setPiece(new Queen(Alliance.WHITE,59));
        builder.setPiece(new King(Alliance.WHITE,60));
        builder.setPiece(new Bishop(Alliance.WHITE,61));
        builder.setPiece(new Knight(Alliance.WHITE,62));
        builder.setPiece(new Rook(Alliance.WHITE,63));
        
        builder.setMoveMaker(Alliance.WHITE);
        
        return builder.build();
        
    }

    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(),
                                                              this.blackPlayer.getLegalMoves())); // guava Magic
    }
    
    public static class Builder{
        
        Map<Integer,Piece> boardConfiguration;
        Alliance nextMoveMaker;
        Pawn enPassantPawn;
        
        public Builder(){
        this.boardConfiguration=new HashMap<>();
        }
        
        public Builder setPiece(final Piece piece){
            this.boardConfiguration.put(piece.getPiecePosition(), piece);
            return this;
        }
        
        public Builder setMoveMaker(final Alliance nextMoveMaker){
            this.nextMoveMaker=nextMoveMaker;
            return this;
        }
        
        
        public Board build(){
            return new Board(this);
        }

      public void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn=enPassantPawn;
        }
        
    }
    
}
