

package stamatisKoulouris.chess.engine.board;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;


import stamatisKoulouris.chess.engine.pieces.Piece;

/**
 *
 * @author Stamatis Koulouris
 */
public abstract class Tile {

   
    
    protected final int tileCoordinate; // protected mono gia SubClasses , final dimiougia MONO STON CUNSTRUCTORA
    //feels safe for me , alla ta Unitest tha to kathorisoun;
    private static final Map<Integer,EmptyTile> EMPTY_TILES_CASHE=createaAllPossibleEmptyTiles(); // BOARD 64 tiles
     
    private static Map<Integer, EmptyTile> createaAllPossibleEmptyTiles() {
       final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
       
       for(int i =0 ; i < BoardUtils.NUM_TILES; i++){
           emptyTileMap.put(i,new EmptyTile(i));
       }
      // Collections.unmodifiableMap(emptyTileMap); doulevei !!!
       return ImmutableMap.copyOf(emptyTileMap);//DOULEVEI KALHTERA!!!!
    }
    
    public static Tile createTile(final int tileCoordinate,final Piece piece){
        return piece != null ? new OccupiedTile(tileCoordinate,piece) : EMPTY_TILES_CASHE.get(tileCoordinate);
    }
    
    private Tile(final int tileCoordinate){
        this.tileCoordinate=tileCoordinate;
    }
    
    public abstract boolean isTileOccupied(); // an ena plakidio einai se xrhsh or not
    
    public abstract Piece getPiece(); 
    
    public int getTileCoordinate(){
        return this.tileCoordinate;
    }
    
    public static final class EmptyTile extends Tile{
        private EmptyTile(int coordinate){
            super(coordinate);
        }
            
            @Override
            public String toString(){
                return "-";
            }
        
            @Override
            public boolean isTileOccupied() {
                return false;
            }
            
            @Override
            public Piece getPiece(){
                return null;
        }
            
     }
    
    public static final class OccupiedTile extends Tile {
        
       private final Piece pieceOnTile; //immutable
       
      private  OccupiedTile(int tileCoordinate,final Piece pieceOnTile){
            super(tileCoordinate);
            this.pieceOnTile=pieceOnTile;
        }
        
      
         //black == lower Case || white == uperCase;
        @Override
            public String toString(){
                return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase() :
                        getPiece().toString();
                           }
      
       @Override
       public boolean isTileOccupied(){
           return true;
       }
       
        @Override
        public Piece getPiece(){
            return this.pieceOnTile;
        }
       
    }
    
    
    }
