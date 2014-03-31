/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzlewithtimer.controller;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import puzzlewithtimer.model.Desk;
import puzzlewithtimer.model.Piece;

/**
 * FXML Controller class
 *
 * @author tohtetsu
 */
public class FXMLController implements Initializable {
    
    private Timeline timeline;
    @FXML
    private Button selectButton;
    @FXML
    private AnchorPane mainPane;
    private File file;
    private Desk desk;
    private List<Piece> pieces;
    private Image image;
    private int numOfColumns, numOfRows;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // load puzzle image
        image = new Image(getClass().getResourceAsStream(
                "/puzzlewithtimer/view/PuzzlePieces-picture.jpg"));
        numOfColumns = (int) (image.getWidth() / Piece.SIZE);
        numOfRows = (int) (image.getHeight() / Piece.SIZE);

        // create desk and pieces 
        desk = new Desk(numOfColumns, numOfRows);
        createPieces(numOfColumns, numOfRows, image);
        
        desk.getChildren().addAll(pieces);
        AnchorPane.setLeftAnchor(desk, 50.0);
        AnchorPane.setTopAnchor(desk, 30.0);
        mainPane.getChildren().add(desk);
    }
    
    @FXML
    public void handleQuit() {
        Platform.exit();
    }
    
    @FXML
    public void handleStart(ActionEvent event) {
        if (timeline != null) {
            timeline.stop();
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
                image = new Image(file.toURI().toURL().toExternalForm());
            } catch (MalformedURLException ex) {
                Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
            createPieces(numOfColumns, numOfRows, image);
            desk.getChildren().clear();
            desk.getChildren().addAll(pieces);
            System.out.println("Yes");
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
                image = new Image(db.getFiles().get(0).toURI().toURL().toExternalForm());
                createPieces(numOfColumns, numOfRows, image);
                desk.getChildren().clear();
                desk.getChildren().addAll(pieces);
                event.setDropCompleted(true);
            } catch (MalformedURLException ex) {
                Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            event.setDropCompleted(false);
        }
        event.consume();
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
}
