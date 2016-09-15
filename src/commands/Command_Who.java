package commands;

import chatserver.ChatServer;
import chatserver.ClientHandler;

import java.io.IOException;

/**
 * Who command
 * Created by anton on 2016-09-13.
 */
public class Command_Who implements ICommand{
    @Override
    public void execute(ChatServer server, ClientHandler client, String args) {

        String clientList = "Clients connected:\n";

        for(ClientHandler ch : server.getClients()){
            clientList += "\t" + ch.getNickname() + " \n";
        }

        try {
            client.send(clientList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            server.disconnectClient(client);
        }
    }

    @Override
    public String getDescription() {
        return "show a list of all connected clients.";
    }
}
