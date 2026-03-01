/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memory.memory;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import javafx.animation.PauseTransition;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import memory.memory.MemoryCard.ShapeStyle;

/**
 *
 * @author Captain Arkeo
 */
public class CardGrid extends GridPane{
    
    //Les cartes créées lors du début de la partie
    private MemoryCard[] cards;
    
    //Les différentes formes disponibles
    private final ShapeStyle[] shapes = {ShapeStyle.CIRCLE, ShapeStyle.SQUARE, ShapeStyle.TRIANGLE};
    
    //Les différentes couleurs disponibles
    private final Color[] colors = {Color.RED, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.BLUE};
    
    //La carte qui était active avant qu'une nouvelle carte ne soit cliquée. Servira à tester les paires
    private MemoryCard activeCard;
    
    //L'application
    private MemoryGame parent;
    
    
    //Lorsque l'on crée une CardGrid, on doit donner le nombre de carte par ligne et par colonne
    //Les nombres possibles donnés à l'utilisateur sont (6, 5) = 15 paires, (5, 4) = 10 paires, (4, 3) = 6 paires, (3, 2) = 3 paires
    //newParent correspond à l'Application de cette grille
    public CardGrid(int cardPerLine, int cardPerColumn, MemoryGame newParent){
        super();
        
        this.parent = newParent;
        
        //Espaces entre les éléments
        this.setHgap(3);
        this.setVgap(3);
        
        //On crée les cartes ici.
        //
        this.createCards(cardPerLine * cardPerColumn / 2);
        this.shuffleCards();
        this.placeCards(cardPerLine, cardPerColumn);
        
    }
    
    //Initialise le tableau de cartes en fonction du nombre de paires.
    private void createCards(int nbPairs){
        this.cards = new MemoryCard[nbPairs * 2];
        int createdPairs = 0;
        
        //Jusqu'à ce qu'on ait assez de pairs ou qu'on ait finit d'explorer les tableaux, on crée une nouvelle carte avec la forme et la couleur courant des tableaux
        for(int i = 0; i < this.shapes.length && createdPairs < nbPairs; i++){
            
            for(int j = 0; j < this.colors.length && createdPairs < nbPairs; j++){
                
                //Première carte de la paire
                MemoryCard newCard = new MemoryCard(this.shapes[i], this.colors[j], this);
                this.cards[createdPairs] = newCard;
                //Deuxième carte de la paire. Mise dans la seconde partie du tableau
                this.cards[createdPairs + nbPairs] = new MemoryCard(this.shapes[i], this.colors[j], this, newCard.getCardId());
                
                createdPairs++;
            }
        }
    }
    
    //Utilise la méthode de Fisher-Yates pour mélanger les cartes.
    //On commence à la carte de la fin et on l'échange avec une carte aléatoire et ainsi de suite
    private void shuffleCards(){
        Random rand = new Random();
        for(int i = this.cards.length - 1; i > 0; i--){
            //La valeur donnée à nextInt() n'est pas incluse
            //Or, pour que la carte ne change pas d'emplacement, on voudrait que son indexe soit inclus
            int randIndex = rand.nextInt(i + 1);
            
            //randIndex == i veut dire qu'on échange un élément avec lui-même, ce qui srait inutile
            if(randIndex != i){
                //Ne pas oublier de garder la carte courante pour pouvoir échanger
                MemoryCard current = this.cards[randIndex];
                this.cards[randIndex] = this.cards[i];
                this.cards[i] = current;
            }
        }
    }
    
    //Cette méthode mets en place les cartes sur la grille
    private void placeCards(int cardPerLine, int cardPerColumn){
        for(int i = 0; i < cardPerLine; i++){
            for(int j = 0; j < cardPerColumn; j++){
                this.add(this.cards[i * cardPerColumn + j], i, j);
            }
        }
    }
    
    //Cette fonction est appelée dès qu'on clique sur une carte
    //En fonction de si le joueur a cliqué sur le bouton de swap ou non, la méthode correspondante est appelée
    public void handleCheck(MemoryCard clickedCard){
        //Le joueur a-t-il le droit de cliquer sur les cartes
        if(this.parent.isPlayerAllowedInputs()){
            if(!this.parent.isPlayerSwapping()){
                handlePairs(clickedCard);
            } else {
                handleSwap(clickedCard);
            }
        }
        
    }
    
    //Si le joueur est en train d'échanger les cartes, cette fonction est appelée
    private void handleSwap(MemoryCard clickedCard){
        clickedCard.selectSwap();

        if (this.activeCard == null) {
            this.activeCard = clickedCard;
            this.parent.forceOrFreePlayerFromPlaying();
        } else {
            
            //On récupères les positions des éléments
            Integer row1 = GridPane.getRowIndex(clickedCard);
            Integer col1 = GridPane.getColumnIndex(clickedCard);
            Integer row2 = GridPane.getRowIndex(this.activeCard);
            Integer col2 = GridPane.getColumnIndex(this.activeCard);


            //On swap les cartes
            GridPane.setRowIndex(clickedCard, row2);
            GridPane.setColumnIndex(clickedCard, col2);

            GridPane.setRowIndex(this.activeCard, row1);
            GridPane.setColumnIndex(this.activeCard, col1);

            //On déselectionne les cartes
            this.activeCard.unselectSwap();
            clickedCard.unselectSwap();
            this.activeCard = null;
            
            this.parent.toggleSwapping();
            this.parent.forceOrFreePlayerFromPlaying();
        }
    }
    
    //Si le joueur est en train de jouer normalement, cette fonction est appelée.
    //Elle permet de tester si les cartes forment une pair.
    private void handlePairs(MemoryCard clickedCard){
        clickedCard.turn();
        if(this.activeCard == null){
            //Si c'est la première carte retournée, on la sauvegarde et on force le joueur à finir son tour
            this.activeCard = clickedCard;
            this.parent.forceOrFreePlayerFromPlaying();
        } else {
            //Si les cartes ne sont pas paires (id = id), on les inverse
            if(!clickedCard.compare(this.activeCard)){
                //On crée une pause de 1 seconde lorsque les cartes ne sont pas paires avant de les inverser
                //Cela laisse le temps au joueur de voir son erreur et de se souvenir des emplacements
                this.parent.disallowOrAllowInputs();
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(e -> {
                    //Une fois la pause terminée, on retourne les cartes et redonne au joueur sa liberté de passer son tour et d'intéragir
                    this.activeCard.turn();
                    clickedCard.turn();
                    this.activeCard = null;
                    this.parent.disallowOrAllowInputs();
                    this.parent.forceOrFreePlayerFromPlaying();
                });
                pause.play();
                
                //Comme dans un vrai jeu de memory, on passe au joueur suivant si on ne trouve pas de paires, sinon on continue.
                this.parent.nextPlayerTurn();
            } else {
                //Si les cartes forment une paire, elle reste retournées et le joueur récupère son droit de passer son tour
                //Aussi, le joueur gagne un point
                this.activeCard = null;
                this.parent.getCurrentPlayerCard().incrementScore();
                this.parent.gameOver(this.parent.incrementPairsFound());
                this.parent.newLastPlayer();
                this.parent.forceOrFreePlayerFromPlaying();
            }
        }
    }
}
