/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalpackage;

import java.awt.event.*;
import javax.swing.*;

/**
 * Author: Jesse Groves
 * Date April 18 2021
 * Purpose: Event file for Chess program
 */
public class ChessEvent implements ActionListener, KeyListener{
    ChessJFrame chess;//variable to hold the ChessJFrame.java file
    boolean first = true;//boolean to check if the current click is the first click or second click
    JButton firstPos = null;//JButton to hold the first piece clicked on
    JButton secondPos = null;//JButton to hold the second piece clicked on
    int moves = 0;//counts how many moves were made
    
    public ChessEvent(ChessJFrame board){
        chess = board;//assigns the ChessJFrame.java file to the import board
    }
    //Method Header Comments:
    //Preform actions when a button is pressed.
    //Moves the pieces from where the user clicked first to where the user clicked second
    public void actionPerformed(ActionEvent event){
        chess.requestFocus();//focuses on the chess board
        for(int i = 0; i < 8; i++){//looks through all buttons for the clicked one
            for(int j = 0; j < 8; j++){
                //finds button that was pressed and makes sure it isnt an empty space
                if(event.getSource() == chess.places[i][j] && first && chess.places[i][j].getIcon() != chess.images[0] && chess.places[i][j].getIcon() != chess.images[1]){
                    if(!String.valueOf(chess.places[i][j].getIcon()).contains("Selected")){
                        //makes sure it is the right players turn
                        if(moves % 2 == 0 && chess.places[i][j].getIcon().toString().startsWith("White") || moves % 2 == 1 && chess.places[i][j].getIcon().toString().startsWith("Black")){
                            firstPos = chess.places[i][j];//saves first button
                            first = false;//sets first to false
                            preview(i, j);//previews the available moves for the selected piece
                            break;//stops the for loop when it finds the button that was clicked
                        }
                    }
                }if(event.getSource() == chess.places[i][j] && !first){
                    //makes sure player can't move to the same spot the piece was selected
                    if(event.getSource() != firstPos && String.valueOf(chess.places[i][j].getIcon()).contains("Selected")){
                        secondPos = chess.places[i][j];//saves second pieces button
                        //makes sure the player cant capture their own pieces
                        if((String.valueOf(firstPos.getIcon()).contains("White_") && !String.valueOf(secondPos.getIcon()).contains("White_")) || (String.valueOf(firstPos.getIcon()).contains("Black_") && !String.valueOf(secondPos.getIcon()).contains("Black_"))){
                            moves++;//one more move made
                            cancelPreview();//cancels the preview
                            first = true;//sets first to true because a move was made so now another can be
                            move(firstPos, secondPos);//moves first piece to second position
                            win();//calls win funtion
                        }
                    }else{
                        cancelPreview();//cancels the preview of the move
                        first = true;//sets first to true because the selection was canceled
                    }
                }
            }
        }
    }
    //Method Header Comments:
    //Pauses the game when Escape key is pressed
    //Opens a JOptionPane giving user the choice to cancel, quit game, or resume
    @Override
    public void keyReleased(KeyEvent esc) {
        if(esc.getKeyCode() == KeyEvent.VK_ESCAPE){//checks if button pressed was the escape key
            int choice = JOptionPane.showConfirmDialog(null, "Quit to main menu?");//stores button press as an int
            if(choice == 0){//checks if the button pressed was yes, quits game
                chess.dispose();//closes game
                new MenuJFrame().setVisible(true);//opens main menu
            }
        }
    }
    //Method Header Comments: 
    //checks if one of the kings are off of the board
    //if so, opposite team of the missing king wins
    public void win(){
        int kingCount = 0;//counts number of kings on the board
        String king = "";//saves value of the kings name, checking if white or black piece
        for(int i = 0; i < 8; i++){//checks through entire board looking for king piece
            for(int j = 0; j < 8; j++){
                if(chess.places[i][j].getIcon().toString().contains("King")){//found a king piece
                    kingCount++;//add one to king count
                    king = chess.places[i][j].getIcon().toString();//saves name of king piece
                }
            }
        }
        if(kingCount == 1){//checks when there is only one king left
            if(king.contains("Black_King")){//checks if remaining king is a black piece
                JOptionPane.showMessageDialog(null, "Winner!\nBlack Wins!");//opend JOptionPane showing that Black won
            }else JOptionPane.showMessageDialog(null, "Winner!\nWhite Wins!");//opend JOptionPane showing that White won
            
            chess.dispose();//closes chess game when any button on the option pane is pressed
            new MenuJFrame().setVisible(true);//opens main menu
        }
    }
    //Method Header Comments:
    //cancels any previewed moves on the board
    //cycles through all the buttons and checks if they are selected, if so, un-selects them
    public void cancelPreview(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(chess.places[i][j].getIcon().toString().contains("Selected")){
                    //sets selected pieces to un-selected
                    chess.places[i][j].setIcon(new ImageIcon(chess.places[i][j].getIcon().toString().replace("Selected_", "")));
                }
            }
        }
    }
    //Method Header Comments:
    //Checks which piece is selected, based on the piece it will preview the available moves
    //highlights available moves so the player can see where a move can be made
    public void preview(int i, int j){
        if(chess.places[i][j].getIcon().toString().contains("Pawn")){//checks if piece that is selected is a form of pawn
            //Try-Catches are for when a move cant be made, it still allows all moves that are able to be made--
            //example: the knight can move in an L shape, but what if the end result spot it can move to is off of the board, as in the button does not exist,
            //the program will still run in cases like those
            
            //below if checks if the selected pawn is a white pawn and if the user is on the white team
            if(chess.places[i][j].getIcon().toString().contains("White_") && chess.colour.equals("White")){
                if(j == 6){//checks if pawns are in starting position, so they will be able to move two spaces
                    try{
                        //selects the spots ahead of it
                        chess.places[i][j-1].setIcon(new ImageIcon("Selected_" + chess.places[i][j-1].getIcon().toString()));
                        chess.places[i][j-2].setIcon(new ImageIcon("Selected_" + chess.places[i][j-2].getIcon().toString()));
                    }catch(ArrayIndexOutOfBoundsException err){}
                }else{//checks if pawns are not in starting position, so they will be able to move one space
                    try{
                        //selects the spots ahead of it
                        chess.places[i][j-1].setIcon(new ImageIcon("Selected_" + chess.places[i][j-1].getIcon().toString()));
                    }catch(ArrayIndexOutOfBoundsException err){}
                }
                try{
                    if(!(chess.places[i][j-1].getIcon().toString().equals("Selected_Black.png") || chess.places[i][j-1].getIcon().toString().equals("Selected_White.png"))){
                        //replaces selected spots with un-selected spots
                        chess.places[i][j-1].setIcon(new ImageIcon(chess.places[i][j-1].getIcon().toString().replace("Selected_", "")));
                        chess.places[i][j-2].setIcon(new ImageIcon(chess.places[i][j-2].getIcon().toString().replace("Selected_", "")));
                    }
                }catch(ArrayIndexOutOfBoundsException err){}
                try{
                    if(!(chess.places[i][j-2].getIcon().toString().equals("Selected_Black.png") || chess.places[i][j-2].getIcon().toString().equals("Selected_White.png"))){
                        //replaces selected spots with un-selected spots
                        chess.places[i][j-2].setIcon(new ImageIcon(chess.places[i][j-2].getIcon().toString().replace("Selected_", "")));
                    }
                }catch(ArrayIndexOutOfBoundsException err){}
                try{
                    if(!(chess.places[i-1][j-1].getIcon().toString().equals("Black.png") || chess.places[i-1][j-1].getIcon().toString().equals("White.png"))){
                        //selects the spots ahead of it
                        chess.places[i-1][j-1].setIcon(new ImageIcon("Selected_" + chess.places[i-1][j-1].getIcon()));
                    }
                }catch(ArrayIndexOutOfBoundsException err){}
                try{
                    if(!(chess.places[i+1][j-1].getIcon().toString().equals("Black.png") || chess.places[i+1][j-1].getIcon().toString().equals("White.png"))){
                        //selects the spots ahead of it
                        chess.places[i+1][j-1].setIcon(new ImageIcon("Selected_" + chess.places[i+1][j-1].getIcon()));
                    }
                }catch(ArrayIndexOutOfBoundsException err){}
                //below else-if checks if the selected pawn is a black pawn and if the user is on the black team
            }else if(chess.places[i][j].getIcon().toString().contains("Black_") && chess.colour.equals("Black")){
                if(j == 6){//checks if pawns are in starting position, so they will be able to move two spaces
                    try{
                        //selects the spots ahead of it
                        chess.places[i][j-1].setIcon(new ImageIcon("Selected_" + chess.places[i][j-1].getIcon().toString()));
                        chess.places[i][j-2].setIcon(new ImageIcon("Selected_" + chess.places[i][j-2].getIcon().toString()));
                    }catch(ArrayIndexOutOfBoundsException err){}
                }else{//checks if pawns are not in starting position, so they will be able to move one space
                    try{
                        //selects the spots ahead of it
                        chess.places[i][j-1].setIcon(new ImageIcon("Selected_" + chess.places[i][j-1].getIcon().toString()));
                    }catch(ArrayIndexOutOfBoundsException err){}
                }
                try{
                    //checks if the two spaces in front of the pawn are not already filled
                    if(!(chess.places[i][j-1].getIcon().toString().equals("Selected_Black.png") || chess.places[i][j-1].getIcon().toString().equals("Selected_White.png"))){
                        //replaces selected spots with un-selected spots
                        chess.places[i][j-1].setIcon(new ImageIcon(chess.places[i][j-1].getIcon().toString().replace("Selected_", "")));
                        chess.places[i][j-2].setIcon(new ImageIcon(chess.places[i][j-2].getIcon().toString().replace("Selected_", "")));
                    }
                }catch(ArrayIndexOutOfBoundsException err){}
                try{
                    //checks if the space in front of the pawn is not already filled
                    if(!(chess.places[i][j-2].getIcon().toString().equals("Selected_Black.png") || chess.places[i][j-2].getIcon().toString().equals("Selected_White.png"))){
                        //replaces selected spots with un-selected spots
                        chess.places[i][j-2].setIcon(new ImageIcon(chess.places[i][j-2].getIcon().toString().replace("Selected_", "")));
                    }
                }catch(ArrayIndexOutOfBoundsException err){}
                try{
                    if(!(chess.places[i-1][j-1].getIcon().toString().equals("Black.png") || chess.places[i-1][j-1].getIcon().toString().equals("White.png"))){
                        //selects the spots ahead of it
                        chess.places[i-1][j-1].setIcon(new ImageIcon("Selected_" + chess.places[i-1][j-1].getIcon()));
                    }
                }catch(ArrayIndexOutOfBoundsException err){}
                try{
                    if(!(chess.places[i+1][j-1].getIcon().toString().equals("Black.png") || chess.places[i+1][j-1].getIcon().toString().equals("White.png"))){
                        //selects the spots ahead of it
                        chess.places[i+1][j-1].setIcon(new ImageIcon("Selected_" + chess.places[i+1][j-1].getIcon()));
                    }
                }catch(ArrayIndexOutOfBoundsException err){}
            }else{//else if the selected pawn is not of the 1st players team colour
                if(j == 1){//checks if pawns are in starting position, so they will be able to move two spaces
                    try{
                        //selects the spots ahead of it
                        chess.places[i][j+1].setIcon(new ImageIcon("Selected_" + chess.places[i][j+1].getIcon().toString()));
                        chess.places[i][j+2].setIcon(new ImageIcon("Selected_" + chess.places[i][j+2].getIcon().toString()));
                    }catch(ArrayIndexOutOfBoundsException err){}
                }else{
                    try{
                        //selects the spots ahead of it
                        chess.places[i][j+1].setIcon(new ImageIcon("Selected_" + chess.places[i][j+1].getIcon().toString()));
                    }catch(ArrayIndexOutOfBoundsException err){}
                }try{
                    
                    if(!(chess.places[i][j+1].getIcon().toString().equals("Selected_Black.png") || chess.places[i][j+1].getIcon().toString().equals("Selected_White.png"))){
                        //replaces selected spots with un-selected spots
                        chess.places[i][j+1].setIcon(new ImageIcon(chess.places[i][j+1].getIcon().toString().replace("Selected_", "")));
                        chess.places[i][j+2].setIcon(new ImageIcon(chess.places[i][j+2].getIcon().toString().replace("Selected_", "")));
                    }
                }catch(ArrayIndexOutOfBoundsException err){}
                try{
                    if(!(chess.places[i][j+2].getIcon().toString().equals("Selected_Black.png") || chess.places[i][j+2].getIcon().toString().equals("Selected_White.png"))){
                        //replaces selected spots with un-selected spots
                        chess.places[i][j+2].setIcon(new ImageIcon(chess.places[i][j+2].getIcon().toString().replace("Selected_", "")));
                    }
                }catch(ArrayIndexOutOfBoundsException err){}
                try{
                    if(!(chess.places[i-1][j+1].getIcon().toString().equals("Black.png") || chess.places[i-1][j+1].getIcon().toString().equals("White.png"))){
                        //selects the spots ahead of it
                        chess.places[i-1][j+1].setIcon(new ImageIcon("Selected_" + chess.places[i-1][j+1].getIcon()));
                    }
                }catch(ArrayIndexOutOfBoundsException err){}
                try{
                    if(!(chess.places[i+1][j+1].getIcon().toString().equals("Black.png") || chess.places[i+1][j+1].getIcon().toString().equals("White.png"))){
                        //selects the spots ahead of it
                        chess.places[i+1][j+1].setIcon(new ImageIcon("Selected_" + chess.places[i+1][j+1].getIcon()));
                    }
                }catch(ArrayIndexOutOfBoundsException err){}
            }
        }if(chess.places[i][j].getIcon().toString().contains("Rook")){//checks if piece that is selected is a form of Rook
            //Try-Catches are for when a move cant be made, it still allows all moves that are able to be made--
            //example: the knight can move in an L shape, but what if the end result spot it can move to is off of the board, as in the button does not exist,
            //the program will still run in cases like those
            try{
                for(int k = 0; k < 8; k++){//loops through either length or width of the board
                    //checks if any spot in a vertical line above the piece is not empty
                    if(chess.places[i][j-k].getIcon().toString().equals("White.png") || chess.places[i][j-k].getIcon().toString().equals("Black.png")){
                        //selects every empty spot that the rook can move to
                        chess.places[i][j-k].setIcon(new ImageIcon("Selected_" + chess.places[i][j-k].getIcon()));
                    }
                    //checks if colliding spot is an enemy piece
                    if((chess.places[i][j-k].getIcon().toString().startsWith("White_") || chess.places[i][j-k].getIcon().toString().startsWith("Black_")) && chess.places[i][j-k] != firstPos){
                        //selects the opposite colour piece if there is one in the way of the rooks available moves
                        chess.places[i][j-k].setIcon(new ImageIcon("Selected_" + chess.places[i][j-k].getIcon()));
                        break;//stops the loop because the rook cant move past a blockade
                    }
                }
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                for(int k = 0; k < 8; k++){//loops through either length or width of the board
                    //checks if any spot in a vertical line below the piece is not empty
                    if(chess.places[i][j+k].getIcon().toString().equals("White.png") || chess.places[i][j+k].getIcon().toString().equals("Black.png")){
                        //selects every empty spot that the rook can move to
                        chess.places[i][j+k].setIcon(new ImageIcon("Selected_" + chess.places[i][j+k].getIcon()));
                    }
                    //checks if colliding spot is an enemy piece
                    if((chess.places[i][j+k].getIcon().toString().startsWith("White_") || chess.places[i][j+k].getIcon().toString().startsWith("Black_")) && chess.places[i][j+k] != firstPos){
                        //selects the opposite colour piece if there is one in the way of the rooks available moves
                        chess.places[i][j+k].setIcon(new ImageIcon("Selected_" + chess.places[i][j+k].getIcon()));
                        break;//stops the loop because the rook cant move past a blockade
                    }
                }
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                for(int k = 0; k < 8; k++){//loops through either length or width of the board
                    //checks if any spot in a horizontal line to the left of the piece is not empty
                    if(chess.places[i-k][j].getIcon().toString().equals("White.png") || chess.places[i-k][j].getIcon().toString().equals("Black.png")){
                        //selects every empty spot that the rook can move to
                        chess.places[i-k][j].setIcon(new ImageIcon("Selected_" + chess.places[i-k][j].getIcon()));
                    }
                    //checks if colliding spot is an enemy piece
                    if((chess.places[i-k][j].getIcon().toString().startsWith("White_") || chess.places[i-k][j].getIcon().toString().startsWith("Black_")) && chess.places[i-k][j] != firstPos){
                        //selects the opposite colour piece if there is one in the way of the rooks available moves
                        chess.places[i-k][j].setIcon(new ImageIcon("Selected_" + chess.places[i-k][j].getIcon()));
                        break; //stops the loop because the rook cant move past a blockade
                    }
                }
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                for(int k = 0; k < 8; k++){//loops through either length or width of the board
                    //checks if any spot in a horizontal line to the right of the piece is not empty
                    if(chess.places[i+k][j].getIcon().toString().equals("White.png") || chess.places[i+k][j].getIcon().toString().equals("Black.png")){
                        //selects every empty spot that the rook can move to
                        chess.places[i+k][j].setIcon(new ImageIcon("Selected_" + chess.places[i+k][j].getIcon()));
                    }
                    //checks if colliding spot is an enemy piece
                    if((chess.places[i+k][j].getIcon().toString().startsWith("White_") || chess.places[i+k][j].getIcon().toString().startsWith("Black_")) && chess.places[i+k][j] != firstPos){
                        //selects the opposite colour piece if there is one in the way of the rooks available moves
                        chess.places[i+k][j].setIcon(new ImageIcon("Selected_" + chess.places[i+k][j].getIcon()));
                        break;//stops the loop because the rook cant move past a blockade
                    }
                }
            }catch(ArrayIndexOutOfBoundsException err){}
        }if(chess.places[i][j].getIcon().toString().contains("Knight")){//checks if piece that is selected is a form of Knight
            //Try-Catches are for when a move cant be made, it still allows all moves that are able to be made--
            //example: the knight can move in an L shape, but what if the end result spot it can move to is off of the board, as in the button does not exist,
            //the program will still run in cases like those
            
            //selects all available spots a knight can move (two spaces up/down and one space left/right, and vice/versa)
            try{
                chess.places[i+1][j-2].setIcon(new ImageIcon("Selected_" + chess.places[i+1][j-2].getIcon()));
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                chess.places[i-1][j-2].setIcon(new ImageIcon("Selected_" + chess.places[i-1][j-2].getIcon()));
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                chess.places[i+2][j-1].setIcon(new ImageIcon("Selected_" + chess.places[i+2][j-1].getIcon()));
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                chess.places[i+2][j+1].setIcon(new ImageIcon("Selected_" + chess.places[i+2][j+1].getIcon()));
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                chess.places[i+1][j+2].setIcon(new ImageIcon("Selected_" + chess.places[i+1][j+2].getIcon()));
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                chess.places[i-1][j+2].setIcon(new ImageIcon("Selected_" + chess.places[i-1][j+2].getIcon()));
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                chess.places[i-2][j-1].setIcon(new ImageIcon("Selected_" + chess.places[i-2][j-1].getIcon()));
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                chess.places[i-2][j+1].setIcon(new ImageIcon("Selected_" + chess.places[i-2][j+1].getIcon()));
            }catch(ArrayIndexOutOfBoundsException err){}
        }if(chess.places[i][j].getIcon().toString().contains("Bishop")){//checks if piece that is selected is a form of bishop
            //Try-Catches are for when a move cant be made, it still allows all moves that are able to be made--
            //example: the knight can move in an L shape, but what if the end result spot it can move to is off of the board, as in the button does not exist,
            //the program will still run in cases like those
            try{
                for(int k = 0; k < 8; k++){//loop for diagnals
                    //checks if any spot in a diagnal down-right line is empty or not
                    if(chess.places[i+k][j+k].getIcon().toString().equals("White.png") || chess.places[i+k][j+k].getIcon().toString().equals("Black.png")){
                        //selects every empty spot that the bishop can move to
                        chess.places[i+k][j+k].setIcon(new ImageIcon("Selected_" + chess.places[i+k][j+k].getIcon()));
                    }
                    //checks if colliding spot is an enemy piece
                    if((chess.places[i+k][j+k].getIcon().toString().startsWith("White_") || chess.places[i+k][j+k].getIcon().toString().startsWith("Black_")) && chess.places[i+k][j+k] != firstPos){
                        //selects the opposite colour piece if there is one in the way of the bishops available moves
                        chess.places[i+k][j+k].setIcon(new ImageIcon("Selected_" + chess.places[i+k][j+k].getIcon()));
                        break;//stops the loop because the bishop cant move past a blockade
                    }
                }
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                for(int k = 0; k < 8; k++){//loop for diagnals
                    //checks if any spot in a diagnal down-left line is empty or not
                    if(chess.places[i-k][j+k].getIcon().toString().equals("White.png") || chess.places[i-k][j+k].getIcon().toString().equals("Black.png")){
                        //selects every empty spot that the bishop can move to
                        chess.places[i-k][j+k].setIcon(new ImageIcon("Selected_" + chess.places[i-k][j+k].getIcon()));
                    }
                    //checks if colliding spot is an enemy piece
                    if((chess.places[i-k][j+k].getIcon().toString().startsWith("White_") || chess.places[i-k][j+k].getIcon().toString().startsWith("Black_")) && chess.places[i-k][j+k] != firstPos){
                        //selects the opposite colour piece if there is one in the way of the bishops available moves
                        chess.places[i-k][j+k].setIcon(new ImageIcon("Selected_" + chess.places[i-k][j+k].getIcon()));
                        break;//stops the loop because the bishop cant move past a blockade
                    }
                }
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                for(int k = 0; k < 8; k++){//loop for diagnals
                    //checks if any spot in a diagnal up-right line is empty or not
                    if(chess.places[i+k][j-k].getIcon().toString().equals("White.png") || chess.places[i+k][j-k].getIcon().toString().equals("Black.png")){
                        //selects every empty spot that the bishop can move to
                        chess.places[i+k][j-k].setIcon(new ImageIcon("Selected_" + chess.places[i+k][j-k].getIcon()));
                    }
                    //checks if colliding spot is an enemy piece
                    if((chess.places[i+k][j-k].getIcon().toString().startsWith("White_") || chess.places[i+k][j-k].getIcon().toString().startsWith("Black_")) && chess.places[i+k][j-k] != firstPos){
                        //selects the opposite colour piece if there is one in the way of the bishops available moves
                        chess.places[i+k][j-k].setIcon(new ImageIcon("Selected_" + chess.places[i+k][j-k].getIcon()));
                        break;//stops the loop because the bishop cant move past a blockade
                    }
                }
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                for(int k = 0; k < 8; k++){//loop for diagnals
                    //checks if any spot in a diagnal up-left line is empty or not
                    if(chess.places[i-k][j-k].getIcon().toString().equals("White.png") || chess.places[i-k][j-k].getIcon().toString().equals("Black.png")){
                        //selects every empty spot that the bishop can move to
                        chess.places[i-k][j-k].setIcon(new ImageIcon("Selected_" + chess.places[i-k][j-k].getIcon()));
                    }
                    //checks if colliding spot is an enemy piece
                    if((chess.places[i-k][j-k].getIcon().toString().startsWith("White_") || chess.places[i-k][j-k].getIcon().toString().startsWith("Black_")) && chess.places[i-k][j-k] != firstPos){
                        //selects the opposite colour piece if there is one in the way of the bishops available moves
                        chess.places[i-k][j-k].setIcon(new ImageIcon("Selected_" + chess.places[i-k][j-k].getIcon()));
                        break;//stops the loop because the bishop cant move past a blockade
                    }
                }
            }catch(ArrayIndexOutOfBoundsException err){}
        }if(chess.places[i][j].getIcon().toString().contains("King")){//checks if piece that is selected is a form of King
            //Try-Catches are for when a move cant be made, it still allows all moves that are able to be made--
            //example: the knight can move in an L shape, but what if the end result spot it can move to is off of the board, as in the button does not exist,
            //the program will still run in cases like those
            
            //selects the eight surrounding places as long as the arent the same colour as the king
            try{
                if(!chess.places[i-1][j].getIcon().toString().contains("White_") && chess.colour.equals("White") || !chess.places[i-1][j].getIcon().toString().contains("Black_") && chess.colour.equals("Black")){
                    chess.places[i-1][j].setIcon(new ImageIcon("Selected_" + chess.places[i-1][j].getIcon()));
                }
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                if(!chess.places[i+1][j].getIcon().toString().contains("White_") && chess.colour.equals("White") || !chess.places[i+1][j].getIcon().toString().contains("Black_") && chess.colour.equals("Black")){
                    chess.places[i+1][j].setIcon(new ImageIcon("Selected_" + chess.places[i+1][j].getIcon().toString()));
                }
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                if(!chess.places[i][j-1].getIcon().toString().contains("White_") && chess.colour.equals("White") || !chess.places[i][j-1].getIcon().toString().contains("Black_") && chess.colour.equals("Black")){
                    chess.places[i][j-1].setIcon(new ImageIcon("Selected_" + chess.places[i][j-1].getIcon()));
                }
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                if(!chess.places[i][j+1].getIcon().toString().contains("White_") && chess.colour.equals("White") || !chess.places[i][j+1].getIcon().toString().contains("Black_") && chess.colour.equals("Black")){
                    chess.places[i][j+1].setIcon(new ImageIcon("Selected_" + chess.places[i][j+1].getIcon()));
                }
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                if(!chess.places[i-1][j-1].getIcon().toString().contains("White_") && chess.colour.equals("White") || !chess.places[i-1][j-1].getIcon().toString().contains("Black_") && chess.colour.equals("Black")){
                    chess.places[i-1][j-1].setIcon(new ImageIcon("Selected_" + chess.places[i-1][j-1].getIcon()));
                }
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                if(!chess.places[i-1][j+1].getIcon().toString().contains("White_") && chess.colour.equals("White") || !chess.places[i-1][j+1].getIcon().toString().contains("Black_") && chess.colour.equals("Black")){
                    chess.places[i-1][j+1].setIcon(new ImageIcon("Selected_" + chess.places[i-1][j+1].getIcon()));
                }
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                if(!chess.places[i+1][j-1].getIcon().toString().contains("White_") && chess.colour.equals("White") || !chess.places[i+1][j-1].getIcon().toString().contains("Black_") && chess.colour.equals("Black")){
                    chess.places[i+1][j-1].setIcon(new ImageIcon("Selected_" + chess.places[i+1][j-1].getIcon()));
                }
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                if(!chess.places[i+1][j+1].getIcon().toString().contains("White_") && chess.colour.equals("White") || !chess.places[i+1][j+1].getIcon().toString().contains("Black_") && chess.colour.equals("Black")){
                    chess.places[i+1][j+1].setIcon(new ImageIcon("Selected_" + chess.places[i+1][j+1].getIcon()));
                }
            }catch(ArrayIndexOutOfBoundsException err){}
        }if(chess.places[i][j].getIcon().toString().contains("Queen")){//checks if piece that is selected is a form of Queen
            //Try-Catches are for when a move cant be made, it still allows all moves that are able to be made--
            //example: the knight can move in an L shape, but what if the end result spot it can move to is off of the board, as in the button does not exist,
            //the program will still run in cases like those
            
            //Queen code is just combined Rook and Bishop code, for documentation see Rook and Bishop code
            
            try{
                for(int k = 0; k < 8; k++){
                    if(chess.places[i][j-k].getIcon().toString().equals("White.png") || chess.places[i][j-k].getIcon().toString().equals("Black.png")){
                        chess.places[i][j-k].setIcon(new ImageIcon("Selected_" + chess.places[i][j-k].getIcon()));
                    }if((chess.places[i][j-k].getIcon().toString().startsWith("White_") || chess.places[i][j-k].getIcon().toString().startsWith("Black_")) && chess.places[i][j-k] != firstPos){
                        chess.places[i][j-k].setIcon(new ImageIcon("Selected_" + chess.places[i][j-k].getIcon()));
                        break;
                    }
                }
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                for(int k = 0; k < 8; k++){
                    if(chess.places[i][j+k].getIcon().toString().equals("White.png") || chess.places[i][j+k].getIcon().toString().equals("Black.png")){
                        chess.places[i][j+k].setIcon(new ImageIcon("Selected_" + chess.places[i][j+k].getIcon()));
                    }if((chess.places[i][j+k].getIcon().toString().startsWith("White_") || chess.places[i][j+k].getIcon().toString().startsWith("Black_")) && chess.places[i][j+k] != firstPos){
                        chess.places[i][j+k].setIcon(new ImageIcon("Selected_" + chess.places[i][j+k].getIcon()));
                        break;
                    }
                }
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                for(int k = 0; k < 8; k++){
                    if(chess.places[i-k][j].getIcon().toString().equals("White.png") || chess.places[i-k][j].getIcon().toString().equals("Black.png")){
                        chess.places[i-k][j].setIcon(new ImageIcon("Selected_" + chess.places[i-k][j].getIcon()));
                    }if((chess.places[i-k][j].getIcon().toString().startsWith("White_") || chess.places[i-k][j].getIcon().toString().startsWith("Black_")) && chess.places[i-k][j] != firstPos){
                        chess.places[i-k][j].setIcon(new ImageIcon("Selected_" + chess.places[i-k][j].getIcon()));
                        break;
                    }
                }
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                for(int k = 0; k < 8; k++){
                    if(chess.places[i+k][j].getIcon().toString().equals("White.png") || chess.places[i+k][j].getIcon().toString().equals("Black.png")){
                        chess.places[i+k][j].setIcon(new ImageIcon("Selected_" + chess.places[i+k][j].getIcon()));
                    }if((chess.places[i+k][j].getIcon().toString().startsWith("White_") || chess.places[i+k][j].getIcon().toString().startsWith("Black_")) && chess.places[i+k][j] != firstPos){
                        chess.places[i+k][j].setIcon(new ImageIcon("Selected_" + chess.places[i+k][j].getIcon()));
                        break;
                    }
                }
            }catch(ArrayIndexOutOfBoundsException err){}
            
            try{
                for(int k = 0; k < 8; k++){
                    if(chess.places[i+k][j+k].getIcon().toString().equals("White.png") || chess.places[i+k][j+k].getIcon().toString().equals("Black.png")){
                        chess.places[i+k][j+k].setIcon(new ImageIcon("Selected_" + chess.places[i+k][j+k].getIcon()));
                    }if((chess.places[i+k][j+k].getIcon().toString().startsWith("White_") || chess.places[i+k][j+k].getIcon().toString().startsWith("Black_")) && chess.places[i+k][j+k] != firstPos){
                        chess.places[i+k][j+k].setIcon(new ImageIcon("Selected_" + chess.places[i+k][j+k].getIcon()));
                        break;
                    }
                }
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                for(int k = 0; k < 8; k++){
                    if(chess.places[i-k][j+k].getIcon().toString().equals("White.png") || chess.places[i-k][j+k].getIcon().toString().equals("Black.png")){
                        chess.places[i-k][j+k].setIcon(new ImageIcon("Selected_" + chess.places[i-k][j+k].getIcon()));
                    }if((chess.places[i-k][j+k].getIcon().toString().startsWith("White_") || chess.places[i-k][j+k].getIcon().toString().startsWith("Black_")) && chess.places[i-k][j+k] != firstPos){
                        chess.places[i-k][j+k].setIcon(new ImageIcon("Selected_" + chess.places[i-k][j+k].getIcon()));
                        break;
                    }
                }
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                for(int k = 0; k < 8; k++){
                    if(chess.places[i+k][j-k].getIcon().toString().equals("White.png") || chess.places[i+k][j-k].getIcon().toString().equals("Black.png")){
                        chess.places[i+k][j-k].setIcon(new ImageIcon("Selected_" + chess.places[i+k][j-k].getIcon()));
                    }if((chess.places[i+k][j-k].getIcon().toString().startsWith("White_") || chess.places[i+k][j-k].getIcon().toString().startsWith("Black_")) && chess.places[i+k][j-k] != firstPos){
                        chess.places[i+k][j-k].setIcon(new ImageIcon("Selected_" + chess.places[i+k][j-k].getIcon()));
                        break;
                    }
                }
            }catch(ArrayIndexOutOfBoundsException err){}
            try{
                for(int k = 0; k < 8; k++){
                    if(chess.places[i-k][j-k].getIcon().toString().equals("White.png") || chess.places[i-k][j-k].getIcon().toString().equals("Black.png")){
                        chess.places[i-k][j-k].setIcon(new ImageIcon("Selected_" + chess.places[i-k][j-k].getIcon()));
                    }if((chess.places[i-k][j-k].getIcon().toString().startsWith("White_") || chess.places[i-k][j-k].getIcon().toString().startsWith("Black_")) && chess.places[i-k][j-k] != firstPos){
                        chess.places[i-k][j-k].setIcon(new ImageIcon("Selected_" + chess.places[i-k][j-k].getIcon()));
                        break;
                    }
                }
            }catch(ArrayIndexOutOfBoundsException err){}
        }
        
        //resets all buttons to unselected if the turn is over
        for(int k = 0; k < 8; k++){
            for(int l = 0; l < 8; l++){
                if(chess.colour.equals("Black") && chess.places[k][l].getIcon().toString().startsWith("Selected_Black_") && moves % 2 == 1){
                    chess.places[k][l].setIcon(new ImageIcon(chess.places[k][l].getIcon().toString().replace("Selected_", "")));
                }if(chess.colour.equals("White") && chess.places[k][l].getIcon().toString().startsWith("Selected_White_") && moves % 2 == 0){
                    chess.places[k][l].setIcon(new ImageIcon(chess.places[k][l].getIcon().toString().replace("Selected_", "")));
                }if(chess.colour.equals("Black") && chess.places[k][l].getIcon().toString().startsWith("Selected_White_") && moves % 2 == 0){
                    chess.places[k][l].setIcon(new ImageIcon(chess.places[k][l].getIcon().toString().replace("Selected_", "")));
                }if(chess.colour.equals("White") && chess.places[k][l].getIcon().toString().startsWith("Selected_Black_") && moves % 2 == 1){
                    chess.places[k][l].setIcon(new ImageIcon(chess.places[k][l].getIcon().toString().replace("Selected_", "")));
                }
            }
        }
        chess.places[i][j].setIcon(new ImageIcon("Selected_" + chess.places[i][j].getIcon()));//selects current piece
    }
    //Method Header Comments:
    //moves the selected buttons icon to the second selected button spot
    public void move(JButton frst, JButton scnd){
        String icon1 = String.valueOf(frst.getIcon());//saves first buttons icon
        icon1 = icon1.replace("Selected_", "");//changes icon to selected version
        String icon2 = String.valueOf(scnd.getIcon());//saves second buttons icon
        if(icon1.endsWith("White.png")){//checks if button one is on a white square
            frst.setIcon(chess.images[1]);//changes the previous position of button to an empty white space
        }else frst.setIcon(chess.images[0]);//changes the previous position of button to an empty black space
        if(icon2.endsWith("White.png")){//checks if piece is moving to a white square
            if(icon1.endsWith("Black.png")){//checks if icon had a black background
                icon1 = icon1.replace("Black.png", "White.png");//changes black background to white
                scnd.setIcon(new ImageIcon(icon1));//sets the buttons icon
            }else if(icon1.endsWith("White.png")){//checks if icon had a white background
                scnd.setIcon(new ImageIcon(icon1));//changes nothing and sets buttons icon
            }
        }else if(icon2.endsWith("Black.png")){//checks if piece is moving to a space with a black background
            if(icon1.endsWith("White.png")){//checks if piece had a white background
                icon1 = icon1.replace("White.png", "Black.png");//change background to black
                scnd.setIcon(new ImageIcon(icon1));//sets buttons icon
            }else if(icon1.endsWith("Black.png")){//checks if piece had a black background
                scnd.setIcon(new ImageIcon(icon1));//changes nothing and sets buttons icon
            }
        }
    }
    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {}
}
