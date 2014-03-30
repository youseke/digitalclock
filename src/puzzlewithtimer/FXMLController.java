/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzlewithtimer;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author tohtetsu
 */
public class FXMLController implements Initializable {

    private Timeline timeline;
    @FXML
    Button startButton;
    @FXML
    Button resultButton;
    @FXML
    AnchorPane mainPane;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // load puzzle image
        Image image = new Image(getClass().getResourceAsStream(
                "PuzzlePieces-picture.jpg"));
        int numOfColumns = (int) (image.getWidth() / Piece.SIZE);
        int numOfRows = (int) (image.getHeight() / Piece.SIZE);

        // create desk
        final Desk desk = new Desk(numOfColumns, numOfRows);

        // create puzzle pieces
        final List<Piece> pieces = new ArrayList<Piece>();
        for (int col = 0; col < numOfColumns; col++) {
            for (int row = 0; row < numOfRows; row++) {
                int x = col * Piece.SIZE;
                int y = row * Piece.SIZE;
                final Piece piece = new Piece(image, x, y, row > 0, col > 0,
                        row < numOfRows - 1, col < numOfColumns - 1,
                        desk.getWidth(), desk.getHeight());
                pieces.add(piece);
            }
        }
        desk.getChildren().addAll(pieces);
        mainPane.getChildren().add(desk);
        
        
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (timeline != null) {
                    timeline.stop();
                }
                timeline = new Timeline();
                for (final Piece piece : pieces) {
                    piece.setActive();
                    double shuffleX = Math.random()
                            * (desk.getWidth() - Piece.SIZE + 48f)
                            - 24f - piece.getCorrectX();
                    double shuffleY = Math.random()
                            * (desk.getHeight() - Piece.SIZE + 30f)
                            - 15f - piece.getCorrectY();
                    timeline.getKeyFrames().add(
                            new KeyFrame(Duration.seconds(1),
                                    new KeyValue(piece.translateXProperty(), shuffleX),
                                    new KeyValue(piece.translateYProperty(), shuffleY)));
                }
                timeline.playFromStart();
            }
        });

        resultButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (timeline != null) {
                    timeline.stop();
                }
                timeline = new Timeline();
                for (final Piece piece : pieces) {
                    piece.setInactive();
                    timeline.getKeyFrames().add(
                            new KeyFrame(Duration.seconds(1),
                                    new KeyValue(piece.translateXProperty(), 0),
                                    new KeyValue(piece.translateYProperty(), 0)));
                }
                timeline.playFromStart();
            }
        });
    }
}
