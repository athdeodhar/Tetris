package tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TetrisRunner 
{

	public static void main(String[] args) 
	{
		JFrame frame = new JFrame("Tetris");
		frame.setSize(new Dimension(716, 640));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setBackground(Color.black);
		
		JPanel left = new JPanel();
		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
		left.setSize(new Dimension(100,100));
		
		JLabel heldPiece = new JLabel();
		heldPiece.setMinimumSize(new Dimension(100, 100));
		heldPiece.setText("HOLD");
		
		
		JPanel right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
		right.setSize(new Dimension(75,450));
		
		JLabel[] nextPieces = new JLabel[6];
		for (int i = 0; i < 6; i++)
		{
			JLabel nextPiece = new JLabel();
			nextPiece.setMinimumSize(new Dimension(75, 75));
			nextPiece.setText(Integer.toString(i + 1));
			nextPieces[i] = nextPiece;
		}
		
		JPanel center = new JPanel();
		TetrisComponent canvas = new TetrisComponent();
		canvas.setMinimumSize(new Dimension(316, 700));
		canvas.setFocusable(true);
//		center.add(canvas);
		
		left.add(heldPiece);
		
		for (int i = 0; i < 6; i++)
		{
			right.add(nextPieces[i]);
		}
		
//		frame.add(left);
		frame.add(canvas);
//		frame.add(right);
		

		frame.setVisible(true);
		
		while (!canvas.getGameover()) 
		{
			System.out.print("");
			if (canvas.getNext()) 
			{
				canvas.generateBlock();
			}
		}
	

	}

}
