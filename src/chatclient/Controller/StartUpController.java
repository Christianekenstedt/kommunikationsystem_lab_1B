package chatclient.Controller;

/**
 * Created by chris on 2016-09-08.
 */
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class StartUpController {

    @FXML
    private Button connectBtn;

    @FXML
    void connectBtnPressed(ActionEvent event) throws IOException {
        Stage chatStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../View/chatclient.fxml"));
        chatStage.setTitle("Chat Client");
        chatStage.setScene(new Scene(root, 400, 400));
        chatStage.show();
        Stage primaryStage = (Stage) connectBtn.getScene().getWindow();
        primaryStage.close();
    }

}
