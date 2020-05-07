package tetris; 

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.Timer;

public class TetrisComponent extends JComponent
{
	public static final Color[] COLORS = {Color.black, Color.cyan, Color.orange, Color.blue, Color.yellow, Color.green, Color.magenta, Color.red}; 
	
	private Graphics canvas;
	private int[][] board;
	private int numPiece;
	private List defaultBag;
	private List currentBag;
	private List futureBag;
	private Tetromino piece;
	private Tetromino ghost;
	private Tetromino heldPiece;
	private List<Integer> nextPieces;
	private int nextPieceIndex;
	private boolean next;
	private boolean gameover;
	private boolean held;
	private Timer gravityTimer;
	private int level;
	private int numClears;
	private int score;
	private int rotIndex;
	private int kickIndex;
	private Locker locker;
	
	public static final int BOARD_OFFSET_X = 200;
	public static final int HELD_OFFSET_X = 25 - 75;
	public static final int HELD_OFFSET_Y = 25 + 75;
	public static final int NEXT_OFFSET_X = 25 - 75 + 500;
	public static final int NEXT_OFFSET_Y = 25 + 75;
	
	class Locker extends Thread
	{
		public void run() 
		{
			while (!next)
			{

				if (!moveableDown(piece)) piece.setLock(true);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if (piece.isLocked())
				{
					next = true;
				}
			}
			
			next = true;
			return;
		}
		
	}
	
	class GravityTimer implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			boolean repeat = false;
			Tetromino tempPiece = (Tetromino) piece.clone();
			tempPiece.moveDown();
			
			for (int i = 0; i < 4; i++)
			{
				if (tempPiece.getLocation()[i][1] > 21) 
				{
					if (!locker.isAlive()) locker.start();
					return;
				}
			}
			
			for (int i = 0; i < 4; i++)
			{
				repeat = false;
				for (int j = 0; j < 4; j++)
				{
					if (tempPiece.getLocation()[i][0] == piece.getLocation()[j][0] && tempPiece.getLocation()[i][1] == piece.getLocation()[j][1])
					{
						repeat = true;
						if (piece.isLocked()) return;
					}
				}
				
				if (!repeat) 
				{
					if (board[tempPiece.getLocation()[i][0]][tempPiece.getLocation()[i][1]] != 0)
					{
						for (int k = 0; k < 4; k++)
						{
							if (piece.getLocation()[k][1] == 0 || piece.getLocation()[k][1] == 1) 
							{
								gameover = true;
								return;
							}
						}
						
						if (!locker.isAlive()) locker.start();
						return;
					}
				}
			}
			
			for (int i = 0; i < 4; i++) 
			{
				board[piece.getLocation()[i][0]][piece.getLocation()[i][1]] = 0;
			}
				
			piece.moveDown();
				
			for (int i = 0; i < 4; i++) 
			{
				board[piece.getLocation()[i][0]][piece.getLocation()[i][1]] = numPiece;
			}
			
			piece.setLock(false);
			repaint();
		}
	}
	
	class InputListener extends KeyAdapter
	{
		
		@Override
		public void keyPressed(KeyEvent e)
		{
			switch (e.getKeyCode())
			{
				case KeyEvent.VK_SPACE:
					while (true)
					{
						if (!moveableDown(piece)) break;
						for (int i = 0; i < 4; i++) 
						{
							board[piece.getLocation()[i][0]][piece.getLocation()[i][1]] = 0;
						}
						
						piece.moveDown();
						
						for (int i = 0; i < 4; i++) 
						{
							board[piece.getLocation()[i][0]][piece.getLocation()[i][1]] = numPiece;
						}
					}
					
					repaint();
					next = true; 
					break;
				case KeyEvent.VK_DOWN:
					if (!moveableDown(piece)) break;
					
					piece.setLock(false);	
					for (int i = 0; i < 4; i++) 
					{
						board[piece.getLocation()[i][0]][piece.getLocation()[i][1]] = 0;
					}
						
					piece.moveDown();
						
					for (int i = 0; i < 4; i++) 
					{
						board[piece.getLocation()[i][0]][piece.getLocation()[i][1]] = numPiece;
					 }
					
					repaint();
					break;
				case KeyEvent.VK_RIGHT:
					if (!moveableRight()) break;
					
					piece.setLock(false);	
					for (int i = 0; i < 4; i++) 
					{
						board[piece.getLocation()[i][0]][piece.getLocation()[i][1]] = 0;
					}
						
					piece.moveRight();
						
					for (int i = 0; i < 4; i++) 
					{
						board[piece.getLocation()[i][0]][piece.getLocation()[i][1]] = numPiece;
					}
					

					repaint();
					break;
				case KeyEvent.VK_LEFT:
					if (!moveableLeft()) break;

					piece.setLock(false);	
					for (int i = 0; i < 4; i++) 
					{
						board[piece.getLocation()[i][0]][piece.getLocation()[i][1]] = 0;
					}
						
					piece.moveLeft();
						
					for (int i = 0; i < 4; i++) 
					{
						board[piece.getLocation()[i][0]][piece.getLocation()[i][1]] = numPiece;
					}
						
					repaint();
					break;
						
				case KeyEvent.VK_UP:
				case KeyEvent.VK_X:
					if (!rotatable()) break;

					piece.setLock(false);	
					for (int i = 0 ; i < 4; i++) 
					{
						board[piece.getLocation()[i][0]][piece.getLocation()[i][1]] = 0;
					}
						
					piece.rotate();
					piece.translate(piece.getKickData()[rotIndex][kickIndex][0], piece.getKickData()[rotIndex][kickIndex][1]);
					for (int i = 0; i < 4; i++) 
					{
						board[piece.getLocation()[i][0]][piece.getLocation()[i][1]] = numPiece;
					}
					repaint();
					break;
				case KeyEvent.VK_Z:
					if (!rotatableCCW()) break;

					piece.setLock(false);	
					for (int i = 0 ; i < 4; i++) 
					{
						board[piece.getLocation()[i][0]][piece.getLocation()[i][1]] = 0;
					}
						
					piece.rotateCCW();
					piece.translate(piece.getKickData()[rotIndex][kickIndex][0], piece.getKickData()[rotIndex][kickIndex][1]);
					for (int i = 0; i < 4; i++) 
					{
						board[piece.getLocation()[i][0]][piece.getLocation()[i][1]] = numPiece;
					}
					repaint();
					break;
				case KeyEvent.VK_SHIFT:
					if (held) break;
					piece.setLock(false);	
					for (int i = 0; i < 4; i++) 
					{
						board[piece.getLocation()[i][0]][piece.getLocation()[i][1]] = 0;
					}
					
					
					Tetromino temp = piece.clone();
					if (heldPiece != null) 
					{
						piece = heldPiece;
						numPiece = heldPiece.getType();
						held = true;
					}
					else next = true;
					heldPiece = new Tetromino(temp.getType());
					
					repaint();
					break;
					
			
			}
		}
		
	}
	
	public TetrisComponent() 
	{
		board = new int[10][22];
		for(int i = 0; i < 10; i++)
		{
			for (int j = 0; j < 22; j++)
			{
				board[i][j] = 0;
			}
		}
		next = true;
		gameover = false;
		held = false;
		addKeyListener(new InputListener());
		level = 0;
		numClears = 0;
		score = 0;
		locker = new Locker();
		defaultBag = new ArrayList<Integer>();
		for (int i = 1; i <=7; i++) defaultBag.add(i);
		currentBag = new ArrayList<Integer>(defaultBag);
		Collections.shuffle(currentBag);
		
		nextPieces = new ArrayList<Integer>();
		for (int i = 0; i < 6; i++)
		{
			nextPieces.add((Integer) currentBag.get(i));
		}
		nextPieceIndex = 0;
		
		futureBag = new ArrayList<Integer>(defaultBag);
		Collections.shuffle(futureBag);
	}
	
	public void paintComponent(Graphics g)
	{
		canvas = (Graphics2D) g;
		
		
		canvas.setColor(Color.black);
		canvas.fillRect(25, 25, 150, 90);
		canvas.fillRect(525, 25, 150, 540);
		
		canvas.setPaintMode();
		canvas.drawString("HELD PIECE", 25, 20);
		
		canvas.drawString("LVL: " + level, 25, 200);
		canvas.drawString("SCORE: " + score, 25, 300);
		canvas.drawString("LINES CLRD:\n " + numClears, 25, 400);
		
		for(int i = 0; i < 10; i++)
		{
			for (int j = 2; j < 22; j++)
			{
				canvas.setColor(COLORS[board[i][j]]);
				canvas.fillRect((i*30) + BOARD_OFFSET_X, (j-2)*30, 30, 30);
			}
		}
		
		
		canvas.setColor(Color.white);
		
//		for(int i = 1; i < 10; i++)
//		{
//			canvas.drawLine((30*i) + BOARD_OFFSET_X, 0, (30*i) + BOARD_OFFSET_X, 700);
//		}
//		
//		for(int i = 1; i < 22; i++)
//		{
//			canvas.drawLine(0 + BOARD_OFFSET_X, 30*i, 300 + BOARD_OFFSET_X, 30*i);
//		}
		
		if (heldPiece != null)
		{
			for (int i = 0; i < 4; i++)
			{
				canvas.setColor(COLORS[heldPiece.getType()]);
				if (held) canvas.setColor(Color.lightGray);
				canvas.fillRect((heldPiece.getLocation()[i][0]*30) + HELD_OFFSET_X, ((heldPiece.getLocation()[i][1] - 2)*30) + HELD_OFFSET_Y, 30, 30);
				
				canvas.setColor(Color.white);
				canvas.drawRect((heldPiece.getLocation()[i][0]*30) + HELD_OFFSET_X, ((heldPiece.getLocation()[i][1] - 2)*30) + HELD_OFFSET_Y, 30, 30);
			}
		}
		
		for (int j = 0; j < 6; j++)
		{
			Tetromino temp = new Tetromino(nextPieces.get(j));
			for (int i = 0; i < 4; i++)
			{
				canvas.setColor(COLORS[temp.getType()]);
				canvas.fillRect((temp.getLocation()[i][0]*30) + NEXT_OFFSET_X, ((temp.getLocation()[i][1] - 2)*30) + NEXT_OFFSET_Y + (j*90), 30, 30);
				
				canvas.setColor(Color.white);
				canvas.drawRect((temp.getLocation()[i][0]*30) + NEXT_OFFSET_X, ((temp.getLocation()[i][1] - 2)*30) + NEXT_OFFSET_Y + (j*90), 30, 30);
			}
		}
		
		if (ghost == null) return;
		
		
		ghost = piece.clone();
		
		while (true)
		{
			if (!moveableDown(ghost)) break;
			
			ghost.moveDown();
		}
		
		if (ghost.getLocation()[0][1] - piece.getLocation()[0][1] <= 2) return;
		for (int i = 0; i < 4; i++)
		{
			canvas.setColor(Color.lightGray); 
			canvas.fillRect((ghost.getLocation()[i][0]*30) + BOARD_OFFSET_X, (ghost.getLocation()[i][1] - 2)*30, 30, 30);
			
			canvas.setColor(COLORS[numPiece]);
			canvas.drawRect((ghost.getLocation()[i][0]*30) + BOARD_OFFSET_X, (ghost.getLocation()[i][1] - 2)*30, 30, 30);
		}
	}
	
	public void generateBlock()
	{
		locker = new Locker();
		lineClear();
		next = false;
		held = false;
		
		if (gravityTimer != null)
		{
			nextPieces.remove(0);
			nextPieces.add((Integer) futureBag.get(nextPieceIndex));
			nextPieceIndex++;
			nextPieceIndex %= 7;
		}
		
		else
		{
			nextPieces.remove(0);
			nextPieces.add((Integer) currentBag.get(currentBag.size() - 1));
		}
		
		if (currentBag.size() == 0)
		{
			currentBag = futureBag;
			futureBag = new ArrayList<Integer>(defaultBag);
			Collections.shuffle(futureBag);
		}
		numPiece = (int) currentBag.remove(0);
		piece = new Tetromino(numPiece);
		ghost = new Tetromino(numPiece);

		
		for (int i = 0; i < 4; i++)
		{
			board[piece.getLocation()[i][0]][piece.getLocation()[i][1]] = numPiece;
		}
		
		repaint();
		if (gravityTimer != null) gravityTimer.stop();
		if (level < 9) gravityTimer = new Timer((int) (800 - (5000.0*level/60) + 0.5), new GravityTimer());
		else
			if (level == 9) gravityTimer = new Timer(100, new GravityTimer());
			else if (level > 9 && level < 13) gravityTimer = new Timer(83, new GravityTimer());
			else if (level > 12 && level < 16) gravityTimer = new Timer(67, new GravityTimer());
			else if (level > 15 && level < 19) gravityTimer = new Timer(50, new GravityTimer());
			else if (level > 18 && level < 29) gravityTimer = new Timer(33, new GravityTimer());
			else if (level > 28) gravityTimer = new Timer(17, new GravityTimer());
		gravityTimer.start();
	}
	
	public boolean getGameover()
	{
		return gameover;
	}
	
	public boolean getNext()
	{
		return next;
	}
	
	public void lineClear()
	{
		boolean clear = false;
		int clears = 0;
		for (int i = 2; i < 22; i++)
		{
			clear = true;
			for (int j = 0; j < 10; j++)
			{
				if (board[j][i] == 0) 
				{
					clear = false;
					break;
				}
			}
			
			if (clear)
			{
				clears++;
				numClears++;
				for (int k = i; k > 2; k--)
				{
					for (int l = 0; l < 10; l++)
					{
						board[l][k] = board[l][k-1];
					}
				}
				
				for (int m = 0; m < 10; m++)
				{
					board[m][2] = 0;
				}
			}
			
		}
		
		if (numClears/10 > level) level = numClears/10;
//		if (numClears > level) level = numClears;

		switch (clears)
		{
			case 1:
				score += 40*(level + 1);
				break;
			case 2:
				score += 100*(level + 1);
				break;
			case 3:
				score += 300*(level + 1);
				break;
			case 4:
				score += 1200*(level + 1);
				break;
		}
		System.out.println(level + " " + score);
		repaint();
	}
	
	public boolean moveableDown(Tetromino piece)
	{
		boolean repeat = false;
		Tetromino tempPiece = (Tetromino) piece.clone();
		tempPiece.moveDown();
			
		for (int i = 0; i < 4; i++)
		{
			if (tempPiece.getLocation()[i][1] > 21) 
			{	
				return false;
			}
		}
			
		for (int i = 0; i < 4; i++)
		{
			repeat = false;
			for (int j = 0; j < 4; j++)
			{
				if (tempPiece.getLocation()[i][0] == piece.getLocation()[j][0] && tempPiece.getLocation()[i][1] == piece.getLocation()[j][1])
				{
					repeat = true;
				}
			}
				
			if (!repeat) 
			{
				if (board[tempPiece.getLocation()[i][0]][tempPiece.getLocation()[i][1]] != 0)
				{	
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean moveableRight()
	{
		boolean repeat = false;
		Tetromino tempPiece = (Tetromino) piece.clone();
		tempPiece.moveRight();
			
		for (int i = 0; i < 4; i++)
		{
			if (tempPiece.getLocation()[i][0] > 9) 
			{	
				return false;
			}
		}
			
		for (int i = 0; i < 4; i++)
		{
			repeat = false;
			for (int j = 0; j < 4; j++)
			{
				if (tempPiece.getLocation()[i][0] == piece.getLocation()[j][0] && tempPiece.getLocation()[i][1] == piece.getLocation()[j][1])
				{
					repeat = true;
				}
			}
				
			if (!repeat) 
			{
				if (board[tempPiece.getLocation()[i][0]][tempPiece.getLocation()[i][1]] != 0)
				{	
					return false;
				}
			}
		}
		
		return true;
	}
	
	public boolean moveableLeft()
	{
		boolean repeat = false;
		Tetromino tempPiece = (Tetromino) piece.clone();
		tempPiece.moveLeft();
		
		for (int i = 0; i < 4; i++)
		{
			if (tempPiece.getLocation()[i][0] < 0) 
			{	
				return false;							}
			}
			
		for (int i = 0; i < 4; i++)
		{
			repeat = false;
			for (int j = 0; j < 4; j++)
			{
				if (tempPiece.getLocation()[i][0] == piece.getLocation()[j][0] && tempPiece.getLocation()[i][1] == piece.getLocation()[j][1])
				{
					repeat = true;
				}
			}
				
			if (!repeat) 
			{
				if (board[tempPiece.getLocation()[i][0]][tempPiece.getLocation()[i][1]] != 0)
				{	
					return false;
				}
			}
		}
		
		return true;
	}
	
	public boolean rotatable()
	{
		boolean repeat = false;
		boolean outOfBounds = false;
		boolean overlap = false;
		Tetromino tempPiece = (Tetromino) piece.clone();
		tempPiece.rotate();
		rotIndex = 0;

		switch (piece.getState())
		{
			case 0:
				rotIndex = 0;
				break;
			case 1:
				rotIndex = 2;
				break;
			case 2:
				rotIndex = 4;
				break;
			case 3:
				rotIndex = 6;
				break;
		}

		
		for (int k = 0; k < 5; k++)
		{
			System.out.println();
			Tetromino kickedPiece = tempPiece.clone();
			kickedPiece.translate(kickedPiece.getKickData()[rotIndex][k][0], kickedPiece.getKickData()[rotIndex][k][1]);
			outOfBounds = false;
			for (int i = 0; i < 4; i++)
			{
				if (kickedPiece.getLocation()[i][0] < 0 || kickedPiece.getLocation()[i][0] > 9 || kickedPiece.getLocation()[i][1] < 0 || kickedPiece.getLocation()[i][1] > 21) 
				{	
					outOfBounds = true;
					break;
				}
			}
			
			if (outOfBounds) 
			{
				System.out.println(k);
				continue;
			}
			
			overlap = false;
			for (int i = 0; i < 4; i++)
			{
				repeat = false;
				for (int j = 0; j < 4; j++)
				{
					if (kickedPiece.getLocation()[i][0] == piece.getLocation()[j][0] && kickedPiece.getLocation()[i][1] == piece.getLocation()[j][1])
					{
						repeat = true;
					}
				}
					
				if (!repeat) 
				{
					if (board[kickedPiece.getLocation()[i][0]][kickedPiece.getLocation()[i][1]] != 0)
					{	
						overlap = true;
						break;
					}
				}
			}
			
			if (overlap) continue;
			
			kickIndex = k;
			return true;
		}
		
		return false;
	}
	
	public boolean rotatableCCW()
	{
		boolean repeat = false;
		boolean outOfBounds = false;
		boolean overlap = false; 
		Tetromino tempPiece = (Tetromino) piece.clone();
		tempPiece.rotateCCW(); 
		rotIndex = 0;

		switch (piece.getState())
		{
			case 0:
				rotIndex = 7;
				break;
			case 1:
				rotIndex = 1 ;
				break;
			case 2:
				rotIndex = 3;
				break;
			case 3:
				rotIndex = 5;
				break;
		}

		
		for (int k = 0; k < 5; k++)
		{
			System.out.println();
			Tetromino kickedPiece = tempPiece.clone();
			kickedPiece.translate(kickedPiece.getKickData()[rotIndex][k][0], kickedPiece.getKickData()[rotIndex][k][1]);
			outOfBounds = false;
			for (int i = 0; i < 4; i++)
			{
				if (kickedPiece.getLocation()[i][0] < 0 || kickedPiece.getLocation()[i][0] > 9 || kickedPiece.getLocation()[i][1] < 0 || kickedPiece.getLocation()[i][1] > 21) 
				{	
					outOfBounds = true;
					break;
				}
			}
			
			if (outOfBounds) 
			{
				System.out.println(k);
				continue;
			}
			
			overlap = false;
			for (int i = 0; i < 4; i++)
			{
				repeat = false;
				for (int j = 0; j < 4; j++)
				{
					if (kickedPiece.getLocation()[i][0] == piece.getLocation()[j][0] && kickedPiece.getLocation()[i][1] == piece.getLocation()[j][1])
					{
						repeat = true;
					}
				}
					
				if (!repeat) 
				{
					if (board[kickedPiece.getLocation()[i][0]][kickedPiece.getLocation()[i][1]] != 0)
					{	
						overlap = true;
						break;
					}
				}
			}
			
			if (overlap) continue;
			
			kickIndex = k;
			return true;
		}
		
		return false;
	}

}
