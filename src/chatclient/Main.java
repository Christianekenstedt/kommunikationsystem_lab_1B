package chatclient;

import chatclient.Controller.Controller;
import chatclient.Model.ChatClient;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("View/chatclient.fxml")
        );
        Parent root = loader.load();
        primaryStage.setTitle("Chat Client");
        primaryStage.setScene(new Scene(root, 400, 400));
        Controller controller = loader.getController();
        ChatClient chatClient = new ChatClient(controller);
        controller.setChatClient(chatClient);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(chatClient != null)
                    chatClient.stop();
            }
        });

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
