package chatclient.Model;

import chatclient.Controller.Controller;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by chris on 2016-09-08.
 */
public class ChatClient {
    private int port;
    private String serverName;

    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;

    private boolean connected = false;

    private Controller controller = null;

    public ChatClient(Controller controller){
        this.controller = controller;
    }

    public boolean getConnected(){
        return connected;
    }

    public void start(String host) throws Exception {
        String[] split = host.split(":");
        try{
            this.serverName = split[0];
            this.port = Integer.parseInt(split[1]);
        }catch (Exception e){
            controller.addMessage("Invalid format. Usage: hostname:port");
            return;
        }

        try{
            controller.addMessage("Connecting to "+serverName+"...\n");
            InetAddress serverAddress;
            try{
                serverAddress = InetAddress.getByName(serverName);
            }catch(UnknownHostException uhe){
                controller.addMessage("Could not resolve host.");
                return;
            }

            try{
                socket = new Socket(serverAddress, port);
            }catch(Exception e){
                controller.addMessage("Could not connect to: " + host);
                return;
            }

            controller.clear();
            connected = true;
            this.out = new PrintWriter(socket.getOutputStream(),true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Thread t = new Thread(){
                @Override
                public void run(){
                    receive();
                }
            };
            t.start();



        }catch(Exception e){
            System.out.println(e.getMessage());
            stop();
        }
    }

    public void send(String message) throws IOException{
        try{
            this.out.println(message);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    private void receive(){
        try{
            while (connected){
                String received = in.readLine();
                if(received == null || received.equals("DISCONNECT")){
                    Platform.runLater(() -> stop());
                    break;
                }else{
                    Platform.runLater(() -> controller.addMessage(received));
                }

            }
        }catch(IOException e){
            System.out.println(e);
            Platform.runLater(() -> stop());
        }
    }

    public void stop(){
        //graceful close
        if(!connected){
            return;
        }

        if(out != null)
            out.println("/quit");

        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                controller.addMessage(e.getMessage());
            }

        }

        try {
            if (in != null) in.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            if (out != null) out.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        connected = false;
        controller.addMessage("Disconnected from server.");


        controller.reset();
    }
}
