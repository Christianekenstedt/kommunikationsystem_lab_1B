package commands;

import chatserver.ChatServer;
import chatserver.ClientHandler;

/**
 * Vill testa att göra kommandon mer dynamiska, ej hårdkodade.
 * Created by anton on 2016-09-13.
 */
public interface ICommand {
    void execute(ChatServer server, ClientHandler client, String args);
}
