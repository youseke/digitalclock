package puzzlewithtimer.model;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 * Node that represents the playing area/ desktop where the puzzle pices sit
 */
public class Desk extends Pane {

    public Desk(int numOfColumns, int numOfRows) {
        setStyle("-fx-background-color: #cccccc; "
                + "-fx-border-color: #464646; "
                + "-fx-effect: innershadow( two-pass-box , rgba(0,0,0,0.8) , 15, 0.0 , 0 , 4 );");
        double DESK_WIDTH = Piece.SIZE * numOfColumns;
        double DESK_HEIGHT = Piece.SIZE * numOfRows;
        setPrefSize(DESK_WIDTH, DESK_HEIGHT);
        setMaxSize(DESK_WIDTH, DESK_HEIGHT);
        autosize();
        // create path for lines
        Path grid = new Path();
        grid.setStroke(Color.rgb(70, 70, 70));
        getChildren().add(grid);
        // create vertical lines
        for (int col = 0; col < numOfColumns - 1; col++) {
            grid.getElements().addAll(
                    new MoveTo(Piece.SIZE + Piece.SIZE * col, 5),
                    new LineTo(Piece.SIZE + Piece.SIZE * col, Piece.SIZE * numOfRows - 5)
            );
        }
        // create horizontal lines
        for (int row = 0; row < numOfRows - 1; row++) {
            grid.getElements().addAll(
                    new MoveTo(5, Piece.SIZE + Piece.SIZE * row),
                    new LineTo(Piece.SIZE * numOfColumns - 5, Piece.SIZE + Piece.SIZE * row)
            );
        }
    }

    @Override
    protected void layoutChildren() {
    }
}
