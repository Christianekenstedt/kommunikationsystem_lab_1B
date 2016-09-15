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
        String input = inputField.getText();
        if(cc!=null){

            if(cc.getConnected() == false){ //Vi måste ansluta
                if(input.length() > 3){
                    cc.start(input);
                }else{
                    addMessage("Invalid hostname");
                }

            }else{                          //Vi är anslutna, skicka meddelande
                if(input.length() > 0)
                    cc.send(input);

            }
        }
        inputField.clear();
    }


    public void setChatClient(ChatClient cc){
        this.cc = cc;
    }

    public void addMessage(String message){
        outputArea.appendText(message + "\n");
    }

    public void reset(){
        sendBtn.setText("Connect");
        inputField.setPromptText("Address");
    }

    public void clear(){
        outputArea.clear();
        sendBtn.setText("Send");
        inputField.setPromptText("Message...");
    }

}
