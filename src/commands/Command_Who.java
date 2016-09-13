package commands;

import chatserver.ChatServer;
import chatserver.ClientHandler;

/**
 * Who command
 * Created by anton on 2016-09-13.
 */
public class Command_Who implements ICommand{
    @Override
    public void execute(ChatServer server, ClientHandler client, String args) {
        server.whoClients(client);
    }
}
