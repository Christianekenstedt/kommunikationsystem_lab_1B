package commands;

import chatserver.ChatServer;
import chatserver.ClientHandler;

/**
 * Created by anton on 2016-09-13.
 */
public class Command_Nick  implements ICommand{

    @Override
    public void execute(ChatServer server, ClientHandler client, String args) {
        String newName = args.substring(args.indexOf(' '));
        client.setNickname(newName);
    }
}
