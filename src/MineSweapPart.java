// Connor Nethen
// MineSweaper Game

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MineSweapPart extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static final int WINDOW_HEIGHT = 760;
	private static final int WINDOW_WIDTH = 760;
	private static final int TOTAL_MINES = 25; // Number of mines placed on grid (can be changed)
  
	private static int guessedMinesLeft = TOTAL_MINES; // Number of flags left to place (can be < 0)
	private static int actualMinesLeft = TOTAL_MINES; // Number of flags placed on correct cells (with mines)

	private static final String INITIAL_CELL_TEXT = "";
	private static final String UNEXPOSED_FLAGGED_CELL_TEXT = "@";
    private static final String EXPOSED_MINE_TEXT = "M";
  
    // visual indication of an exposed MyJButton
    private static final Color EXPOSED_CELL_BACKGROUND_COLOR = Color.lightGray;
    // colors used when displaying the getStateStr() String
    private static final Color EXPOSED_CELL_FOREGROUND_COLOR_MAP[] = {Color.lightGray, Color.blue, Color.green, Color.cyan, Color.yellow, 
                                           Color.orange, Color.pink, Color.magenta, Color.red, Color.red};

  
    // holds the "number of mines in perimeter" value for each MyJButton 
    private static final int MINEGRID_ROWS = 16;
    private static final int MINEGRID_COLS = 16;
    private int[][] mineGrid = new int[MINEGRID_ROWS][MINEGRID_COLS];
    private int[] mineLocations = new int[TOTAL_MINES];
    private static int cellsToUncover = (MINEGRID_ROWS*MINEGRID_COLS)-TOTAL_MINES; // Number of cells to be uncovered to complete game

    private static final int NO_MINES_IN_PERIMETER_MINEGRID_VALUE = 0;
    private static final int ALL_MINES_IN_PERIMETER_MINEGRID_VALUE = 8;
    private static final int IS_A_MINE_IN_MINEGRID_VALUE = 9;
  
    private boolean running = true;
    private boolean gameStatus = true;
    private boolean flag = false;
  
    public MineSweapPart()
    {
    	this.setTitle("MineSweap                                                         " + 
                  MineSweapPart.guessedMinesLeft +" Mines left");
    	this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    	this.setResizable(false);
    	this.setLayout(new GridLayout(MINEGRID_ROWS, MINEGRID_COLS, 0, 0));
    	this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    	// set the grid of MyJbuttons
    	this.createContents();
    
    	// place MINES number of mines in sGrid and adjust all of the "mines in perimeter" values
    	this.setMines();
    
    	this.setVisible(true);
    }

    public void createContents()
    {
    	for (int mgr = 0; mgr < MINEGRID_ROWS; ++mgr)
    	{  
    		for (int mgc = 0; mgc < MINEGRID_COLS; ++mgc)
    		{  
    			// set sGrid[mgr][mgc] entry to 0 - no mines in it's perimeter
    			this.mineGrid[mgr][mgc] = NO_MINES_IN_PERIMETER_MINEGRID_VALUE; 
        
    			// create a MyJButton that will be at location (mgr, mgc) in the GridLayout
    			MyJButton but = new MyJButton(INITIAL_CELL_TEXT, mgr, mgc); 
        
    			// register the event handler with this MyJbutton
    			but.addActionListener(new MyListener());
        
    			// add the MyJButton to the GridLayout collection
    			this.add(but);
    		}  
    	}
    }


    // begin nested private class
    private class MyListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent event)
    	{
    		if ( running )
    		{
    			// used to determine if ctrl or alt key was pressed at the time of mouse action
    			int mod = event.getModifiers();
    			MyJButton mjb = (MyJButton)event.getSource();
        
    			// is the MyJbutton that the mouse action occurred in flagged
    			boolean flagged = mjb.getText().equals(MineSweapPart.UNEXPOSED_FLAGGED_CELL_TEXT);
        
    			// is the MyJbutton that the mouse action occurred in already exposed
    			boolean exposed = mjb.getBackground().equals(EXPOSED_CELL_BACKGROUND_COLOR);
       
    			// flag a cell : ctrl + left click
    			if ( !flagged && !exposed && (mod & ActionEvent.CTRL_MASK) != 0 )
    			{
    				mjb.setText(MineSweapPart.UNEXPOSED_FLAGGED_CELL_TEXT);
    				--MineSweapPart.guessedMinesLeft;
          
    				// if the MyJbutton that the mouse action occurred in is a mine
    				if ( mineGrid[mjb.ROW][mjb.COL] == IS_A_MINE_IN_MINEGRID_VALUE )
    				{
    					--MineSweapPart.actualMinesLeft;
    					if (MineSweapPart.actualMinesLeft == 0 && MineSweapPart.guessedMinesLeft == 0)
    					{
    						flag = true; // Win condition can be met ...
    					}
    					else
    					{
    						flag = false; // Win cannot take place
    					}
    				}
    				setTitle("MineSweap                                                         " + 
    						MineSweapPart.guessedMinesLeft +" Mines left");
    			}
       
    			// unflag a cell : alt + left click
    			else if ( flagged && !exposed && (mod & ActionEvent.ALT_MASK) != 0 )
    			{
    				mjb.setText(INITIAL_CELL_TEXT);
    				++MineSweapPart.guessedMinesLeft;
          
    				// if the MyJbutton that the mouse action occurred in is a mine
    				if ( mineGrid[mjb.ROW][mjb.COL] == IS_A_MINE_IN_MINEGRID_VALUE )
    				{
    					++MineSweapPart.actualMinesLeft;
    					if (MineSweapPart.actualMinesLeft == 0 && MineSweapPart.guessedMinesLeft == 0)
    					{
    						flag = true; // Win condition can be met ...
    					}
    					else
    					{
    						flag = false; // Win cannot take place
    					}
    				}
    				else
    				{
    					if (MineSweapPart.actualMinesLeft == 0 && MineSweapPart.guessedMinesLeft == 0)
    					{
    						flag = true; // Win condition can be met ...
    					}
    					else
    					{
    						flag = false; // Win cannot take place
    					}
    				}
    				setTitle("MineSweap                                                         " + 
    						MineSweapPart.guessedMinesLeft +" Mines left");
    			}
    			
    			// expose a cell : left click
    			else if ( !flagged && !exposed )
    			{
    				exposeCell(mjb);
    			}
    		}
    		
    		if (flag) // Check to see if all remaining cells are uncovered
    		{
    			if (MineSweapPart.cellsToUncover == 0)
    				running = false;
    		}
    		
    		if (!running)
    		{
    			if (gameStatus) // If user wins game
    				setTitle("MineSweap                                                         " +  "Congratulations, You Win!");
    			else // If user loses game
    				setTitle("MineSweap                                                         " +  "Game Over, You Lose!");
    		}
    	}
    	
    	public void exposeMines(MyJButton mjb)
    	{
    		// Upon losing game, reveal the remaining mines that were not flagged
    		for (int i = 0; i < mineLocations.length; ++i)
    		{
    			MyJButton newButton = (MyJButton)mjb.getParent().getComponent(mineLocations[i]);
    			if (!newButton.getText().equals(UNEXPOSED_FLAGGED_CELL_TEXT))
    			{
    				newButton.setBackground(EXPOSED_CELL_BACKGROUND_COLOR);
    				newButton.setText(EXPOSED_MINE_TEXT);
    				newButton.setForeground(EXPOSED_CELL_FOREGROUND_COLOR_MAP[mineGrid[newButton.ROW][newButton.COL]]);
    				mjb.setText(getGridValueStr(newButton.ROW, newButton.COL));
    			}
    		}
    	}
    	
    	public void exposeCell(MyJButton mjb)
    	{
    		if ( !running )
    			return;
    		
    		--MineSweapPart.cellsToUncover; // One less cell to uncover
    		// expose this MyJButton 
    		mjb.setBackground(EXPOSED_CELL_BACKGROUND_COLOR);
    		mjb.setForeground(EXPOSED_CELL_FOREGROUND_COLOR_MAP[mineGrid[mjb.ROW][mjb.COL]]);
    		mjb.setText(getGridValueStr(mjb.ROW, mjb.COL));
      
    		// if the MyJButton that was just exposed is a mine
    		if ( mineGrid[mjb.ROW][mjb.COL] == IS_A_MINE_IN_MINEGRID_VALUE )
    		{  
    			gameStatus = false; // Game is over, lost
    			exposeMines(mjb); // Expose Mines
    			running = false; // No longer running
    			return;
    		}
      
    		// if the MyJButton that was just exposed has no mines in its perimeter
    		if ( mineGrid[mjb.ROW][mjb.COL] == NO_MINES_IN_PERIMETER_MINEGRID_VALUE )
    		{
    			for (int r = -1; r < 2; ++r)
    			{
    				for (int c = -1; c < 2; ++c)
    				{
    					if ((mjb.ROW+r >= 0 && mjb.ROW+r < MineSweapPart.MINEGRID_ROWS) && (mjb.COL+c >= 0 && mjb.COL+c < MineSweapPart.MINEGRID_COLS))
    					{
    						if (!(r == 0 && c == 0))
    						{
    							int nextButtonLocation = (mjb.ROW+r) * MineSweapPart.MINEGRID_COLS + mjb.COL+c;
    							MyJButton newButton = (MyJButton)mjb.getParent().getComponent(nextButtonLocation);
    							if (!newButton.getText().equals(UNEXPOSED_FLAGGED_CELL_TEXT) && !newButton.getBackground().equals(EXPOSED_CELL_BACKGROUND_COLOR))
    								exposeCell(newButton);
    						}
    					}
    				}
    			}
    		}
    	}
    }
    // end nested private class

    public static void main(String[] args)
    {
    	new MineSweapPart();
    }
  
    //************************************************************************************************

    // place MINES number of mines in sGrid and adjust all of the "mines in parimeter" values
    private void setMines()
    {
    	int row, col, loc;
    	int count = 0;
    	
    	do // Place the specified number of mines around the grid
    	{
    		row = (int) (Math.random() * MINEGRID_ROWS);
        	col = (int) (Math.random() * MINEGRID_COLS);
        	
    		if (this.mineGrid[row][col] != IS_A_MINE_IN_MINEGRID_VALUE) // Check to see if mine is already placed
    		{
    			this.mineGrid[row][col] = IS_A_MINE_IN_MINEGRID_VALUE; // Set Mine at mineGrid[row][col]
    			
    			loc = row * MINEGRID_COLS + col; // Calculate 1-D location
    			this.mineLocations[count] = loc; // Save mine location
    			++count; // Increase mine count
    			
    			// Adjust all the "mines in perimeter" values
    			for (int r = -1; r < 2; ++r)
    			{
    				for (int c = -1; c < 2; ++c)
    				{
    					if (!(r == 0 && c == 0))
    					{
    						try {
    							if (this.mineGrid[row+r][col+c] != IS_A_MINE_IN_MINEGRID_VALUE)
    								++this.mineGrid[row+r][col+c];
    						} catch (Exception e) {
    							continue;
    						}
    					}
    				}
    			}
    		}
    	} while (count != TOTAL_MINES);
    }
  
    private String getGridValueStr(int row, int col)
    {
    	// no mines in this MyJbutton's perimeter
    	if ( this.mineGrid[row][col] == NO_MINES_IN_PERIMETER_MINEGRID_VALUE )
    		return INITIAL_CELL_TEXT;
    
    	// 1 to 8 mines in this MyJButton's perimeter
    	else if ( this.mineGrid[row][col] > NO_MINES_IN_PERIMETER_MINEGRID_VALUE && 
    			this.mineGrid[row][col] <= ALL_MINES_IN_PERIMETER_MINEGRID_VALUE )
    		return "" + this.mineGrid[row][col];
    
    	// this MyJButton in a mine
    	else // this.mineGrid[row][col] = IS_A_MINE_IN_GRID_VALUE
    		return MineSweapPart.EXPOSED_MINE_TEXT;
    }
}
