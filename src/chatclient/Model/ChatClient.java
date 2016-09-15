package chatclient.Model;

import javafx.geometry.NodeOrientation;
import javafx.scene.Scene;
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
    private static final int PORT_SERVER = 12345;
    private String serverName;

    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;

    private TextArea outputArea;

    private boolean connected = false;

    public ChatClient(String serverName,TextArea outputArea){
        this.serverName = serverName;
        this.outputArea = outputArea;
    }

    public void start() throws Exception {
        if (serverName == null) System.exit(0);

        try{
            outputArea.setText("Connecting to "+serverName+"...\n");
            InetAddress serverAddress = InetAddress.getByName(serverName);
            socket = new Socket(serverAddress, PORT_SERVER);
            connected = true;
            this.out = new PrintWriter(socket.getOutputStream(),true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Thread(){
                @Override
                public void run(){
                    recive();
                }
            }.run();


        }finally {
            try {
                if (socket != null) {
                    socket.close();
                    outputArea.appendText("\n"+"Connection closed.");
                }

            } catch (Exception e) {
                System.out.println(e);
                outputArea.appendText("\n"+"Connection lost.");
            }
            try {
                if (in != null) in.close();
            } catch (Exception e) {
                System.out.println(e);
            }
            try {
                if (out != null) out.close();
            }catch (Exception e){
                System.out.println(e);
            }

        }
    }

    public void send(String message) throws IOException{
        if (message.equals("/exit")){
            connected = false;
        }
        try{
            this.out.println(message);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    private void recive(){
        try{
            while (connected){
                String input = in.readLine();
                printMessage(input);
            }
        }catch(IOException e){
            System.out.println(e);
            // Stänga?
        }
    }

    private void printMessage(String message){
        synchronized (outputArea){
            outputArea.appendText(message);
        }

    }
}
