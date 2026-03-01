/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memory.glues;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 *
 * @author Captain Arkeo
 */
public class HorizontalGlue extends Region {
    
    //JavaFX ne propose pas d'horizontalGlue. Ainsi, ce composant vient répliquer son comportement.
    public HorizontalGlue() {
        super();
        HBox.setHgrow(this, Priority.ALWAYS);
    }
}
