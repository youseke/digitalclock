package puzzlewithtimer.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author tohtetsu
 */
public class CongratsController implements Initializable {

    @FXML
    private BorderPane  b;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        b.setStyle("-fx-background-image: url('/puzzlewithtimer/view/image.jpg')");
    }

    @FXML
    public void handleOK(ActionEvent event) {
        Stage s = (Stage) ((Node) (event.getSource())).getScene().getWindow();
        s.close();
    }

}
