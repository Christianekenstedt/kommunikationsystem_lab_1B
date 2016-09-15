package chatserver;

import com.sun.deploy.util.SessionState;
import commands.CommandManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Christian on 2016-09-08.
 */
public class ChatServer {
    private static final int PORT = 12345;
    private ServerSocket serverSocket = null;
    private ArrayList<ClientHandler> clients = null;
    private boolean listening = false;
    private CommandManager cmdManager = null;

    public void start() throws IOException{
        try{
            cmdManager = new CommandManager();
            clients = new ArrayList<>();
            serverSocket = new ServerSocket(PORT);

            listening = true;
            //Start listening for clients
            while(listening){
                System.out.println("Waiting for client to connect...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted client from " + clientSocket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clientHandler.start();
                clients.add(clientHandler);
            }
        }finally{
            try{
                disconnectAll();
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
            try{
                if(serverSocket != null){
                    serverSocket.close();
                }
            }catch (Exception e){
                System.out.println(e);
            }
        }
    }

    /**
     * Sends list of all connected clients to the client who requested it.
     * @param target The client who requested the list
     */
    public synchronized void whoClients(ClientHandler target){
        String clientList = "";

        for(ClientHandler ch : clients){
            clientList += ch.getNickname() + " \n";
        }

        try {
            target.send(clientList);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            disconnectClient(target);
        }
    }

    public synchronized void disconnectAll(){
        for(ClientHandler ch : clients){
            ch.disconnect();
        }
    }

    /**
     * Attempts to disconnect a client and remove it from the clienthandler list.
     * @param ch The client to disconnect
     */
    public synchronized void disconnectClient(ClientHandler ch){
        ch.disconnect();
        clients.remove(ch);
    }

    /**
     * Send a message to all connected clients
     * TODO: even the client who sent the message??
     * @param msg
     */
    public synchronized void broadcast(String msg){
        ArrayList<ClientHandler> toDisconnect = new ArrayList<>();

        for(ClientHandler ch : clients){
            try {
                ch.send(ch.getNickname() + ": " + msg);
            } catch (IOException e) {
                e.printStackTrace();
                toDisconnect.add(ch);
            }
        }

        for(ClientHandler ch : toDisconnect){
            ch.disconnect();
        }

    }

    public CommandManager getCommandManager(){
        return cmdManager;
    }

    public static void main(String[] args) throws IOException{
        ChatServer server = new ChatServer();
        server.start();
    }

}
