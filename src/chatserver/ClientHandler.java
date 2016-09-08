package chatserver;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by chris on 2016-09-08.
 */
public class ClientHandler extends Thread {

    private Socket socket = null;
    private PrintWriter out = null;

    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try{

            out = new PrintWriter(socket.getOutputStream(),true);
            out.println("Welcome "+socket.getInetAddress() + ":"+ socket.getPort());

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



}
