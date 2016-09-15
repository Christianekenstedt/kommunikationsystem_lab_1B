package commands;

import chatserver.ChatServer;
import chatserver.ClientHandler;

/**
 * Created by anton on 2016-09-13.
 */
public class Command_Quit implements ICommand {
    @Override
    public void execute(ChatServer server, ClientHandler client, String args) {
        server.disconnectClient(client);
    }

    @Override
    public String getDescription() {
        return "disconnect from server.";
    }
}
