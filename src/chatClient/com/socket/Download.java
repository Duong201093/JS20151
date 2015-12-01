package chatClient.com.socket;

import java.io.*;
import java.net.*;

import chatClient.com.gui.clientFrame;

public class Download implements Runnable{
    
    public ServerSocket server;
    public Socket socket;
    public int port;
    public String saveTo = "";
    public InputStream In;
    public FileOutputStream Out;
    public clientFrame gui;
    
    public Download(String saveTo, clientFrame gui){
        try {
            server = new ServerSocket(0);
            port = server.getLocalPort();
            this.saveTo = saveTo;
            this.gui = gui;
        } 
        catch (IOException ex) {
            System.out.println(" Exception [Download : Download(...)] ");
        }
    }

    @Override
    public void run() {
        try {
            socket = server.accept();
            System.out.println(" Download : "+socket.getRemoteSocketAddress());
            
            In = socket.getInputStream();
            Out = new FileOutputStream(saveTo);
            
            byte[] buffer = new byte[1024];
            int count;
            
            while((count = In.read(buffer)) >= 0){
                Out.write(buffer, 0, count);
            }
            
            Out.flush();
            
            gui.jTextArea1.append(" [Application > Me] : Download complete\n ");
            
            if(Out != null){ Out.close(); }
            if(In != null){ In.close(); }
            if(socket != null){ socket.close(); }
        } 
        catch (Exception ex) {
            System.out.println(" Exception [Download : run(...)] ");
        }
    }
}