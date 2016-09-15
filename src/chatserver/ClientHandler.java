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
    private String nickName = "unnamed";

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

            receive();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            server.disconnectClient(this);
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
                if(received != null)
                    processMessage(received);
                else{
                    break;
                }
            }
        }catch(Exception e){
            System.out.println("Error when receiving from client: " + getNickname());
        }finally {
            server.disconnectClient(this);
        }
    }


    /**
     * Updated with a new command-system. Makes commands dynamic
     * @param message
     */
    private void processMessage(String message){
        String[] split = message.split(" ");

        if(message.charAt(0) == '/'){
            if(server.getCommandManager().tryExecuteCommand(split[0], server, this, message.replace(split[0],""))){
                System.out.println("Command executed.");
            }else{
                try{
                    send("Unknown command.");
                }catch(IOException e){
                    System.out.println("Could not send to client " + getNickname());
                }

            }

        }else{
            //it was a normal text
            server.broadcast(message, this);
        }
    }

    /**
     * Send a string to client.
     * @param msg The message to be sent.
     */
    public synchronized void send(String msg)throws IOException{
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

        out.println("DISCONNECT"); //Tell client that we are disconnecting.

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
