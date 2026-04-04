package main;

import java.awt.*;
import java.awt.event.*;
public class MouseClickListener extends Frame implements MouseListener{
    private String start = null;  // Stores the start position
    private String end = null;    // Stores the end position
    private ChessGame game;       // Reference to ChessGame

    public MouseClickListener(ChessGame game) {
        this.game = game;
        addMouseListener(this);
        setSize(800, 820);
        setLayout(null);
        setVisible(false);
    }
    /*MouseClickListener(){

        addMouseListener(this);

        setSize(800,820);
        setLayout(null);
        setVisible(true);
    }*/

    public void mouseClicked(MouseEvent e) {
        int x =  e.getX()/100 + 97;
        int y = (e.getY()-20)/100 + 1;
        System.out.println((char)x+""+y);
    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}

    /*public static void main(String[] args) {
        Main c = new Main();
    }*/
}