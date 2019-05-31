package demineur_w__bouton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.time.Year;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.text.StyledEditorKit.ForegroundAction;

public class Demineur implements ActionListener {

	JFrame frame = new JFrame("Demineur");
	JButton reset = new JButton("Reset");
	JButton [][] buttons = new JButton[20][20];
	int[][] counts = new int [20][20];
	Container grid = new Container();
	final int MINES = 10;

	public  Demineur() {
		frame.setSize(1000, 700);
		frame.setLayout(new BorderLayout());
		frame.add(reset, BorderLayout.NORTH);
		reset.addActionListener(this);

		//Grille bouton
		grid.setLayout(new GridLayout(20, 20));
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[0].length; j++) {
				buttons[i][j] = new JButton();
				buttons[i][j].addActionListener(this);
				grid.add(buttons[i][j]);
			}

		}
		frame.add(grid, BorderLayout.CENTER);
		minesAleatoires();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	public static void main(String[] args) {
		new Demineur();
	}

	public void minesAleatoires() {
		//initialisation de la liste aleatoire
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < counts.length; i++) {
			for (int j = 0; j < counts[0].length; j++) {
				list.add(i*100+j);
			}
		}
		//reset counts, placer 30 mines
		counts = new int[20][20];
		for (int i = 0; i < 30; i++) {
			int choice = (int)(Math.random()*list.size());
			counts[list.get(choice)/100][list.get(choice)%100]=MINES;
			list.remove(choice);
		}
		//Initialisation comptage mine au voisinage
		for (int i = 0; i < counts.length; i++) {
			for (int j = 0; j < counts[0].length; j++) {
				if (counts[i][j] != MINES) {
					int neighborcount = 0;
					if(i>0 && j>0 && counts[i-1][j-1]==MINES) {// haut à gauche
						neighborcount++;
					}
					if (j>0 && counts[i][j-1] == MINES){// haut
						neighborcount++;
					}
					if (i<counts.length-1 && j< counts[0].length-1 && counts[i+1][j+1]==MINES) {// bas à droite
						neighborcount++;
					}
					counts[i][j]=neighborcount;
				}
			}
		}
	}
	public void lostGame() {
		for (int i=0; i<buttons.length;i++) {
			for (int j=0; j<buttons[0].length;j++) {
				if(buttons[i][j].isEnabled()) {
					if(counts[i][j] != MINES) {
						buttons[i][j].setText(counts[i][j]+"");
						buttons[i][j].setEnabled(false);
					}
					else {
						buttons[i][j].setText("X");
						buttons[i][j].setEnabled(false);

					}
				}
			}
		}
	}
	public void clearZeros(ArrayList<Integer> toClear) {
		if (toClear.size()==0) {
			return;
		}
		else {
			int i =toClear.get(0)/100;
			int j = toClear.get(0)%100;
			toClear.remove(0);
			if (counts[i][j]==0) {
				if (i>0 && j>0) {// haut gauche
					buttons[i-1][j-1].setText(counts[i-1][j-1]+"");
					buttons[i-1][j-1].setEnabled(false);
					if (counts[i-1][j-1]==0) {
						toClear.add((i-1)*100+(j-1));
						
					}
				} 
				if (j>0) {//haut
					buttons[i][j-1].setText(counts[i][j-1]+"");
					buttons[i][j-1].setEnabled(false);
					if (counts[i][j-1]==0) {
						toClear.add((i)*100+(j-1));
					}
				}
				if (i < counts.length -1 && j > 0) {//haut droite
					buttons[i+1][j-1].setText(counts[i+1][j-1]+"");
					buttons[i+1][j-1].setEnabled(false);
					if (counts[i+1][j-1]==0) {
						toClear.add((i+1)*100+(j-1));
					}
				}
			}
		}
		clearZeros(toClear);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(reset)) {
			for (int i = 0; i < buttons.length; i++) {
				for (int j = 0; j < buttons[0].length; j++) {
					buttons[i][j].setEnabled(true);
					buttons[i][j].setText("");
				}
			}
			minesAleatoires();
		}
		else {
			for (int i=0; i<buttons.length;i++) {
				for (int j = 0; j < buttons[0].length; j++) {
					if (event.getSource().equals(buttons[i][j])) {
						if (counts[i][j]==MINES) {
							buttons[i][j].setForeground(Color.red);
							buttons[i][j].setText("X");
							buttons[i][j].setForeground(Color.black);
							lostGame();
						}
						else if (counts[i][j]==0) {
							buttons[i][j].setText(counts[i][j]+"");
							buttons[i][j].setEnabled(false);
							ArrayList<Integer> toClear = new ArrayList<Integer>();
							toClear.add(i*100+j);
							clearZeros(toClear);
						}
						else {
							buttons[i][j].setText(counts[i][j]+"");
							buttons[i][j].setEnabled(false);
						}
					}
				}
			}
		}
	}
}
