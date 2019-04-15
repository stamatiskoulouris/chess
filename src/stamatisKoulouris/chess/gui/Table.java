/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stamatisKoulouris.chess.gui;

import com.google.common.collect.Lists;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import static javax.swing.SwingUtilities.isRightMouseButton;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

import stamatisKoulouris.chess.engine.board.Board;
import stamatisKoulouris.chess.engine.board.BoardUtils;
import stamatisKoulouris.chess.engine.board.Move;
import stamatisKoulouris.chess.engine.board.Tile;
import stamatisKoulouris.chess.engine.pieces.Piece;
import stamatisKoulouris.chess.engine.player.MoveTransition;


/**
 *
 * @author Stamatis Koulouris
 */
public class Table  {
    
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private  Board chessBoard ;
   
    
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;
     
    private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");
    
    private static String PiecesImagePath = "src\\piecesIcons\\";
    
    
    private static  final Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350); //400 , 350
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
    
    
    public Table(){
        
        this.gameFrame=new JFrame("JChess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar();
       
        
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.chessBoard=Board.createStandarBoard();
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
      
        this.boardDirection=BoardDirection.NORMAL;
        this.boardPanel=new BoardPanel();
        this.gameFrame.add(this.boardPanel,BorderLayout.CENTER);
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.gameFrame.setVisible(true);
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar=new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu= new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                System.out.println("open pgn files");
            }
        });
        
        fileMenu.add(openPGN);
        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                
               System.exit(0);
            }
        });  
        fileMenu.add(exitMenuItem);
         return fileMenu;
        }
    
    private JMenu createPreferencesMenu() {
        final JMenu preferencesMenu = new JMenu("Preferences");
        final JMenuItem flipBoardMenuItem = new JMenu("Flip Board");
        flipBoardMenuItem.addActionListener(new ActionListener (){
            @Override
            public void actionPerformed( ActionEvent e) {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);
            }
            
        });
        
        preferencesMenu.add(flipBoardMenuItem);
        return preferencesMenu;
    }
    
   public enum BoardDirection { 
       NORMAL {
           @Override 
           List<TilePanel> traverse (final List<TilePanel> boardTiles){
               return boardTiles;
           }
           
           @Override
           BoardDirection opposite(){
               return FLIPPED;
           }
       },
       
       FLIPPED{
           @Override
           List<TilePanel> traverse(final List<TilePanel> boardTiles){
               return Lists.reverse(boardTiles);
           }
           @Override
           BoardDirection opposite(){
               return NORMAL;
           }
       };
       abstract List<TilePanel> traverse (final List<TilePanel> boardTiles);
        abstract BoardDirection opposite();
   }
       
    
    private class BoardPanel extends JPanel{
    final List<TilePanel> boardTiles;
    
    BoardPanel(){
        super(new GridLayout(8,8));
        this.boardTiles = new ArrayList<>();
        
        for(int i = 0; i<BoardUtils.NUM_TILES; i ++ ){
            final TilePanel tilePanel = new TilePanel(this,i);
            this.boardTiles.add(tilePanel);
            add(tilePanel);
        }
        setPreferredSize(BOARD_PANEL_DIMENSION);
        validate();
    }
    
    public void drawBoard(final Board board){
        removeAll();
        for(final TilePanel tilePanel : boardDirection.traverse(boardTiles)){
                tilePanel.drawTile(board);
                add(tilePanel);
        }
        
       validate();
        repaint();
         
    }
}
  
    
    private class TilePanel extends JPanel{
    
            private final int tileId;
            
            TilePanel(final BoardPanel boardPanel,final int tileId){
              super(new GridBagLayout());
              
              this.tileId=tileId;
              
              setPreferredSize(TILE_PANEL_DIMENSION);
              assignTileColor();
              assignTilePieceIcon(chessBoard);
              
          addMouseListener(new MouseListener() {
           @Override
           public void mouseClicked(final MouseEvent e){ 
            
               if(isRightMouseButton(e)){
                   
                 sourceTile = null ;
                 destinationTile=null;
                 humanMovedPiece = null;
                     System.out.print("i Press Right Click");
                    }else if (isLeftMouseButton(e)) {
                        
                        if(sourceTile == null){//fisrt click
                        sourceTile = chessBoard.getTile(tileId);
                        humanMovedPiece = sourceTile.getPiece();
                         System.out.print("i Press Left Click 1");
                        if(humanMovedPiece == null ) {
                            sourceTile = null;
                        }
              
                    } 
                  
                  else {
                         destinationTile = chessBoard.getTile(tileId);
                          final Move move = Move.MoveFactory.createMove(chessBoard,sourceTile.getTileCoordinate()
                                  , destinationTile.getTileCoordinate());
                           System.out.print("i Press Left Click 2");
                        final  MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                          if(transition.getMoveStatus().isDone()){
                              chessBoard = transition.getTransionBoard();
                              //so far so good   
                          }
                          
                          sourceTile = null;
                          destinationTile = null;
                          humanMovedPiece = null; 
                         
                        }
                        
                         // i should invoke here?
                        SwingUtilities.invokeLater(new Runnable(){
                            @Override
                            public void run() {
                                boardPanel.drawBoard(chessBoard);
                                
                            }
                            
                        });
                        
                  }
              }   

                  @Override
                  public void mousePressed(MouseEvent e) {
                  
                  }

                  @Override
                  public void mouseReleased(MouseEvent e) {
                      
                  }

                  @Override
                  public void mouseEntered(MouseEvent e) {
                      
                  }

                  @Override
                  public void mouseExited(MouseEvent e) {
           
                  }

              
                  
              });
          
          
                validate();
               
              }
            
            public void drawTile(final Board board){
                assignTileColor();
                assignTilePieceIcon(board);
                validate();
                repaint();
                        
            }
         
            
        private void assignTilePieceIcon(final Board board) {
                this.removeAll();
                if(board.getTile(tileId).isTileOccupied()){
                   
                    final BufferedImage image;
                   
                    try {
                        image = ImageIO.read(
                                new File( PiecesImagePath +
                                        board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0,1)
                                        + board.getTile(this.tileId).getPiece().toString() + ".gif"));
                                    add(new JLabel(new ImageIcon(image)));
                    } catch (IOException ex) {
                       ex.printStackTrace();
                    }
                
                    
                }
        }
            
        private void assignTileColor() {
            if(BoardUtils.EIGHTH_RANK[this.tileId] ||
               BoardUtils.SIXTH_RANK[this.tileId] ||
               BoardUtils.FORTH_RANK[this.tileId] ||
               BoardUtils.SECOND_RANK[this.tileId] ){
     
                setBackground(this.tileId % 2  == 0 ? lightTileColor : darkTileColor);
            } 
            else if (BoardUtils.SEVENTH_RANK[this.tileId] ||
                    BoardUtils.FIFTH_RANK[this.tileId] ||
                    BoardUtils.THIRD_RANK[this.tileId] ||
                    BoardUtils.FIRST_RANK[this.tileId]) {
             setBackground(this.tileId % 2  != 0 ? lightTileColor : darkTileColor);
                
            }
                    
            
        }
                
        
}
    
    }
    
    

