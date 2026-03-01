/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memory.glues;

import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 *
 * @author Captain Arkeo
 */
public class VerticalGlue extends Region {
    //JavaFX ne propose pas de verticalGlue. Ainsi, ce composant vient répliquer son comportement.
    public VerticalGlue() {
        super();
        VBox.setVgrow(this, Priority.ALWAYS);
    }
}
