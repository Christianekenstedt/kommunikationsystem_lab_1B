package chatclient.Model;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

    private TextArea outputArea;

    public ChatClient(String serverName,TextArea outputArea){
        this.serverName = serverName;
        this.outputArea = outputArea;
    }

    public void start() throws Exception {
        if (serverName == null) System.exit(0);

        try{
            InetAddress serverAddress = InetAddress.getByName(serverName);
            socket = new Socket(serverAddress, PORT_SERVER);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input = in.readLine();
            System.out.println(input);
            outputArea.setText(input);
        }finally{
            try{
                if (socket!=null) socket.close();
                try {
                    if (in != null) in.close();
                }catch (Exception e){
                    System.out.println(e);
                }
            }catch (Exception e){
                System.out.println(e);
            }
        }
    }

    public void send() {
    }
}
