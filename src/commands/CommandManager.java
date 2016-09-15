package commands;

import chatserver.ChatServer;
import chatserver.ClientHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Manages commands registered on server
 * Created by anton on 2016-09-13.
 */
public class CommandManager {

    private Map<String,ICommand> commands = null;

    public CommandManager (){
        this.commands = new HashMap<>();

        register(new Command_Who(), "who");
        register(new Command_Quit(), "quit");
        register(new Command_Nick(), "nick");
        register(new Command_Help(), "help");
    }

    public Set<String> getCommands(){
        return commands.keySet();
    }

    public boolean register(ICommand cmd, String key){

        if(!commands.containsKey(key) && !commands.containsValue(cmd)){
            commands.put(key, cmd);
            return true;
        }
        else{
            return false;
        }
    }

    public boolean tryExecuteCommand(String key, ChatServer server, ClientHandler client, String args){
        ICommand cmd = commands.get(key.substring(1));
        if(cmd != null){
            cmd.execute(server, client, args);
            return true;
        }
        else{
            return false;
        }
    }
}
