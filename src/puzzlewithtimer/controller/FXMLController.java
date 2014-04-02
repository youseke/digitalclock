/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzlewithtimer.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import puzzlewithtimer.model.Desk;
import puzzlewithtimer.model.DigitalClock;
import puzzlewithtimer.model.Piece;

/**
 * FXML Controller class
 *
 * @author tohtetsu
 */
public class FXMLController implements Initializable {

    @FXML
    private AnchorPane mainPane;
    @FXML
    private VBox vbox;
    @FXML
    private ImageView preview;
    @FXML
    private AnchorPane bottomPane;
    @FXML
    private ImageView hint01;
    @FXML
    private ImageView hint02;
    @FXML
    private ImageView hint03;

    private Timeline timeline;
    private final DigitalClock digitalClock = new DigitalClock(Color.ORANGERED, null, 0.3);
    private File file;
    private Desk desk;
    private List<Piece> pieces;
    private Image image;
    private int numOfColumns, numOfRows;
    private ImageView[] hints;
    private int hintCounts = 3;
    private long startTime;
    private BorderPane congratsWindow;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hints = new ImageView[]{hint01, hint02, hint03};
        // load default puzzle image
        image = new Image(getClass().getResourceAsStream(
                "/puzzlewithtimer/view/default.jpg"), 550, 0, true, true);
        try {
            congratsWindow = FXMLLoader.load(getClass().getResource("/puzzlewithtimer/view/Congrats.fxml"));;
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        mainPane.setStyle("-fx-background-color: #696969;");
        vbox.setStyle("-fx-background-color: #F0F8FF;");
        bottomPane.setStyle("-fx-background-color: #F0F8FF;");
        setPuzzleWithTimer();
    }

    @FXML
    public void handleStart(ActionEvent event) {
        if (timeline != null) {
            timeline.stop();
        }
        startTime = System.currentTimeMillis();
        hintCounts = 3;
        for (ImageView hint : hints) {
            hint.setVisible(true);
        }
        timeline = new Timeline();
        pieces.stream().map((piece) -> {
            piece.setActive();
            return piece;
        }).forEach((piece) -> {
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
        });
        timeline.playFromStart();
    }

    @FXML
    public void handleResult(ActionEvent event) {
        if (timeline != null) {
            timeline.stop();
        }
        timeline = new Timeline();
        pieces.stream().map((piece) -> {
            piece.setInactive();
            return piece;
        }).forEach((piece) -> {
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(1),
                            new KeyValue(piece.translateXProperty(), 0),
                            new KeyValue(piece.translateYProperty(), 0)));
        });
        timeline.playFromStart();
    }

    @FXML
    public void handleSelect(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPGE files (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
        //Show open file dialog
        file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                image = new Image(file.toURI().toURL().toExternalForm(), 550, 0, true, true);
            } catch (MalformedURLException ex) {
                Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
            setPuzzleWithTimer();
        }
    }

    @FXML
    public void handleHint(ActionEvent event) {
        if (hintCounts == 0) {
            return;
        }
        hints[--hintCounts].setVisible(false);
        if (timeline != null) {
            timeline.stop();
        }

        for (Piece p : pieces) {
            if (p.getTranslateX() != 0 || p.getTranslateY() != 0) {
                timeline = new Timeline();
                p.setInactive();
                timeline.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(1),
                                new KeyValue(p.translateXProperty(), 0),
                                new KeyValue(p.translateYProperty(), 0)));
                timeline.playFromStart();
                break;
            }
        }
    }

    @FXML
    public void handleDragOver(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        } else {
            event.consume();
        }
    }

    @FXML
    public void handleDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
            try {
                image = new Image(db.getFiles().get(0).toURI().toURL().toExternalForm(), 550, 0, true, true);
            } catch (MalformedURLException ex) {
                Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
            setPuzzleWithTimer();
            event.setDropCompleted(true);
        } else {
            event.setDropCompleted(false);
        }
        event.consume();
    }

    @FXML
    public void handleOnMouseReleased() {
        for (Piece p : pieces) {
            if (p.getTranslateX() != 0 || p.getTranslateY() != 0) {
                return;
            }
        }
        long time = (System.currentTimeMillis() - startTime) / 1000;
        Text text = new Text();
        text.setText("Congrats!¥n You finished your game in " + time + " secs");
        text.setFont(Font.font("Verdana", 20));
        text.setFill(Color.RED);
        congratsWindow.setCenter(text);
        Stage stage = new Stage();
        stage.setScene(new Scene(congratsWindow));
        stage.show();
    }

    private void createPieces(int numOfColumns, int numOfRows, Image image) {
        // create puzzle pieces
        pieces = new ArrayList<>();
        for (int col = 0; col < numOfColumns; col++) {
            for (int row = 0; row < numOfRows; row++) {
                int x = col * Piece.SIZE;
                int y = row * Piece.SIZE;
                final Piece piece = new Piece(image, x, y, row > 0, col > 0,
                        row < numOfRows - 1, col < numOfColumns - 1,
                        Piece.SIZE * numOfColumns, Piece.SIZE * numOfColumns);
                pieces.add(piece);
            }
        }
    }

    private void setPuzzleWithTimer() {
        //calculate colums and rows
        numOfColumns = (int) (image.getWidth() / Piece.SIZE);
        numOfRows = (int) (image.getHeight() / Piece.SIZE);

        // create desk and pieces 
        desk = new Desk(numOfColumns, numOfRows);
        createPieces(numOfColumns, numOfRows, image);
        Collections.shuffle(pieces);
        desk.getChildren().addAll(pieces);
        // clear main pane
        mainPane.getChildren().clear();
        // set puzzle
        mainPane.getChildren().add(desk);
        AnchorPane.clearConstraints(desk);
        AnchorPane.setLeftAnchor(desk, 50.0);
        AnchorPane.setTopAnchor(desk, 50.0);
        mainPane.setPrefSize(desk.getWidth() + 100, desk.getHeight() + 100);
        mainPane.setMaxSize(desk.getWidth() + 100, desk.getHeight() + 100);
        // set timer
        digitalClock.setLayoutX(225);
        digitalClock.setLayoutY(10);
        digitalClock.getTransforms().add(new Scale(0.83f, 0.83f, 0, 0));
        mainPane.getChildren().add(digitalClock);
        // set preview
        preview.setImage(image);
        bottomPane.setPrefHeight(preview.getFitHeight() + 30);
        bottomPane.setMaxHeight(preview.getFitHeight() + 30);
        // set vbox
        //vbox.setPrefHeight(desk.getHeight() + preview.getFitHeight() + 130);
        //vbox.setMaxHeight(desk.getHeight() + preview.getFitHeight() + 130);
    }
}
