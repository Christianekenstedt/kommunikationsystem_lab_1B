package chatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Christian on 2016-09-08.
 */
public class ChatServer {
    private static final int PORT = 12345;
    ServerSocket serverSocket = null;

    public void start() throws IOException{
        try{
            serverSocket = new ServerSocket(PORT);
            //Start listending after clients and thread it up.
            while(true){
                System.out.println("Waiting for client to connect...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted client from "+clientSocket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        }finally{
            try{
                if(serverSocket != null) serverSocket.close();
            }catch (Exception e){
                System.out.println(e);
            }
        }
    }

    public static void main(String[] args) throws IOException{
        ChatServer server = new ChatServer();
        server.start();
    }

}
