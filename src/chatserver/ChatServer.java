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

    public ClientHandler getClientByName(String name){
        for(ClientHandler ch : clients){
            if(ch.getNickname().equals(name)){
                return ch;
            }
        }
        return null;
    }

    public ClientHandler[] getClients(){
        return (ClientHandler[]) clients.toArray(new ClientHandler[clients.size()]);
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
    public synchronized void broadcast(String msg, ClientHandler sender){
        ArrayList<ClientHandler> toDisconnect = new ArrayList<>();

        for(ClientHandler ch : clients){
            try {
                ch.send(sender.getNickname() + ": " + msg);
            } catch (IOException e) {
                e.printStackTrace();
                toDisconnect.add(ch);
            }
        }

        for(ClientHandler ch : toDisconnect){
            disconnectClient(ch);
        }

    }

    /**
     * Used to send a server announcement to all connected clients.
     * @param msg The message to send
     */
    public synchronized void announcement(String msg){
        ArrayList<ClientHandler> toDisconnect = new ArrayList<>();

        for(ClientHandler ch : clients){
            try {
                ch.send("[SERVER]" + msg);
            } catch (IOException e) {
                System.out.println("Could not send message to client " + ch.getNickname());
                toDisconnect.add(ch);
            }
        }

        for(ClientHandler ch : toDisconnect){
            disconnectClient(ch);
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
