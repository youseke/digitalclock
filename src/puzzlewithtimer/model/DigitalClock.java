package puzzlewithtimer.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Calendar;
import javafx.event.ActionEvent;

public final class DigitalClock extends Parent {

    private final Calendar calendar = Calendar.getInstance();
    private final Digit[] digits;

    public DigitalClock(Color onColor, Color offColor, double sizeRatio) {
        // create effect for on LEDs
        Glow onEffect = new Glow(1.7f);
        onEffect.setInput(new InnerShadow());
        // create effect for on dot LEDs
        Glow onDotEffect = new Glow(1.7f);
        onDotEffect.setInput(new InnerShadow(5, Color.BLACK));
        // create effect for off LEDs
        InnerShadow offEffect = new InnerShadow();
        // create digits
        digits = new Digit[7];
        for (int i = 0; i < 6; i++) {
            Digit digit = new Digit(onColor, offColor, onEffect, offEffect, sizeRatio);
            digit.setLayoutX(i * 80 * sizeRatio + ((i + 1) % 2) * 20 * sizeRatio);
            digits[i] = digit;
            getChildren().add(digit);
        }
        // create dots
        Group dots = new Group(
                new Circle((80 + 54 + 20) * sizeRatio, 44 * sizeRatio, 6 * sizeRatio, onColor),
                new Circle((80 + 54 + 17) * sizeRatio, 64 * sizeRatio, 6 * sizeRatio, onColor),
                new Circle(((80 * 3) + 54 + 20) * sizeRatio, 44 * sizeRatio, 6 * sizeRatio, onColor),
                new Circle(((80 * 3) + 54 + 17) * sizeRatio, 64 * sizeRatio, 6 * sizeRatio, onColor));
        dots.setEffect(onDotEffect);
        getChildren().add(dots);
        // update digits to current time and start timer to update every second
        refreshClocks();
        play();
    }

    private void refreshClocks() {
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        digits[0].showNumber(hours / 10);
        digits[1].showNumber(hours % 10);
        digits[2].showNumber(minutes / 10);
        digits[3].showNumber(minutes % 10);
        digits[4].showNumber(seconds / 10);
        digits[5].showNumber(seconds % 10);
    }

    public void play() {
        // wait till start of next second then start a timeline to call refreshClocks() every second
        Timeline delayTimeline = new Timeline();
        delayTimeline.getKeyFrames().add(
                new KeyFrame(new Duration(1000 - (System.currentTimeMillis() % 1000)), (ActionEvent event) -> {
                    Timeline everySecond = new Timeline();
                    everySecond.setCycleCount(Timeline.INDEFINITE);
                    everySecond.getKeyFrames().add(
                            new KeyFrame(Duration.seconds(1), (ActionEvent event1) -> {
                                refreshClocks();
                            }));
                    everySecond.play();
                })
        );
        delayTimeline.play();
    }

}
