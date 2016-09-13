package chatserver;

import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by chris on 2016-09-08.
 */
public class ClientHandler extends Thread {

    private Socket socket = null;
    private PrintWriter out = null;
    private ChatServer server = null;
    private boolean connected = false;
    private BufferedReader in = null;
    private String nickName = "Unnamed";

    public ClientHandler(Socket socket, ChatServer server){
        this.socket = socket;
        this.server = server;
        this.connected = true;
    }

    @Override
    public void run() {
        try{

            this.out = new PrintWriter(socket.getOutputStream(),true);
            this.in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            this.out.println("Welcome "+socket.getInetAddress() + ":"+ socket.getPort());

        } catch (IOException e) {
            System.out.println(e);

        } finally {
            try{
                if(socket != null) socket.close();
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                try {
                    if (out != null) out.close();
                }catch (Exception ex){
                    System.out.println(ex);
                }
            }
        }
    }

    public void setNickname(String nick){
        this.nickName = nick;
    }

    public String getNickname(){
        return this.nickName;
    }

    /**
     * Receive until the client is no longer connected.
     */
    private void receive(){
        try{
            while(connected){
                String received = in.readLine();
                processMessage(received);
            }
        }catch(Exception e){

        }finally {
            server.disconnectClient(this);
        }
    }

    private void processMessage(String message){
        String[] split = message.split(" ");
        switch(split[0]){
            //commands
            case "/quit":
                server.disconnectClient(this);
                break;
            case "/who":
                //send all user names currently connected
                break;
            default:
                server.broadcast(message);
                //broadcast message
                break;

        }
    }

    /**
     * Updated with a new command-system. Makes commands dynamic
     * @param message
     */
    private void processMessageNew(String message){
        String[] split = message.split(" ");

        if(server.getCommandManager().tryExecuteCommand(split[0], server, this, message)){
            //it was a command
        }else{
            //it was a normal text
            server.broadcast(message);
        }
    }

    /**
     * Send a string to client.
     * @param msg The message to be sent.
     */
    public synchronized void send(String msg){
        out.println(msg);
    }

    /**
     * Do not call this method directly from this class.
     * That will cause the server to keep this client in its list.
     * Write logic to handle this situation?
     */
    public synchronized void disconnect(){
        if(connected == false){ //To protect from circular calls.
            return;
        }

        try{
            if(in != null)
                in.close();
        }catch(IOException ioe){
            System.out.println(ioe.getMessage());
        }

        if(out != null)
            out.close();

        if(socket != null){
            try{
                socket.close();
            }catch(IOException ioe){
                System.out.println(ioe.getMessage());
            }
        }
        connected = false;
    }



}
