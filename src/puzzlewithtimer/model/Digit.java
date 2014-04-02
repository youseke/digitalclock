package puzzlewithtimer.model;

import javafx.scene.Parent;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Shear;

/**
 * Simple 7 segment LED style digit. It supports the numbers 0 through 9.
 */
public final class Digit extends Parent {

    private static final boolean[][] DIGIT_COMBINATIONS = new boolean[][]{
        new boolean[]{true, false, true, true, true, true, true},
        new boolean[]{false, false, false, false, true, false, true},
        new boolean[]{true, true, true, false, true, true, false},
        new boolean[]{true, true, true, false, true, false, true},
        new boolean[]{false, true, false, true, true, false, true},
        new boolean[]{true, true, true, true, false, false, true},
        new boolean[]{true, true, true, true, false, true, true},
        new boolean[]{true, false, false, false, true, false, true},
        new boolean[]{true, true, true, true, true, true, true},
        new boolean[]{true, true, true, true, true, false, true}};
    private final double[][] defaultSizes = {
        {2, 0, 52, 0, 42, 10, 12, 10},
        {12, 49, 42, 49, 52, 54, 42, 59, 12f, 59f, 2f, 54f},
        {12, 98, 42, 98, 52, 108, 2, 108},
        {0, 2, 10, 12, 10, 47, 0, 52},
        {44, 12, 54, 2, 54, 52, 44, 47},
        {0, 56, 10, 61, 10, 96, 0, 106},
        {44, 61, 54, 56, 54, 106, 44, 96}
    };
    private Polygon[] polygons;
    private final Color onColor;
    private final Color offColor;
    private final Effect onEffect;
    private final Effect offEffect;

    public Digit(Color onColor, Color offColor, Effect onEffect, Effect offEffect, double sizeRatio) {
        this.onColor = onColor;
        this.offColor = offColor;
        this.onEffect = onEffect;
        this.offEffect = offEffect;
        this.setPolygonSize(sizeRatio);
        getChildren().addAll(polygons);
        getTransforms().add(new Shear(-0.1, 0));
        showNumber(0);
    }

    public void showNumber(Integer num) {
        if (num < 0 || num > 9) {
            num = 0; // default to 0 for non-valid numbers
        }
        for (int i = 0; i < 7; i++) {
            polygons[i].setFill(DIGIT_COMBINATIONS[num][i] ? onColor : offColor);
            polygons[i].setEffect(DIGIT_COMBINATIONS[num][i] ? onEffect : offEffect);
        }
    }

    private void setPolygonSize(double ratio) {
        for (double[] size : defaultSizes) {
            for (int i = 0; i < size.length; i++) {
                size[i] = size[i] * ratio;
            }
        }
        polygons = new Polygon[]{
            new Polygon(defaultSizes[0]),
            new Polygon(defaultSizes[1]),
            new Polygon(defaultSizes[2]),
            new Polygon(defaultSizes[3]),
            new Polygon(defaultSizes[4]),
            new Polygon(defaultSizes[5]),
            new Polygon(defaultSizes[6])};
    }
}
