package commands;

import chatserver.ChatServer;
import chatserver.ClientHandler;

/**
 * Get commands registered in CommandManager and send to the client that reqeusted them
 * Created by anton on 2016-09-13.
 */
public class Command_Help implements ICommand {
    @Override
    public void execute(ChatServer server, ClientHandler client, String args) {
        String toSend = "";
        for(String s : server.getCommandManager().getCommands()){
            toSend += s+"\n";
        }
        client.send(toSend);
    }
}
