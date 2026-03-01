/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memory.memory;

import memory.memory.CardGrid;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 *
 * @author Captain Arkeo
 */
public class MemoryCard extends Pane {
    
    public enum ShapeStyle {
        SQUARE, TRIANGLE, CIRCLE
    }
    
    //Indique si la carte est sélectionnée
    private boolean selected = false;
    
    //Le nombre de pairs déjà créées.
    private static int nbPairs = 0;
    
    //L'id permet de reconnaître la carte. Deux cartes peuvent partager le même id si elles ont le même contenu
    private final int id;
    
    //La forme, à remettre sur la carte lorsqu'elle est face visible
    private Shape myShape;
    
    //Correspond à la taille de chaque côté de la carte
    private static final double CARD_SIZE = 100;
    
    //On récupère le parent de cette carte pour pouvoir intéragir avec celui-ci
    private CardGrid parent;
    
    //Ce constructeur est à appeler pour la première carte d'une paire
    public MemoryCard(ShapeStyle shape, Color color, CardGrid newParent){
        super();
        nbPairs++;
        this.id = nbPairs;
        this.parent = newParent;
        this.makeCard(shape, color);
    }
    
    //Ce constructeur est à appeler pour la deuxième carte d'une paire.
    //copiedId correspond à l'id de la première carte de la paire
    public MemoryCard(ShapeStyle shape, Color color, CardGrid newParent, int copiedId){
        super();
        this.id = copiedId;
        this.parent = newParent;
        this.makeCard(shape, color);
    }
    
    public int getCardId() {
        return this.id;
    }
    
    public void turn(){
        if(this.selected){
            this.getChildren().remove(this.myShape);
            this.selected = false;
        } else {
            this.getChildren().add(this.myShape);
            this.selected = true;
        }
    }
    
    //Si la carte est sélectionnée lors d'un swap, on change la couleur de la bordure
    public void selectSwap(){
        this.setStyle("-fx-border-color: magenta ;"
                + "-fx-border-width: 3 ;"
                + "-fx-border-style: solid");
    }
    
    //Si la carte est déselectionnée suite à un swap, on remets la couleur de bordure à noir
    public void unselectSwap(){
        this.setStyle("-fx-border-color: black ;"
                + "-fx-border-width: 1 ;"
                + "-fx-border-style: solid");
    }
    
    //Cette fonction crée la carte en question.
    //En fonction de la forme (shape) et de la couleur (color), on mets à jour myShape
    private void makeCard(ShapeStyle shape, Color color) {
        this.setMinSize(CARD_SIZE, CARD_SIZE);
        this.unselectSwap(); //Même si cette fonction est surtout là pour les swap, mettre la bordure à noir est ce qu'il nous faut ici. Dans ce cas, on l'appelle.
        this.createShape(shape, color);
        
        //Lorsqu'on clique sur une carte, on vérifie si elle est déjà retournée.
        //Si non, on utilise handleCheck de sa grille parente afin de voir s'il y a une paire ou non
        this.setOnMouseClicked(event -> {
            if(!this.selected){
                parent.handleCheck(this);
            }
        });
    }
    
    //Cette fonction est appelée par makeCard pour créer la forme à l'intérieur de la carte
    private void createShape(ShapeStyle shape, Color color){
        switch(shape){
            case CIRCLE :
                //On crée un cercle se situant au milieu de la carte
                Circle circle = new Circle(
                        CARD_SIZE / 2, 
                        CARD_SIZE / 2, 
                        CARD_SIZE / 4
                );  //(50, 50) et rayon = 25 pour CARD_SIZE = 100
                this.myShape = circle;
                this.myShape.setFill(color);
                break;
            case TRIANGLE :
                //On crée un polygone possèdant trois points.
                //Les points sont placés de telle sorte qu'ils forment un triangle avec le sommet au centre
                Polygon triangle = new Polygon();
                triangle.getPoints().addAll(new Double[]{
                    CARD_SIZE / 4, CARD_SIZE * 0.75,   //(25, 75) pour CARD_SIZE = 100
                    CARD_SIZE / 2, CARD_SIZE / 2,      //(50, 50) pour CARD_SIZE = 100
                    CARD_SIZE * 0.75, CARD_SIZE * 0.75,//(75, 75) pour CARD_SIZE
                });
                this.myShape = triangle;
                this.myShape.setFill(color);
                break;
            case SQUARE :
                //On crée un carré se situant au milieu
                Rectangle square = new Rectangle(
                        CARD_SIZE / 4, 
                        CARD_SIZE / 4, 
                        CARD_SIZE / 2, 
                        CARD_SIZE / 2
                ); //(25, 25) avec largeur = 50 et longueur = 50 pour CARD_SIZE = 100
                this.myShape = square;
                this.myShape.setFill(color);
                break;
            default :
                break;
        }
    }
    
    public boolean compare(MemoryCard otherCard){
        return this.getCardId() == otherCard.getCardId();
    }
}
