/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cit260.piggysRevenge.view;

import cit260.piggysRevenge.control.MapControl;
import cit260.piggysRevenge.control.MiniGameControl;
import cit260.piggysRevenge.exceptions.MapControlException;
import cit260.piggysRevenge.exceptions.MiniGameControlException;
import cit260.piggysRevenge.model.MiniGame;
import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;
import piggysrevenge.PiggysRevenge;

/**
 *
 * @author natebolton
 */
class MiniGameView extends View {
    
    //private final String miniGameIntro;
    private final MiniGame miniGame;
    private final int comboLength;
    
    public MiniGameView(MiniGame miniGame) {
        super("\n\nGuess the correct order (ie. 1234, or 3241, etc.), or Q to quit: ");
        this.console.println("\n-----------------------------------------------------------------"
                + "\nIn this game, you will guess the corect order of four numbers. (1 2 3 4)"
                + "\nThe correct order is chosen at random.  You will enter a four"
                + "\ndigit number and then you will be told how many digits"
                + "\nare in the correct order.  You win when you guess the correct order."
                + "\n-----------------------------------------------------------------");
        this.miniGame = miniGame;
        this.comboLength = miniGame.getComboOrder().length();
    }

    @Override
    public boolean doAction(String userGuess) {
        // this.console.println("\n*** doAction() function called ***");
        userGuess = userGuess.toUpperCase();
        
        if ("Q".equals(userGuess)) {
            return true;
        }
        
        int result = -1;
        try {
            result = MiniGameControl.checkKeyOrder(userGuess, this.miniGame.getComboOrder());
        } catch (MiniGameControlException ex) {
            this.console.println(ex.getMessage());
            return false;
        }
        
        switch (result) {
            case 0:
            case 1:
            case 2:
            case 3:
                this.console.println("\n-----------------------------------------------------------------"
                        + "\nYou have " + String.valueOf(result) + " digits in the correct order."
                        + "\n-----------------------------------------------------------------");
                break;
            case 4:
                this.console.println("\n-----------------------------------------------------------------"
                        + "\nYou win! The correct order was "
                        + String.valueOf(this.miniGame.getComboOrder())
                        + "\n-----------------------------------------------------------------");
                PiggysRevenge.getCurrentGame().getPlayer().setHasEaten(true);
                return true;
        }
        Point playerLoc = PiggysRevenge.getCurrentGame().getPlayer().getCoordinates();
        try {
            MapControl.movePlayer(playerLoc,"");
        } catch (MapControlException ex) {
            this.console.println(ex.getMessage());
        }
        Point wolfLoc = PiggysRevenge.getCurrentGame().getWolf().getCoordinates();
        if (PiggysRevenge.getCurrentGame().getPlayer().getCurrentShoes() != null) {
            if (
                "Sneakers".equals(PiggysRevenge.getCurrentGame().getPlayer().getCurrentShoes().getName())
                && !PiggysRevenge.getCurrentGame().isWolfMovesThisTurn()
               ) {
                this.console.println("You're Wearing Sneakers --> The wolf didn't move this turn.");
            } else {
                this.console.println("The Wolf moved.");
                MapControl.moveWolf(wolfLoc);                
            }
        } else {
                this.console.println("The Wolf moved.");
                MapControl.moveWolf(wolfLoc);                
        }
        //check for wolf-player collision
        if (playerLoc.x == wolfLoc.x && playerLoc.y == wolfLoc.y) {
            WolfView wolfView = new WolfView();
            wolfView.display();
        }
        return false;  
    }
}
