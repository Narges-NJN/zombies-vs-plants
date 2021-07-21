package util;

import javafx.scene.Node;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;

public class Style {
    public static void Glow (Node source, double val){
        Glow glow=new Glow();
        source.setEffect(glow);
        glow.setLevel(val);
    }
}
