/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memory.memory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import memory.glues.HorizontalGlue;

/**
 *
 * @author Captain Arkeo
 */
public class BottomButtons extends HBox{
    private final MemoryGame parent;
    
    public BottomButtons(MemoryGame newParent){
        super(20);
        
        this.parent = newParent;
        
        //Les boutons doivent apparaître à droite
        HorizontalGlue hGlue = new HorizontalGlue();
        
        //Bouton permettant à un joueur de mélanger des cartes
        Button swapCards = new Button("Swap cards");
        swapCards.setOnAction(event -> {
            if(this.parent.isPlayerAllowedInputs() && !this.parent.isPlayerForcedToPlay()){
                //Si c'est le joueur avec le plus petit score, il a bien accès au bouton.
                if(this.parent.isCurrentPlayerLast()){
                    this.parent.toggleSwapping();
                }
                
            }
        });
        
        //Bouton permettant de passer au joueur suivant
        Button nextPlayer = new Button("Next player");
        nextPlayer.setOnAction(event -> {
            //Si le joueur n'a pas le droit d'intéragir ou de passer son tour, on ne passe pas au joueur suivant.
            if(this.parent.isPlayerAllowedInputs() && !this.parent.isPlayerForcedToPlay()){
                this.parent.nextPlayerTurn();
            }
        });
        
        //Le bouton permettant de quitter la partie
        //Ce bouton fonctionne même si le joueur n'a pas le droit d'intéragir avec la fenêtre
        Button quit = new Button("Quit the game");
        quit.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
        
        this.getChildren().addAll(hGlue, swapCards, nextPlayer, quit);
    }
}
