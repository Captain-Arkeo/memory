/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memory.memory;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import memory.glues.HorizontalGlue;

/**
 *
 * @author Captain Arkeo
 */
public class PlayerCard extends HBox {
    
    //Le label contenant le nom du joueur.
    private Label nameLabel;
    //Le label contenant le score.
    private Label scoreLabel;
    //Le score est stocké en dehors du label pour pouvoir faire des 
    private int score = 0;
    
    public PlayerCard(String name) {
        super();
        
        //On crée le label du nom et on lui mets une bordure noire fine et droite.
        this.nameLabel = new Label(name);
        this.nameLabel.setStyle(
                "-fx-border-color: black ;"
                + "-fx-border-width: 3 ;"
                + "-fx-border-style: solid ;"
                + "-fx-padding: 5 ;"
        );
        //Un espace de 10 à la droite du label
        HBox.setMargin(this.nameLabel, new Insets(0, 10, 0, 0));
        
        //On crée le label du nom et on lui mets une bordure noire fine et droite.
        this.scoreLabel = new Label("0");
        this.scoreLabel.setStyle(
                "-fx-border-color: black ;"
                + "-fx-border-width: 3 ;"
                + "-fx-border-style: solid ;"
                + "-fx-padding: 5 ;"
        );
        
        //Un label qui affiche juste le text "score" au milieu des deux labels
        Label middleLabel = new Label("Score");
        //Un espace de 10 à la droite du label
        HBox.setMargin(middleLabel, new Insets(0, 10, 0, 0));
        
        /*//Les espaces entre les labels se remplissent automatiquement.
        HorizontalGlue hglue1 = new HorizontalGlue();
        HorizontalGlue hglue2 = new HorizontalGlue();*/
        
        this.getChildren().addAll(this.nameLabel, middleLabel, this.scoreLabel);
        //On applique un style similaire aux labels, mais avec un padding
        this.setStyle(
                "-fx-border-color: black ;"
                + "-fx-border-width: 3 ;"
                + "-fx-border-style: solid ;"
                + "-fx-padding: 20 ;"
        );
    }
    
    //Augmente le score du joueur de 1
    public void incrementScore() {
        this.score++;
        this.scoreLabel.setText("" + score);
    }
    
    //Retourne le score du joueur
    public int getScore(){
        return this.score;
    }
    
    //Permet de dire si c'est le tour du joueur (change le background en jaune)
    public void select() {
        this.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
    }
    
    public void unselect() {
        this.setBackground(null);
    }
    
    
}
