package chatclient.Controller;

import chatclient.Model.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {

    private ChatClient cc = null;

    @FXML
    private Button sendBtn;
    @FXML
    private TextArea outputArea;
    @FXML
    private TextField inputField;


    @FXML
    void sendBtnPressed(ActionEvent event) throws Exception {
        if(cc == null){
            String serverName = inputField.getText();
            if (serverName != null) {
                cc = new ChatClient(serverName,outputArea);
                cc.start();
                sendBtn.setText("Send");
                inputField.setText("");
                inputField.setPromptText("Type message...");

            }

        }else{
            cc.send(inputField.getText());
        }

    }

}
