package tetris;

public class Tetromino 
{
	/*Instance Fields*/
	private int[][] location;
	private int type; //Type of piece, one of 7
	private int rotState;
	private int[][] rotMatrix; //Clockwise rotation. These 2-D arrays move the individual squares of each piece around.
	private int[][] rotCCWMatrix; //Counterclockwise rotation
	
	private int[][][] kickData;
	
	private boolean locked;
	
	public Tetromino(int typeIn) 
	{
		type = typeIn;
		locked = false;
		rotState = 0;
									
		/*
		 * The locations are instantiated based on where they start on the board when generated. 
		 * Each type of piece has two different rotation matrices. 
		 * The grid's origin is at the top left, and x and y values increase moving away from it.
		 */
		switch (type)
		{
			case 1: //I-block
				location = new int[][]{{3, 1},{4, 1},{5, 1},{6, 1}};
				rotMatrix = new int[][] {{2, -1}, {1, 0}, {0, 1}, {-1, 2}}; 
				rotCCWMatrix = new int[][] {{1, 2}, {0, 1}, {-1, 0}, {-2, -1}}; 
				kickData = new int[][][] 
				   {{{0, 0}, {-2, 0}, {1, 0}, {-2, 1}, {1, -2}}, 
					{{0, 0}, {2, 0}, {-1, 0}, {2, -1}, {-1, 2}}, 
					{{0, 0}, {-1, 0}, {2, 0}, {-1, -2}, {2, 1}},
					{{0, 0}, {1, 0}, {-2, 0}, {1, 2}, {-2, -1}}, 
					{{0, 0}, {2, 0}, {1, 0}, {2, -1}, {-1, 2}}, 
					{{0, 0}, {-2, 0}, {-1, 0}, {-2, 1}, {1, -2}},
					{{0, 0}, {1, 0}, {-2, 0}, {1, 2}, {-2, -1}}, 
					{{0, 0}, {-1, 0}, {2, 0}, {-1, -2}, {2, 1}}
					};
				break;
			case 2: //L-block
				location = new int[][]{{3, 1},{4, 1},{5, 1},{5, 0}};
				rotMatrix = new int[][] {{1, -1}, {0, 0}, {-1, 1}, {0, 2}};
				rotCCWMatrix = new int[][] {{1, 1}, {0, 0}, {-1, -1}, {-2, 0}}; 
				kickData = new int[][][] {{{0, 0}, {-1, 0}, {-1, -1}, {0, 2}, {-1, 2}}, 
					{{0, 0}, {1, 0}, {1, 1}, {0, -2}, {1, -2}}, 
					{{0, 0}, {1, 0}, {1, 1}, {0, -2}, {1, -2}},
					{{0, 0}, {-1, 0}, {-1, -1}, {0, 2}, {-1, 2}}, 
					{{0, 0}, {1, 0}, {1, -1}, {0, 2}, {1, 2}}, 
					{{0, 0}, {-1, 0}, {-1, 1}, {0, -2}, {-1, -2}},
					{{0, 0}, {-1, 0}, {-1, 1}, {0, -2}, {-1, -2}}, 
					{{0, 0}, {1, 0}, {1, -1}, {0, 2}, {1, 2}}
					};
				break;
			case 3: //J-block
				location = new int[][]{{3, 0},{3, 1},{4, 1},{5, 1}};
				rotMatrix = new int[][] {{2, 0}, {1, -1}, {0, 0}, {-1, 1}};
				rotCCWMatrix = new int[][] {{0, 2}, {1, 1}, {0, 0}, {-1, -1}};
				kickData = new int[][][] {{{0, 0}, {-1, 0}, {-1, -1}, {0, 2}, {-1, 2}}, 
					{{0, 0}, {1, 0}, {1, 1}, {0, -2}, {1, -2}}, 
					{{0, 0}, {1, 0}, {1, 1}, {0, -2}, {1, -2}},
					{{0, 0}, {-1, 0}, {-1, -1}, {0, 2}, {-1, 2}}, 
					{{0, 0}, {1, 0}, {1, -1}, {0, 2}, {1, 2}}, 
					{{0, 0}, {-1, 0}, {-1, 1}, {0, -2}, {-1, -2}},
					{{0, 0}, {-1, 0}, {-1, 1}, {0, -2}, {-1, -2}}, 
					{{0, 0}, {1, 0}, {1, -1}, {0, 2}, {1, 2}}
					};
				break;
			case 4: //O-block
				location = new int[][]{{4, 0},{4, 1},{5, 1},{5, 0}};
				rotMatrix = new int[][]{{0, 0}, {0, 0}, {0, 0}, {0, 0}};
				rotCCWMatrix = new int[][]{{0, 0}, {0, 0}, {0, 0}, {0, 0}};
				kickData = new int[8][5][2];
				for (int i = 0; i < 8; i++) for (int j = 0; j < 5; j++) for (int k = 0; k < 2; k++) kickData[i][j][k] = 0;
				break;
			case 5: //S-block
				location = new int[][]{{3, 1},{4, 1},{4, 0},{5, 0}};
				rotMatrix = new int[][] {{1, -1}, {0, 0}, {1, 1}, {0, 2}};
				rotCCWMatrix = new int[][] {{1, 1}, {0, 0}, {-1, 1}, {-2, 0}};
				kickData = new int[][][] {{{0, 0}, {-1, 0}, {-1, -1}, {0, 2}, {-1, 2}}, 
					{{0, 0}, {1, 0}, {1, 1}, {0, -2}, {1, -2}}, 
					{{0, 0}, {1, 0}, {1, 1}, {0, -2}, {1, -2}},
					{{0, 0}, {-1, 0}, {-1, -1}, {0, 2}, {-1, 2}}, 
					{{0, 0}, {1, 0}, {1, -1}, {0, 2}, {1, 2}}, 
					{{0, 0}, {-1, 0}, {-1, 1}, {0, -2}, {-1, -2}},
					{{0, 0}, {-1, 0}, {-1, 1}, {0, -2}, {-1, -2}}, 
					{{0, 0}, {1, 0}, {1, -1}, {0, 2}, {1, 2}}
					};
				break;
			case 6: //T-block
				location = new int[][]{{3, 1},{4, 1},{4, 0},{5, 1}};
				rotMatrix = new int[][] {{1, -1}, {0, 0}, {1, 1}, {-1, 1}}; 
				rotCCWMatrix = new int[][] {{1, 1}, {0, 0}, {-1, 1}, {-1, -1}}; 
				kickData = new int[][][] {{{0, 0}, {-1, 0}, {-1, -1}, {0, 2}, {-1, 2}}, 
					{{0, 0}, {1, 0}, {1, 1}, {0, -2}, {1, -2}}, 
					{{0, 0}, {1, 0}, {1, 1}, {0, -2}, {1, -2}},
					{{0, 0}, {-1, 0}, {-1, -1}, {0, 2}, {-1, 2}}, 
					{{0, 0}, {1, 0}, {1, -1}, {0, 2}, {1, 2}}, 
					{{0, 0}, {-1, 0}, {-1, 1}, {0, -2}, {-1, -2}},
					{{0, 0}, {-1, 0}, {-1, 1}, {0, -2}, {-1, -2}}, 
					{{0, 0}, {1, 0}, {1, -1}, {0, 2}, {1, 2}}
					};
				break;
			case 7: //Z-block
				location = new int[][]{{3, 0},{4, 0},{4, 1},{5, 1}};
				rotMatrix = new int[][] {{2, 0}, {1, 1}, {0, 0}, {-1, 1}};
				rotCCWMatrix = new int[][] {{0, 2}, {-1, 1}, {0, 0}, {-1, -1}};
				kickData = new int[][][] {{{0, 0}, {-1, 0}, {-1, -1}, {0, 2}, {-1, 2}}, 
					{{0, 0}, {1, 0}, {1, 1}, {0, -2}, {1, -2}}, 
					{{0, 0}, {1, 0}, {1, 1}, {0, -2}, {1, -2}},
					{{0, 0}, {-1, 0}, {-1, -1}, {0, 2}, {-1, 2}}, 
					{{0, 0}, {1, 0}, {1, -1}, {0, 2}, {1, 2}}, 
					{{0, 0}, {-1, 0}, {-1, 1}, {0, -2}, {-1, -2}},
					{{0, 0}, {-1, 0}, {-1, 1}, {0, -2}, {-1, -2}}, 
					{{0, 0}, {1, 0}, {1, -1}, {0, 2}, {1, 2}}
					};
				break;
		}
			
	}
	
	public void moveDown()
	{
		for (int i = 0; i < 4; i++)
		{
			location[i][1]++; //Increments each y-coordinate.
		}
	}
	
	public void moveRight()
	{
		for (int i = 0; i < 4; i++)
		{
			location[i][0]++; //Increments each x-coordinate.
		}
	}
	
	public void moveLeft()
	{
		for (int i = 0; i < 4; i++)
		{
			location[i][0]--; //Decrements each x-coordinate.
		}
	}
	
	/*
	 * Each method corresponds to one direction of rotation.
	 * Each tetromino is rotated about an origin.
	 * After a rotation, the rotation matrix no longer applies. Therefore, it is updated 
	 * 	to reflect what should happen come the next rotation.
	 */
	
	public void rotate()
	{
		int temp = 0;
		for (int i = 0; i < 4; i++)
		{
			location[i][0] += rotMatrix[i][0];
			location[i][1] += rotMatrix[i][1];
			
			temp = rotMatrix[i][1];
			rotMatrix[i][1] = rotMatrix[i][0];
			rotMatrix[i][0] = temp*(-1);
			
			temp = rotCCWMatrix[i][1];
			rotCCWMatrix[i][1] = rotCCWMatrix[i][0];
			rotCCWMatrix[i][0] = temp*(-1);
		}
		
		rotState++;
		rotState %= 4;
	}
	
	public void rotateCCW()
	{
		int temp = 0;
		for (int i = 0; i < 4; i++)
		{
			location[i][0] += rotCCWMatrix[i][0];
			location[i][1] += rotCCWMatrix[i][1];
			
			temp = rotMatrix[i][0];
			rotMatrix[i][0] = rotMatrix[i][1];
			rotMatrix[i][1] = temp*(-1);
			
			temp = rotCCWMatrix[i][0];
			rotCCWMatrix[i][0] = rotCCWMatrix[i][1];
			rotCCWMatrix[i][1] = temp*(-1);
		}
		
		rotState--;
		rotState %= 4;
	}
	
	public void setLock(boolean status)
	{
		locked = status;
	}
	
	public boolean isLocked()
	{
		return locked;
	}
	
	
	public int[][] getLocation()
	{
		return location;
	}
	
	public int getState()
	{
		return rotState;
	}
	
	public int[][][] getKickData()
	{
		return kickData;
	}
	
	public void translate(int x, int y)
	{
		for (int i = 0; i < 4; i++)
		{
			location[i][0] += x;
			location[i][1] += y;
		}
	}
	
	public Tetromino clone()
	{
		Tetromino clone = new Tetromino(type);
		
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 2; j++)
			{
				clone.location[i][j] = location[i][j];
				clone.rotMatrix[i][j] = rotMatrix[i][j];
				clone.rotCCWMatrix[i][j] = rotCCWMatrix[i][j];
			}
		}
		
		clone.rotState = rotState;
		clone.locked = locked;
		
		return clone;
	}

	public int getType() 
	{
		return type;
	}

}
