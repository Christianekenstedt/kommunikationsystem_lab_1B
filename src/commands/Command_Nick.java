package commands;

import chatserver.ChatServer;
import chatserver.ClientHandler;

import java.io.IOException;

/**
 * Created by anton on 2016-09-13.
 */
public class Command_Nick  implements ICommand{

    @Override
    public void execute(ChatServer server, ClientHandler client, String args) {
        String newName = args.trim();


        if(newName.length()> 3){
            if(server.getClientByName(newName) == null){
                server.announcement(" User '" + client.getNickname() + "' changed their name to '" + newName + "'");
                client.setNickname(newName);
            }else{
                try {
                    client.send("SERVER: Name is already taken.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }else{
            try {
                client.send("SERVER: Nickname must be at least 3 characters.");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public String getDescription() {
        return "Change nickname. Usage '/quit name'";
    }
}
