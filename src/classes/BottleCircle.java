package classes;

import javafx.scene.shape.Circle;

public class BottleCircle extends Circle {
    private Bottle bottle;

    public BottleCircle(double radius, Bottle bottle)
    {
        super(radius);
        this.bottle = bottle;
    }

    public Bottle getBottle() {
        return bottle;
    }
}
